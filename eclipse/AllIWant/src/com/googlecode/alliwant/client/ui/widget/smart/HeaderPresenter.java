/**
 * @file HeaderPresenter.java
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

import static com.googlecode.alliwant.client.StringUtils.isEmpty;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.alliwant.client.event.ModelEvent;
import com.googlecode.alliwant.client.model.User;
import com.googlecode.alliwant.client.place.ListsPlace;
import com.googlecode.alliwant.client.rpc.Manager;

public class HeaderPresenter implements Header {

  private HeaderView view;
  private Manager manager;
  private boolean requestingUser = false;
  
  public HeaderPresenter(HeaderView view, EventBus eventBus, Manager manager) {
    this.view = view;
    this.manager = manager;
    addEventBusHandlers(eventBus);
  }
  
  // ================================================================
  // BEGIN: Header methods
  // ================================================================
  
  @Override
  public Widget asWidget() {
    return view.asWidget();
  }

  @Override
  public void getUser() {
    view.showProcessingOverlay();
    requestingUser = true;
    manager.getCurrentUser(view.getURL());
  }
  
  // ================================================================
  // END: Header methods
  // ================================================================
 
  private void addEventBusHandlers(EventBus eventBus) {
    {
      ModelEvent.Handler<User> handler = new ModelEvent.Handler<User>() {
        public void onModel(ModelEvent<User> event) {
          handleUser(event.getModel());
        }
      };
      ModelEvent.register(eventBus, User.class, handler);
    }
  } // addEventBusHandlers //
 
  private void handleUser(User user) {
    if (!requestingUser) return;
    requestingUser = false;
    view.hideProcessingOverlay();
    if (!isEmpty(user.getLoginUrl())) {
      view.redirect(user.getLoginUrl());
    } else {
      String label = view.getMsgs().user(user.getNickname(), user.getEmail());
      view.setUser(label);
      view.setLogoutURL(user.getLogoutUrl());
    }
    
    String url = view.getURL();
    view.setListsActive(url.contains(ListsPlace.PREFIX));
    view.setGroupsActive(url.contains("Groups"));
    view.setSettingsActive(url.contains("Settings"));
  } // handleUser //
  
} // HeaderPresenter //
