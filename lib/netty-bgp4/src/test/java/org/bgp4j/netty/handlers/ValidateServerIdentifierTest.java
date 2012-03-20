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

import java.util.UUID;

import junit.framework.Assert;

import org.bgp4j.net.ASType;
import org.bgp4j.net.AutonomousSystem4Capability;
import org.bgp4j.netty.BGPv4Constants;
import org.bgp4j.netty.LocalChannelBGPv4TestBase;
import org.bgp4j.netty.MessageRecordingChannelHandler;
import org.bgp4j.netty.MockPeerConnectionInformation;
import org.bgp4j.netty.protocol.open.BadBgpIdentifierNotificationPacket;
import org.bgp4j.netty.protocol.open.BadPeerASNotificationPacket;
import org.bgp4j.netty.protocol.open.OpenPacket;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.local.LocalAddress;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class ValidateServerIdentifierTest extends LocalChannelBGPv4TestBase {

	@Before
	public void before() {
		peerInfo = new MockPeerConnectionInformation();

		messageRecorder = obtainInstance(MessageRecordingChannelHandler.class);
		messageRecorder.setPeerInfo(peerInfo);
		
		LocalAddress codecOnlyAddress = new LocalAddress(UUID.randomUUID().toString());
		
		serverBootstrap = buildLocalServerBootstrap(new ChannelPipelineFactory() {
			
			@Override
			public ChannelPipeline getPipeline() throws Exception {
				return Channels.pipeline(new ChannelHandler[] { 
						obtainInstance(ValidateServerIdentifier.class), 
						messageRecorder });
			}
		});
		serverChannel = serverBootstrap.bind(codecOnlyAddress);

		clientBootstrap = buildLocalClientBootstrap(Channels.pipeline(new ChannelHandler[] {
				messageRecorder 
				}));
		clientChannel = clientBootstrap.connect(codecOnlyAddress).getChannel();		
	}
	
	@After
	public void after() {
		if(clientChannel != null)
			clientChannel.close();
		if(serverChannel != null)
			serverChannel.close();
		clientChannel = null;
		clientBootstrap.releaseExternalResources();
		clientBootstrap = null;
		serverBootstrap.releaseExternalResources();
		serverBootstrap = null;

		peerInfo = null;
	}

	private MessageRecordingChannelHandler messageRecorder;
	private MockPeerConnectionInformation peerInfo;
	
	private ServerBootstrap serverBootstrap;
	private ClientBootstrap clientBootstrap;
	private Channel clientChannel;
	private Channel serverChannel;

	
	@Test
	public void testPassOpenMessage() throws Exception {
		peerInfo.setAsTypeInUse(ASType.AS_NUMBER_2OCTETS);
		peerInfo.setLocalAS(64172);
		peerInfo.setRemoteAS(64172);
		peerInfo.setRemoteBgpIdentifier(12345);

		OpenPacket open = new OpenPacket();
		
		open.setAutonomousSystem(64172);
		open.setBgpIdentifier(12345);
		
		clientChannel.write(open);
		
		Assert.assertEquals(0, messageRecorder.getWaitingEventNumber(clientChannel));
		Assert.assertEquals(1, messageRecorder.getWaitingEventNumber(serverChannel));
	
		OpenPacket consumed = safeDowncast(safeExtractChannelEvent(messageRecorder.nextEvent(serverChannel)), OpenPacket.class);

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
			
		clientChannel.write(open);
		
		Assert.assertEquals(0, messageRecorder.getWaitingEventNumber(clientChannel));
		Assert.assertEquals(1, messageRecorder.getWaitingEventNumber(serverChannel));
	
		OpenPacket consumed = safeDowncast(safeExtractChannelEvent(messageRecorder.nextEvent(serverChannel)), OpenPacket.class);

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
			
		clientChannel.write(open);
		
		Assert.assertEquals(0, messageRecorder.getWaitingEventNumber(clientChannel));
		Assert.assertEquals(1, messageRecorder.getWaitingEventNumber(serverChannel));
	
		OpenPacket consumed = safeDowncast(safeExtractChannelEvent(messageRecorder.nextEvent(serverChannel)), OpenPacket.class);

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

		clientChannel.write(open);
		
		Assert.assertEquals(1, messageRecorder.getWaitingEventNumber(clientChannel));
		Assert.assertEquals(1, messageRecorder.getWaitingEventNumber(serverChannel));
	
		Assert.assertEquals(BadPeerASNotificationPacket.class, safeExtractChannelEvent(messageRecorder.nextEvent(clientChannel)).getClass());
		assertNotificationEvent(BadPeerASNotificationPacket.class, messageRecorder.nextEvent(serverChannel));
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

		clientChannel.write(open);
		
		Assert.assertEquals(1, messageRecorder.getWaitingEventNumber(clientChannel));
		Assert.assertEquals(1, messageRecorder.getWaitingEventNumber(serverChannel));
	
		Assert.assertEquals(BadBgpIdentifierNotificationPacket.class, safeExtractChannelEvent(messageRecorder.nextEvent(clientChannel)).getClass());
		assertNotificationEvent(BadBgpIdentifierNotificationPacket.class, messageRecorder.nextEvent(serverChannel));
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
			
		clientChannel.write(open);
		
		Assert.assertEquals(1, messageRecorder.getWaitingEventNumber(clientChannel));
		Assert.assertEquals(1, messageRecorder.getWaitingEventNumber(serverChannel));
	
		Assert.assertEquals(BadPeerASNotificationPacket.class, safeExtractChannelEvent(messageRecorder.nextEvent(clientChannel)).getClass());
		assertNotificationEvent(BadPeerASNotificationPacket.class, messageRecorder.nextEvent(serverChannel));
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
			
		clientChannel.write(open);
		
		Assert.assertEquals(1, messageRecorder.getWaitingEventNumber(clientChannel));
		Assert.assertEquals(1, messageRecorder.getWaitingEventNumber(serverChannel));
	
		Assert.assertEquals(BadPeerASNotificationPacket.class, safeExtractChannelEvent(messageRecorder.nextEvent(clientChannel)).getClass());
		assertNotificationEvent(BadPeerASNotificationPacket.class, messageRecorder.nextEvent(serverChannel));
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
			
		clientChannel.write(open);
		
		Assert.assertEquals(1, messageRecorder.getWaitingEventNumber(clientChannel));
		Assert.assertEquals(1, messageRecorder.getWaitingEventNumber(serverChannel));
	
		Assert.assertEquals(BadPeerASNotificationPacket.class, safeExtractChannelEvent(messageRecorder.nextEvent(clientChannel)).getClass());
		assertNotificationEvent(BadPeerASNotificationPacket.class, messageRecorder.nextEvent(serverChannel));
	}
	
}
