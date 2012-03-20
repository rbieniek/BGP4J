/**
 *  Copyright 2012 Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * 
 * File: org.bgp4j.rib.RouteEventCatcher.java 
 */
package org.bgp4j.rib;

import java.util.LinkedList;
import java.util.List;

import javax.enterprise.event.Observes;
import javax.inject.Singleton;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
@Singleton
public class RouteEventCatcher {

	private List<RouteAdded> routeAddedEvents = new LinkedList<RouteAdded>();
	private List<RouteWithdrawn> routeWithdrawnEvents = new LinkedList<RouteWithdrawn>();
	
	public void routeAdded(@Observes RouteAdded event) {
		routeAddedEvents.add(event);
	}
	
	public void routeWithdrawn(@Observes RouteWithdrawn event) {
		routeWithdrawnEvents.add(event);
	}

	/**
	 * @return the routeAddedEvents
	 */
	public List<RouteAdded> getRouteAddedEvents() {
		return routeAddedEvents;
	}

	/**
	 * @return the routeWithdrawnEvents
	 */
	public List<RouteWithdrawn> getRouteWithdrawnEvents() {
		return routeWithdrawnEvents;
	}
	
	public void reset() {
		routeAddedEvents.clear();
		routeWithdrawnEvents.clear();
	}
}
