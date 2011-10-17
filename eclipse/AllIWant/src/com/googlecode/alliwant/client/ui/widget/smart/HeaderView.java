/**
 * @file HeaderView.java
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

import com.googlecode.alliwant.client.i18n.AiwConstants;
import com.googlecode.alliwant.client.i18n.AiwMessages;
import com.googlecode.alliwant.client.ui.JhbView;

public interface HeaderView extends JhbView {
  void setUser(String user);
  void setLogoutURL(String url);
  void setListsActive(boolean active);
  void setGroupsActive(boolean active);
  void setRequestsActive(boolean active);
  void setRequestsVisible(boolean visible);
  void setSettingsActive(boolean active);
  void redirect(String url);
  AiwConstants getConsts();
  AiwMessages getMsgs();
 
  void setPresenter(Presenter presenter);
  interface Presenter {
    void lists();
    void groups();
    void requests();
    void settings();
  }
} // HeaderView //
