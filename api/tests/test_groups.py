"""
#
# File: test_groups.py
# Description: Unit tests for Groups resource
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
import unittest

from api.groups import Groups
from api.tests.base import ResourceTest
from core.exception import (
    DuplicateNameError,
    PermissionDeniedError,
    UserVisibleError,
)
from mocks.mock_models import User


class GroupsTest(ResourceTest):
    resource_cls = Groups
    user_owner = True

    def _setup_groups(self):
        # Create some groups owned by current user
        self.db.add_group(name='Avengers', description='Marvel Heroes')
        self.db.add_group(name='X-Men', description='Mutant Heroes')

        # Create some groups owned by another user
        thanos_user = User(nickname='Thanos', email='thanos@evil.com')
        thanos_owner = self.db.add_owner(thanos_user)
        self.db.add_group(name='Sinister Six',
                          description='Doctor Octopus and Crew',
                          owner=thanos_owner)
        self.db.add_group(name='Brotherhood of Mutants',
                          description='Magneto and Company',
                          owner=thanos_owner)

        # Create another group that the current user is a member of
        stan_lee_user = User(nickname='Stan Lee', email='stan.lee@marvel.com')
        stan_lee_owner = self.db.add_owner(stan_lee_user)
        marvel_universe = self.db.add_group(name='Marvel Universe',
                                            description='Everyone Marvel',
                                            owner=stan_lee_owner)
        self.db.add_group_member(marvel_universe)

    def test_create_no_owner(self):
        """Can't create if not owner"""
        self.resource.owner = None
        with self.assertRaises(PermissionDeniedError):
            self.resource.post()

    def test_create_duplicate_name(self):
        """Can't create with duplicate name"""
        self._setup_groups()
        json_dict = dict(name='Avengers', description='Superheroes')
        self.request.body = json.dumps(json_dict)
        with self.assertRaises(DuplicateNameError):
            self.resource.post()

    def test_create(self):
        """Create a new group"""
        description = (
            'Strategic Homeland Intervention, '
            'Enforcement and Logistics Division'
        )
        json_dict = dict(name='S.H.I.E.L.D.', description=description)
        self.request.body = json.dumps(json_dict)

        self.resource.post()

        group = self.db.groups[self.db.group_ids[-1]]
        self.assertEqual(group.name, 'S.H.I.E.L.D.')
        self.assertEqual(group.description, description)

        resp = self.resource.last_response
        self.assertEqual(resp.id, group.key().id())
        self.assertEqual(resp.name, group.name)
        self.assertEqual(resp.description, group.description)

    def test_get_no_owner(self):
        """Can't list groups when not owner"""
        self.resource.owner = None
        with self.assertRaises(PermissionDeniedError):
            self.resource.get()

    def test_get_list_admin(self):
        """Admin lists all groups"""
        self._setup_groups()
        self.resource.user.is_admin = True
        self.resource.get()
        resp = self.resource.last_response
        self.assertEqual(len(resp), 5)
        actual_names = [group.name for group in resp]
        expected_names = [
            'Avengers',
            'X-Men',
            'Brotherhood of Mutants',
            'Sinister Six',
            'Marvel Universe',
        ]
        self.assertEqual(sorted(actual_names), sorted(expected_names))

    def test_get_list_non_admin(self):
        """Admin lists all groups"""
        self._setup_groups()
        self.resource.user.is_admin = False
        self.resource.get()
        resp = self.resource.last_response
        self.assertEqual(len(resp), 3)
        actual_names = [group.name for group in resp]
        expected_names = [
            'Avengers',
            'X-Men',
            'Marvel Universe',
        ]
        self.assertEqual(sorted(actual_names), sorted(expected_names))

    def test_get_own_group(self):
        """Fetch a group you own by ID"""
        self._setup_groups()
        group_id = self.db.group_ids[0]
        self.resource.get(resource_id=str(group_id))

        resp = self.resource.last_response
        self.assertEqual(resp.name, 'Avengers')

    def test_get_member_group(self):
        """Fetch a group you're a member of by ID"""
        self._setup_groups()
        group_id = self.db.group_ids[-1]
        self.resource.get(resource_id=str(group_id))

        resp = self.resource.last_response
        self.assertEqual(resp.name, 'Marvel Universe')

    def test_get_other_group_non_admin(self):
        """Can't fetch a group you're not member of"""
        self._setup_groups()
        self.user.is_admin = False
        group_id = self.db.group_ids[2]
        with self.assertRaises(PermissionDeniedError):
            self.resource.get(resource_id=str(group_id))

    def test_get_other_group_admin(self):
        """Can't fetch a group you're not member of"""
        self._setup_groups()
        self.user.is_admin = True
        group_id = self.db.group_ids[2]
        self.resource.get(resource_id=str(group_id))

        resp = self.resource.last_response
        self.assertEqual(resp.name, 'Sinister Six')

    def test_update_no_owner(self):
        """Can't update if not owner"""
        self.resource.owner = None
        with self.assertRaises(PermissionDeniedError):
            self.resource.put()

    def test_non_admin_update_other_group(self):
        """Can't update someone else's group if not an admin"""
        self._setup_groups()
        self.user.is_admin = False
        group_id = self.db.group_ids[-1]
        self.request.body = json.dumps(dict(name='New Name',
                                            description='n/a'))
        with self.assertRaises(PermissionDeniedError):
            self.resource.put(str(group_id))

    def test_admin_update_other_group(self):
        """Admin can update someone else's group"""
        self._setup_groups()
        self.user.is_admin = True
        group_id = self.db.group_ids[-1]
        self.request.body = json.dumps(dict(name='New Name',
                                            description='n/a'))
        self.resource.put(str(group_id))
        self.assertEqual(self.db.groups[group_id].name, 'New Name')

    def test_non_admin_update_own_group(self):
        """Non admin user can update his/her own groups"""
        self._setup_groups()
        self.user.is_admin = False
        group_id = self.db.group_ids[0]
        self.request.body = json.dumps(dict(name='Super Squad',
                                            description='n/a'))
        self.resource.put(str(group_id))
        group = self.db.get_group(group_id)
        self.assertEqual(group.name, 'Super Squad')
        self.assertEqual(group.description, 'n/a')

    def test_no_duplicate_names(self):
        """Can't update someone else's group if not an admin"""
        self._setup_groups()
        self.user.is_admin = True
        group_id = self.db.group_ids[-1]
        self.request.body = json.dumps(dict(name='Avengers',
                                            description='n/a'))
        with self.assertRaises(DuplicateNameError):
            self.resource.put(str(group_id))

    def test_delete(self):
        """Delete your own group"""
        self._setup_groups()
        self.assertEqual(len(self.db.groups), 5)
        group_id = self.db.group_ids[0]
        self.assertTrue(group_id in self.db.groups)

        self.resource.delete(group_id)

        self.assertEqual(len(self.db.groups), 4)
        self.assertFalse(group_id in self.db.groups)

    def test_delete_other_group_admin(self):
        """Delete someone else's group (as admin)"""
        self._setup_groups()
        self.user.is_admin = True
        self.assertEqual(len(self.db.groups), 5)
        group_id = self.db.group_ids[-1]
        self.assertTrue(group_id in self.db.groups)

        self.resource.delete(group_id)

        self.assertEqual(len(self.db.groups), 4)
        self.assertFalse(group_id in self.db.groups)

    def test_delete_other_group_non_admin(self):
        """Delete someone else's group (as non-admin)"""
        self._setup_groups()
        self.user.is_admin = False
        group_id = self.db.group_ids[-1]

        with self.assertRaises(PermissionDeniedError):
            self.resource.delete(group_id)

    def test_leave_group(self):
        """Leave a group"""
        self._setup_groups()
        # Last group should be the one we're just a member of
        group_id = self.db.group_ids[-1]
        self.assertEqual(len(self.resource.owner.memberships), 1)
        self.resource.leave(str(group_id))
        self.assertEqual(len(self.resource.owner.memberships), 0)

    def test_leave_own_group(self):
        """Can't leave a group you own"""
        self._setup_groups()
        # First group is one we own
        group_id = self.db.group_ids[0]
        with self.assertRaises(UserVisibleError):
            self.resource.leave(str(group_id))

if __name__ == '__main__':
    unittest.main()
