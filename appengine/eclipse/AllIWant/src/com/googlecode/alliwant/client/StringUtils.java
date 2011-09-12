/**
 * @file StringUtils.java
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
package com.googlecode.jhb.gwt.client;

public abstract class StringUtils {
 
  /**
   * Check if a String is empty (or null)
   * @param s
   * @return true if s is empty or null
   */
  public static boolean isEmpty(String s) {
    return (null == s) || (0 == s.length());
  }
 
  /**
   * Check if two Strings are equal, allowing either to be null
   * @param s1
   * @param s2
   * @return true if s1 == s2, else false
   */
  public static boolean areEqual(String s1, String s2) {
    if (null == s1) return (null == s2);
    if (null == s2) return false;
    return 0 == s1.compareTo(s2);
  }
} // StringUtils //
