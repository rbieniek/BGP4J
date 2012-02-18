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

import junit.framework.Assert;

import org.bgp4j.netty.ASType;
import org.bgp4j.netty.BGPv4Constants;
import org.bgp4j.netty.BGPv4TestBase;
import org.bgp4j.netty.MockChannel;
import org.bgp4j.netty.MockChannelHandler;
import org.bgp4j.netty.MockChannelSink;
import org.bgp4j.netty.MockPeerConnectionInformation;
import org.bgp4j.netty.PeerConnectionInformation;
import org.bgp4j.netty.protocol.open.BadBgpIdentifierNotificationPacket;
import org.bgp4j.netty.protocol.open.BadPeerASNotificationPacket;
import org.bgp4j.netty.protocol.open.OpenPacket;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.Channels;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class ValidateServerIdentifierTest extends BGPv4TestBase {

	@Before
	public void before() {
		ValidateServerIdentifier checker = obtainInstance(ValidateServerIdentifier.class);
		
		channelHandler = obtainInstance(MockChannelHandler.class);
		sink = obtainInstance(MockChannelSink.class);
		pipeline = Channels.pipeline(new ChannelHandler[] { 
				checker,
				channelHandler });
		channel = new MockChannel(pipeline, sink);
		
		peerInfo = new MockPeerConnectionInformation();

		// attach the context object to the channel handler
		channel.getPipeline().getContext(checker).setAttachment(peerInfo);
	}
	
	@After
	public void after() {
		channel = null;
		channelHandler = null;
		sink = null;
		pipeline = null;
		peerInfo = null;
	}

	private MockChannelHandler channelHandler;
	private MockChannelSink sink;
	private ChannelPipeline pipeline;
	private MockChannel channel;
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
		
		pipeline.sendUpstream(buildUpstreamBgpMessageEvent(channel, open));
		
		Assert.assertEquals(0, sink.getWaitingEventNumber());
		Assert.assertEquals(1, channelHandler.getWaitingEventNumber());
	
		OpenPacket consumed = safeDowncast(safeExtractChannelEvent(channelHandler.nextEvent()), OpenPacket.class);

		Assert.assertEquals(12345, consumed.getBgpIdentifier());
		Assert.assertEquals(64172, consumed.getEffectiveAutonomousSystem());
	}
	
	@Test
	public void testPassOpenAS4Message() throws Exception {
		peerInfo.setAsTypeInUse(ASType.AS_NUMBER_4OCTETS);
		peerInfo.setLocalAS(641720);
		peerInfo.setRemoteAS(641720);
		peerInfo.setRemoteBgpIdentifier(12345);

		OpenPacket open = new OpenPacket();
		
		open.setAutonomousSystem(BGPv4Constants.BGP_AS_TRANS);
		open.setAs4AutonomousSystem(641720);
		open.setBgpIdentifier(12345);
		
		pipeline.sendUpstream(buildUpstreamBgpMessageEvent(channel, open));
		
		Assert.assertEquals(0, sink.getWaitingEventNumber());
		Assert.assertEquals(1, channelHandler.getWaitingEventNumber());
	
		OpenPacket consumed = safeDowncast(safeExtractChannelEvent(channelHandler.nextEvent()), OpenPacket.class);

		Assert.assertEquals(12345, consumed.getBgpIdentifier());
		Assert.assertEquals(641720, consumed.getEffectiveAutonomousSystem());
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

		pipeline.sendUpstream(buildUpstreamBgpMessageEvent(channel, open));
		
		Assert.assertEquals(1, sink.getWaitingEventNumber());
		Assert.assertEquals(0, channelHandler.getWaitingEventNumber());
	
		Assert.assertEquals(BadPeerASNotificationPacket.class, safeExtractChannelEvent(sink.getEvents().remove(0)).getClass());
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

		pipeline.sendUpstream(buildUpstreamBgpMessageEvent(channel, open));
		
		Assert.assertEquals(1, sink.getWaitingEventNumber());
		Assert.assertEquals(0, channelHandler.getWaitingEventNumber());
	
		Assert.assertEquals(BadBgpIdentifierNotificationPacket.class, safeExtractChannelEvent(sink.getEvents().remove(0)).getClass());
	}
}
