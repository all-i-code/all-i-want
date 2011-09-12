/**
 * @file DataTestImpl.java
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

import static com.googlecode.jhb.gwt.client.logging.Logging.logger;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DataTestImpl extends ModelJson
 implements Data {

  public static List<Data> parseArray(String json) {
    List<Data> al = new ArrayList<Data>();
    try {
      JSONArray arr = new JSONArray(json);
      for (int i = 0; i < arr.length(); i++) {
        al.add(new DataTestImpl(arr.getJSONObject(i))); 
      }
    } catch (JSONException e) {
      logger().severe("DataTestImpl::parseArray: " +
        e.getLocalizedMessage()); 
    }
    return al;
  } // parseArray //

  public DataTestImpl(String json) {
    super(json);
  }
  
  public DataTestImpl(JSONObject obj) {
    super(obj);
  }


  @Override
  public List<Account> getAccounts() {
    return AccountTestImpl.parseArray(getArray("a"));
  }

  @Override
  public List<Category> getCategories() {
    return CategoryTestImpl.parseArray(getArray("b"));
  }

  @Override
  public List<Split> getSplits() {
    return SplitTestImpl.parseArray(getArray("c"));
  }

  @Override
  public List<Transaction> getTransactions() {
    return TransactionTestImpl.parseArray(getArray("d"));
  }

  @Override
  public List<PaymentType> getPaymentTypes() {
    return PaymentTypeTestImpl.parseArray(getArray("e"));
  }

  @Override
  public List<Bill> getBills() {
    return BillTestImpl.parseArray(getArray("f"));
  }

  @Override
  public List<Budget> getBudgets() {
    return BudgetTestImpl.parseArray(getArray("g"));
  }

  @Override
  public List<BudgetItem> getBudgetItems() {
    return BudgetItemTestImpl.parseArray(getArray("h"));
  }


} // DataTestImpl //
