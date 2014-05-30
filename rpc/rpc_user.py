"""
#
# File: rpc_user.py
# Description: Handler for all User RPCs related to AE User and ListOwnerDb
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

from core.exception import PermissionDeniedError
from core.model import ListOwner, AccessReq, ListPermission
from core.util import extract_name
from rpc.rpc_meta import RpcGroupBase, RpcReqHandler, Rpc
from rpc.rpc_params import (
    RpcParamBoolean as Boolean,
    RpcParamInt as Integer,
    RpcParamString as String,
)


class UserRpcGroup(RpcGroupBase):
    rpcs = (
        Rpc(name='update_owner',
            params=(Integer('owner_id'), String('name'), String('nickname'),)),
        Rpc(name='get_requests'),
        Rpc(name='approve_request', params=(Integer('req_id'),)),
        Rpc(name='deny_request', params=(Integer('req_id'),)),
    )

    def update_owner(self, owner_id, name, nickname):
        """
        Update the name and nickname of the ListOwner object with the given
        owner_id and return the updated object.
        """
        owner = self.db.get_owner(owner_id)
        owner.name = name
        owner.nickname = nickname
        return ListOwner.from_db(owner.put())

    def get_requests(self):
        """
        Return a list of all requests
        """
        if not self.db.user.is_admin:
            raise PermissionDeniedError()

        return [AccessReq.from_db(db) for db in self.db.get_reqs()]

    def approve_request(self, req_id):
        """
        Approve the given AccessRequestDb
        """
        if not self.db.user.is_admin:
            raise PermissionDeniedError()

        req = self.db.get_req(req_id)
        self.db.add_owner(req.user)
        self.db.delete(req)
        to = req.user.email()
        subject = 'Account Activated'
        body = self.ae.APPROVE_TEMPLATE % extract_name(to)
        self.ae.send_mail(to, subject, body)
        return []

    def deny_request(self, req_id):
        """
        Deny the given AccessRequestDb
        """

        if not self.db.user.is_admin:
            raise PermissionDeniedError()

        req = self.db.get_req(req_id)
        req.denied = True
        req.put()
        to = req.user.email()
        subject = 'Account Not Activated'
        body = self.ae.DENY_TEMPLATE % extract_name(to)
        self.ae.send_mail(to, subject, body)
        return []


class UserRpcReqHandler(RpcReqHandler):
    group_cls = UserRpcGroup
