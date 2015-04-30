"""
#
# File: test_requests.py
# Description: Unit tests for Requests resource
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

from api.requests import Requests
from api.tests.base import ResourceTest
from core.exception import PermissionDeniedError
from core.util import extract_name
from mocks.mock_models import User


class RequestsTest(ResourceTest):
    resource_cls = Requests

    def test_list_no_admin(self):
        """Can't list if not admin"""
        with self.assertRaises(PermissionDeniedError):
            self.resource.get()

    def test_list(self):
        """List all requests"""
        user1 = User(email='john.doe@email.com')
        user2 = User(email='jane.doe@acme.com')
        self.db.add_req(user1)
        self.db.add_req(user2)

        self.user.is_admin = True
        self.resource.get()
        reqs = self.resource.get_last_response()

        self.assertEqual(2, len(reqs))

    def test_get_no_admin(self):
        """Can't get if not admin"""
        user = User(email='john.doe@email.com')
        req = self.db.add_req(user)
        with self.assertRaises(PermissionDeniedError):
            self.resource.get(req.key().id())

    def test_get(self):
        """Get a request by ID"""
        user = User(email='john.doe@email.com')
        req = self.db.add_req(user)
        self.user.is_admin = True

        self.resource.get(req.key().id())
        js_req = self.resource.get_last_response()
        self.assertEqual(js_req.id, req.key().id())
        self.assertEqual(js_req.email, req.user.email())

    def test_approve_no_admin(self):
        """Can't approve if not admin"""
        with self.assertRaises(PermissionDeniedError):
            self.resource.approve(1)

    def test_deny_no_admin(self):
        """Can't deny if not admin"""
        with self.assertRaises(PermissionDeniedError):
            self.resource.deny(1)

    def test_approve(self):
        """Approve creates Owner"""
        self.user.is_admin = True
        user = User(email='joe.smith@email.com')
        req = self.db.add_req(user)
        self.assertEqual({}, self.ae.msg)

        self.resource.approve(req.key().id())

        self.assertEqual(0, len(self.db.request_ids))
        owner = self.db.owners.values()[0]
        self.assertEqual(user, owner.user)
        msg = self.ae.msg
        self.assertEqual(self.ae.FROM_ADDRESS, msg['f'])
        self.assertEqual(owner.email, msg['t'])
        self.assertEqual('Account Activated', msg['s'])
        body = self.ae.APPROVE_TEMPLATE % extract_name(owner.email)
        self.assertEqual(body, msg['b'])

    def test_deny(self):
        """Deny doesn't create owner"""
        self.user.is_admin = True
        user = User(email='joe.smith@email.com')
        req = self.db.add_req(user)
        self.assertFalse(req.denied)
        self.assertEqual({}, self.ae.msg)

        self.resource.deny(req.key().id())

        self.assertTrue(req.denied)
        self.assertEqual(1, len(self.db.request_ids))
        self.assertEqual(0, len(self.db.owners))

        msg = self.ae.msg
        self.assertEqual(self.ae.FROM_ADDRESS, msg['f'])
        self.assertEqual(user.email(), msg['t'])
        self.assertEqual('Account Not Activated', msg['s'])
        body = self.ae.DENY_TEMPLATE % extract_name(user.email())
        self.assertEqual(body, msg['b'])


if __name__ == '__main__':
    unittest.main()
