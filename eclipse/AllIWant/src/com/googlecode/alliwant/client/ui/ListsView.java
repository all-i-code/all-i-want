/**
 * @file ListsView.java
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
import com.googlecode.alliwant.client.i18n.AiwConstants;
import com.googlecode.alliwant.client.i18n.AiwMessages;

public interface ListsView extends AiwView {
  void setHeader(IsWidget header);
  void clearOwners();
  void addOwnerItem(String item, String value);
  void setOwner(String owner);
  String getOwner();
 
  void setCanEditLists(boolean canEdit);
  void clearLists();
  void addListItem(String item, String value);
  void setList(String list);
  String getList();
  
  void clearOrders();
  void addOrderItem(String item, String value);
  void setOrder(String order);
  String getOrder();
  
  void setOwnItemsVisible(boolean visible);
  void setNumOwnItems(int numItems);
  void setOwnItem(int index, String item);
  void setOwnItemCategory(int index, String category);
  void setOwnItemLinkVisible(int index, boolean visible);
  
  void setItemsVisible(boolean visible);
  void setNumItems(int numItems);
  void setItem(int index, String item);
  void setItemCategory(int index, String category);
  void setItemLinkVisible(int index, boolean visible);
  void setItemStatus(int index, String status);
  void setItemActionVisible(int index, boolean visible);
  void setItemActionText(int index, String text);

  void setChristmasImagesVisible(boolean visible);
  void openURL(String url);
  AiwConstants getAiwc();
  AiwMessages getAiwm();
  void setPresenter(Presenter presenter);
  interface Presenter {
    void userChanged(); 
    void addList();
    void editList();
    void deleteList();
    void listChanged();
    void orderChanged();
    void addItem();
    void goToItemUrl(int index);
    void itemDetail(int index);
    void itemAction(int index);
    void editItem(int index);
    void deleteItem(int index);
  }
}
