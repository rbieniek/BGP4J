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
 * File: org.bgp4.config.nodes.impl.ServerConfigurationParserTest.java 
 */
package org.bgp4.config.nodes.impl;

import java.util.Iterator;
import java.util.Map;

import junit.framework.Assert;

import org.apache.commons.configuration.XMLConfiguration;
import org.bgp4.config.ConfigTestBase;
import org.bgp4.config.nodes.Capabilities;
import org.bgp4j.net.AddressFamily;
import org.bgp4j.net.AutonomousSystem4Capability;
import org.bgp4j.net.Capability;
import org.bgp4j.net.MultiProtocolCapability;
import org.bgp4j.net.ORFSendReceive;
import org.bgp4j.net.ORFType;
import org.bgp4j.net.OutboundRouteFilteringCapability;
import org.bgp4j.net.RouteRefreshCapability;
import org.bgp4j.net.SubsequentAddressFamily;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class CapabilitiesParserTest extends ConfigTestBase {

	@Before
	public void before() throws Exception {
		this.config = loadConfiguration("config/nodes/CapabilityConfig.xml");
		this.parser = obtainInstance(CapabilitiesParser.class);
	}
	
	@After
	public void after() {
		this.config = null;
		this.parser = null;
	}
	
	private XMLConfiguration config;
	private CapabilitiesParser parser;
		
	@Test
	public void testEmptyConfiguration() throws Exception {
		Capabilities caps = parser.parseConfig(config.configurationAt("Capabilities(0)"));
		
		Assert.assertEquals(0, caps.getCapabilities().size());
	}

	@Test
	public void testOneAS4Configuration() throws Exception {
		Capabilities caps = parser.parseConfig(config.configurationAt("Capabilities(1)"));
		Iterator<Capability> capIt = caps.getCapabilities().iterator();
		
		Assert.assertTrue(capIt.hasNext());
		AutonomousSystem4Capability cap = (AutonomousSystem4Capability)capIt.next();
		Assert.assertEquals(256, cap.getAutonomousSystem());
		Assert.assertFalse(capIt.hasNext());
	}

	@Test
	public void testTwoAS4Configuration() throws Exception {
		Capabilities caps = parser.parseConfig(config.configurationAt("Capabilities(2)"));
		Iterator<Capability> capIt = caps.getCapabilities().iterator();
		
		Assert.assertTrue(capIt.hasNext());
		AutonomousSystem4Capability cap = (AutonomousSystem4Capability)capIt.next();
		Assert.assertEquals(256, cap.getAutonomousSystem());

		Assert.assertFalse(capIt.hasNext());
	}

	@Test
	public void testOneRouteRefreshConfiguration() throws Exception {
		Capabilities caps = parser.parseConfig(config.configurationAt("Capabilities(3)"));
		Iterator<Capability> capIt = caps.getCapabilities().iterator();
		
		Assert.assertTrue(capIt.hasNext());
		Assert.assertEquals(RouteRefreshCapability.class, capIt.next().getClass());

		Assert.assertFalse(capIt.hasNext());
	}

	@Test
	public void testOneMultiProtoclConfiguration() throws Exception {
		Capabilities caps = parser.parseConfig(config.configurationAt("Capabilities(4)"));
		Iterator<Capability> capIt = caps.getCapabilities().iterator();
		
		Assert.assertTrue(capIt.hasNext());
		MultiProtocolCapability cap = (MultiProtocolCapability)capIt.next();
		Assert.assertEquals(AddressFamily.IPv4, cap.getAfi());
		Assert.assertEquals(SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, cap.getSafi());

		Assert.assertFalse(capIt.hasNext());
	}


	@Test
	public void testTwoMultiProtoclConfiguration() throws Exception {
		Capabilities caps = parser.parseConfig(config.configurationAt("Capabilities(5)"));
		Iterator<Capability> capIt = caps.getCapabilities().iterator();
		
		Assert.assertTrue(capIt.hasNext());
		MultiProtocolCapability cap = (MultiProtocolCapability)capIt.next();
		Assert.assertEquals(AddressFamily.IPv4, cap.getAfi());
		Assert.assertEquals(SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, cap.getSafi());

		Assert.assertTrue(capIt.hasNext());
		cap = (MultiProtocolCapability)capIt.next();
		Assert.assertEquals(AddressFamily.IPv6, cap.getAfi());
		Assert.assertEquals(SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, cap.getSafi());

		Assert.assertFalse(capIt.hasNext());
	}

	@Test
	public void testOneOutboundRouteFilteringConfiguration() throws Exception {
		Capabilities caps = parser.parseConfig(config.configurationAt("Capabilities(6)"));
		Iterator<Capability> capIt = caps.getCapabilities().iterator();
		
		Assert.assertTrue(capIt.hasNext());
		OutboundRouteFilteringCapability cap = (OutboundRouteFilteringCapability)capIt.next();
		Map<ORFType, ORFSendReceive> filters = cap.getFilters();
		Assert.assertEquals(AddressFamily.IPv4, cap.getAddressFamily());
		Assert.assertEquals(SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, cap.getSubsequentAddressFamily());
		Assert.assertEquals(1, filters.size());
		Assert.assertTrue(filters.containsKey(ORFType.ADDRESS_PREFIX_BASED));
		Assert.assertEquals(ORFSendReceive.BOTH, filters.get(ORFType.ADDRESS_PREFIX_BASED));
		
		Assert.assertFalse(capIt.hasNext());
	}


	@Test
	public void testTwoOutboundRouteFilteringConfiguration() throws Exception {
		Capabilities caps = parser.parseConfig(config.configurationAt("Capabilities(7)"));
		Iterator<Capability> capIt = caps.getCapabilities().iterator();
		
		Assert.assertTrue(capIt.hasNext());
		OutboundRouteFilteringCapability cap = (OutboundRouteFilteringCapability)capIt.next();
		Map<ORFType, ORFSendReceive> filters = cap.getFilters();
		Assert.assertEquals(AddressFamily.IPv4, cap.getAddressFamily());
		Assert.assertEquals(SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, cap.getSubsequentAddressFamily());
		Assert.assertEquals(1, filters.size());
		Assert.assertTrue(filters.containsKey(ORFType.ADDRESS_PREFIX_BASED));
		Assert.assertEquals(ORFSendReceive.BOTH, filters.get(ORFType.ADDRESS_PREFIX_BASED));
		
		cap = (OutboundRouteFilteringCapability)capIt.next();
		filters = cap.getFilters();
		Assert.assertEquals(AddressFamily.IPv6, cap.getAddressFamily());
		Assert.assertEquals(SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, cap.getSubsequentAddressFamily());
		Assert.assertEquals(1, filters.size());
		Assert.assertTrue(filters.containsKey(ORFType.ADDRESS_PREFIX_BASED));
		Assert.assertEquals(ORFSendReceive.BOTH, filters.get(ORFType.ADDRESS_PREFIX_BASED));
		
		Assert.assertFalse(capIt.hasNext());
	}

	@Test
	public void testOneAS4TwoMultiProtoclConfiguration() throws Exception {
		Capabilities caps = parser.parseConfig(config.configurationAt("Capabilities(8)"));
		Iterator<Capability> capIt = caps.getCapabilities().iterator();
		
		Assert.assertTrue(capIt.hasNext());
		AutonomousSystem4Capability as4cap = (AutonomousSystem4Capability)capIt.next();
		Assert.assertEquals(256, as4cap.getAutonomousSystem());

		Assert.assertTrue(capIt.hasNext());
		MultiProtocolCapability cap = (MultiProtocolCapability)capIt.next();
		Assert.assertEquals(AddressFamily.IPv4, cap.getAfi());
		Assert.assertEquals(SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, cap.getSafi());

		Assert.assertTrue(capIt.hasNext());
		cap = (MultiProtocolCapability)capIt.next();
		Assert.assertEquals(AddressFamily.IPv6, cap.getAfi());
		Assert.assertEquals(SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, cap.getSafi());

		Assert.assertFalse(capIt.hasNext());
	}

}