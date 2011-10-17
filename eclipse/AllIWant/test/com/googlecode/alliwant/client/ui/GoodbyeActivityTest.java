/**
 * @file GoodbyeActivityTest.java
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
package com.googlecode.alliwant.client.ui;

import static org.mockito.Mockito.*;
import com.google.gwt.event.shared.EventBus;
import com.googlecode.alliwant.client.ClientFactoryTestImpl;
import com.googlecode.alliwant.client.activity.GoodbyeActivity;
import com.googlecode.alliwant.client.event.ModelEvent;
import com.googlecode.alliwant.client.model.MockModel;
import com.googlecode.alliwant.client.model.User;
import com.googlecode.alliwant.client.place.GoodbyePlace;
import com.googlecode.alliwant.client.rpc.Manager;

import junit.framework.TestCase;

public class GoodbyeActivityTest extends TestCase {

  private AcceptsOneWidgetTestImpl panel;
  private GoodbyePlace place;
  private GoodbyeActivity activity;
  private Manager manager;
  private EventBus eventBus;
  private GoodbyeViewTestImpl view;
  private ClientFactoryTestImpl cf;
 
  // ================================================================
  // BEGIN: TestCase methods 
  // ================================================================
  
  @Override
  public void setUp() throws Exception {
    super.setUp();
    panel = new AcceptsOneWidgetTestImpl();
    cf = new ClientFactoryTestImpl();
    view = cf.getGoodbyeView();
    place = new GoodbyePlace();
    activity = new GoodbyeActivity(place, cf);
    eventBus = cf.getEventBus();
    manager = cf.getManager();
  } // setUp //
  
  // ================================================================
  // END: TestCase methods 
  // ================================================================
  
  
  // ================================================================
  // BEGIN: Tests 
  // ================================================================
  
  public void testSuccess() {
    User user = MockModel.newUser("https://super.secure.login.url");
    start(user);
    assertEquals(view.getJhbc().logoutSuccess(), view.getMessage());
    assertFalse(view.isLogoutVisible());
  } // testSuccess //
  
  public void testFail() {
    String URL = "http://host.domain#Place:";
    view.setURL(URL);
    User user = MockModel.newUser("john.dorian@sacredheart.net", "J.D.", 
     "12345", "https://fine.leave.then");
    start(user);
    assertEquals(view.getJhbc().logoutFail(), view.getMessage());
    assertTrue(view.isLogoutVisible());
    assertEquals(user.getLogoutUrl(), view.getLogoutURL());
  } // testFail //
  
  // ================================================================
  // END: Tests 
  // ================================================================
 
  private void start(User user) {
    assertFalse(view.isProcessingOverlayShowing());
    activity.start(panel, eventBus);
    assertEquals(view, panel.getWidget());
    assertTrue(view.isProcessingOverlayShowing());
    verify(manager).getCurrentUser();
    eventBus.fireEvent(new ModelEvent<User>(User.class, user));
    assertFalse(view.isProcessingOverlayShowing());
  } // start //
  
} // GoodbyeActivityTest //
