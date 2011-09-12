/**
 * @file AlertTestImpl.java
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
package com.googlecode.jhb.gwt.client.ui.widget;

public class AlertTestImpl implements Alert {

  private boolean showing = false;
  private String message = "";
  private Handler handler = null;
  
  // ================================================================
  // BEGIN: Alert methods
  // ================================================================
  
  @Override
  public void show(String message) {
    show(message, null);
  }

  @Override
  public void show(String message, Handler handler) {
    this.message = message;
    this.handler = handler;
    showing = true;
  }
  
  // ================================================================
  // END: Alert methods
  // ================================================================

  public void reset() {
    message = "";
    showing = false;
    handler = null;
  }
  
  public boolean isShowing() {
    return showing;
  }
  
  public String getMessage() {
    return message;
  }
  
  public Handler getHandler() {
    return handler;
  }
  
} // AlertTestImpl //
