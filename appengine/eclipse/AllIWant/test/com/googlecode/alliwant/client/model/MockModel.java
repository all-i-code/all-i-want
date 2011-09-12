/**
 * @file MockModel.java
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
package com.googlecode.jhb.gwt.client.model;

import static org.mockito.Mockito.*;

public abstract class MockModel {

  public static User newUser(String email, String nickname, String userId,
   String logoutURL) {
    User user = mock(User.class);
    when(user.getEmail()).thenReturn(email);
    when(user.getNickname()).thenReturn(nickname);
    when(user.getUserId()).thenReturn(userId);
    when(user.getLogoutUrl()).thenReturn(logoutURL);
    return user;
  } // newUser //
  
  public static User newUser(String loginURL) {
    User user = mock(User.class);
    when(user.getLoginUrl()).thenReturn(loginURL);
    return user;
  } // newUser //
  
} // MockModel //
