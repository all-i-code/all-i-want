/**
 * @file HeaderViewImpl.java
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

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.googlecode.alliwant.client.i18n.AiwMessages;
import com.googlecode.alliwant.client.ui.ProcessingOverlay;

public class HeaderViewImpl extends Composite implements HeaderView {

  interface Binder extends UiBinder<FlowPanel, HeaderViewImpl> {}
  private Binder uiBinder = GWT.create(Binder.class);
  private AiwMessages aiwm = GWT.create(AiwMessages.class);
  
  @UiField
  InlineLabel listsLabel, groupsLabel, user, settingsLabel;
  
  @UiField
  Anchor listsLink, groupsLink, settingsLink, logout;

  
  public HeaderViewImpl() {
    initWidget(uiBinder.createAndBindUi(this));
  }
 
  // ================================================================
  // BEGIN: HeaderView methods
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
  public void setUser(String user) {
    this.user.setText(user);
  }

  @Override
  public void setLogoutURL(String url) {
    logout.setHref(url);
  }

  @Override
  public void setListsActive(boolean active) {
    listsLink.setVisible(!active);
    listsLabel.setVisible(active);
  }

  @Override
  public void setGroupsActive(boolean active) {
    groupsLink.setVisible(!active);
    groupsLabel.setVisible(active);
  }

  @Override
  public void setSettingsActive(boolean active) {
    settingsLink.setVisible(!active);
    settingsLabel.setVisible(active);
  }
  
  @Override
  public String getURL() {
    return Window.Location.getHref();
  }

  @Override
  public void redirect(String url) {
    Window.Location.assign(url);
  }
 
  @Override
  public AiwMessages getMsgs() {
    return aiwm;
  }
  
  // ================================================================
  // END: HeaderView methods
  // ================================================================
  
} // HeaderViewImpl //
