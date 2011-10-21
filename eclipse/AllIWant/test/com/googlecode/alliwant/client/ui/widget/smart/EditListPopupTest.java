/**
 * @file EditListPopupTest.java
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

public class EditListPopupTest implements EditListPopup {

  private boolean showing = false;
  private WishList wl = null;
  private Handler handler = null;
  
  @Override
  public void show(Handler handler) {
    showing = true;
    this.handler = handler;
  }

  @Override
  public void show(WishList wl, Handler handler) {
    showing = true;
    this.handler = handler;
    this.wl = wl;
  }
 
  public void close() {
    showing = false;
    handler = null;
    wl = null;
  }
  
  public boolean isShowing() {
    return showing;
  }
  
  public Handler getHandler() {
    return handler;
  }

  public WishList getList() {
    return wl;
  }
  
} // EditListPopupTest //
