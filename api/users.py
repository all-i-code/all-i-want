"""
#
# File: users.py
# Description: Module for defining Users API Handler
#
# Copyright 2011-2014 Adam Meadows
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

from api.meta import ApiHandler
from core.model import User as UserModel
from core.util import get_base_url

class CurrentUserHandler(ApiHandler):
    """ Current User Handler """

    def get(self):
        """Fetch the currently logged in User"""
        url = self.request.get('url', '/')

        if self.user is None:
            login_url = self.ae.create_login_url(url)
            model = UserModel(login_url=login_url)
            self.dump(model)
            return

        model = UserModel(
            user_id=self.user.user_id(),
            nickname=self.user.nickname(),
            email=self.user.email(),
            is_admin=self.user.is_admin,
            owner_id=self.owner.id() if self.owner else -1,
            login_url='',
            logout_url=self.ae.create_logout_url('/')
        )

        self.dump(model)
