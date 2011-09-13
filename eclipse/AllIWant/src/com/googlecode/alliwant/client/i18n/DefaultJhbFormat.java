/**
 * @file DefaultJhbFormat.java
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
package com.googlecode.jhb.gwt.client.i18n;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.NumberFormat;

public class DefaultJhbFormat implements JhbFormat {

  private final JhbMessages jhbm = GWT.create(JhbMessages.class);
  private final NumberFormat nf = NumberFormat.getFormat("#0.00");
  
  public DefaultJhbFormat() {}
  
  @Override
  public String formatCurrency(double amount) {
    boolean neg = (amount < 0);
    String formatted = neg ? nf.format(amount) : nf.format(-1 * amount);
    return neg ? jhbm.currency(formatted) : jhbm.negatedCurrency(formatted);
  }

} // DefaultJhbFormat //
