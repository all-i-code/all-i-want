/**
 * @file ListPermissionTestImpl.java
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
 * instead modify core/model.py and re-generate
 *
*/

package com.googlecode.alliwant.client.model;

import static com.googlecode.alliwant.client.logging.Logging.logger;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ListPermissionTestImpl extends ModelJson
 implements ListPermission {

  public static List<ListPermission> parseArray(String json) {
    List<ListPermission> al = new ArrayList<ListPermission>();
    try {
      JSONArray arr = new JSONArray(json);
      for (int i = 0; i < arr.length(); i++) {
        al.add(new ListPermissionTestImpl(arr.getJSONObject(i))); 
      }
    } catch (JSONException e) {
      logger().severe("ListPermissionTestImpl::parseArray: " +
        e.getLocalizedMessage()); 
    }
    return al;
  } // parseArray //

  public ListPermissionTestImpl(String json) {
    super(json);
  }
  
  public ListPermissionTestImpl(JSONObject obj) {
    super(obj);
  }


  @Override
  public int getId() {
    return getInt("a");
  }

  @Override
  public int getOwnerId() {
    return getInt("b");
  }

  @Override
  public String getEmail() {
    return getStr("c");
  }


} // ListPermissionTestImpl //
