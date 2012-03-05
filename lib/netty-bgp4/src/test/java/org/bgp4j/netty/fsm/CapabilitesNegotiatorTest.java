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
 * File: org.bgp4j.netty.fsm.CapabilitesNegotiatorTest.java 
 */
package org.bgp4j.netty.fsm;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import junit.framework.Assert;

import org.apache.commons.configuration.XMLConfiguration;
import org.bgp4.config.Configuration;
import org.bgp4.config.ConfigurationParser;
import org.bgp4.config.nodes.PeerConfiguration;
import org.bgp4j.net.AddressFamily;
import org.bgp4j.net.AutonomousSystem4Capability;
import org.bgp4j.net.Capability;
import org.bgp4j.net.MultiProtocolCapability;
import org.bgp4j.net.SubsequentAddressFamily;
import org.bgp4j.netty.protocol.open.CapabilityListUnsupportedCapabilityNotificationPacket;
import org.bgp4j.netty.protocol.open.OpenPacket;
import org.bgp4j.weld.WeldTestCaseBase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class CapabilitesNegotiatorTest  extends WeldTestCaseBase {

	@Before
	public void before() {
		parser = obtainInstance(ConfigurationParser.class);
		negotiator = obtainInstance(CapabilitesNegotiator.class);
	}
	
	@After
	public void after() {
		negotiator = null;
		parser = null;
	}
	
	private ConfigurationParser parser;
	private CapabilitesNegotiator negotiator;

	@Test
	public void testCreateOpenWithoutCapabilities() throws Exception {
		PeerConfiguration peerConfig = loadConfiguration("org/bgp4j/netty/fsm/Config-With-BgpPeers-With-Capabilities.xml").getPeer("peer1");
		
		negotiator.setup(peerConfig);
		
		OpenPacket open = new OpenPacket();
		open.setAutonomousSystem(65280);
		
		negotiator.insertNegotiatedCapabilities(open);
		
		Assert.assertEquals(0, open.getCapabilities().size());
	}
	
	@Test
	public void testCreateOpenWithOneCapability() throws Exception {
		PeerConfiguration peerConfig = loadConfiguration("org/bgp4j/netty/fsm/Config-With-BgpPeers-With-Capabilities.xml").getPeer("peer2");
		
		negotiator.setup(peerConfig);
		
		OpenPacket open = new OpenPacket();
		open.setAutonomousSystem(65280);
		
		negotiator.insertNegotiatedCapabilities(open);
		
		Assert.assertEquals(1, open.getCapabilities().size());
		
		Iterator<Capability> capIt = open.getCapabilities().iterator();
		
		Assert.assertTrue(capIt.hasNext());
		AutonomousSystem4Capability as4Cap = (AutonomousSystem4Capability)capIt.next();
		Assert.assertEquals(65280, as4Cap.getAutonomousSystem());
		
		Assert.assertFalse(capIt.hasNext());
	}
	
	@Test
	public void testCreateOpenWithThreeCapability() throws Exception {
		PeerConfiguration peerConfig = loadConfiguration("org/bgp4j/netty/fsm/Config-With-BgpPeers-With-Capabilities.xml").getPeer("peer3");
		
		negotiator.setup(peerConfig);
		
		OpenPacket open = new OpenPacket();
		open.setAutonomousSystem(65280);
		
		negotiator.insertNegotiatedCapabilities(open);
		
		Assert.assertEquals(3, open.getCapabilities().size());
		
		Iterator<Capability> capIt = open.getCapabilities().iterator();
		
		Assert.assertTrue(capIt.hasNext());
		AutonomousSystem4Capability as4Cap = (AutonomousSystem4Capability)capIt.next();
		Assert.assertEquals(65280, as4Cap.getAutonomousSystem());
		
		Assert.assertTrue(capIt.hasNext());
		MultiProtocolCapability mpCap = (MultiProtocolCapability)capIt.next();
		Assert.assertEquals(AddressFamily.IPv4, mpCap.getAfi());
		Assert.assertEquals(SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, mpCap.getSafi());

		Assert.assertTrue(capIt.hasNext());
		mpCap = (MultiProtocolCapability)capIt.next();
		Assert.assertEquals(AddressFamily.IPv6, mpCap.getAfi());
		Assert.assertEquals(SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, mpCap.getSafi());

		Assert.assertFalse(capIt.hasNext());
	}

	@Test
	public void testReceivedOpenWithoutCapabilities() throws Exception {
		PeerConfiguration peerConfig = loadConfiguration("org/bgp4j/netty/fsm/Config-With-BgpPeers-With-Capabilities.xml").getPeer("peer1");
		
		negotiator.setup(peerConfig);
		
		OpenPacket open = new OpenPacket();
		open.setAutonomousSystem(65280);
		open.getCapabilities().add(new AutonomousSystem4Capability(65280));
		open.getCapabilities().add(new MultiProtocolCapability(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING));
		open.getCapabilities().add(new MultiProtocolCapability(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING));

		Iterator<Capability> capIt = negotiator.checkOpenForUnsupportedCapabilities(open).iterator();
		
		Assert.assertTrue(capIt.hasNext());
		AutonomousSystem4Capability as4Cap = (AutonomousSystem4Capability)capIt.next();
		Assert.assertEquals(65280, as4Cap.getAutonomousSystem());
		
		Assert.assertTrue(capIt.hasNext());
		MultiProtocolCapability mpCap = (MultiProtocolCapability)capIt.next();
		Assert.assertEquals(AddressFamily.IPv4, mpCap.getAfi());
		Assert.assertEquals(SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, mpCap.getSafi());

		Assert.assertTrue(capIt.hasNext());
		mpCap = (MultiProtocolCapability)capIt.next();
		Assert.assertEquals(AddressFamily.IPv6, mpCap.getAfi());
		Assert.assertEquals(SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, mpCap.getSafi());

		Assert.assertFalse(capIt.hasNext());
		
	}
	
	@Test
	public void testReceivedOpenWithOneCapability() throws Exception {
		PeerConfiguration peerConfig = loadConfiguration("org/bgp4j/netty/fsm/Config-With-BgpPeers-With-Capabilities.xml").getPeer("peer2");
		
		negotiator.setup(peerConfig);
		
		OpenPacket open = new OpenPacket();
		open.setAutonomousSystem(65280);
		open.getCapabilities().add(new AutonomousSystem4Capability(65280));
		open.getCapabilities().add(new MultiProtocolCapability(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING));
		open.getCapabilities().add(new MultiProtocolCapability(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING));

		Iterator<Capability> capIt = negotiator.checkOpenForUnsupportedCapabilities(open).iterator();
		
		Assert.assertTrue(capIt.hasNext());
		MultiProtocolCapability mpCap = (MultiProtocolCapability)capIt.next();
		Assert.assertEquals(AddressFamily.IPv4, mpCap.getAfi());
		Assert.assertEquals(SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, mpCap.getSafi());

		Assert.assertTrue(capIt.hasNext());
		mpCap = (MultiProtocolCapability)capIt.next();
		Assert.assertEquals(AddressFamily.IPv6, mpCap.getAfi());
		Assert.assertEquals(SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, mpCap.getSafi());

		Assert.assertFalse(capIt.hasNext());
		
	}

	@Test
	public void testReceivedOpenWithThreeCapability() throws Exception {
		PeerConfiguration peerConfig = loadConfiguration("org/bgp4j/netty/fsm/Config-With-BgpPeers-With-Capabilities.xml").getPeer("peer3");
		
		negotiator.setup(peerConfig);
		
		OpenPacket open = new OpenPacket();
		open.setAutonomousSystem(65280);
		open.getCapabilities().add(new AutonomousSystem4Capability(65280));
		open.getCapabilities().add(new MultiProtocolCapability(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING));
		open.getCapabilities().add(new MultiProtocolCapability(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING));

		Iterator<Capability> capIt = negotiator.checkOpenForUnsupportedCapabilities(open).iterator();
		
		Assert.assertFalse(capIt.hasNext());
		
	}

	@Test
	public void testCreateOpenWithThreeCapabilityRejectAutonomousSystem4() throws Exception {
		PeerConfiguration peerConfig = loadConfiguration("org/bgp4j/netty/fsm/Config-With-BgpPeers-With-Capabilities.xml").getPeer("peer3");
		List<Capability> rejected = new LinkedList<Capability>();
		AutonomousSystem4Capability as4Cap;
		MultiProtocolCapability mpCap;
		
		negotiator.setup(peerConfig);
		
		OpenPacket open = new OpenPacket();
		open.setAutonomousSystem(65280);
		
		negotiator.insertNegotiatedCapabilities(open);
		
		Assert.assertEquals(3, open.getCapabilities().size());
		
		Iterator<Capability> capIt = open.getCapabilities().iterator();
		
		Assert.assertTrue(capIt.hasNext());
		as4Cap = (AutonomousSystem4Capability)capIt.next();
		Assert.assertEquals(65280, as4Cap.getAutonomousSystem());
		rejected.add(as4Cap);
		
		Assert.assertTrue(capIt.hasNext());
		mpCap = (MultiProtocolCapability)capIt.next();
		Assert.assertEquals(AddressFamily.IPv4, mpCap.getAfi());
		Assert.assertEquals(SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, mpCap.getSafi());

		Assert.assertTrue(capIt.hasNext());
		mpCap = (MultiProtocolCapability)capIt.next();
		Assert.assertEquals(AddressFamily.IPv6, mpCap.getAfi());
		Assert.assertEquals(SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, mpCap.getSafi());

		Assert.assertFalse(capIt.hasNext());
		
		negotiator.handleUnsupportedPeerCapability(new CapabilityListUnsupportedCapabilityNotificationPacket(rejected));

		negotiator.insertNegotiatedCapabilities(open);
		
		capIt = open.getCapabilities().iterator();
				
		Assert.assertTrue(capIt.hasNext());
		mpCap = (MultiProtocolCapability)capIt.next();
		Assert.assertEquals(AddressFamily.IPv4, mpCap.getAfi());
		Assert.assertEquals(SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, mpCap.getSafi());

		Assert.assertTrue(capIt.hasNext());
		mpCap = (MultiProtocolCapability)capIt.next();
		Assert.assertEquals(AddressFamily.IPv6, mpCap.getAfi());
		Assert.assertEquals(SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, mpCap.getSafi());

		Assert.assertFalse(capIt.hasNext());
	}
	
	@Test
	public void testCreateOpenWithThreeCapabilityRejectIPv4Multiprotocol() throws Exception {
		PeerConfiguration peerConfig = loadConfiguration("org/bgp4j/netty/fsm/Config-With-BgpPeers-With-Capabilities.xml").getPeer("peer3");
		List<Capability> rejected = new LinkedList<Capability>();
		AutonomousSystem4Capability as4Cap;
		MultiProtocolCapability mpCap;
		
		negotiator.setup(peerConfig);
		
		OpenPacket open = new OpenPacket();
		open.setAutonomousSystem(65280);
		
		negotiator.insertNegotiatedCapabilities(open);
		
		Assert.assertEquals(3, open.getCapabilities().size());
		
		Iterator<Capability> capIt = open.getCapabilities().iterator();
		
		Assert.assertTrue(capIt.hasNext());
		as4Cap = (AutonomousSystem4Capability)capIt.next();
		Assert.assertEquals(65280, as4Cap.getAutonomousSystem());
		
		Assert.assertTrue(capIt.hasNext());
		mpCap = (MultiProtocolCapability)capIt.next();
		Assert.assertEquals(AddressFamily.IPv4, mpCap.getAfi());
		Assert.assertEquals(SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, mpCap.getSafi());
		rejected.add(mpCap);
		
		Assert.assertTrue(capIt.hasNext());
		mpCap = (MultiProtocolCapability)capIt.next();
		Assert.assertEquals(AddressFamily.IPv6, mpCap.getAfi());
		Assert.assertEquals(SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, mpCap.getSafi());

		Assert.assertFalse(capIt.hasNext());
		
		negotiator.handleUnsupportedPeerCapability(new CapabilityListUnsupportedCapabilityNotificationPacket(rejected));

		negotiator.insertNegotiatedCapabilities(open);
		
		capIt = open.getCapabilities().iterator();
				
		Assert.assertTrue(capIt.hasNext());
		as4Cap = (AutonomousSystem4Capability)capIt.next();
		Assert.assertEquals(65280, as4Cap.getAutonomousSystem());

		Assert.assertTrue(capIt.hasNext());
		mpCap = (MultiProtocolCapability)capIt.next();
		Assert.assertEquals(AddressFamily.IPv6, mpCap.getAfi());
		Assert.assertEquals(SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, mpCap.getSafi());

		Assert.assertFalse(capIt.hasNext());
	}
	
	
	@Test
	public void testCreateOpenWithThreeCapabilityRejectIPv4MultiprotocolIPv6Multiprotocol() throws Exception {
		PeerConfiguration peerConfig = loadConfiguration("org/bgp4j/netty/fsm/Config-With-BgpPeers-With-Capabilities.xml").getPeer("peer3");
		List<Capability> rejected = new LinkedList<Capability>();
		AutonomousSystem4Capability as4Cap;
		MultiProtocolCapability mpCap;
		
		negotiator.setup(peerConfig);
		
		OpenPacket open = new OpenPacket();
		open.setAutonomousSystem(65280);
		
		negotiator.insertNegotiatedCapabilities(open);
		
		Assert.assertEquals(3, open.getCapabilities().size());
		
		Iterator<Capability> capIt = open.getCapabilities().iterator();
		
		Assert.assertTrue(capIt.hasNext());
		as4Cap = (AutonomousSystem4Capability)capIt.next();
		Assert.assertEquals(65280, as4Cap.getAutonomousSystem());
		
		Assert.assertTrue(capIt.hasNext());
		mpCap = (MultiProtocolCapability)capIt.next();
		Assert.assertEquals(AddressFamily.IPv4, mpCap.getAfi());
		Assert.assertEquals(SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, mpCap.getSafi());
		rejected.add(mpCap);
		
		Assert.assertTrue(capIt.hasNext());
		mpCap = (MultiProtocolCapability)capIt.next();
		Assert.assertEquals(AddressFamily.IPv6, mpCap.getAfi());
		Assert.assertEquals(SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, mpCap.getSafi());
		rejected.add(mpCap);

		Assert.assertFalse(capIt.hasNext());
		
		negotiator.handleUnsupportedPeerCapability(new CapabilityListUnsupportedCapabilityNotificationPacket(rejected));

		negotiator.insertNegotiatedCapabilities(open);
		
		capIt = open.getCapabilities().iterator();
				
		Assert.assertTrue(capIt.hasNext());
		as4Cap = (AutonomousSystem4Capability)capIt.next();
		Assert.assertEquals(65280, as4Cap.getAutonomousSystem());

		Assert.assertFalse(capIt.hasNext());
	}

	// -- end of test messages
	private Configuration loadConfiguration(String fileName) throws Exception {
		return parser.parseConfiguration(new XMLConfiguration(fileName));
	}

}
