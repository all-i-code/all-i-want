/**
 * @file HeaderPresenterTest.java
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
package com.googlecode.alliwant.client.ui.widget.smart;

import static org.mockito.Mockito.*;
import com.google.gwt.event.shared.EventBus;
import com.googlecode.alliwant.client.ClientFactoryTestImpl;
import com.googlecode.alliwant.client.event.ModelEvent;
import com.googlecode.alliwant.client.model.MockModel;
import com.googlecode.alliwant.client.model.User;
import com.googlecode.alliwant.client.rpc.Manager;

import junit.framework.TestCase;

public class HeaderPresenterTest extends TestCase {

  private HeaderPresenter presenter;
  private Manager manager;
  private EventBus eventBus;
  private HeaderViewTestImpl view;
  private ClientFactoryTestImpl cf;
 
  // ================================================================
  // BEGIN: TestCase methods 
  // ================================================================
  
  @Override
  public void setUp() throws Exception {
    super.setUp();
    cf = new ClientFactoryTestImpl();
    view = new HeaderViewTestImpl();
    eventBus = cf.getEventBus();
    manager = cf.getManager();
    presenter = new HeaderPresenter(view, eventBus, manager);
  } // setUp //
  
  // ================================================================
  // END: TestCase methods 
  // ================================================================
  
  
  // ================================================================
  // BEGIN: Tests 
  // ================================================================
  
  public void testLogin() {
    String URL = "http://host.domain#Place:";
    view.setURL(URL);
    assertFalse(view.isProcessingOverlayShowing());
    presenter.getUser();
    assertTrue(view.isProcessingOverlayShowing());
    assertEquals("", view.getRedirectTo());
    verify(manager).getCurrentUser(URL);
    User user = MockModel.newUser("https://super.secure.login.url");
    eventBus.fireEvent(new ModelEvent<User>(User.class, user));
    assertFalse(view.isProcessingOverlayShowing());
    assertEquals(user.getLoginUrl(), view.getRedirectTo());
  } // testLogin //
  
  public void testValidUser() {
    String URL = "http://host.domain#Place:";
    view.setURL(URL);
    assertFalse(view.isProcessingOverlayShowing());
    presenter.getUser();
    assertTrue(view.isProcessingOverlayShowing());
    verify(manager).getCurrentUser(URL);
    User user = MockModel.newUser("john.dorian@sacredheart.net", "J.D.", 
     "12345", "https://fine.leave.then");
    eventBus.fireEvent(new ModelEvent<User>(User.class, user));
    assertFalse(view.isProcessingOverlayShowing());
    String label = view.getMsgs().user(user.getNickname(), user.getEmail());
    assertEquals(label, view.getUser());
    assertEquals(user.getLogoutUrl(), view.getLogoutURL());
  } // testValidUser //
  
  // ================================================================
  // END: Tests 
  // ================================================================
  
} // HeaderPresenterTest //
