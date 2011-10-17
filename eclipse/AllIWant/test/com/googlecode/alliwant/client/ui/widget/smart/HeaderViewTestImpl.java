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

import com.googlecode.alliwant.client.i18n.AiwConstants;
import com.googlecode.alliwant.client.i18n.AiwConstantsTestImpl;
import com.googlecode.alliwant.client.i18n.AiwMessages;
import com.googlecode.alliwant.client.i18n.AiwMessagesTestImpl;
import com.googlecode.alliwant.client.ui.JhbViewTestImpl;

public class HeaderViewTestImpl extends JhbViewTestImpl implements HeaderView {

  private String user = "", logoutURL = "", redirectTo = "";
  private boolean listsActive = false, groupsActive = false,
   reqsActive = false, reqsVisible = false, settingsActive = false;

  private Presenter presenter = null;
  private AiwMessagesTestImpl aiwm = new AiwMessagesTestImpl();
  private AiwConstantsTestImpl aiwc = new AiwConstantsTestImpl();
  
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
  public void setRequestsActive(boolean active) {
    reqsActive = active;
  }

  @Override
  public void setRequestsVisible(boolean visible) {
    reqsVisible = visible;
  }

  @Override
  public void setSettingsActive(boolean active) {
    settingsActive = active;
  }

  @Override
  public void redirect(String url) {
    redirectTo = url;
  }
  
  @Override
  public AiwConstants getConsts() {
    return aiwc;
  }
  
  @Override
  public AiwMessages getMsgs() {
    return aiwm;
  }
  
  @Override
  public void setPresenter(Presenter presenter) {
    this.presenter = presenter;
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
  
  public boolean isRequestsActive() {
    return reqsActive;
  }
  
  public boolean isRequestsVisible() {
    return reqsVisible;
  }
  
  public boolean isSettingsActive() {
    return settingsActive;
  }
  
  public Presenter getPresenter() {
    return presenter;
  }

} // HeaderViewTestImpl //
