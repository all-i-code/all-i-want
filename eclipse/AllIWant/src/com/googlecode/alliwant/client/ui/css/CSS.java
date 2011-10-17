package com.googlecode.alliwant.client.ui.css;

import com.google.gwt.core.client.GWT;

public abstract class CSS {
  private static CssBundle css = GWT.create(CssBundle.class);
  
  public static CssBundle getBundle() {
    return css;
  }
  
  public static void ensureInjected() {
    getBundle().main().ensureInjected();
  }
  
} // CSS //
