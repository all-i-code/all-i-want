/**
 * @file RpcImpl.java
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

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.googlecode.alliwant.client.i18n.AiwMessages;
import com.googlecode.alliwant.client.logging.Logging;
import com.googlecode.alliwant.client.model.FailureReport;
import com.googlecode.alliwant.client.model.FailureReportImpl;
import com.googlecode.alliwant.client.ui.widget.Alert;

public class RpcImpl implements Rpc {

  private Alert alert;
  private Handler handler;
  private AiwMessages aiwm = GWT.create(AiwMessages.class);
  private RequestCallback callback= new RequestCallback() {
    public void onResponseReceived(Request request, Response response) {
      if (response.getStatusCode() == Response.SC_OK) {
        handler.onComplete(response.getText());
      } else {
        Logging.logger().info("rep: " + response.getText());
        FailureReport fr = FailureReportImpl.decode(response.getText());
        alert.show(fr.getMessage());
      }
    }
    
    public void onError(Request request, Throwable exception) {
      alert.show(aiwm.rpcError(request.toString(), exception.getLocalizedMessage()));
      exception.printStackTrace();
    }
  };
  
  public RpcImpl(Alert alert) {
    this.alert = alert;
  }
  
  // ================================================================
  // BEGIN: Rpc methods
  // ================================================================
  
  @Override
  public String add(String name, String value) {
    return add(name, value, false);
  }
  
  @Override
  public String add(String name, String value, boolean first) {
    return (first ? "" : "&") + name + "=" + URL.encodeQueryString(value);
  }
  
  @Override
  public String add(String name, double value) {
    return add(name, value, false);
  }
  
  @Override
  public String add(String name, double value, boolean first) {
    return add(name, Double.toString(value), first);
  }
  
  @Override
  public String add(String name, int value) {
    return add(name, value, false);
  }
  
  @Override
  public String add(String name, int value, boolean first) {
    return add(name, Integer.toString(value), first);
  }
  
  @Override
  public String add(String name, boolean value) {
    return add(name, value, false);
  }
  
  @Override
  public String add(String name, boolean value, boolean first) {
    return add(name, Boolean.toString(value), first);
  }
  
  @Override
  public void send(String url, Handler handler) {
    this.handler = handler;
    RequestBuilder rb = new RequestBuilder(RequestBuilder.GET, url);
    rb.setCallback(callback);
    try {
      rb.send();
    } catch (RequestException e) {
      alert.show(aiwm.rpcError(url, e.getLocalizedMessage()));
      e.printStackTrace();
    }
  } // send //

  @Override
  public void sendPost(String url, String params, Handler handler) {
    this.handler = handler;
    RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, url);
    try {
      rb.sendRequest(params, callback);
    } catch (RequestException e) {
      alert.show(aiwm.rpcError(url, e.getLocalizedMessage()));
      e.printStackTrace();
    }
  } // sendPost //

  // ================================================================
  // END: Rpc methods
  // ================================================================
  
} // RpcImpl //
