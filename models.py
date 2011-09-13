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
from models_meta import Db

class Db(db.Model):
    user = db.UserProperty(auto_current_user_add=True)
    id = db.IntegerProperty()
    last_modified = db.DateTimeProperty(auto_now=True)
    
    @classmethod
    def all(cls, user):
        return super(Db, cls).all().filter('user =', user)

    def put(self):
        ''' Overriding put to auto-fill id if None'''
        if self.id is None:
            cls = self.__class__.__name__
            q_str = 'SELECT * FROM %s ORDER BY id DESC LIMIT 1' % cls
            q = db.GqlQuery(q_str)
            r = q.fetch(1)
            lid = r[0].id if len(r) > 0 else 0
            self.id = lid + 1
        
        super(Db, self).put()

class GroupDb(Db):
    name = db.StringProperty()
    description = db.StringProperty(multiline=True)
    is_active = db.BooleanProperty()
    
class GroupInvitationDb(Db):
    group_id = db.IntegerProperty()
    group_name = db.StringProperty()
    description = db.StringProperty(multiline=True)
    member_email = db.StringProperty()
    
class GroupMemberDb(Db):
    group_id = db.IntegerProperty()
    
class ListItemDb(Db):
    type = db.StringProperty()
    name = db.StringProperty()
    description = db.StringProperty(multiline=True)
    url = db.StringProperty()
    reserved_by = db.StringProperty()
    purchased_by = db.StringProperty()
    
