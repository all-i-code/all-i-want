/**
 * @file SettingsActivity.java
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
package com.googlecode.alliwant.client.activity;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.googlecode.alliwant.client.ClientFactory;
import com.googlecode.alliwant.client.place.SettingsPlace;
import com.googlecode.alliwant.client.ui.SettingsView;

public class SettingsActivity implements Activity {

  private ClientFactory cf;
  private SettingsView view;
  
  public SettingsActivity(SettingsPlace place, ClientFactory cf) {
    this.cf = cf;
    view = cf.getSettingsView();
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
    panel.setWidget(view.asWidget());
    addEventBusHandlers(eventBus);
    view.setHeader(cf.getHeader());
    cf.getManager().getCurrentUser();
  }
  
  // ==========================================================================
  // END: Activity methods
  // ==========================================================================
  
  
  private void addEventBusHandlers(EventBus eventBus) {
  } // addEventBusHandlers //

} // SettingsActivity //
