/**
 * @file GoodbyeViewTestImpl.java
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
package com.googlecode.alliwant.client.ui;

import com.googlecode.alliwant.client.i18n.JhbConstants;
import com.googlecode.alliwant.client.i18n.JhbConstantsTestImpl;

public class GoodbyeViewTestImpl extends JhbViewTestImpl implements GoodbyeView {

  private String message = "", logoutURL = "", url = "TEST_URL";
  private boolean logoutVisible = false;
  private JhbConstants jhbc = new JhbConstantsTestImpl();
  
  // ================================================================
  // BEGIN: GoodbyeView methods
  // ================================================================

  @Override
  public void setMessage(String message) {
    this.message = message;
  }
  
  @Override
  public void setLogoutURL(String url) {
    logoutURL = url;
  }
  
  @Override
  public void setLogoutVisible(boolean visible) {
    logoutVisible = visible;
  }

  @Override
  public JhbConstants getJhbc() {
    return jhbc;
  }
  
  @Override
  public String getURL() {
    return url;
  }

  // ================================================================
  // END: GoodbyeView methods
  // ================================================================

  public String getMessage() {
    return message;
  }
  
  public String getLogoutURL() {
    return logoutURL;
  }
  
  public boolean isLogoutVisible() {
    return logoutVisible;
  }

  public void setURL(String url) {
    this.url = url;
  }
  
} // GoodbyeViewTestImpl //
