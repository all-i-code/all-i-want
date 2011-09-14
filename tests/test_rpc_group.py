'''
#
# File: test_rpc_group.py
# Description: Unit tests for rpc_group module
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

import unittest
from rpc.rpc_group import GroupRpcGroup
from tests.dummy_models import Group as DummyGroup, User as DummyUser,\
    GroupInvitation as DummyInvitation, GroupMember as DummyMember

class DummyAccess(object):
    def __init__(self, user=None):
        self.groups = {}
        self.group_ids = []
        self.invitations = {}
        self.invitation_ids = []
        self.members = {}
        self.member_ids = []
        self.user = user

    def add_group(self, name, description):
        g = DummyGroup(name=name, description=description, owner=self.user)
        self.groups[g.key().id()] = g
        self.group_ids.append(g.key().id())
        return g

    def get_group(self, id):
        return self.groups[id]

    def get_groups(self):
        return (self.groups[id] for id in self.group_ids)

    def add_group_invitation(self, group, email):
        invite = DummyInvitation(group, email)
        group.invitations.append(invite)
        self.invitations[invite.key().id()] = invite
        self.invitation_ids.append(invite.key().id())
        return invite

    def get_group_invitation(self, id):
        return self.invitations[id]

    def add_group_member(self, group):
        m = DummyMember(self.user, group)
        group.members.append(m)
        self.members[m.key().id()] = m
        self.member_ids.append(m.key().id())
        return m

    def save(self, obj):
        return obj

    def delete(self, obj):
        if isinstance(obj, DummyGroup):
            self._delete_group(obj)
        elif isinstance(obj, DummyInvitation):
            self._delete_invitation(obj)
        elif isinstance(obj, DummyMember):
            self._delete_member(obj)

    def _delete_group(self, group):
        for i in group.invitations:
            self._delete_invitation(i)
        for m in group.members:
            self._delete_member(m)
        del self.groups[group.key().id()]
        self.group_ids.remove(group.key().id())

    def _delete_invitation(self, invite):
        invite.group.invitations.remove(invite)
        del self.invitations[invite.key().id()]
        self.invitation_ids.remove(invite.key().id())
        
    def _delete_member(self, member):
        member.group.members.remove(member)
        del self.members[member.key().id()]
        self.member_ids.remove(member.key().id())

class GroupRpcTest(unittest.TestCase):
    
    def setUp(self):
        self.db = DummyAccess(DummyUser())
        self.rpc = GroupRpcGroup(self.db)

    def add_groups(self, count, include_invites=False):
        for i in xrange(1, count+1):
            g = self.db.add_group('Name %s' % i, 'Desc %s' % i)
            self.assertEquals([], g.invitations)
            self.assertEquals([], g.members)
            if include_invites:
                self.db.add_group_invitation(g, 'email_%s@domain.com' % i)

    def compare_group(self, db, group):
        self.assertEquals(db.key().id(), group.id)
        self.assertEquals(db.name, group.name)
        self.assertEquals(db.description, group.description)

    def test_add_group(self):
        '''Confirm that add_group actually adds a group'''
        self.db.user.is_admin = True
        self.rpc.add_group('Group Name', 'Group Desc')
        self.assertEquals(1, len(self.db.groups))
        g = self.db.groups.values()[0]
        self.assertTrue(g.key().id() in range(1, 1001))
        self.assertEquals('Group Name', g.name)
        self.assertEquals('Group Desc', g.description)

    def test_add_group_requires_admin(self):
        '''Confirm trying to add group with non admin user raised Exception''' 
        self.assertRaises(Exception, self.rpc.add_group, 'Name', 'Desc')

    def test_get_groups(self):
        '''Confirm group retrieval'''
        self.add_groups(5)
        groups = self.rpc.get_groups()
        self.assertEquals(5, len(groups))
        for g, group in zip(self.db.get_groups(), groups):
            self.compare_group(g, group)

    def test_update_group(self):
        '''Confirm ability to change group name/desc'''
        self.add_groups(1)
        id = self.db.group_ids[0]
        self.rpc.update_group(id, 'New Name', 'New Desc')
        g = self.db.get_group(id)
        self.assertEquals('New Name', g.name)
        self.assertEquals('New Desc', g.description)

    def test_invite_member(self):
        '''Confirm ability to invite someone to a group'''
        self.add_groups(1)
        id = self.db.group_ids[0]
        group = self.db.groups[id]
        self.assertEquals(0, len(group.invitations))
        self.rpc.invite_member(id, 'address@email.com')
        self.assertEquals(1, len(group.invitations))
        invite = group.invitations[0]
        self.assertEquals('address@email.com', invite.email)

    def test_accept_invitation(self):
        '''Confirm ability to accept a group invite'''
        self.add_groups(1, include_invites=True)
        invite = self.db.invitations.values()[0]
        group = invite.group
        num_invites = len(group.invitations)
        self.rpc.accept_invitation(invite.key().id())
        self.assertEquals(num_invites - 1, len(group.invitations))
        self.assertEquals(1, len(group.members))
        self.assertEquals(self.db.user, group.members[0].member)

    def test_decline_invitation(self):
        '''Confirm ability to decline a group invitation'''
        self.add_groups(1, include_invites=True)
        invite = self.db.invitations.values()[0]
        group = invite.group
        num_invites = len(group.invitations)
        num_members = len(group.members)
        self.rpc.decline_invitation(invite.key().id())
        self.assertEquals(num_invites - 1, len(group.invitations))
        self.assertEquals(num_members, len(group.members))
        self.assertTrue(invite not in self.db.invitations.values())

if __name__ == '__main__':
    unittest.main()
