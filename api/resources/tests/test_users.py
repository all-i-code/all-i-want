"""
#
# File: test_users.py
# Description: Unit tests for Users resource
#
# Copyright 2014 Adam Meadows
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

from core.util import get_base_url, extract_name
from api.resources.tests.base import ResourceTest
from api.resources import Users


class UsersTest(ResourceTest):
    resource_cls = Users

    def test_get_user(self):
        """
        Verify that calling get_current_user while logged in provides you
        with a logout url and your login info
        """
        self.user.is_admin = True

        # Verify no owner exists before call
        self.assertEqual(None, self.db.user_owners.get(self.user, None))
        num_owners = len(self.db.owners)

        # make the API Call
        self.resource.get_current()

        # grab the last js_response object
        user = self.resource.last_js_response

        # Verify correct user details are returned
        logout_url = self.ae.create_logout_url('/goodbye')
        self.assertEqual(logout_url, user.logout_url)
        self.assertEqual(self.user.nickname(), user.nickname)
        self.assertEqual(self.user.email(), user.email)

        # Verify owner was created correctly
        owner = self.db.user_owners.get(self.user)
        self.assertEqual(num_owners + 1, len(self.db.owners))
        self.assertEqual(self.user, owner.user)
        self.assertEqual(owner.key().id(), user.owner_id)
        self.assertEqual(self.user.nickname(), owner.nickname)
        self.assertEqual(self.user.email(), owner.email)
        self.assertEqual(extract_name(self.user.email()), owner.name)

    def Xtest_get_user_owner_exists(self):
        """
        Verify that a duplicate owner is not created when get_current_user
        is called
        """
        # Make sure owner exists before call
        owner = self.db.add_owner(self.user)
        num_owners = len(self.db.owners)

        url = 'http://www.domain.com/some/extra/path/'
        user = self.rpc.get_current_user(url)

        # verify still the same owner after the call
        self.assertEqual(num_owners, len(self.db.owners))
        self.assertEqual(owner.key().id(), user.owner_id)
        self.assertEqual(owner, self.db.user_owners[self.user])

    def Xtest_get_user_add_req(self):
        """
        Verify that when non-admin user tries to log in, an Access Request is
        created for them.
        """

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

    def Xtest_get_user_req_exists(self):
        """
        Verify that the second time get_current_user is called, a second
        AccessRequest is not created.
        """

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


if __name__ == '__main__':
    unittest.main()
