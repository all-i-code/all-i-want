'''
#
# File: rpc_user.py
# Description: Handler for get_current_user RPC to ensure user is logged in
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

import os
from json import dumps
from google.appengine.api import users
from google.appengine.ext import webapp
from urlparse import urlparse
from urllib import urlencode
from core.model import User

class UserRpc(webapp.RequestHandler):
    def get(self):
        fn_name = os.path.basename(self.request.path)
        fn = getattr(self, fn_name)
        fn()
   
    def get_current_user(self):
        _ = lambda x: self.request.get(x, None)
        e, n, ui, li, lo = (None, None, None, None, None)
        user = users.get_current_user()
        
        if user is None:
            li = users.create_login_url(_('url'))
        else:
            o = urlparse(self.request.url)
            base_url = o.scheme + '://' + o.netloc + '/'
            e, n, ui  = user.email(), user.nickname(), user.user_id()
            lo = users.create_logout_url(base_url + '#Goodbye:')
        d = dict(email=e, nickname=n, user_id=ui, login_url=li, logout_url=lo)
        self.dump(User(**d).to_json_dict())

    def dump(self, json_result):
        self.response.headers['Content-Type'] = 'application/json'
        self.response.out.write(dumps(json_result))
