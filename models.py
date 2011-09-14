'''
#
# File: models.py
# Description: Model objects
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

from google.appengine.ext import db

class Db(db.Model):
    last_modified = db.DateTimeProperty(auto_now=True)
    
    @classmethod
    def all(cls, user):
        return super(Db, cls).all().filter('user =', user)

    def put(self):
        super(Db, self).put()
        return self

class GroupDb(Db):
    name = db.StringProperty()
    description = db.StringProperty(indexed=False, multiline=True)
    owner = db.UserProperty(auto_current_user_add=True)
    
class GroupInvitationDb(Db):
    group = db.ReferenceProperty(GroupDb, collection_name='invitations')
    email = db.StringProperty()
    
class GroupMemberDb(Db):
    member = db.UserProperty(auto_current_user_add=True)
    group = db.ReferenceProperty(GroupDb, collection_name='members')

class ListDb(Db):
    name = db.StringProperty(indexed=False)
    owner = db.UserProperty(auto_current_user_add=True)

class ListItemDb(Db):
    parent_list = db.ReferenceProperty(ListDb, collection_name='items')
    name = db.StringProperty(indexed=False)
    category = db.StringProperty(indexed=False)
    description = db.StringProperty(indexed=False, multiline=True)
    url = db.StringProperty(indexed=False)
    reserved_by = db.UserProperty()
    purchased_by = db.UserProperty()
    owner = db.UserProperty(auto_current_user_add=True)

