'''
#
# File: rpc_user.py
# Description: Handler for all User RPCs related to AE User and ListOwnerDb
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
from core.model import User, ListOwner

class AeWrapper:
    def create_login_url(self, url):
        from google.appengine.api import users
        return users.create_login_url(url)

    def create_logout_url(self, url):
        from google.appengine.api import users
        return users.create_logout_url(url)

class UserRpcGroup(RpcGroupBase):
    rpcs = (
        Rpc(name='get_current_user', params=(
            RpcParamString('url'),
        )),
        Rpc(name='get_owner', params=(
            RpcParamInt('owner_id'),
        )),
    )

    def __init__(self, db, ae_wrapper=None):
        super(UserRpcGroup, self).__init__(db)
        if ae_wrapper is None:
            ae_wrapper = AeWrapper()
        self.ae = ae_wrapper
        
    def get_current_user(self, url):
        '''
        Return a User object with details of the current authenticated user,
        including a url for logging out.
        
        If user has not yet been authenticated, return a User object with a
        url to allow user to log in.
        '''
        e, n, ui, li, lo, oid = (None, None, None, None, None, -1)
        user = self.db.user
        if user is None:
            li = self.ae.create_login_url(url)
        else:
            e, n, ui = user.email(), user.nickname(), user.user_id()
            # make sure ListOwnerDb record exists
            oid = self.db.confirm_owner(user)
            from core.util import get_base_url
            base = get_base_url(url)
            lo = self.ae.create_logout_url('%s/#Goodbye:' % base)
        return User(email=e, nickname=n, user_id=ui, login_url=li,
            logout_url=lo, owner_id=oid)

    def get_owner(self, owner_id):
        '''
        Return the ListOwner object with the given owner_id
        '''
        return ListOwner.from_db(self.db.get_owner(owner_id))
    
    def update_owner(self, owner_id, name, nickname):
        '''
        Update the name and nickname of the ListOwner object with the given
        owner_id and return the updated object.
        '''
        return ListOwner.from_db(self.db.get_owner(owner_id))

class UserRpcReqHandler(RpcReqHandler):
    group_cls = UserRpcGroup

