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
 * File: org.bgp4j.net.CapabilityTest.java 
 */
package org.bgp4j.net;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Tests equals(), hashCode() and compareTo() for Capability and subclasses
 * 
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class CapabilityTest {

	@Test
	public void testAutonomousSystem4Capability() {
		AutonomousSystem4Capability cap1 = new AutonomousSystem4Capability(10);
		AutonomousSystem4Capability cap2 = new AutonomousSystem4Capability(10);
		AutonomousSystem4Capability cap3 = new AutonomousSystem4Capability(20);

		Assert.assertTrue(cap1.equals(cap2));
		Assert.assertTrue(cap1.hashCode() == cap2.hashCode());
		Assert.assertEquals(0, cap1.compareTo(cap2));

		Assert.assertFalse(cap1.equals(cap3));
		Assert.assertFalse(cap1.hashCode() == cap3.hashCode());
		Assert.assertTrue(cap1.compareTo(cap3) < 0);
		Assert.assertTrue(cap3.compareTo(cap1) > 0);
		
	}
	
	@Test
	public void testMultiprotocolCapabilityTest() {
		MultiProtocolCapability cap1 = new MultiProtocolCapability(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING);
		MultiProtocolCapability cap2 = new MultiProtocolCapability(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING);
		MultiProtocolCapability cap3 = new MultiProtocolCapability(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING);
		MultiProtocolCapability cap4 = new MultiProtocolCapability(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_MULTICAST_FORWARDING);
		MultiProtocolCapability cap5 = new MultiProtocolCapability(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_MULTICAST_FORWARDING);

		Assert.assertTrue(cap1.equals(cap2));
		Assert.assertTrue(cap1.hashCode() == cap2.hashCode());
		Assert.assertEquals(0, cap1.compareTo(cap2));

		Assert.assertFalse(cap1.equals(cap3));
		Assert.assertFalse(cap1.hashCode() == cap3.hashCode());
		Assert.assertTrue(cap1.compareTo(cap3) < 0);
		Assert.assertTrue(cap3.compareTo(cap1) > 0);

		Assert.assertFalse(cap1.equals(cap4));
		Assert.assertFalse(cap1.hashCode() == cap4.hashCode());
		Assert.assertTrue(cap1.compareTo(cap4) < 0);
		Assert.assertTrue(cap4.compareTo(cap1) > 0);

		Assert.assertFalse(cap1.equals(cap5));
		Assert.assertFalse(cap1.hashCode() == cap5.hashCode());
		Assert.assertTrue(cap1.compareTo(cap5) < 0);
		Assert.assertTrue(cap5.compareTo(cap1) > 0);

		Assert.assertFalse(cap3.equals(cap5));
		Assert.assertFalse(cap3.hashCode() == cap5.hashCode());
		Assert.assertTrue(cap3.compareTo(cap5) < 0);
		Assert.assertTrue(cap5.compareTo(cap3) > 0);
	}
	
	@Test
	public void testOutboundRouteFilteringCapability() {
		Map<ORFType, ORFSendReceive> map1 = new HashMap<ORFType, ORFSendReceive>();
		Map<ORFType, ORFSendReceive> map2 = new HashMap<ORFType, ORFSendReceive>();
		Map<ORFType, ORFSendReceive> map3 = new HashMap<ORFType, ORFSendReceive>();
		Map<ORFType, ORFSendReceive> map4 = new HashMap<ORFType, ORFSendReceive>();
		Map<ORFType, ORFSendReceive> map5 = new HashMap<ORFType, ORFSendReceive>();
		Map<ORFType, ORFSendReceive> map6 = new HashMap<ORFType, ORFSendReceive>();
		Map<ORFType, ORFSendReceive> map7 = new HashMap<ORFType, ORFSendReceive>();
		Map<ORFType, ORFSendReceive> map8 = new HashMap<ORFType, ORFSendReceive>();
		Map<ORFType, ORFSendReceive> map9 = new HashMap<ORFType, ORFSendReceive>();
		Map<ORFType, ORFSendReceive> map10 = new HashMap<ORFType, ORFSendReceive>();

		map3.put(ORFType.ADDRESS_PREFIX_BASED, ORFSendReceive.RECEIVE);
		map4.put(ORFType.ADDRESS_PREFIX_BASED, ORFSendReceive.RECEIVE);
		map7.put(ORFType.ADDRESS_PREFIX_BASED, ORFSendReceive.SEND);
		map8.put(ORFType.ADDRESS_PREFIX_BASED, ORFSendReceive.BOTH);
		map9.put(ORFType.ADDRESS_PREFIX_BASED, ORFSendReceive.SEND);
		map10.put(ORFType.ADDRESS_PREFIX_BASED, ORFSendReceive.BOTH);

		OutboundRouteFilteringCapability cap1 = new OutboundRouteFilteringCapability(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, map1);
		OutboundRouteFilteringCapability cap2 = new OutboundRouteFilteringCapability(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, map2);
		OutboundRouteFilteringCapability cap3 = new OutboundRouteFilteringCapability(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, map3);
		OutboundRouteFilteringCapability cap4 = new OutboundRouteFilteringCapability(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, map4);
		OutboundRouteFilteringCapability cap5 = new OutboundRouteFilteringCapability(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, map5);
		OutboundRouteFilteringCapability cap6 = new OutboundRouteFilteringCapability(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_MULTICAST_FORWARDING, map6);
		OutboundRouteFilteringCapability cap7 = new OutboundRouteFilteringCapability(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, map7);
		OutboundRouteFilteringCapability cap8 = new OutboundRouteFilteringCapability(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, map8);
		OutboundRouteFilteringCapability cap9 = new OutboundRouteFilteringCapability(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, map9);
		OutboundRouteFilteringCapability cap10 = new OutboundRouteFilteringCapability(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, map10);

		Assert.assertTrue(cap1.equals(cap2));
		Assert.assertTrue(cap1.hashCode() == cap2.hashCode());
		Assert.assertEquals(0, cap1.compareTo(cap2));

		Assert.assertFalse(cap1.equals(cap3));
		Assert.assertFalse(cap1.hashCode() == cap3.hashCode());
		Assert.assertTrue(cap1.compareTo(cap3) < 0);
		Assert.assertTrue(cap3.compareTo(cap1) > 0);

		Assert.assertTrue(cap3.equals(cap4));
		Assert.assertTrue(cap3.hashCode() == cap4.hashCode());
		Assert.assertEquals(0, cap3.compareTo(cap4));

		Assert.assertFalse(cap1.equals(cap5));
		Assert.assertFalse(cap5.hashCode() == cap3.hashCode());
		Assert.assertTrue(cap1.compareTo(cap5) < 0);
		Assert.assertTrue(cap5.compareTo(cap1) > 0);

		Assert.assertFalse(cap1.equals(cap6));
		Assert.assertFalse(cap1.hashCode() == cap6.hashCode());
		Assert.assertTrue(cap1.compareTo(cap6) < 0);
		Assert.assertTrue(cap6.compareTo(cap1) > 0);

		Assert.assertFalse(cap3.equals(cap7));
		Assert.assertFalse(cap7.hashCode() == cap3.hashCode());
		Assert.assertTrue(cap3.compareTo(cap7) < 0);
		Assert.assertTrue(cap7.compareTo(cap3) > 0);

		Assert.assertFalse(cap3.equals(cap8));
		Assert.assertFalse(cap8.hashCode() == cap3.hashCode());
		Assert.assertTrue(cap3.compareTo(cap8) < 0);
		Assert.assertTrue(cap8.compareTo(cap3) > 0);

		Assert.assertFalse(cap7.equals(cap8));
		Assert.assertFalse(cap8.hashCode() == cap7.hashCode());
		Assert.assertTrue(cap3.compareTo(cap7) < 0);
		Assert.assertTrue(cap7.compareTo(cap3) > 0);

		Assert.assertFalse(cap7.equals(cap9));
		Assert.assertFalse(cap9.hashCode() == cap7.hashCode());
		Assert.assertTrue(cap7.compareTo(cap9) < 0);
		Assert.assertTrue(cap9.compareTo(cap7) > 0);

		Assert.assertFalse(cap8.equals(cap10));
		Assert.assertFalse(cap10.hashCode() == cap8.hashCode());
		Assert.assertTrue(cap8.compareTo(cap10) < 0);
		Assert.assertTrue(cap10.compareTo(cap8) > 0);
	}
	
	@Test
	public void testRouteRefreshCapability() {
		RouteRefreshCapability cap1 = new RouteRefreshCapability();
		RouteRefreshCapability cap2 = new RouteRefreshCapability();
		
		Assert.assertTrue(cap1.equals(cap2));
		Assert.assertTrue(cap1.hashCode() == cap2.hashCode());
		Assert.assertEquals(0, cap1.compareTo(cap2));
	}
	
	@Test
	public void testUnknownCapability() {
		UnknownCapability cap1 = new UnknownCapability(1, new byte[] { 0x0a, (byte)0xa0 });
		UnknownCapability cap2 = new UnknownCapability(1, new byte[] { 0x0a, (byte)0xa0 });
		UnknownCapability cap3 = new UnknownCapability(2, new byte[] { 0x0a, (byte)0xa0 });
		UnknownCapability cap4 = new UnknownCapability(1, new byte[] { 0x0b, (byte)0xa0 });
		UnknownCapability cap5 = new UnknownCapability(1, new byte[] { 0x0a, (byte)0xb0 });
		UnknownCapability cap6 = new UnknownCapability(2, new byte[] { 0x0b, (byte)0xb0 });

		Assert.assertTrue(cap1.equals(cap2));
		Assert.assertTrue(cap1.hashCode() == cap2.hashCode());
		Assert.assertEquals(0, cap1.compareTo(cap2));

		Assert.assertFalse(cap1.equals(cap3));
		Assert.assertFalse(cap1.hashCode() == cap3.hashCode());
		Assert.assertTrue(cap1.compareTo(cap3) < 0);
		Assert.assertTrue(cap3.compareTo(cap1) > 0);

		Assert.assertFalse(cap1.equals(cap4));
		Assert.assertFalse(cap1.hashCode() == cap4.hashCode());
		Assert.assertTrue(cap1.compareTo(cap4) < 0);
		Assert.assertTrue(cap4.compareTo(cap1) > 0);

		Assert.assertFalse(cap1.equals(cap5));
		Assert.assertFalse(cap1.hashCode() == cap5.hashCode());
		Assert.assertTrue(cap1.compareTo(cap5) < 0);
		Assert.assertTrue(cap5.compareTo(cap1) > 0);

		Assert.assertFalse(cap1.equals(cap6));
		Assert.assertFalse(cap1.hashCode() == cap6.hashCode());
		Assert.assertTrue(cap1.compareTo(cap6) < 0);
		Assert.assertTrue(cap6.compareTo(cap1) > 0);

		Assert.assertFalse(cap2.equals(cap6));
		Assert.assertFalse(cap2.hashCode() == cap6.hashCode());
		Assert.assertTrue(cap2.compareTo(cap6) < 0);
		Assert.assertTrue(cap6.compareTo(cap2) > 0);
	}
	
	@Test
	public void testMixedCapabilities() {
		AutonomousSystem4Capability cap1 = new AutonomousSystem4Capability(10);
		MultiProtocolCapability cap2 = new MultiProtocolCapability(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING);
		OutboundRouteFilteringCapability cap3 = new OutboundRouteFilteringCapability(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING);
		RouteRefreshCapability cap4 = new RouteRefreshCapability();
		UnknownCapability cap5 = new UnknownCapability(1, new byte[] { 0x0a, (byte)0xa0 });

		Assert.assertFalse(cap1.equals(cap2));
		Assert.assertFalse(cap1.hashCode() == cap2.hashCode());
		Assert.assertTrue(cap1.compareTo(cap2) < 0);
		Assert.assertTrue(cap2.compareTo(cap1) > 0);
		
		Assert.assertFalse(cap1.equals(cap3));
		Assert.assertFalse(cap1.hashCode() == cap3.hashCode());
		Assert.assertTrue(cap1.compareTo(cap3) < 0);
		Assert.assertTrue(cap3.compareTo(cap1) > 0);

		Assert.assertFalse(cap1.equals(cap4));
		Assert.assertFalse(cap1.hashCode() == cap4.hashCode());
		Assert.assertTrue(cap1.compareTo(cap4) < 0);
		Assert.assertTrue(cap4.compareTo(cap1) > 0);

		Assert.assertFalse(cap1.equals(cap5));
		Assert.assertFalse(cap1.hashCode() == cap5.hashCode());
		Assert.assertTrue(cap1.compareTo(cap5) < 0);
		Assert.assertTrue(cap5.compareTo(cap1) > 0);
		
		Assert.assertFalse(cap2.equals(cap3));
		Assert.assertFalse(cap2.hashCode() == cap3.hashCode());
		Assert.assertTrue(cap2.compareTo(cap3) < 0);
		Assert.assertTrue(cap3.compareTo(cap2) > 0);

		Assert.assertFalse(cap2.equals(cap4));
		Assert.assertFalse(cap2.hashCode() == cap4.hashCode());
		Assert.assertTrue(cap2.compareTo(cap4) < 0);
		Assert.assertTrue(cap4.compareTo(cap2) > 0);

		Assert.assertFalse(cap2.equals(cap5));
		Assert.assertFalse(cap2.hashCode() == cap5.hashCode());
		Assert.assertTrue(cap2.compareTo(cap5) < 0);
		Assert.assertTrue(cap5.compareTo(cap2) > 0);

		Assert.assertFalse(cap3.equals(cap4));
		Assert.assertFalse(cap3.hashCode() == cap4.hashCode());
		Assert.assertTrue(cap3.compareTo(cap4) < 0);
		Assert.assertTrue(cap4.compareTo(cap3) > 0);

		Assert.assertFalse(cap3.equals(cap5));
		Assert.assertFalse(cap3.hashCode() == cap5.hashCode());
		Assert.assertTrue(cap3.compareTo(cap5) < 0);
		Assert.assertTrue(cap5.compareTo(cap3) > 0);

		Assert.assertFalse(cap4.equals(cap5));
		Assert.assertFalse(cap4.hashCode() == cap5.hashCode());
		Assert.assertTrue(cap4.compareTo(cap5) < 0);
		Assert.assertTrue(cap5.compareTo(cap4) > 0);
	}
}
