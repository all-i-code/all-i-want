/**
 * @file ListsActivity.java
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.googlecode.alliwant.client.ClientFactory;
import com.googlecode.alliwant.client.StringUtils;
import com.googlecode.alliwant.client.event.ModelEvent;
import com.googlecode.alliwant.client.event.ModelListEvent;
import com.googlecode.alliwant.client.model.ListOwner;
import com.googlecode.alliwant.client.model.User;
import com.googlecode.alliwant.client.model.WishList;
import com.googlecode.alliwant.client.place.ListsPlace;
import com.googlecode.alliwant.client.rpc.Manager;
import com.googlecode.alliwant.client.ui.ListsView;

public class ListsActivity implements Activity, ListsView.Presenter {

  private ClientFactory cf;
  private ListsView view;
  private Manager manager;
  private User user;
  private WishList currentList = null;
  private Map<Integer, WishList> listMap = new HashMap<Integer, WishList>();
  private Map<Integer, ListOwner> ownerMap = new HashMap<Integer, ListOwner>();
  
  public ListsActivity(ListsPlace place, ClientFactory cf) {
    this.cf = cf;
    view = cf.getListsView();
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
    panel.setWidget(view.asWidget());
    addEventBusHandlers(eventBus);
    view.setHeader(cf.getHeader());
    cf.getManager().getCurrentUser();
  }
  
  // ==========================================================================
  // END: Activity methods
  // ==========================================================================
  
  // ==========================================================================
  // BEGIN: ListsView.Presenter methods
  // ==========================================================================
  
  @Override
  public void userChanged() {
    view.showProcessingOverlay();
    int oid = StringUtils.toInt(view.getOwner());
    manager.getLists(oid);
  }
    
  @Override
  public void addList() {
    // TODO: implement this
  }
   
  @Override
  public void listChanged() {
    view.showProcessingOverlay();
    int lid = StringUtils.toInt(view.getList());
    currentList = listMap.get(lid);
    showList();
  }
    
  @Override
  public void addItem() {
    // TODO: implement this
  }
  
  // ==========================================================================
  // END: ListsView.Presenter methods
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
    
    {
      ModelListEvent.Handler<ListOwner> handler =
       new ModelListEvent.Handler<ListOwner>() {
        public void onModelList(ModelListEvent<ListOwner> event) {
          handleOwners(event.getModelList());
        }
      };
      ModelListEvent.register(eventBus, ListOwner.class, handler);
    }
    
    {
      ModelListEvent.Handler<WishList> handler =
       new ModelListEvent.Handler<WishList>() {
        public void onModelList(ModelListEvent<WishList> event) {
          handleLists(event.getModelList());
        }
      };
      ModelListEvent.register(eventBus, WishList.class, handler);
    }
  } // addEventBusHandlers //

  private void handleUser(User user) {
    view.showProcessingOverlay();
    this.user = user;
    manager.getAvailableOwners(user.getOwnerId());
  } // handleUser //
  
  private void handleOwners(List<ListOwner> owners) {
    view.clearOwners();
    ownerMap.clear();
    for (ListOwner owner : owners) {
      ownerMap.put(owner.getId(), owner);
      view.addOwnerItem(owner.getNickname(), Integer.toString(owner.getId()));
    }
  } // handleOwners //
  
  private void handleLists(List<WishList> lists) {
    view.clearLists();
    listMap.clear();
    for (WishList wl : lists) {
      listMap.put(wl.getId(), wl);
      view.addListItem(wl.getName(), Integer.toString(wl.getId()));
    }
  } // handleLists //
 
  private void showList() {
    // TODO: implement this
  } // showList //
  
} // ListsActivity //
