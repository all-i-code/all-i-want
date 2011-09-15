'''
#
# File: util.py
# Description: Utility methods for use in AllIWant 
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

def extract_name(email):
    '''
    Extracts a name from an email assuming the email is in the form:
        first.last@domain.com
    '''
    username = email.split('@')[0]
    return ' '.join((w[0].upper() + w[1:] for w in username.split('.')))

def get_base_url(url):
    '''Extract the base (root) URL from a complete URL'''
    from urlparse import urlparse
    parts = urlparse(url)
    return '%s://%s' % (parts.scheme, parts.netloc)

def prefetch_refprops(entities, *props):
    '''
    Wonderful little method provided by Nick Johnson at
    http://blog.notdot.net/2010/01/ReferenceProperty-prefetching-in-App-Engine
    '''
    fields = [(entity, prop) for entity in entities for prop in props]
    ref_keys = [prop.get_value_for_datastore(x) for x, prop in fields]
    ref_entities = dict((x.key(), x) for x in db.get(set(ref_keys)))
    for (entity, prop), ref_key in zip(fields, ref_keys):
        prop.__set__(entity, ref_entities.get(ref_key, None))
    return entities

