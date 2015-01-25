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
 */
package org.bgp4j.netty.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.embedded.EmbeddedChannel;

import org.bgp4j.net.BGPv4Constants;
import org.bgp4j.netty.BGPv4TestBase;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class BGPv4PacketReframerTest extends BGPv4TestBase {

	private EmbeddedChannel channel;
	private UserEventInboundHandler eventHandler;

	@Before
	public void before() {
		eventHandler = new UserEventInboundHandler();
		
		channel = new EmbeddedChannel(new BGPv4Reframer(), eventHandler);
	}
	
	@After
	public void after() {
		channel.close();
		channel = null;
	}
	
	@Test
	public void testValidPacket() throws Exception {
		byte[] packet = new byte[19];
		
		// KEEP alive packet
		for(int i=0; i<BGPv4Constants.BGP_PACKET_MARKER_LENGTH; i++)
			packet[i] = (byte)0xff;
		packet[BGPv4Constants.BGP_PACKET_MARKER_LENGTH]     = 0x00;
		packet[BGPv4Constants.BGP_PACKET_MARKER_LENGTH + 1] = 0x13;
		packet[BGPv4Constants.BGP_PACKET_MARKER_LENGTH + 2] = 0x04;
		
		channel.writeInbound(buildProtocolPacket(packet));
		
		Assert.assertEquals(1, channel.inboundMessages().size());
		Assert.assertEquals(0, channel.outboundMessages().size());
		Assert.assertEquals(0,  eventHandler.events().size());
		
		assertArrayByteBufEquals(new byte[] { 0x04 }, (ByteBuf)channel.readInbound());
	}

	@Test
	public void testValidPacketTwoParts() throws Exception {
		byte[] packet1 = new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, 
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff,  
		};
		byte[] packet2 = new byte[] {
				(byte)0xff, (byte)0xff,
				0x00, 0x13, 0x04
		};

		channel.writeInbound(buildProtocolPacket(packet1));
		channel.writeInbound(buildProtocolPacket(packet2));

		Assert.assertEquals(1, channel.inboundMessages().size());
		Assert.assertEquals(0, channel.outboundMessages().size());
		Assert.assertEquals(0,  eventHandler.events().size());
		
		assertArrayByteBufEquals(new byte[] { 0x04 }, (ByteBuf)channel.readInbound());
	}	

	@Test
	public void testBrokenMarker() throws Exception {
		byte[] packet = new byte[19];
		
		// KEEP alive packet
		packet[0] = (byte)0xfe; 
		for(int i=1; i<BGPv4Constants.BGP_PACKET_MARKER_LENGTH; i++)
			packet[i] = (byte)0xff;
		packet[BGPv4Constants.BGP_PACKET_MARKER_LENGTH]     = 0x00;
		packet[BGPv4Constants.BGP_PACKET_MARKER_LENGTH + 1] = 0x13;
		packet[BGPv4Constants.BGP_PACKET_MARKER_LENGTH + 2] = 0x04;
		
		channel.writeInbound(buildProtocolPacket(packet));
		
		Assert.assertEquals(0, channel.inboundMessages().size());
		Assert.assertEquals(1, channel.outboundMessages().size());
		Assert.assertEquals(1,  eventHandler.events().size());

		assertArrayByteBufEquals(new byte[] { 
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				0x00, 0x15, // length
				0x03,       // notification type code
				0x01, 0x01, // Error code: Message Header Error, Error subcode: Connection not synchronized
				}, (ByteBuf)channel.readOutbound());
		
		// assertNotificationEvent(ConnectionNotSynchronizedNotificationPacket.class, messageRecorder.nextEvent(serverChannel));
	}

	@Test
	public void testBrokenLengthShortPacket() throws Exception {
		byte[] packet = new byte[19];
		
		// KEEP alive packet
		for(int i=0; i<BGPv4Constants.BGP_PACKET_MARKER_LENGTH; i++)
			packet[i] = (byte)0xff;
		packet[BGPv4Constants.BGP_PACKET_MARKER_LENGTH]     = 0x00;
		packet[BGPv4Constants.BGP_PACKET_MARKER_LENGTH + 1] = 0x10;
		packet[BGPv4Constants.BGP_PACKET_MARKER_LENGTH + 2] = 0x04;
		
		channel.writeInbound(buildProtocolPacket(packet));
		
		Assert.assertEquals(0, channel.inboundMessages().size());
		Assert.assertEquals(1, channel.outboundMessages().size());
		Assert.assertEquals(1,  eventHandler.events().size());

		assertArrayByteBufEquals(new byte[] { 
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				0x00, 0x17, // length
				0x03,       // notification type code
				0x01, 0x02, // Error code: Message Header Error, Error subcode: Bad message length
				0x00, 0x10  // Data: Broken length field
				}, (ByteBuf)channel.readOutbound());
	}

	@Test
	public void testBrokenLengthGiantPacket() throws Exception {
		byte[] packet = new byte[19];
		
		// KEEP alive packet
		for(int i=0; i<BGPv4Constants.BGP_PACKET_MARKER_LENGTH; i++)
			packet[i] = (byte)0xff;
		packet[BGPv4Constants.BGP_PACKET_MARKER_LENGTH]     = 0x14; // 20*256 = 5120
		packet[BGPv4Constants.BGP_PACKET_MARKER_LENGTH + 1] = 0x13;
		packet[BGPv4Constants.BGP_PACKET_MARKER_LENGTH + 2] = 0x04;
		
		channel.writeInbound(buildProtocolPacket(packet));
		
		Assert.assertEquals(0, channel.inboundMessages().size());
		Assert.assertEquals(1, channel.outboundMessages().size());
		Assert.assertEquals(1,  eventHandler.events().size());
		
		assertArrayByteBufEquals(new byte[] { 
			(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
			(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
			0x00, 0x17, // length
			0x03,       // notification type code
			0x01, 0x02, // Error code: Message Header Error, Error subcode: Bad message length
			0x14, 0x13  // Data: Broken length field
			}, (ByteBuf)channel.readOutbound());
		
		
		// assertNotificationEvent(BadMessageLengthNotificationPacket.class, messageRecorder.nextEvent(serverChannel));
	}
}
