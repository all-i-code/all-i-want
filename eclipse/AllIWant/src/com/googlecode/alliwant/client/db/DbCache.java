/**
 * @file DbCache.java
 * @author Adam Meadows
 *
 * Copyright 2011 Adam Meadows 
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
*/
package com.googlecode.jhb.gwt.client.db;

import java.util.List;

import com.googlecode.jhb.gwt.client.model.Account;
import com.googlecode.jhb.gwt.client.model.Bill;
import com.googlecode.jhb.gwt.client.model.Budget;
import com.googlecode.jhb.gwt.client.model.BudgetItem;
import com.googlecode.jhb.gwt.client.model.Category;
import com.googlecode.jhb.gwt.client.model.Data;
import com.googlecode.jhb.gwt.client.model.PaymentType;
import com.googlecode.jhb.gwt.client.model.Split;
import com.googlecode.jhb.gwt.client.model.Transaction;
import com.googlecode.jhb.gwt.client.model.User;

public interface DbCache {

  User getUser();
  void updateUser(User user);
  
  Account getAccount(int id);
  List<Category> getCategories(int accountId);
  List<Account> getAccounts();
  void updateAccounts(List<Account> accounts);
  
  Category getCategory(int id);
  List<Category> getCategories();
  void updateCategories(List<Category> categories);
  
  List<Bill> getBills();
  void updateBills(List<Bill> bills);
  
  Budget getBudget(int id);
  List<BudgetItem> getBudgetItems(int budgetId);
  List<Budget> getBudgets();
  void updateBudgets(List<Budget> budgets);
  void updateBudgetItems(List<BudgetItem> budgetItems);
  
  void update(Data data);
  void reset(Data data);
  
  List<PaymentType> getPaymentTypes();
  void updatePaymentTypes(List<PaymentType> paymentTypes);
  
  Transaction getTransaction(int id);
  Transaction getReference(int transactionId);
  Bill getBill(int transactionId);
  PaymentType getPaymentType(int transactionId);
  List<Split> getSplits(int transactionId);
  List<Transaction> getTransactions();
  List<Transaction> getTransactions(boolean isActive);
  List<Transaction> getTransactions(String query);
  List<Transaction> getTransactions(boolean isActive, String query);
  void updateTransactions(List<Transaction> transactions);
  
  Category getTo(int splitId);
  Category getFrom(int splitId);
  void updateSplits(List<Split> splits);
  
} // DbCache //
