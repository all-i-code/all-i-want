"""
#
# File: test_group.py
# Description: Unit tests for rpc_group module
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

import unittest
from core.exception import UserVisibleError, DuplicateNameError
from rpc.rpc_group import GroupRpcGroup
from mocks.mock_access import MockAccess
from mocks.mock_ae import MockWrapper
from mocks.mock_models import User


class GroupRpcTest(unittest.TestCase):

    def setUp(self):
        self.db = MockAccess(User(), add_owner=True)
        self.ae = MockWrapper()
        self.rpc = GroupRpcGroup(self.db, self.ae)

    def set_user(self, user):
        self.db.user = user

    def set_owner(self, owner):
        self.db.owner = owner
        self.db.user = owner.user

    def add_groups(self, count, include_invites=False):
        for i in xrange(1, count + 1):
            g = self.db.add_group('Name {}'.format(i), 'Desc {}'.format(i))
            self.assertEqual([], g.invitations)
            self.assertEqual([], g.members)
            if include_invites:
                self.db.add_group_invite(g, 'email_{}@domain.com'.format(i))

    def compare_group(self, db, group):
        self.assertEqual(db.key().id(), group.id)
        self.assertEqual(db.name, group.name)
        self.assertEqual(db.description, group.description)

    def setup_groups(self):
        O = self.db.add_owner
        self.a1 = O(User('a1', 'a1@email.com', is_admin=True))
        self.a2 = O(User('a2', 'a2@email.com', is_admin=True))
        self.u1 = O(User('u1', 'u1@email.com'))
        self.u2 = O(User('u2', 'u2@email.com'))
        self.u3 = O(User('u3', 'u3@email.com'))

        self.g1 = self.db.add_group('g1', 'g1desc', self.a1)
        self.g2 = self.db.add_group('g2', 'g1desc', self.a2)
        self.db.add_group_member(self.g1, self.a1)
        self.db.add_group_member(self.g1, self.u1)
        self.db.add_group_member(self.g1, self.u3)

        self.db.add_group_member(self.g2, self.a2)
        self.db.add_group_member(self.g2, self.u2)
        self.db.add_group_member(self.g2, self.u3)

    def test_add_group(self):
        """
        Confirm that add_group actually adds a group
        """
        self.db.user.is_admin = True
        self.rpc.add_group('Group Name', 'Group Desc')
        self.assertEqual(1, len(self.db.groups))
        g = self.db.groups.values()[0]
        self.assertTrue(g.key().id() in range(1, 1001))
        self.assertEqual('Group Name', g.name)
        self.assertEqual('Group Desc', g.description)

    def test_add_group_requires_admin(self):
        """
        Confirm trying to add group with non admin user raised Exception
        """
        with self.assertRaises(UserVisibleError):
            self.rpc.add_group('Name', 'Desc')

    def test_add_group_duplicate_name(self):
        """
        Confirm that trying to add a group with a duplicate name raises
        a DuplicateNameError
        """
        self.db.user.is_admin = True
        self.add_groups(1)
        name = self.db.groups.values()[0].name
        with self.assertRaises(DuplicateNameError):
            self.rpc.add_group(name, 'Group Desc')

    def test_get_groups(self):
        """
        Confirm group retrieval
        """
        self.add_groups(5)
        groups = self.rpc.get_groups()
        self.assertEqual(5, len(groups))
        for g, group in zip(self.db.get_groups(), groups):
            self.compare_group(g, group)

    def test_update_group(self):
        """
        Confirm ability to change group name/desc
        """
        self.add_groups(1)
        id = self.db.group_ids[0]
        self.rpc.update_group(id, 'New Name', 'New Desc')
        g = self.db.get_group(id)
        self.assertEqual('New Name', g.name)
        self.assertEqual('New Desc', g.description)

    def test_update_group_duplicate_name(self):
        """
        Confirm that trying to update a group with a used name raises a
        DuplicateNameError
        """
        self.add_groups(2)
        id = self.db.group_ids[0]
        name = self.db.get_group(self.db.group_ids[1]).name
        with self.assertRaises(DuplicateNameError):
            self.rpc.update_group(id, name, 'New Desc')

    def test_update_group_same_name(self):
        """
        Confirm that trying to update a group with its own name and a new
        description does NOT raise a DuplicateNameError
        """
        self.add_groups(1)
        g = self.db.groups.values()[0]
        self.rpc.update_group(g.key().id(), g.name, 'New Desc')

    def test_invite_member(self):
        """
        Confirm ability to invite someone to a group
        """
        self.add_groups(1)
        id = self.db.group_ids[0]
        group = self.db.groups[id]
        self.assertEqual(0, len(group.invitations))
        self.rpc.invite_member(id, 'address@email.com')
        self.assertEqual(1, len(group.invitations))
        invite = group.invitations[0]
        self.assertEqual('address@email.com', invite.email)

    def test_accept_invitation(self):
        """
        Confirm ability to accept a group invite
        """
        self.add_groups(1, include_invites=True)
        invite = self.db.invitations.values()[0]
        group = invite.group
        num_invites = len(group.invitations)
        self.rpc.accept_invitation(invite.key().id())
        self.assertEqual(num_invites - 1, len(group.invitations))
        self.assertEqual(1, len(group.members))
        self.assertEqual(self.db.owner, group.members[0].member)

    def test_decline_invitation(self):
        """
        Confirm ability to decline a group invitation
        """
        self.add_groups(1, include_invites=True)
        invite = self.db.invitations.values()[0]
        group = invite.group
        num_invites = len(group.invitations)
        num_members = len(group.members)
        self.rpc.decline_invitation(invite.key().id())
        self.assertEqual(num_invites - 1, len(group.invitations))
        self.assertEqual(num_members, len(group.members))
        self.assertTrue(invite not in self.db.invitations.values())

    def test_get_available_owners_a1(self):
        """
        Confirm lookup of all available users for a1 owner
        """
        self.setup_groups()
        self.set_owner(self.a1)

        owners = self.rpc.get_available_owners(self.a1.key().id())
        self.assertEqual(3, len(owners))
        expected = set((o.email for o in (self.a1, self.u1, self.u3)))
        actual = set((o.email for o in owners))
        self.assertEqual(expected, actual)

    def test_get_available_owners_a2(self):
        """
        Confirm lookup of all available users for a2 owner
        """
        self.setup_groups()
        self.set_owner(self.a2)

        owners = self.rpc.get_available_owners(self.a2.key().id())
        self.assertEqual(3, len(owners))
        expected = set((o.email for o in (self.a2, self.u2, self.u3)))
        actual = set((o.email for o in owners))
        self.assertEqual(expected, actual)

    def test_get_available_owners_u1(self):
        """
        Confirm lookup of all available users for u1 owner
        """
        self.setup_groups()
        self.set_owner(self.u1)

        owners = self.rpc.get_available_owners(self.u1.key().id())
        self.assertEqual(3, len(owners))
        expected = set((o.email for o in (self.a1, self.u1, self.u3)))
        actual = set((o.email for o in owners))
        self.assertEqual(expected, actual)

    def test_get_available_owners_u2(self):
        """
        Confirm lookup of all available users for u2 owner
        """
        self.setup_groups()
        self.set_owner(self.u2)

        owners = self.rpc.get_available_owners(self.u2.key().id())
        self.assertEqual(3, len(owners))
        expected = set((o.email for o in (self.a2, self.u2, self.u3)))
        actual = set((o.email for o in owners))
        self.assertEqual(expected, actual)

    def test_get_available_owners_u3(self):
        """
        Confirm lookup of all available users for u3 owner
        """
        self.setup_groups()
        self.set_owner(self.u3)

        owners = self.rpc.get_available_owners(self.u3.key().id())
        self.assertEqual(5, len(owners))
        all = (self.a1, self.a2, self.u1, self.u2, self.u3)
        expected = set((o.email for o in all))
        actual = set((o.email for o in owners))
        self.assertEqual(expected, actual)


if __name__ == '__main__':
    unittest.main()
