/**
 * @file DashboardActivity.java
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

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.googlecode.jhb.gwt.client.ClientFactory;
import com.googlecode.jhb.gwt.client.logging.Logging;
import com.googlecode.jhb.gwt.client.place.DashboardPlace;
import com.googlecode.jhb.gwt.client.ui.DashboardView;

public class DashboardActivity implements Activity {

  private ClientFactory cf;
  private DashboardView view;
  
  public DashboardActivity(DashboardPlace place, ClientFactory cf) {
    this.cf = cf;
    view = cf.getDashboardView();
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
    Logging.logger().info("DashboardActiviy::start()");
    panel.setWidget(view.asWidget());
    addEventBusHandlers(eventBus);
    cf.getHeader().getUser();
  }
  
  // ==========================================================================
  // END: Activity methods
  // ==========================================================================
  
  
  private void addEventBusHandlers(EventBus eventBus) {
    
  } // addEventBusHandlers //

} // DashboardActivity //
