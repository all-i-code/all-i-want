/**
 * @file ManagerImpl.java
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
package com.googlecode.jhb.gwt.client.rpc;

import java.util.List;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.googlecode.jhb.gwt.client.db.DbCache;
import com.googlecode.jhb.gwt.client.event.ModelEvent;
import com.googlecode.jhb.gwt.client.event.ModelListEvent;
import com.googlecode.jhb.gwt.client.model.Account;
import com.googlecode.jhb.gwt.client.model.AccountImpl;
import com.googlecode.jhb.gwt.client.model.Category;
import com.googlecode.jhb.gwt.client.model.CategoryImpl;
import com.googlecode.jhb.gwt.client.model.User;
import com.googlecode.jhb.gwt.client.model.UserImpl;

public class ManagerImpl implements Manager {

  private Rpc rpc;
  private EventBus eventBus;
  private DbCache db;
  private User user;
  
  public ManagerImpl(Rpc rpc, EventBus eventBus, DbCache db) {
    this.rpc = rpc;
    this.eventBus = eventBus;
    this.db = db;
    user = null;
  }
  
  // ================================================================
  // BEGIN: Manager methods
  // ================================================================
  
  /**
   * Request the currently logged in user, or a url for them to login
   * @param currentURL the current URL (used to redirect back after login)
   */
  @Override
  public void getCurrentUser(String currentURL) {    
    if (null != user) {
      eventBus.fireEvent(new ModelEvent<User>(User.class, user));
      return;
    }
    
    if (currentURL.length() < 1)
      currentURL = Window.Location.getHref();
    String url = "/rpc/user/get_current_user?";
    url += rpc.add("url", currentURL, true);
    rpc.send(url, new Rpc.Handler() {
      public void onComplete(String result) {
        handleUser(result);
      }
    });
  } // getCurrentUser //
  
  /**
   * Request a listing of all Accounts for the current User
   */
  @Override
  public void getAccounts() {
    List<Account> accounts = db.getAccounts();
    if (accounts.size() > 0) {
      eventBus.fireEvent(new ModelListEvent<Account>(Account.class, accounts));
      return;
    }
    
    String url = "/rpc/account/get_accounts";
    rpc.send(url, new Rpc.Handler() {
      public void onComplete(String result) {
        handleAccounts(result);
      }
    });
  } // getAccounts //
  
  /**
   * Request a listing of all Categories for the current User
   */
  @Override
  public void getCategories() {
    List<Category> categories = db.getCategories();
    if (categories.size() > 0) {
      eventBus.fireEvent(new ModelListEvent<Category>(Category.class, categories));
      return;
    }
    
    String url = "/rpc/account/get_categories";
    rpc.send(url, new Rpc.Handler() {
      public void onComplete(String result) {
        handleCategories(result);
      }
    });
  } // getCategories //
  
  // ================================================================
  // END: Manager methods
  // ================================================================
  
  private void handleUser(String response) {
    user = UserImpl.decode(response);
    eventBus.fireEvent(new ModelEvent<User>(User.class, user));
  } // handleUser //
  
  private void handleAccounts(String response) {
    List<Account> accounts = AccountImpl.decodeList(response);
    db.updateAccounts(accounts);
    eventBus.fireEvent(new ModelListEvent<Account>(Account.class, accounts));
  } // handleAccounts //
  
  private void handleCategories(String response) {
    List<Category> categories = CategoryImpl.decodeList(response);
    db.updateCategories(categories);
    eventBus.fireEvent(new ModelListEvent<Category>(Category.class, categories));
  } // handleCategories //
  
} // ManagerJson //
