/**
 * @file RequestsPlace.java
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
package com.googlecode.alliwant.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class RequestsPlace extends Place {
 
  public static final String PREFIX = "Requests";
 
  @Prefix(PREFIX)
  public static class Tokenizer implements PlaceTokenizer<RequestsPlace> {
    
    @Override
    public RequestsPlace getPlace(String token) {
      return new RequestsPlace();
    }

    @Override
    public String getToken(RequestsPlace place) {
      return "";
    }
    
  } // Tokenizer //
} 
