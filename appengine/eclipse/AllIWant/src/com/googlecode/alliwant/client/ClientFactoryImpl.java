/**
 * @file ClientFactoryImpl.java
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

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.place.shared.PlaceController;
import com.googlecode.jhb.gwt.client.db.DbCache;
import com.googlecode.jhb.gwt.client.db.DbCacheImpl;
import com.googlecode.jhb.gwt.client.rpc.Manager;
import com.googlecode.jhb.gwt.client.rpc.ManagerImpl;
import com.googlecode.jhb.gwt.client.rpc.Rpc;
import com.googlecode.jhb.gwt.client.rpc.RpcImpl;
import com.googlecode.jhb.gwt.client.ui.DashboardView;
import com.googlecode.jhb.gwt.client.ui.DashboardViewImpl;
import com.googlecode.jhb.gwt.client.ui.GoodbyeView;
import com.googlecode.jhb.gwt.client.ui.GoodbyeViewImpl;
import com.googlecode.jhb.gwt.client.ui.widget.Alert;
import com.googlecode.jhb.gwt.client.ui.widget.AlertImpl;
import com.googlecode.jhb.gwt.client.ui.widget.smart.Header;
import com.googlecode.jhb.gwt.client.ui.widget.smart.HeaderPresenter;
import com.googlecode.jhb.gwt.client.ui.widget.smart.HeaderView;
import com.googlecode.jhb.gwt.client.ui.widget.smart.HeaderViewImpl;

public class ClientFactoryImpl implements ClientFactory {

  private EventBus eventBus = new SimpleEventBus();
  private PlaceController placeController = new PlaceController(eventBus);
  private Alert alert = new AlertImpl();
  private Rpc rpc = new RpcImpl(getAlert());
  private DbCache db = new DbCacheImpl();
  private Manager manager = new ManagerImpl(rpc, eventBus, db);
  private HeaderView headerView = new HeaderViewImpl();
  private Header header = new HeaderPresenter(headerView, getEventBus(), 
   getManager());
  private DashboardView dashboardView = new DashboardViewImpl(header);
  private GoodbyeView goodbyeView = new GoodbyeViewImpl();
  
  @Override 
  public EventBus getEventBus() { 
    return eventBus; 
  }
  
  @Override
  public PlaceController getPlaceController() { 
    return placeController; 
  }
  
  @Override
  public Alert getAlert() {
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
  public DashboardView getDashboardView() {
    return dashboardView;
  }
  
  @Override
  public GoodbyeView getGoodbyeView() {
    return goodbyeView;
  }

} // ClientFactoryImpl ///
