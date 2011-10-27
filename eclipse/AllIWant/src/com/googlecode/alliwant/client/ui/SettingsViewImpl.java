/**
 * @file SettingsViewImpl.java
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

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.googlecode.alliwant.client.i18n.AiwConstants;
import com.googlecode.alliwant.client.ui.widget.Table;

public class SettingsViewImpl extends Composite implements SettingsView {

  interface Binder extends UiBinder<FlowPanel, SettingsViewImpl> {}
  private static final Binder uiBinder = GWT.create(Binder.class);

  private Presenter presenter;
  private AiwConstants aiwc = GWT.create(AiwConstants.class);
  private int emailCol = -1, deleteCol = -1;
  private List<HandlerRegistration> regs = new ArrayList<HandlerRegistration>();
  
  @UiField
  SimplePanel headerWrapper;

  @UiField
  TextBox name, nickname;
 
  @UiField
  Button save;

  @UiField
  Anchor addPerm;
  
  @UiField
  Table permissions;
  
  public SettingsViewImpl() {
    initWidget(uiBinder.createAndBindUi(this));
    permissions.setNumColumns(2);
    permissions.setNumRecords(0);
    int col = 0;
    permissions.setHeader(emailCol = col++, aiwc.email());
    permissions.setHeader(deleteCol = col++, "");
  }
  
  @UiHandler("save")
  void saveClick(ClickEvent event) {
    presenter.updateOwner();
  }
  
  @UiHandler("addPerm")
  void addPermClick(ClickEvent event) {
    presenter.addPermission();
  }
  
  @UiHandler("name")
  void onNameKeyDown(KeyDownEvent event) {
    if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER)
      presenter.updateOwner();
  }
  
  @UiHandler("nickname")
  void onNicknameKeyDown(KeyDownEvent event) {
    if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER)
      presenter.updateOwner();
  }
  
  // ================================================================
  // BEGIN: SettingsView methods
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
  public void setOwnerName(String name) {
    this.name.setText(name);
  }
  
  @Override
  public String getOwnerName() {
    return name.getText();
  }
  
  @Override
  public void setOwnerNickname(String nickname) {
    this.nickname.setText(nickname);
  }
  
  @Override
  public String getOwnerNickname() {
    return nickname.getText();
  }
  
  @Override
  public void setNumPermissions(int count) {
    for (HandlerRegistration reg : regs) reg.removeHandler();
    regs.clear();
    permissions.setNumRecords(count);
    for (int i = 0; i < count; i++) {
      final int index = i;
      Anchor delete = new Anchor(aiwc.delete());
      regs.add(delete.addClickHandler(new ClickHandler() {
        public void onClick(ClickEvent event) {
          presenter.deletePermission(index);
        }
      }));
      permissions.setWidget(index, deleteCol, delete);
    } // for count //
  } // setNumPermissions //
  
  @Override
  public void setPermissionEmail(int index, String email) {
    permissions.setText(index, emailCol, email);
  }

  @Override
  public AiwConstants getAiwc() {
    return aiwc;
  }
  
  @Override
  public void setPresenter(Presenter presenter) {
    this.presenter = presenter;
  }
  
  // ================================================================
  // END: SettingsView methods
  // ================================================================
  
} // SettingsViewImpl //
