/**
 * @file JhbConstants.java
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
package com.googlecode.jhb.gwt.client.i18n;

import com.google.gwt.i18n.client.Constants;

public interface JhbConstants extends Constants {
  
  @DefaultStringValue("Jobber''s Home Budget")
  String jobbersHomeBudget();
  
  @DefaultStringValue("$")
  String currencySymbol();
  
  @DefaultStringValue("OK")
  String ok();
  
  @DefaultStringValue("Cancel")
  String cancel();
  
  @DefaultStringValue("Logout")
  String logout();
  
  @DefaultStringValue("Login")
  String login();
  
  @DefaultStringValue("Account")
  String account();
  
  @DefaultStringValue("Accounts")
  String accounts();
  
  @DefaultStringValue("Name")
  String name();
  
  @DefaultStringValue("Description")
  String description();
  
  @DefaultStringValue("Balance")
  String balance();
  
  @DefaultStringValue(" | ")
  String pipe();
  
  @DefaultStringValue("Dashboard")
  String dashboard();
  
  @DefaultStringValue("Transactions")
  String transactions();
  
  @DefaultStringValue("Budget")
  String budget();
  
  @DefaultStringValue("Bills")
  String bills();
  
  @DefaultStringValue("Settings")
  String settings();
  
  @DefaultStringValue("Reports")
  String reports();
  
  @DefaultStringValue("Upcoming Bills")
  String upcomingBills();
  
  @DefaultStringValue("Account Balances")
  String accountBalances();
  
  @DefaultStringValue("Most Recent Transactions")
  String mostRecentTransactions();
  
  @DefaultStringValue("Date")
  String date();
  
  @DefaultStringValue("Amount")
  String amount();
  
  @DefaultStringValue("Payment Type")
  String paymentType();
  
  @DefaultStringValue("Split")
  String split();
  
  @DefaultStringValue("You have been successfully logged out.")
  String logoutSuccess();
  
  @DefaultStringValue("You are still logged in.")
  String logoutFail();
  
} // JhbConstants //
