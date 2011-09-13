/**
 * @file DashboardViewImpl.java
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

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.googlecode.alliwant.client.i18n.JhbConstants;
import com.googlecode.alliwant.client.ui.widget.Table;
import com.googlecode.alliwant.client.ui.widget.smart.Header;

public class DashboardViewImpl extends Composite implements DashboardView {

  interface Binder extends UiBinder<FlowPanel, DashboardViewImpl> {}
  private static final Binder uiBinder = GWT.create(Binder.class);
  
  private int bDateCol, bNameCol, aNameCol, aBalCol, tDateCol, tDescCol,
   tAmountCol, tPtCol, tSplitCol;
  
  private JhbConstants jhbc = GWT.create(JhbConstants.class);
  
  @UiField 
  Table bills, accounts, transactions;
 
  @UiField
  SimplePanel headerWrapper;
  
  public DashboardViewImpl(Header header) {
    initWidget(uiBinder.createAndBindUi(this));
    headerWrapper.setWidget(header);
    
    int col = 0;
    bills.setNumColumns(2);
    bills.setHeader(bDateCol = col++, jhbc.date());
    bills.setHeader(bNameCol = col++, jhbc.name());
    
    col = 0;
    accounts.setNumColumns(2);
    accounts.setHeader(aNameCol = col++, jhbc.name());
    accounts.setHeader(aBalCol, jhbc.balance());
    
    col = 0;
    transactions.setNumColumns(5);
    transactions.setHeader(tDateCol = col++, jhbc.date());
    transactions.setHeader(tDescCol = col++, jhbc.description());
    transactions.setHeader(tAmountCol = col++, jhbc.amount());
    transactions.setHeader(tPtCol = col++, jhbc.paymentType());
    transactions.setHeader(tSplitCol = col++, jhbc.split());
  } // DashboardViewImpl //

  // ================================================================
  // BEGIN: RegisterView methods
  // ================================================================
  
  @Override
  public void setNumBills(int numBills) {
    bills.setNumRecords(numBills);
  }

  @Override
  public void setBillName(int index, String name) {
    bills.setText(index, bNameCol, name);
  }

  @Override
  public void setBillDate(int index, String date) {
    bills.setText(index, bDateCol, date);
  }

  @Override
  public void setNumAccounts(int numAccounts) {
    accounts.setNumRecords(numAccounts);
  }

  @Override
  public void setAccountName(int index, String name) {
    accounts.setText(index, aNameCol, name);
  }

  @Override
  public void setAccountBalance(int index, String balance) {
    accounts.setText(index, aBalCol, balance);
  }

  @Override
  public void setNumTransactions(int numTransactions) {
    transactions.setNumRecords(numTransactions);
  }

  @Override
  public void setTransactionDate(int index, String date) {
    transactions.setText(index, tDateCol, date);
  }

  @Override
  public void setTransactionDescription(int index, String description) {
    transactions.setText(index, tDescCol, description);
  }

  @Override
  public void setTransactionAmount(int index, String amount) {
    transactions.setText(index, tAmountCol, amount);
  }

  @Override
  public void setTransactionPaymentType(int index, String paymentType) {
    transactions.setText(index, tPtCol, paymentType);
  }

  @Override
  public void setTransactionSplit(int index, String split) {
    transactions.setText(index, tSplitCol, split);
  }

  // ================================================================
  // END: RegisterView methods
  // ================================================================
  
} // RegisterViewImpl //
