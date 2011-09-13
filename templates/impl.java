/**
 * @file {{ cls.get_java_class }}.java
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

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

public class {{ cls.get_java_class }} extends JavaScriptObject implements {{ cls.get_java_iface }} {
  
  public static final List<{{ cls.get_java_iface }}> decodeList(JsArray<{{ cls.get_java_class}}> js_arr) {
    List<{{ cls.get_java_iface }}> arr = new ArrayList<{{ cls.get_java_iface }}>();
    for (int i = 0; i < js_arr.length(); i++) arr.add(js_arr.get(i));
    return arr; 
  }

  public static final List<{{ cls.get_java_iface }}> decodeList(String json) {
    return decodeList(decodeArray(json)); 
  }

  public static final native JsArray<{{ cls.get_java_class }}> decodeArray(String json) /*-{
    eval('var js = ' + json);
    return js;
  }-*/;
  
  public static final native {{ cls.get_java_class }} decode(String json) /*-{
    eval('var js = ' + json);
    return js;
  }-*/;
{% if cls.has_create %}  
  public static final native {{ cls.get_java_class }} create(
   {{ cls.get_java_create_params }}
  ) /*-{
    return {
      {{ cls.get_java_create_eval|safe }} 
    }; 
  }-*/;
{% endif %}
  protected {{ cls.get_java_class }}() {}
{% for f in cls.get_fields %}
  {{ f.get_java_getter|safe }}{% endfor %}

} // {{ cls.get_java_class }}
