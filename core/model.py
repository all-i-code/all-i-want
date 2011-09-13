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

from core.meta import Model, FieldInt, FieldString, FieldText

class Group(Model):
    fields = (
        FieldInt(name='id'),
        FieldString(name='name'),
        FieldText(name='description'),
    )

class GroupInvitation(Model):
    fields = (
        FieldInt(name='id'),
        FieldInt(name='group_id'),
        FieldString(name='group_name'),
        FieldString(name='owner_email'),
        FieldString(name='member_email'),
    )

class GroupMember(Model):
    fields = (
        FieldInt(name='id'),
        FieldInt(name='group_id'),
        FieldString(name='nickname'),
        FieldString(name='email'),
    )

class ListItem(Model):
    fields = (
        FieldInt(name='id'),
        FieldString(name='name'),
        FieldText(name='description'),
        FieldString(name='url'),
        FieldString(name='reserved_by'),
        FieldString(name='purchased_by'),
    )

class User(Model):
    fields = (
        FieldString(name='email'),
        FieldString(name='nickname'),
        FieldString(name='user_id'),
        FieldString(name='login_url'),
        FieldString(name='logout_url'),
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

