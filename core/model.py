'''
#
# File: model.py
# Description: JHB Model classes
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

from jhb.core.meta import JhbModel, JhbFieldInt, JhbFieldString, JhbFieldText, \
    JhbFieldFloat, JhbFieldBoolean, JhbFieldModelArray

class Account(JhbModel):
    fields = (
        JhbFieldInt(name='id'),
        JhbFieldString(name='name'),
        JhbFieldText(name='description'),
        JhbFieldBoolean(name='is_active'),
    )

class Category(JhbModel):
    fields = (
        JhbFieldInt(name='id'),
        JhbFieldInt(name='aid'),
        JhbFieldString(name='name'),
        JhbFieldText(name='description'),
        JhbFieldFloat(name='balance'),
        JhbFieldBoolean(name='is_active'),
    )

class Split(JhbModel):
    fields = (
        JhbFieldInt(name='id'),
        JhbFieldInt(name='transaction_id'),
        JhbFieldInt(name='from_id'),
        JhbFieldInt(name='to_id'),
        JhbFieldFloat(name='amount'),
    )

class Transaction(JhbModel):
    '''Base class for all transaction types'''
    fields = (
        JhbFieldInt(name='id'),
        JhbFieldString(name='type'),
        JhbFieldString(name='location'),
        JhbFieldText(name='description'),
        JhbFieldString(name='date'),
        JhbFieldString(name='posting_date'),
        JhbFieldBoolean(name='is_active'),
        JhbFieldInt(name='reference_id'),
        JhbFieldInt(name='payment_type_id'),
        JhbFieldInt('bill_id'),
        JhbFieldBoolean('is_recurring'),
    )

class PaymentType(JhbModel):
    '''Base class for all Payment Types'''
    types = (
        'credit',
        'debit',
        'check',
    )
    
    fields = (
        JhbFieldInt(name='id'),
        JhbFieldString(name='type'),
        JhbFieldString(name='name'),
        JhbFieldString(name='number'),
        JhbFieldInt(name='to_id'),
        JhbFieldString(name='payable_to'),
    )

class Bill(JhbModel):
    fields = (
        JhbFieldInt(name='id'),
        JhbFieldString(name='name'),
        JhbFieldString(name='due'),
        JhbFieldString(name='recurrence'),
        JhbFieldString(name='last_paid'),
        JhbFieldInt(name='transaction_id'),
    )

class Budget(JhbModel):
    fields = (
        JhbFieldInt(name='id'),
        JhbFieldString(name='name'),
        JhbFieldString(name='when_done'),
        JhbFieldInt(name='overflow_category_id'),
    )

class BudgetItem(JhbModel):
    fields = (
        JhbFieldInt(name='id'),
        JhbFieldInt(name='budget_id'),
        JhbFieldInt(name='order'),
        JhbFieldInt(name='category_id'),
        JhbFieldFloat(name='amount'),
    )

class Data(JhbModel):
    db = False 
    fields = (
        JhbFieldModelArray(type=Account),
        JhbFieldModelArray(type=Category),
        JhbFieldModelArray(type=Split),
        JhbFieldModelArray(type=Transaction),
        JhbFieldModelArray(type=PaymentType),
        JhbFieldModelArray(type=Bill),
        JhbFieldModelArray(type=Budget),
        JhbFieldModelArray(type=BudgetItem),
    )

class User(JhbModel):
    db = False
    fields = (
        JhbFieldString(name='email'),
        JhbFieldString(name='nickname'),
        JhbFieldString(name='user_id'),
        JhbFieldString(name='login_url'),
        JhbFieldString(name='logout_url'),
    )

class FailureReport(JhbModel):
    db = False    
    fields = (
        JhbFieldString(name='error_type'),
        JhbFieldString(name='message'),
        JhbFieldString(name='traceback'),
    )

def get_all_classes():
    from jhb.core.meta import JhbModelManager as manager
    return manager.models

def get_db_classes():
    from jhb.core.meta import JhbModelManager as manager
    return (cls for cls in manager.models if cls.in_db())

