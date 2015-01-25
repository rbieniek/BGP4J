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
 * File: org.bgp4j.netty.handlers.UpdateAttributeCheckerTest.java 
 */
package org.bgp4j.netty.handlers;

import static org.fest.assertions.api.Assertions.assertThat;
import io.netty.channel.embedded.EmbeddedChannel;

import org.bgp4j.net.ASType;
import org.bgp4j.net.BGPv4Constants;
import org.bgp4j.net.capabilities.AutonomousSystem4Capability;
import org.bgp4j.net.events.open.BadBgpIdentifierNotificationEvent;
import org.bgp4j.net.events.open.BadPeerASNotificationEvent;
import org.bgp4j.net.packets.BGPv4Packet;
import org.bgp4j.net.packets.open.BadBgpIdentifierNotificationPacket;
import org.bgp4j.net.packets.open.BadPeerASNotificationPacket;
import org.bgp4j.net.packets.open.OpenPacket;
import org.bgp4j.netty.Attributes;
import org.bgp4j.netty.BGPv4TestBase;
import org.bgp4j.netty.MockPeerConnectionInformation;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class ValidateServerIdentifierTest extends BGPv4TestBase {

	private EmbeddedChannel channel;
	private UserEventInboundHandler eventHandler;

	@Before
	public void before() {
		eventHandler = new UserEventInboundHandler();		
		channel = new EmbeddedChannel(new ValidateServerIdentifier(), eventHandler);
		peerInfo = new MockPeerConnectionInformation();
		
		channel.attr(Attributes.peerInfoKey).set(peerInfo);
	}
	
	@After
	public void after() {
		channel.close();
		channel = null;
		peerInfo = null;
	}

	private MockPeerConnectionInformation peerInfo;
		
	@Test
	public void testPassOpenMessage() throws Exception {
		peerInfo.setAsTypeInUse(ASType.AS_NUMBER_2OCTETS);
		peerInfo.setLocalAS(64172);
		peerInfo.setRemoteAS(64172);
		peerInfo.setRemoteBgpIdentifier(12345);

		OpenPacket open = new OpenPacket();
		
		open.setAutonomousSystem(64172);
		open.setBgpIdentifier(12345);
		
		channel.writeInbound(open);
		
		Assert.assertEquals(0, channel.outboundMessages().size());
		Assert.assertEquals(1, channel.inboundMessages().size());
		Assert.assertEquals(0, eventHandler.events().size());
	
		OpenPacket consumed = safeDowncast((BGPv4Packet)channel.readInbound(), OpenPacket.class);

		Assert.assertEquals(12345, consumed.getBgpIdentifier());
		Assert.assertEquals(64172, consumed.getAutonomousSystem());
	}
	
	@Test
	public void testPassOpenAS4Message() throws Exception {
		peerInfo.setAsTypeInUse(ASType.AS_NUMBER_4OCTETS);
		peerInfo.setLocalAS(641720);
		peerInfo.setRemoteAS(641720);
		peerInfo.setRemoteBgpIdentifier(12345);

		OpenPacket open = new OpenPacket();
		
		open.setAutonomousSystem(BGPv4Constants.BGP_AS_TRANS);
		open.getCapabilities().add(new AutonomousSystem4Capability(641720));
		open.setBgpIdentifier(12345);
			
		channel.writeInbound(open);
		
		Assert.assertEquals(0, channel.outboundMessages().size());
		Assert.assertEquals(1, channel.inboundMessages().size());
		Assert.assertEquals(0, eventHandler.events().size());
	
		OpenPacket consumed = safeDowncast((BGPv4Packet)channel.readInbound(), OpenPacket.class);

		Assert.assertEquals(12345, consumed.getBgpIdentifier());
		Assert.assertEquals(BGPv4Constants.BGP_AS_TRANS, consumed.getAutonomousSystem());
		AutonomousSystem4Capability as4cap = consumed.findCapability(AutonomousSystem4Capability.class);
		
		Assert.assertNotNull(as4cap);
		Assert.assertEquals(641720, as4cap.getAutonomousSystem());
	}
	
	
	@Test
	public void testPassOpenAS4MessageWith2OctetsAS() throws Exception {
		peerInfo.setAsTypeInUse(ASType.AS_NUMBER_4OCTETS);
		peerInfo.setLocalAS(64172);
		peerInfo.setRemoteAS(64172);
		peerInfo.setRemoteBgpIdentifier(12345);

		OpenPacket open = new OpenPacket();
		
		open.setAutonomousSystem(64172);
		open.getCapabilities().add(new AutonomousSystem4Capability(64172));
		open.setBgpIdentifier(12345);
			
		channel.writeInbound(open);
		
		Assert.assertEquals(0, channel.outboundMessages().size());
		Assert.assertEquals(1, channel.inboundMessages().size());
		Assert.assertEquals(0, eventHandler.events().size());
	
		OpenPacket consumed = safeDowncast((BGPv4Packet)channel.readInbound(), OpenPacket.class);

		Assert.assertEquals(12345, consumed.getBgpIdentifier());
		Assert.assertEquals(64172, consumed.getAutonomousSystem());
		AutonomousSystem4Capability as4cap = consumed.findCapability(AutonomousSystem4Capability.class);
		
		Assert.assertNotNull(as4cap);
		Assert.assertEquals(64172, as4cap.getAutonomousSystem());
	}
	
	@Test
	public void testASNumberMismatchConnection() throws Exception {
		peerInfo.setAsTypeInUse(ASType.AS_NUMBER_2OCTETS);
		peerInfo.setLocalAS(64172);
		peerInfo.setRemoteAS(64172);
		peerInfo.setRemoteBgpIdentifier(12345);

		OpenPacket open = new OpenPacket();
		
		open.setAutonomousSystem(64173);
		open.setBgpIdentifier(12345);

		channel.writeInbound(open);
		
		Assert.assertEquals(1, channel.outboundMessages().size());
		Assert.assertEquals(0, channel.inboundMessages().size());
		Assert.assertEquals(1, eventHandler.events().size());
	
		assertThat(channel.readOutbound()).isInstanceOf(BadPeerASNotificationPacket.class);
		assertThat(eventHandler.events().get(0)).isInstanceOf(BadPeerASNotificationEvent.class);
	}
	
	@Test
	public void testBgpIdentifierMismatchConnection() throws Exception {
		peerInfo.setAsTypeInUse(ASType.AS_NUMBER_2OCTETS);
		peerInfo.setLocalAS(64172);
		peerInfo.setRemoteAS(64172);
		peerInfo.setRemoteBgpIdentifier(12345);

		OpenPacket open = new OpenPacket();
		
		open.setAutonomousSystem(64172);
		open.setBgpIdentifier(123456);

		channel.writeInbound(open);
		
		Assert.assertEquals(1, channel.outboundMessages().size());
		Assert.assertEquals(0, channel.inboundMessages().size());
		Assert.assertEquals(1, eventHandler.events().size());

		assertThat(channel.readOutbound()).isInstanceOf(BadBgpIdentifierNotificationPacket.class);
		assertThat(eventHandler.events().get(0)).isInstanceOf(BadBgpIdentifierNotificationEvent.class);
	}

	@Test
	public void testRejectOpenAS4MessageWith2OctetASNotMatching4OctetAS() throws Exception {
		peerInfo.setAsTypeInUse(ASType.AS_NUMBER_4OCTETS);
		peerInfo.setLocalAS(64172);
		peerInfo.setRemoteAS(64172);
		peerInfo.setRemoteBgpIdentifier(12345);

		OpenPacket open = new OpenPacket();
		
		open.setAutonomousSystem(64172);
		open.getCapabilities().add(new AutonomousSystem4Capability(64173));
		open.setBgpIdentifier(12345);
			
		channel.writeInbound(open);
		
		Assert.assertEquals(1, channel.outboundMessages().size());
		Assert.assertEquals(0, channel.inboundMessages().size());
		Assert.assertEquals(1, eventHandler.events().size());
	
		assertThat(channel.readOutbound()).isInstanceOf(BadPeerASNotificationPacket.class);
		assertThat(eventHandler.events().get(0)).isInstanceOf(BadPeerASNotificationEvent.class);
	}

	@Test
	public void testRejectOpenAS4MessageWith2OctetASNotASTrans() throws Exception {
		peerInfo.setAsTypeInUse(ASType.AS_NUMBER_4OCTETS);
		peerInfo.setLocalAS(641720);
		peerInfo.setRemoteAS(641720);
		peerInfo.setRemoteBgpIdentifier(12345);

		OpenPacket open = new OpenPacket();
		
		open.setAutonomousSystem(64172);
		open.getCapabilities().add(new AutonomousSystem4Capability(641720));
		open.setBgpIdentifier(12345);
			
		channel.writeInbound(open);
		
		Assert.assertEquals(1, channel.outboundMessages().size());
		Assert.assertEquals(0, channel.inboundMessages().size());
		Assert.assertEquals(1, eventHandler.events().size());
	
		assertThat(channel.readOutbound()).isInstanceOf(BadPeerASNotificationPacket.class);
		assertThat(eventHandler.events().get(0)).isInstanceOf(BadPeerASNotificationEvent.class);
	}
	

	@Test
	public void testRejectOpenAS4MessageWith4OctetASNotMatching() throws Exception {
		peerInfo.setAsTypeInUse(ASType.AS_NUMBER_4OCTETS);
		peerInfo.setLocalAS(641720);
		peerInfo.setRemoteAS(641720);
		peerInfo.setRemoteBgpIdentifier(12345);

		OpenPacket open = new OpenPacket();
		
		open.setAutonomousSystem(BGPv4Constants.BGP_AS_TRANS);
		open.getCapabilities().add(new AutonomousSystem4Capability(641721));
		open.setBgpIdentifier(12345);
			
		channel.writeInbound(open);
		
		Assert.assertEquals(1, channel.outboundMessages().size());
		Assert.assertEquals(0, channel.inboundMessages().size());
		Assert.assertEquals(1, eventHandler.events().size());
	
		assertThat(channel.readOutbound()).isInstanceOf(BadPeerASNotificationPacket.class);
		assertThat(eventHandler.events().get(0)).isInstanceOf(BadPeerASNotificationEvent.class);
	}
	
}
