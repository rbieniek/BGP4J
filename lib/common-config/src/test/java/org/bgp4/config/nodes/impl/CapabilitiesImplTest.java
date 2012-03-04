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
 * File: org.bgp4.config.nodes.impl.CapabilitiesImplTest.java 
 */
package org.bgp4.config.nodes.impl;

import junit.framework.Assert;

import org.bgp4j.net.AddressFamily;
import org.bgp4j.net.AutonomousSystem4Capability;
import org.bgp4j.net.MultiProtocolCapability;
import org.bgp4j.net.RouteRefreshCapability;
import org.bgp4j.net.SubsequentAddressFamily;
import org.junit.Test;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class CapabilitiesImplTest {

	@Test
	public void testCapabilitiesSameSizeSameArgsSameOrder() {
		CapabilitiesImpl cap1 = new CapabilitiesImpl();
		CapabilitiesImpl cap2 = new CapabilitiesImpl();
		
		cap1.addCapability(new AutonomousSystem4Capability(16));
		cap1.addCapability(new MultiProtocolCapability(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING));
		cap1.addCapability(new MultiProtocolCapability(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING));
		cap1.addCapability(new RouteRefreshCapability());
		
		cap2.addCapability(new AutonomousSystem4Capability(16));
		cap2.addCapability(new MultiProtocolCapability(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING));
		cap2.addCapability(new MultiProtocolCapability(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING));
		cap2.addCapability(new RouteRefreshCapability());
		
		Assert.assertEquals(cap1, cap2);
		Assert.assertEquals(cap1.hashCode(), cap2.hashCode());
	}

	@Test
	public void testCapabilitiesSameSizeSameArgsDifferentOrder() {
		CapabilitiesImpl cap1 = new CapabilitiesImpl();
		CapabilitiesImpl cap2 = new CapabilitiesImpl();
		
		cap1.addCapability(new AutonomousSystem4Capability(16));
		cap1.addCapability(new MultiProtocolCapability(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING));
		cap1.addCapability(new MultiProtocolCapability(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING));
		cap1.addCapability(new RouteRefreshCapability());
		
		cap2.addCapability(new RouteRefreshCapability());
		cap2.addCapability(new MultiProtocolCapability(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING));
		cap2.addCapability(new MultiProtocolCapability(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING));
		cap2.addCapability(new AutonomousSystem4Capability(16));
		
		Assert.assertEquals(cap1, cap2);
		Assert.assertEquals(cap1.hashCode(), cap2.hashCode());
	}

	@Test
	public void testCapabilitiesSameSizeDifferentArgsSameOrder() {
		CapabilitiesImpl cap1 = new CapabilitiesImpl();
		CapabilitiesImpl cap2 = new CapabilitiesImpl();
		
		cap1.addCapability(new AutonomousSystem4Capability(16));
		cap1.addCapability(new MultiProtocolCapability(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING));
		cap1.addCapability(new MultiProtocolCapability(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING));
		cap1.addCapability(new RouteRefreshCapability());
		
		cap2.addCapability(new AutonomousSystem4Capability(16));
		cap2.addCapability(new MultiProtocolCapability(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING));
		cap2.addCapability(new RouteRefreshCapability());
		
		Assert.assertFalse(cap1.equals(cap2));
		Assert.assertFalse(cap1.hashCode() == cap2.hashCode());
	}

	@Test
	public void testCapabilitiesRepeatedArgs() {
		CapabilitiesImpl cap1 = new CapabilitiesImpl();
		CapabilitiesImpl cap2 = new CapabilitiesImpl();
		
		cap1.addCapability(new AutonomousSystem4Capability(16));
		cap1.addCapability(new MultiProtocolCapability(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING));
		cap1.addCapability(new MultiProtocolCapability(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING));
		cap1.addCapability(new RouteRefreshCapability());
		
		cap2.addCapability(new AutonomousSystem4Capability(16));
		cap2.addCapability(new AutonomousSystem4Capability(16));
		cap2.addCapability(new MultiProtocolCapability(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING));
		cap2.addCapability(new MultiProtocolCapability(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING));
		cap2.addCapability(new MultiProtocolCapability(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING));
		cap2.addCapability(new MultiProtocolCapability(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING));
		cap2.addCapability(new RouteRefreshCapability());
		cap2.addCapability(new RouteRefreshCapability());
		
		Assert.assertEquals(cap1, cap2);
		Assert.assertEquals(cap1.hashCode(), cap2.hashCode());
	}
}
