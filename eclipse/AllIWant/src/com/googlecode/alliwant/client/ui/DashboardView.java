/**
 * @file DashboardView.java
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
package com.googlecode.jhb.gwt.client.ui;

import com.google.gwt.user.client.ui.IsWidget;

public interface DashboardView extends IsWidget {
  void setNumBills(int numBills);
  void setBillName(int index, String name);
  void setBillDate(int index, String date);
  void setNumAccounts(int numAccounts);
  void setAccountName(int index, String name);
  void setAccountBalance(int index, String balance);
  void setNumTransactions(int numTransactions);
  void setTransactionDate(int index, String date);
  void setTransactionDescription(int index, String description);
  void setTransactionAmount(int index, String amount);
  void setTransactionPaymentType(int index, String paymentType);
  void setTransactionSplit(int index, String split);
}
