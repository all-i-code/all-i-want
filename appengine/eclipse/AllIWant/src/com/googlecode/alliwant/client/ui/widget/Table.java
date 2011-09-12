/**
 * @file Table.java
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
package com.googlecode.jhb.gwt.client.ui.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public class Table extends Composite implements IsTable {

  private static TableUiBinder uiBinder = GWT.create(TableUiBinder.class);
  interface TableUiBinder extends UiBinder<Widget, Table> {}

  public interface Css extends CssResource {
    String header();
    String alternate();
  }
  
  public Table() {
    initWidget(uiBinder.createAndBindUi(this));
  }

  @UiField
  Grid grid;
  
  @UiField
  Css css;

  public Table(String firstName) {
    initWidget(uiBinder.createAndBindUi(this));
  }

  // ==========================================================================
  // BEGIN: IsTable methods
  // ==========================================================================
  
  @Override
  public void setNumColumns(int numColumns) {
    int rows = 1;
    if (grid.getRowCount() > rows) rows = grid.getRowCount();
    grid.resize(rows, numColumns);
    grid.getRowFormatter().addStyleName(0, css.header());
  } // setNumColumns //

  @Override
  public void setNumRecords(int numRecords) {
    grid.resizeRows(numRecords + 1); // plus 1 for header row
    for (int i = 0; i < numRecords; i++) {
      int row = i+1;
      if (0 != (i % 2)) 
        grid.getRowFormatter().addStyleName(row, css.alternate());
    }
  } // setNumRecords //

  @Override
  public void setHeader(int column, String text) {
    setHeader(column, text, null);
  }
  
  @Override
  public void setHeader(int column, String text, String style) {
    grid.setText(0, column, text);
    if (null != style) 
      grid.getCellFormatter().addStyleName(0, column, style);
  }

  @Override
  public void setHeader(int column, IsWidget widget) {
    setHeader(column, widget, null);
  }
  
  @Override
  public void setHeader(int column, IsWidget widget, String style) {
    grid.setWidget(0, column, widget.asWidget());
    if (null != style)
      grid.getCellFormatter().addStyleName(0, column, style);
  }

  @Override
  public void setText(int index, int column, String text) {
    setText(index, column, text, null);
  }
  
  @Override
  public void setText(int index, int column, String text, String style) {
    int row = index+1; // skip header row
    grid.setText(row, column, text); 
    if (null != style)
      grid.getCellFormatter().addStyleName(row, column, style);
  }

  @Override
  public void setWidget(int index, int column, IsWidget widget) {
    setWidget(index, column, widget, null);
  }
  
  @Override
  public void setWidget(int index, int column, IsWidget widget, String style) {
    int row = index+1; // skip header row
    grid.setWidget(row, column, widget.asWidget());
    if (null != style)
      grid.getCellFormatter().addStyleName(row, column, style);
  }

  @Override
  public int getNumRecords() {
    return grid.getRowCount() - 1; // don't count header row
  }
  
  //==========================================================================
  // END: IsTable methods
  // ========================================================================== 
  
} // Table //
