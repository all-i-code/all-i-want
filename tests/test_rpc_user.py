'''
#
# File: test_rpc_user.py
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
'''

import unittest
from rpc.rpc_user import UserRpcGroup
from tests.ut_access import DummyAccess
from tests.ut_ae import DummyWrapper
from tests.ut_models import User, ListOwner as Owner, AccessReq as Req
from core.exception import PermissionDeniedError

class UserRpcTest(unittest.TestCase):
    
    def setUp(self):
        self.set_user(User())

    def set_user(self, user):
        self.user = user
        self.db = DummyAccess(self.user)
        self.ae = DummyWrapper()
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

        self.set_user(User(is_admin=True))
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
        self.assertEquals(owner.key().id(), user.owner_id)
        self.assertEquals(self.user.nickname(), owner.nickname)
        self.assertEquals(self.user.email(), owner.email)
        self.assertEquals(extract_name(self.user.email()), owner.name)

    def test_get_user_owner_exists(self):
        '''
        Verify that a duplicate owner is not created when get_current_user
        is called
        '''
        # Make sure owner exists before call 
        owner = self.db.add_owner(self.user)
        num_owners = len(self.db.owners)

        url = 'http://www.domain.com/some/extra/path/'
        user = self.rpc.get_current_user(url)

        # verify still the same owner after the call
        self.assertEquals(num_owners, len(self.db.owners))
        self.assertEquals(owner.key().id(), user.owner_id)
        self.assertEquals(owner, self.db.user_owners[self.user])

    def test_get_user_add_req(self):
        '''
        Verify that when non-admin user tries to log in, an Access Request is
        created for them.
        '''
        from core.util import get_base_url
        
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
        self.assertEquals(-1, user.owner_id)
        self.assertEquals(num_owners, len(self.db.owners))
       
        # Verify access request was created correctly
        self.assertEquals(1, len(self.db.request_ids))
        req = self.db.requests.values()[0]
        self.assertEquals(self.user, req.user)
    
    def test_get_user_req_exists(self):
        '''
        Verify that the second time get_current_user is called, a second
        AccessRequest is not created.
        '''
        from core.util import get_base_url
        
        # Verify no owner exists before call
        self.assertEquals(None, self.db.user_owners.get(self.user, None))
        num_owners = len(self.db.owners)

        # Create the request that already exists
        req = self.db.add_req(self.user)
        self.assertEquals(1, len(self.db.request_ids))

        url = 'http://www.domain.com/some/extra/path/'
        user = self.rpc.get_current_user(url)
        
        # Verify correct user details are returned
        self.assertEquals(None, user.login_url)
        self.assertEquals(-1, user.owner_id)
        self.assertEquals(num_owners, len(self.db.owners))
       
        # Verify no additional request was created correctly
        self.assertEquals(1, len(self.db.request_ids))

    def test_get_owner(self):
        '''
        Verify ability to lookup owner by id 
        '''
        # Make sure the owner exists
        owner = self.db.add_owner(self.user)
        oid = owner.key().id()
        lo = self.rpc.get_owner(oid)

        self.assertEquals(oid, lo.id)
        self.assertEquals(owner.email, lo.email)
        self.assertEquals(owner.nickname, lo.nickname)
        self.assertEquals(owner.name, lo.name)

    def test_update_owner(self):
        '''
        Make sure user can change name and nickname of list owner
        '''
        # Make sure the owner exists
        self.set_user(User('John Doe', 'Johnny'))
        self.db.add_owner(self.user)
        owner = self.db.add_owner(self.user)
        self.assertFalse(owner.saved())

        self.rpc.update_owner(owner.key().id(), 'Joe Smith', 'Joe')
        
        self.assertTrue(owner.saved())
        self.assertEquals('Joe Smith', owner.name)
        self.assertEquals('Joe', owner.nickname)

    def test_approve_request_no_admin(self):
        '''
        Verify that attempting to approve a request as non admin user
        raises a PermissionDeniedError
        '''
        self.assertRaises(PermissionDeniedError, self.rpc.approve_request, 1)

    def test_deny_request_no_admin(self):
        '''
        Verify that attempting to approve a request as non admin user
        raises a PermissionDeniedError
        '''
        self.assertRaises(PermissionDeniedError, self.rpc.approve_request, 1)

    def test_approve_request(self):
        '''
        Confirm approving a request for access to all i want
        '''
        from core.util import extract_name
        self.set_user(User(is_admin=True))
        user = User(email='joe.smith@email.com')
        req = self.db.add_req(user)
        self.assertEquals({}, self.ae.msg)

        self.rpc.approve_request(req.key().id())

        self.assertEquals(0, len(self.db.request_ids))
        owner = self.db.owners.values()[0]
        self.assertEquals(user, owner.user)
        msg = self.ae.msg
        self.assertEquals(self.ae.FROM_ADDRESS, msg['f'])
        self.assertEquals(owner.email, msg['t'])
        self.assertEquals('Account Activated', msg['s'])
        body = self.ae.APPROVE_TEMPLATE % extract_name(owner.email)
        self.assertEquals(body, msg['b'])
    
    def test_deny_request(self):
        '''
        Confirm verifying a request for access to all i want
        '''
        from core.util import extract_name
       
        self.set_user(User(is_admin=True))
        user = User(email='joe.smith@email.com')
        req = self.db.add_req(user)
        self.assertEquals({}, self.ae.msg)

        self.rpc.approve_request(req.key().id())

        self.assertEquals(0, len(self.db.request_ids))
        owner = self.db.owners.values()[0]
        self.assertEquals(user, owner.user)
        msg = self.ae.msg
        self.assertEquals(self.ae.FROM_ADDRESS, msg['f'])
        self.assertEquals(owner.email, msg['t'])
        self.assertEquals('Account Activated', msg['s'])
        body = self.ae.APPROVE_TEMPLATE % extract_name(owner.email)
        self.assertEquals(body, msg['b'])

if __name__ == '__main__':
    unittest.main()
