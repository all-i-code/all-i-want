'''
#
# File: gaepdb.py
# Description: A GAE wrapper for pdb to allow pdb.set_trace() to work in GAE
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

def set_trace():
    import sys, pdb
    for attr in ('stdin', 'stdout', 'stderr'):
        setattr(sys, attr, getattr(sys, '__%s__' % attr))
    pdb.set_trace()
