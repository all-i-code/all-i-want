/**
 * @file RequestsViewImpl.java
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
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.googlecode.alliwant.client.i18n.AiwConstants;
import com.googlecode.alliwant.client.i18n.AiwMessages;
import com.googlecode.alliwant.client.ui.css.CssBundle;

public class RequestsViewImpl extends Composite implements RequestsView {

  interface Binder extends UiBinder<FlowPanel, RequestsViewImpl> {}
  private static final Binder uiBinder = GWT.create(Binder.class);

  private Presenter presenter = null;
  private AiwConstants aiwc = GWT.create(AiwConstants.class);
  private AiwMessages aiwm = GWT.create(AiwMessages.class);
  private CssBundle css = GWT.create(CssBundle.class);
  
  @UiField
  SimplePanel headerWrapper;

  @UiField
  Grid requests;
  
  public RequestsViewImpl() {
    initWidget(uiBinder.createAndBindUi(this));
    requests.resize(1, 4);
    requests.getRowFormatter().addStyleName(0, css.font().tableHeader());
    requests.setText(0, 0, aiwc.email());
    requests.setText(0, 1, aiwc.denied());
  } // RequestsViewImpl //

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
  public void setNumRequests(int num) {
    requests.resizeRows(num+1);
    for (int i = 1; i <= num; i++) {
      final int index = i-1;
      Anchor approve = new Anchor(aiwc.approve());
      approve.addClickHandler(new ClickHandler() {
        public void onClick(ClickEvent event) {
          presenter.approve(index);
        }
      });
      requests.setWidget(i, 2, approve);
     
      Anchor deny = new Anchor(aiwc.deny());
      deny.addClickHandler(new ClickHandler() {
        public void onClick(ClickEvent event) {
          presenter.deny(index);
        }
      });
      requests.setWidget(i, 3, deny);
      if (index % 2 != 0)
        requests.getRowFormatter().addStyleName(i, css.font().tableAlt());
    } // for all requests //
  } // setNumRequests //
 
  @Override
  public void setEmail(int index, String email) {
    requests.setText(index+1, 0, email);
  }
  
  @Override
  public void setDenied(int index, String denied) {
    requests.setText(index+1, 1, denied);
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
