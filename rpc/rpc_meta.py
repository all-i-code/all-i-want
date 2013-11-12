'''
#
# File: rpc_meta.py
# Description: Module for meta info when defining RPCs
#
# Copyright 2011-2013 Adam Meadows
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
'''

import os.path
from simplejson import dumps
from google.appengine.api import users
from google.appengine.ext import webapp
from core.model import FailureReport
from core.util import camelize
from access import DbAccess
from ae import Wrapper

class RpcReqHandler(webapp.RequestHandler):

    @classmethod
    def get_rpc_group_cls(cls):
        return getattr(cls, 'group_cls', None)

    def __init__(self):
        group_cls = self.get_rpc_group_cls()
        self.rpc_group = group_cls(DbAccess(), Wrapper())
        super(RpcReqHandler, self).__init__()

    def post(self):
        self.process_req()

    def get(self):
        self.process_req()

    def process_req(self):
        rpc_name = os.path.basename(self.request.path)
        self.user = users.get_current_user()
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
        _ = lambda x: x.to_json_dict() if isinstance(x, Model) else x
        is_list = result.__class__ == list
        result_json = [ _(o) for o in result ] if is_list else _(result)
        self.response.headers['Content-Type'] = 'application/json'
        self.response.out.write(dumps(result_json))

    def handle_exception(self, exception, debug_mode):
        import traceback, sys
        nm = lambda e: e.__class__.__name__
        tb = '\n'.join(traceback.format_exception(*sys.exc_info()))
        d = dict(error_type=nm(exception), message=str(exception), traceback=tb)
        self.response.set_status(500)
        self.dump(FailureReport(**d))

class RpcGroupManager(object):
    groups = []

    @classmethod
    def register(cls, group):
        if group.is_abstract(): return
        assert(group not in cls.groups)
        cls.groups.append(group)

    @classmethod
    def get_groups(cls):
        return cls.groups

class RpcGroupMeta(type):
    '''Meta class for RPC group classes'''
    def __new__(cls, class_name, bases, class_dict):
        nc = type.__new__(cls, class_name, bases, class_dict)
        RpcGroupManager.register(nc)
        for rpc in nc.get_rpcs():
            rpc.group_name = nc.get_name()
            nc.rpc_dict[rpc.get_name()] = rpc
        return nc

class Rpc(object):
    '''Class to represent a remote procedure call'''
    def __init__(self, name='', params=(), rt=None, event=None, rt_array=False):
        tp = lambda x: x.split('.')[-1] if x is not None else x
        pkg = lambda x: '.'.join(x.split('.')[:-1]) if x is not None else x
        self.name = name
        self.params = params
        self.return_type = tp(rt)
        self.event = tp(event)
        self.rt_array = rt_array
        self.imports = (pkg(rt), pkg(event))
        if not rt_array: return
        self.imports += (
            'java.util.ArrayList',
            'com.google.gwt.core.client.JsArray'
        )

    def get_name(self):
        return self.name

    def get_group_name(self):
        '''Get the group name assigned to this rpc by parent group'''
        return self.group_name

    def get_java_name(self):
        return camelize(self.name, trailing=True)

    def get_java_iface(self):
        return self.get_java_prototype() + ';'

    def get_java_prototype(self):
        _ = lambda x: '%s %s' % (x.get_java_type(), x.get_java_name())
        nm = self.get_java_name()
        return 'void %s(%s)' (nm, ', '.join(( _(p) for p in self.params )))

    def get_java_method(self):
        _ = lambda x, n=2: ' '*n + x
        proto, gn = (self.get_java_prototype(), self.get_group_name())
        rt, evt = (self.return_type, self.event)
        template = '@Override\npublic %s {\n' +\
            _('String url = "/rpc/%s/%s";\n') +\
            _('String params = "";\n')
        method = template % (proto, gn, self.name)
        for p in self.params:
            pstr = 'RpcUtil.encode("%s", %s);\n' % (p.name, p.get_java_name())
            method += _(pstr)

        method += _('request.sendPost(url, params, new Request.Handler() {\n')
        method += _('public void onSuccess(String result) {\n',4)
        if rt_array:
            t = 'ArrayList<%s> al = new ArrayList<%s>();\n'
            method += _(t % (rt, rt),6)
            t = 'JsArray<%sJs> jsa = %sJs.decodeArray(result);\n'
            method += _(t % (rt, rt),6)
            t = 'for (int i = 0; i < jsa.length(); i++) al.add(jsa.get(i));\n'
            method += _(t,6)
            method += _('eventBus.fireEvent(new %s(ar));\n' % evt,6)
        else:
            method += _('%s data = %sJs.decode(result);\n' % (rt, rt),6)
            method += _('eventBus.fireEvent(new %s(data));\n' % evt,6)

        method += _('} // onSuccess //\n',4)
        method += _('} // sendPost //\n')
        return method + '} // %s //' % self.get_java_name()

    def validate_params(self, req):
        '''Validate the parameters in the given request object'''
        _ = lambda x: req.get(x, None)
        for p in self.params:
            if _(p.name) is None:
                raise Exception('Missing required parameter [%s]' % p.name)

        for arg in req.arguments():
            if arg not in [ p.name for p in self.params ]:
                raise Exception('Invalid parameter: [%s]' % arg)

    def get_param_values(self, handler):
        '''Get the parameter values from the given request'''
        req = handler.request
        self.validate_params(req)
        _ = lambda p: p.get_value(req.get(p.name))
        params = dict( (p.name, _(p)) for p in self.params )
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
        '''Call the given rpc with the given request object'''
        f = getattr(self, rpc_name)
        rpc = self.get_rpc(rpc_name)
        params = rpc.get_param_values(handler)
        return f(**params)

