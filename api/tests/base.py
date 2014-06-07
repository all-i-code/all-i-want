"""
#
# File: base.py
# Description: Base TestCase for testing Resource classes
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

import mock
import unittest

from mocks.mock_access import MockAccess
from mocks.mock_ae import MockAppEngine
from mocks.mock_models import Db


class ResourceTest(unittest.TestCase):

    @classmethod
    def get_resource_cls(cls):
        return getattr(cls, 'resource_cls')

    def setUp(self):
        self.ae = MockAppEngine()
        self.user = self.ae.get_current_user()
        self.db = MockAccess(self.user)

        # set up mock Request and Response objects
        self.request = mock.MagicMock()
        self.response = mock.MagicMock()

        # Create a special SpyResource class that has all the same
        # functionality as the appropriate Resource for this test, but
        # has a fake 'dump' method which stores the last js_object dumped
        resource_cls = self.get_resource_cls()

        class SpyResource(resource_cls):
            def __init__(self, *args, **kwargs):
                super(SpyResource, self).__init__(*args, **kwargs)
                self.last_response = None

            def get_last_response(self):
                return self.last_response

            def dump(self, js_response):
                self.last_response = js_response

        # instantiate the SpyResource with fake Request/Response as well as
        # AppEngine and DbAccess
        self.resource = SpyResource(
            request=self.request,
            response=self.response,
            ae=self.ae,
            db=self.db,
        )

    def tearDown(self):
        # reset the ID count for mock models
        Db.reset()
