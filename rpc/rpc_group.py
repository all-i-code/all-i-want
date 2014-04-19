"""
#
# File: rpc_group.py
# Description:
#   Handler for all Group RPCs (setting up Groups and inviting members)
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

from rpc.rpc_meta import RpcGroupBase, RpcReqHandler, Rpc
from rpc.rpc_params import (
    RpcParamString as String,
    RpcParamInt as Integer,
)
from core.model import Group, GroupInvitation, ListOwner
from core.exception import PermissionDeniedError, DuplicateNameError


class GroupRpcGroup(RpcGroupBase):
    rpcs = (
        Rpc(name='add_group', params=(String('name'), String('desc'),)),
        Rpc(name='get_groups'),
        Rpc(name='update_group',
            params=(Integer('id'), String('name'), String('desc'),)),
        Rpc(name='delete_group', params=(Integer('group_id'),)),
        Rpc(name='get_invitations'),
        Rpc(name='invite_member',
            params=(Integer('group_id'), String('email'),)),
        Rpc(name='leave_group', params=(Integer('group_id'),)),
        Rpc(name='accept_invitation', params=(Integer('invite_id'),)),
        Rpc(name='decline_invitation', params=(Integer('invite_id'),)),
        Rpc(name='get_available_owners', params=(Integer('owner_id'),)),
    )

    def _verify_owner(self):
        self.owner = self.db.get_owner_by_user(self.db.user)
        if self.owner is None:
            raise PermissionDeniedError()

    def add_group(self, name, desc):
        self._verify_owner()
        if not self.db.user.is_admin:
            raise PermissionDeniedError()

        if not self.db.is_group_name_unique(name):
            raise DuplicateNameError(Group, name)

        g = self.db.add_group(name, desc, self.owner)
        self.db.add_group_member(g, self.owner)
        return Group.from_db(g)

    def get_groups(self):
        self._verify_owner()
        if self.db.user.is_admin:
            groups = self.db.get_groups()
        else:
            groups = [g for g in self.owner.groups]
            groups.extend([m.group for m in self.owner.memberships])
        return [Group.from_db(g) for g in groups]

    def update_group(self, id, name, desc):
        self._verify_owner()
        g = self.db.get_group(id)

        if not self.db.is_group_name_unique(name, g.key()):
            raise DuplicateNameError(Group, name)

        g.name = name
        g.description = desc
        return Group.from_db(g.put())

    def delete_group(self, group_id):
        self._verify_owner()
        g = self.db.get_group(group_id)
        if g.owner.key().id() != self.owner.key().id():
            raise PermissionDeniedError()
        self.db.delete(g)
        return []

    def invite_member(self, group_id, email):
        self._verify_owner()
        g = self.db.get_group(group_id)
        self.db.add_group_invite(g, email)
        return []

    def get_invitations(self):
        self._verify_owner()
        if self.db.user.is_admin:
            invites = self.db.get_group_invites()
        else:
            invites = self.db.get_group_invites(self.owner.email)
        return [GroupInvitation.from_db(db) for db in invites]

    def accept_invitation(self, invite_id):
        self._verify_owner()
        i = self.db.get_group_invite(invite_id)
        m = self.db.add_group_member(i.group, self.owner)
        m.put()
        self.db.delete(i)
        return []

    def leave_group(self, group_id):
        self._verify_owner()
        g = self.db.get_group(group_id)
        gm = self.db.get_group_member(self.owner, g)
        self.db.delete(gm)
        return []

    def decline_invitation(self, invite_id):
        self._verify_owner()
        i = self.db.get_group_invite(invite_id)
        self.db.delete(i)
        return []

    def get_available_owners(self, owner_id):
        self._verify_owner()
        # TODO: optimize this to minimize queries
        owner = self.db.get_owner(owner_id)
        gs, gms = (owner.groups, owner.memberships)
        owners = [owner]
        owners.extend(m.member for g in gs for m in g.members)
        owners.extend(m.member for gm in gms for m in gm.group.members)
        unique_owners = []
        oids = []
        for owner in owners:
            if owner.key().id() not in oids:
                oids.append(owner.key().id())
                unique_owners.append(owner)
        return [ListOwner.from_db(o) for o in unique_owners]


class GroupRpcReqHandler(RpcReqHandler):
    group_cls = GroupRpcGroup
