'''
#
# File: model.py
# Description: Model classes
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
'''

# TODO: update these to user Model.property.get_value_for_datastore(instance)
# to get ids for reference properties

from core.meta import (
    Model,
    FieldBoolean as Boolean,
    FieldInt as Integer,
    FieldString as String,
    FieldText as Text,
    FieldModelArray as ModelArray,
)


class AccessReq(Model):
    fields = (
        Integer(name='id'),
        Boolean(name='was_denied'),
        String(name='email'),
    )

    @classmethod
    def from_db(cls, db):
        return cls(
            id=db.key().id(),
            was_denied=db.denied,
            email=db.user.email(),
        )


class ListPermission(Model):
    fields = (
        Integer(name='id'),
        Integer(name='owner_id'),
        String(name='email'),
    )

    @classmethod
    def from_db(cls, db):
        _ = lambda x: x.key().id()
        return cls(id=_(db), owner_id=_(db.owner), email=db.email)


class ListOwner(Model):
    fields = (
        Integer(name='id'),
        String(name='name'),
        String(name='nickname'),
        String(name='email'),
    )

    @classmethod
    def from_db(cls, db):
        _ = lambda x: x.key().id()
        return cls(
            id=_(db),
            name=db.name,
            nickname=db.nickname,
            email=db.email,
        )


class GroupInvitation(Model):
    fields = (
        Integer(name='id'),
        Integer(name='group_id'),
        String(name='group_name'),
        String(name='owner_name'),
        String(name='member_email'),
    )

    @classmethod
    def from_db(cls, db):
        _ = lambda x: x.key().id()
        return cls(
            id=_(db),
            group_id=_(db.group),
            group_name=db.group.name,
            owner_name=db.group.owner.label(),
            member_email=db.email,
        )


class GroupMember(Model):
    fields = (
        Integer(name='id'),
        Integer(name='group_id'),
        String(name='name'),
        String(name='nickname'),
        String(name='email'),
        String(name='user_id'),
    )

    @classmethod
    def from_db(cls, db):
        _ = lambda x: x.key().id()
        return cls(
            id=_(db),
            group_id=_(db.group),
            name=db.member.name,
            user_id=db.member.user.user_id(),
            nickname=db.member.nickname,
            email=db.member.email,
        )


class Group(Model):
    fields = (
        Integer(name='id'),
        String(name='name'),
        String(name='owner'),
        Integer(name='owner_id'),
        Text(name='description'),
        ModelArray(type=GroupInvitation, name='invitations'),
        ModelArray(type=GroupMember, name='members'),
    )

    @classmethod
    def from_db(cls, db):
        _id = lambda x: x.key().id() if x is not None else -1
        _lbl = lambda x: x.label() if x is not None else -1
        invitations = [ GroupInvitation.from_db(i) for i in db.invitations ]
        members = [ GroupMember.from_db(m) for m in db.members ]
        return cls(
            id=db.key().id(),
            name=db.name,
            owner_id=_id(db.owner),
            owner=_lbl(db.owner),
            description=db.description,
            invitations=invitations,
            members=members,
        )


class ListItem(Model):
    fields = (
        Integer(name='id'),
        String(name='name'),
        Text(name='description'),
        String(name='category'),
        String(name='url'),
        String(name='reserved_by'),
        Integer(name='reserved_by_owner_id'),
        String(name='purchased_by'),
        Integer(name='purchased_by_owner_id'),
        Boolean(name='is_surprise'),
    )

    @classmethod
    def from_db(cls, db):
        _ = lambda x: x.nickname if x is not None else ''
        _id = lambda x: x.key().id() if x is not None else -1
        return cls(
            id=db.key().id(),
            name=db.name,
            description=db.description,
            category=db.category,
            url=db.url,
            reserved_by=_(db.reserved_by),
            purchased_by=_(db.purchased_by),
            reserved_by_owner_id=_id(db.reserved_by),
            purchased_by_owner_id=_id(db.purchased_by),
            is_surprise=db.is_surprise,
        )


class WishList(Model):
    fields = (
        Integer(name='id'),
        String(name='name'),
        String(name='description'),
        ModelArray(type=ListItem, name='items'),
    )

    @classmethod
    def from_db(cls, db, own=False):
        _ = lambda i: (not own) or (not i.is_surprise)
        items = [ ListItem.from_db(i) for i in db.items if _(i) ]
        return cls(
            id=db.key().id(),
            name=db.name,
            description=db.description,
            items=items,
        )


class User(Model):
    fields = (
        String(name='email'),
        String(name='nickname'),
        String(name='user_id'),
        String(name='login_url'),
        String(name='logout_url'),
        Integer(name='owner_id'),
        Boolean(name='was_req_denied'),
        Boolean(name='is_admin'),
    )


class FailureReport(Model):
    fields = (
        String(name='error_type'),
        String(name='message'),
        String(name='traceback'),
    )


def get_all_classes():
    from core.meta import ModelManager as manager
    return manager.models
