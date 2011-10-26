/**
 * @file EditGroupPopupViewImpl.java
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
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.googlecode.alliwant.client.i18n.AiwConstants;

public class EditGroupPopupViewImpl implements EditGroupPopupView {

  interface Binder extends UiBinder<PopupPanel, EditGroupPopupViewImpl> {}
  private Binder uiBinder = GWT.create(Binder.class);
  
  private PopupPanel popup;
  private AiwConstants aiwc = GWT.create(AiwConstants.class);
  private Presenter presenter = null;
  
  @UiField
  Label header;
 
  @UiField
  TextBox name, description;
  
  @UiField
  Button ok, cancel;
  
  public EditGroupPopupViewImpl() {
    popup = uiBinder.createAndBindUi(this);
  }
  
  @UiHandler("ok")
  void onOkClick(ClickEvent event) {
    presenter.ok();
  }
  
  @UiHandler("cancel")
  void onCancelClick(ClickEvent event) {
    presenter.cancel();
  }
  
  @UiHandler("name")
  void onNameKeyDown(KeyDownEvent event) {
    if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER)
      presenter.ok();
  }
  
  @UiHandler("description")
  void onDescriptionKeyDown(KeyDownEvent event) {
    if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER)
      presenter.ok();
  }
  
  // ================================================================
  // BEGIN: EditGroupPopupView methods
  // ================================================================
  
  @Override
  public void show() {
    popup.center();
    Scheduler.get().scheduleDeferred(new ScheduledCommand() {
      public void execute() {
        name.setFocus(true);
        name.selectAll();
      }
    });
  } // show //
  
  @Override
  public void hide() {
    popup.hide();
  }
  
  @Override
  public void setHeader(String header) {
    this.header.setText(header);
  }
  
  @Override
  public void setName(String name) {
    this.name.setText(name);
  }
  
  @Override
  public String getName() {
    return name.getText();
  }
  
  @Override
  public void setDescription(String description) {
    this.description.setText(description);
  }
  
  @Override
  public String getDescription() {
    return description.getText();
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
  // END: EditGroupPopupView methods
  // ================================================================

} // EditGroupPopupViewImpl //
