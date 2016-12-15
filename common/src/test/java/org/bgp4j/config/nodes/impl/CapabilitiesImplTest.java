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
package org.bgp4j.config.nodes.impl;

import junit.framework.Assert;

import org.bgp4j.config.nodes.impl.CapabilitiesImpl;
import org.bgp4j.net.AddressFamily;
import org.bgp4j.net.SubsequentAddressFamily;
import org.bgp4j.net.capabilities.AutonomousSystem4Capability;
import org.bgp4j.net.capabilities.MultiProtocolCapability;
import org.bgp4j.net.capabilities.RouteRefreshCapability;
import org.junit.Test;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class CapabilitiesImplTest {

	@Test
	public void testRequiredCapabilitiesSameSizeSameArgsSameOrder() {
		CapabilitiesImpl cap1 = new CapabilitiesImpl();
		CapabilitiesImpl cap2 = new CapabilitiesImpl();
		
		cap1.addRequiredCapability(new AutonomousSystem4Capability(16));
		cap1.addRequiredCapability(new MultiProtocolCapability(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING));
		cap1.addRequiredCapability(new MultiProtocolCapability(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING));
		cap1.addRequiredCapability(new RouteRefreshCapability());
		
		cap2.addRequiredCapability(new AutonomousSystem4Capability(16));
		cap2.addRequiredCapability(new MultiProtocolCapability(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING));
		cap2.addRequiredCapability(new MultiProtocolCapability(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING));
		cap2.addRequiredCapability(new RouteRefreshCapability());
		
		Assert.assertEquals(cap1, cap2);
		Assert.assertEquals(cap1.hashCode(), cap2.hashCode());
	}

	@Test
	public void testRequiredCapabilitiesSameSizeSameArgsDifferentOrder() {
		CapabilitiesImpl cap1 = new CapabilitiesImpl();
		CapabilitiesImpl cap2 = new CapabilitiesImpl();
		
		cap1.addRequiredCapability(new AutonomousSystem4Capability(16));
		cap1.addRequiredCapability(new MultiProtocolCapability(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING));
		cap1.addRequiredCapability(new MultiProtocolCapability(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING));
		cap1.addRequiredCapability(new RouteRefreshCapability());
		
		cap2.addRequiredCapability(new RouteRefreshCapability());
		cap2.addRequiredCapability(new MultiProtocolCapability(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING));
		cap2.addRequiredCapability(new MultiProtocolCapability(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING));
		cap2.addRequiredCapability(new AutonomousSystem4Capability(16));
		
		Assert.assertEquals(cap1, cap2);
		Assert.assertEquals(cap1.hashCode(), cap2.hashCode());
	}

	@Test
	public void testRequiredCapabilitiesSameSizeDifferentArgsSameOrder() {
		CapabilitiesImpl cap1 = new CapabilitiesImpl();
		CapabilitiesImpl cap2 = new CapabilitiesImpl();
		
		cap1.addRequiredCapability(new AutonomousSystem4Capability(16));
		cap1.addRequiredCapability(new MultiProtocolCapability(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING));
		cap1.addRequiredCapability(new MultiProtocolCapability(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING));
		cap1.addRequiredCapability(new RouteRefreshCapability());
		
		cap2.addRequiredCapability(new AutonomousSystem4Capability(16));
		cap2.addRequiredCapability(new MultiProtocolCapability(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING));
		cap2.addRequiredCapability(new RouteRefreshCapability());
		
		Assert.assertFalse(cap1.equals(cap2));
		Assert.assertFalse(cap1.hashCode() == cap2.hashCode());
	}

	@Test
	public void testRequiredCapabilitiesRepeatedArgs() {
		CapabilitiesImpl cap1 = new CapabilitiesImpl();
		CapabilitiesImpl cap2 = new CapabilitiesImpl();
		
		cap1.addRequiredCapability(new AutonomousSystem4Capability(16));
		cap1.addRequiredCapability(new MultiProtocolCapability(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING));
		cap1.addRequiredCapability(new MultiProtocolCapability(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING));
		cap1.addRequiredCapability(new RouteRefreshCapability());
		
		cap2.addRequiredCapability(new AutonomousSystem4Capability(16));
		cap2.addRequiredCapability(new AutonomousSystem4Capability(16));
		cap2.addRequiredCapability(new MultiProtocolCapability(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING));
		cap2.addRequiredCapability(new MultiProtocolCapability(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING));
		cap2.addRequiredCapability(new MultiProtocolCapability(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING));
		cap2.addRequiredCapability(new MultiProtocolCapability(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING));
		cap2.addRequiredCapability(new RouteRefreshCapability());
		cap2.addRequiredCapability(new RouteRefreshCapability());
		
		Assert.assertEquals(cap1, cap2);
		Assert.assertEquals(cap1.hashCode(), cap2.hashCode());
	}
	
	// ---

	@Test
	public void testOptionalCapabilitiesSameSizeSameArgsSameOrder() {
		CapabilitiesImpl cap1 = new CapabilitiesImpl();
		CapabilitiesImpl cap2 = new CapabilitiesImpl();
		
		cap1.addOptionalCapability(new AutonomousSystem4Capability(16));
		cap1.addOptionalCapability(new MultiProtocolCapability(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING));
		cap1.addOptionalCapability(new MultiProtocolCapability(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING));
		cap1.addOptionalCapability(new RouteRefreshCapability());
		
		cap2.addOptionalCapability(new AutonomousSystem4Capability(16));
		cap2.addOptionalCapability(new MultiProtocolCapability(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING));
		cap2.addOptionalCapability(new MultiProtocolCapability(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING));
		cap2.addOptionalCapability(new RouteRefreshCapability());
		
		Assert.assertEquals(cap1, cap2);
		Assert.assertEquals(cap1.hashCode(), cap2.hashCode());
	}

	@Test
	public void testOptionalCapabilitiesSameSizeSameArgsDifferentOrder() {
		CapabilitiesImpl cap1 = new CapabilitiesImpl();
		CapabilitiesImpl cap2 = new CapabilitiesImpl();
		
		cap1.addOptionalCapability(new AutonomousSystem4Capability(16));
		cap1.addOptionalCapability(new MultiProtocolCapability(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING));
		cap1.addOptionalCapability(new MultiProtocolCapability(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING));
		cap1.addOptionalCapability(new RouteRefreshCapability());
		
		cap2.addOptionalCapability(new RouteRefreshCapability());
		cap2.addOptionalCapability(new MultiProtocolCapability(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING));
		cap2.addOptionalCapability(new MultiProtocolCapability(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING));
		cap2.addOptionalCapability(new AutonomousSystem4Capability(16));
		
		Assert.assertEquals(cap1, cap2);
		Assert.assertEquals(cap1.hashCode(), cap2.hashCode());
	}

	@Test
	public void testOptionalCapabilitiesSameSizeDifferentArgsSameOrder() {
		CapabilitiesImpl cap1 = new CapabilitiesImpl();
		CapabilitiesImpl cap2 = new CapabilitiesImpl();
		
		cap1.addOptionalCapability(new AutonomousSystem4Capability(16));
		cap1.addOptionalCapability(new MultiProtocolCapability(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING));
		cap1.addOptionalCapability(new MultiProtocolCapability(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING));
		cap1.addOptionalCapability(new RouteRefreshCapability());
		
		cap2.addOptionalCapability(new AutonomousSystem4Capability(16));
		cap2.addOptionalCapability(new MultiProtocolCapability(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING));
		cap2.addOptionalCapability(new RouteRefreshCapability());
		
		Assert.assertFalse(cap1.equals(cap2));
		Assert.assertFalse(cap1.hashCode() == cap2.hashCode());
	}

	@Test
	public void testOptionalCapabilitiesRepeatedArgs() {
		CapabilitiesImpl cap1 = new CapabilitiesImpl();
		CapabilitiesImpl cap2 = new CapabilitiesImpl();
		
		cap1.addOptionalCapability(new AutonomousSystem4Capability(16));
		cap1.addOptionalCapability(new MultiProtocolCapability(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING));
		cap1.addOptionalCapability(new MultiProtocolCapability(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING));
		cap1.addOptionalCapability(new RouteRefreshCapability());
		
		cap2.addOptionalCapability(new AutonomousSystem4Capability(16));
		cap2.addOptionalCapability(new AutonomousSystem4Capability(16));
		cap2.addOptionalCapability(new MultiProtocolCapability(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING));
		cap2.addOptionalCapability(new MultiProtocolCapability(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING));
		cap2.addOptionalCapability(new MultiProtocolCapability(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING));
		cap2.addOptionalCapability(new MultiProtocolCapability(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING));
		cap2.addOptionalCapability(new RouteRefreshCapability());
		cap2.addOptionalCapability(new RouteRefreshCapability());
		
		Assert.assertEquals(cap1, cap2);
		Assert.assertEquals(cap1.hashCode(), cap2.hashCode());
	}

	@Test
	public void testRequiredCapabilitiesOptionalCapabilitiesSameSizeSameArgsSameOrder() {
		CapabilitiesImpl cap1 = new CapabilitiesImpl();
		CapabilitiesImpl cap2 = new CapabilitiesImpl();
		
		cap1.addRequiredCapability(new AutonomousSystem4Capability(16));
		cap1.addRequiredCapability(new MultiProtocolCapability(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING));
		cap1.addRequiredCapability(new MultiProtocolCapability(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING));
		cap1.addRequiredCapability(new RouteRefreshCapability());
		
		cap2.addOptionalCapability(new AutonomousSystem4Capability(16));
		cap2.addOptionalCapability(new MultiProtocolCapability(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING));
		cap2.addOptionalCapability(new MultiProtocolCapability(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING));
		cap2.addOptionalCapability(new RouteRefreshCapability());
		
		Assert.assertFalse(cap1.equals(cap2));
		Assert.assertFalse(cap1.hashCode() == cap2.hashCode());
	}


}
