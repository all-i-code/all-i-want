"""
#
# File: rpc_group.py
# Description:
#   Handler for all Group RPCs (setting up Groups and inviting members)
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

from rpc.rpc_meta import RpcGroupBase, RpcReqHandler, Rpc
from rpc.rpc_params import (
    RpcParamInt as Integer,
)
from core.model import ListOwner
from core.exception import PermissionDeniedError


class GroupRpcGroup(RpcGroupBase):
    rpcs = (
        Rpc(name='get_available_owners', params=(Integer('owner_id'),)),
    )

    def _verify_owner(self):
        self.owner = self.db.get_owner_by_user(self.db.user)
        if self.owner is None:
            raise PermissionDeniedError()

    def get_available_owners(self, owner_id):
        self._verify_owner()
        # TODO: optimize this to minimize queries
        owner = self.db.get_owner(owner_id)
        gs, gms = (owner.groups, owner.memberships)
        owners = [owner]
        owners.extend(m.member for g in gs for m in g.members)
        owners.extend(m.member for gm in gms for m in gm.group.members)
        unique_owners = []
        oids = []
        for owner in owners:
            if owner.key().id() not in oids:
                oids.append(owner.key().id())
                unique_owners.append(owner)
        return [ListOwner.from_db(o) for o in unique_owners]


class GroupRpcReqHandler(RpcReqHandler):
    group_cls = GroupRpcGroup
