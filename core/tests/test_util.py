"""
#
# File: test_util.py
# Description: unit tests for core.util methods
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
"""

import unittest
import core.util as util


class UtilTest(unittest.TestCase):

    def test_camelize(self):
        """Confirm camilzation of string """
        self.assertEqual(util.camelize('camel_case_string'),
                         'CamelCaseString')

    def test_camelize_trailing(self):
        """Confirm camilzation of trailing part of string """
        self.assertEqual(util.camelize('long_method_name', trailing=True),
                         'longMethodName')

    def test_uncamelize(self):
        """Confirm converting from camel case to '_' separated"""
        self.assertEqual(util.uncamelize('longMethodName'),
                         'long_method_name')

    def test_pluralize(self):
        """Confirm converting singulars into plurals"""
        self.assertEqual(util.pluralize('family'), 'families')
        self.assertEqual(util.pluralize('stud'), 'studs')

    def test_extract_name(self):
        """Confirm extraction of a name from an email address"""
        self.assertEqual(util.extract_name('first.last@email.com'),
                         'First Last')

    def test_get_base_url(self):
        """Confirm proper extraction of base url"""
        base = 'http://www.domain.com'
        url = '{}/extra/stuff/at/the/end.html'.format(base)
        self.assertEqual(util.get_base_url(url), base)

if __name__ == '__main__':
    unittest.main()
