/**
 * @file GoodbyeViewImpl.java
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
package com.googlecode.jhb.gwt.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.googlecode.jhb.gwt.client.i18n.JhbConstants;

public class GoodbyeViewImpl extends Composite implements GoodbyeView {

  interface Binder extends UiBinder<FlowPanel, GoodbyeViewImpl> {}
  private static final Binder uiBinder = GWT.create(Binder.class);
  
  @UiField
  Label message;

  @UiField
  Anchor logout;
  
  private JhbConstants jhbc = GWT.create(JhbConstants.class);
  
  public GoodbyeViewImpl() {
    initWidget(uiBinder.createAndBindUi(this));
  } 

  // ================================================================
  // BEGIN: GoodbyeView methods
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
  public void setMessage(String message) {
    this.message.setText(message);
  }

  @Override
  public void setLogoutURL(String url) {
    logout.setHref(url);
  }
  
  @Override
  public void setLogoutVisible(boolean visible) {
    logout.setVisible(visible);
  }
  
  @Override
  public JhbConstants getJhbc() {
    return jhbc;
  }
 
  @Override
  public String getURL() {
    return Window.Location.getHref();
  }
  
  // ================================================================
  // END: GoodbyeView methods
  // ================================================================
  
} // GoodbyeViewImpl //
