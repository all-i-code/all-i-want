/**
 * @file InfoEvent.java
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
package com.googlecode.alliwant.client.event;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

public class InfoEvent extends GwtEvent<InfoEvent.Handler> {

  public static final String REQ_APPROVED = "reqApproved";
  public static final String REQ_DENIED = "reqDenied";
  public static final String MEMBER_INVITED = "memberInvited";
  public static final String INVITE_ACCEPTED = "inviteAccepted";
  public static final String INVITE_DECLINED = "inviteDeclined";
  public static final String LIST_DELETED = "listDeleted";
  public static final String ITEM_DELETED = "itemDeleted";
  public static final String PERMISSION_DELETED = "permissionDeleted";
  
  private static Map<String, Type<Handler>> TYPES =
    new HashMap<String, Type<Handler>>();

   public static Type<Handler> getType(String message) {
     if (!TYPES.containsKey(message)) {
       TYPES.put(message, new Type<Handler>());
     }
     return TYPES.get(message);
   }

   public static HandlerRegistration register(EventBus eventBus, 
    String message, Handler handler) {
     return eventBus.addHandler(getType(message), handler); 
   }
   
   public interface Handler extends EventHandler {
     void onInfo(InfoEvent event);
   }
 
  private final String message;
  
  public InfoEvent(final String message) {
    this.message = message;
  }
  
  public String getMessage() { return message; }
  
  @Override 
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public Type<Handler> getAssociatedType() { 
    return (Type) getType(this.message); }
  
  @Override 
  public void dispatch(final Handler handler) { 
    handler.onInfo(this); 
  }
  
} // InfoEvent //
