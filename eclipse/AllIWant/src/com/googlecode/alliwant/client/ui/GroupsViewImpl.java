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
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.googlecode.alliwant.client.i18n.AiwConstants;
import com.googlecode.alliwant.client.i18n.AiwMessages;
import com.googlecode.alliwant.client.ui.css.CSS;
import com.googlecode.alliwant.client.ui.css.CssBundle;
import com.googlecode.alliwant.client.ui.widget.Table;

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
  Table myGroups, groups, invites;
  
  public GroupsViewImpl() {
    initWidget(uiBinder.createAndBindUi(this));
    int col = 0;
    myGroups.setNumRecords(0);
    myGroups.setNumColumns(6);
    myGroups.setHeader(mgNameCol = col++, aiwc.name());
    myGroups.setHeader(mgDescCol = col++, aiwc.description());
    myGroups.setHeader(mgInvitesCol = col++, aiwc.invitations());
    myGroups.setHeader(mgMembersCol = col++, aiwc.members());
    myGroups.setHeader(mgInviteCol = col++, "", css.main().number());
    myGroups.setHeader(mgDeleteCol = col++, "", css.main().number());
   
    col = 0;
    groups.setNumRecords(0);
    groups.setNumColumns(4);
    groups.setHeader(gNameCol = col++, aiwc.name());
    groups.setHeader(gDescCol = col++, aiwc.description());
    groups.setHeader(gOwnerCol = col++, aiwc.owner());
    groups.setHeader(gLeaveCol = col++, "");
   
    col = 0;
    invites.setNumRecords(0);
    invites.setNumColumns(5);
    invites.setHeader(iNameCol = col++, aiwc.group());
    invites.setHeader(iOwnerCol = col++, aiwc.owner());
    invites.setHeader(iEmailCol = col++, aiwc.email());
    invites.setHeader(iAcceptCol = col++, "");
    invites.setHeader(iDeclineCol = col++, "");
  } // GroupsViewImpl //

  @UiHandler("createGroup")
  void createGroupClick(ClickEvent event) {
    presenter.createGroup();
  }
  
  // ================================================================
  // BEGIN: GroupsView methods
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
    myGroups.setNumRecords(num);
    for (int i = 0; i < num; i++) {
      final int index = i;
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
    } // for all requests //
  } // setNumGroups //
 
  @Override
  public void setMyGroupName(int index, String name) {
    myGroups.setText(index, mgNameCol, name);
  }
  
  @Override
  public void setMyGroupDescription(int index, String description) {
    myGroups.setText(index, mgDescCol, description);
  }
  
  @Override
  public void setMyGroupMemberCount(int index, String memberCount) {
    myGroups.setText(index, mgMembersCol, memberCount, css.main().number());
  }
  
  @Override
  public void setMyGroupInviteCount(int index, String inviteCount) {
    myGroups.setText(index, mgInvitesCol, inviteCount, css.main().number());
  }
  
  @Override
  public void setNumGroups(int num) {
    groups.setNumRecords(num);
    for (int i = 0; i < num; i++) {
      final int index = i;
      Anchor leave = new Anchor(aiwc.leaveGroup());
      leave.addClickHandler(new ClickHandler() {
        public void onClick(ClickEvent event) {
          presenter.leaveGroup(index);
        }
      });
      groups.setWidget(i, gLeaveCol, leave);
    } // for all requests //
  } // setNumGroups //
 
  @Override
  public void setGroupName(int index, String name) {
    groups.setText(index, gNameCol, name);
  }
  
  @Override
  public void setGroupDescription(int index, String description) {
    groups.setText(index, gDescCol, description);
  }
  
  @Override
  public void setGroupOwner(int index, String owner) {
    groups.setText(index, gOwnerCol, owner);
  }
  
  @Override
  public void setNumInvites(int num) {
    invites.setNumRecords(num);
    for (int i = 0; i < num; i++) {
      final int index = i;
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
    } // for all requests //
  } // setNumGroups //
 
  @Override
  public void setInviteName(int index, String name) {
    invites.setText(index, iNameCol, name);
  }
  
  @Override
  public void setInviteOwner(int index, String owner) {
    invites.setText(index, iOwnerCol, owner);
  }
  
  @Override
  public void setInviteEmail(int index, String email) {
    invites.setText(index, iEmailCol, email);
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
  // END: GroupsView methods
  // ================================================================
  
} // GroupsViewImpl //
