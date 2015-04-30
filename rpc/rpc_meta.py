"""
#
# File: rpc_meta.py
# Description: Module for meta info when defining RPCs
#
# Copyright 2011-2015 Adam Meadows
#
#    Licensed under the Apache License, Version 2.0 (the "License");
#    you may not use this file except in compliance with the License.
#    You may obtain a copy of the License at
#
#        http://www.apache.org/licenses/LICENSE-2.0
#
#    Unless required by applicable law or agreed to in writing, software
#    distributed under the License is distributed on an "AS IS" BASIS,
#    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#    See the License for the specific language governing permissions and
#    limitations under the License.
#
"""

import os
import json
from google.appengine.api import users
from google.appengine.ext import webapp

from core.model import FailureReport
from access import DbAccess
from ae import AppEngine


class RpcReqHandler(webapp.RequestHandler):

    @classmethod
    def get_rpc_group_cls(cls):
        return getattr(cls, 'group_cls', None)

    def __init__(self):
        group_cls = self.get_rpc_group_cls()
        self.rpc_group = group_cls(DbAccess(), AppEngine())
        super(RpcReqHandler, self).__init__()

    def post(self):
        self.process_req()

    def get(self):
        self.process_req()

    def process_req(self):
        rpc_name = os.path.basename(self.request.path)
        self.user = self.ae.get_current_user()
        if self.user is None:
            # A bit of a hack to let first get the user
            if rpc_name != 'get_current_user':
                self.dump([])
                return
        else:
            self.user.is_admin = users.is_current_user_admin()

        self.rpc_group.db.user = self.user
        self.dump(self.rpc_group.call(rpc_name, self))

    def dump(self, result):
        from core.meta import Model

        def _jsd(x):
            return x.to_json_dict() if isinstance(x, Model) else x

        is_list = result.__class__ == list
        result_json = [_jsd(o) for o in result] if is_list else _jsd(result)
        self.response.headers['Content-Type'] = 'application/json'
        self.response.out.write(json.dumps(result_json))

    def handle_exception(self, exception, debug_mode):
        import sys
        import traceback

        def nm(e):
            return e.__class__.__name__

        tb = '\n'.join(traceback.format_exception(*sys.exc_info()))
        d = dict(
            error_type=nm(exception),
            message=str(exception),
            traceback=tb,
        )
        self.response.set_status(500)
        self.dump(FailureReport(**d))


class RpcGroupManager(object):
    groups = []

    @classmethod
    def register(cls, group):
        if group.is_abstract():
            return
        assert(group not in cls.groups)
        cls.groups.append(group)

    @classmethod
    def get_groups(cls):
        return cls.groups


class RpcGroupMeta(type):
    """Meta class for RPC group classes"""
    def __new__(cls, class_name, bases, class_dict):
        nc = type.__new__(cls, class_name, bases, class_dict)
        RpcGroupManager.register(nc)
        for rpc in nc.get_rpcs():
            rpc.group_name = nc.get_name()
            nc.rpc_dict[rpc.get_name()] = rpc
        return nc


class Rpc(object):
    """Class to represent a remote procedure call"""
    def __init__(self, name='', params=(), rt=None,
                 event=None, rt_array=False):

        def tp(x):
            return x.split('.')[-1] if x is not None else x

        def pkg(x):
            return '.'.join(x.split('.')[:-1]) if x is not None else x

        self.name = name
        self.params = params
        self.return_type = tp(rt)
        self.event = tp(event)
        self.rt_array = rt_array
        self.imports = (pkg(rt), pkg(event))
        if not rt_array:
            return
        self.imports += (
            'java.util.ArrayList',
            'com.google.gwt.core.client.JsArray'
        )

    def get_name(self):
        return self.name

    def get_group_name(self):
        """Get the group name assigned to this rpc by parent group"""
        return self.group_name

    def validate_params(self, req):
        """Validate the parameters in the given request object"""

        def _get(x):
            return req.get(x, None)

        for p in self.params:
            if _get(p.name) is None:
                raise Exception('Missing required parameter [%s]' % p.name)

        for arg in req.arguments():
            if arg not in [p.name for p in self.params]:
                raise Exception('Invalid parameter: [%s]' % arg)

    def get_param_values(self, handler):
        """Get the parameter values from the given request"""

        def _v(p):
            return p.get_value(req.get(p.name))

        req = handler.request
        self.validate_params(req)

        params = dict((p.name, _v(p)) for p in self.params)
        return params


class RpcGroupBase(object):
    __metaclass__ = RpcGroupMeta
    abstract = True
    rpc_dict = {}

    @classmethod
    def is_abstract(cls):
        return cls.__dict__.get('abstract', False)

    @classmethod
    def get_name(cls):
        return getattr(cls, 'name', cls.__name__.lower())

    @classmethod
    def get_rpcs(cls):
        return getattr(cls, 'rpcs', ())

    @classmethod
    def get_rpc(cls, name):
        return cls.rpc_dict.get(name, None)

    def __init__(self, db, ae):
        self.db = db
        self.ae = ae

    def call(self, rpc_name, handler):
        """Call the given rpc with the given request object"""
        f = getattr(self, rpc_name)
        rpc = self.get_rpc(rpc_name)
        params = rpc.get_param_values(handler)
        return f(**params)
