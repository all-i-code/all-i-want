/**
 * @file GroupsViewImpl.java
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
package com.googlecode.alliwant.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.googlecode.alliwant.client.i18n.AiwConstants;
import com.googlecode.alliwant.client.i18n.AiwMessages;
import com.googlecode.alliwant.client.ui.css.CSS;
import com.googlecode.alliwant.client.ui.css.CssBundle;

public class GroupsViewImpl extends Composite implements GroupsView {

  interface Binder extends UiBinder<FlowPanel, GroupsViewImpl> {}
  private static final Binder uiBinder = GWT.create(Binder.class);

  private Presenter presenter = null;
  private AiwConstants aiwc = GWT.create(AiwConstants.class);
  private AiwMessages aiwm = GWT.create(AiwMessages.class);
  private CssBundle css = CSS.getBundle();
 
  private int mgNameCol, mgDescCol, mgInvitesCol, mgMembersCol, mgInviteCol, 
   mgDeleteCol;
  private int gNameCol, gDescCol, gOwnerCol, gLeaveCol;
  private int iNameCol, iOwnerCol, iEmailCol, iAcceptCol, iDeclineCol;
  
  @UiField
  SimplePanel headerWrapper;

  @UiField
  Grid myGroups, groups, invites;
  
  public GroupsViewImpl() {
    initWidget(uiBinder.createAndBindUi(this));
    int col = 0;
    myGroups.resize(1, 6);
    myGroups.getRowFormatter().addStyleName(0, css.main().tableHeader());
    myGroups.setText(0, mgNameCol = col++, aiwc.name());
    myGroups.setText(0, mgDescCol = col++, aiwc.description());
    myGroups.setText(0, mgInvitesCol = col++, aiwc.invitations());
    myGroups.setText(0, mgMembersCol = col++, aiwc.members());
    myGroups.getCellFormatter().addStyleName(0, mgInvitesCol, css.main().number());
    myGroups.getCellFormatter().addStyleName(0, mgMembersCol, css.main().number());
    myGroups.setText(0, mgInviteCol = col++, "");
    myGroups.setText(0, mgDeleteCol = col++, "");
   
    col = 0;
    groups.resize(1, 4);
    groups.getRowFormatter().addStyleName(0, css.main().tableHeader());
    groups.setText(0, gNameCol = col++, aiwc.name());
    groups.setText(0, gDescCol = col++, aiwc.description());
    groups.setText(0, gOwnerCol = col++, aiwc.owner());
    groups.setText(0, gLeaveCol = col++, "");
   
    col = 0;
    invites.resize(1, 5);
    invites.getRowFormatter().addStyleName(0, css.main().tableHeader());
    invites.setText(0, iNameCol = col++, aiwc.group());
    invites.setText(0, iOwnerCol = col++, aiwc.owner());
    invites.setText(0, iEmailCol = col++, aiwc.email());
    invites.setText(0, iAcceptCol = col++, "");
    invites.setText(0, iDeclineCol = col++, "");
  } // RequestsViewImpl //

  @UiHandler("createGroup")
  void createGroupClick(ClickEvent event) {
    presenter.createGroup();
  }
  
  // ================================================================
  // BEGIN: RequestsView methods
  // ================================================================
  
  @Override
  public void showProcessingOverlay() {
    ProcessingOverlay.show();
  }

  @Override
  public void hideProcessingOverlay() {
    ProcessingOverlay.hide();
  }
  
  @Override
  public void setHeader(IsWidget header) {
    headerWrapper.setWidget(header);
  }

  @Override
  public void setNumMyGroups(int num) {
    myGroups.resizeRows(num+1);
    for (int i = 1; i <= num; i++) {
      final int index = i-1;
      myGroups.getCellFormatter().addStyleName(i, mgInvitesCol, css.main().number());
      myGroups.getCellFormatter().addStyleName(i, mgMembersCol, css.main().number());
      Anchor invite = new Anchor(aiwc.inviteMember());
      invite.addClickHandler(new ClickHandler() {
        public void onClick(ClickEvent event) {
          presenter.inviteMember(index);
        }
      });
      myGroups.setWidget(i, mgInviteCol, invite);
      
      Anchor delete = new Anchor(aiwc.deleteGroup());
      delete.addClickHandler(new ClickHandler() {
        public void onClick(ClickEvent event) {
          presenter.deleteGroup(index);
        }
      });
      myGroups.setWidget(i, mgDeleteCol, delete);
     
      if (index % 2 != 0)
        myGroups.getRowFormatter().addStyleName(i, css.main().tableAlt());
    } // for all requests //
  } // setNumGroups //
 
  @Override
  public void setMyGroupName(int index, String name) {
    myGroups.setText(index+1, mgNameCol, name);
  }
  
  @Override
  public void setMyGroupDescription(int index, String description) {
    myGroups.setText(index+1, mgDescCol, description);
  }
  
  @Override
  public void setMyGroupMemberCount(int index, String memberCount) {
    myGroups.setText(index+1, mgMembersCol, memberCount);
  }
  
  @Override
  public void setMyGroupInviteCount(int index, String inviteCount) {
    myGroups.setText(index+1, mgInvitesCol, inviteCount);
  }
  
  @Override
  public void setNumGroups(int num) {
    groups.resizeRows(num+1);
    for (int i = 1; i <= num; i++) {
      final int index = i-1;
      Anchor leave = new Anchor(aiwc.leaveGroup());
      leave.addClickHandler(new ClickHandler() {
        public void onClick(ClickEvent event) {
          presenter.leaveGroup(index);
        }
      });
      groups.setWidget(i, gLeaveCol, leave);
     
      if (index % 2 != 0)
        groups.getRowFormatter().addStyleName(i, css.main().tableAlt());
    } // for all requests //
  } // setNumGroups //
 
  @Override
  public void setGroupName(int index, String name) {
    groups.setText(index+1, gNameCol, name);
  }
  
  @Override
  public void setGroupDescription(int index, String description) {
    groups.setText(index+1, gDescCol, description);
  }
  
  @Override
  public void setGroupOwner(int index, String owner) {
    groups.setText(index+1, gOwnerCol, owner);
  }
  
  @Override
  public void setNumInvites(int num) {
    invites.resizeRows(num+1);
    for (int i = 1; i <= num; i++) {
      final int index = i-1;
      Anchor accept = new Anchor(aiwc.accept());
      accept.addClickHandler(new ClickHandler() {
        public void onClick(ClickEvent event) {
          presenter.acceptInvite(index);
        }
      });
      invites.setWidget(i, iAcceptCol, accept);
     
      Anchor decline = new Anchor(aiwc.decline());
      decline.addClickHandler(new ClickHandler() {
        public void onClick(ClickEvent event) {
          presenter.declineInvite(index);
        }
      });
      invites.setWidget(i, iDeclineCol, decline);
     
      if (index % 2 != 0)
        groups.getRowFormatter().addStyleName(i, css.main().tableAlt());
    } // for all requests //
  } // setNumGroups //
 
  @Override
  public void setInviteName(int index, String name) {
    invites.setText(index+1, iNameCol, name);
  }
  
  @Override
  public void setInviteOwner(int index, String owner) {
    invites.setText(index+1, iOwnerCol, owner);
  }
  
  @Override
  public void setInviteEmail(int index, String email) {
    invites.setText(index+1, iEmailCol, email);
  }
  
  @Override
  public AiwConstants getAiwc() {
    return aiwc;
  }
  
  @Override
  public AiwMessages getAiwm() {
    return aiwm;
  }
  
  @Override
  public void setPresenter(Presenter presenter) {
    this.presenter = presenter;
  }
  
  // ================================================================
  // END: RequestsView methods
  // ================================================================
  
} // RequestsViewImpl //
