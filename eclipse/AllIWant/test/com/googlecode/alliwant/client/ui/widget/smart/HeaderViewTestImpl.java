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

import com.googlecode.alliwant.client.ui.JhbViewTestImpl;

public class HeaderViewTestImpl extends JhbViewTestImpl implements HeaderView {

  private String nickname = "", email = "", logoutURL = "", redirectTo = "", 
   url = "TEST_URL";
  private boolean dashboardActive = false, balanceActive = false,
   transactionsActive = false, reportsActive = false, billsActive = false,
   budgetActive = false, settingsActive = false;
  
  // ================================================================
  // BEGIN: HeaderView methods 
  // ================================================================
  
  @Override
  public void setNickname(String nickname) {
    this.nickname = nickname;
  }

  @Override
  public void setEmail(String email) {
    this.email = email;
  }

  @Override
  public void setLogoutURL(String url) {
    logoutURL = url;
  }

  @Override
  public void setDashboardActive(boolean active) {
    dashboardActive = active;
  }

  @Override
  public void setBalanceActive(boolean active) {
    balanceActive = active;
  }

  @Override
  public void setTransactionsActive(boolean active) {
    transactionsActive = active;
  }

  @Override
  public void setReportsActive(boolean active) {
    reportsActive = active;
  }

  @Override
  public void setBillsActive(boolean active) {
    billsActive = active;
  }

  @Override
  public void setBudgetActive(boolean active) {
    budgetActive = active;
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
  
  // ================================================================
  // END: HeaderView methods 
  // ================================================================
 
  public String getNickname() {
    return nickname;
  }
  
  public String getEmail() {
    return email;
  }
  
  public String getLogoutURL() {
    return logoutURL;
  }
  
  public String getRedirectTo() {
    return redirectTo;
  }
  
  public boolean isDashboardActive() {
    return dashboardActive;
  }
  
  public boolean isBalanceActive() {
    return balanceActive;
  }
  
  public boolean isTransactionsActive() {
    return transactionsActive;
  }
  
  public boolean isReportsActive() {
    return reportsActive;
  }
  
  public boolean isBillsActive() {
    return billsActive;
  }
  
  public boolean isBudgetActive() {
    return budgetActive;
  }
  
  public boolean isSettingsActive() {
    return settingsActive;
  }
  
  public void setURL(String url) {
    this.url = url;
  }
  
} // HeaderViewTestImpl //
