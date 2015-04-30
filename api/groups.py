"""
#
# File: groups.py
# Description: Module for defining Groups API Resource
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

from api.meta import (
    Resource,
    require_owner,
)
from core.exception import (
    DuplicateNameError,
    PermissionDeniedError,
    UserVisibleError,
)
from core.model import (
    Group as JsGroup,
)


class Groups(Resource):
    """Groups resource (users who have requested access to AIW)"""

    custom_methods = [
        ('<req_id:(\d+)>/leave', 'leave'),
    ]

    @require_owner
    def post(self):
        """Create a new group"""
        logging.info('groups::post', extra=dict(body=self.request.body))
        data = self.parse_json(self.request.body)

        if not self.db.is_group_name_unique(data.name):
            raise DuplicateNameError(JsGroup, data.name)

        group = self.db.add_group(data.name, data.description, self.owner)
        self.db.add_group_member(group, self.owner)
        self.dump(JsGroup.from_db(group))

    @require_owner
    def get(self, resource_id=None):
        """Fetch group(s)"""

        logging.info('groups::get', extra=dict(resource_id=resource_id))

        if self.user.is_admin:
            owner_groups = self.db.get_groups()
        else:
            owner_groups = [g for g in self.owner.groups]
            owner_groups.extend([m.group for m in self.owner.memberships])

        if resource_id is None:
            self.dump([JsGroup.from_db(g) for g in owner_groups])
        else:
            group = self.db.get_group(int(resource_id))
            if group not in owner_groups:
                raise PermissionDeniedError()
            self.dump(JsGroup.from_db(group))

    @require_owner
    def put(self, resource_id):
        """Update an existing resource"""
        extra = dict(resource_id=resource_id, body=self.request.body)
        logging.info('groups::put', extra=extra)
        data = self.parse_json(self.request.body)

        group = self.db.get_group(int(resource_id))

        # You can only update your own groups (unless you're an admin)
        is_own_group = group.owner.key().id() == self.owner.key().id()
        if not self.user.is_admin and not is_own_group:
            raise PermissionDeniedError()

        if not self.db.is_group_name_unique(data.name, group.key()):
            raise DuplicateNameError(JsGroup, data.name)

        group.name = data.name
        group.description = data.description
        self.dump(JsGroup.from_db(group.put()))

    @require_owner
    def delete(self, resource_id):
        """Delete an existing resource"""
        logging.info('groups::delete', extra=dict(resource_id=resource_id))
        group = self.db.get_group(int(resource_id))

        # you can only delete your own groups, unless you're an admin
        is_group_owner = group.owner.key().id() == self.owner.key().id()
        if not self.user.is_admin and not is_group_owner:
            raise PermissionDeniedError()

        self.db.delete(group)
        self.dump({})

    @require_owner
    def leave(self, resource_id):
        """Leave the group"""
        logging.info('groups::leave', extra=dict(resource_id=resource_id))

        group = self.db.get_group(int(resource_id))
        if self.owner.key().id() == group.owner.key().id():
            raise UserVisibleError('You cannot leave a group you created.')

        member = self.db.get_group_member(self.owner, group)
        self.db.delete(member)
        self.dump({})
