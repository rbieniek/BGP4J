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
package org.bgp4j.netty.handlers;

import static org.fest.assertions.api.Assertions.assertThat;
import io.netty.buffer.ByteBuf;
import io.netty.channel.embedded.EmbeddedChannel;

import java.net.Inet4Address;
import java.util.List;

import org.bgp4j.net.ASType;
import org.bgp4j.net.AddressFamily;
import org.bgp4j.net.AddressPrefixBasedORFEntry;
import org.bgp4j.net.NetworkLayerReachabilityInformation;
import org.bgp4j.net.ORFAction;
import org.bgp4j.net.ORFEntry;
import org.bgp4j.net.ORFMatch;
import org.bgp4j.net.ORFRefreshType;
import org.bgp4j.net.ORFType;
import org.bgp4j.net.Origin;
import org.bgp4j.net.OutboundRouteFilter;
import org.bgp4j.net.SubsequentAddressFamily;
import org.bgp4j.net.attributes.ASPathAttribute;
import org.bgp4j.net.attributes.LocalPrefPathAttribute;
import org.bgp4j.net.attributes.MultiExitDiscPathAttribute;
import org.bgp4j.net.attributes.MultiProtocolReachableNLRI;
import org.bgp4j.net.attributes.MultiProtocolUnreachableNLRI;
import org.bgp4j.net.attributes.NextHopPathAttribute;
import org.bgp4j.net.attributes.OriginPathAttribute;
import org.bgp4j.net.capabilities.AutonomousSystem4Capability;
import org.bgp4j.net.capabilities.Capability;
import org.bgp4j.net.capabilities.MultiProtocolCapability;
import org.bgp4j.net.capabilities.RouteRefreshCapability;
import org.bgp4j.net.capabilities.UnknownCapability;
import org.bgp4j.net.events.ConnectionNotSynchronizedNotificationEvent;
import org.bgp4j.net.events.open.BadBgpIdentifierNotificationEvent;
import org.bgp4j.net.events.open.UnsupportedOptionalParameterNotificationEvent;
import org.bgp4j.net.events.open.UnsupportedVersionNumberNotificationEvent;
import org.bgp4j.net.events.update.AttributeLengthNotificationEvent;
import org.bgp4j.net.events.update.InvalidNetworkFieldNotificationEvent;
import org.bgp4j.net.events.update.InvalidNextHopNotificationEvent;
import org.bgp4j.net.events.update.InvalidOriginNotificationEvent;
import org.bgp4j.net.events.update.MalformedASPathAttributeNotificationEvent;
import org.bgp4j.net.events.update.MalformedAttributeListNotificationEvent;
import org.bgp4j.net.events.update.OptionalAttributeErrorNotificationEvent;
import org.bgp4j.net.packets.BGPv4Packet;
import org.bgp4j.net.packets.open.OpenPacket;
import org.bgp4j.net.packets.refresh.RouteRefreshPacket;
import org.bgp4j.net.packets.update.UpdatePacket;
import org.bgp4j.netty.BGPv4TestBase;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class BGPv4CodecTest extends BGPv4TestBase {
	private EmbeddedChannel channel;
	private UserEventInboundHandler eventHandler;
	private EmbeddedChannel completeChannel;
	private UserEventInboundHandler completeEventHandler;

	@Before
	public void before() {
		eventHandler = new UserEventInboundHandler();		
		channel = new EmbeddedChannel(new BGPv4Codec(), eventHandler);
		completeEventHandler = new UserEventInboundHandler();		
		completeChannel = new EmbeddedChannel(new BGPv4Reframer(), new BGPv4Codec(), completeEventHandler);
	}
	
	@After
	public void after() {
		channel.close();
		channel = null;
		completeChannel.close();
		completeChannel = null;
	}

	@Test
	public void testStrippedBasicOpenPacket() throws Exception {
		channel.writeInbound(buildProtocolPacket(new byte[] {
				(byte)0x01, // type code OPEN
				(byte)0x04, // BGP version 4 
				(byte)0xfc, (byte)0x00, // Autonomous system 64512 
				(byte)0x00, (byte)0xb4, // hold time 180 seconds
				(byte)0xc0, (byte)0xa8, (byte)0x09, (byte)0x01, /// BGP identifier 192.168.9.1 
				(byte)0x0, // optional parameter length 0 
		}));

		assertThat(channel.outboundMessages()).hasSize(0);
		assertThat(channel.inboundMessages()).hasSize(1);
	
		OpenPacket open = safeDowncast((BGPv4Packet)channel.readInbound(), OpenPacket.class);
		
		Assert.assertEquals(4, open.getProtocolVersion());
		Assert.assertEquals(64512, open.getAutonomousSystem());
		Assert.assertEquals(180, open.getHoldTime());
		Assert.assertEquals(0, open.getCapabilities().size());
		Assert.assertEquals(((192L<<24) | (168L << 16) | (9L << 8) | 1L), open.getBgpIdentifier());
	}

	@Test
	public void testCompleteBasicOpenPacket() throws Exception {
		completeChannel.writeInbound(buildProtocolPacket(new byte[] {
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

		assertThat(completeChannel.inboundMessages()).hasSize(1);
		assertThat(completeChannel.outboundMessages()).hasSize(0);
	
		OpenPacket open = safeDowncast((BGPv4Packet)completeChannel.readInbound(), OpenPacket.class);
		
		Assert.assertEquals(4, open.getProtocolVersion());
		Assert.assertEquals(64512, open.getAutonomousSystem());
		Assert.assertEquals(180, open.getHoldTime());
		Assert.assertEquals(0, open.getCapabilities().size());
		Assert.assertEquals(((192L<<24) | (168L << 16) | (9L << 8) | 1), open.getBgpIdentifier());
	}

	@Test
	public void testStrippedFullOpenPacket() throws Exception {
		Capability cap;
		
		channel.writeInbound(buildProtocolPacket(new byte[] {
				(byte)0x01, // type code OPEN
				(byte)0x04, // BGP version 4 
				(byte)0xfc, (byte)0x00, // Autonomous system 64512 
				(byte)0x00, (byte)0xb4, // hold time 180 seconds
				(byte)0xc0, (byte)0xa8, (byte)0x09, (byte)0x01, /// BGP identifier 192.168.9.1 
				(byte)0x18, // optional parameter length 
				(byte)0x02, (byte)0x06, // parameter type 2 (capability), length 6 octets 
				(byte)0x01, (byte)0x04, (byte)0x00, (byte)0x01, (byte)0x00, (byte)0x01, // Multi-Protocol capability (type 1), IPv4, Unicast 
				(byte)0x02, (byte)0x02, // parameter type 2 (capability), length 2 octets 
				(byte)0x80, (byte)0x00, // Route-Refresh capability according to Wireshark, length 0 octets
				(byte)0x02, (byte)0x02, // parameter type 2 (capability), length 2 octets
				(byte)0x02, (byte)0x00, // Route-Refresh capability, length 0 octets
				(byte)0x02, (byte)0x06, // parameter type 2 (capability), length 6 octets
				(byte)0x41,	(byte)0x04, (byte)0x00, (byte)0x00, (byte)0xfc, (byte)0x00 // 4 octet AS capability, AS 64512
		}));

		assertThat(channel.outboundMessages()).hasSize(0);
		assertThat(channel.inboundMessages()).hasSize(1);
	
		OpenPacket open = safeDowncast((BGPv4Packet)channel.readInbound(), OpenPacket.class);
		
		Assert.assertEquals(4, open.getProtocolVersion());
		Assert.assertEquals(64512, open.getAutonomousSystem());
		Assert.assertEquals(180, open.getHoldTime());
		Assert.assertEquals(((192L<<24) | (168L << 16) | (9L << 8) | 1L), open.getBgpIdentifier());
		Assert.assertEquals(4, open.getCapabilities().size());

		cap = open.getCapabilities().remove(0);
		Assert.assertEquals(MultiProtocolCapability.class, cap.getClass());
		Assert.assertEquals(AddressFamily.IPv4, ((MultiProtocolCapability)cap).getAfi());
		Assert.assertEquals(SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, ((MultiProtocolCapability)cap).getSafi());

		cap = open.getCapabilities().remove(0);
		Assert.assertEquals(UnknownCapability.class, cap.getClass());
		Assert.assertEquals(128, ((UnknownCapability)cap).getCapabilityType());
		
		cap = open.getCapabilities().remove(0);
		Assert.assertEquals(RouteRefreshCapability.class, cap.getClass());

		cap = open.getCapabilities().remove(0);
		Assert.assertEquals(AutonomousSystem4Capability.class, cap.getClass());
		Assert.assertEquals(64512, ((AutonomousSystem4Capability)cap).getAutonomousSystem());
	}

	@Test
	public void testCompleteFullOpenPacket() throws Exception {
		Capability cap;
		
		completeChannel.writeInbound(buildProtocolPacket(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				0x00, 0x35, // length 53 octets
				(byte)0x01, // type code OPEN
				(byte)0x04, // BGP version 4 
				(byte)0xfc, (byte)0x00, // Autonomous system 64512 
				(byte)0x00, (byte)0xb4, // hold time 180 seconds
				(byte)0xc0, (byte)0xa8, (byte)0x09, (byte)0x01, /// BGP identifier 192.168.9.1 
				(byte)0x18, // optional parameter length 
				(byte)0x02, (byte)0x06, // parameter type 2 (capability), length 6 octets 
				(byte)0x01, (byte)0x04, (byte)0x00, (byte)0x01, (byte)0x00, (byte)0x01, // Multi-Protocol capability (type 1), IPv4, Unicast 
				(byte)0x02, (byte)0x02, // parameter type 2 (capability), length 2 octets 
				(byte)0x80, (byte)0x00, // Route-Refresh capability according to Wireshark, length 0 octets
				(byte)0x02, (byte)0x02, // parameter type 2 (capability), length 2 octets
				(byte)0x02, (byte)0x00, // Route-Refresh capability, length 0 octets
				(byte)0x02, (byte)0x06, // parameter type 2 (capability), length 6 octets
				(byte)0x41,	(byte)0x04, (byte)0x00, (byte)0x00, (byte)0xfc, (byte)0x00 // 4 octet AS capability, AS 64512
		}));

		assertThat(completeChannel.outboundMessages()).hasSize(0);
		assertThat(completeChannel.inboundMessages()).hasSize(1);
	
		OpenPacket open = safeDowncast((BGPv4Packet)completeChannel.readInbound(), OpenPacket.class);
		
		Assert.assertEquals(4, open.getProtocolVersion());
		Assert.assertEquals(64512, open.getAutonomousSystem());
		Assert.assertEquals(180, open.getHoldTime());
		Assert.assertEquals(((192L<<24) | (168L << 16) | (9L << 8) | 1), open.getBgpIdentifier());
		Assert.assertEquals(4, open.getCapabilities().size());

		cap = open.getCapabilities().remove(0);
		Assert.assertEquals(MultiProtocolCapability.class, cap.getClass());
		Assert.assertEquals(AddressFamily.IPv4, ((MultiProtocolCapability)cap).getAfi());
		Assert.assertEquals(SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, ((MultiProtocolCapability)cap).getSafi());

		cap = open.getCapabilities().remove(0);
		Assert.assertEquals(UnknownCapability.class, cap.getClass());
		Assert.assertEquals(128, ((UnknownCapability)cap).getCapabilityType());
		
		cap = open.getCapabilities().remove(0);
		Assert.assertEquals(RouteRefreshCapability.class, cap.getClass());

		cap = open.getCapabilities().remove(0);
		Assert.assertEquals(AutonomousSystem4Capability.class, cap.getClass());
		Assert.assertEquals(64512, ((AutonomousSystem4Capability)cap).getAutonomousSystem());
	}
	
	@Test
	public void testStrippedBadBgpVersionOpenPacket() throws Exception {
		channel.writeInbound(buildProtocolPacket(new byte[] {
				(byte)0x01, // type code OPEN
				(byte)0x05, // BGP version 5 
				(byte)0xfc, (byte)0x00, // Autonomous system 64512 
				(byte)0x00, (byte)0xb4, // hold time 180 seconds
				(byte)0xc0, (byte)0xa8, (byte)0x09, (byte)0x01, /// BGP identifier 192.168.9.1 
				(byte)0x0, // optional parameter length 0 
		}));

		assertThat(eventHandler.events()).hasSize(1);
		assertThat(channel.outboundMessages()).hasSize(1);
	
		assertBufferContents(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0x00, (byte)0x17, // length 23 octets
				(byte)0x03, // type code NOTIFICATION
				(byte)0x2, // OPEN error message
				(byte)01, // Unsupported Version Number
				(byte)0x00, (byte)0x04, // BGP version 4 
		}, (ByteBuf)channel.readOutbound());
		
		assertThat(eventHandler.readEvent()).isInstanceOf(UnsupportedVersionNumberNotificationEvent.class);
	}
	
	@Test
	public void testCompleteBadBgpVersionOpenPacket() throws Exception {
		completeChannel.writeInbound(buildProtocolPacket(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				0x00, 0x1d, // length 29 octets
				(byte)0x01, // type code OPEN
				(byte)0x05, // BGP version 5 
				(byte)0xfc, (byte)0x00, // Autonomous system 64512 
				(byte)0x00, (byte)0xb4, // hold time 180 seconds
				(byte)0xc0, (byte)0xa8, (byte)0x09, (byte)0x01, /// BGP identifier 192.168.9.1 
				(byte)0x0, // optional parameter length 0 
		}));

		assertThat(completeEventHandler.events()).hasSize(1);
		assertThat(completeChannel.outboundMessages()).hasSize(1);

		assertArrayByteBufEquals(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0x00, (byte)0x17, // length 23 octets
				(byte)0x03, // type code NOTIFICATION
				(byte)0x2, // OPEN error message
				(byte)01, // Unsupported Version Number
				(byte)0x00, (byte)0x04, // BGP version 4 
		}, (ByteBuf)completeChannel.readOutbound());		
		
		assertThat(completeEventHandler.readEvent()).isInstanceOf(UnsupportedVersionNumberNotificationEvent.class);
	}
	
	@Test
	public void testStrippedBadBgpIdentifierOpenPacket() throws Exception {
		channel.writeInbound(buildProtocolPacket(new byte[] {
				(byte)0x01, // type code OPEN
				(byte)0x04, // BGP version 4 
				(byte)0xfc, (byte)0x00, // Autonomous system 64512 
				(byte)0x00, (byte)0xb4, // hold time 180 seconds
				(byte)0xe0, (byte)0x0, (byte)0x0, (byte)0x01, /// BGP identifier 224.0.0.1 (Multicast IP) 
				(byte)0x0, // optional parameter length 0 
		}));

		assertThat(eventHandler.events()).hasSize(1);
		assertThat(channel.outboundMessages()).hasSize(1);
	
		assertArrayByteBufEquals(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0x00, (byte)0x15, // length 21 octets
				(byte)0x03, // type code NOTIFICATION
				(byte)0x2, // OPEN error message
				(byte)0x3, // Bad BGP Identifier
		}, (ByteBuf)channel.readOutbound());		
		
		assertThat(eventHandler.readEvent()).isInstanceOf(BadBgpIdentifierNotificationEvent.class);
	}

	@Test
	public void testStrippedUnsupportedOptionalParameterOpenPacket() throws Exception {
		channel.writeInbound(buildProtocolPacket(new byte[] {
				(byte)0x01, // type code OPEN
				(byte)0x04, // BGP version 4 
				(byte)0xfc, (byte)0x00, // Autonomous system 64512 
				(byte)0x00, (byte)0xb4, // hold time 180 seconds
				(byte)0xc0, (byte)0xa8, (byte)0x09, (byte)0x01, /// BGP identifier 192.168.9.1 
				(byte)0x2, // optional parameter length 0
				(byte)0x03, (byte)0x00 // bogus optional parameter type code 3
		}));

		assertThat(eventHandler.events()).hasSize(1);
		assertThat(channel.outboundMessages()).hasSize(1);
	
		assertArrayByteBufEquals(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0x00, (byte)0x15, // length 21 octets
				(byte)0x03, // type code NOTIFICATION
				(byte)0x2, // OPEN error message
				(byte)0x4, // Unsupported Optional Parameter
		}, (ByteBuf)channel.readOutbound());		
		
		assertThat(eventHandler.readEvent()).isInstanceOf(UnsupportedOptionalParameterNotificationEvent.class);
	}

	@Test
	public void testCompleteUpdatePacket() throws Exception {
		completeChannel.writeInbound(buildProtocolPacket(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				0x00, 0x35, // length 53 octets
				(byte)0x02, // type code 2 (UPDATE) 
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x1d, // path attributes length (29 octets)
				(byte)0x40, (byte)0x01, (byte)0x01, (byte)0x02, // Path attribute: ORIGIN INCOMPLETE  
				(byte)0x50, (byte)0x02, (byte)0x00, (byte)0x00, // Path attribute: AS_PATH emtpy 
				(byte)0x40, (byte)0x03, (byte)0x04, (byte)0xc0, (byte)0xa8, (byte)0x04, (byte)0x02, // Path attribute: NEXT_HOP 192.168.4.2
				(byte)0x80, (byte)0x04, (byte)0x04, (byte)0x00, (byte)0x00, (byte)0x08, (byte)0x00, // Path attribute: MULT_EXIT_DISC 2048
				(byte)0x40, (byte)0x05, (byte)0x04, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x64, // Path attribute: LOCAL_PREF 100
				(byte)0x00 // NLRI: 0.0.0.0/0	
		}));

		assertThat(completeChannel.outboundMessages()).hasSize(0);
		assertThat(completeChannel.inboundMessages()).hasSize(1);
	
		UpdatePacket packet = safeDowncast((BGPv4Packet)completeChannel.readInbound(), UpdatePacket.class);
		
		Assert.assertEquals(0, packet.getWithdrawnRoutes().size());
		Assert.assertEquals(5, packet.getPathAttributes().size());
		Assert.assertEquals(1, packet.getNlris().size());		
		
		OriginPathAttribute origin = (OriginPathAttribute)packet.getPathAttributes().remove(0);
		ASPathAttribute asPath = (ASPathAttribute)packet.getPathAttributes().remove(0);
		NextHopPathAttribute nextHop = (NextHopPathAttribute)packet.getPathAttributes().remove(0);
		MultiExitDiscPathAttribute multiExitDisc = (MultiExitDiscPathAttribute)packet.getPathAttributes().remove(0);
		LocalPrefPathAttribute localPref = (LocalPrefPathAttribute)packet.getPathAttributes().remove(0);
		NetworkLayerReachabilityInformation nlri = packet.getNlris().remove(0);
		
		Assert.assertEquals(Origin.INCOMPLETE, origin.getOrigin());
		Assert.assertEquals(ASType.AS_NUMBER_2OCTETS, asPath.getAsType());
		Assert.assertEquals(0, asPath.getPathSegments().size());
		Assert.assertEquals(Inet4Address.getByAddress(new byte[] {(byte)0xc0, (byte)0xa8, (byte)0x04, (byte)0x02 }), nextHop.getNextHop().getAddress());
		Assert.assertEquals(2048, multiExitDisc.getDiscriminator());
		Assert.assertEquals(100, localPref.getLocalPreference());
		
		Assert.assertEquals(0, nlri.getPrefixLength());
		Assert.assertNotNull(nlri.getPrefix());
		Assert.assertEquals(1, nlri.getPrefix().length);
		Assert.assertEquals(0, nlri.getPrefix()[0]);
	}
	
	@Test
	public void testMalformedWithdrawnRoutesNopathAttributeListUpdatePacket() throws Exception {
		completeChannel.writeInbound(buildProtocolPacket(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				0x00, 0x15, // length 21 octets
				(byte)0x02, // type code UPDATE
				0x00, 0x00, // bad withdrawn routes length (2 octets), points to end of packet 
		}));

		assertThat(completeEventHandler.events()).hasSize(1);
		assertThat(completeChannel.outboundMessages()).hasSize(1);

		assertArrayByteBufEquals(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0x00, (byte)0x15, // length 21 octets
				(byte)0x03, // type code NOTIFICATION
				(byte)0x01, // Message header error
				(byte)0x01, // Connection not synchronized
		}, (ByteBuf)completeChannel.readOutbound());
		
		assertThat(completeEventHandler.readEvent()).isInstanceOf(ConnectionNotSynchronizedNotificationEvent.class);

	}

	@Test
	public void testDecodeMalformedWithdrawnRoutesLengthOnPacketEndUpdatePacket() throws Exception {
		completeChannel.writeInbound(buildProtocolPacket(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				0x00, 0x17, // length 23 octets
				(byte)0x02, // type code UPDATE
				0x00, 0x02, // bad withdrawn routes length (2 octets), points to end of packet 
				0x00, 0x00, // Total path attributes length  (0 octets)
		}));

		assertThat(completeEventHandler.events()).hasSize(1);
		assertThat(completeChannel.outboundMessages()).hasSize(1);

		assertArrayByteBufEquals(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0x00, (byte)0x15, // length 21 octets
				(byte)0x03, // type code NOTIFICATION
				(byte)0x03, // Update message error
				(byte)0x01, // Malformed attribute list
		}, (ByteBuf)completeChannel.readOutbound());		
	
		assertThat(completeEventHandler.readEvent()).isInstanceOf(MalformedAttributeListNotificationEvent.class);
	}
		
	@Test
	public void testDecodeMalformedWithdrawnRoutesLengthOverEndUpdatePacket() throws Exception {
		completeChannel.writeInbound(buildProtocolPacket(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				0x00, 0x17, // length 23 octets
				(byte)0x02, // type code UPDATE
				0x00, 0x04, // bad withdrawn routes length (4 octets), points beyond end of packet 
				0x00, 0x00, // Total path attributes length  (0 octets)
		}));

		assertThat(completeEventHandler.events()).hasSize(1);
		assertThat(completeChannel.outboundMessages()).hasSize(1);

		assertArrayByteBufEquals(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0x00, (byte)0x15, // length 21 octets
				(byte)0x03, // type code NOTIFICATION
				(byte)0x03, // Update message error
				(byte)0x01, // Malformed attribute list
		}, (ByteBuf)completeChannel.readOutbound());		
		
		assertThat(completeEventHandler.readEvent()).isInstanceOf(MalformedAttributeListNotificationEvent.class);
	}
	
	@Test
	public void testDecodeAttributeListTooLongUpdatePacket() throws Exception {
		completeChannel.writeInbound(buildProtocolPacket(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				0x00, 0x17, // length 23 octets
				(byte)0x02, // type code UPDATE
				0x00, 0x00, // withdrawn routes length (0 octets) 
				0x00, 0x02, // Total path attributes length  (2 octets), points to end of packet
		}));

		assertThat(completeEventHandler.events()).hasSize(1);
		assertThat(completeChannel.outboundMessages()).hasSize(1);

		assertArrayByteBufEquals(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0x00, (byte)0x15, // length 21 octets
				(byte)0x03, // type code NOTIFICATION
				(byte)0x03, // Update message error
				(byte)0x01, // Malformed attribute list
		}, (ByteBuf)completeChannel.readOutbound());		
		
		assertThat(completeEventHandler.readEvent()).isInstanceOf(MalformedAttributeListNotificationEvent.class);
	}

	
	@Test
	public void testDecodeOriginInvalidPacket() throws Exception {
		completeChannel.writeInbound(buildProtocolPacket(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				0x00, 0x1b, // length 27 octets
				(byte)0x02, // type code UPDATE
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x04, // path attributes length (29 octets)
				(byte)0x40, (byte)0x01, (byte)0x01, (byte)0x04, // Path attribute: ORIGIN INCOMPLETE  
		}));

		assertThat(completeEventHandler.events()).hasSize(1);
		assertThat(completeChannel.outboundMessages()).hasSize(1);

		assertArrayByteBufEquals(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0x00, (byte)0x19, // length 25 octets
				(byte)0x03, // type code NOTIFICATION
				(byte)0x03, // Update message error
				(byte)0x06, // Invalid origin
				(byte)0x40, (byte)0x01, (byte)0x01, (byte)0x04, // Path attribute: ORIGIN INCOMPLETE  
		}, (ByteBuf)completeChannel.readOutbound());		
		
		assertThat(completeEventHandler.readEvent()).isInstanceOf(InvalidOriginNotificationEvent.class);
	}

	@Test
	public void testDecodeOriginShortPacket() throws Exception {
		completeChannel.writeInbound(buildProtocolPacket(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				0x00, 0x1a, // length 26 octets
				(byte)0x02, // type code UPDATE
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x03, // path attributes length (29 octets)
				(byte)0x40, (byte)0x01, (byte)0x00, // Path attribute:   
		}));

		assertThat(completeEventHandler.events()).hasSize(1);
		assertThat(completeChannel.outboundMessages()).hasSize(1);

		assertArrayByteBufEquals(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0x00, (byte)0x18, // length 24 octets
				(byte)0x03, // type code NOTIFICATION
				(byte)0x03, // Update message error
				(byte)0x05, // Attribute length
				(byte)0x40, (byte)0x01, (byte)0x00, // Path attribute: ORIGIN INCOMPLETE  
		}, (ByteBuf)completeChannel.readOutbound());
		
		assertThat(completeEventHandler.readEvent()).isInstanceOf(AttributeLengthNotificationEvent.class);
	}

	@Test
	public void testDecodeASPath4BadPathTypeOneASNumberPacket() throws Exception {
		completeChannel.writeInbound(buildProtocolPacket(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				0x00, 0x21, // length 33 octets
				(byte)0x02, // type code UPDATE
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x0a, // path attributes length (10 octets)
				(byte)0x50, (byte)0x11, (byte)0x00, (byte)0x06, // Path attribute: 6 octets AS_PATH  
				0x05, 0x01, 0x00, 0x00, 0x12, 0x34, // Invalid 0x1234 
		}));

		assertThat(completeEventHandler.events()).hasSize(1);
		assertThat(completeChannel.outboundMessages()).hasSize(1);

		assertArrayByteBufEquals(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0x00, (byte)0x1f, // length 31 octets
				(byte)0x03, // type code NOTIFICATION
				(byte)0x03, // Update message error
				(byte)0x0b, // Malformed AS Path
				(byte)0x50, (byte)0x11, (byte)0x00, (byte)0x06,  0x05, 0x01, 0x00, 0x00, 0x12, 0x34, 
		}, (ByteBuf)completeChannel.readOutbound());
		
		assertThat(completeEventHandler.readEvent()).isInstanceOf(MalformedASPathAttributeNotificationEvent.class);
	}

	@Test
	public void testDecodeASPath4ASSequenceTwoASNumberOneMissingPacket() throws Exception {
		completeChannel.writeInbound(buildProtocolPacket(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				0x00, 0x21, // length 33 octets
				(byte)0x02, // type code UPDATE
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x0a, // path attributes length (8 octets)
				(byte)0x50, (byte)0x11, (byte)0x00, (byte)0x06, // Path attribute: 4 octets AS_PATH  
				0x02, 0x02, 0x00, 0x00, 0x12, 0x34, // Invalid 0x1234 
		}));

		assertThat(completeEventHandler.events()).hasSize(1);
		assertThat(completeChannel.outboundMessages()).hasSize(1);

		assertArrayByteBufEquals(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0x00, (byte)0x1f, // length 31 octets
				(byte)0x03, // type code NOTIFICATION
				(byte)0x03, // Update message error
				(byte)0x0b, // Malformed AS Path
				(byte)0x50, (byte)0x11, (byte)0x00, (byte)0x06,  0x02, 0x02, 0x00, 0x00, 0x12, 0x34, 
		}, (ByteBuf)completeChannel.readOutbound());		
		
		assertThat(completeEventHandler.readEvent()).isInstanceOf(MalformedASPathAttributeNotificationEvent.class);
	}
	
	@Test
	public void testDecodeASPath2BadPathTypeOneASNumberPacket() throws Exception {
		completeChannel.writeInbound(buildProtocolPacket(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				0x00, 0x1f, // length 31 octets
				(byte)0x02, // type code UPDATE
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x08, // path attributes length (8 octets)
				(byte)0x50, (byte)0x02, (byte)0x00, (byte)0x04, // Path attribute: 4 octets AS_PATH  
				0x05, 0x01, 0x12, 0x34, // Invalid 0x1234 
		}));

		assertThat(completeEventHandler.events()).hasSize(1);
		assertThat(completeChannel.outboundMessages()).hasSize(1);

		assertArrayByteBufEquals(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0x00, (byte)0x1d, // length 29 octets
				(byte)0x03, // type code NOTIFICATION
				(byte)0x03, // Update message error
				(byte)0x0b, // Malformed AS Path
				(byte)0x50, (byte)0x02, (byte)0x00, (byte)0x04, 0x05, 0x01, 0x12, 0x34, 
		}, (ByteBuf)completeChannel.readOutbound());
		
		assertThat(completeEventHandler.readEvent()).isInstanceOf(MalformedASPathAttributeNotificationEvent.class);
	}

	@Test
	public void testDecodeASPath2ASSequenceTwoASNumberOneMissingPacket() throws Exception {
		completeChannel.writeInbound(buildProtocolPacket(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				0x00, 0x1f, // length 31 octets
				(byte)0x02, // type code UPDATE
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x08, // path attributes length (8 octets)
				(byte)0x50, (byte)0x02, (byte)0x00, (byte)0x04, // Path attribute: 4 octets AS_PATH  
				0x02, 0x02, 0x12, 0x34, // Invalid 0x1234 
		}));

		assertThat(completeEventHandler.events()).hasSize(1);
		assertThat(completeChannel.outboundMessages()).hasSize(1);

		assertArrayByteBufEquals(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0x00, (byte)0x1d, // length 29 octets
				(byte)0x03, // type code NOTIFICATION
				(byte)0x03, // Update message error
				(byte)0x0b, // Malformed AS Path
				(byte)0x50, (byte)0x02, (byte)0x00, (byte)0x04, 0x02, 0x02, 0x12, 0x34, 
		}, (ByteBuf)completeChannel.readOutbound());
		
		assertThat(completeEventHandler.readEvent()).isInstanceOf(MalformedASPathAttributeNotificationEvent.class);
	}

	@Test
	public void testNextHopPacketIpMulticastNextHop() throws Exception {
		completeChannel.writeInbound(buildProtocolPacket(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				0x00, 0x1e, // length 30 octets
				(byte)0x02, // type code UPDATE
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x07, // path attributes length (7 octets)
				(byte)0x40, (byte)0x03, (byte)0x04, (byte)0xe0, (byte)0x00, (byte)0x00, (byte)0x01, // Path attribute: NEXT_HOP 224.0.0.1
		}));

		assertThat(completeEventHandler.events()).hasSize(1);
		assertThat(completeChannel.outboundMessages()).hasSize(1);

		assertArrayByteBufEquals(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0x00, (byte)0x1c, // length 28 octets
				(byte)0x03, // type code NOTIFICATION
				(byte)0x03, // Update message error
				(byte)0x08, // Invalid next hop
				(byte)0x40, (byte)0x03, (byte)0x04, (byte)0xe0, (byte)0x00, (byte)0x00, (byte)0x01,
		}, (ByteBuf)completeChannel.readOutbound());
		
		assertThat(completeEventHandler.readEvent()).isInstanceOf(InvalidNextHopNotificationEvent.class);
	}
	
	@Test
	public void testDecodeOneNlri() throws Exception {
		completeChannel.writeInbound(buildProtocolPacket(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				0x00, 0x1a, // length 26 octets
				(byte)0x02, // type code UPDATE
				0x00, 0x00, // withdrawn routes length (0 octets)
				0x00, 0x00, // Total path attributes length  (0 octets)
				0x10, (byte)0xac, 0x10, // NLRI 172.16/16 
		}));

		assertThat(completeChannel.outboundMessages()).hasSize(0);
		assertThat(completeChannel.inboundMessages()).hasSize(1);

		UpdatePacket packet = safeDowncast((BGPv4Packet)completeChannel.readInbound(), UpdatePacket.class);
		NetworkLayerReachabilityInformation nlri;
		
		Assert.assertEquals(0, packet.getWithdrawnRoutes().size());
		Assert.assertEquals(0, packet.getPathAttributes().size());
		Assert.assertEquals(1, packet.getNlris().size());
		
		nlri = packet.getNlris().remove(0);

		Assert.assertEquals(16, nlri.getPrefixLength());
		assertArraysEquals(new byte[] { (byte)0xac, 0x10} , nlri.getPrefix());
	}
	
	@Test
	public void testDecodeTwoNlri() throws Exception {
		completeChannel.writeInbound(buildProtocolPacket(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				0x00, 0x1f, // length 31 octets
				(byte)0x02, // type code UPDATE
				0x00, 0x00, // withdrawn routes length (0 octets)
				0x00, 0x00, // Total path attributes length  (0 octets)
				0x10, (byte)0xac, 0x10, // NLRI 172.16/16
				0x1c, (byte)0xc0, (byte)0xa8, 0x20, 0, // NLRI 192.168.32.0/28
		}));

		assertThat(completeChannel.outboundMessages()).hasSize(0);
		assertThat(completeChannel.inboundMessages()).hasSize(1);

		UpdatePacket packet = safeDowncast((BGPv4Packet)completeChannel.readInbound(), UpdatePacket.class);
		NetworkLayerReachabilityInformation nlri;
		
		Assert.assertEquals(0, packet.getWithdrawnRoutes().size());
		Assert.assertEquals(0, packet.getPathAttributes().size());
		Assert.assertEquals(2, packet.getNlris().size());
		
		nlri = packet.getNlris().remove(0);

		Assert.assertEquals(16, nlri.getPrefixLength());
		assertArraysEquals(new byte[] { (byte)0xac, 0x10} , nlri.getPrefix());

		nlri = packet.getNlris().remove(0);

		Assert.assertEquals(28, nlri.getPrefixLength());
		assertArraysEquals(new byte[] { (byte)0xc0, (byte)0xa8, 0x20, 0} , nlri.getPrefix());
	}

	@Test
	public void testDecodeBogusNlri() throws Exception {
		completeChannel.writeInbound(buildProtocolPacket(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				0x00, 0x1e, // length 30 octets
				(byte)0x02, // type code UPDATE
				0x00, 0x00, // withdrawn routes length (0 octets)
				0x00, 0x00, // Total path attributes length  (0 octets)
				0x10, (byte)0xac, 0x10, // NLRI 172.16/16
				0x1c, (byte)0xc0, (byte)0xa8, 0x20,  // NLRI 192.168.32/28 bogus one octet missing
		}));

		assertThat(completeEventHandler.events()).hasSize(1);
		assertThat(completeChannel.outboundMessages()).hasSize(1);

		assertArrayByteBufEquals(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0x00, (byte)0x15, // length 21 octets
				(byte)0x03, // type code NOTIFICATION
				(byte)0x03, // Update message error
				(byte)0x0a, // Invalid network field
		}, (ByteBuf)completeChannel.readOutbound());		
		
		assertThat(completeEventHandler.readEvent()).isInstanceOf(InvalidNetworkFieldNotificationEvent.class);
	}
	
	@Test
	public void testDecodeValidMpReachNlriFourByteNextHopTwoNlri() throws Exception {
		completeChannel.writeInbound(buildProtocolPacket(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				0x00, 0x2a, // length 42 octets
				(byte)0x02, // type code 2 (UPDATE) 
				0x00, 0x00, // withdrawn routes length (0 octets)
				0x00, 0x13, // Total path attributes length  (19 octets)
				(byte)0x80, 0x0e, 0x10, // Path Attribute MP_REACH_NLRI
				0x00, 0x01, 0x01, // AFI(IPv4) SAFI(UNICAT_ROUTING) 
				0x04, (byte)0xc0, (byte)0xa8, 0x04, 0x02, // NEXT_HOP 4 octets 192.168.4.2, 
				0x00, // reserved 
				0x0c, (byte)0xab, 0x10, //  NLRI 172.16.0.0/12
				0x14, (byte)0xc0, (byte)0xa8, (byte)0xf0, //  NLRI 192.168.255.0/20
		}));

		assertThat(completeChannel.outboundMessages()).hasSize(0);
		assertThat(completeChannel.inboundMessages()).hasSize(1);
	
		UpdatePacket packet = safeDowncast((BGPv4Packet)completeChannel.readInbound(), UpdatePacket.class);
		
		Assert.assertEquals(0, packet.getWithdrawnRoutes().size());
		Assert.assertEquals(1, packet.getPathAttributes().size());
		Assert.assertEquals(0, packet.getNlris().size());		
		
		MultiProtocolReachableNLRI mp = (MultiProtocolReachableNLRI)packet.getPathAttributes().remove(0);
		
		Assert.assertEquals(AddressFamily.IPv4, mp.getAddressFamily());
		Assert.assertEquals(SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, mp.getSubsequentAddressFamily());
		assertArraysEquals(new byte[] { (byte)0xc0, (byte)0xa8, 0x04, 0x02, }, mp.getNextHop().getAddress());
		Assert.assertEquals(2, mp.getNlris().size());

		NetworkLayerReachabilityInformation nlri = mp.getNlris().remove(0);
		
		Assert.assertEquals(12, nlri.getPrefixLength());
		assertArraysEquals(new byte[] { (byte)0xab, 0x10, } , nlri.getPrefix());
		
		nlri = mp.getNlris().remove(0);
		Assert.assertEquals(20, nlri.getPrefixLength());
		assertArraysEquals(new byte[] { (byte)0xc0, (byte)0xa8, (byte)0xf0, } , nlri.getPrefix());
	}

	@Test
	public void testDecodeBogusSafiMpReachNlri() throws Exception {
		completeChannel.writeInbound(buildProtocolPacket(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				0x00, 0x1f, // length 31 octets
				(byte)0x02, // type code UPDATE
				0x00, 0x00, // withdrawn routes length (0 octets)
				0x00, 0x08, // Total path attributes length  (8 octets)
				(byte)0x80, 0x0e, 0x05, // Path Attribute MP_REACH_NLRI
				0x00, (byte)0x01, // AFI(IPv4) 
				0x04, // Bogus SAFI 
				0x00, // NEXT_HOP length 0
				0x00, // reserved
		}));

		assertThat(completeEventHandler.events()).hasSize(1);
		assertThat(completeChannel.outboundMessages()).hasSize(1);

		assertArrayByteBufEquals(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0x00, (byte)0x1d, // length 29 octets
				(byte)0x03, // type code NOTIFICATION
				(byte)0x03, // Update message error
				(byte)0x09, // Invalid Optional Attribute Error
				(byte)0x80, 0x0e, 0x05, // Path Attribute MP_REACH_NLRI
				0x00, (byte)0x01, // AFI(IPv4) 
				0x04, // Bogus SAFI 
				0x00, // NEXT_HOP length 0
				0x00, // reserved
		}, (ByteBuf)completeChannel.readOutbound());
		
		assertThat(completeEventHandler.readEvent()).isInstanceOf(OptionalAttributeErrorNotificationEvent.class);
	}
	
	@Test
	public void testDecodeValidMpUnreachNlriTwoNlri() throws Exception {
		completeChannel.writeInbound(buildProtocolPacket(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				0x00, 0x24, // length 36 octets
				(byte)0x02, // type code 2 (UPDATE) 
				0x00, 0x00, // withdrawn routes length (0 octets)
				0x00, 0x0d, // Total path attributes length  (13 octets)
				(byte)0x80, 0x0f, 0x0a, // Path Attribute MP_REACH_NLRI
				0x00, 0x01, 0x01, // AFI(IPv4) SAFI(UNICAT_ROUTING) 
				0x0c, (byte)0xab, 0x10, //  NLRI 172.16.0.0/12
				0x14, (byte)0xc0, (byte)0xa8, (byte)0xf0, //  NLRI 192.168.255.0/20
		}));

		assertThat(completeChannel.outboundMessages()).hasSize(0);
		assertThat(completeChannel.inboundMessages()).hasSize(1);
	
		UpdatePacket packet = safeDowncast((BGPv4Packet)completeChannel.readInbound(), UpdatePacket.class);
		
		Assert.assertEquals(0, packet.getWithdrawnRoutes().size());
		Assert.assertEquals(1, packet.getPathAttributes().size());
		Assert.assertEquals(0, packet.getNlris().size());		
		
		MultiProtocolUnreachableNLRI mp = (MultiProtocolUnreachableNLRI)packet.getPathAttributes().remove(0);
		
		Assert.assertEquals(AddressFamily.IPv4, mp.getAddressFamily());
		Assert.assertEquals(SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, mp.getSubsequentAddressFamily());
		Assert.assertEquals(2, mp.getNlris().size());

		NetworkLayerReachabilityInformation nlri = mp.getNlris().remove(0);
		
		Assert.assertEquals(12, nlri.getPrefixLength());
		assertArraysEquals(new byte[] { (byte)0xab, 0x10, } , nlri.getPrefix());
		
		nlri = mp.getNlris().remove(0);
		Assert.assertEquals(20, nlri.getPrefixLength());
		assertArraysEquals(new byte[] { (byte)0xc0, (byte)0xa8, (byte)0xf0, } , nlri.getPrefix());
	}

	@Test
	public void testDecodeBogusSafiMpUnreachNlri() throws Exception {
		completeChannel.writeInbound(buildProtocolPacket(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				0x00, 0x1d, // length 29 octets
				(byte)0x02, // type code UPDATE
				0x00, 0x00, // withdrawn routes length (0 octets)
				0x00, 0x06, // Total path attributes length  (6 octets)
				(byte)0x80, 0x0f, 0x03, // Path Attribute MP_REACH_NLRI
				0x00, (byte)0x01, // AFI(IPv4) 
				0x04, // Bogus SAFI 
		}));

		assertThat(completeEventHandler.events()).hasSize(1);
		assertThat(completeChannel.outboundMessages()).hasSize(1);

		assertArrayByteBufEquals(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0x00, (byte)0x1b, // length 27 octets
				(byte)0x03, // type code NOTIFICATION
				(byte)0x03, // Update message error
				(byte)0x09, // Invalid Optional Attribute Error
				(byte)0x80, 0x0f, 0x03, // Path Attribute MP_REACH_NLRI
				0x00, (byte)0x01, // AFI(IPv4) 
				0x04, // Bogus SAFI 
		}, (ByteBuf)completeChannel.readOutbound());
		
		assertThat(completeEventHandler.readEvent()).isInstanceOf(OptionalAttributeErrorNotificationEvent.class);
	}
	
	@Test
	public void testCompleteFullRouteRefreshPacket() throws Exception {
		completeChannel.writeInbound(buildProtocolPacket(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				0x00, 0x24, // length 36 octets
				(byte)0x05, // type code 5 (ROUTE REFRESH)
				(byte)0x00, (byte)0x01, // AFI IPv4
				(byte)0x00, // reserved
				(byte)0x01, // SAFI Unicast forwarding
				(byte)0x01, // IMMEDIATE REFRESH
				(byte)0x40, // Address Prefix Based ORF
				(byte)0x00, (byte)0x9, // ORF entries length 9 octets
				(byte)0x80, // Action REMOVE-ALL
				(byte)0x00, // Action ADD Match PERMIT
				(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x01, // sequence number 1
				(byte)0x0, // min length 0
				(byte)0x00, // max length 0
				(byte)0x0, // prefix 0.0.0.0/0
		}));

		assertThat(completeChannel.outboundMessages()).hasSize(0);
		assertThat(completeChannel.inboundMessages()).hasSize(1);
	
		RouteRefreshPacket packet = safeDowncast((BGPv4Packet)completeChannel.readInbound(), RouteRefreshPacket.class);
		
		Assert.assertEquals(AddressFamily.IPv4, packet.getAddressFamily());
		Assert.assertEquals(SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, packet.getSubsequentAddressFamily());
		
		OutboundRouteFilter orf = packet.getOutboundRouteFilter(); 
		Assert.assertNotNull(orf);
		Assert.assertEquals(AddressFamily.IPv4, orf.getAddressFamily());
		Assert.assertEquals(SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, orf.getSubsequentAddressFamily());
		Assert.assertEquals(ORFRefreshType.IMMEDIATE, orf.getRefreshType());
		Assert.assertEquals(1, orf.getEntries().size());
		
		AddressPrefixBasedORFEntry entry;
		List<ORFEntry> entries = orf.getEntries().get(ORFType.ADDRESS_PREFIX_BASED);

		Assert.assertEquals(2, entries.size());

		entry = (AddressPrefixBasedORFEntry)entries.remove(0);
		Assert.assertEquals(ORFAction.REMOVE_ALL, entry.getAction());
		Assert.assertEquals(ORFMatch.PERMIT, entry.getMatch());
		Assert.assertEquals(0, entry.getSequence());
		Assert.assertEquals(0, entry.getMinLength());
		Assert.assertEquals(0, entry.getMaxLength());
		Assert.assertNull(entry.getPrefix());

		entry = (AddressPrefixBasedORFEntry)entries.remove(0);
		Assert.assertEquals(ORFAction.ADD, entry.getAction());
		Assert.assertEquals(ORFMatch.PERMIT, entry.getMatch());
		Assert.assertEquals(1, entry.getSequence());
		Assert.assertEquals(0, entry.getMinLength());
		Assert.assertEquals(0, entry.getMaxLength());
		Assert.assertEquals(new NetworkLayerReachabilityInformation(0, null), entry.getPrefix());
	}

}
