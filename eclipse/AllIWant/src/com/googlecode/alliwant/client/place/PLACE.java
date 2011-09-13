/**
 * @file PLACE.java
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

import com.google.gwt.http.client.URL;

public abstract class PLACE {
  
  // ==========================================================================
  // BEGIN: Encoding methods 
  // ==========================================================================
  
  public static String encode(String name, boolean value) {
    return encode(name, value, false);
  }
  
  public static String encode(String name, boolean value, boolean first) {
    return (first ? "" : "&") + name + "=" + Boolean.toString(value);
  }
  
  public static String encode(String name, double value) {
    return encode(name, value, false);
  }
  
  public static String encode(String name, double value, boolean first) {
    return (first ? "" : "&" ) + name + "=" + Double.toString(value);
  }
  
  public static String encode(String name, int value) {
    return encode(name, value, false);
  }
  
  public static String encode(String name, int value, boolean first) {
    return (first ? "" : "&") + name + "=" + Integer.toString(value);
  }
  
  public static String encode(String name, String value) { 
    return encode(name, value, false); 
  }
  
  public static String encode(String name, String value, boolean first) {
    String encoded = first ? "" : "&";
    encoded += name + "=" + encodeQs(value);
    return encoded;
  }
  
  public static String encodeQs(String decoded) {
    return URL.encodeQueryString(decoded);
  }
  
  public static String encode(String decodedURL) { 
    return URL.encode(decodedURL); 
  }
  
  // ==========================================================================
  // END: Encoding methods 
  // ==========================================================================
  
  // ==========================================================================
  // BEGIN: Decoding methods 
  // ==========================================================================
  
  public static boolean decodeBoolean(String name, String token, 
   boolean defaultValue) {
    try {
      return Boolean.parseBoolean(getParameter(name, token));
    } catch (Exception e) {
      return defaultValue;
    }
  } // decodeBoolean //
  
  public static double decodeDouble(String name, String token, double defaultValue) {
    try {
      return Double.parseDouble(getParameter(name, token));
    } catch (Exception e) {
      return defaultValue;
    }
  } // decodeDouble //
  
  public static int decodeInt(String name, String token, int defaultValue) {
    try {
      return Integer.parseInt(getParameter(name, token));
    } catch (Exception e) {
      return defaultValue;
    }
  } // decodeInt //
  
  public static String decodeString(String name, String token) {
    return getParameter(name, token);
  } // decodeString //
  
  public static String getParameter(String name, String token) {
    int start = token.indexOf(name);
    if (start < 0) return null;
    start = token.indexOf("=", start);
    int end = token.indexOf("&", start);
    return token.substring(start, end);
  } // getParameter //
  
  public static String decodeQs(String encoded) {
    return URL.decodeQueryString(encoded);
  }
  
  public static String decode(String encodedURL) { 
    return URL.decode(encodedURL); 
  }
  
  // ==========================================================================
  // END: Decoding methods 
  // ==========================================================================
  
} // PLACE //
