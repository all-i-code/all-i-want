"""
#
# File: access.py
# Description: Interface into the database
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
from google.appengine.api import mail
from google.appengine.ext.db import Key

from core.util import extract_name as extract
from models import (
    AccessReqDb, ListOwnerDb, GroupDb, GroupInvitationDb,
    GroupMemberDb, ListDb, ListItemDb, ListPermissionDb,
)


class DbAccess(object):
    def __init__(self, user=None):
        self.user = user
        self.owner = None

    def send_mail(self, sender, to, subject, message_body):
        message = mail.EmailMessage()
        message.sender = sender
        message.to = to
        message.subject = subject
        message.body = message_body
        message.send()

    def is_group_name_unique(self, name, key=None):
        q = GroupDb.all().filter('name =', name)
        if key is not None:
            q = q.filter('__key__ !=', key)
        return q.count(1) == 0

    def add_group(self, name, description, owner):
        g = GroupDb(owner=owner, name=name, description=description)
        return g.put()

    def get_group(self, id):
        return GroupDb.get_by_id(id)

    def get_groups(self, keys=None):
        q = GroupDb.all()
        if keys is not None:
            q = q.get(keys)
        return q

    def add_group_invite(self, group, email):
        return GroupInvitationDb(group=group, email=email).put()

    def get_group_invite(self, id):
        return GroupInvitationDb.get_by_id(id)

    def get_group_invites(self, email=None):
        q = GroupInvitationDb.all()
        if email is not None:
            return q.filter('email =', email)
        return q

    def add_group_member(self, group, member):
        return GroupMemberDb(group=group, member=member).put()

    def get_group_member(self, owner, group):
        return owner.memberships.filter('group = ', group).get()

    def add_owner(self, user):
        _ = self
        e, n = user.email(), user.nickname()
        _.owner = ListOwnerDb(user=user, name=extract(e), nickname=n, email=e)
        return _.owner.put()

    def get_owner(self, owner_id):
        return ListOwnerDb.get_by_id(owner_id)

    def get_owner_by_user(self, user):
        return ListOwnerDb.all().filter('user =', user).get()

    def add_permission(self, owner, email):
        return ListPermissionDb(owner=owner, email=email).put()

    def get_permission(self, permission_id):
        return ListPermissionDb.get_by_id(permission_id)

    def get_permissions_by_email(self, email):
        return ListPermissionDb.all().filter('email =', email)

    def get_req_by_user(self, user):
        return AccessReqDb.all().filter('user =', user).get()

    def add_req(self, user):
        return AccessReqDb(user=user, denied=False).put()

    def get_req(self, req_id):
        return AccessReqDb.get_by_id(req_id)

    def get_reqs(self):
        return AccessReqDb.all()

    def get_list(self, list_id):
        return ListDb.get_by_id(list_id)

    def add_list(self, owner_id, name, desc):
        owner = self.get_owner(owner_id)
        return ListDb(name=name, description=desc, owner=owner).put()

    def is_list_name_unique(self, owner_id, name, key=None):
        owner = self.get_owner(owner_id)
        q = ListDb.all().filter('owner = ', owner).filter('name =', name)
        if key is not None:
            q = q.filter('__key__ !=', key)
        return q.count(1) == 0

    def add_list_item(self, list_id, name, category, desc, url, is_surprise):
        key = Key.from_path('ListDb', list_id)
        return ListItemDb(
            parent_list=key,
            name=name,
            category=category,
            description=desc,
            url=url,
            is_surprise=is_surprise,
        ).put()

    def get_item(self, item_id):
        return ListItemDb.get_by_id(item_id)

    def delete(self, obj):
        if isinstance(obj, GroupDb):
            self._delete_group(obj)
        else:
            obj.delete()

    def _delete_group(self, group):
        [m.delete() for m in group.members]
        [i.delete() for i in group.invitations]
        group.delete()
