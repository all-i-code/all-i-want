"""
#
# File: meta.py
# Description: Module for meta API handler
#
# Copyright 2014 Adam Meadows
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
import sys
import traceback

import webapp2
from webob.exc import (
    HTTPException,
)
from google.appengine.api import users

from access import DbAccess
from ae import Wrapper
from core.meta import Model
from core.model import FailureReport


class ApiHandler(webapp2.RequestHandler):

    def __init__(self, *args, **kwargs):
        self.db = kwargs.pop('db') if 'db' in kwargs else DbAccess()
        self.ae = kwargs.pop('ae') if 'ae' in kwargs else Wrapper()
        self.user = users.get_current_user()
        if self.user is not None:
            self.db.user = self.user
            self.user.is_admin = users.is_current_user_admin()
            self.owner = self.db.get_owner_by_user(self.user)
        super(ApiHandler, self).__init__(*args, **kwargs)

    def dump(self, result):
        _ = lambda x: x.to_json_dict() if isinstance(x, Model) else x
        is_list = result.__class__ == list
        result_json = [_(o) for o in result] if is_list else _(result)
        self.response.headers['Content-Type'] = 'application/json'
        self.response.out.write(json.dumps(result_json))

    def handle_exception(self, exception, debug_mode):

        status_code = 500
        if isinstance(exception, HTTPException):
            status_code = exception.code

        nm = lambda e: e.__class__.__name__
        tb = '\n'.join(traceback.format_exception(*sys.exc_info()))
        d = dict(
            error_type=nm(exception),
            message=str(exception),
            traceback=tb,
        )
        self.response.set_status(status_code)
        self.dump(FailureReport(**d))
