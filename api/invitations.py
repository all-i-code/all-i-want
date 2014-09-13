"""
#
# File: invitations.py
# Description: Module for defining Invitations API Resource
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

import logging
import json

from api.meta import (
    Resource,
    require_owner,
)
from core.exception import (
    PermissionDeniedError,
)
from core.model import (
    GroupInvitation as JsGroupInvitation,
)


class Invitations(Resource):
    """Invitations (emails who have been invited to join AIW)"""

    custom_methods = [
        ('<req_id:(\d+)>/accept', 'accept'),
    ]

    @require_owner
    def post(self):
        """Create a new invitation"""
        logging.info('invitations::post', extra=dict(body=self.request.body))
        data = json.loads(self.request.body)

        group = self.db.get_group(data.group_id)
        db = self.db.add_group_invite(group, data.email)
        self.dump(JsGroupInvitation.from_db(db))

    @require_owner
    def get(self, resource_id=None):
        """Fetch group(s)"""

        logging.info('invitations::get', extra=dict(resource_id=resource_id))
        if resource_id is None:
            if self.user.is_admin:
                invites = self.db.get_group_invites()
            else:
                invites = self.db.get_group_invites(self.owner.email)
            self.dump([JsGroupInvitation.from_db(db) for db in invites])
        else:
            invite = self.db.get_group_invite(int(resource_id))
            self.dump(JsGroupInvitation.from_db(invite))

    @require_owner
    def delete(self, resource_id):
        """Decline an invitation"""
        extra = dict(resource_id=resource_id)
        logging.info('invitations::decline', extra=extra)
        invite = self.db.get_group_invite(int(resource_id))
        if invite.email != self.owner.email:
            raise PermissionDeniedError()
        self.db.delete(invite)
        self.dump({})

    @require_owner
    def accept(self, resource_id):
        """Accept an invitation"""
        extra = dict(resource_id=resource_id)
        logging.info('invitations::accept', extra=extra)

        invite = self.db.get_group_invite(int(resource_id))
        member = self.db.add_group_member(invite.group, self.owner)
        member.put()
        self.db.delete(invite)
        self.dump({})
