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
package com.googlecode.alliwant.client;

import static org.mockito.Mockito.mock;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.place.shared.PlaceController;
import com.googlecode.alliwant.client.rpc.Manager;
import com.googlecode.alliwant.client.ui.GroupsViewTestImpl;
import com.googlecode.alliwant.client.ui.ListsViewTestImpl;
import com.googlecode.alliwant.client.ui.GoodbyeViewTestImpl;
import com.googlecode.alliwant.client.ui.RequestsViewTestImpl;
import com.googlecode.alliwant.client.ui.SettingsViewTestImpl;
import com.googlecode.alliwant.client.ui.widget.AlertTestImpl;
import com.googlecode.alliwant.client.ui.widget.smart.EditItemPopup;
import com.googlecode.alliwant.client.ui.widget.smart.EditListPopupTest;
import com.googlecode.alliwant.client.ui.widget.smart.HeaderTestImpl;
import com.googlecode.alliwant.client.ui.widget.smart.ItemDetailPopup;

public class ClientFactoryTestImpl implements ClientFactory {

  private EventBus eventBus = new SimpleEventBus();
  private Manager manager = mock(Manager.class);
  private PlaceController pc = mock(PlaceController.class);
  private AlertTestImpl alert = new AlertTestImpl();
  private HeaderTestImpl header = new HeaderTestImpl();
  private EditListPopupTest editListPopup = new EditListPopupTest();
  private ListsViewTestImpl listsView = new ListsViewTestImpl();
  private GroupsViewTestImpl groupsView = new GroupsViewTestImpl();
  private RequestsViewTestImpl reqsView = new RequestsViewTestImpl();
  private SettingsViewTestImpl settingsView = new SettingsViewTestImpl();
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
  public HeaderTestImpl getHeader() {
    return header;
  }
  
  @Override
  public EditListPopupTest getEditListPopup() {
    return editListPopup;
  }

  @Override
  public ListsViewTestImpl getListsView() {
    return listsView;
  }
 
  @Override
  public GroupsViewTestImpl getGroupsView() {
    return groupsView;
  }
  
  @Override
  public RequestsViewTestImpl getRequestsView() {
    return reqsView;
  }
  
  @Override
  public SettingsViewTestImpl getSettingsView() {
    return settingsView;
  }
  
  @Override
  public GoodbyeViewTestImpl getGoodbyeView() {
    return goodbyeView;
  }

  @Override
  public ItemDetailPopup getItemDetailPopup() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public EditItemPopup getEditItemPopup() {
    // TODO Auto-generated method stub
    return null;
  }
  
  // ================================================================
  // END: ClientFactory methods
  // ================================================================
  
} // ClientFactoryTestImpl //
