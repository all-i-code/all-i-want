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
from tests.dummy_models import Group as DummyGroup, User as DummyUser

class DummyAccess:
    def __init__(self, user=None):
        self.groups = []
        self.user = user

    def add_group(self, name, description):
        g = DummyGroup(name=name, description=description)
        self.groups.append(g)
        return g

class GroupRpcTest(unittest.TestCase):
    
    def setUp(self):
        self.db = DummyAccess(DummyUser())
        self.rpc = GroupRpcGroup(self.db)
   
    def test_add_group(self):
        '''Confirm that add_group actually adds a group'''
        self.db.user.is_admin = True
        self.rpc.add_group('Group Name', 'Group Desc')
        self.assertEquals(1, len(self.db.groups))
        g = self.db.groups[0]
        self.assertEquals('Group Name', g.name)
        self.assertEquals('Group Desc', g.description)

    def test_add_group_requires_admin(self):
        '''Confirm trying to add group with non admin user raised Exception''' 
        self.assertRaises(Exception, self.rpc.add_group, 'Name', 'Desc')

if __name__ == '__main__':
    unittest.main()
