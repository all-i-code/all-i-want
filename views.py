'''
#
# File: views.py
# Description: Module for meta info when defining views
#
# Copyright 2011 Adam Meadows
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

class ReqHandler(webapp.RequestHandler):

    def __init__(self):
        super(ReqHandler, self).__init__()

    def get(self):
        self.process_req()

    def process_req(self):
        view_name = os.path.basename(self.request.path)
        # TODO: special case if view_name is '' 
        self.user = users.get_current_user()
        if self.user is None:
            # TODO: redirect to login?
            self.response.out.write('500')
            self.response.set_status(500)
            return
        else:
            self.user.is_admin = users.is_current_user_admin()


        fn = getattr(self, view_name, None)
        if fn is None:
            # TODO: display 404 message
            self.response.out.write('404')
            self.response.set_status(404)
            return
        fn()
        return

    def main(self):
        self.response.out.write('main')

    def handle_exception(self, exception, debug_mode):
        # TODO: display 500 message
        import traceback, sys
        nm = lambda e: e.__class__.__name__
        tb = '\n'.join(traceback.format_exception(*sys.exc_info()))
        d = dict(error_type=nm(exception), message=str(exception), traceback=tb)
        self.response.set_status(500)
        return

