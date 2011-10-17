/**
 * @file GroupsViewTestImpl.java
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

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.alliwant.client.i18n.AiwConstants;
import com.googlecode.alliwant.client.i18n.AiwConstantsTestImpl;
import com.googlecode.alliwant.client.i18n.AiwMessages;
import com.googlecode.alliwant.client.i18n.AiwMessagesTestImpl;

public class GroupsViewTestImpl implements GroupsView {

  private boolean processing = false;
  private IsWidget header = null;
  private Presenter presenter = null;
  private String[] gNames, gDescs, gOwners;
  private String[] iNames, iOwners, iEmails;
  private AiwMessages aiwm = new AiwMessagesTestImpl();
  private AiwConstants aiwc = new AiwConstantsTestImpl();
  
  @Override
  public Widget asWidget() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void showProcessingOverlay() {
    processing = true;
  }

  @Override
  public void hideProcessingOverlay() {
    processing = false;
  }
  
  @Override
  public void setHeader(IsWidget header) {
    this.header = header;
  }
  
  @Override
  public void setNumGroups(int num) {
    gNames = new String[num];
    gDescs = new String[num];
    gOwners = new String[num];
    for (int i = 0; i < num; i++) {
      gNames[i] = "";
      gDescs[i] = "";
      gOwners[i] = "";
    }
  } // setNumGroups //
 
  @Override
  public void setGroupName(int index, String name) {
    gNames[index] = name;
  }
  
  @Override
  public void setGroupDescription(int index, String desc) {
    gDescs[index] = desc;
  }
 
  @Override
  public void setGroupOwner(int index, String owner) {
    gOwners[index] = owner;
  }
 
  @Override
  public void setNumInvites(int num) {
    iNames = new String[num];
    iOwners = new String[num];
    iEmails = new String[num];
    for (int i = 0; i < num; i++) {
      iNames[i] = "";
      iOwners[i] = "";
      iEmails[i] = "";
    }
  } // setNumInvites //
 
  @Override
  public void setInviteName(int index, String name) {
    iNames[index] = name;
  }
  
  @Override
  public void setInviteOwner(int index, String owner) {
    iOwners[index] = owner;
  }
 
  @Override
  public void setInviteEmail(int index, String email) {
    iEmails[index] = email;
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
  
  public boolean isProcessing() {
    return processing;
  }

  public IsWidget getHeader() {
    return header;
  }
  
  public Presenter getPresenter() {
    return presenter;
  }
  
  public String getGroupName(int index) {
    return gNames[index];
  }
  
  public String getGroupDesc(int index) {
    return gDescs[index];
  }
  
  public String getGroupOwner(int index) {
    return gOwners[index];
  }
  
  public String getInviteName(int index) {
    return iNames[index];
  }
  
  public String getInviteOwner(int index) {
    return iOwners[index];
  }
  
  public String getInviteEmail(int index) {
    return iEmails[index];
  }
  
} // RequestsViewTestImpl //
