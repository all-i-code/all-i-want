/**
 * @file RequestsActivity.java
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

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.googlecode.alliwant.client.ClientFactory;
import com.googlecode.alliwant.client.event.InfoEvent;
import com.googlecode.alliwant.client.event.ModelEvent;
import com.googlecode.alliwant.client.event.ModelListEvent;
import com.googlecode.alliwant.client.model.AccessReq;
import com.googlecode.alliwant.client.model.User;
import com.googlecode.alliwant.client.place.RequestsPlace;
import com.googlecode.alliwant.client.rpc.Manager;
import com.googlecode.alliwant.client.ui.RequestsView;

public class RequestsActivity implements Activity, RequestsView.Presenter {

  private ClientFactory cf;
  private RequestsView view;
  private Manager manager;
  private List<AccessReq> reqs = new ArrayList<AccessReq>();
  private int reqIndex = -1;
  
  public RequestsActivity(RequestsPlace place, ClientFactory cf) {
    this.cf = cf;
    view = cf.getRequestsView();
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
    view.setPresenter(this);
    addEventBusHandlers(eventBus);
    view.setHeader(cf.getHeader());
    manager.getCurrentUser();
  }
  
  // ==========================================================================
  // END: Activity methods
  // ==========================================================================
  
  // ==========================================================================
  // BEGIN: RequestsView.Presenter methods
  // ==========================================================================
    
  @Override
  public void approve(int index) {
    reqIndex = index;
    view.showProcessingOverlay();
    manager.approveRequest(reqs.get(index).getId());
  }
    
  @Override
  public void deny(int index) {
    reqIndex = index;
    view.showProcessingOverlay();
    manager.denyRequest(reqs.get(index).getId());
  }
  
  // ==========================================================================
  // END: RequestsView.Presenter methods
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
      ModelListEvent.Handler<AccessReq> handler = new ModelListEvent.Handler<AccessReq>() {
        public void onModelList(ModelListEvent<AccessReq> event) {
          handleRequests(event.getModelList());
        }
      };
      ModelListEvent.register(eventBus, AccessReq.class, handler);
    }
    
    {
      InfoEvent.Handler handler = new InfoEvent.Handler() {
        public void onInfo(InfoEvent event) {
          handleApproval();
        }
      };
      InfoEvent.register(eventBus, InfoEvent.REQ_APPROVED, handler);
    }
    
    {
      InfoEvent.Handler handler = new InfoEvent.Handler() {
        public void onInfo(InfoEvent event) {
          handleDenial();
        }
      };
      InfoEvent.register(eventBus, InfoEvent.REQ_DENIED, handler);
    }
  } // addEventBusHandlers //

  private void handleUser(User user) {
    if (user.getOwnerId() < 0) return;
    view.showProcessingOverlay();
    manager.getAccessRequests();
  }
  
  private void handleRequests(List<AccessReq> requests) {
    view.hideProcessingOverlay();
    reqs.clear();
    reqs.addAll(requests);
    view.setNumRequests(reqs.size());
    for (int i = 0; i < reqs.size(); i++) {
      view.setEmail(i, reqs.get(i).getEmail());
      String denied = view.getAiwc().no();
      if (reqs.get(i).wasDenied()) denied = view.getAiwc().yes();
      view.setDenied(i, denied);
    }
  } // handleRequests //
  
  private void handleApproval() {
    if (reqIndex < 0) return;
    String msg = view.getAiwm().reqApproved(reqs.get(reqIndex).getEmail());
    cf.getAlert().show(msg);
    reqIndex = -1;
    manager.getAccessRequests();
  }
  
  private void handleDenial() {
    if (reqIndex < 0) return;
    String msg = view.getAiwm().reqDenied(reqs.get(reqIndex).getEmail());
    cf.getAlert().show(msg);
    reqIndex = -1;
    manager.getAccessRequests();
  }

} // RequestsActivity //
