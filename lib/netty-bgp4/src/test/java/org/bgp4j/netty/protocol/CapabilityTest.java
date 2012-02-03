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
 * File: org.bgp4j.netty.protocol.CapabilityTest.java 
 */
package org.bgp4j.netty.protocol;

import java.util.LinkedList;
import java.util.List;

import junit.framework.Assert;

import org.bgp4j.netty.BGPv4Constants;
import org.bgp4j.netty.BGPv4Constants.AddressFamily;
import org.bgp4j.netty.BGPv4Constants.SubsequentAddressFamily;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.junit.Test;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class CapabilityTest extends ProtocolPacketTestBase {

	@Test
	public void testDecodeMultiProtocolCapability() {
		byte[] packet = new byte[] { 0x01, 0x04, 0x00, 0x01, 0x00, 0x01 };
		ChannelBuffer buffer = ChannelBuffers.buffer(packet.length);
		
		buffer.writeBytes(packet);
		
		Capability cap = Capability.decodeCapability(buffer);
		
		Assert.assertEquals(cap.getClass(), MultiProtocolCapability.class);
		Assert.assertEquals(cap.getCapabilityType(), BGPv4Constants.BGP_CAPABILITY_TYPE_MULTIPROTOCOL);
		Assert.assertEquals(BGPv4Constants.AddressFamily.IPv4, ((MultiProtocolCapability)cap).getAfi());
		Assert.assertEquals(BGPv4Constants.SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, ((MultiProtocolCapability)cap).getSafi());
	}

	@Test
	public void testEncodeMultiProtocolCapability() {
		MultiProtocolCapability cap = new MultiProtocolCapability();
		
		cap.setAfi(AddressFamily.IPv4);
		cap.setSafi(SubsequentAddressFamily.NLRI_UNICAST_FORWARDING);
		
		ChannelBuffer buffer;
		byte[] packet; 
		
		buffer = cap.encodeCapability();
		packet = new byte[buffer.readableBytes()];
		buffer.readBytes(packet);
		assertArraysEquals(packet, new byte[] { 0x01, 0x04, 0x00, 0x01, 0x00, 0x01 });

		cap.setAfi(AddressFamily.IPv4);
		cap.setSafi(SubsequentAddressFamily.NLRI_MULTICAST_FORWARDING);

		buffer = cap.encodeCapability();
		packet = new byte[buffer.readableBytes()];
		buffer.readBytes(packet);
		assertArraysEquals(packet, new byte[] { 0x01, 0x04, 0x00, 0x01, 0x00, 0x02 });

		cap.setAfi(AddressFamily.IPv4);
		cap.setSafi(SubsequentAddressFamily.NLRI_UNICAST_MULTICAST_FORWARDING);

		buffer = cap.encodeCapability();
		packet = new byte[buffer.readableBytes()];
		buffer.readBytes(packet);
		assertArraysEquals(packet, new byte[] { 0x01, 0x04, 0x00, 0x01, 0x00, 0x03 });
		
		cap.setAfi(AddressFamily.IPv6);
		cap.setSafi(SubsequentAddressFamily.NLRI_UNICAST_FORWARDING);
		
		buffer = cap.encodeCapability();
		packet = new byte[buffer.readableBytes()];
		buffer.readBytes(packet);
		assertArraysEquals(packet, new byte[] { 0x01, 0x04, 0x00, 0x02, 0x00, 0x01 });

		cap.setAfi(AddressFamily.IPv6);
		cap.setSafi(SubsequentAddressFamily.NLRI_MULTICAST_FORWARDING);

		buffer = cap.encodeCapability();
		packet = new byte[buffer.readableBytes()];
		buffer.readBytes(packet);
		assertArraysEquals(packet, new byte[] { 0x01, 0x04, 0x00, 0x02, 0x00, 0x02 });

		cap.setAfi(AddressFamily.IPv6);
		cap.setSafi(SubsequentAddressFamily.NLRI_UNICAST_MULTICAST_FORWARDING);

		buffer = cap.encodeCapability();
		packet = new byte[buffer.readableBytes()];
		buffer.readBytes(packet);
		assertArraysEquals(packet, new byte[] { 0x01, 0x04, 0x00, 0x02, 0x00, 0x03 });
}
	
	@Test
	public void testDecodeShortMultiProtocolCapability() {
		boolean caught = false;
		byte[] packet = new byte[] { 0x01, 0x03, 0x00, 0x01, 0x00, };
		ChannelBuffer buffer = ChannelBuffers.buffer(packet.length);
		
		buffer.writeBytes(packet);

		try {
			Capability.decodeCapability(buffer);
		} catch(UnspecificOpenPacketException e) {
			caught = true;
		}
		
		Assert.assertTrue(caught);
	}

	@Test
	public void testDecodeAutonomousSystem4Capability() {
		byte[] packet = new byte[] { 0x41, 0x04, 0x00, 0x00, (byte)0xfc, 0x00 };
		ChannelBuffer buffer = ChannelBuffers.buffer(packet.length);
		
		buffer.writeBytes(packet);
		
		Capability cap = Capability.decodeCapability(buffer);
		
		Assert.assertEquals(cap.getClass(), AutonomousSystem4Capability.class);
		Assert.assertEquals(cap.getCapabilityType(), BGPv4Constants.BGP_CAPABILITY_TYPE_AS4_NUMBERS);
		Assert.assertEquals(((AutonomousSystem4Capability)cap).getAutonomousSystem(), 64512);
	}
	
	@Test
	public void testEncodeAutonomousSystem4Capability() {
		AutonomousSystem4Capability cap = new AutonomousSystem4Capability();
		byte[] packet;
		ChannelBuffer buffer;
		
		cap.setAutonomousSystem(64512);
		buffer = cap.encodeCapability();
		packet = new byte[buffer.readableBytes()];
		buffer.readBytes(packet);
		assertArraysEquals(new byte[] { 0x41, 0x04, 0x00, 0x00, (byte)0xfc, 0x00 }, packet);
	}
	
	@Test
	public void testDecodeRouteRefreshCapability() {
		byte[] packet = new byte[] { 0x02, 0x00 };
		ChannelBuffer buffer = ChannelBuffers.buffer(packet.length);
		
		buffer.writeBytes(packet);
		
		Capability cap = Capability.decodeCapability(buffer);
		
		Assert.assertEquals(cap.getClass(), RouteRefreshCapability.class);
	}
	
	@Test
	public void testEncodeRouteRefreshCapability() {
		RouteRefreshCapability cap = new RouteRefreshCapability();

		byte[] packet;
		ChannelBuffer buffer;

		buffer = cap.encodeCapability();
		packet = new byte[buffer.readableBytes()];
		buffer.readBytes(packet);
		assertArraysEquals(new byte[] { 0x02, 0x00 }, packet);
	}
	
	@Test
	public void testDecodeMultiProtocolCapabilityAutonomousSystem4Capabilit() {
		byte[] packet = new byte[] { 0x01, 0x04, 0x00, 0x01, 0x00, 0x01, 0x41, 0x04, 0x00, 0x00, (byte)0xfc, 0x00  };
		ChannelBuffer buffer = ChannelBuffers.buffer(packet.length);
		
		buffer.writeBytes(packet);
		
		List<Capability> caps = Capability.decodeCapabilities(buffer);

		Assert.assertEquals(2, caps.size());

		Capability cap = caps.remove(0);
		Assert.assertEquals(cap.getClass(), MultiProtocolCapability.class);
		Assert.assertEquals(cap.getCapabilityType(), BGPv4Constants.BGP_CAPABILITY_TYPE_MULTIPROTOCOL);
		Assert.assertEquals(BGPv4Constants.AddressFamily.IPv4, ((MultiProtocolCapability)cap).getAfi());
		Assert.assertEquals(BGPv4Constants.SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, ((MultiProtocolCapability)cap).getSafi());

		cap = caps.remove(0);
		Assert.assertEquals(cap.getClass(), AutonomousSystem4Capability.class);
		Assert.assertEquals(cap.getCapabilityType(), BGPv4Constants.BGP_CAPABILITY_TYPE_AS4_NUMBERS);
		Assert.assertEquals(((AutonomousSystem4Capability)cap).getAutonomousSystem(), 64512);
	}

	@Test
	public void testEncodeMultiProtocolCapabilityAutonomousSystem4Capabilit() {
		List<Capability> caps = new LinkedList<Capability>();
		MultiProtocolCapability cap1 = new MultiProtocolCapability();
		AutonomousSystem4Capability cap2 = new AutonomousSystem4Capability();
		byte[] packet;
		ChannelBuffer buffer;
		
		cap1.setAfi(BGPv4Constants.AddressFamily.IPv4);
		cap1.setSafi(BGPv4Constants.SubsequentAddressFamily.NLRI_UNICAST_FORWARDING);
		caps.add(cap1);
		
		cap2.setAutonomousSystem(64512);
		caps.add(cap2);
	
		buffer = Capability.encodeCapabilities(caps);
		packet = new byte[buffer.readableBytes()];
		buffer.readBytes(packet);
		assertArraysEquals(new byte[] { 0x01, 0x04, 0x00, 0x01, 0x00, 0x01, 0x41, 0x04, 0x00, 0x00, (byte)0xfc, 0x00  }, packet);

	}
	
	@Test
	public void testDecodeAutonomousSystem4CapabilityMultiProtocolCapability() {
		byte[] packet = new byte[] { 0x41, 0x04, 0x00, 0x00, (byte)0xfc, 0x00, 0x01, 0x04, 0x00, 0x01, 0x00, 0x01,   };
		ChannelBuffer buffer = ChannelBuffers.buffer(packet.length);
		
		buffer.writeBytes(packet);
		
		List<Capability> caps = Capability.decodeCapabilities(buffer);

		Assert.assertEquals(2, caps.size());

		Capability cap; 

		cap = caps.remove(0);
		Assert.assertEquals(cap.getClass(), AutonomousSystem4Capability.class);
		Assert.assertEquals(cap.getCapabilityType(), BGPv4Constants.BGP_CAPABILITY_TYPE_AS4_NUMBERS);
		Assert.assertEquals(((AutonomousSystem4Capability)cap).getAutonomousSystem(), 64512);
		
		cap = caps.remove(0);
		Assert.assertEquals(cap.getClass(), MultiProtocolCapability.class);
		Assert.assertEquals(cap.getCapabilityType(), BGPv4Constants.BGP_CAPABILITY_TYPE_MULTIPROTOCOL);
		Assert.assertEquals(BGPv4Constants.AddressFamily.IPv4, ((MultiProtocolCapability)cap).getAfi());
		Assert.assertEquals(BGPv4Constants.SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, ((MultiProtocolCapability)cap).getSafi());
	}
	
}
