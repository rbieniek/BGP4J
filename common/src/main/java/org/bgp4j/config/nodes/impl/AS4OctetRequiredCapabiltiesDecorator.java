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
 * File: org.bgp4.config.nodes.impl.AS4OctetRequiredCapabiltiesDecorator.java 
 */
package org.bgp4j.config.nodes.impl;

import java.util.Set;
import java.util.TreeSet;

import org.bgp4j.config.nodes.Capabilities;
import org.bgp4j.config.nodes.CapabilitiesDecorator;
import org.bgp4j.net.capabilities.AutonomousSystem4Capability;
import org.bgp4j.net.capabilities.Capability;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class AS4OctetRequiredCapabiltiesDecorator extends CapabilitiesDecorator {

	private int as4number;
	
	public AS4OctetRequiredCapabiltiesDecorator(Capabilities decorated, int as4number) {
		super(decorated);
		
		this.as4number = as4number;
	}

	/* (non-Javadoc)
	 * @see org.bgp4.config.nodes.CapabilitiesDecorator#getRequiredCapabilities()
	 */
	@Override
	public Set<Capability> getRequiredCapabilities() {
		TreeSet<Capability> requiredCapabilities = new TreeSet<Capability>(super.getRequiredCapabilities());
		boolean found = false;
		
		for(Capability cap : requiredCapabilities) {
			if(cap instanceof AutonomousSystem4Capability) {
				found = true;
				break;
			}
		}
		
		if(!found)
			requiredCapabilities.add(new AutonomousSystem4Capability(as4number));
		
		return requiredCapabilities;
	}

}
