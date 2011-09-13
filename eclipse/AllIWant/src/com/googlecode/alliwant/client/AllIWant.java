/**
 * @file AllIWant.java
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
package com.googlecode.alliwant.client;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.googlecode.alliwant.client.mvp.AiwActivityMapper;
import com.googlecode.alliwant.client.mvp.AiwPlaceHistoryMapper;
import com.googlecode.alliwant.client.place.DashboardPlace;
import com.googlecode.alliwant.client.place.PlaceHistoryHandler;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class AllIWant implements EntryPoint {
	
  private final Place defaultPlace = new DashboardPlace();
  private final SimplePanel appWidget = new SimplePanel();
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
	  // Create ClientFactory using deferred binding so we can replace with different
    // impls in gwt.xml
    ClientFactory clientFactory = GWT.create(ClientFactory.class);
    EventBus eventBus = clientFactory.getEventBus();
    PlaceController placeController = clientFactory.getPlaceController();
    
    // Start ActivityManager for the main widget with our ActivityMapper
    ActivityMapper activityMapper = new AiwActivityMapper(clientFactory);
    ActivityManager activityManager = new ActivityManager(activityMapper, eventBus);
    activityManager.setDisplay(appWidget);
    
    // Start PlaceHistoryHandler with our PlaceHistoryMapper
    AiwPlaceHistoryMapper historyMapper = GWT.create(AiwPlaceHistoryMapper.class);
    PlaceHistoryHandler historyHandler = new PlaceHistoryHandler(historyMapper);
	  historyHandler.register(placeController, eventBus, defaultPlace);
	  
	  RootPanel.get().add(appWidget);
	  
	  // Goes to place represented on URL or default place
	  historyHandler.handleCurrentHistory();
	} // onModuleLoad //
} // AllIWant //
