/**
 * @file ListsViewImpl.java
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
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;

public class ListsViewImpl extends Composite implements ListsView {

  interface Binder extends UiBinder<FlowPanel, ListsViewImpl> {}
  private static final Binder uiBinder = GWT.create(Binder.class);
  private Presenter presenter;
  
  @UiField
  SimplePanel headerWrapper;

  @UiField
  ListBox listOwner, wishList;

  @UiField
  Anchor addList, addItem;
  
  @UiField
  Grid items;
  
  public ListsViewImpl() {
    initWidget(uiBinder.createAndBindUi(this));
  }

  @UiHandler("listOwner")
  void onOwnerChange(ChangeEvent event) {
    presenter.userChanged();
  }
  
  @UiHandler("wishList")
  void onListChange(ChangeEvent event) {
    presenter.listChanged();
  }
  
  @UiHandler("addList")
  void onAddListClick(ClickEvent event) {
    presenter.addList();
  }
  
  @UiHandler("addItem")
  void onAddItemClick(ClickEvent event) {
    presenter.addItem();
  }
  
  // ================================================================
  // BEGIN: ListsView methods
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
  public void clearOwners() {
    listOwner.clear();
  }
  
  @Override
  public void addOwnerItem(String item, String value) {
    listOwner.addItem(item, value);
  }
  
  @Override
  public String getOwner() {
    return listOwner.getValue(listOwner.getSelectedIndex());
  }
  
  @Override
  public void clearLists() {
    wishList.clear();
  }
  
  @Override
  public void addListItem(String item, String value) {
    wishList.addItem(item, value);
  }
  
  @Override
  public String getList() {
    return wishList.getValue(wishList.getSelectedIndex());
  }
  
  @Override
  public void setPresenter(Presenter presenter) {
    this.presenter = presenter;
  }
  
  // ================================================================
  // END: ListsView methods
  // ================================================================
  
} // ListsViewImpl //
