/**
 * @file ClientFactoryTestImpl.java
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
package com.googlecode.jhb.gwt.client;

import static org.mockito.Mockito.mock;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.place.shared.PlaceController;
import com.googlecode.jhb.gwt.client.rpc.Manager;
import com.googlecode.jhb.gwt.client.ui.DashboardViewTestImpl;
import com.googlecode.jhb.gwt.client.ui.GoodbyeViewTestImpl;
import com.googlecode.jhb.gwt.client.ui.widget.AlertTestImpl;
import com.googlecode.jhb.gwt.client.ui.widget.smart.Header;
import com.googlecode.jhb.gwt.client.ui.widget.smart.HeaderTestImpl;

public class ClientFactoryTestImpl implements ClientFactory {

  private EventBus eventBus = new SimpleEventBus();
  private Manager manager = mock(Manager.class);
  private PlaceController pc = mock(PlaceController.class);
  private AlertTestImpl alert = new AlertTestImpl();
  private HeaderTestImpl header = new HeaderTestImpl();
  private DashboardViewTestImpl dashboardView = new DashboardViewTestImpl();
  private GoodbyeViewTestImpl goodbyeView = new GoodbyeViewTestImpl();
  
  // ================================================================
  // BEGIN: ClientFactory methods
  // ================================================================
  
  @Override
  public EventBus getEventBus() {
    return eventBus;
  }

  @Override
  public PlaceController getPlaceController() {
    return pc;
  }

  @Override
  public AlertTestImpl getAlert() {
    return alert;
  }

  @Override
  public Manager getManager() {
    return manager;
  }

  @Override
  public Header getHeader() {
    return header;
  }

  @Override
  public DashboardViewTestImpl getDashboardView() {
    return dashboardView;
  }
  
  @Override
  public GoodbyeViewTestImpl getGoodbyeView() {
    return goodbyeView;
  }
  
  // ================================================================
  // END: ClientFactory methods
  // ================================================================
  
} // ClientFactoryTestImpl //
