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
 * File: org.bgp4j.rib.PeerRoutingInformationBaseTest.java 
 */
package org.bgp4j.rib;

import junit.framework.Assert;

import org.bgp4j.net.AddressFamily;
import org.bgp4j.net.AddressFamilyKey;
import org.bgp4j.net.RIBSide;
import org.bgp4j.net.SubsequentAddressFamily;
import org.bgp4j.weld.WeldTestCaseBase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class PeerRoutingInformationBaseTest extends WeldTestCaseBase {

	private PeerRoutingInformationBase prib;
	private CreatedEventCatcher createCatcher;
	private DestroyedEventCatcher destroyCatcher;
	
	@Before
	public void before() {
		createCatcher = obtainInstance(CreatedEventCatcher.class);
		createCatcher.reset();
		destroyCatcher = obtainInstance(DestroyedEventCatcher.class);
		destroyCatcher.reset();
		
		prib = obtainInstance(PeerRoutingInformationBase.class);
		prib.setPeerName("peer");
	}
	
	@After
	public void after() {
		prib = null;
		createCatcher = null;
		destroyCatcher = null;
	}
	
	@Test
	public void testCreateRIBIPv4Unicast() {
		Assert.assertNull(prib.routingBase(RIBSide.Local, new AddressFamilyKey(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING)));
		
		prib.allocateRoutingInformationBase(RIBSide.Local, new AddressFamilyKey(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING));
		Assert.assertNotNull(prib.routingBase(RIBSide.Local, new AddressFamilyKey(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING)));

		Assert.assertEquals(1, createCatcher.ribSize());
		Assert.assertEquals(1, createCatcher.getRIBCreatedCount(new AddressFamilyKey(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING)));
	}

	@Test
	public void testCreateRIBIPv4UnicastMulticast() {
		Assert.assertNull(prib.routingBase(RIBSide.Local, new AddressFamilyKey(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING)));
		Assert.assertNull(prib.routingBase(RIBSide.Local, new AddressFamilyKey(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_MULTICAST_FORWARDING)));
		
		prib.allocateRoutingInformationBase(RIBSide.Local, new AddressFamilyKey(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING));
		Assert.assertNotNull(prib.routingBase(RIBSide.Local, new AddressFamilyKey(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING)));
		prib.allocateRoutingInformationBase(RIBSide.Local, new AddressFamilyKey(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_MULTICAST_FORWARDING));
		Assert.assertNotNull(prib.routingBase(RIBSide.Local, new AddressFamilyKey(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_MULTICAST_FORWARDING)));

		Assert.assertEquals(2, createCatcher.ribSize());
		Assert.assertEquals(1, createCatcher.getRIBCreatedCount(new AddressFamilyKey(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING)));
		Assert.assertEquals(1, createCatcher.getRIBCreatedCount(new AddressFamilyKey(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_MULTICAST_FORWARDING)));
	}

	@Test
	public void testCreateRIBIPv4IPv6Unicast() {
		Assert.assertNull(prib.routingBase(RIBSide.Local, new AddressFamilyKey(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING)));
		Assert.assertNull(prib.routingBase(RIBSide.Local, new AddressFamilyKey(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING)));
		
		prib.allocateRoutingInformationBase(RIBSide.Local, new AddressFamilyKey(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING));
		Assert.assertNotNull(prib.routingBase(RIBSide.Local, new AddressFamilyKey(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING)));
		prib.allocateRoutingInformationBase(RIBSide.Local, new AddressFamilyKey(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING));
		Assert.assertNotNull(prib.routingBase(RIBSide.Local, new AddressFamilyKey(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING)));

		Assert.assertEquals(2, createCatcher.ribSize());
		Assert.assertEquals(1, createCatcher.getRIBCreatedCount(new AddressFamilyKey(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING)));
		Assert.assertEquals(1, createCatcher.getRIBCreatedCount(new AddressFamilyKey(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING)));
	}
	
	@Test
	public void testCreateRIBIPv4UnicastDestroyRIBIPv4Unicast() {
		Assert.assertNull(prib.routingBase(RIBSide.Local, new AddressFamilyKey(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING)));
		
		prib.allocateRoutingInformationBase(RIBSide.Local, new AddressFamilyKey(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING));
		Assert.assertNotNull(prib.routingBase(RIBSide.Local, new AddressFamilyKey(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING)));

		Assert.assertEquals(1, createCatcher.ribSize());
		Assert.assertEquals(1, createCatcher.getRIBCreatedCount(new AddressFamilyKey(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING)));
		Assert.assertEquals(0, destroyCatcher.ribSize());
		Assert.assertEquals(0, destroyCatcher.getRIBCreatedCount(new AddressFamilyKey(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING)));
		
		prib.destroyRoutingInformationBase(RIBSide.Local, new AddressFamilyKey(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING));
		Assert.assertNull(prib.routingBase(RIBSide.Local, new AddressFamilyKey(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING)));
		Assert.assertEquals(1, createCatcher.ribSize());
		Assert.assertEquals(1, createCatcher.getRIBCreatedCount(new AddressFamilyKey(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING)));
		Assert.assertEquals(1, destroyCatcher.ribSize());
		Assert.assertEquals(1, destroyCatcher.getRIBCreatedCount(new AddressFamilyKey(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING)));
	}
	
	@Test
	public void testCreateRIBIPv4UnicastDestroyRIBIPv6Unicast() {
		Assert.assertNull(prib.routingBase(RIBSide.Local, new AddressFamilyKey(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING)));
		
		prib.allocateRoutingInformationBase(RIBSide.Local, new AddressFamilyKey(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING));
		Assert.assertNotNull(prib.routingBase(RIBSide.Local, new AddressFamilyKey(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING)));

		Assert.assertEquals(1, createCatcher.ribSize());
		Assert.assertEquals(1, createCatcher.getRIBCreatedCount(new AddressFamilyKey(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING)));
		Assert.assertEquals(0, destroyCatcher.ribSize());
		Assert.assertEquals(0, destroyCatcher.getRIBCreatedCount(new AddressFamilyKey(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING)));
		
		prib.destroyRoutingInformationBase(RIBSide.Local, new AddressFamilyKey(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING));
		Assert.assertNotNull(prib.routingBase(RIBSide.Local, new AddressFamilyKey(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING)));
		Assert.assertEquals(1, createCatcher.ribSize());
		Assert.assertEquals(1, createCatcher.getRIBCreatedCount(new AddressFamilyKey(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING)));
		Assert.assertEquals(0, destroyCatcher.ribSize());
		Assert.assertEquals(0, destroyCatcher.getRIBCreatedCount(new AddressFamilyKey(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING)));
	}
}
