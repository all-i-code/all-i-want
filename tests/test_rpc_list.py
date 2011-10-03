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
from core.exception import UserVisibleError, DuplicateNameError,\
    PermissionDeniedError

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

    def delete_owner(self):
        oid = self.db.owner.key().id()
        self.db.delete(self.db.owner)
        self.db.owner = None
        return oid

    def create_group(self, created_by, members, name='Group', desc='Desc'):
        g = self.db.add_group(name, desc, created_by)
        [ self.db.add_group_member(g, o) for o in members ]
        return g

    def create_list(self, oid, idx, count):
        _ = lambda x: '%s %s' % (x, idx)
        wl = self.db.add_list(oid, _('List'), _('List Desc'))
        lid = wl.key().id()
        for idx in xrange(count):
            self.db.add_list_item(lid, _('Item'), _('Cat'), _('Desc'),
                _('url'), idx % 2 == 0)

    def create_lists(self, owner, count):
        oid = owner.key().id()
        _ = lambda x: '%s %s' % (x, i)
        for i in xrange(count):
            self.create_list(oid, i, 2*i)
        
    def compare_all_lists(self, lists, wlists):
        self.assertEquals(len(lists), len(wlists))
        for l, wl in zip(lists, wlists):
            self.compare_lists(l, wl)

    def compare_lists(self, l, wl):
        l_items, wl_items = (l.items, wl.items)
        self.assertEquals(len(l_items), len(wl_items))
        for item, witem in zip(l_items, wl_items):
            self.compare_items(item, witem)

    def compare_items(self, item, witem):
        ga, eq = (getattr, self.assertEquals)
        _ = lambda x: x.label() if x is not None else '' 
        attrs = ('name', 'category', 'description', 'url', 'is_surprise')
        [ eq(ga(item, a), ga(witem, a)) for a in attrs ]
        eq(_(item.purchased_by), witem.purchased_by)
        eq(_(item.reserved_by), witem.reserved_by)

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

    def test_add_list_no_owner(self):
        '''
        Confirm that trying to add a list with no owner raises a
        PermissionDeniedError
        '''
        oid = self.delete_owner()
        self.assertRaises(PermissionDeniedError, self.rpc.add_list,
            oid, 'Name', 'Desc')

    def test_add_list_other_owner(self):
        '''
        Confirm that trying to add a list for another owner raises a
        PermissionDeniedError
        '''
        o = self.db.add_owner(User('foo', 'foo@email.com'))
        oid = o.key().id()
        self.assertRaises(PermissionDeniedError, self.rpc.add_list,
            oid, 'Name', 'Desc')

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

    def test_update_list(self):
        '''
        Confirm that update_list actually updates a list
        '''
        oid = self.db.owner.key().id()
        l = self.db.add_list(oid, 'Name', 'Description')
        lid = l.key().id()
        self.rpc.update_list(lid, 'New Name', 'New Desc')
        self.assertEquals(1, len(self.db.lists))
        l = self.db.lists.values()[0]
        self.assertEquals('New Name', l.name)
        self.assertEquals('New Desc', l.description)

    def test_update_list_no_owner(self):
        '''
        Confirm that trying to update a list with no owner raises a
        PermissionDeniedError
        '''
        o = self.db.add_owner(User('foo', 'foo@email.com'))
        l = self.db.add_list(o.key().id(), 'List Name', 'List Desc')
        lid = l.key().id()
        self.delete_owner()
        self.assertRaises(PermissionDeniedError, self.rpc.update_list,
            lid, 'Name', 'Desc')

    def test_update_list_invalid_owner(self):
        '''
        Confirm that trying to add a list for an invalid owner raises a
        PermissionDeniedError
        '''
        o = self.db.add_owner(User('foo', 'foo@email.com'))
        oid = o.key().id()
        l = self.db.add_list(oid, 'New List', 'New Desc')
        lid = l.key().id()
        self.assertRaises(PermissionDeniedError, self.rpc.update_list,
            lid, 'Name', 'Desc')

    def test_update_list_duplicate_name(self):
        '''
        Confirm that trying to update a list with a duplicate name raises
        a DuplicateNameError
        '''
        oid = self.db.owner.key().id()
        name = 'My Wish List' 
        self.db.add_list(oid, name, 'Description')
        l = self.db.add_list(oid, 'Second Name', 'Second Description')
        lid = l.key().id()
        self.assertRaises(DuplicateNameError, self.rpc.update_list, lid, name,
            'Another List Description')

    def test_get_list_no_owner(self):
        '''
        Confirm that trying to get lists  with no owner raises a
        PermissionDeniedError
        '''
        o = self.db.add_owner(User('foo', 'foo@email.com'))
        oid = o.key().id()
        self.delete_owner()
        self.assertRaises(PermissionDeniedError, self.rpc.get_lists, oid)

    def test_get_lists_invalid_owner(self):
        '''
        Confirm that trying to get lists for an invalid owner raises
        PermissionDeniedError
        '''
        o = self.db.add_owner(User('foo', 'foo@email.com'))
        oid = o.key().id()
        self.assertRaises(PermissionDeniedError, self.rpc.get_lists, oid)

    def test_get_own_lists(self):
        '''
        Confirm that get_lists works for current owner 
        '''
        oid = self.db.owner.key().id()
        _ = lambda x: self.db.add_list(oid, 'List %s' % x, 'Desc %s' % x)
        lists = [ _(i) for i in '12345' ]
        wls = self.rpc.get_lists(oid)
        self.assertEquals(len(lists), len(wls))

    def test_get_lists_from_member_of_my_group(self):
        '''
        Confirm that get_lists works for an owner who is a member of 
        a group owned by current owner
        '''
        o = self.db.add_owner(User('foo', 'foo@email.com'))
        self.create_lists(o, 5)
        self.create_group(self.db.owner, [o])
        oid = o.key().id()
        wls = self.rpc.get_lists(oid)
        self.compare_all_lists(o.lists, wls)

    def test_get_lists_from_member_of_group_im_in(self):
        '''
        Confirm that get_lists works for an owner who is a member of 
        a group current owner is also in
        '''
        pass

if __name__ == '__main__':
    unittest.main()
