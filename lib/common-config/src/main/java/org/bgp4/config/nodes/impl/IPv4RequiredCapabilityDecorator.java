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
package org.bgp4.config.nodes.impl;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import org.bgp4.config.nodes.Capabilities;
import org.bgp4.config.nodes.CapabilitiesDecorator;
import org.bgp4j.net.AddressFamily;
import org.bgp4j.net.SubsequentAddressFamily;
import org.bgp4j.net.capabilities.Capability;
import org.bgp4j.net.capabilities.MultiProtocolCapability;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class IPv4RequiredCapabilityDecorator extends CapabilitiesDecorator {

	private MultiProtocolCapability ipv4UnicastCap = new MultiProtocolCapability(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING);
	private MultiProtocolCapability ipv4AnycastCap = new MultiProtocolCapability(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_MULTICAST_FORWARDING);
	private MultiProtocolCapability ipv4MulticastCap = new MultiProtocolCapability(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_MULTICAST_FORWARDING);
	
	public IPv4RequiredCapabilityDecorator(Capabilities decorated) {
		super(decorated);
	}

	/* (non-Javadoc)
	 * @see org.bgp4.config.nodes.CapabilitiesDecorator#getRequiredCapabilities()
	 */
	@Override
	public Set<Capability> getRequiredCapabilities() {
		Set<Capability> caps = super.getRequiredCapabilities();
		
		if(caps.contains(ipv4AnycastCap)) {
			caps = new TreeSet<Capability>(caps);
			
			caps.remove(ipv4AnycastCap);
			caps.add(ipv4MulticastCap);
			caps.add(ipv4UnicastCap);
			caps = Collections.unmodifiableSet(caps);
		}
		if(!caps.contains(ipv4UnicastCap)) {
			caps = new TreeSet<Capability>(caps);
			
			caps.add(ipv4UnicastCap);
			caps = Collections.unmodifiableSet(caps);
		}
		
		return caps;
	}

	/* (non-Javadoc)
	 * @see org.bgp4.config.nodes.CapabilitiesDecorator#getRequiredCapabilities()
	 */
	@Override
	public Set<Capability> getOptionalCapabilities() {
		Set<Capability> caps = super.getOptionalCapabilities();
		
		if(caps.contains(ipv4AnycastCap)) {
			caps = new TreeSet<Capability>(caps);
			
			caps.remove(ipv4AnycastCap);
			caps.add(ipv4MulticastCap);
			caps = Collections.unmodifiableSet(caps);
		}
		if(caps.contains(ipv4UnicastCap)) {
			caps = new TreeSet<Capability>(caps);
			
			caps.remove(ipv4UnicastCap);
			caps = Collections.unmodifiableSet(caps);
		}
		
		return caps;
	}
}
