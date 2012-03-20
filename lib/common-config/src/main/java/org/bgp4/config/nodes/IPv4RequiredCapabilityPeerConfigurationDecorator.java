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
 * File: org.bgp4.config.nodes.AS4OctetPeerConfigurationDecorator.java 
 */
package org.bgp4.config.nodes;

import org.bgp4.config.nodes.impl.IPv4RequiredCapabilityDecorator;

/**
 * Decorator which assures that the IPv4 unicast routing is always part of the required capabilities.
 * If this capability is set among the optional capabilties, it is removed there. If this capability is not set among
 * the required capabilities, it is set there. 
 * 
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class IPv4RequiredCapabilityPeerConfigurationDecorator extends PeerConfigurationDecorator {

	private IPv4RequiredCapabilityDecorator ipv4decorator;
	
	public IPv4RequiredCapabilityPeerConfigurationDecorator(PeerConfiguration decorated) {
		super(decorated);
		
		ipv4decorator = new IPv4RequiredCapabilityDecorator(super.getCapabilities());
	}

	/* (non-Javadoc)
	 * @see org.bgp4.config.nodes.PeerConfigurationDecorator#getCapabilities()
	 */
	@Override
	public Capabilities getCapabilities() {
		return ipv4decorator;
	}

}
