/**
 * @file IsTable.java
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
package com.googlecode.alliwant.client.ui.widget;

import com.google.gwt.user.client.ui.IsWidget;

public interface IsTable extends IsWidget {
  void setNumColumns(int numColumns);
  void setNumRecords(int numRecords);
  void setHeader(int column, String text);
  void setHeader(int column, String text, String style);
  void setHeader(int column, IsWidget widget);
  void setHeader(int column, IsWidget widget, String style);
  void setText(int index, int column, String text);
  void setText(int index, int column, String text, String style);
  void setWidget(int index, int column, IsWidget widget);
  void setWidget(int index, int column, IsWidget widget, String style);
  int getNumRecords();
}
