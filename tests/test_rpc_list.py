'''
#
# File: test_rpc_list.py
# Description: Unit tests for rpc_list module
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
from rpc.rpc_list import ListRpcGroup
from core.exception import UserVisibleError, DuplicateNameError
from tests.ut_access import DummyAccess
from tests.ut_ae import DummyWrapper
from tests.ut_models import User, ListOwner as Owner, List, ListItem as Item

class ListRpcTest(unittest.TestCase):
    
    def setUp(self):
        self.db = DummyAccess(User(), add_owner=True)
        self.ae = DummyWrapper()
        self.rpc = ListRpcGroup(self.db, self.ae)

    def set_user(self, user):
        self.db.user = user

    def set_owner(self, owner):
        self.db.owner = owner
        self.db.user = owner.user

    def test_add_list(self):
        '''
        Confirm that add_list actually adds a list
        '''
        oid = self.db.owner.key().id()
        self.rpc.add_list(oid, 'List Name', 'List Desc')
        self.assertEquals(1, len(self.db.lists))
        l = self.db.lists.values()[0]
        self.assertTrue(l.key().id() in range(1, 1001))
        self.assertEquals('List Name', l.name)
        self.assertEquals('List Desc', l.description)

    def test_add_list_duplicate_name(self):
        '''
        Confirm that trying to add a list with a duplicate name raises
        a DuplicateNameError
        '''
        oid = self.db.owner.key().id()
        name = 'My Wish List' 
        self.db.add_list(oid, name, 'Description')
        self.assertRaises(DuplicateNameError, self.rpc.add_list, oid, name,
            'Another List Description')


if __name__ == '__main__':
    unittest.main()
