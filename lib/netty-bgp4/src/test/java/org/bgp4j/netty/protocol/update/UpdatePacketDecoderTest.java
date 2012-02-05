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
 * File: org.bgp4j.netty.protocol.update.UpdatePacketDecoderTest.java 
 */
package org.bgp4j.netty.protocol.update;

import java.net.Inet4Address;

import junit.framework.Assert;

import org.bgp4j.netty.BGPv4Constants;
import org.bgp4j.netty.NetworkLayerReachabilityInformation;
import org.bgp4j.netty.protocol.ASType;
import org.bgp4j.netty.protocol.BadMessageLengthException;
import org.bgp4j.netty.protocol.ProtocolPacketTestBase;
import org.bgp4j.netty.protocol.update.ASPathAttribute.PathType;
import org.bgp4j.netty.protocol.update.OriginPathAttribute.Origin;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class UpdatePacketDecoderTest extends ProtocolPacketTestBase {
	private UpdatePacketDecoder decoder;
	
	@Before
	public void before() {
		decoder = obtainInstance(UpdatePacketDecoder.class);
	}
	
	@After
	public void after() {
		decoder = null;
	}

	@Test
	public void testEmptyUpdatePacket() {
		UpdatePacket packet = safeDowncast(decoder.decodeUpdatePacket(buildProtocolPacket(new byte[] {
				// (byte)0x03, // type code UPDATE
				0x00, 0x00, // withdrawn routes length (0 octets)
				0x00, 0x00, // Total path attributes length  (0 octets)
		})), UpdatePacket.class);

		Assert.assertEquals(2, packet.getType());
		Assert.assertEquals(0, packet.getWithdrawnRoutes().size());
		Assert.assertEquals(0, packet.getPathAttributes().size());
		Assert.assertEquals(0, packet.getNlris().size());
	}	
	
	@Test
	public void testCompletePacket() throws Exception {
		UpdatePacket packet = safeDowncast(decoder.decodeUpdatePacket(buildProtocolPacket(new byte[] {
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				// (byte)0x00, (byte)0x35, // length 53 octets 
				// (byte)0x02, // type code 2 (UPDATE) 
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x1d, // path attributes length (29 octets)
				(byte)0x40, (byte)0x01, (byte)0x01, (byte)0x02, // Path attribute: ORIGIN INCOMPLETE  
				(byte)0x50, (byte)0x02, (byte)0x00, (byte)0x00, // Path attribute: AS_PATH emtpy 
				(byte)0x40, (byte)0x03, (byte)0x04, (byte)0xc0, (byte)0xa8, (byte)0x04, (byte)0x02, // Path attribute: NEXT_HOP 192.168.4.2
				(byte)0x80, (byte)0x04, (byte)0x04, (byte)0x00, (byte)0x00, (byte)0x08, (byte)0x00, // Path attribute: MULT_EXIT_DISC 2048
				(byte)0x40, (byte)0x05, (byte)0x04, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x64, // Path attribute: LOCAL_PREF 100
				(byte)0x00 // NLRI: 0.0.0.0/0	
		})), UpdatePacket.class);
		
		Assert.assertEquals(2, packet.getType());
		Assert.assertEquals(0, packet.getWithdrawnRoutes().size());
		Assert.assertEquals(5, packet.getPathAttributes().size());
		Assert.assertEquals(1, packet.getNlris().size());		
		
		OriginPathAttribute origin = (OriginPathAttribute)packet.getPathAttributes().remove(0);
		ASPathAttribute asPath = (ASPathAttribute)packet.getPathAttributes().remove(0);
		NextHopPathAttribute nextHop = (NextHopPathAttribute)packet.getPathAttributes().remove(0);
		MultiExitDiscPathAttribute multiExitDisc = (MultiExitDiscPathAttribute)packet.getPathAttributes().remove(0);
		LocalPrefPathAttribute localPref = (LocalPrefPathAttribute)packet.getPathAttributes().remove(0);
		NetworkLayerReachabilityInformation nlri = packet.getNlris().remove(0);
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_ORIGIN, origin.getTypeCode());
		Assert.assertEquals(Origin.INCOMPLETE, origin.getOrigin());
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS_PATH, asPath.getTypeCode());
		Assert.assertEquals(ASType.AS_NUMBER_2OCTETS, asPath.getAsType());
		Assert.assertEquals(PathType.AS_SEQUENCE, asPath.getPathType());
		Assert.assertEquals(0, asPath.getAses().size());
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_NEXT_HOP, nextHop.getTypeCode());
		Assert.assertEquals(Inet4Address.getByAddress(new byte[] {(byte)0xc0, (byte)0xa8, (byte)0x04, (byte)0x02 }), nextHop.getNextHop());
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_MULTI_EXIT_DISC, multiExitDisc.getTypeCode());
		Assert.assertEquals(2048, multiExitDisc.getDiscriminator());
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_LOCAL_PREF, localPref.getTypeCode());
		Assert.assertEquals(100, localPref.getLocalPreference());
		
		Assert.assertEquals(0, nlri.getPrefixLength());
		Assert.assertNull(nlri.getPrefix());
	}
	
	@Test
	public void testWithdrawnDefaultPrefixUpdatePacket() {
		UpdatePacket packet = safeDowncast(decoder.decodeUpdatePacket(buildProtocolPacket(new byte[] {
				// (byte)0x03, // type code UPDATE
				0x00, 0x01, // withdrawn routes length (1 octets)
				0x00,       // withdrawn 0/0 prefix
				0x00, 0x00, // Total path attributes length  (0 octets)
		})), UpdatePacket.class);

		Assert.assertEquals(2, packet.getType());
		Assert.assertEquals(0, packet.getPathAttributes().size());
		Assert.assertEquals(0, packet.getNlris().size());
		
		Assert.assertEquals(1, packet.getWithdrawnRoutes().size());
		Assert.assertEquals(packet.getWithdrawnRoutes().remove(0), new NetworkLayerReachabilityInformation(0, null));
	}	
	
	@Test
	public void testWithdrawnFourBitPrefixPrefixUpdatePacket() {
		UpdatePacket packet = safeDowncast(decoder.decodeUpdatePacket(buildProtocolPacket(new byte[] {
				// (byte)0x03, // type code UPDATE
				0x00, 0x02, // withdrawn routes length (1 octets)
				0x04, (byte)0xc0, // withdrawn 192.0.0.0/4 prefix
				0x00, 0x00, // Total path attributes length  (0 octets)
		})), UpdatePacket.class);

		Assert.assertEquals(2, packet.getType());
		Assert.assertEquals(0, packet.getPathAttributes().size());
		Assert.assertEquals(0, packet.getNlris().size());
		
		Assert.assertEquals(1, packet.getWithdrawnRoutes().size());
		Assert.assertEquals(packet.getWithdrawnRoutes().remove(0), new NetworkLayerReachabilityInformation(4, new byte[] { 
				(byte)0xc0 
		}));
	}	
	
	@Test
	public void testWithdrawnEightBitPrefixPrefixUpdatePacket() {
		UpdatePacket packet = safeDowncast(decoder.decodeUpdatePacket(buildProtocolPacket(new byte[] {
				// (byte)0x03, // type code UPDATE
				0x00, 0x02, // withdrawn routes length (1 octets)
				0x08, (byte)0xc8, // withdrawn 200.0.0.0/8 prefix
				0x00, 0x00, // Total path attributes length  (0 octets)
		})), UpdatePacket.class);

		Assert.assertEquals(2, packet.getType());
		Assert.assertEquals(0, packet.getPathAttributes().size());
		Assert.assertEquals(0, packet.getNlris().size());
		
		Assert.assertEquals(1, packet.getWithdrawnRoutes().size());
		Assert.assertEquals(packet.getWithdrawnRoutes().remove(0), new NetworkLayerReachabilityInformation(8, new byte[] { 
				(byte)0xc8 
		}));
	}	
	
	@Test
	public void testWithdrawnTwelveBitPrefixPrefixUpdatePacket() {
		UpdatePacket packet = safeDowncast(decoder.decodeUpdatePacket(buildProtocolPacket(new byte[] {
				// (byte)0x03, // type code UPDATE
				0x00, 0x03, // withdrawn routes length (1 octets)
				0x0c, (byte)0xc0, (byte)0xe0, // withdrawn 192.224.0.0/12 prefix
				0x00, 0x00, // Total path attributes length  (0 octets)
		})), UpdatePacket.class);

		Assert.assertEquals(2, packet.getType());
		Assert.assertEquals(0, packet.getPathAttributes().size());
		Assert.assertEquals(0, packet.getNlris().size());
		
		Assert.assertEquals(1, packet.getWithdrawnRoutes().size());
		Assert.assertEquals(packet.getWithdrawnRoutes().remove(0), new NetworkLayerReachabilityInformation(12, new byte[] { 
				(byte)0xc0, (byte)0xe0
		}));
	}	
	
	@Test
	public void testWithdrawnSixteenBitPrefixPrefixUpdatePacket() {
		UpdatePacket packet = safeDowncast(decoder.decodeUpdatePacket(buildProtocolPacket(new byte[] {
				// (byte)0x03, // type code UPDATE
				0x00, 0x03, // withdrawn routes length (1 octets)
				0x10, (byte)0xc0, (byte)0xef, // withdrawn 192.239.0.0/12 prefix
				0x00, 0x00, // Total path attributes length  (0 octets)
		})), UpdatePacket.class);

		Assert.assertEquals(2, packet.getType());
		Assert.assertEquals(0, packet.getPathAttributes().size());
		Assert.assertEquals(0, packet.getNlris().size());
		
		Assert.assertEquals(1, packet.getWithdrawnRoutes().size());
		Assert.assertEquals(packet.getWithdrawnRoutes().remove(0), new NetworkLayerReachabilityInformation(16, new byte[] { 
				(byte)0xc0, (byte)0xef 
		}));
	}	
	
	@Test
	public void testMalformedWithdrawnRoutesNopathAttributeListUpdatePacket() throws Exception {
		(new AssertExecption() {
			
			@Override
			protected void doExecute() {
				decoder.decodeUpdatePacket(buildProtocolPacket(new byte[] {
						// (byte)0x03, // type code UPDATE
						0x00, 0x00, // bad withdrawn routes length (2 octets), points to end of packet 
				}));
			}
		}).execute(BadMessageLengthException.class);
	}

	@Test
	public void testMalformedWithdrawnRoutesLengthOnPacketEndUpdatePacket() throws Exception {
		(new AssertExecption() {
			
			@Override
			protected void doExecute() {
				decoder.decodeUpdatePacket(buildProtocolPacket(new byte[] {
						// (byte)0x03, // type code UPDATE
						0x00, 0x02, // bad withdrawn routes length (2 octets), points to end of packet 
						0x00, 0x00, // Total path attributes length  (0 octets)
				}));
			}
		}).execute(MalformedAttributeListException.class);
	}
		
	@Test
	public void testMalformedWithdrawnRoutesLengthOverEndUpdatePacket() throws Exception {
		(new AssertExecption() {
			
			@Override
			protected void doExecute() {
				decoder.decodeUpdatePacket(buildProtocolPacket(new byte[] {
						// (byte)0x03, // type code UPDATE
						0x00, 0x04, // bad withdrawn routes length (4 octets), points beyond end of packet 
						0x00, 0x00, // Total path attributes length  (0 octets)
				}));
			}
		}).execute(MalformedAttributeListException.class);
	}
	
	@Test
	public void testAttributeListTooLongUpdatePacket() throws Exception {
		(new AssertExecption() {
			
			@Override
			protected void doExecute() {
				decoder.decodeUpdatePacket(buildProtocolPacket(new byte[] {
						// (byte)0x03, // type code UPDATE
						0x00, 0x00, // withdrawn routes length (0 octets) 
						0x00, 0x02, // Total path attributes length  (2 octets), points to end of packet
				}));
			}
		}).execute(MalformedAttributeListException.class);
	}

}
