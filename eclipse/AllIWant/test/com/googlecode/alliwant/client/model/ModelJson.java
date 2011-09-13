/**
 * @file ModelJson.java
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
package com.googlecode.alliwant.client.model;

import static com.googlecode.alliwant.client.logging.Logging.logger;

import org.json.JSONException;
import org.json.JSONObject;


public class ModelJson {

  protected JSONObject obj = null;
  
  protected ModelJson(String json) {
    parse(json);
  }
  
  protected ModelJson(JSONObject obj) {
    this.obj = obj;
  }
  
  protected int getInt(String name) {
    try {
      return obj.getInt(name);
    } catch (JSONException e) {
      fieldError("getInt", name, e);
    }
    return -1;
  } // getInt //

  protected String getStr(String name) {
    try {
      return obj.getString(name);
    } catch (JSONException e) {
      fieldError("getStr", name, e);
    }
    return "";
  } // getStr //
  
  protected double getDbl(String name) {
    try {
      return obj.getDouble(name);
    } catch (JSONException e) {
      fieldError("getDbl", name, e);
    }
    return 0.0;
  } // getDbl //
  
  protected boolean getBool(String name) {
    try {
      return obj.getBoolean(name);
    } catch (JSONException e) {
      fieldError("getBool", name, e);
    }
    return false;
  } // getBool //
  
  protected String getArray(String name) {
    try {
      return obj.getJSONArray(name).toString();
    } catch (JSONException e) {
      fieldError("getArray", name, e);
    }
    return null;
  } // getArray //
  
  private void parse(String json) {
    obj = null;
    try {
      obj = new JSONObject(json);
    } catch (JSONException e) {
      logger().severe("ModelJson::parse: Invalid json! [" + 
       json + "], error: " + e.getLocalizedMessage());
    }
  } // parse //
  
  private void fieldError(String method, String field, JSONException e) {
    logger().severe("ModelJson::" + method + "(" + field + "), error: " +
     e.getLocalizedMessage());
  } // error //
  
} // ModelJson //
