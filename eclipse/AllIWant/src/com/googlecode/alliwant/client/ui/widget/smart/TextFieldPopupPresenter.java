/**
 * @file TextFieldPopupPresenter.java
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

public class TextFieldPopupPresenter implements TextFieldPopup, 
 TextFieldPopupView.Presenter {

  private TextFieldPopupView view;
  private Handler handler = null;
  
  public TextFieldPopupPresenter(TextFieldPopupView view) {
    view.setPresenter(this);
    this.view = view;
  }
  
  // ================================================================
  // BEGIN: TextFieldPopup methods
  // ================================================================
  
  @Override
  public void show(String header, String name, String value, Handler handler) {
    this.handler = handler;
    view.setHeader(header);
    view.setName(name);
    view.setValue(value);
    view.show();
  }
  
  // ================================================================
  // END: TextFieldPopup methods
  // ================================================================
  
  // ================================================================
  // BEGIN: TextFieldPopupView.Presener methods
  // ================================================================

  @Override
  public void ok() {
    handler.onSave(view.getValue());
    view.hide();
  }
    
  @Override
  public void cancel() {
    view.setHeader("");
    view.setName("");
    view.setValue("");
    view.hide();
  }
  
  // ================================================================
  // END: TextFieldPopupView.Presenter methods
  // ================================================================
 
} // TextFieldPopupPresenter //
