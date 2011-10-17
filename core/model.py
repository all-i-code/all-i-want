'''
#
# File: model.py
# Description: Model classes
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

# TODO: update these to user Model.property.get_value_for_datastore(instance)
# to get ids for reference properties

from core.meta import Model, FieldBoolean, FieldInt, FieldString, FieldText,\
    FieldModelArray

class AccessReq(Model):
    fields = (
        FieldInt(name='id'),
        FieldBoolean(name='was_denied'),
        FieldString(name='email'),
    )

    @classmethod
    def from_db(cls, db):
        return cls(id=db.key().id(), was_denied=db.denied,
            email=db.user.email())

class ListOwner(Model):
    fields = (
        FieldInt(name='id'),
        FieldString(name='name'),
        FieldString(name='nickname'),
        FieldString(name='email'),
    )

    @classmethod
    def from_db(cls, db):
        _ = lambda x: x.key().id()
        return cls(id=_(db), name=db.name, nickname=db.nickname, email=db.email)

class GroupInvitation(Model):
    fields = (
        FieldInt(name='id'),
        FieldInt(name='group_id'),
        FieldString(name='group_name'),
        FieldString(name='owner_name'),
        FieldString(name='member_email'),
    )
    
    @classmethod
    def from_db(cls, db):
        _ = lambda x: x.key().id()
        return cls(id=_(db), group_id=_(db.group),
            group_name=db.group.name, owner_name=db.group.owner.label(),
            member_email=db.email)

class GroupMember(Model):
    fields = (
        FieldInt(name='id'),
        FieldInt(name='group_id'),
        FieldString(name='name'),
        FieldString(name='nickname'),
        FieldString(name='email'),
        FieldString(name='user_id'),
    )
    
    @classmethod
    def from_db(cls, db):
        _ = lambda x: x.key().id()
        return cls(id=_(db), group_id=_(db.group),
            user_id=db.member.user.user_id(), nickname=db.member.nickname,
            email=db.member.email)

class Group(Model):
    fields = (
        FieldInt(name='id'),
        FieldString(name='name'),
        FieldString(name='owner'),
        FieldText(name='description'),
        FieldModelArray(type=GroupInvitation, name='invitations'),
        FieldModelArray(type=GroupMember, name='members'),
    )

    @classmethod
    def from_db(cls, db):
        invitations = [ GroupInvitation.from_db(i) for i in db.invitations ]
        members = [ GroupMember.from_db(m) for m in db.members ]
        return cls(id=db.key().id(), name=db.name, owner=db.owner.label(),
            description=db.description, invitations=invitations,
            members=members)

class ListItem(Model):
    fields = (
        FieldInt(name='id'),
        FieldString(name='name'),
        FieldText(name='description'),
        FieldString(name='category'),
        FieldString(name='url'),
        FieldString(name='reserved_by'),
        FieldString(name='purchased_by'),
        FieldBoolean(name='is_surprise'),
    )
    
    @classmethod
    def from_db(cls, db):
        _ = lambda x: x.label() if x is not None else ''
        return cls(key=db.key().id(), name=db.name,
            description=db.description, category=db.category, url=db.url,
            reserved_by=_(db.reserved_by), purchased_by=_(db.purchased_by),
            is_surprise=db.is_surprise)

class WishList(Model):
    fields = (
        FieldInt(name='id'),
        FieldString(name='name'),
        FieldModelArray(type=ListItem, name='items'),
    )

    @classmethod
    def from_db(cls, db):
        items = [ ListItem.from_db(i) for i in db.items ]
        return cls(id=db.key().id(), name=db.name, items=items)

class User(Model):
    fields = (
        FieldString(name='email'),
        FieldString(name='nickname'),
        FieldString(name='user_id'),
        FieldString(name='login_url'),
        FieldString(name='logout_url'),
        FieldInt(name='owner_id'),
        FieldBoolean(name='was_req_denied'),
        FieldBoolean(name='is_admin'),
    )

class FailureReport(Model):
    fields = (
        FieldString(name='error_type'),
        FieldString(name='message'),
        FieldString(name='traceback'),
    )

def get_all_classes():
    from core.meta import ModelManager as manager
    return manager.models

