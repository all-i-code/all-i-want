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
package com.googlecode.alliwant.client.rpc;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.googlecode.alliwant.client.event.ModelEvent;
import com.googlecode.alliwant.client.model.User;
import com.googlecode.alliwant.client.model.UserImpl;

public class ManagerImpl implements Manager {

  private Rpc rpc;
  private EventBus eventBus;
  private User user;
  
  public ManagerImpl(Rpc rpc, EventBus eventBus) {
    this.rpc = rpc;
    this.eventBus = eventBus;
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
  
  // ================================================================
  // END: Manager methods
  // ================================================================
  
  private void handleUser(String response) {
    user = UserImpl.decode(response);
    eventBus.fireEvent(new ModelEvent<User>(User.class, user));
  } // handleUser //
  
} // ManagerJson //
