/**
 * @file {{ cls.get_java_test_class }}.java
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

public class {{ cls.get_java_test_class }} extends ModelJson
 implements {{ cls.get_java_iface }} {

  public static List<{{ cls.get_java_iface }}> parseArray(String json) {
    List<{{ cls.get_java_iface }}> al = new ArrayList<{{ cls.get_java_iface }}>();
    try {
      JSONArray arr = new JSONArray(json);
      for (int i = 0; i < arr.length(); i++) {
        al.add(new {{ cls.get_java_test_class }}(arr.getJSONObject(i))); 
      }
    } catch (JSONException e) {
      logger().severe("{{ cls.get_java_test_class }}::parseArray: " +
        e.getLocalizedMessage()); 
    }
    return al;
  } // parseArray //

  public {{ cls.get_java_test_class }}(String json) {
    super(json);
  }
  
  public {{ cls.get_java_test_class }}(JSONObject obj) {
    super(obj);
  }

{% for f in cls.get_fields %}
  {{ f.get_java_test_getter|safe }}{% endfor %}

} // {{ cls.get_java_test_class }} //
