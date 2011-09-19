'''
#
# File: access.py
# Description: Interface into the database
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
from core.util import extract_name as extract
from models import AccessReqDb, ListOwnerDb, GroupDb, GroupInvitationDb,\
    GroupMemberDb, ListDb, ListItemDb

class DbAccess:
    def __init__(self, user=None):
        self.user = user
        self.owner = None

    def send_mail(self, sender, to, subject, message_body):
        from google.appengine.api import mail
        message = mail.EmailMessage()
        message.sender = sender
        message.to = to
        message.subject = subject
        message.body = message_body
        message.send()

    def is_group_name_unique(self, name, key=None):
        q = GroupDb.all().filter('name =', name)
        if key is not None:
            q.filter('key !=', key)
        return q.count(1) == 0

    def add_group(self, name, description):
        g = GroupDb(name=name, description=description)
        return g.put()

    def get_group(self, id):
        return GroupDb.get_by_id(id)

    def get_groups(self, keys=None):
        if keys is None:
            return GroupDb.all().filter('user = ', self.user)
        return GroupDb.get(keys)

    def add_group_invite(self, group, email):
        return GroupInvitationDb(group=group, email=email).put()

    def get_group_invite(self, id):
        return GroupInvitationDb.get_by_id(id)

    def add_group_member(self, group):
        return GroupMemberDb(group=group, member=self.user).put()
  
    def get_group_members(self):
        return GroupMemberDb.all().filter('member = ', self.user)

    def add_owner(self, user):
        _ = self
        e, n = user.email(), user.nickname()
        _.owner = ListOwnerDb(user=user, name=extract(e), nickname=n, email=e)
        return _.owner.put()

    def get_owner(self, owner_id):
        return ListOwnerDb.get_by_id(owner_id)

    def get_owner_by_user(self, user):
        return ListOwnerDb.all().filter('user =', user).get()

    def get_req_by_user(self, user):
        return AccessReqDb.all().filter('user =', user).get()

    def add_req(self, user):
        return AccessReqDb(user=user).put()

    def get_req(self, req_id):
        return AccessReqDb.get_by_id(req_id)

    def get_reqs(self):
        return AccessReqDb.all()

    def add_list(self, owner_id, name, desc):
        owner = self.get_owner(owner_id)
        return ListDb(name=name, description=desc, owner=owner).put()

    def is_list_name_unique(self, owner_id, name, key=None):
        owner = self.get_owner(owner_id)
        q = ListDb.all().filter('owner = ', owner).filter('name =', name)
        if key is not None:
            q.filter('key !=', key)
        return q.count(1) == 0

    def add_list_item(self, wlist, name, category, desc, url, is_surprise):
        return ListItemDb(parent_list=wlist, name=name, category=category,
            description=desc, url=url, is_surprise=is_surprise).put()

    def get_item(self, item_id):
        return ListItemDb.get_by_id(item_id)

    def delete(self, obj):
        obj.delete()

