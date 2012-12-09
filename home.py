'''
#
# File: urls.py
# Description: GAE app to handle URLs
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
import webapp2
from google.appengine.api import users
from google.appengine.ext.webapp import template

class MainPage(webapp2.RequestHandler):
    def get(self):
        user = users.get_current_user()
        template_path = os.path.join(os.path.dirname(__file__), 'templates/index.html')
        data = {
            'email': user.email(),
            'username': user.nickname(),
            'is_admin': users.is_current_user_admin(),
            'logout_url': users.create_logout_url('/'),
        }
        self.response.out.write(template.render(template_path, data))

app = webapp2.WSGIApplication([('/', MainPage)], debug=True)

