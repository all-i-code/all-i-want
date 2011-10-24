/**
 * @file ItemDetailPopupPresenter.java
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

import com.googlecode.alliwant.client.model.ListItem;
import com.googlecode.alliwant.client.model.User;

public class ItemDetailPopupPresenter implements ItemDetailPopup, 
 ItemDetailPopupView.Presenter {

  private ItemDetailPopupView view;
  private ListItem item = null;
  private User user = null;
  private Handler handler = null;
  
  public ItemDetailPopupPresenter(ItemDetailPopupView view) {
    view.setPresenter(this);
    this.view = view;
  }
  
  // ================================================================
  // BEGIN: ItemDetailPopup methods
  // ================================================================
  
  @Override
  public void show(ListItem item, User user, Handler handler) {
    this.item = item;
    this.user = user;
    this.handler = handler;
    
    view.setName(item.getName());
    view.setCategory(item.getCategory());
    view.setDescription(item.getDescription());
    
    String actionText = "";
    boolean available = ((item.getReservedByOwnerId() == 0) && 
     (item.getPurchasedByOwnerId() == 0));
    if (available) {
      actionText = view.getAiwc().reserve();
    } else if (item.getReservedByOwnerId() == user.getOwnerId()) {
      actionText = view.getAiwc().purchase();
    } else if (item.getPurchasedByOwnerId() == user.getOwnerId()) {
      actionText = view.getAiwc().unPurchase();
    }
    view.setActionText(actionText);
    view.setActionVisible(actionText.length() > 0);
    
    view.setLinkVisible(item.getUrl().length() > 0);
    
    view.show();
  } // show //
  
  // ================================================================
  // END: ItemDetailListPopup methods
  // ================================================================
  
  // ================================================================
  // BEGIN: ItemDetailPopupView.Presener methods
  // ================================================================

  @Override
  public void action() {
    if (item.getReservedByOwnerId() == user.getOwnerId()) {
      handler.purchaseItem(item.getId());
    } else if (item.getPurchasedByOwnerId() == user.getOwnerId()) {
      handler.unPurchaseItem(item.getId());
    } else {
      handler.reserveItem(item.getId());
    }
  } // action //
  
  @Override
  public void goToUrl() {
    view.openURL(item.getUrl());
  }
    
  @Override
  public void done() {
    view.hide();
  }
  
  // ================================================================
  // END: ItemDetailPopupView.Presenter methods
  // ================================================================
 
} // ItemDetailPopupPresenter //
