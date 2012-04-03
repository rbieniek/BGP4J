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
 * File: org.bgp4j.rib.web.application.ManagementApplication.java 
 */
package org.bgp4j.management.web.application;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

/**
 * @author Rainer Bieniek (rainer@bgp4j.org)
 *
 */
public class ManagementApplication extends Application {

	private static Set<Class<?>> registeredClasses = new HashSet<Class<?>>();
	private static Set<Object> registeredSingletons = new HashSet<Object>();
	
	/* (non-Javadoc)
	 * @see javax.ws.rs.core.Application#getClasses()
	 */
	@Override
	public Set<Class<?>> getClasses() {
		return registeredClasses;
	}

	/* (non-Javadoc)
	 * @see javax.ws.rs.core.Application#getSingletons()
	 */
	@Override
	public Set<Object> getSingletons() {
		return registeredSingletons;
	}

	public static void addRegisteredClass(Class<?> clazz) {
		registeredClasses.add(clazz);
	}
	
	public static void addRegisteredSingleton(Object singleton) {
		registeredSingletons.add(singleton);
	}
}
