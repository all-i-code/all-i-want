/**
 * @file GroupsActivity.java
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
import com.googlecode.alliwant.client.model.Group;
import com.googlecode.alliwant.client.model.GroupInvitation;
import com.googlecode.alliwant.client.model.User;
import com.googlecode.alliwant.client.place.GroupsPlace;
import com.googlecode.alliwant.client.rpc.Manager;
import com.googlecode.alliwant.client.ui.GroupsView;
import com.googlecode.alliwant.client.ui.widget.Alert;
import com.googlecode.alliwant.client.ui.widget.smart.EditGroupPopup;

public class GroupsActivity implements Activity, GroupsView.Presenter {

  private ClientFactory cf;
  private GroupsView view;
  private Manager manager;
  private Alert alert;
  private EditGroupPopup editGroupPopup; 
  private User user;
  private List<Group> myGroups = new ArrayList<Group>();
  private List<Group> groups = new ArrayList<Group>();
  private List<GroupInvitation> invites = new ArrayList<GroupInvitation>();
  
  public GroupsActivity(GroupsPlace place, ClientFactory cf) {
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
    view = cf.getGroupsView();
    manager = cf.getManager();
    alert = cf.getAlert();
    editGroupPopup = cf.getEditGroupPopup();
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
  // BEGIN: GroupsView.Presenter methods
  // ==========================================================================

  @Override
  public void createGroup() {
    editGroupPopup.show(new EditGroupPopup.Handler() {
      public void onSave(int groupId, String name, String description) {
        addGroup(name, description);
      }
    });
  } // createGroup //
 
  @Override
  public void inviteMember(int index) {
    // TODO: Implement this
    alert.show(view.getAiwc().comingSoon());
  }
  
  @Override
  public void deleteGroup(int index) {
    // TODO: Implement this
    alert.show(view.getAiwc().comingSoon());
  }
  
  @Override
  public void leaveGroup(int index) {
    // TODO: Implement this
    alert.show(view.getAiwc().comingSoon());
  }
  
  @Override
  public void acceptInvite(int index) {
    view.showProcessingOverlay();
    manager.acceptInvite(invites.get(index).getId());
  }
    
  @Override
  public void declineInvite(int index) {
    view.showProcessingOverlay();
    manager.declineInvite(invites.get(index).getId());
  }
  
  // ==========================================================================
  // END: GroupsView.Presenter methods
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
      ModelListEvent.Handler<Group> handler = new ModelListEvent.Handler<Group>() {
        public void onModelList(ModelListEvent<Group> event) {
          handleGroups(event.getModelList());
        }
      };
      ModelListEvent.register(eventBus, Group.class, handler);
    }
    
    {
      ModelListEvent.Handler<GroupInvitation> handler = 
       new ModelListEvent.Handler<GroupInvitation>() {
        public void onModelList(ModelListEvent<GroupInvitation> event) {
          handleInvites(event.getModelList());
        }
      };
      ModelListEvent.register(eventBus, Group.class, handler);
    }
    
    {
      InfoEvent.Handler handler = new InfoEvent.Handler() {
        public void onInfo(InfoEvent event) {
          handleAccept();
        }
      };
      InfoEvent.register(eventBus, InfoEvent.INVITE_ACCEPTED, handler);
    }
    
    {
      InfoEvent.Handler handler = new InfoEvent.Handler() {
        public void onInfo(InfoEvent event) {
          handleDecline();
        }
      };
      InfoEvent.register(eventBus, InfoEvent.INVITE_DECLINED, handler);
    }
  } // addEventBusHandlers //

  private void handleUser(User user) {
    view.showProcessingOverlay();
    this.user = user;
    manager.getAccessRequests();
  }
  
  private void handleGroups(List<Group> allGroups) {
    view.hideProcessingOverlay();
    
    myGroups.clear();
    groups.clear();
    for (Group g : allGroups) {
      if (g.getOwnerId() == user.getOwnerId()) myGroups.add(g);
      else groups.add(g);
    }
   
    view.setNumMyGroups(myGroups.size());
    for (int i = 0; i < myGroups.size(); i++) {
      Group g = myGroups.get(i);
      view.setMyGroupName(i, g.getName());
      view.setMyGroupDescription(i, g.getDescription());
    }
    
    view.setNumGroups(groups.size());
    for (int i = 0; i < groups.size(); i++) {
      Group g = groups.get(i);
      view.setGroupName(i, g.getName());
      view.setGroupDescription(i, g.getDescription());
      view.setGroupOwner(i, g.getOwner());
    }
  } // handleGroups //
 
  private void handleInvites(List<GroupInvitation> invites) {
    this.invites.clear();
    this.invites.addAll(invites);
    view.setNumInvites(invites.size());
    for (int i = 0; i < invites.size(); i++) {
      GroupInvitation invite = invites.get(i);
      view.setInviteName(i, invite.getGroupName());
      view.setInviteOwner(i, invite.getOwnerName());
      view.setInviteEmail(i, invite.getMemberEmail());
    }
    manager.getGroups();
  } // handleInvites //
  
  private void handleAccept() {
    view.hideProcessingOverlay();
    alert.show(view.getAiwc().invitationAccepted());
    view.showProcessingOverlay();
    manager.getGroupInvites();
  }
  
  private void handleDecline() {
    view.hideProcessingOverlay();
    alert.show(view.getAiwc().invitationDeclined());
    view.showProcessingOverlay();
    manager.getGroupInvites();
  }

  private void addGroup(String name, String description) {
    view.showProcessingOverlay();
    manager.addGroup(name, description);
  }
  
} // RequestsActivity //
