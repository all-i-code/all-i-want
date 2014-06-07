"""
#
# File: test_owners.py
# Description: Unit tests for Owners Resource
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

import unittest

from api.tests.base import ResourceTest
from api.owners import Owners
from mocks.mock_models import User


class OwnersTest(ResourceTest):
    resource_cls = Owners

    def Xtest_get_owners(self):
        """
        Verify ability to list all owners
        """
        # Make sure the owner exists
        owner = self.db.add_owner(self.user)
        oid = owner.key().id()
        lo = self.rpc.get_owner(oid)

        self.assertEqual(oid, lo.id)
        self.assertEqual(owner.email, lo.email)
        self.assertEqual(owner.nickname, lo.nickname)
        self.assertEqual(owner.name, lo.name)

    def Xtest_get_owner(self):
        """
        Verify ability to lookup owner by id
        """
        # Make sure the owner exists
        owner = self.db.add_owner(self.user)
        oid = owner.key().id()
        lo = self.rpc.get_owner(oid)

        self.assertEqual(oid, lo.id)
        self.assertEqual(owner.email, lo.email)
        self.assertEqual(owner.nickname, lo.nickname)
        self.assertEqual(owner.name, lo.name)

    def Xtest_update_owner(self):
        """
        Make sure user can change name and nickname of list owner
        """
        # Make sure the owner exists
        self.set_user(User('John Doe', 'Johnny'))
        self.db.add_owner(self.user)
        owner = self.db.add_owner(self.user)
        self.assertFalse(owner.saved())

        self.rpc.update_owner(owner.key().id(), 'Joe Smith', 'Joe')

        self.assertTrue(owner.saved())
        self.assertEqual('Joe Smith', owner.name)
        self.assertEqual('Joe', owner.nickname)


if __name__ == '__main__':
    unittest.main()
