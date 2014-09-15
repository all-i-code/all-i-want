"""
#
# File: mock_access.py
# Description: Mock Interface into the database for testing
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
from mocks.mock_models import (
    AccessReq as Req,
    ListOwner as Owner,
    Group,
    GroupInvitation as Invite,
    GroupMember as Member,
    List,
    ListItem as Item,
)


class MockAccess(object):
    def __init__(self, user=None, add_owner=False):
        self.owners = {}
        self.user_owners = {}
        self.requests = {}
        self.user_requests = {}
        self.request_ids = []

        self.groups = {}
        self.group_ids = []
        self.invitations = {}
        self.invitation_ids = []
        self.members = {}
        self.member_ids = []

        self.lists = {}
        self.list_ids = []
        self.items = {}
        self.item_ids = []

        self.user = user
        self.owner = None
        if add_owner:
            self.owner = self.add_owner(user)

    def add_owner(self, user):
        owner = Owner(user)
        self.user_owners[user] = owner
        self.owners[owner.key().id()] = owner
        return owner

    def is_group_name_unique(self, name, key=None):
        groups = [g for g in self.groups.values() if g.name == name]
        if key is not None:
            groups = [g for g in groups if g.key() != key]
        return len(groups) == 0

    def add_group(self, name, description, owner=None):
        owner = owner or self.owner
        g = Group(name=name, description=description, owner=owner)
        gid = g.key().id()
        self.groups[gid] = g
        self.group_ids.append(gid)
        owner.groups.append(g)
        return g

    def get_group(self, group_id):
        return self.groups.get(group_id, None)

    def get_groups(self, keys=None):
        if keys is not None:
            return (self.groups[key.id()] for key in keys)
        return (self.groups[id] for id in self.group_ids)

    def add_group_invite(self, group, email):
        i = Invite(group, email)
        iid = i.key().id()
        group.invitations.append(i)
        self.invitations[iid] = i
        self.invitation_ids.append(iid)
        return i

    def get_group_invite(self, invite_id):
        return self.invitations.get(invite_id, None)

    def add_group_member(self, group, owner=None):
        owner = owner or self.owner
        m = Member(owner, group)
        mid = m.key().id()
        group.members.append(m)
        self.members[mid] = m
        self.member_ids.append(mid)
        owner.memberships.append(m)
        return m

    def get_group_member(self, owner, group):
        membership = None
        for m in owner.memberships:
            if m.group.key().id() == group.key().id():
                membership = m
                break
        return membership

    def get_owner(self, owner_id):
        return self.owners.get(owner_id, None)

    def get_owner_by_user(self, user):
        return self.user_owners.get(user, None)

    def get_req_by_user(self, user):
        return self.user_requests.get(user, None)

    def add_req(self, user):
        req = Req(user)
        rid = req.key().id()
        self.user_requests[user] = req
        self.requests[rid] = req
        self.request_ids.append(rid)
        return req

    def get_req(self, req_id):
        return self.requests.get(req_id, None)

    def get_reqs(self):
        return (self.get_req(rid) for rid in self.request_ids)

    def add_list(self, owner_id, name, desc):
        owner = self.get_owner(owner_id)
        l = List(name=name, description=desc, owner=owner)
        lid = l.key().id()
        owner.lists.append(l)
        self.lists[lid] = l
        self.list_ids.append(lid)
        return l

    def get_list(self, list_id):
        return self.lists[list_id]

    def is_list_name_unique(self, owner_id, name, key=None):
        owner = self.get_owner(owner_id)
        _ = lambda l: l.owner == owner and l.name == name
        lists = [l for l in self.lists.values() if _(l)]
        if key is not None:
            lists = [l for l in lists if l.key() != key]
        return len(lists) == 0

    def add_list_item(self, list_id, name, category, desc, url, is_surprise):
        parent = self.lists[list_id]
        item = Item(parent_list=parent, name=name,
                    category=category, description=desc, url=url,
                    is_surprise=is_surprise)
        iid = item.key().id()
        parent.items.append(item)
        self.items[iid] = item
        self.item_ids.append(iid)
        return item

    def get_item(self, item_id):
        return self.items[item_id]

    def save(self, obj):
        obj._saved = True
        return obj

    def delete(self, obj):
        if isinstance(obj, Req):
            self._delete_req(obj)
        elif isinstance(obj, Group):
            self._delete_group(obj)
        elif isinstance(obj, Invite):
            self._delete_invite(obj, from_group=True)
        elif isinstance(obj, Member):
            self._delete_member(obj, from_group=True)
        elif isinstance(obj, Owner):
            self._delete_owner(obj)
        elif isinstance(obj, Item):
            self._delete_item(obj)

    def _delete_req(self, req):
        rid = req.key().id()
        del self.requests[rid]
        del self.user_requests[req.user]
        self.request_ids.remove(rid)

    def _delete_group(self, group):
        gid = group.key().id()
        for i in group.invitations:
            self._delete_invite(i)
        for m in group.members:
            self._delete_member(m)
        del self.groups[gid]
        self.group_ids.remove(gid)

    def _delete_invite(self, invite, from_group=False):
        iid = invite.key().id()
        if from_group:
            invite.group.invitations.remove(invite)
        del self.invitations[iid]
        self.invitation_ids.remove(iid)

    def _delete_member(self, member, from_group=False):
        mid = member.key().id()
        if from_group:
            member.group.members.remove(member)
        del self.members[mid]
        self.member_ids.remove(mid)
        owner = member.member
        owner.memberships.remove(member)

    def _delete_owner(self, owner):
        del self.user_owners[owner.user]
        del self.owners[owner.key().id()]

    def _delete_item(self, item):
        del self.items[item.key().id()]
        item.parent_list.items.remove(item)

    def get_permissions_by_email(self, email):
        return []
