'''
#
# File: test_rpc_list.py
# Description: Unit tests for rpc_list module
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
'''

import unittest

from core.exception import (
    UserVisibleError, DuplicateNameError, PermissionDeniedError,
)
from rpc.rpc_list import ListRpcGroup
from tests.ut_access import DummyAccess
from tests.ut_ae import DummyWrapper
from tests.ut_models import User


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
        for i in xrange(count):
            self.create_list(oid, i, 2 * i)

    def compare_all_lists(self, lists, wlists):
        self.assertEqual(len(lists), len(wlists))
        for l, wl in zip(lists, wlists):
            self.compare_lists(l, wl)

    def compare_lists(self, l, wl):
        l_items, wl_items = (l.items, wl.items)
        self.assertEqual(len(l_items), len(wl_items))
        for item, witem in zip(l_items, wl_items):
            self.compare_items(item, witem)

    def compare_items(self, item, witem):
        ga, eq = (getattr, self.assertEqual)
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
        self.assertEqual(1, len(self.db.lists))
        l = self.db.lists.values()[0]
        self.assertTrue(l.key().id() in range(1, 1001))
        self.assertEqual('List Name', l.name)
        self.assertEqual('List Desc', l.description)

    def test_add_list_no_owner(self):
        '''
        Confirm that trying to add a list with no owner raises a
        PermissionDeniedError
        '''
        oid = self.delete_owner()
        with self.assertRaises(PermissionDeniedError):
            self.rpc.add_list(oid, 'Name', 'Desc')

    def test_add_list_other_owner(self):
        '''
        Confirm that trying to add a list for another owner raises a
        PermissionDeniedError
        '''
        o = self.db.add_owner(User('foo', 'foo@email.com'))
        oid = o.key().id()
        with self.assertRaises(PermissionDeniedError):
            self.rpc.add_list(oid, 'Name', 'Desc')

    def test_add_list_duplicate_name(self):
        '''
        Confirm that trying to add a list with a duplicate name raises
        a DuplicateNameError
        '''
        oid = self.db.owner.key().id()
        name = 'My Wish List'
        self.db.add_list(oid, name, 'Description')
        with self.assertRaises(DuplicateNameError):
            self.rpc.add_list(oid, name, 'Another List Description')

    def test_update_list(self):
        '''
        Confirm that update_list actually updates a list
        '''
        oid = self.db.owner.key().id()
        l = self.db.add_list(oid, 'Name', 'Description')
        lid = l.key().id()
        self.rpc.update_list(lid, 'New Name', 'New Desc')
        self.assertEqual(1, len(self.db.lists))
        l = self.db.lists.values()[0]
        self.assertEqual('New Name', l.name)
        self.assertEqual('New Desc', l.description)

    def test_update_list_no_owner(self):
        '''
        Confirm that trying to update a list with no owner raises a
        PermissionDeniedError
        '''
        o = self.db.add_owner(User('foo', 'foo@email.com'))
        l = self.db.add_list(o.key().id(), 'List Name', 'List Desc')
        lid = l.key().id()
        self.delete_owner()
        with self.assertRaises(PermissionDeniedError):
            self.rpc.update_list(lid, 'Name', 'Desc')

    def test_update_list_invalid_owner(self):
        '''
        Confirm that trying to add a list for an invalid owner raises a
        PermissionDeniedError
        '''
        o = self.db.add_owner(User('foo', 'foo@email.com'))
        oid = o.key().id()
        l = self.db.add_list(oid, 'New List', 'New Desc')
        lid = l.key().id()
        with self.assertRaises(PermissionDeniedError):
            self.rpc.update_list(lid, 'Name', 'Desc')

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
        with self.assertRaises(DuplicateNameError):
            self.rpc.update_list(lid, name, 'Another List Description')

    def test_get_list_no_owner(self):
        '''
        Confirm that trying to get lists  with no owner raises a
        PermissionDeniedError
        '''
        o = self.db.add_owner(User('foo', 'foo@email.com'))
        oid = o.key().id()
        self.delete_owner()
        with self.assertRaises(PermissionDeniedError):
            self.rpc.get_lists(oid)

    def test_get_lists_invalid_owner(self):
        '''
        Confirm that trying to get lists for an invalid owner raises
        PermissionDeniedError
        '''
        o = self.db.add_owner(User('foo', 'foo@email.com'))
        oid = o.key().id()
        with self.assertRaises(PermissionDeniedError):
            self.rpc.get_lists(oid)

    def test_get_own_lists(self):
        '''
        Confirm that get_lists works for current owner
        '''
        oid = self.db.owner.key().id()
        _ = lambda x: self.db.add_list(oid, 'List %s' % x, 'Desc %s' % x)
        lists = [ _(i) for i in '12345' ]
        wls = self.rpc.get_lists(oid)
        self.assertEqual(len(lists), len(wls))

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
        o1 = self.db.add_owner(User('foo', 'foo@email.com'))
        o2 = self.db.add_owner(User('bar', 'bar@email.com'))
        self.create_lists(o1, 5)
        self.create_group(o2, [o1, self.db.owner])
        oid = o1.key().id()
        wls = self.rpc.get_lists(oid)
        self.compare_all_lists(o1.lists, wls)
        pass

    def test_get_lists_from_owner_of_group_im_in(self):
        '''
        Confirm that get_lists works for an owner who is the owner of
        a group current owner is in
        '''
        o = self.db.add_owner(User('foo', 'foo@email.com'))
        self.create_lists(o, 5)
        self.create_group(o, [self.db.owner])
        oid = o.key().id()
        wls = self.rpc.get_lists(oid)
        self.compare_all_lists(o.lists, wls)

    def test_add_item_to_invalid_owner(self):
        '''
        Confirm that trying to add an item to the list of an invalid owner
        raises PermissionDeniedError
        '''
        o = self.db.add_owner(User('foo', 'foo@email.com'))
        self.create_lists(o, 5)
        self.create_group(o, [self.db.owner])
        lid = o.lists[0].key().id()
        with self.assertRaises(PermissionDeniedError):
            self.rpc.add_item(lid, 'item', 'cat', 'desc', 'url', False)

    def test_add_surprise_item_to_invalid_owner(self):
        '''
        Confirm that trying to add a surprise item to the list of an invalid
        owner raises PermissionDeniedError
        '''
        o = self.db.add_owner(User('foo', 'foo@email.com'))
        self.create_lists(o, 5)
        lid = o.lists[0].key().id()
        with self.assertRaises(PermissionDeniedError):
            self.rpc.add_item(lid, 'item', 'cat', 'desc', 'url', True)

    def test_add_surprise_item_for_fellow_group_memeber(self):
        '''
        Confirm ability to add a surprise item to a list you have access to
        '''
        o = self.db.add_owner(User('foo', 'foo@email.com'))
        self.create_lists(o, 5)
        self.create_group(o, [self.db.owner])
        l = o.lists[2]
        count = len(l.items)
        lid = l.key().id()
        w_list = self.rpc.add_item(lid, 'item', 'cat', 'desc', 'url', True)
        witem = w_list.items[-1]
        self.assertEqual(count + 1, len(o.lists[2].items))
        self.assertEqual('item', witem.name)
        self.assertEqual('cat', witem.category)
        self.assertEqual('desc', witem.description)
        self.assertEqual('url', witem.url)
        self.assertEqual(True, witem.is_surprise)
        self.assertEqual(self.db.owner.nickname, witem.reserved_by)

    def test_add_item_to_own_list(self):
        '''
        Confirm ability to add an item to your own list
        '''
        self.create_lists(self.db.owner, 1)
        l = self.db.owner.lists[0]
        count = len(l.items)
        lid = l.key().id()
        w_list = self.rpc.add_item(lid, 'item', 'cat', 'desc', 'url', False)
        witem = w_list.items[0]
        self.assertEqual(count + 1, len(self.db.owner.lists[0].items))
        self.assertEqual('item', witem.name)
        self.assertEqual('cat', witem.category)
        self.assertEqual('desc', witem.description)
        self.assertEqual('url', witem.url)
        self.assertEqual(False, witem.is_surprise)
        self.assertEqual('', witem.reserved_by)
        self.assertEqual('', witem.purchased_by)

    def test_update_item_in_own_list(self):
        '''
        Confirm ability to update an existing item in your own list
        '''
        self.create_lists(self.db.owner, 2)
        l = self.db.owner.lists[1]
        count = len(l.items)
        item = l.items[0]
        iid = item.key().id()
        n, c, d, u = ('New Name', 'New Cat', 'New Desc', 'New URL')
        w_list = self.rpc.update_item(iid, n, c, d, u)
        witem = w_list.items[0]
        self.assertEqual(count, len(self.db.owner.lists[1].items))
        self.assertEqual(n, witem.name)
        self.assertEqual(c, witem.category)
        self.assertEqual(d, witem.description)
        self.assertEqual(u, witem.url)

    def test_update_item_in_invalid_list(self):
        ''''
        Confirm that trying to update an item in someone else's list
        raises PermissionDeniedError
        '''
        o = self.db.add_owner(User('foo', 'foo@email.com'))
        self.create_lists(o, 5)
        self.create_group(o, [self.db.owner])
        item = o.lists[2].items[1]
        iid = item.key().id()
        with self.assertRaises(PermissionDeniedError):
            self.rpc.update_item(iid, 'item', 'cat', 'desc', 'url')

    def test_remove_item_from_own_list(self):
        '''
        Confirm ability to remove an item from your own list
        '''
        self.create_lists(self.db.owner, 5)
        l = self.db.owner.lists[3]
        count = len(l.items)
        item = l.items[0]
        iid = item.key().id()
        self.rpc.remove_item(iid)
        self.assertEqual(count - 1, len(self.db.owner.lists[3].items))
        self.assertTrue(item not in self.db.owner.lists[3].items)

    def test_remove_reserved_item_from_own_list(self):
        '''
        Confirm ability to remove an item from your own list
        which has previously been marked as reserved, and that an
        email is sent to the reserver
        '''
        self.create_lists(self.db.owner, 5)
        l = self.db.owner.lists[-1]
        count = len(l.items)
        item = l.items[0]
        iid = item.key().id()
        o = self.db.add_owner(User('foo', 'foo@email.com'))
        item.reserved_by = o
        item.put()
        self.rpc.remove_item(iid)
        self.assertEqual(count - 1, len(self.db.owner.lists[-1].items))
        owner = self.db.owner
        subject = 'Wish List Item Deleted'
        template_tuple = (
            item.reserved_by.nickname,
            owner.nickname, owner.email, item.name, l.name, 'Reserved',
            owner.nickname, owner.email,
        )
        body = self.ae.DELETED_ITEM_TEMPLATE % template_tuple
        msg = dict(f=self.ae.FROM_ADDRESS, t=o.email, s=subject, b=body)
        self.assertEqual(msg, self.ae.msg)

    def test_remove_purchased_item_from_own_list(self):
        '''
        Confirm ability to remove an item from your own list
        which has previously been marked as purchased, and that an
        email is sent to the purchaser
        '''
        self.create_lists(self.db.owner, 5)
        l = self.db.owner.lists[-1]
        count = len(l.items)
        item = l.items[0]
        iid = item.key().id()
        o = self.db.add_owner(User('foo', 'foo@email.com'))
        item.purchased_by = o
        item.put()
        self.rpc.remove_item(iid)
        self.assertEqual(count - 1, len(self.db.owner.lists[-1].items))
        owner = self.db.owner
        subject = 'Wish List Item Deleted'
        template_tuple = (
            item.purchased_by.nickname,
            owner.nickname, owner.email, item.name, l.name, 'Purchased',
            owner.nickname, owner.email,
        )
        body = self.ae.DELETED_ITEM_TEMPLATE % template_tuple
        msg = dict(f=self.ae.FROM_ADDRESS, t=o.email, s=subject, b=body)
        self.assertEqual(msg, self.ae.msg)

    def test_remove_item_from_invalid_list(self):
        '''
        Confirm that trying to remove an item from someone else's
        list raises PermissionDeniedError
        '''
        o = self.db.add_owner(User('foo', 'foo@email.com'))
        self.create_lists(o, 5)
        self.create_group(o, [self.db.owner])
        iid = o.lists[2].items[1].key().id()
        with self.assertRaises(PermissionDeniedError):
            self.rpc.remove_item(iid)

    def test_reserve_item(self):
        '''
        Confirm ability to reserve an item from list you can access
        '''
        o = self.db.add_owner(User('foo', 'foo@email.com'))
        self.create_lists(o, 5)
        self.create_group(o, [self.db.owner])
        item = o.lists[-1].items[-1]
        iid = item.key().id()
        self.assertEqual(None, item.reserved_by)
        self.assertEqual(None, item.purchased_by)
        self.rpc.reserve_item(iid)
        self.assertEqual(self.db.owner, item.reserved_by)
        self.assertEqual(None, item.purchased_by)

    def test_reserve_reserved_item(self):
        '''
        Confirm trying to reserve a reserved item raises UserVisibleError
        '''
        o = self.db.add_owner(User('foo', 'foo@email.com'))
        o2 = self.db.add_owner(User('bar', 'bar@email.com'))
        self.create_lists(o, 5)
        self.create_group(o, [self.db.owner])
        item = o.lists[-1].items[-1]
        iid = item.key().id()
        item.reserved_by = o2
        item.put()
        with self.assertRaises(UserVisibleError):
            self.rpc.reserve_item(iid)

    def test_reserve_puchased_item(self):
        '''
        Confirm trying to reserve a purchased item raises UserVisibleError
        '''
        o = self.db.add_owner(User('foo', 'foo@email.com'))
        o2 = self.db.add_owner(User('bar', 'bar@email.com'))
        self.create_lists(o, 5)
        self.create_group(o, [self.db.owner])
        item = o.lists[-1].items[-1]
        iid = item.key().id()
        item.purchased_by = o2
        item.put()
        with self.assertRaises(UserVisibleError):
            self.rpc.reserve_item(iid)

    def test_unreserve_item(self):
        '''
        Confirm ability to unreserve and item previously reserved by you
        '''
        o = self.db.add_owner(User('foo', 'foo@email.com'))
        self.create_lists(o, 5)
        self.create_group(o, [self.db.owner])
        item = o.lists[-1].items[-1]
        item.reserved_by = self.db.owner
        item.put()
        iid = item.key().id()
        self.assertEqual(self.db.owner, item.reserved_by)
        self.assertEqual(None, item.purchased_by)
        self.rpc.unreserve_item(iid)
        self.assertEqual(None, item.reserved_by)
        self.assertEqual(None, item.purchased_by)

    def test_unreserve_item_not_reserved_by_you(self):
        '''
        Confirm that trying to unrserve an item reserved by someone else
        raises a UserVisibleError
        '''
        o = self.db.add_owner(User('foo', 'foo@email.com'))
        o2 = self.db.add_owner(User('bar', 'bar@email.com'))
        self.create_lists(o, 5)
        self.create_group(o, [self.db.owner])
        item = o.lists[-1].items[-1]
        iid = item.key().id()
        item.reserved_by = o2
        item.put()
        with self.assertRaises(UserVisibleError):
            self.rpc.unreserve_item(iid)

    def test_reserve_invalid_item(self):
        '''
        Confirm trying to reserve an item you don't have access to raises
        PermissionDeniedError
        '''
        o = self.db.add_owner(User('foo', 'foo@email.com'))
        self.create_lists(o, 5)
        item = o.lists[2].items[1]
        iid = item.key().id()
        with self.assertRaises(PermissionDeniedError):
            self.rpc.reserve_item(iid)

    def test_unreserve_invalid_item(self):
        '''
        Confirm trying to unreserve an item you don't have access to raises
        PermissionDeniedError
        '''
        o = self.db.add_owner(User('foo', 'foo@email.com'))
        self.create_lists(o, 5)
        item = o.lists[2].items[1]
        iid = item.key().id()
        with self.assertRaises(PermissionDeniedError):
            self.rpc.unreserve_item(iid)

    def test_purchase_item(self):
        '''
        Confirm ability to purchase an item from list you can access
        '''
        o = self.db.add_owner(User('foo', 'foo@email.com'))
        self.create_lists(o, 5)
        self.create_group(o, [self.db.owner])
        item = o.lists[-1].items[-1]
        iid = item.key().id()
        self.assertEqual(None, item.reserved_by)
        self.assertEqual(None, item.purchased_by)
        self.rpc.purchase_item(iid)
        self.assertEqual(None, item.reserved_by)
        self.assertEqual(self.db.owner, item.purchased_by)

    def test_purchase_reserved_item(self):
        '''
        Confirm trying to purchase a reserved item raises UserVisibleError
        '''
        o = self.db.add_owner(User('foo', 'foo@email.com'))
        o2 = self.db.add_owner(User('bar', 'bar@email.com'))
        self.create_lists(o, 5)
        self.create_group(o, [self.db.owner])
        item = o.lists[-1].items[-1]
        iid = item.key().id()
        item.reserved_by = o2
        item.put()
        with self.assertRaises(UserVisibleError):
            self.rpc.purchase_item(iid)

    def test_purchase_puchased_item(self):
        '''
        Confirm trying to purchase a purchased item raises UserVisibleError
        '''
        o = self.db.add_owner(User('foo', 'foo@email.com'))
        o2 = self.db.add_owner(User('bar', 'bar@email.com'))
        self.create_lists(o, 5)
        self.create_group(o, [self.db.owner])
        item = o.lists[-1].items[-1]
        iid = item.key().id()
        item.purchased_by = o2
        item.put()
        with self.assertRaises(UserVisibleError):
            self.rpc.purchase_item(iid)

    def test_purchase_invalid_item(self):
        '''
        Confirm trying to purchase an item you don't have access to raises
        PermissionDeniedError
        '''
        o = self.db.add_owner(User('foo', 'foo@email.com'))
        self.create_lists(o, 5)
        item = o.lists[2].items[1]
        iid = item.key().id()
        with self.assertRaises(PermissionDeniedError):
            self.rpc.purchase_item(iid)

    def test_unpurchase_invalid_item(self):
        '''
        Confirm trying to unpurchase an item you don't have access to raises
        PermissionDeniedError
        '''
        o = self.db.add_owner(User('foo', 'foo@email.com'))
        self.create_lists(o, 5)
        item = o.lists[2].items[1]
        iid = item.key().id()
        with self.assertRaises(PermissionDeniedError):
            self.rpc.unpurchase_item(iid)

    def test_unpurchase_item(self):
        '''
        Confirm ability to unpurchase an item you previously purchased
        '''
        o = self.db.add_owner(User('foo', 'foo@email.com'))
        self.create_lists(o, 5)
        self.create_group(o, [self.db.owner])
        item = o.lists[-1].items[-1]
        item.purchased_by = self.db.owner
        item.put()
        iid = item.key().id()
        self.assertEqual(None, item.reserved_by)
        self.assertEqual(self.db.owner, item.purchased_by)
        self.rpc.unpurchase_item(iid)
        self.assertEqual(None, item.reserved_by)
        self.assertEqual(None, item.purchased_by)

    def test_unpurchase_item_purchased_by_someone_else(self):
        '''
        Confirm trying to unpurchase an item purchased by someone else
        raises a UserVisibleError
        '''
        o = self.db.add_owner(User('foo', 'foo@email.com'))
        o2 = self.db.add_owner(User('bar', 'bar@email.com'))
        self.create_lists(o, 5)
        self.create_group(o, [self.db.owner])
        item = o.lists[-1].items[-1]
        iid = item.key().id()
        item.purchased_by = o2
        item.put()
        with self.assertRaises(UserVisibleError):
            self.rpc.unpurchase_item(iid)


if __name__ == '__main__':
    unittest.main()
