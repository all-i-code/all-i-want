/**
 * @file DataImpl.java
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
 * WARNING: This file is auto-generated, don't modify it directly,
 * instead modify jhb/core/model.py and re-generate
 *
*/

package com.googlecode.jhb.gwt.client.model;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

public class DataImpl extends JavaScriptObject implements Data {
  
  public static final List<Data> decodeList(JsArray<DataImpl> js_arr) {
    List<Data> arr = new ArrayList<Data>();
    for (int i = 0; i < js_arr.length(); i++) arr.add(js_arr.get(i));
    return arr; 
  }

  public static final List<Data> decodeList(String json) {
    return decodeList(decodeArray(json)); 
  }

  public static final native JsArray<DataImpl> decodeArray(String json) /*-{
    eval('var js = ' + json);
    return js;
  }-*/;
  
  public static final native DataImpl decode(String json) /*-{
    eval('var js = ' + json);
    return js;
  }-*/;

  protected DataImpl() {}

  private final native JsArray<AccountImpl> getAccountsJs()/*-{
    return this.a;
  }-*/;

  @Override
  public final List<Account> getAccounts() {
    return AccountImpl.decodeList(getAccountsJs());
  }

  private final native JsArray<CategoryImpl> getCategoriesJs()/*-{
    return this.b;
  }-*/;

  @Override
  public final List<Category> getCategories() {
    return CategoryImpl.decodeList(getCategoriesJs());
  }

  private final native JsArray<SplitImpl> getSplitsJs()/*-{
    return this.c;
  }-*/;

  @Override
  public final List<Split> getSplits() {
    return SplitImpl.decodeList(getSplitsJs());
  }

  private final native JsArray<TransactionImpl> getTransactionsJs()/*-{
    return this.d;
  }-*/;

  @Override
  public final List<Transaction> getTransactions() {
    return TransactionImpl.decodeList(getTransactionsJs());
  }

  private final native JsArray<PaymentTypeImpl> getPaymentTypesJs()/*-{
    return this.e;
  }-*/;

  @Override
  public final List<PaymentType> getPaymentTypes() {
    return PaymentTypeImpl.decodeList(getPaymentTypesJs());
  }

  private final native JsArray<BillImpl> getBillsJs()/*-{
    return this.f;
  }-*/;

  @Override
  public final List<Bill> getBills() {
    return BillImpl.decodeList(getBillsJs());
  }

  private final native JsArray<BudgetImpl> getBudgetsJs()/*-{
    return this.g;
  }-*/;

  @Override
  public final List<Budget> getBudgets() {
    return BudgetImpl.decodeList(getBudgetsJs());
  }

  private final native JsArray<BudgetItemImpl> getBudgetItemsJs()/*-{
    return this.h;
  }-*/;

  @Override
  public final List<BudgetItem> getBudgetItems() {
    return BudgetItemImpl.decodeList(getBudgetItemsJs());
  }


} // DataImpl
