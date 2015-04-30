"""
#
# File: mock_models.py
# Description: Mock Model objects
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

from core.util import extract_name


class Db(object):
    _id_map = {}

    @classmethod
    def reset(cls):
        cls._id_map = {}

    def __init__(self, id=None):
        if id is None:
            id = Db._id_map.setdefault(self.__class__, 1)
            Db._id_map[self.__class__] += 1
        self._id = id

    def id(self):
        return self._id

    def key(self):
        return self

    def put(self):
        self._saved = True
        return self

    def saved(self):
        return getattr(self, '_saved', False)


class AccessReq(Db):
    def __init__(self, user, denied=False, **kwargs):
        super(AccessReq, self).__init__(**kwargs)
        self.user = user
        self.denied = denied


class User(Db):
    def __init__(self, nickname='Jobber', email='jobber@email.com',
                 is_admin=False, **kwargs):
        super(User, self).__init__(**kwargs)
        self.is_admin = is_admin
        self._nickname = nickname
        self._email = email
        self._user_id = self.id

    def email(self):
        return self._email

    def nickname(self):
        return self._nickname

    def user_id(self):
        return self._user_id


class ListOwner(Db):
    def __init__(self, user, **kwargs):
        super(ListOwner, self).__init__(**kwargs)
        self.user = user
        self.name = extract_name(user.email())
        self.nickname = user.nickname()
        self.email = user.email()
        self.groups = []
        self.memberships = []
        self.lists = []

    def label(self):
        return '%s (%s)' % (self.nickname, self.email)


class Group(Db):
    def __init__(self, name='', description='', owner=None, **kwargs):
        super(Group, self).__init__(**kwargs)
        self.name = name
        self.description = description
        self.owner = owner
        self.invitations = []
        self.members = []


class GroupInvitation(Db):
    def __init__(self, group=None, email='', **kwargs):
        super(GroupInvitation, self).__init__(**kwargs)
        self.group = group
        self.email = email


class GroupMember(Db):
    def __init__(self, member=None, group=None, **kwargs):
        super(GroupMember, self).__init__(**kwargs)
        self.member = member
        self.group = group


class List(Db):
    def __init__(self, name='', description='', owner=None, **kwargs):
        super(List, self).__init__(**kwargs)
        self.name = name
        self.description = description
        self.owner = owner
        self.items = []


class ListItem(Db):
    def __init__(self, parent_list=None, name='', category='', description='',
                 url='', reserved_by=None, purchased_by=None,
                 is_surprise=False, owner=None, **kwargs):
        super(ListItem, self).__init__(**kwargs)
        self.parent_list = parent_list
        self.name = name
        self.category = category
        self.description = description
        self.url = url
        self.reserved_by = reserved_by
        self.purchased_by = purchased_by
        self.is_surprise = is_surprise
        self.owner = owner
