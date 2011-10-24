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

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.googlecode.alliwant.client.i18n.AiwConstants;
import com.googlecode.alliwant.client.i18n.AiwMessages;
import com.googlecode.alliwant.client.ui.widget.Table;

public class ListsViewImpl extends Composite implements ListsView {

  interface Binder extends UiBinder<FlowPanel, ListsViewImpl> {}
  private static final Binder uiBinder = GWT.create(Binder.class);

  private AiwConstants aiwc = GWT.create(AiwConstants.class);
  private AiwMessages aiwm = GWT.create(AiwMessages.class);
  private List<HandlerRegistration> regs = new ArrayList<HandlerRegistration>();
  private List<HandlerRegistration> ownRegs = 
   new ArrayList<HandlerRegistration>();
  
  private List<Anchor> ownItemLinks = new ArrayList<Anchor>();
  private List<Anchor> ownItemDetailLinks = new ArrayList<Anchor>();
  private List<Anchor> itemLinks = new ArrayList<Anchor>();
  private List<Anchor> itemDetailLinks = new ArrayList<Anchor>();
  private List<Anchor> itemActionLinks = new ArrayList<Anchor>();
 
  private int oItemCol, oCatCol, oLinkCol, oDetailCol;
  private int itemCol, catCol, linkCol, statCol, actCol, detailCol;
  
  private Presenter presenter;
  
  @UiField
  SimplePanel headerWrapper;

  @UiField
  ListBox listOwner, wishList;

  @UiField
  Anchor addList, editList, addItem;
  
  @UiField
  Table items, ownItems;
  
  public ListsViewImpl() {
    initWidget(uiBinder.createAndBindUi(this));
   
    int col = 0;
    ownItems.setNumColumns(4);
    ownItems.setHeader(oItemCol = col++, aiwc.name());
    ownItems.setHeader(oCatCol = col++, aiwc.category());
    ownItems.setHeader(oLinkCol = col++, aiwc.url());
    ownItems.setHeader(oDetailCol = col++, "");

    col = 0;
    items.setNumColumns(6);
    items.setHeader(itemCol = col++, aiwc.name());
    items.setHeader(catCol = col++, aiwc.category());
    items.setHeader(linkCol = col++, aiwc.url());
    items.setHeader(statCol = col++, aiwc.status());
    items.setHeader(actCol = col++, aiwc.action());
    items.setHeader(detailCol = col++, "");
  } // ListViewImpl //

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
  
  @UiHandler("editList")
  void onEditListClick(ClickEvent event) {
    presenter.editList();
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
  public void setOwner(String owner) {
    int index = 0;
    for (int i = 0; i < listOwner.getItemCount(); i++) {
      String value = listOwner.getValue(i);
      if (0 == value.compareTo(owner)) index = i;
    }
    listOwner.setSelectedIndex(index);
  } // setOwner //
  
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
  public void setList(String list) {
    int index = 0;
    for (int i = 0; i < wishList.getItemCount(); i++) {
      String value = wishList.getValue(i);
      if (0 == value.compareTo(list)) index = i;
    }
    wishList.setSelectedIndex(index);
  } // setList //
  
  @Override
  public String getList() {
    return wishList.getValue(wishList.getSelectedIndex());
  }
  
  @Override
  public void setOwnItemsVisible(boolean visible) {
    ownItems.setVisible(visible);
  }
  
  @Override
  public void setNumOwnItems(int numItems) {
    clearHandlers(ownRegs);
    ownItemLinks.clear();
    ownItemDetailLinks.clear();
   
    ownItems.setNumRecords(numItems);
    for (int i = 0; i < numItems; i++) {
      final int index = i;
      Anchor link = new Anchor(aiwc.link());
      ownRegs.add(link.addClickHandler(new ClickHandler() {
        public void onClick(ClickEvent event) {
          presenter.goToItemUrl(index);
        }
      }));
      ownItemLinks.add(link);
      ownItems.setWidget(index, oLinkCol, link);
      
      Anchor dLink = new Anchor(aiwc.detail());
      ownRegs.add(link.addClickHandler(new ClickHandler() {
        public void onClick(ClickEvent event) {
          presenter.itemDetail(index);
        }
      }));
      ownItemDetailLinks.add(dLink);
      ownItems.setWidget(index, oDetailCol, dLink);
    } // for all items //
  } // setNumOwnItems //
  
  @Override
  public void setOwnItem(int index, String item) {
    ownItems.setText(index, oItemCol, item);
  }
  
  @Override
  public void setOwnItemCategory(int index, String category) {
    ownItems.setText(index, oCatCol, category);
  }
  
  @Override
  public void setOwnItemLinkVisible(int index, boolean visible) {
    ownItemLinks.get(index).setVisible(visible);
  }
  
  @Override
  public void setItemsVisible(boolean visible) {
    items.setVisible(visible);
  }
  
  @Override
  public void setNumItems(int numItems) {
    clearHandlers(regs);
    itemLinks.clear();
    itemActionLinks.clear();
    itemDetailLinks.clear();
   
    items.setNumRecords(numItems);
    for (int i = 0; i < numItems; i++) {
      final int index = i;
      Anchor link = new Anchor(aiwc.link());
      regs.add(link.addClickHandler(new ClickHandler() {
        public void onClick(ClickEvent event) {
          presenter.goToItemUrl(index);
        }
      }));
      itemLinks.add(link);
      items.setWidget(index, linkCol, link);
      
      Anchor action = new Anchor(aiwc.action());
      regs.add(link.addClickHandler(new ClickHandler() {
        public void onClick(ClickEvent event) {
          presenter.itemAction(index);
        }
      }));
      itemActionLinks.add(action);
      items.setWidget(index, actCol, action);
      
      Anchor dLink = new Anchor(aiwc.detail());
      regs.add(link.addClickHandler(new ClickHandler() {
        public void onClick(ClickEvent event) {
          presenter.itemDetail(index);
        }
      }));
      itemDetailLinks.add(dLink);
      items.setWidget(index, detailCol, dLink);
    } // for all items //
    
  } // setNumItems //
  
  @Override
  public void setItem(int index, String item) {
    items.setText(index, itemCol, item);
  }
  
  @Override
  public void setItemCategory(int index, String category) {
    items.setText(index, catCol, category);
  }
  
  @Override
  public void setItemLinkVisible(int index, boolean visible) {
    itemLinks.get(index).setVisible(visible);
  }
  
  @Override
  public void setItemStatus(int index, String status) {
    items.setText(index, statCol, status);
  }
  
  @Override
  public void setItemActionVisible(int index, boolean visible) {
    itemActionLinks.get(index).setVisible(visible);
  }
  
  @Override
  public void setItemActionText(int index, String text) {
    itemActionLinks.get(index).setText(text);
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
  public AiwMessages getAiwm() {
    return aiwm;
  }
  
  @Override
  public void setPresenter(Presenter presenter) {
    this.presenter = presenter;
  }
  
  // ================================================================
  // END: ListsView methods
  // ================================================================
 
  private void clearHandlers(List<HandlerRegistration> regs) {
    for (HandlerRegistration reg : regs) reg.removeHandler();
    regs.clear();
  }
  
} // ListsViewImpl //
