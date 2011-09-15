'''
#
# File: test_rpc_user.py
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
from rpc.rpc_user import UserRpcGroup
from tests.dummy_models import User as DummyUser, ListOwner as DummyOwner

class DummyAccess(object):
    def __init__(self, user=None):
        self.user = user
        self.owners = {}
        self.user_owners = {}

    def confirm_owner(self, user):
        owner = self.user_owners.get(user, None)
        if owner is None:
            owner = DummyOwner(user)
            self.user_owners[user] = owner
            self.owners[owner.id] = owner
        return owner.id

    def get_owner(self, id):
        return self.owners[id]

    def save(self, obj):
        return obj

class DummyAeWrapper:
    def create_login_url(self, url):
        return 'LOGIN:%s' % url

    def create_logout_url(self, url):
        return 'LOGOUT:%s' % url

class UserRpcTest(unittest.TestCase):
    
    def setUp(self):
        self.set_user(DummyUser())

    def set_user(self, user):
        self.user = user
        self.db = DummyAccess(self.user)
        self.ae = DummyAeWrapper()
        self.rpc = UserRpcGroup(self.db, self.ae)

    def test_get_user_none(self):
        '''
        Verify that calling get_current_user without being logged in
        provides you with a login url 
        '''
        # Start with no user logged in 
        self.set_user(None)

        url = 'http://www.domain.com/some/extra/path/'
        user = self.rpc.get_current_user(url)
        
        # Verify nothing but login url is returned 
        self.assertEquals(None, user.logout_url)
        self.assertEquals(None, user.email)
        self.assertEquals(None, user.user_id)
        self.assertEquals(None, user.nickname)
        login_url = self.ae.create_login_url(url)
        self.assertEquals(login_url, user.login_url)

    def test_get_user(self):
        '''
        Verify that calling get_current_user while logged in provides you
        with a logout url and your login info
        '''
        from core.util import get_base_url, extract_name

        # Verify no owner exists before call
        self.assertEquals(None, self.db.user_owners.get(self.user, None))
        num_owners = len(self.db.owners)

        url = 'http://www.domain.com/some/extra/path/'
        user = self.rpc.get_current_user(url)
        
        # Verify correct user details are returned
        base = get_base_url(url)
        goodbye = '%s/#Goodbye:' % base
        self.assertEquals(self.ae.create_logout_url(goodbye), user.logout_url)
        self.assertEquals(self.user.nickname(), user.nickname)
        self.assertEquals(self.user.email(), user.email)
        self.assertEquals(None, user.login_url)
       
        # Verify owner was created correctly
        owner = self.db.user_owners.get(self.user)
        self.assertEquals(num_owners+1, len(self.db.owners))
        self.assertEquals(self.user, owner.user)
        self.assertEquals(owner.id, user.owner_id)
        self.assertEquals(self.user.nickname(), owner.nickname)
        self.assertEquals(self.user.email(), owner.email)
        self.assertEquals(extract_name(self.user.email()), owner.name)

    def test_get_user_owner_exists(self):
        '''
        Verify that a duplicate owner is not created when get_current_user
        is called
        '''
        # Make sure owner exists before call 
        owner = DummyOwner(self.user)
        self.db.owners[owner.id] = owner
        self.db.user_owners[self.user] = owner
        num_owners = len(self.db.owners)

        url = 'http://www.domain.com/some/extra/path/'
        user = self.rpc.get_current_user(url)

        # verify still the same owner after the call
        self.assertEquals(num_owners, len(self.db.owners))
        self.assertEquals(owner.id, user.owner_id)
        self.assertEquals(owner, self.db.user_owners[self.user])

    def test_get_owner(self):
        '''
        Verify ability to lookup owner by id 
        '''
        # Make sure the owner exists
        owner = DummyOwner(self.user)
        self.db.owners[owner.id] = owner
        self.db.user_owners[self.user] = owner
        
        list_owner = self.rpc.get_owner(owner.id)

        self.assertEquals(owner.key().id(), list_owner.id)
        self.assertEquals(owner.email, list_owner.email)
        self.assertEquals(owner.nickname, list_owner.nickname)
        self.assertEquals(owner.name, list_owner.name)

if __name__ == '__main__':
    unittest.main()
