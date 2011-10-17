/**
 * @file AiwMessages.java
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
package com.googlecode.alliwant.client.i18n;

import com.google.gwt.i18n.client.Messages;

public interface AiwMessages extends Messages {
 
  @DefaultMessage("{0} <{1}>")
  String user(String nickname, String email);
  
  @DefaultMessage("RPC Error: request: [{0}] error: [{1}]")
  String rpcError(String request, String error);

  @DefaultMessage("Account Request for <{0}> approved.")
  String reqApproved(String email);
  
  @DefaultMessage("Account Request for <{0}> denied")
  String reqDenied(String email);
  
} // AiwMessages //
