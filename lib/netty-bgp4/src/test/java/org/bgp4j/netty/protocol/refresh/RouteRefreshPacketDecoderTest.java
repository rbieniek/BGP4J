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
 * File: org.bgp4j.netty.protocol.refresh.RouteRefreshPacketDecoderTest.java 
 */
package org.bgp4j.netty.protocol.refresh;

import java.util.List;

import junit.framework.Assert;

import org.bgp4j.netty.BGPv4Constants.AddressFamily;
import org.bgp4j.netty.BGPv4Constants.SubsequentAddressFamily;
import org.bgp4j.netty.NetworkLayerReachabilityInformation;
import org.bgp4j.netty.protocol.ProtocolPacketTestBase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class RouteRefreshPacketDecoderTest extends ProtocolPacketTestBase {

	private RouteRefreshPacketDecoder decoder;
	
	@Before
	public void before() {
		decoder = obtainInstance(RouteRefreshPacketDecoder.class);
	}
	
	@After
	public void afer() {
		decoder = null;
	}
	
	@Test
	public void testSimpleRouteRefreshPacket() {
		RouteRefreshPacket packet = safeDowncast(decoder.decodeRouteRefreshPacket(buildProtocolPacket(new byte[] {
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				// (byte)0x00, (byte)0x35, // length 53 octets 
				// (byte)0x05, // type code 5 (ROUTE REFRESH)
				(byte)0x00, (byte)0x01, // AFI IPv4
				(byte)0x00, // reserved
				(byte)0x01, // SAFI Unicast forwarding
		})), RouteRefreshPacket.class);

		Assert.assertEquals(AddressFamily.IPv4, packet.getAddressFamily());
		Assert.assertEquals(SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, packet.getSubsequentAddressFamily());
		Assert.assertNull(packet.getOutboundRouteFilter());
	}

	@Test
	public void testMinimalORFRouteRefreshPacket() {
		RouteRefreshPacket packet = safeDowncast(decoder.decodeRouteRefreshPacket(buildProtocolPacket(new byte[] {
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				// (byte)0x00, (byte)0x35, // length 53 octets 
				// (byte)0x05, // type code 5 (ROUTE REFRESH)
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
		})), RouteRefreshPacket.class);

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
	
	@Test
	public void testCompleteRouteRefreshPacket() {
		RouteRefreshPacket packet = safeDowncast(decoder.decodeRouteRefreshPacket(buildProtocolPacket(new byte[] {
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				// (byte)0x00, (byte)0x35, // length 53 octets 
				// (byte)0x05, // type code 5 (ROUTE REFRESH)
				(byte)0x00, (byte)0x01, // AFI IPv4
				(byte)0x00, // reserved
				(byte)0x01, // SAFI Unicast forwarding
				(byte)0x01, // IMMEDIATE REFRESH
				(byte)0x40, // Address Prefix Based ORF
				(byte)0x00, (byte)0x2C, // ORF entries length 44 octets
				(byte)0x80, // Action REMOVE-ALL
				(byte)0x00, // Action ADD Match PERMIT
				(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x01, // sequence number 1
				(byte)0x10, // min length 16
				(byte)0x20, // max length 32
				(byte)0x10, (byte)0xac, (byte)0x10, // prefix 172.16.0.0/16
				(byte)0x40, // Action REMOVE Match PERMIT
				(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x02, // sequence number 2
				(byte)0x18, // min length 24
				(byte)0x1c, // max length 28
				(byte)0x18, (byte)0xc0, (byte)0xa8, 0x10, // prefix 192.168.16.0/24
				(byte)0x20, // Action ADD Match DENY
				(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x03, // sequence number 3
				(byte)0x18, // min length 24
				(byte)0x1c, // max length 28
				(byte)0x18, (byte)0xc0, (byte)0xa8, 0x20, // prefix 192.168.32.0/24
				(byte)0x60, // Action REMOVE Match DENY
				(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x04, // sequence number 4
				(byte)0x18, // min length 24
				(byte)0x1c, // max length 28
				(byte)0x18, (byte)0xc0, (byte)0xa8, 0x30, // prefix 192.168.48.0/24
				
		})), RouteRefreshPacket.class);

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

		Assert.assertEquals(5, entries.size());

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
		Assert.assertEquals(16, entry.getMinLength());
		Assert.assertEquals(32, entry.getMaxLength());
		Assert.assertEquals(new NetworkLayerReachabilityInformation(16, new byte[] { (byte)0xac, 0x10, }), entry.getPrefix());

		entry = (AddressPrefixBasedORFEntry)entries.remove(0);
		Assert.assertEquals(ORFAction.REMOVE, entry.getAction());
		Assert.assertEquals(ORFMatch.PERMIT, entry.getMatch());
		Assert.assertEquals(2, entry.getSequence());
		Assert.assertEquals(24, entry.getMinLength());
		Assert.assertEquals(28, entry.getMaxLength());
		Assert.assertEquals(new NetworkLayerReachabilityInformation(24, new byte[] { (byte)0xc0, (byte)0xa8, 0x10, }), entry.getPrefix());

		entry = (AddressPrefixBasedORFEntry)entries.remove(0);
		Assert.assertEquals(ORFAction.ADD, entry.getAction());
		Assert.assertEquals(ORFMatch.DENY, entry.getMatch());
		Assert.assertEquals(3, entry.getSequence());
		Assert.assertEquals(24, entry.getMinLength());
		Assert.assertEquals(28, entry.getMaxLength());
		Assert.assertEquals(new NetworkLayerReachabilityInformation(24, new byte[] { (byte)0xc0, (byte)0xa8, 0x20, }), entry.getPrefix());

		entry = (AddressPrefixBasedORFEntry)entries.remove(0);
		Assert.assertEquals(ORFAction.REMOVE, entry.getAction());
		Assert.assertEquals(ORFMatch.DENY, entry.getMatch());
		Assert.assertEquals(4, entry.getSequence());
		Assert.assertEquals(24, entry.getMinLength());
		Assert.assertEquals(28, entry.getMaxLength());
		Assert.assertEquals(new NetworkLayerReachabilityInformation(24, new byte[] { (byte)0xc0, (byte)0xa8, 0x30, }), entry.getPrefix());
	}
	
	@Test
	public void testBrokenAFIMinimalORFRouteRefreshPacket() {
		Assert.assertNull(decoder.decodeRouteRefreshPacket(buildProtocolPacket(new byte[] {
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				// (byte)0x00, (byte)0x35, // length 53 octets 
				// (byte)0x05, // type code 5 (ROUTE REFRESH)
				(byte)0x01, (byte)0x01, // AFI IPv4 <<-- Broken
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
		})));
	}
		
	@Test
	public void testBrokenSAFIMinimalORFRouteRefreshPacket() {
		Assert.assertNull(decoder.decodeRouteRefreshPacket(buildProtocolPacket(new byte[] {
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				// (byte)0x00, (byte)0x35, // length 53 octets 
				// (byte)0x05, // type code 5 (ROUTE REFRESH)
				(byte)0x00, (byte)0x01, // AFI IPv4 
				(byte)0x00, // reserved
				(byte)0x05, // SAFI Unicast forwarding <<-- Broken
				(byte)0x01, // IMMEDIATE REFRESH
				(byte)0x40, // Address Prefix Based ORF
				(byte)0x00, (byte)0x9, // ORF entries length 9 octets
				(byte)0x80, // Action REMOVE-ALL
				(byte)0x00, // Action ADD Match PERMIT
				(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x01, // sequence number 1
				(byte)0x0, // min length 0
				(byte)0x00, // max length 0
				(byte)0x0, // prefix 0.0.0.0/0
		})));
	}
	
	@Test
	public void testBrokenRefresMinimalORFRouteRefreshPacket() {
		Assert.assertNull(decoder.decodeRouteRefreshPacket(buildProtocolPacket(new byte[] {
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				// (byte)0x00, (byte)0x35, // length 53 octets 
				// (byte)0x05, // type code 5 (ROUTE REFRESH)
				(byte)0x00, (byte)0x01, // AFI IPv4 
				(byte)0x00, // reserved
				(byte)0x01, // SAFI Unicast forwarding 
				(byte)0x03, // IMMEDIATE REFRESH <<-- Broken
				(byte)0x40, // Address Prefix Based ORF
				(byte)0x00, (byte)0x9, // ORF entries length 9 octets
				(byte)0x80, // Action REMOVE-ALL
				(byte)0x00, // Action ADD Match PERMIT
				(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x01, // sequence number 1
				(byte)0x0, // min length 0
				(byte)0x00, // max length 0
				(byte)0x0, // prefix 0.0.0.0/0
		})));
	}

	@Test
	public void testBrokenRefreshMinimalORFRouteRefreshPacket() {
		Assert.assertNull(decoder.decodeRouteRefreshPacket(buildProtocolPacket(new byte[] {
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				// (byte)0x00, (byte)0x35, // length 53 octets 
				// (byte)0x05, // type code 5 (ROUTE REFRESH)
				(byte)0x00, (byte)0x01, // AFI IPv4 
				(byte)0x00, // reserved
				(byte)0x01, // SAFI Unicast forwarding
				(byte)0x03, // IMMEDIATE REFRESH <<-- Broken
				(byte)0x40, // Address Prefix Based ORF
				(byte)0x00, (byte)0x9, // ORF entries length 9 octets
				(byte)0x80, // Action REMOVE-ALL
				(byte)0x00, // Action ADD Match PERMIT
				(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x01, // sequence number 1
				(byte)0x0, // min length 0
				(byte)0x00, // max length 0
				(byte)0x0, // prefix 0.0.0.0/0
		})));
	}
		
	@Test
	public void testBrokenTypeAFIMinimalORFRouteRefreshPacket() {
		Assert.assertNull(decoder.decodeRouteRefreshPacket(buildProtocolPacket(new byte[] {
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				// (byte)0x00, (byte)0x35, // length 53 octets 
				// (byte)0x05, // type code 5 (ROUTE REFRESH)
				(byte)0x00, (byte)0x01, // AFI IPv4 
				(byte)0x00, // reserved
				(byte)0x01, // SAFI Unicast forwarding
				(byte)0x01, // IMMEDIATE REFRESH
				(byte)0x41, // Address Prefix Based ORF <<-- Broken
				(byte)0x00, (byte)0x9, // ORF entries length 9 octets
				(byte)0x80, // Action REMOVE-ALL
				(byte)0x00, // Action ADD Match PERMIT
				(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x01, // sequence number 1
				(byte)0x0, // min length 0
				(byte)0x00, // max length 0
				(byte)0x0, // prefix 0.0.0.0/0
		})));
	}
	
	@Test
	public void testBrokenLengthMinimalORFRouteRefreshPacket() {
		Assert.assertNull(decoder.decodeRouteRefreshPacket(buildProtocolPacket(new byte[] {
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				// (byte)0x00, (byte)0x35, // length 53 octets 
				// (byte)0x05, // type code 5 (ROUTE REFRESH)
				(byte)0x00, (byte)0x01, // AFI IPv4 <<-- Broken
				(byte)0x00, // reserved
				(byte)0x01, // SAFI Unicast forwarding
				(byte)0x01, // IMMEDIATE REFRESH
				(byte)0x40, // Address Prefix Based ORF
				(byte)0x00, (byte)0xa, // ORF entries length 9 octets <<-- Broken
				(byte)0x80, // Action REMOVE-ALL
				(byte)0x00, // Action ADD Match PERMIT
				(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x01, // sequence number 1
				(byte)0x0, // min length 0
				(byte)0x00, // max length 0
				(byte)0x0, // prefix 0.0.0.0/0
		})));
	}
	
	@Test
	public void testBrokenActionMinimalORFRouteRefreshPacket() {
		Assert.assertNull(decoder.decodeRouteRefreshPacket(buildProtocolPacket(new byte[] {
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				// (byte)0x00, (byte)0x35, // length 53 octets 
				// (byte)0x05, // type code 5 (ROUTE REFRESH)
				(byte)0x00, (byte)0x01, // AFI IPv4 <<-- Broken
				(byte)0x00, // reserved
				(byte)0x01, // SAFI Unicast forwarding
				(byte)0x01, // IMMEDIATE REFRESH
				(byte)0x40, // Address Prefix Based ORF
				(byte)0x00, (byte)0x9, // ORF entries length 9 octets
				(byte)0xc0, // Action REMOVE-ALL <<-- broken
				(byte)0x00, // Action ADD Match PERMIT
				(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x01, // sequence number 1
				(byte)0x0, // min length 0
				(byte)0x00, // max length 0
				(byte)0x0, // prefix 0.0.0.0/0
		})));
	}
}
