'''
#
# File: rpc_account.py
# Description: 
#   Handler for all JHB Account RPCs (setting up Accounts and Categories) 
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

from json import dumps
from rpc.rpc_meta import RpcGroupBase, RpcReqHandler, Rpc
from rpc.rpc_params import RpcParamString, RpcParamInt, RpcParamBoolean,\
    RpcParamFloat
from access import GaeAccess
from jhb.core.model import Account, Category
from jhb.core.exception import DuplicateNameError

class AccountRpcGroup(RpcGroupBase):
    rpcs = (
        Rpc(name='add_account',
            params = (
                RpcParamString('name'),
                RpcParamString('desc'),
            ),
        ),
        Rpc(name='get_accounts'),
        Rpc(name='update_account',
            params = (
                RpcParamInt('id'),
                RpcParamString('name'),
                RpcParamString('desc'),
                RpcParamBoolean('active'),
            ),
        ),
        Rpc(name='add_category',
            params = (
                RpcParamInt('aid'),
                RpcParamString('name'),
                RpcParamString('desc'),
                RpcParamFloat('balance'),
            ),
        ),
        Rpc(name='get_categories'),
        Rpc(name='update_category',
            params = (
                RpcParamInt('id'),
                RpcParamInt('aid'),
                RpcParamString('name'),
                RpcParamString('desc'),
                RpcParamFloat('balance'),
                RpcParamBoolean('active'),
            ),
        ),
    )

    def add_account(self, name, desc):
        # Check for duplicate names is done within manager 
        a = Account(id=None, name=name, description=desc, is_active=True)
        return self.manager.save(a)

    def get_accounts(self):
        return self.manager.filter(Account)
  
    def update_account(self, id, name, desc, active):
        a = self.manager.get(Account, id)
       
        # Check for duplicate names is done within manager 
        a.name = name
        a.description = desc
        a.is_active = active
        return self.manager.save(a)

    def add_category(self, aid, name, desc, balance):
        # Check for duplicate names is done within manager 
        c = Category(id=None, aid=aid, name=name, description=desc,\
            balance=balance, is_active=True)
        return self.manager.save(c)

    def get_categories(self):
        return self.manager.filter(Category)
  
    def update_category(self, id, aid, name, desc, balance, active):
        c = self.manager.get(Category, id)
       
        # Check for duplicate names is done within manager 
        c.aid = aid
        c.name = name
        c.description = desc
        c.balance = balance
        c.is_active = active
        return self.manager.save(c)
  
class AccountRpcReqHandler(RpcReqHandler):
    group_cls = AccountRpcGroup

