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


from api.meta import ApiHandler
from core.model import (
    User as JsUser,
    ListOwner as JsOwner,
)


class CurrentUserHandler(ApiHandler):
    """ Current User Handler """

    def get(self):
        """Fetch the currently logged in User"""

        req = None

        # If user is not yet a list owner, either submit a request
        # or, if user is an admin, create owner record
        if self.owner is None:
            if self.user.is_admin:
                self.owner = self.db.add_owner(self.user)
            else:
                req = self.db.get_req_by_user(self.user)
                if req is None:
                    req = self.db.add_req(self.user)

        js_owner = JsOwner.from_db(self.owner) if self.owner else None
        js_user = JsUser(
            user_id=self.user.user_id(),
            nickname=self.user.nickname(),
            email=self.user.email(),
            was_request_denied=req.denied if req else False,
            is_admin=self.user.is_admin,
            owner_id=js_owner.id if js_owner else -1,
            logout_url=self.ae.create_logout_url('/goodbye')
        )

        self.dump(js_user)


class OwnersHandler(ApiHandler):
    """ Owners resource handler"""

    def get(self, owner_id=None):
        """ Fetch owner """
        if owner_id is None:
            self.abort(404)

        owner = self.db.get_owner(int(owner_id))
        if owner is None:
            self.abort(404)

        js_owner = JsOwner.from_db(owner)
        self.dump(js_owner)
