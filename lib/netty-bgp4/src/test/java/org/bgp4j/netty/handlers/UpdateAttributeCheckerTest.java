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

import java.net.Inet4Address;

import junit.framework.Assert;

import org.bgp4j.netty.ASType;
import org.bgp4j.netty.BGPv4TestBase;
import org.bgp4j.netty.MockChannel;
import org.bgp4j.netty.MockChannelHandler;
import org.bgp4j.netty.MockChannelSink;
import org.bgp4j.netty.PeerConnectionInformation;
import org.bgp4j.netty.protocol.update.ASPathAttribute;
import org.bgp4j.netty.protocol.update.LocalPrefPathAttribute;
import org.bgp4j.netty.protocol.update.NextHopPathAttribute;
import org.bgp4j.netty.protocol.update.OriginPathAttribute;
import org.bgp4j.netty.protocol.update.OriginPathAttribute.Origin;
import org.bgp4j.netty.protocol.update.UpdatePacket;
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
public class UpdateAttributeCheckerTest extends BGPv4TestBase {

	@Before
	public void before() {
		UpdateAttributeChecker checker = obtainInstance(UpdateAttributeChecker.class);
		
		channelHandler = obtainInstance(MockChannelHandler.class);
		sink = obtainInstance(MockChannelSink.class);
		pipeline = Channels.pipeline(new ChannelHandler[] { 
				checker,
				channelHandler });
		channel = new MockChannel(pipeline, sink);
		
		peerInfo = new PeerConnectionInformation();

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
	private PeerConnectionInformation peerInfo;
	
	@Test
	public void testPassAllRequiredAttributes2OctetsASIBGPConnection() throws Exception {
		peerInfo.setAsType(ASType.AS_NUMBER_2OCTETS);
		peerInfo.setLocalAS(64172);
		peerInfo.setRemoteAS(64172);
		
		UpdatePacket update = new UpdatePacket();
		
		update.getPathAttributes().add(new OriginPathAttribute(Origin.INCOMPLETE));
		update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_2OCTETS));
		update.getPathAttributes().add(new NextHopPathAttribute((Inet4Address)Inet4Address.getByAddress(new byte[] { (byte)0xc0, (byte)0xa8, 0x4, 0x1 })));
		update.getPathAttributes().add(new LocalPrefPathAttribute(100));
		
		pipeline.sendUpstream(buildUpstreamBgpMessageEvent(channel, update));
		
		Assert.assertEquals(0, sink.getWaitingEventNumber());
		Assert.assertEquals(1, channelHandler.getWaitingEventNumber());
	
		UpdatePacket consumed = safeDowncast(safeExtractChannelEvent(channelHandler.nextEvent()), UpdatePacket.class);

		Assert.assertEquals(4, consumed.getPathAttributes().size());
	}
	
	@Test
	public void testPassAllRequiredAttributes4OctetsASIBGPConnection() throws Exception {
		peerInfo.setAsType(ASType.AS_NUMBER_4OCTETS);
		peerInfo.setLocalAS(64172);
		peerInfo.setRemoteAS(64172);
		
		UpdatePacket update = new UpdatePacket();
		
		update.getPathAttributes().add(new OriginPathAttribute(Origin.INCOMPLETE));
		update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_4OCTETS));
		update.getPathAttributes().add(new NextHopPathAttribute((Inet4Address)Inet4Address.getByAddress(new byte[] { (byte)0xc0, (byte)0xa8, 0x4, 0x1 })));
		update.getPathAttributes().add(new LocalPrefPathAttribute(100));
		
		pipeline.sendUpstream(buildUpstreamBgpMessageEvent(channel, update));
		
		Assert.assertEquals(0, sink.getWaitingEventNumber());
		Assert.assertEquals(1, channelHandler.getWaitingEventNumber());
	
		UpdatePacket consumed = safeDowncast(safeExtractChannelEvent(channelHandler.nextEvent()), UpdatePacket.class);

		Assert.assertEquals(4, consumed.getPathAttributes().size());
	}

	
	@Test
	public void testPassAllRequiredAttributes2OctetsASEBGPConnection() throws Exception {
		peerInfo.setAsType(ASType.AS_NUMBER_2OCTETS);
		peerInfo.setLocalAS(64172);
		peerInfo.setRemoteAS(64173);
		
		UpdatePacket update = new UpdatePacket();
		
		update.getPathAttributes().add(new OriginPathAttribute(Origin.INCOMPLETE));
		update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_2OCTETS));
		update.getPathAttributes().add(new NextHopPathAttribute((Inet4Address)Inet4Address.getByAddress(new byte[] { (byte)0xc0, (byte)0xa8, 0x4, 0x1 })));
		
		pipeline.sendUpstream(buildUpstreamBgpMessageEvent(channel, update));
		
		Assert.assertEquals(0, sink.getWaitingEventNumber());
		Assert.assertEquals(1, channelHandler.getWaitingEventNumber());
	
		UpdatePacket consumed = safeDowncast(safeExtractChannelEvent(channelHandler.nextEvent()), UpdatePacket.class);

		Assert.assertEquals(3, consumed.getPathAttributes().size());
	}
	
	@Test
	public void testPassAllRequiredAttributes4OctetsASEBGPConnection() throws Exception {
		peerInfo.setAsType(ASType.AS_NUMBER_4OCTETS);
		peerInfo.setLocalAS(64172);
		peerInfo.setRemoteAS(64173);
		
		UpdatePacket update = new UpdatePacket();
		
		update.getPathAttributes().add(new OriginPathAttribute(Origin.INCOMPLETE));
		update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_4OCTETS));
		update.getPathAttributes().add(new NextHopPathAttribute((Inet4Address)Inet4Address.getByAddress(new byte[] { (byte)0xc0, (byte)0xa8, 0x4, 0x1 })));
		
		pipeline.sendUpstream(buildUpstreamBgpMessageEvent(channel, update));
		
		Assert.assertEquals(0, sink.getWaitingEventNumber());
		Assert.assertEquals(1, channelHandler.getWaitingEventNumber());
	
		UpdatePacket consumed = safeDowncast(safeExtractChannelEvent(channelHandler.nextEvent()), UpdatePacket.class);

		Assert.assertEquals(3, consumed.getPathAttributes().size());
	}
}
