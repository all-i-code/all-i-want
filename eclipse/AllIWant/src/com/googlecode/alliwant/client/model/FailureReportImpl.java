/**
 * @file FailureReportImpl.java
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

package com.googlecode.alliwant.client.model;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

public class FailureReportImpl extends JavaScriptObject implements FailureReport {
  
  public static final List<FailureReport> decodeList(JsArray<FailureReportImpl> js_arr) {
    List<FailureReport> arr = new ArrayList<FailureReport>();
    for (int i = 0; i < js_arr.length(); i++) arr.add(js_arr.get(i));
    return arr; 
  }

  public static final List<FailureReport> decodeList(String json) {
    return decodeList(decodeArray(json)); 
  }

  public static final native JsArray<FailureReportImpl> decodeArray(String json) /*-{
    eval('var js = ' + json);
    return js;
  }-*/;
  
  public static final native FailureReportImpl decode(String json) /*-{
    eval('var js = ' + json);
    return js;
  }-*/;

  protected FailureReportImpl() {}

  @Override
  public final native String getErrorType() /*-{
    return this.a;
  }-*/;

  @Override
  public final native String getMessage() /*-{
    return this.b;
  }-*/;

  @Override
  public final native String getTraceback() /*-{
    return this.c;
  }-*/;


} // FailureReportImpl
