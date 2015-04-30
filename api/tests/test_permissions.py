"""
#
# File: test_permissions.py
# Description: Unit tests for Permissions Resource
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
from api.permissions import Permissions
# from mocks.mock_models import ListPermission as Permission


class PermissionsTest(ResourceTest):
    resource_cls = Permissions

    def test_list(self):
        """
        list all permissions
        """
        pass

    def test_get(self):
        """
        lookup permission by id
        """
        pass

    def test_create(self):
        """
        create a new Permission
        """

    def test_delete(self):
        """
        delete an existing Permission
        """

if __name__ == '__main__':
    unittest.main()
