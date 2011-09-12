/**
 * @file BillTestImpl.java
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

public class BillTestImpl extends ModelJson
 implements Bill {

  public static List<Bill> parseArray(String json) {
    List<Bill> al = new ArrayList<Bill>();
    try {
      JSONArray arr = new JSONArray(json);
      for (int i = 0; i < arr.length(); i++) {
        al.add(new BillTestImpl(arr.getJSONObject(i))); 
      }
    } catch (JSONException e) {
      logger().severe("BillTestImpl::parseArray: " +
        e.getLocalizedMessage()); 
    }
    return al;
  } // parseArray //

  public BillTestImpl(String json) {
    super(json);
  }
  
  public BillTestImpl(JSONObject obj) {
    super(obj);
  }


  @Override
  public int getId() {
    return getInt("a");
  }

  @Override
  public String getName() {
    return getStr("b");
  }

  @Override
  public String getDue() {
    return getStr("c");
  }

  @Override
  public String getRecurrence() {
    return getStr("d");
  }

  @Override
  public String getLastPaid() {
    return getStr("e");
  }

  @Override
  public int getTransactionId() {
    return getInt("f");
  }


} // BillTestImpl //
