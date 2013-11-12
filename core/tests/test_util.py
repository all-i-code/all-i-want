'''
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
'''

import core.util as util

def test_camelize():
    '''Confirm camilzation of string '''
    assert 'CamelCaseString' == util.camelize('camel_case_string')

def test_camelize_trailing():
    '''Confirm camilzation of trailing part of string '''
    assert 'longMethodName' == util.camelize('long_method_name', trailing=True)

def test_uncamelize():
    '''Confirm converting from camel case to '_' separated'''
    assert 'long_method_name' == util.uncamelize('longMethodName')

def test_pluralize():
    '''Confirm converting singulars into plurals'''
    assert 'families' == util.pluralize('family')
    assert 'studs' == util.pluralize('stud')

def test_extract_Name():
    '''Confirm extraction of a name from an email address'''
    assert 'First Last' == util.extract_name('first.last@email.com')

def test_get_base_url():
    '''Confirm proper extraction of base url'''
    base = 'http://www.domain.com'
    url = '%s/extra/stuff/at/the/end.html' % base
    assert base == util.get_base_url(url)


