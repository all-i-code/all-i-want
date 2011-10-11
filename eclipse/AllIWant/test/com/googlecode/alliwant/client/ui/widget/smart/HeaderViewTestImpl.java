/**
 * @file HeaderViewTestImpl.java
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
package com.googlecode.alliwant.client.ui.widget.smart;

import com.googlecode.alliwant.client.i18n.AiwMessages;
import com.googlecode.alliwant.client.i18n.AiwMessagesTestImpl;
import com.googlecode.alliwant.client.ui.JhbViewTestImpl;

public class HeaderViewTestImpl extends JhbViewTestImpl implements HeaderView {

  private String user = "", logoutURL = "", redirectTo = "", 
   url = "TEST_URL";
  private boolean listsActive = false, groupsActive = false,
   settingsActive = false;
 
  private AiwMessagesTestImpl aiwm = new AiwMessagesTestImpl();
  
  // ================================================================
  // BEGIN: HeaderView methods 
  // ================================================================
  
  @Override
  public void setUser(String user) {
    this.user = user;
  }

  @Override
  public void setLogoutURL(String url) {
    logoutURL = url;
  }

  @Override
  public void setListsActive(boolean active) {
    listsActive = active;
  }

  @Override
  public void setGroupsActive(boolean active) {
    groupsActive = active;
  }

  @Override
  public void setSettingsActive(boolean active) {
    settingsActive = active;
  }

  @Override
  public String getURL() {
    return url;
  }

  @Override
  public void redirect(String url) {
    redirectTo = url;
  }
  
  @Override
  public AiwMessages getMsgs() {
    return aiwm;
  }
  
  // ================================================================
  // END: HeaderView methods 
  // ================================================================
 
  public String getUser() {
    return user;
  }
  
  public String getLogoutURL() {
    return logoutURL;
  }
  
  public String getRedirectTo() {
    return redirectTo;
  }
  
  public boolean isListsActive() {
    return listsActive;
  }
  
  public boolean isGroupsActive() {
    return groupsActive;
  }
  
  public boolean isSettingsActive() {
    return settingsActive;
  }
  
  public void setURL(String url) {
    this.url = url;
  }

} // HeaderViewTestImpl //
