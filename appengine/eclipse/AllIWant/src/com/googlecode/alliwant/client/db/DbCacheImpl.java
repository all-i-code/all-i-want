/**
 * @file DbCacheImpl.java
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

public class DbCacheImpl implements DbCache {

  private User user = null;
  private Map<Integer, Account> accountMap = new HashMap<Integer, Account>();
  private Map<Integer, Bill> billMap = new HashMap<Integer, Bill>();
  private Map<Integer, Budget> budgetMap = new HashMap<Integer, Budget>();
  private Map<Integer, BudgetItem> budgetItemMap = 
   new HashMap<Integer, BudgetItem>();
  private Map<Integer, Category> categoryMap = new HashMap<Integer, Category>();
  private Map<Integer, PaymentType> paymentTypeMap =
   new HashMap<Integer, PaymentType>();
  private Map<Integer, Split> splitMap = new HashMap<Integer, Split>();
  private Map<Integer, Transaction> transactionMap =
   new HashMap<Integer, Transaction>();

  private Comparator<Account> acctCmp = new Comparator<Account>() {
    public int compare(Account o1, Account o2) {
      return o1.getName().compareTo(o2.getName());
    }
  };
  
  private Comparator<Bill> billCmp = new Comparator<Bill>() {
    public int compare(Bill o1, Bill o2) {
      return o1.getName().compareTo(o2.getName());
    }
  };
  
  private Comparator<Budget> budgetCmp = new Comparator<Budget>() {
    public int compare(Budget o1, Budget o2) {
      return o1.getName().compareTo(o2.getName());
    }
  };
  
  private Comparator<BudgetItem> budgetItemCmp = new Comparator<BudgetItem>() {
    public int compare(BudgetItem o1, BudgetItem o2) {
      return new Integer(o1.getOrder()).compareTo(new Integer(o2.getOrder()));
    }
  };
  
  private Comparator<Category> catCmp = new Comparator<Category>() {
    public int compare(Category o1, Category o2) {
      return o1.getName().compareTo(o2.getName());
    }
  };
  
  private Comparator<PaymentType> ptCmp = new Comparator<PaymentType>() {
    public int compare(PaymentType o1, PaymentType o2) {
      return o1.getName().compareTo(o2.getName());
    }
  };
  
  private Comparator<Split> splitCmp = new Comparator<Split>() {
    public int compare(Split o1, Split o2) {
      return new Integer(o1.getId()).compareTo(new Integer(o2.getId()));
    }
  };
  
  private Comparator<Transaction> transCmp = new Comparator<Transaction>() {
    public int compare(Transaction o1, Transaction o2) {
      // TODO: redo this to sort by date?
      return new Integer(o1.getId()).compareTo(new Integer(o2.getId()));
    }
  };
  
  public DbCacheImpl() {
    
  }
  
  // =====================================================================
  // BEGIN: DbCache methods
  // =====================================================================
 
  @Override
  public User getUser() {
    return user;
  }
 
  @Override
  public void updateUser(User user) {
    this.user = user;
  }
  
  @Override
  public Account getAccount(int id) {
    return accountMap.get(id);
  }
  
  @Override
  public List<Category> getCategories(int accountId) {
    List<Category> categories = new ArrayList<Category>();
    for (Category cat : categoryMap.values())
      if (cat.getAid() == accountId) categories.add(cat);
    Collections.sort(categories, catCmp);
    return categories;
  }
  
  @Override
  public List<Account> getAccounts() {
    List<Account> accounts = new ArrayList<Account>(accountMap.values());
    Collections.sort(accounts, acctCmp);
    return accounts;
  }
  
  @Override
  public void updateAccounts(List<Account> accounts) {
    for (Account a : accounts)
      accountMap.put(a.getId(), a);
  }
  
  @Override
  public Category getCategory(int id) {
    return categoryMap.get(id);
  }
  
  @Override
  public List<Category> getCategories() {
    List<Category> cats = new ArrayList<Category>(categoryMap.values());
    Collections.sort(cats, catCmp); 
    return cats;
  }
 
  @Override
  public void updateCategories(List<Category> categories) {
    for (Category c : categories)
      categoryMap.put(c.getId(), c);
  }
  
  @Override
  public List<Bill> getBills() {
    List<Bill> bills = new ArrayList<Bill>(billMap.values());
    Collections.sort(bills, billCmp);
    return bills;
  }
  
  @Override
  public void updateBills(List<Bill> bills) {
    for (Bill b : bills)
      billMap.put(b.getId(), b);
  }
 
  @Override
  public Budget getBudget(int id) {
    return budgetMap.get(id);
  }
  
  @Override
  public List<BudgetItem> getBudgetItems(int budgetId) {
    List<BudgetItem> items = new ArrayList<BudgetItem>();
    for (BudgetItem item : budgetItemMap.values())
      if (item.getBudgetId() == budgetId) items.add(item);
    Collections.sort(items, budgetItemCmp);
    return items;
  }
  
  @Override
  public List<Budget> getBudgets() {
    List<Budget> budgets = new ArrayList<Budget>(budgetMap.values());
    Collections.sort(budgets, budgetCmp);
    return budgets;
  }
  
  @Override
  public void updateBudgets(List<Budget> budgets) {
    for (Budget b : budgets)
      budgetMap.put(b.getId(), b);
  }
  
  @Override
  public void updateBudgetItems(List<BudgetItem> budgetItems) {
    for (BudgetItem item : budgetItems)
      budgetItemMap.put(item.getId(), item);
  }
  
  @Override
  public void update(Data data) {
    updateAccounts(data.getAccounts());
    updateBills(data.getBills());
    updateBudgets(data.getBudgets());
    updateBudgetItems(data.getBudgetItems());
    updateCategories(data.getCategories());
    updatePaymentTypes(data.getPaymentTypes());
    updateSplits(data.getSplits());
    updateTransactions(data.getTransactions());
  }
  
  @Override
  public void reset(Data data) {
    accountMap.clear();
    billMap.clear();
    budgetMap.clear();
    budgetItemMap.clear();
    categoryMap.clear();
    paymentTypeMap.clear();
    splitMap.clear();
    transactionMap.clear();
    update(data);
  }
  
  @Override
  public List<PaymentType> getPaymentTypes() {
    List<PaymentType> pts = new ArrayList<PaymentType>(paymentTypeMap.values());
    Collections.sort(pts, ptCmp);
    return pts;
  }
  
  @Override
  public void updatePaymentTypes(List<PaymentType> paymentTypes) {
    for (PaymentType type : paymentTypes)
      paymentTypeMap.put(type.getId(), type);
  }
  
  @Override
  public Transaction getTransaction(int id) {
    return transactionMap.get(id);
  }
 
  @Override
  public Transaction getReference(int transactionId) {
    Transaction t = transactionMap.get(transactionId);
    return transactionMap.get(t.getReferenceId());
  }
  
  @Override
  public Bill getBill(int transactionId) {
    Transaction t = transactionMap.get(transactionId);
    return billMap.get(t.getBillId());
  }
  
  @Override
  public PaymentType getPaymentType(int transactionId) {
    Transaction t = transactionMap.get(transactionId);
    return paymentTypeMap.get(t.getPaymentTypeId());
  }
  
  @Override
  public List<Split> getSplits(int transactionId) {
    List<Split> splits = new ArrayList<Split>();
    for (Split split : splitMap.values())
      if (split.getTransactionId() == transactionId) splits.add(split);
    Collections.sort(splits, splitCmp);
    return splits;
  }
  
  @Override
  public List<Transaction> getTransactions() {
    List<Transaction> ts = new ArrayList<Transaction>(transactionMap.values());
    Collections.sort(ts, transCmp);
    return ts;
  }
  
  @Override
  public List<Transaction> getTransactions(boolean isActive) {
    return getTransactions(true, isActive, "");
  }
  
  @Override
  public List<Transaction> getTransactions(String query) {
    return getTransactions(false, false, query);
  }
  
  @Override
  public List<Transaction> getTransactions(boolean isActive, String query) {
    return getTransactions(true, isActive, query);
  }
  
  @Override
  public void updateTransactions(List<Transaction> transactions) {
    for (Transaction trans : transactions)
      transactionMap.put(trans.getId(), trans);
  }
  
  @Override
  public Category getTo(int splitId) {
    Split split = splitMap.get(splitId);
    return categoryMap.get(split.getToId());
  }
  
  @Override
  public Category getFrom(int splitId) {
    Split split = splitMap.get(splitId);
    return categoryMap.get(split.getFromId());
  }
  
  @Override
  public void updateSplits(List<Split> splits) {
    for (Split split : splits)
      splitMap.put(split.getId(), split);
  }
  
  // =====================================================================
  // END: DbCache methods
  // =====================================================================

  private List<Transaction> getTransactions(boolean matchActive, 
   boolean isActive, String query) {
    List<Transaction> transactions = new ArrayList<Transaction>();
    for (Transaction t : transactionMap.values()) {
      if (matchActive && (isActive != t.isActive())) continue;
      if ((query.length() > 0) && !matches(t, query)) continue;
      transactions.add(t);
    }
    Collections.sort(transactions, transCmp);
    return transactions;
  }
  
  private boolean matches(Transaction transaction, String query) {
    int tid = transaction.getId();
    if (matches(transaction.getDescription(), query)) return true;
    if (matches(transaction.getLocation(), query)) return true;
    if (matches(getPaymentType(transaction.getId()).getName(), query)) return true;
    for (Split s : getSplits(tid)) {
      Category to = getTo(s.getId()),  from = getFrom(s.getId());
      if ((null != to) && matches(to.getName(), query)) return true;
      if ((null != to) && matches(to.getDescription(), query)) return true;
      if ((null != from) && matches(from.getName(), query)) return true;
      if ((null != from) && matches(from.getDescription(), query)) return true;
    }
    return false;
  } // matches //
  
  private boolean matches(String value, String query) {
    return value.toLowerCase().contains(query.toLowerCase());
  } // matches //
  
} // DbCacheImpl //
