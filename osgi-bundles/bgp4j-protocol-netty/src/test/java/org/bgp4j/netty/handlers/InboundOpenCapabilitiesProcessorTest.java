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
 * File: org.bgp4j.netty.handlers.InboundOpenCapabilitiesProcessorTest.java 
 */
package org.bgp4j.netty.handlers;

import static org.fest.assertions.api.Assertions.assertThat;
import io.netty.channel.embedded.EmbeddedChannel;

import org.bgp4j.net.AddressFamily;
import org.bgp4j.net.BGPv4Constants;
import org.bgp4j.net.SubsequentAddressFamily;
import org.bgp4j.net.capabilities.AutonomousSystem4Capability;
import org.bgp4j.net.capabilities.MultiProtocolCapability;
import org.bgp4j.net.events.open.BadPeerASNotificationEvent;
import org.bgp4j.net.packets.BGPv4Packet;
import org.bgp4j.net.packets.open.BadPeerASNotificationPacket;
import org.bgp4j.net.packets.open.OpenPacket;
import org.bgp4j.netty.BGPv4TestBase;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class InboundOpenCapabilitiesProcessorTest extends BGPv4TestBase {

	private EmbeddedChannel channel;
	private UserEventInboundHandler eventHandler;

	@Before
	public void before() {
		eventHandler = new UserEventInboundHandler();		
		channel = new EmbeddedChannel(new InboundOpenCapabilitiesProcessor(), eventHandler);
	}
	
	@After
	public void after() {
		channel.close();
		channel = null;
	}
	
	@Test
	public void testTwoOctetASNumberNoASCap() throws Exception {
		OpenPacket open = new OpenPacket();
		
		open.setAutonomousSystem(64172);
		
		channel.writeInbound(open);
		
		assertThat(channel.outboundMessages()).isEmpty();
		assertThat(channel.inboundMessages()).hasSize(1);
		assertThat(eventHandler.events()).isEmpty();
	
		OpenPacket consumed = safeDowncast((BGPv4Packet)channel.readInbound(), OpenPacket.class);

		Assert.assertEquals(64172, consumed.getAutonomousSystem());	
	}

	@Test
	public void testFourOctetASNumberAS4Cap() throws Exception {
		OpenPacket open = new OpenPacket();
		
		open.setAutonomousSystem(BGPv4Constants.BGP_AS_TRANS);
		open.getCapabilities().add(new AutonomousSystem4Capability(641723));
		
		channel.writeInbound(open);
		
		assertThat(channel.outboundMessages()).isEmpty();
		assertThat(channel.inboundMessages()).hasSize(1);
		assertThat(eventHandler.events()).isEmpty();
	
		OpenPacket consumed = safeDowncast((BGPv4Packet)channel.readInbound(), OpenPacket.class);

		Assert.assertEquals(BGPv4Constants.BGP_AS_TRANS, consumed.getAutonomousSystem());	
		
		AutonomousSystem4Capability as4cap = consumed.findCapability(AutonomousSystem4Capability.class);
		
		Assert.assertNotNull(as4cap);
		Assert.assertEquals(641723, as4cap.getAutonomousSystem());
	}
	
	@Test
	public void testTwoOctetASNumberMatchingASCap() throws Exception {
		OpenPacket open = new OpenPacket();
		
		open.setAutonomousSystem(64172);
		open.getCapabilities().add(new AutonomousSystem4Capability(64172));
		
		channel.writeInbound(open);
		
		assertThat(channel.outboundMessages()).isEmpty();
		assertThat(channel.inboundMessages()).hasSize(1);
		assertThat(eventHandler.events()).isEmpty();

		OpenPacket consumed = safeDowncast((BGPv4Packet)channel.readInbound(), OpenPacket.class);

		Assert.assertEquals(64172, consumed.getAutonomousSystem());
		
		AutonomousSystem4Capability as4cap = consumed.findCapability(AutonomousSystem4Capability.class);
		
		Assert.assertNotNull(as4cap);
		Assert.assertEquals(64172, as4cap.getAutonomousSystem());
	}
	
	@Test
	public void testTwoOctetASNumberMismatchingASCap() throws Exception {
		OpenPacket open = new OpenPacket();
		
		open.setAutonomousSystem(64172);
		open.getCapabilities().add(new AutonomousSystem4Capability(64173));
		
		channel.writeInbound(open);
		
		assertThat(channel.outboundMessages()).hasSize(1);
		assertThat(channel.inboundMessages()).isEmpty();
		assertThat(eventHandler.events()).hasSize(1);
	
		assertThat(channel.readOutbound()).isInstanceOf(BadPeerASNotificationPacket.class);
		assertThat(eventHandler.readEvent()).isInstanceOf(BadPeerASNotificationEvent.class);
	}

	@Test
	public void testTwoOctetASNumberMismatchingAS4Cap() throws Exception {
		OpenPacket open = new OpenPacket();
		
		open.setAutonomousSystem(64172);
		open.getCapabilities().add(new AutonomousSystem4Capability(641720));
		
		channel.writeInbound(open);
		
		assertThat(channel.outboundMessages()).hasSize(1);
		assertThat(channel.inboundMessages()).isEmpty();
		assertThat(eventHandler.events()).hasSize(1);
	
		assertThat(channel.readOutbound()).isInstanceOf(BadPeerASNotificationPacket.class);
		assertThat(eventHandler.readEvent()).isInstanceOf(BadPeerASNotificationEvent.class);
	}
	
	public void testIPv4MissingCapabilty() {
		OpenPacket open = new OpenPacket();
		
		open.setAutonomousSystem(64172);

		channel.writeInbound(open);
		
		assertThat(channel.outboundMessages()).isEmpty();
		assertThat(channel.inboundMessages()).hasSize(1);
	
		OpenPacket consumed = safeDowncast((BGPv4Packet)channel.readInbound(), OpenPacket.class);

		Assert.assertTrue(consumed.getCapabilities().contains(new MultiProtocolCapability(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING)));
	}
	
	public void testIPv4AvailableCapabilty() {
		OpenPacket open = new OpenPacket();
		
		open.setAutonomousSystem(64172);
		open.getCapabilities().add(new MultiProtocolCapability(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING));
		
		channel.writeInbound(open);
				
		assertThat(channel.outboundMessages()).isEmpty();
		assertThat(channel.inboundMessages()).hasSize(1);
	
		OpenPacket consumed = safeDowncast((BGPv4Packet)channel.readInbound(), OpenPacket.class);

		Assert.assertTrue(consumed.getCapabilities().contains(new MultiProtocolCapability(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING)));
	}

	
	public void testIPv4AnycastAvailableCapabilty() {
		OpenPacket open = new OpenPacket();
		
		open.setAutonomousSystem(64172);
		open.getCapabilities().add(new MultiProtocolCapability(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_MULTICAST_FORWARDING));
		
		channel.writeInbound(open);
				
		assertThat(channel.outboundMessages()).isEmpty();
		assertThat(channel.inboundMessages()).hasSize(1);
	
		OpenPacket consumed = safeDowncast((BGPv4Packet)channel.readInbound(), OpenPacket.class);

		Assert.assertTrue(consumed.getCapabilities().contains(new MultiProtocolCapability(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING)));
		Assert.assertTrue(consumed.getCapabilities().contains(new MultiProtocolCapability(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_MULTICAST_FORWARDING)));
	}
}
