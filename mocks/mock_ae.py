"""
#
# File: mock_ae.py
# Description: Dummy wrapper for app engine services
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
"""

from ae import AppEngine
from mocks.mock_models import User


class MockAppEngine(AppEngine):

    def __init__(self):
        self.msg = {}
        self.user = User()

    def create_login_url(self, url):
        return 'LOGIN: %s' % url

    def create_logout_url(self, url):
        return 'LOGOUT: %s' % url

    def get_current_user(self):
        return self.user

    def send_mail_from(self, sender, to, subject, body):
        self.msg = dict(f=sender, t=to, s=subject, b=body)

    def set_current_user(self, user):
        self.user = user
