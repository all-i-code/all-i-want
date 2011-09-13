/**
 * @file GoodbyeActivity.java
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
package com.googlecode.jhb.gwt.client.activity;

import static com.googlecode.jhb.gwt.client.StringUtils.isEmpty;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.googlecode.jhb.gwt.client.ClientFactory;
import com.googlecode.jhb.gwt.client.event.ModelEvent;
import com.googlecode.jhb.gwt.client.model.User;
import com.googlecode.jhb.gwt.client.place.GoodbyePlace;
import com.googlecode.jhb.gwt.client.rpc.Manager;
import com.googlecode.jhb.gwt.client.ui.GoodbyeView;

public class GoodbyeActivity implements Activity {

  private GoodbyeView view;
  private Manager manager;
  
  public GoodbyeActivity(GoodbyePlace place, ClientFactory cf) {
    view = cf.getGoodbyeView();
    manager = cf.getManager();
  }
  
  // ==========================================================================
  // BEGIN: Activity methods
  // ==========================================================================
  
  @Override
  public String mayStop() {
    return null;
  }

  @Override
  public void onCancel() {
  }

  @Override
  public void onStop() {
  }

  @Override
  public void start(AcceptsOneWidget panel, EventBus eventBus) {
    panel.setWidget(view);
    addEventBusHandlers(eventBus);
    view.showProcessingOverlay();
    manager.getCurrentUser(view.getURL());
  }
  
  // ==========================================================================
  // END: Activity methods
  // ==========================================================================
  
  
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
    view.hideProcessingOverlay();
    boolean success = isEmpty(user.getLogoutUrl());
    view.setLogoutVisible(!success);
    if (success) {
      view.setMessage(view.getJhbc().logoutSuccess());
    } else { 
      view.setMessage(view.getJhbc().logoutFail());
      view.setLogoutURL(user.getLogoutUrl());
    }
  } // handleUser //

} // GoodbyeActivity //
