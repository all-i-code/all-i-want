"""
#
# File: meta.py
# Description: Module for meta API Resource handlers
#
# Copyright 2014-2015 Adam Meadows
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

import json
import logging
import sys
import traceback

import webapp2
from webapp2_extras import routes
from webob.exc import HTTPException

from access import DbAccess
from ae import AppEngine
from core.exception import PermissionDeniedError
from core.meta import Model
from core.model import FailureReport


def require_owner(func):
    """Decorate a Resource instance method to require a user to be an owner

    :param func: instance method to decorate
    :type func: function

    :returns: the decorated instance method
    """
    def wrapped(self, *args, **kwargs):
        if not self.owner:
            raise PermissionDeniedError()
        return func(self, *args, **kwargs)
    return wrapped


def require_admin(func):
    """Decorate a Resource method to require user to be admin

    :param func: instance method to decorate
    :type func: function

    :returns: the decorated instance method
    """
    def wrapped(self, *args, **kwargs):
        if not self.user.is_admin:
            raise PermissionDeniedError()
        return func(self, *args, **kwargs)
    return wrapped


def to_object(json_dict):
    """Convert a JSON dictionary to a Python object

    The key/value pairs of the dictionary will be mapped into attributes
    of the Python object

    :param json_dict: the JSON dictionary
    :type json_dict: dict

    :returns: a Python object with attribute name/vals matching dict key/vals
    """
    class SimpleObject(object):
        def __init__(self, **kwargs):
            for key, value in kwargs.iteritems():
                setattr(self, key, value)

    return SimpleObject(**json_dict)


class Resource(webapp2.RequestHandler):
    """Special request handler for a REST Resource"""

    @classmethod
    def get_name(cls):
        """Return URI-friendly name of this resource"""
        return getattr(cls, 'name', cls.__name__.lower())

    @classmethod
    def get_methods(cls):
        """Return a list of custom methods on this resource (if any)

        Custom methods are returned as tuples of the form:
        (uri_path, method_name)
        """
        return getattr(cls, 'custom_methods', [])

    @classmethod
    def get_routes(cls):
        """Return a PathPrefixRoute for this resource"""

        # By default, everyone gets the /objects and /objects/<id> route
        sub_routes = [
            webapp2.Route('', handler=cls),
            webapp2.Route('/<resource_id:(\d+)>', handler=cls),
        ]

        # add any custom routes as needed
        for uri_path, method_name in cls.get_methods():
            logging.info('adding /{} -> {}'.format(uri_path, method_name))
            route = webapp2.Route('/{}'.format(uri_path),
                                  handler=cls,
                                  handler_method=method_name)
            sub_routes.append(route)

        logging.info('/{} routes = {}'.format(cls.get_name(), sub_routes))
        return routes.PathPrefixRoute('/{}'.format(cls.get_name()), sub_routes)

    def __init__(self, *args, **kwargs):
        """Initialize the Resource with access to db & ae

        All Resource instances have access to...
            - the current user (self.user)
            - a wrapper to access the DataStore (self.db)
            - a wrapper to access the App Engine API (self.ae)

        """
        self.db = kwargs.pop('db') if 'db' in kwargs else DbAccess()
        self.ae = kwargs.pop('ae') if 'ae' in kwargs else AppEngine()
        self.user = self.ae.get_current_user()
        if self.user is not None:
            self.db.user = self.user
            self.user.is_admin = self.ae.is_current_user_admin()
            self.owner = self.db.get_owner_by_user(self.user)
        super(Resource, self).__init__(*args, **kwargs)

    def parse_json(self, json_string):
        """Parse the given JSON string into an object with properties

        :param json_string: The original JSON string
        :type json_string: str

        :returns: A Python object whose properties map to the JSON dict keys
        """
        return json.loads(json_string, object_hook=to_object)

    def dump(self, result):
        """Write the given result to the response object as JSON

        :param result: The result to dump
        :type result: core.Model or JSON serializable data type
        """

        def _jsd(x):
            return x.to_json_dict() if isinstance(x, Model) else x

        is_list = result.__class__ == list
        result_json = [_jsd(o) for o in result] if is_list else _jsd(result)
        self.response.headers['Content-Type'] = 'application/json'
        self.response.out.write(json.dumps(result_json))

    def handle_exception(self, exception, debug_mode):
        """Exception handler for Resource

        :param exception: The exception being handled
        :type exception: Exception

        :param debug_mode: Flag for if we're in debug mode or not
        :type debug_mode: bool.

        If the exception is an HTTPException, pass along the appropriate
        status_code, otherwise use a generic 500

        Wrap the exception as a FailureReport model and send that as JSON
        in the response.
        """

        status_code = 500
        if isinstance(exception, HTTPException):
            status_code = exception.code

        tb = '\n'.join(traceback.format_exception(*sys.exc_info()))
        d = dict(
            error_type=exception.__class__.__name__,
            message=str(exception),
            traceback=tb,
        )
        self.response.set_status(status_code)
        self.dump(FailureReport(**d))
