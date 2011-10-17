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
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.alliwant.client.event.ModelEvent;
import com.googlecode.alliwant.client.model.User;
import com.googlecode.alliwant.client.place.GroupsPlace;
import com.googlecode.alliwant.client.place.ListsPlace;
import com.googlecode.alliwant.client.place.RequestsPlace;
import com.googlecode.alliwant.client.place.SettingsPlace;
import com.googlecode.alliwant.client.rpc.Manager;
import com.googlecode.alliwant.client.ui.widget.Alert;

public class HeaderPresenter implements Header, HeaderView.Presenter {

  private PlaceController pc;
  private HeaderView view;
  private Alert alert;
  
  public HeaderPresenter(HeaderView view, Alert alert, EventBus eventBus,
   Manager manager, PlaceController pc) {
    view.setPresenter(this);
    this.view = view;
    this.alert = alert;
    this.pc = pc;
    addEventBusHandlers(eventBus);
    view.showProcessingOverlay();
  }
  
  // ================================================================
  // BEGIN: Header methods
  // ================================================================
  
  @Override
  public Widget asWidget() {
    return view.asWidget();
  }

  // ================================================================
  // END: Header methods
  // ================================================================
  
  // ================================================================
  // BEGIN: HeaderView.Presener methods
  // ================================================================

  @Override
  public void lists() {
    pc.goTo(new ListsPlace());
  }
  
  @Override
  public void groups() {
    pc.goTo(new GroupsPlace());
  }
  
  @Override
  public void requests() {
    pc.goTo(new RequestsPlace());
  }
  
  @Override
  public void settings() {
    pc.goTo(new SettingsPlace());
  }
  
  // ================================================================
  // END: HeaderView.Presenter methods
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
    
    {
      PlaceChangeEvent.Handler handler = new PlaceChangeEvent.Handler() {
        public void onPlaceChange(PlaceChangeEvent event) {
          handlePlaceChange(event.getNewPlace());
        }
      };
      eventBus.addHandler(PlaceChangeEvent.TYPE, handler);
    }
  } // addEventBusHandlers //
 
  private void handleUser(final User user) {
    if (!view.asWidget().isAttached()) return;
    view.setRequestsVisible(user.isAdmin());
    view.hideProcessingOverlay();
    if (!isEmpty(user.getLoginUrl())) {
      view.redirect(user.getLoginUrl());
    } else if (user.getOwnerId() < 0) {
      String msg = view.getConsts().reqBeingProcessed();
      if (user.wasReqDenied()) msg = view.getConsts().reqDenied();
      alert.show(msg, new Alert.Handler() {
        public void onClose() {
          view.redirect(user.getLogoutUrl());
        }
      });
    } else {
      String label = view.getMsgs().user(user.getNickname(), user.getEmail());
      view.setUser(label);
      view.setLogoutURL(user.getLogoutUrl());
    }
  } // handleUser //
 
  private void handlePlaceChange(Place newPlace) {
    view.setListsActive(newPlace instanceof ListsPlace);
    view.setGroupsActive(newPlace instanceof GroupsPlace);
    view.setRequestsActive(newPlace instanceof RequestsPlace);
    view.setSettingsActive(newPlace instanceof SettingsPlace);
  }
  
} // HeaderPresenter //
