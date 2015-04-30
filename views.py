"""
#
# File: views.py
# Description: URL handlers for html/js pages
#
# Copyright 2012 Adam Meadows
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
import webapp2
from google.appengine.api import users
from google.appengine.ext.webapp import template


class PageHandler(webapp2.RequestHandler):
    def __init__(self, *args, **kwargs):
        super(PageHandler, self).__init__(*args, **kwargs)
        self.user = users.get_current_user()
        self.user.is_admin = users.is_current_user_admin()

    def render_template(self, template_name, data=None):
        data = data or {}
        data.setdefault('user', self.user)
        data.setdefault('logout_url', users.create_logout_url('/'))
        my_path = os.path.dirname(__file__)
        template_path = os.path.join(my_path, 'templates/%s' % template_name)
        return template.render(template_path, data)


class ListPage(PageHandler):
    def get(self):
        data = {'main': 'list-main.js'}
        self.response.out.write(self.render_template('index.html', data))


class SettingsPage(PageHandler):
    def get(self):
        data = {'main': 'settings-main.js'}
        self.response.out.write(self.render_template('index.html', data))


class NotFoundPage(PageHandler):
    def get(self):
        self.error(404)
        self.response.out.write(self.render_template('404.html'))


class GoodbyePage(PageHandler):
    def get(self):
        self.response.out.write(self.render_template('bye.html'))
