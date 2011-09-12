'''
#
# File: util.py
# Description: Utility methods for use in JHB 
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

def camelize(s, trailing=False):
    '''Translate string into camel-case'''
    u = lambda i: not trailing or i > 0
    _ = lambda x, i: x[0].upper() + x[1:] if u(i) else x
    return ''.join(_(w, i) for i,w in enumerate(s.split('_')))

def uncamelize(s):
    '''Convert camel-case into underscore-separated'''
    news = s[0]
    for c in s[1:]: news += '_' + c if c.upper() == c else c
    return news.lower()

def pluralize(s):
    '''Pluralize singular string'''
    if s[-1] == 'y': return s[:-1] + 'ies'
    return s + 's'

