'''
#
# File: rpc_group.py
# Description: 
#   Handler for all Group RPCs (setting up Groups and inviting members) 
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

from rpc.rpc_meta import RpcGroupBase, RpcReqHandler, Rpc
from rpc.rpc_params import RpcParamString, RpcParamInt
from core.model import Group, GroupInvitation, GroupMember, ListOwner
from core.exception import DuplicateNameError

class GroupRpcGroup(RpcGroupBase):
    rpcs = (
        Rpc(name='add_group', params=(
            RpcParamString('name'),
            RpcParamString('desc'),
        )),
        Rpc(name='get_groups'),
        Rpc(name='update_group', params=(
            RpcParamInt('id'),
            RpcParamString('name'),
            RpcParamString('desc'),
        )),
        Rpc(name='invite_member', params=(
            RpcParamInt('group_id'),
            RpcParamString('email'),
        )),
        Rpc(name='accept_invitation',params=(
            RpcParamInt('invite_id'),
        )),
        Rpc(name='decline_invitation',params=(
            RpcParamInt('invite_id'),
        )),
        Rpc(name='get_available_owners',params=(
            RpcParamInt('owner_id'),
        )),
    )

    def add_group(self, name, desc):
        if not self.db.user.is_admin:
            raise Exception('Not Authorized')
        # TODO: check for duplicate names
        g = self.db.add_group(name, desc)
        self.db.add_group_member(g)
        return Group.from_db(g)

    def get_groups(self):
        return [ Group.from_db(g) for g in self.db.get_groups() ]
  
    def update_group(self, id, name, desc):
        g = self.db.get_group(id)
      
        # TODO: check for duplicate names
        g.name = name
        g.description = desc
        return Group.from_db(self.db.save(g))

    def invite_member(self, group_id, email):
        g = self.db.get_group(group_id)
        self.db.add_group_invite(g, email)
        return []

    def accept_invitation(self, invite_id):
        i = self.db.get_group_invite(invite_id)
        m = self.db.add_group_member(i.group)
        self.db.save(m)
        self.db.delete(i)
        return []
    
    def decline_invitation(self, invite_id):
        i = self.db.get_group_invite(invite_id)
        self.db.delete(i)
        return []
    
    def get_available_owners(self, owner_id):
        # TODO: optimize this to minimize queries 
        owner = self.db.get_owner(owner_id)
        _ = lambda x: ListOwner.from_db(x)
        owners = []
        for g in owner.groups:
            owners.extend(m.member for m in g.members)
        for gm in owner.memberships:
            owners.extend(m.member for m in gm.group.members)

        return [ ListOwner.from_db(o) for o in set(owners) ]

class GroupRpcReqHandler(RpcReqHandler):
    group_cls = GroupRpcGroup

