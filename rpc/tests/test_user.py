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

from core.exception import PermissionDeniedError
from core.util import get_base_url, extract_name
from rpc.rpc_user import UserRpcGroup
from mocks.mock_access import MockAccess
from mocks.mock_ae import MockWrapper
from mocks.mock_models import User


class UserRpcTest(unittest.TestCase):

    def setUp(self):
        self.set_user(User())

    def set_user(self, user):
        self.user = user
        self.db = MockAccess(self.user)
        self.ae = MockWrapper()
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
        self.assertEqual(None, user.logout_url)
        self.assertEqual(None, user.email)
        self.assertEqual(None, user.user_id)
        self.assertEqual(None, user.nickname)
        login_url = self.ae.create_login_url(url)
        self.assertEqual(login_url, user.login_url)

    def test_get_user(self):
        '''
        Verify that calling get_current_user while logged in provides you
        with a logout url and your login info
        '''

        self.set_user(User(is_admin=True))
        # Verify no owner exists before call
        self.assertEqual(None, self.db.user_owners.get(self.user, None))
        num_owners = len(self.db.owners)

        url = 'http://www.domain.com/some/extra/path/'
        user = self.rpc.get_current_user(url)

        # Verify correct user details are returned
        base = get_base_url(url)
        goodbye = '{}/#Goodbye:'.format(base)
        self.assertEqual(self.ae.create_logout_url(goodbye), user.logout_url)
        self.assertEqual(self.user.nickname(), user.nickname)
        self.assertEqual(self.user.email(), user.email)
        self.assertEqual(None, user.login_url)

        # Verify owner was created correctly
        owner = self.db.user_owners.get(self.user)
        self.assertEqual(num_owners + 1, len(self.db.owners))
        self.assertEqual(self.user, owner.user)
        self.assertEqual(owner.key().id(), user.owner_id)
        self.assertEqual(self.user.nickname(), owner.nickname)
        self.assertEqual(self.user.email(), owner.email)
        self.assertEqual(extract_name(self.user.email()), owner.name)

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
        self.assertEqual(num_owners, len(self.db.owners))
        self.assertEqual(owner.key().id(), user.owner_id)
        self.assertEqual(owner, self.db.user_owners[self.user])

    def test_get_user_add_req(self):
        '''
        Verify that when non-admin user tries to log in, an Access Request is
        created for them.
        '''

        # Verify no owner exists before call
        self.assertEqual(None, self.db.user_owners.get(self.user, None))
        num_owners = len(self.db.owners)

        url = 'http://www.domain.com/some/extra/path/'
        user = self.rpc.get_current_user(url)

        # Verify correct user details are returned
        base = get_base_url(url)
        goodbye = '{}/#Goodbye:'.format(base)
        self.assertEqual(self.ae.create_logout_url(goodbye), user.logout_url)
        self.assertEqual(self.user.nickname(), user.nickname)
        self.assertEqual(self.user.email(), user.email)
        self.assertEqual(None, user.login_url)
        self.assertEqual(-1, user.owner_id)
        self.assertEqual(num_owners, len(self.db.owners))

        # Verify access request was created correctly
        self.assertEqual(1, len(self.db.request_ids))
        req = self.db.requests.values()[0]
        self.assertEqual(self.user, req.user)

    def test_get_user_req_exists(self):
        '''
        Verify that the second time get_current_user is called, a second
        AccessRequest is not created.
        '''

        # Verify no owner exists before call
        self.assertEqual(None, self.db.user_owners.get(self.user, None))
        num_owners = len(self.db.owners)

        # Create the request that already exists
        self.db.add_req(self.user)
        self.assertEqual(1, len(self.db.request_ids))

        url = 'http://www.domain.com/some/extra/path/'
        user = self.rpc.get_current_user(url)

        # Verify correct user details are returned
        self.assertEqual(None, user.login_url)
        self.assertEqual(-1, user.owner_id)
        self.assertEqual(num_owners, len(self.db.owners))

        # Verify no additional request was created correctly
        self.assertEqual(1, len(self.db.request_ids))

    def test_get_owner(self):
        '''
        Verify ability to lookup owner by id
        '''
        # Make sure the owner exists
        owner = self.db.add_owner(self.user)
        oid = owner.key().id()
        lo = self.rpc.get_owner(oid)

        self.assertEqual(oid, lo.id)
        self.assertEqual(owner.email, lo.email)
        self.assertEqual(owner.nickname, lo.nickname)
        self.assertEqual(owner.name, lo.name)

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
        self.assertEqual('Joe Smith', owner.name)
        self.assertEqual('Joe', owner.nickname)

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
        self.set_user(User(is_admin=True))
        user = User(email='joe.smith@email.com')
        req = self.db.add_req(user)
        self.assertEqual({}, self.ae.msg)

        self.rpc.approve_request(req.key().id())

        self.assertEqual(0, len(self.db.request_ids))
        owner = self.db.owners.values()[0]
        self.assertEqual(user, owner.user)
        msg = self.ae.msg
        self.assertEqual(self.ae.FROM_ADDRESS, msg['f'])
        self.assertEqual(owner.email, msg['t'])
        self.assertEqual('Account Activated', msg['s'])
        body = self.ae.APPROVE_TEMPLATE % extract_name(owner.email)
        self.assertEqual(body, msg['b'])

    def test_deny_request(self):
        '''
        Confirm verifying a request for access to all i want
        '''

        self.set_user(User(is_admin=True))
        user = User(email='joe.smith@email.com')
        req = self.db.add_req(user)
        self.assertEqual({}, self.ae.msg)

        self.rpc.approve_request(req.key().id())

        self.assertEqual(0, len(self.db.request_ids))
        owner = self.db.owners.values()[0]
        self.assertEqual(user, owner.user)
        msg = self.ae.msg
        self.assertEqual(self.ae.FROM_ADDRESS, msg['f'])
        self.assertEqual(owner.email, msg['t'])
        self.assertEqual('Account Activated', msg['s'])
        body = self.ae.APPROVE_TEMPLATE % extract_name(owner.email)
        self.assertEqual(body, msg['b'])

if __name__ == '__main__':
    unittest.main()
