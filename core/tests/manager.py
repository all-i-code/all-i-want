'''
#
# File: manager.py
# Description: Unit tests for jhb.core.manager.Manager
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

import unittest
from jhb.core.manager import Manager
from jhb.core.access import MemoryAccess
from jhb.core.model import Account, Category

class ManagerTest(unittest.TestCase):
    def setUp(self):
        # set up initial account structure
        self.manager = Manager(MemoryAccess())
        from jhb.core.tests.fixture import ACCOUNTS, CATEGORIES
        self.accounts, self.categories = (ACCOUNTS, CATEGORIES)
        self.splits = []
        self.transactions = []
        self.payment_types = []
        self.bills = []
        self.budgets = []
        self.budget_items = []

    def _create_data(self):
        return Data(accounts=self.accounts, categories=self.categories,
            splits=self.splits, transactions=self.transactions,
            payment_types=self.payment_types, bills=self.bills,
            budgets=self.budgets, budget_items=self.budget_items)

    def test_execute(self):
        pass

    def test_undo(self):
        pass

    def test_update(self):
        pass

    def test_save_as_recurring(self):
        pass

    def test_execute_recurring(self):
        pass

if __name__ == '__main__':
    unittest.main()

