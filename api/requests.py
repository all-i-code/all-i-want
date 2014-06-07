"""
#
# File: requests.py
# Description: Module for defining Requests API Resource
#
# Copyright 2011-2014 Adam Meadows
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

from api.meta import Resource
from core.exception import PermissionDeniedError
from core.model import (
    AccessReq as JsRequest,
    Success as JsSuccess,
)
from core.util import extract_name


class Requests(Resource):
    """Requests resource (users who have requested access to AIW)"""

    custom_methods = [
        ('<req_id:(\d+)>/approve', 'approve'),
        ('<req_id:(\d+)>/deny', 'deny'),
    ]

    def get(self, resource_id=None):
        """Fetch request(s)"""

        if not self.user.is_admin:
            raise PermissionDeniedError()

        if resource_id is None:
            self.dump([JsRequest.from_db(req) for req in self.db.get_reqs()])
            return

        req = self.db.get_req(int(resource_id))
        if req is None:
            self.abort(404)

        self.dump(JsRequest.from_db(req))

    def approve(self, req_id):
        """Approve the given request"""
        if not self.user.is_admin:
            raise PermissionDeniedError()

        req = self.db.get_req(int(req_id))
        if req is None:
            self.abort(404)

        self.db.add_owner(req.user)
        self.db.delete(req)
        to = req.user.email()
        subject = 'Account Activated'
        body = self.ae.APPROVE_TEMPLATE % extract_name(to)
        self.ae.send_mail(to, subject, body)
        self.dump(JsSuccess())

    def deny(self, req_id):
        """Deny the given request"""
        if not self.user.is_admin:
            raise PermissionDeniedError()

        req = self.db.get_req(int(req_id))
        if req is None:
            self.abort(404)

        req.denied = True
        req.put()
        to = req.user.email()
        subject = 'Account Not Activated'
        body = self.ae.DENY_TEMPLATE % extract_name(to)
        self.ae.send_mail(to, subject, body)
        self.dump(JsSuccess())
