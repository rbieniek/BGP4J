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
 * File: org.bgp4j.netty.protocol.BGPv4CodecTest.java 
 */
package org.bgp4j.netty.protocol;

import junit.framework.Assert;

import org.bgp4j.netty.protocol.open.OpenPacket;
import org.bgp4j.weld.WeldTestCaseBase;
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
public class BGPv4CodecTest extends ProtocolPacketTestBase {
	@Before
	public void before() {
		codecOnlyHandler = obtainInstance(MockChannelHandler.class);
		codecOnlySink = obtainInstance(MockChannelSink.class);;
		codecOnlyPipeline = Channels.pipeline(new ChannelHandler[] { obtainInstance(BGPv4Codec.class), codecOnlyHandler });
		codecOnlyChannel = new MockChannel(codecOnlyPipeline, codecOnlySink);		

		completeHandler = obtainInstance(MockChannelHandler.class);
		completeSink = obtainInstance(MockChannelSink.class);
		completePipeline = Channels.pipeline(new ChannelHandler[] { 
				obtainInstance(BGPv4Reframer.class), 
				obtainInstance(BGPv4Codec.class), 
				completeHandler });
		completeChannel = new MockChannel(completePipeline, completeSink);		
		
		Assert.assertNotSame(codecOnlyHandler, completeHandler);
		Assert.assertNotSame(codecOnlySink, completeSink);
}
	
	@After
	public void after() {
		codecOnlyHandler = null;
		codecOnlySink = null;
		codecOnlyPipeline = null;
		codecOnlyChannel = null;
	}

	// channel setup with only the codec in the chain
	private MockChannelHandler codecOnlyHandler;
	private MockChannelSink codecOnlySink;
	private ChannelPipeline codecOnlyPipeline;
	private MockChannel codecOnlyChannel;
	
	// channel setup with reframer and codec in the chain to test complete BGP protocol packets (header + payload)
	private MockChannelHandler completeHandler;
	private MockChannelSink completeSink;
	private ChannelPipeline completePipeline;
	private MockChannel completeChannel;

	@Test
	public void testStrippedbasicOpenPacket() throws Exception {
		codecOnlyPipeline.sendUpstream(buildProtocolPacketUpstreamMessageEvent(codecOnlyChannel, new byte[] {
				(byte)0x01, // type code OPEN
				(byte)0x04, // BGP version 4 
				(byte)0xfc, (byte)0x00, // Autonomous system 64512 
				(byte)0x00, (byte)0xb4, // hold time 180 seconds
				(byte)0xc0, (byte)0xa8, (byte)0x09, (byte)0x01, /// BGP identifier 192.168.9.1 
				(byte)0x0, // optional parameter length 0 
		}));

		Assert.assertEquals(0, codecOnlySink.getWaitingEventNumber());
		Assert.assertEquals(1, codecOnlyHandler.getWaitingEventNumber());
	
		OpenPacket open = safeDowncast(safeExtractChannelEvent(codecOnlyHandler.nextEvent()), OpenPacket.class);
		
		Assert.assertEquals(4, open.getProtocolVersion());
		Assert.assertEquals(64512, open.getAutonomousSystem());
		Assert.assertEquals(180, open.getHoldTime());
		Assert.assertEquals(0, open.getCapabilities().size());
		Assert.assertEquals(((192<<24) | (168 << 16) | (9 << 8) | 1), open.getBgpIdentifier());
	}

	@Test
	public void testCompleteBasicOpenPacket() throws Exception {
		completePipeline.sendUpstream(buildProtocolPacketUpstreamMessageEvent(completeChannel, new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				0x00, 0x1d, // length 29 octets
				(byte)0x01, // type code OPEN
				(byte)0x04, // BGP version 4 
				(byte)0xfc, (byte)0x00, // Autonomous system 64512 
				(byte)0x00, (byte)0xb4, // hold time 180 seconds
				(byte)0xc0, (byte)0xa8, (byte)0x09, (byte)0x01, /// BGP identifier 192.168.9.1 
				(byte)0x0, // optional parameter length 0 
		}));

		Assert.assertEquals(0, completeSink.getWaitingEventNumber());
		Assert.assertEquals(1, completeHandler.getWaitingEventNumber());
	
		OpenPacket open = safeDowncast(safeExtractChannelEvent(completeHandler.nextEvent()), OpenPacket.class);
		
		Assert.assertEquals(4, open.getProtocolVersion());
		Assert.assertEquals(64512, open.getAutonomousSystem());
		Assert.assertEquals(180, open.getHoldTime());
		Assert.assertEquals(0, open.getCapabilities().size());
		Assert.assertEquals(((192<<24) | (168 << 16) | (9 << 8) | 1), open.getBgpIdentifier());
	}
}
