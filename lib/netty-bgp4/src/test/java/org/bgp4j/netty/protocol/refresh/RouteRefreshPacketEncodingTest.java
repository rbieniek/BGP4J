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
 * File: org.bgp4j.netty.protocol.refresh.RouteRefreshPacketEncodingTest.java 
 */
package org.bgp4j.netty.protocol.refresh;

import org.bgp4j.net.AddressFamily;
import org.bgp4j.net.AddressPrefixBasedORFEntry;
import org.bgp4j.net.NetworkLayerReachabilityInformation;
import org.bgp4j.net.ORFAction;
import org.bgp4j.net.ORFEntry;
import org.bgp4j.net.ORFMatch;
import org.bgp4j.net.ORFRefreshType;
import org.bgp4j.net.OutboundRouteFilter;
import org.bgp4j.net.SubsequentAddressFamily;
import org.bgp4j.netty.BGPv4TestBase;
import org.junit.Test;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class RouteRefreshPacketEncodingTest extends BGPv4TestBase {
	
	@Test
	public void testEncodeSimpleRouteRefreshPacket() throws Exception {
		RouteRefreshPacket packet = new RouteRefreshPacket(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING);
		
		assertBufferContents(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0x00, (byte)0x17, // length 23 octets
				(byte)0x05, // ROUTE REFRESH
				(byte)0x00, (byte)0x01, // AFI IPv4
				(byte)0x00, // reserved
				(byte)0x01, // SAFI Unicast forwarding
		}, packet.encodePacket());
	}

	@Test
	public void testEncodeMinimalORFRouteRefreshPacket() throws Exception {
		RouteRefreshPacket packet = new RouteRefreshPacket(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, 
				new OutboundRouteFilter(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, ORFRefreshType.IMMEDIATE, new ORFEntry[] {
						new AddressPrefixBasedORFEntry(ORFAction.REMOVE_ALL, ORFMatch.PERMIT),
						new AddressPrefixBasedORFEntry(ORFAction.ADD, ORFMatch.PERMIT, 1, 0, 0, new NetworkLayerReachabilityInformation(0, null)),
				}));

		assertBufferContents(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0x00, (byte)0x24, // length 36 octets
				(byte)0x05, // ROUTE REFRESH
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
		}, packet.encodePacket());
	}

	@Test
	public void testEncodeThreeEntryORFRouteRefreshPacket() throws Exception {
		RouteRefreshPacket packet = new RouteRefreshPacket(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, 
				new OutboundRouteFilter(AddressFamily.IPv4, 
						SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, 
						ORFRefreshType.IMMEDIATE, new ORFEntry[] {
						new AddressPrefixBasedORFEntry(ORFAction.REMOVE_ALL, 
								ORFMatch.PERMIT),
						new AddressPrefixBasedORFEntry(ORFAction.ADD, 
								ORFMatch.PERMIT, 
								1, 
								16, 
								32, 
								new NetworkLayerReachabilityInformation(16, 
										new byte[] { (byte)0xab, 0x10, } )),
						new AddressPrefixBasedORFEntry(ORFAction.ADD, 
								ORFMatch.DENY, 
								2, 
								24, 
								32, 
								new NetworkLayerReachabilityInformation(24, 
										new byte[] { (byte)0xc0, (byte)0xa8, 0x10, } )),
				}));

		assertBufferContents(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0x00, (byte)0x31, // length 49 octets
				(byte)0x05, // ROUTE REFRESH
				(byte)0x00, (byte)0x01, // AFI IPv4
				(byte)0x00, // reserved
				(byte)0x01, // SAFI Unicast forwarding
				(byte)0x01, // IMMEDIATE REFRESH
				(byte)0x40, // Address Prefix Based ORF
				(byte)0x00, (byte)0x16, // ORF entries length 22 octets
				(byte)0x80, // Action REMOVE-ALL
				(byte)0x00, // Action ADD Match PERMIT
				(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x01, // sequence number 1
				(byte)0x10, // min length 16
				(byte)0x20, // max length 32
				(byte)0x10, (byte)0xab, 0x10, // prefix 172.16.0.0/16
				(byte)0x20, // Action ADD Match DENY
				(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x02, // sequence number 2
				(byte)0x18, // min length 24
				(byte)0x20, // max length 32
				(byte)0x18, (byte)0xc0, (byte)0xa8, 0x10, // prefix 192.168.10.0/16
		}, packet.encodePacket());
	}
}
