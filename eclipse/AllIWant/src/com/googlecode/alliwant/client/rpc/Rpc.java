/**
 * @file Rpc.java
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
package com.googlecode.alliwant.client.rpc;

public interface Rpc {
  String add(String name, String value);
  String add(String name, String value, boolean first);
  String add(String name, double value);
  String add(String name, double value, boolean first);
  String add(String name, int value);
  String add(String name, int value, boolean first);
  String add(String name, boolean value);
  String add(String name, boolean value, boolean first);
  
  void send(String url, Handler handler);
  void sendPost(String url, String params, Handler handler);
  
  interface Handler {
    void onComplete(String result);
  }
} // ServerRequest //
