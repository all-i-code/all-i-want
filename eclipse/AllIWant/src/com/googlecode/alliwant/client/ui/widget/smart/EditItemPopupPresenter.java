/**
 * @file EditItemPopupPresenter.java
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

public class EditItemPopupPresenter implements EditItemPopup, 
 EditItemPopupView.Presenter {

  private EditItemPopupView view;
  private ListItem item = null;
  private Handler handler = null;
  
  public EditItemPopupPresenter(EditItemPopupView view) {
    view.setPresenter(this);
    this.view = view;
  }
  
  // ================================================================
  // BEGIN: EditListPopup methods
  // ================================================================
  
  @Override
  public void show(Handler handler) {
    this.handler = handler;
    view.setHeader(view.getAiwc().addItem());
    view.setName("");
    view.setCategory("");
    view.setDescription("");
    view.setUrl("");
    view.show();
  }
  
  @Override
  public void show(ListItem item, Handler handler) {
    this.item = item;
    this.handler = handler;
    view.setHeader(view.getAiwc().editItem());
    view.setName(item.getName());
    view.setCategory(item.getCategory());
    view.setDescription(item.getDescription());
    view.setUrl(item.getUrl());
    view.show();
  }

  // ================================================================
  // END: EditItemPopup methods
  // ================================================================
  
  // ================================================================
  // BEGIN: EditItemPopupView.Presener methods
  // ================================================================

  @Override
  public void ok() {
    int id = -1;
    if (null != item) id = item.getId();
    handler.onSave(id, view.getName(), view.getCategory(), 
     view.getDescription(), view.getUrl());
    view.hide();
  }
    
  @Override
  public void cancel() {
    view.setName("");
    view.setCategory("");
    view.setDescription("");
    view.setUrl("");
    view.hide();
  }
  
  // ================================================================
  // END: EditItemPopupView.Presenter methods
  // ================================================================
 
} // EditItemPopupPresenter //
