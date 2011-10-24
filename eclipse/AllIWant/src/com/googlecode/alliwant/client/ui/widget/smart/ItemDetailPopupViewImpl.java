/**
 * @file ItemDetailPopupViewImpl.java
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
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.googlecode.alliwant.client.i18n.AiwConstants;

public class ItemDetailPopupViewImpl implements ItemDetailPopupView {

  interface Binder extends UiBinder<PopupPanel, ItemDetailPopupViewImpl> {}
  private Binder uiBinder = GWT.create(Binder.class);
  
  private PopupPanel popup;
  private AiwConstants aiwc = GWT.create(AiwConstants.class);
  private Presenter presenter = null;
  
  @UiField
  Label name, category, description, reservedBy, purchasedBy;
 
  @UiField
  Anchor url, action;
  
  @UiField
  Button done;
  
  public ItemDetailPopupViewImpl() {
    popup = uiBinder.createAndBindUi(this);
  }
  
  @UiHandler("done")
  void onDoneClick(ClickEvent event) {
    presenter.done();
  }
  
  @UiHandler("action")
  void onActionClick(ClickEvent event) {
    presenter.action();
  }
  
  @UiHandler("url")
  void onUrlClick(ClickEvent event) {
    presenter.goToUrl();
  }
  
  // ================================================================
  // BEGIN: ItemDetailPopupView methods
  // ================================================================
  
  @Override
  public void show() {
    popup.center();
  }
  
  @Override
  public void hide() {
    popup.hide();
  }
  
  @Override
  public void setName(String name) {
    this.name.setText(name);
  }
  
  @Override
  public void setCategory(String category) {
    this.category.setText(category);
  }
  
  @Override
  public void setDescription(String description) {
    this.description.setText(description);
  }
  
  @Override
  public void setLinkVisible(boolean visible) {
    url.setVisible(visible);
  }
  
  @Override
  public void setReservedBy(String reservedBy) {
    this.reservedBy.setText(reservedBy);
  }
  
  @Override
  public void setPurchasedBy(String purchasedBy) {
    this.purchasedBy.setText(purchasedBy);
  }
  
  @Override
  public void setActionVisible(boolean visible) {
    action.setVisible(visible);
  }
  
  @Override
  public void setActionText(String action) {
    this.action.setText(action);
  }
  
  @Override
  public void openURL(String url) {
    Window.open(url, "_blank", "");
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
  // END: ItemDetailPopupView methods
  // ================================================================

} // EditListPopupViewImpl //
