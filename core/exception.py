'''
#
# File: exception.py
# Description: AllIWant specific exceptions
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

class UserVisibleError(Exception):
    pass

class PermissionDeniedError(UserVisibleError):
    def __init__(self):
        super(PermissionDeniedError, self).__init__('Permission Denied')

class DuplicateNameError(UserVisibleError):
    def __init__(self, cls, name):
        self.cls = cls
        self.name = name

    def __str__(self):
        n, c = (self.name, self.cls.__name__)
        return 'A %s with name "%s" already exists' % (c, n)

class IdError(Exception):
    def __init__(self, mcls, id):
        self.mcls = mcls
        self.id = id
    
    def __str__(self):
        return 'id "%s" not found for class %s' % (self.id, self.mcls.__name__)

class OverrideError(Exception):
    def __init__(self, function):
        self.function = function

    def __str__(self):
        return 'The function "%s" should be overriden' % self.function

