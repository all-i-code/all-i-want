/**
 * @file ModelEvent.java
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
package com.googlecode.jhb.gwt.client.event;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

public class ModelEvent<T> extends GwtEvent<ModelEvent.Handler<T>> {
 
  private static Map<Class<?>, Type<Handler<?>>> TYPES =
    new HashMap<Class<?>, Type<Handler<?>>>();

   public static Type<Handler<?>> getType(Class<?> klass) {
     if (!TYPES.containsKey(klass)) {
       TYPES.put(klass, new Type<Handler<?>>());
     }
     return TYPES.get(klass);
   }

   public static HandlerRegistration register(EventBus eventBus, 
    Class<?> klass, Handler<?> handler) {
     return eventBus.addHandler(getType(klass), handler); 
   }
   
   public interface Handler<T> extends EventHandler {
     void onModel(ModelEvent<T> event);
   }
 
  private final Class<?> klass;
  private final T model;
  
  public ModelEvent(final Class<?> klass, final T model) {
    this.klass = klass;
    this.model = model;
  }
  
  public T getModel() { return model; }
  
  @Override 
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public Type<Handler<T>> getAssociatedType() { 
    return (Type) getType(this.klass); }
  
  @Override 
  public void dispatch(final Handler<T> handler) { 
    handler.onModel(this); 
  }
  
} // ModelEvent //
