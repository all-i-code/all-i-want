/**
 * @file EditListPopupPresenter.java
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

import com.googlecode.alliwant.client.model.WishList;

public class EditListPopupPresenter implements EditListPopup, 
 EditListPopupView.Presenter {

  private EditListPopupView view;
  private WishList wl = null;
  private Handler handler = null;
  
  public EditListPopupPresenter(EditListPopupView view) {
    view.setPresenter(this);
    this.view = view;
  }
  
  // ================================================================
  // BEGIN: EditListPopup methods
  // ================================================================
  
  @Override
  public void show(Handler handler) {
    this.handler = handler;
    view.setHeader(view.getAiwc().addList());
    view.setName("");
    view.setDescription("");
    view.show();
  }
  
  @Override
  public void show(WishList wl, Handler handler) {
    this.wl = wl;
    this.handler = handler;
    view.setHeader(view.getAiwc().editList());
    view.setName(wl.getName());
    view.setDescription(wl.getDescription());
    view.show();
  }

  // ================================================================
  // END: EditListPopup methods
  // ================================================================
  
  // ================================================================
  // BEGIN: EditListPopupView.Presener methods
  // ================================================================

  @Override
  public void ok() {
    int id = -1;
    if (null != wl) id = wl.getId();
    handler.onSave(id, view.getName(), view.getDescription());
    view.hide();
  }
    
  @Override
  public void cancel() {
    view.setName("");
    view.setDescription("");
    view.hide();
  }
  
  // ================================================================
  // END: EditListPopupView.Presenter methods
  // ================================================================
 
} // EditListPopupPresenter //
