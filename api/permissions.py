"""
#
# File: permissions.py
# Description: Module for defining Permissions API Resource
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

from api.meta import Resource
from core.model import (
    ListPermission,
    Success,
)


class Permissions(Resource):
    """ Permissions resource"""

    def get_permission(self, permission_id):
        """Fetch a single Permission

        :param permission_id: db id of the permission
        :type permission_id: int
        """
        db_perm = self.db.get_permission(permission_id)
        permission = ListPermission.from_db(db_perm)
        self.dump(permission)

    def list_permissions(self):
        """Fetch all matching permissions"""
        owner_id = self.request.get('ownerId', None)
        email = self.request.get('email', None)

        db_perms = []
        if owner_id is not None:
            owner = self.db.get_owner(int(owner_id))
            if owner is None:
                self.abort(400, 'Invalid ownerId "{}"'.format(owner_id))
            db_perms = owner.permissions
        elif email is not None:
            db_perms = self.db.get_permissions_by_email(email)
        else:
            # We only support filtering by either owner_id or email
            self.abort(400, 'One of "ownerId" or "email" are required.')
        self.dump([ListPermission.from_db(db_perm) for db_perm in db_perms])

    def get(self, resource_id=None):
        """ Fetch permission(s) """

        if resource_id is not None:
            self.get_permission(int(resource_id))
        else:
            self.list_permissions()

    def post(self, resource_id=None):
        """Create a new permission"""
        if resource_id is not None:
            # We don't currently support updating a permission
            self.abort(400, 'Update not supported')

        content = json.loads(self.request.body)
        owner_id = content.get('ownerId', None)
        email = content.get('email', None)
        owner = self.db.get_owner(owner_id) if owner_id else None

        if owner is None or email is None:
            self.abort(400, 'Valid "ownerId" and "email" are required.')

        self.db.add_permission(owner, email)
        self.dump(Success(message='Permission added.'))

    def delete(self, resource_id=None):
        """Delete a permission"""

        if resource_id is None:
            self.abort(400, 'You cannot delete all Permissions')

        db_perm = self.db.get_permission(int(resource_id))
        self.db.delete(db_perm)
        self.dump(Success(message='Permission deleted.'))
