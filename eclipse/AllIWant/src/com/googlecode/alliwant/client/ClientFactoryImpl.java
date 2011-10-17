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
package com.googlecode.alliwant.client;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.place.shared.PlaceController;
import com.googlecode.alliwant.client.rpc.Manager;
import com.googlecode.alliwant.client.rpc.ManagerImpl;
import com.googlecode.alliwant.client.rpc.Rpc;
import com.googlecode.alliwant.client.rpc.RpcImpl;
import com.googlecode.alliwant.client.ui.GroupsView;
import com.googlecode.alliwant.client.ui.GroupsViewImpl;
import com.googlecode.alliwant.client.ui.ListsView;
import com.googlecode.alliwant.client.ui.ListsViewImpl;
import com.googlecode.alliwant.client.ui.GoodbyeView;
import com.googlecode.alliwant.client.ui.GoodbyeViewImpl;
import com.googlecode.alliwant.client.ui.RequestsView;
import com.googlecode.alliwant.client.ui.RequestsViewImpl;
import com.googlecode.alliwant.client.ui.widget.Alert;
import com.googlecode.alliwant.client.ui.widget.AlertImpl;
import com.googlecode.alliwant.client.ui.widget.smart.Header;
import com.googlecode.alliwant.client.ui.widget.smart.HeaderPresenter;
import com.googlecode.alliwant.client.ui.widget.smart.HeaderView;
import com.googlecode.alliwant.client.ui.widget.smart.HeaderViewImpl;

public class ClientFactoryImpl implements ClientFactory {

  private EventBus eventBus = new SimpleEventBus();
  private PlaceController placeController = new PlaceController(eventBus);
  private Alert alert = new AlertImpl();
  private Rpc rpc = new RpcImpl(getAlert());
  private Manager manager = new ManagerImpl(rpc, eventBus);
  private HeaderView headerView = new HeaderViewImpl();
  private Header header = new HeaderPresenter(headerView, alert, getEventBus(), 
   getManager(), placeController);
  private ListsView listsView = new ListsViewImpl();
  private GroupsView groupsView = new GroupsViewImpl();
  private RequestsView requestsView = new RequestsViewImpl();
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
  public ListsView getListsView() {
    return listsView;
  }
  
  @Override
  public GroupsView getGroupsView() {
    return groupsView;
  }
  
  @Override
  public RequestsView getRequestsView() {
    return requestsView;
  }
  
  @Override
  public GoodbyeView getGoodbyeView() {
    return goodbyeView;
  }

} // ClientFactoryImpl ///
