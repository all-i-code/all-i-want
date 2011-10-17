/**
 * @file RequestsViewTestImpl.java
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

public class RequestsViewTestImpl implements RequestsView {

  private boolean processing = false;
  private IsWidget header = null;
  private Presenter presenter = null;
  private String[] emails, denieds;
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
  public void setNumRequests(int num) {
    emails = new String[num];
    denieds = new String[num];
    for (int i = 0; i < num; i++) {
      emails[i] = "";
      denieds[i] = "";
    }
  }
 
  @Override
  public void setEmail(int index, String email) {
    emails[index] = email;
  }
  
  @Override
  public void setDenied(int index, String denied) {
    denieds[index] = denied;
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
  
  public String getEmail(int index) {
    return emails[index];
  }
  
  public String getDenied(int index) {
    return denieds[index];
  }
  
} // RequestsViewTestImpl //
