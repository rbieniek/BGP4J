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
 * File: org.bgp4j.weld.NewAnnotated.java 
 */
package org.bgp4j.weld;

import javax.enterprise.inject.New;
import javax.inject.Inject;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class NewAnnotated {

	private @Inject @New DummyBean instance1;
	private @Inject @New DummyBean instance2;
	
	/**
	 * @return the instance1
	 */
	public DummyBean getInstance1() {
		return instance1;
	}
	/**
	 * @return the instance2
	 */
	public DummyBean getInstance2() {
		return instance2;
	}
	
	
}
