'''
#
# File: models.py
# Description: JHB Model objects
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
# WARNING: This file is auto-generated, don't modify directly, 
# instead modify templates/models.py and re-generate
#
'''

from google.appengine.ext import db
from models_meta import JhbDb


class AccountDb(JhbDb):
    name = db.StringProperty()
    description = db.StringProperty(multiline=True)
    is_active = db.BooleanProperty()
    
class CategoryDb(JhbDb):
    aid = db.IntegerProperty()
    name = db.StringProperty()
    description = db.StringProperty(multiline=True)
    balance = db.FloatProperty()
    is_active = db.BooleanProperty()
    
class SplitDb(JhbDb):
    transaction_id = db.IntegerProperty()
    from_id = db.IntegerProperty()
    to_id = db.IntegerProperty()
    amount = db.FloatProperty()
    
class TransactionDb(JhbDb):
    type = db.StringProperty()
    location = db.StringProperty()
    description = db.StringProperty(multiline=True)
    date = db.StringProperty()
    posting_date = db.StringProperty()
    is_active = db.BooleanProperty()
    reference_id = db.IntegerProperty()
    payment_type_id = db.IntegerProperty()
    bill_id = db.IntegerProperty()
    is_recurring = db.BooleanProperty()
    
class PaymentTypeDb(JhbDb):
    type = db.StringProperty()
    name = db.StringProperty()
    number = db.StringProperty()
    to_id = db.IntegerProperty()
    payable_to = db.StringProperty()
    
class BillDb(JhbDb):
    name = db.StringProperty()
    due = db.StringProperty()
    recurrence = db.StringProperty()
    last_paid = db.StringProperty()
    transaction_id = db.IntegerProperty()
    
class BudgetDb(JhbDb):
    name = db.StringProperty()
    when_done = db.StringProperty()
    overflow_category_id = db.IntegerProperty()
    
class BudgetItemDb(JhbDb):
    budget_id = db.IntegerProperty()
    order = db.IntegerProperty()
    category_id = db.IntegerProperty()
    amount = db.FloatProperty()
    

