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
package org.bgp4j.config.nodes;

import junit.framework.Assert;

import org.bgp4j.config.nodes.CapabilitiesDecorator;
import org.bgp4j.config.nodes.impl.CapabilitiesImpl;
import org.bgp4j.net.AddressFamily;
import org.bgp4j.net.SubsequentAddressFamily;
import org.bgp4j.net.capabilities.AutonomousSystem4Capability;
import org.bgp4j.net.capabilities.Capability;
import org.bgp4j.net.capabilities.MultiProtocolCapability;
import org.bgp4j.net.capabilities.RouteRefreshCapability;
import org.junit.Test;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class CapabilitiesDecoratorTest {

	@Test
	public void testRequiredCapabilitiesSameSizeSameArgsSameOrder() {
		CapabilitiesImpl cd1 = new CapabilitiesImpl(new Capability[] { 
				new AutonomousSystem4Capability(16), new MultiProtocolCapability(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING),
				new MultiProtocolCapability(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING), new RouteRefreshCapability()
				});
		CapabilitiesImpl cd2 = new CapabilitiesImpl(new Capability[] {
				new AutonomousSystem4Capability(16), new MultiProtocolCapability(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING),
				new MultiProtocolCapability(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING), new RouteRefreshCapability()
				});
		CapabilitiesDecorator cp1 = new CapabilitiesDecorator(cd1);
		CapabilitiesDecorator cp2 = new CapabilitiesDecorator(cd2);
		
		Assert.assertEquals(cd1, cd2);
		Assert.assertEquals(cd1.hashCode(), cd2.hashCode());
		Assert.assertEquals(cp1, cp2);
		Assert.assertEquals(cp1.hashCode(), cp2.hashCode());
		Assert.assertEquals(cd1, cp1);
		Assert.assertEquals(cd1.hashCode(), cp1.hashCode());
		Assert.assertEquals(cd2, cp2);
		Assert.assertEquals(cd2.hashCode(), cp2.hashCode());
	}

	@Test
	public void testRequiredCapabilitiesSameSizeSameArgsDifferentOrder() {
		CapabilitiesImpl cd1 = new CapabilitiesImpl(new Capability[] {
				new AutonomousSystem4Capability(16), new MultiProtocolCapability(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING),
				new MultiProtocolCapability(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING), new RouteRefreshCapability()
		});
		CapabilitiesImpl cd2 = new CapabilitiesImpl(new Capability[] {
				new RouteRefreshCapability(), new MultiProtocolCapability(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING),
				new MultiProtocolCapability(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING), new AutonomousSystem4Capability(16)
		});
		CapabilitiesDecorator cp1 = new CapabilitiesDecorator(cd1);
		CapabilitiesDecorator cp2 = new CapabilitiesDecorator(cd2);
		
		Assert.assertEquals(cd1, cd2);
		Assert.assertEquals(cd1.hashCode(), cd2.hashCode());
		Assert.assertEquals(cp1, cp2);
		Assert.assertEquals(cp1.hashCode(), cp2.hashCode());
		Assert.assertEquals(cd1, cp1);
		Assert.assertEquals(cd1.hashCode(), cp1.hashCode());
		Assert.assertEquals(cd2, cp2);
		Assert.assertEquals(cd2.hashCode(), cp2.hashCode());
	}

	@Test
	public void testRequiredCapabilitiesSameSizeDifferentArgsSameOrder() {
		CapabilitiesImpl cd1 = new CapabilitiesImpl(new Capability[] {
				new AutonomousSystem4Capability(16), new MultiProtocolCapability(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING),
				new MultiProtocolCapability(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING), new RouteRefreshCapability()
		});
		CapabilitiesImpl cd2 = new CapabilitiesImpl(new Capability[] {
				new AutonomousSystem4Capability(16), new MultiProtocolCapability(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING),
				new RouteRefreshCapability()
		});
		CapabilitiesDecorator cp1 = new CapabilitiesDecorator(cd1);
		CapabilitiesDecorator cp2 = new CapabilitiesDecorator(cd2);
		
		Assert.assertEquals(cd1, cp1);
		Assert.assertEquals(cd2, cp2);
		Assert.assertTrue(cd1.hashCode() == cp1.hashCode());
		Assert.assertTrue(cd2.hashCode() == cp2.hashCode());
		Assert.assertFalse(cd1.equals(cd2));
		Assert.assertFalse(cd1.hashCode() == cd2.hashCode());
		Assert.assertFalse(cp1.equals(cp2));
		Assert.assertFalse(cp1.hashCode() == cp2.hashCode());
	}

	@Test
	public void testRequiredCapabilitiesRepeatedArgs() {
		CapabilitiesImpl cd1 = new CapabilitiesImpl(new Capability[] {
				new AutonomousSystem4Capability(16), new MultiProtocolCapability(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING),
				new MultiProtocolCapability(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING), new RouteRefreshCapability()
		});
		CapabilitiesImpl cd2 = new CapabilitiesImpl(new Capability[] {
			new AutonomousSystem4Capability(16), new AutonomousSystem4Capability(16),
			new MultiProtocolCapability(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING), new MultiProtocolCapability(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING),
			new MultiProtocolCapability(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING), new MultiProtocolCapability(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING),
			new RouteRefreshCapability(), new RouteRefreshCapability()
		});
		CapabilitiesDecorator cp1 = new CapabilitiesDecorator(cd1);
		CapabilitiesDecorator cp2 = new CapabilitiesDecorator(cd2);
		
		Assert.assertEquals(cd1, cd2);
		Assert.assertEquals(cd1.hashCode(), cd2.hashCode());
		Assert.assertEquals(cp1, cp2);
		Assert.assertEquals(cp1.hashCode(), cp2.hashCode());
		Assert.assertEquals(cd1, cp1);
		Assert.assertEquals(cd1.hashCode(), cp1.hashCode());
		Assert.assertEquals(cd2, cp2);
		Assert.assertEquals(cd2.hashCode(), cp2.hashCode());
	}
	
	// ---

	@Test
	public void testOptionalCapabilitiesSameSizeSameArgsSameOrder() {
		CapabilitiesImpl cd1 = new CapabilitiesImpl(null, new Capability[] {
			new AutonomousSystem4Capability(16), new MultiProtocolCapability(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING),
			new MultiProtocolCapability(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING), new RouteRefreshCapability()
		});
		CapabilitiesImpl cd2 = new CapabilitiesImpl(null, new Capability[] {
			new AutonomousSystem4Capability(16), new MultiProtocolCapability(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING),
			new MultiProtocolCapability(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING), new RouteRefreshCapability()
		});
		CapabilitiesDecorator cp1 = new CapabilitiesDecorator(cd1);
		CapabilitiesDecorator cp2 = new CapabilitiesDecorator(cd2);
		
		Assert.assertEquals(cd1, cd2);
		Assert.assertEquals(cd1.hashCode(), cd2.hashCode());
		Assert.assertEquals(cp1, cp2);
		Assert.assertEquals(cp1.hashCode(), cp2.hashCode());
		Assert.assertEquals(cd1, cp1);
		Assert.assertEquals(cd1.hashCode(), cp1.hashCode());
		Assert.assertEquals(cd2, cp2);
		Assert.assertEquals(cd2.hashCode(), cp2.hashCode());
	}

	@Test
	public void testOptionalCapabilitiesSameSizeSameArgsDifferentOrder() {
		CapabilitiesImpl cd1 = new CapabilitiesImpl(null, new Capability[] {
			new AutonomousSystem4Capability(16),  new MultiProtocolCapability(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING),
			new MultiProtocolCapability(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING), new RouteRefreshCapability()
		});
		CapabilitiesImpl cd2 = new CapabilitiesImpl(null, new Capability[] {
				new RouteRefreshCapability(), new MultiProtocolCapability(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING),
				new MultiProtocolCapability(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING), new AutonomousSystem4Capability(16)
		});
		CapabilitiesDecorator cp1 = new CapabilitiesDecorator(cd1);
		CapabilitiesDecorator cp2 = new CapabilitiesDecorator(cd2);
		
		Assert.assertEquals(cd1, cd2);
		Assert.assertEquals(cd1.hashCode(), cd2.hashCode());
		Assert.assertEquals(cp1, cp2);
		Assert.assertEquals(cp1.hashCode(), cp2.hashCode());
		Assert.assertEquals(cd1, cp1);
		Assert.assertEquals(cd1.hashCode(), cp1.hashCode());
		Assert.assertEquals(cd2, cp2);
		Assert.assertEquals(cd2.hashCode(), cp2.hashCode());
	}

	@Test
	public void testOptionalCapabilitiesSameSizeDifferentArgsSameOrder() {
		CapabilitiesImpl cd1 = new CapabilitiesImpl(null, new Capability[] {
			new AutonomousSystem4Capability(16), new MultiProtocolCapability(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING),
			new MultiProtocolCapability(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING), new RouteRefreshCapability()
		});
		CapabilitiesImpl cd2 = new CapabilitiesImpl(null, new Capability[] {
			new AutonomousSystem4Capability(16), new MultiProtocolCapability(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING),
			new RouteRefreshCapability()
		});
		CapabilitiesDecorator cp1 = new CapabilitiesDecorator(cd1);
		CapabilitiesDecorator cp2 = new CapabilitiesDecorator(cd2);
		
		Assert.assertEquals(cd1, cp1);
		Assert.assertEquals(cd2, cp2);
		Assert.assertTrue(cd1.hashCode() == cp1.hashCode());
		Assert.assertTrue(cd2.hashCode() == cp2.hashCode());
		Assert.assertFalse(cd1.equals(cd2));
		Assert.assertFalse(cd1.hashCode() == cd2.hashCode());
		Assert.assertFalse(cp1.equals(cp2));
		Assert.assertFalse(cp1.hashCode() == cp2.hashCode());
	}

	@Test
	public void testOptionalCapabilitiesRepeatedArgs() {
		CapabilitiesImpl cd1 = new CapabilitiesImpl(null, new Capability[] {
			new AutonomousSystem4Capability(16), new MultiProtocolCapability(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING),
			new MultiProtocolCapability(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING), new RouteRefreshCapability()
		});
		CapabilitiesImpl cd2 = new CapabilitiesImpl(null, new Capability[] {
			new AutonomousSystem4Capability(16), new AutonomousSystem4Capability(16),
			new MultiProtocolCapability(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING), new MultiProtocolCapability(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING),
			new MultiProtocolCapability(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING), new MultiProtocolCapability(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING),
			new RouteRefreshCapability(), new RouteRefreshCapability()
		});
		CapabilitiesDecorator cp1 = new CapabilitiesDecorator(cd1);
		CapabilitiesDecorator cp2 = new CapabilitiesDecorator(cd2);
		
		Assert.assertEquals(cd1, cd2);
		Assert.assertEquals(cd1.hashCode(), cd2.hashCode());
		Assert.assertEquals(cp1, cp2);
		Assert.assertEquals(cp1.hashCode(), cp2.hashCode());
		Assert.assertEquals(cd1, cp1);
		Assert.assertEquals(cd1.hashCode(), cp1.hashCode());
		Assert.assertEquals(cd2, cp2);
		Assert.assertEquals(cd2.hashCode(), cp2.hashCode());
	}

	@Test
	public void testRequiredCapabilitiesOptionalCapabilitiesSameSizeSameArgsSameOrder() {
		CapabilitiesImpl cd1 = new CapabilitiesImpl(null, new Capability[] {
			new AutonomousSystem4Capability(16), new MultiProtocolCapability(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING),
			new MultiProtocolCapability(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING), new RouteRefreshCapability()
		});
		CapabilitiesImpl cd2 = new CapabilitiesImpl(new Capability[] {
			new AutonomousSystem4Capability(16), new MultiProtocolCapability(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING),
			new MultiProtocolCapability(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING), new RouteRefreshCapability()
		});
		CapabilitiesDecorator cp1 = new CapabilitiesDecorator(cd1);
		CapabilitiesDecorator cp2 = new CapabilitiesDecorator(cd2);
		
		Assert.assertEquals(cd1, cp1);
		Assert.assertEquals(cd2, cp2);
		Assert.assertTrue(cd1.hashCode() == cp1.hashCode());
		Assert.assertTrue(cd2.hashCode() == cp2.hashCode());
		Assert.assertFalse(cd1.equals(cd2));
		Assert.assertFalse(cd1.hashCode() == cd2.hashCode());
		Assert.assertFalse(cp1.equals(cp2));
		Assert.assertFalse(cp1.hashCode() == cp2.hashCode());
	}
}
