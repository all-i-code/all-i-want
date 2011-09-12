'''
#
# File: manager.py
# Description: The brains behind JHB, the logic that pulls it all together
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

from jhb.core.model import Account, Category, Transaction, Split, Data
from jhb.core.exception import DuplicateNameError, InactiveCategoryError

class Manager(object):

    def __init__(self, access):
        '''Initialize this Manager with a JhbAccess to get at data'''
        self.access = access

    def get(self, cls, id):
        return self.access.get(cls, id)

    def filter(self, cls, field=None, value=None):
        return self.access.filter(cls, field, value)

    def get_data(self):
        return self.access.get_data()

    def save(self, obj):
        if obj.__class__ == Account:
            for a in self.filter(Account):
                if a.id == obj.id: continue
                if a.name == obj.name:
                    raise DuplicateNameError(Account, obj.name)

        elif obj.__class__ == Category:
            for c in self.filter(Category):
                if c.id == obj.id or c.aid != obj.aid: continue
                if c.name == obj.name:
                    raise DuplicateNameError(Category, obj.name)
 
        return self.access.save(obj)

    def execute(self, trans, splits):
        '''Execute a transaction, returning a Data obj with all updated data''' 
        _ = lambda obj: self.save(obj)
        self._check_invalid_cats(splits)
        
        # Add new transaction and splits 
        new_trans = _(trans)
        cats = []

        def getc(id):
            cat = find(lambda c: c.id == id, cats)
            if cat is None:
                cat = self.get(Category, id)
                cats.append(cat)
            return cat

        def process(split):
            split.transaction_id = new_trans.id
            if split.from_id is not None:
                getc(split.from_id).balance -= split.amount
            
            if split.to_id is not None:
                getc(split.to_id).balance += split.amount
            
            return _(split)
       
        new_splits = [ process(s) for s in splits ]

        return Data(dict(categories=[ _(c) for c in cats],
            transactions=[new_trans], splits=new_splits))

    def undo(self, tid):
        '''Undo the given transaction, returning a Data obj with updated data'''
        _ = lambda obj: self.save(obj)
       
        splits = self.filter(Split, 'transaction_id', tid)
        self._check_invalid_cats(splits)

        # Add new transaction and splits 
        trans = self.get(Transaction, 'id', tid)
        trans.is_active = False
        updated_trans = _(trans)
        cats = []

        def getc(id):
            cat = find(lambda c: c.id == id, cats)
            if cat is None:
                cat = self.get(Category, 'id', id)
                cats.append(cat)
            return cat

        def ud(split):
            if split.from_id is not None:
                getc(split.from_id).balance += split.amount
            
            if split.to_id is not None:
                getc(split.to_id).balance -= split.amount
            
        for s in splits:
            ud(s)

        return Data(dict(categories=[ _(c) for c in cats],
            transactions=[updated_trans]))

    def update(self, trans, splits):
        '''Update a transaction with new info/splits'''
        _ = lambda obj: self.save(obj)
        tid = trans.id 
        self._check_invalid_cats(splits)
        
        # First we undo the old transaction
        self.undo(tid)
        old_trans = self.get(Transaction, tid)
       
        # Then we execute the new one that references the old one 
        trans.id = None
        trans.reference_id = tid
        data = self.execute(trans, splits)
        data.transactions.insert(0, old_trans)

        return data

    def save_as_recurring(self, trans, splits):
        '''Save given transaction/splits as a recurring transaction'''
        _ = lambda obj: self.save(obj)
        self._check_invalid_cats(splits)
        trans.is_recurring = True
        rec_trans = _(trans)
        def p(split):
            split.transaction_id = rec_trans.id
            return _(split)

        rec_splits = [ p(s) for s in splits ]

        return Data(dict(transactions=[rec_trans], splits=rec_splits))

    def execute_recurring(self, tid):
        '''Execute a given recurring transaction'''
        _ = lambda obj: obj.clone()
        trans = _(self.get(Transaction, tid))
        splits = [ _(s) for s in self.filter(Split, 'transaction_id', tid) ]
        self._check_invalid_cats(splits)
        trans.id = None
        trans.is_recurring = False
        return self.execute(trans, splits)

    def _check_invalid_cats(self, splits):
        gc = lambda x: self.get(Category, x) if x is not None else None
        _ = lambda x: x.is_active if x is not None else True
        for s in splits:
            for c in (gc(s.from_id), gc(s.to_id)):
                if not _(c): raise InactiveCategoryError(c)

