/**
 * @file AlertImpl.java
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
package com.googlecode.alliwant.client.ui.widget;

import com.google.gwt.core.client.GWT; 
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;

public class AlertImpl implements Alert {

  interface Binder extends UiBinder<PopupPanel, AlertImpl> {}
  private Binder uiBinder = GWT.create(Binder.class);
  
  private PopupPanel popup;
  private Handler handler = null;
  
  @UiField Label message;
  @UiField Button ok;
  
  public AlertImpl() {
    message = new Label();
    popup = uiBinder.createAndBindUi(this);
  }
  
  @UiHandler("ok")
  void onOkClick(ClickEvent event) {
    if (null != handler) handler.onClose();
    message.setText("");
    popup.hide();
  }
  
  // ================================================================
  // BEGIN: Alert methods
  // ================================================================
  
  @Override
  public void show(String message) {
    show(message, null);
  }

  @Override
  public void show(String message, Handler handler) {
    this.handler = handler;
    this.message.setText(message);
    popup.center();
  }
  
  // ================================================================
  // END: Alert methods
  // ================================================================

} // AlertViewImpl //
