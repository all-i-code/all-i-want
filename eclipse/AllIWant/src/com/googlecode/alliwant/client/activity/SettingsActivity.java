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

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.googlecode.alliwant.client.ClientFactory;
import com.googlecode.alliwant.client.event.InfoEvent;
import com.googlecode.alliwant.client.event.ModelEvent;
import com.googlecode.alliwant.client.event.ModelListEvent;
import com.googlecode.alliwant.client.model.ListOwner;
import com.googlecode.alliwant.client.model.ListPermission;
import com.googlecode.alliwant.client.model.User;
import com.googlecode.alliwant.client.place.SettingsPlace;
import com.googlecode.alliwant.client.rpc.Manager;
import com.googlecode.alliwant.client.ui.SettingsView;
import com.googlecode.alliwant.client.ui.widget.Alert;
import com.googlecode.alliwant.client.ui.widget.Confirm;
import com.googlecode.alliwant.client.ui.widget.smart.TextFieldPopup;

public class SettingsActivity implements Activity, SettingsView.Presenter {

  private ClientFactory cf;
  private SettingsView view;
  private Alert alert;
  private Confirm confirm;
  private TextFieldPopup textFieldPopup;
  private Manager manager;
  private ListOwner owner;
  private List<ListPermission> permissions = new ArrayList<ListPermission>();
  private boolean updating = false;
  
  public SettingsActivity(SettingsPlace place, ClientFactory cf) {
    this.cf = cf;
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
    view = cf.getSettingsView();
    manager = cf.getManager();
    alert = cf.getAlert();
    confirm = cf.getConfirm();
    textFieldPopup = cf.getTextFieldPopup();
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
  // BEGIN: SettingsView.Presenter methods
  // ==========================================================================
  
  @Override
  public void updateOwner() {
    String name = view.getOwnerName(), nickname = view.getOwnerNickname();
    if (name.length() < 1) {
      alert.show(view.getAiwc().pleaseEnterName());
      return;
    } else if (nickname.length() < 1) {
      alert.show(view.getAiwc().pleaseEnterNickname());
      return;
    }
   
    updating = true;
    view.showProcessingOverlay();
    manager.updateOwner(owner.getId(), name, nickname);
  } // updateOwner //
    
  @Override
  public void addPermission() {
    textFieldPopup.show(view.getAiwc().addPermission(), view.getAiwc().email(), 
     "", new TextFieldPopup.Handler() {
      public void onSave(String text) {
        reallyAddPermission(text);
      }
    });
  } // addPermission //
    
  @Override
  public void deletePermission(int index) {
    final int pid = permissions.get(index).getId();
    confirm.show(view.getAiwc().confirmDeletePermission(),
     new Confirm.Handler() {
      public void onYes() {
        reallyDeletePerm(pid);
      }
    });
  } // deletePermission //
  
  // ==========================================================================
  // END: SettingsView.Presenter methods
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
      ModelEvent.Handler<ListOwner> handler = new ModelEvent.Handler<ListOwner>() {
        public void onModel(ModelEvent<ListOwner> event) {
          handleOwner(event.getModel());
        }
      };
      ModelEvent.register(eventBus, ListOwner.class, handler);
    }
    
    {
      ModelListEvent.Handler<ListPermission> handler =
       new ModelListEvent.Handler<ListPermission>() {
        public void onModelList(ModelListEvent<ListPermission> event) {
          handlePermissions(event.getModelList());
        }
      };
      ModelListEvent.register(eventBus, ListPermission.class, handler);
    }
    
    {
      InfoEvent.Handler handler = new InfoEvent.Handler() {
        public void onInfo(InfoEvent event) {
          handlePermissionDeleted();
        }
      };
      InfoEvent.register(eventBus, InfoEvent.PERMISSION_DELETED, handler);
    }
  } // addEventBusHandlers //

  private void handleUser(User user) {
    if (user.getOwnerId() < 0) return;
    manager.getOwner(user.getOwnerId());
  }
  
  private void handleOwner(ListOwner owner) {
    this.owner = owner;
    if (updating) alert.show(view.getAiwc().settingsUpdated());
    updating = false;
    view.setOwnerName(owner.getName());
    view.setOwnerNickname(owner.getNickname());
    manager.getPermissions(owner.getId(), false);
  }
  
  private void handlePermissions(List<ListPermission> perms) {
    view.hideProcessingOverlay();
    permissions.clear();
    permissions.addAll(perms);
    view.setNumPermissions(permissions.size());
    for (int i = 0; i < permissions.size(); i++) {
      view.setPermissionEmail(i, permissions.get(i).getEmail());
    }
  } // handlePermissions //

  private void handlePermissionDeleted() {
    manager.getPermissions(owner.getId(), false);
  }
  
  private void reallyAddPermission(String email) {
    view.showProcessingOverlay();
    manager.addPermission(owner.getId(), email);
  }
  
  private void reallyDeletePerm(int permId) {
    view.showProcessingOverlay();
    manager.removePermission(permId);
  }
  
} // SettingsActivity //
