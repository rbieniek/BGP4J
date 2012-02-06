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
		Assert.assertEquals(0, asPath.getPathSegments().size());
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

	@Test
	public void testOriginIgpPacket() throws Exception {
		UpdatePacket packet = safeDowncast(decoder.decodeUpdatePacket(buildProtocolPacket(new byte[] {
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				// (byte)0x00, (byte)0x35, // length 53 octets 
				// (byte)0x02, // type code 2 (UPDATE) 
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x04, // path attributes length (29 octets)
				(byte)0x40, (byte)0x01, (byte)0x01, (byte)0x00, // Path attribute: ORIGIN IGP  
		})), UpdatePacket.class);
		
		Assert.assertEquals(2, packet.getType());
		Assert.assertEquals(0, packet.getWithdrawnRoutes().size());
		Assert.assertEquals(1, packet.getPathAttributes().size());
		Assert.assertEquals(0, packet.getNlris().size());		
		
		OriginPathAttribute origin = (OriginPathAttribute)packet.getPathAttributes().remove(0);
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_ORIGIN, origin.getTypeCode());
		Assert.assertEquals(Origin.IGP, origin.getOrigin());
	}
	
	@Test
	public void testOriginEgpPacket() throws Exception {
		UpdatePacket packet = safeDowncast(decoder.decodeUpdatePacket(buildProtocolPacket(new byte[] {
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				// (byte)0x00, (byte)0x35, // length 53 octets 
				// (byte)0x02, // type code 2 (UPDATE) 
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x04, // path attributes length (29 octets)
				(byte)0x40, (byte)0x01, (byte)0x01, (byte)0x01, // Path attribute: ORIGIN EGP  
		})), UpdatePacket.class);
		
		Assert.assertEquals(2, packet.getType());
		Assert.assertEquals(0, packet.getWithdrawnRoutes().size());
		Assert.assertEquals(1, packet.getPathAttributes().size());
		Assert.assertEquals(0, packet.getNlris().size());		
		
		OriginPathAttribute origin = (OriginPathAttribute)packet.getPathAttributes().remove(0);
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_ORIGIN, origin.getTypeCode());
		Assert.assertEquals(Origin.EGP, origin.getOrigin());
	}
	
	@Test
	public void testOriginIncompletePacket() throws Exception {
		UpdatePacket packet = safeDowncast(decoder.decodeUpdatePacket(buildProtocolPacket(new byte[] {
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				// (byte)0x00, (byte)0x35, // length 53 octets 
				// (byte)0x02, // type code 2 (UPDATE) 
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x04, // path attributes length (29 octets)
				(byte)0x40, (byte)0x01, (byte)0x01, (byte)0x02, // Path attribute: ORIGIN INCOMPLETE  
		})), UpdatePacket.class);
		
		Assert.assertEquals(2, packet.getType());
		Assert.assertEquals(0, packet.getWithdrawnRoutes().size());
		Assert.assertEquals(1, packet.getPathAttributes().size());
		Assert.assertEquals(0, packet.getNlris().size());		
		
		OriginPathAttribute origin = (OriginPathAttribute)packet.getPathAttributes().remove(0);
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_ORIGIN, origin.getTypeCode());
		Assert.assertEquals(Origin.INCOMPLETE, origin.getOrigin());
	}
	
	@Test
	public void testOriginInvalidPacket() throws Exception {
		(new AssertExecption() {
			
			@Override
			protected void doExecute() {
				safeDowncast(decoder.decodeUpdatePacket(buildProtocolPacket(new byte[] {
						// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
						// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
						// (byte)0x00, (byte)0x35, // length 53 octets 
						// (byte)0x02, // type code 2 (UPDATE) 
						(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
						(byte)0x00, (byte)0x04, // path attributes length (29 octets)
						(byte)0x40, (byte)0x01, (byte)0x01, (byte)0x04, // Path attribute: ORIGIN INCOMPLETE  
				})), UpdatePacket.class);
			}
		}).execute(InvalidOriginException.class);
	}

	@Test
	public void testOriginShortPacket() throws Exception {
		(new AssertExecption() {
			
			@Override
			protected void doExecute() {
				safeDowncast(decoder.decodeUpdatePacket(buildProtocolPacket(new byte[] {
						// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
						// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
						// (byte)0x00, (byte)0x35, // length 53 octets 
						// (byte)0x02, // type code 2 (UPDATE) 
						(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
						(byte)0x00, (byte)0x03, // path attributes length (29 octets)
						(byte)0x40, (byte)0x01, (byte)0x00, // Path attribute:   
				})), UpdatePacket.class);
			}
		}).execute(AttributeLengthException.class);
	}

	@Test
	public void testASPath4EmptyPacket() throws Exception {
		UpdatePacket packet = safeDowncast(decoder.decodeUpdatePacket(buildProtocolPacket(new byte[] {
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				// (byte)0x00, (byte)0x35, // length 53 octets 
				// (byte)0x02, // type code 2 (UPDATE) 
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x04, // path attributes length (4 octets)
				(byte)0x50, (byte)0x11, (byte)0x00, (byte)0x00, // Path attribute: AS4_PATH emtpy 
		})), UpdatePacket.class);
		
		Assert.assertEquals(2, packet.getType());
		Assert.assertEquals(0, packet.getWithdrawnRoutes().size());
		Assert.assertEquals(1, packet.getPathAttributes().size());
		Assert.assertEquals(0, packet.getNlris().size());		
		
		ASPathAttribute asPath = (ASPathAttribute)packet.getPathAttributes().remove(0);
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS4_PATH, asPath.getTypeCode());
		Assert.assertEquals(ASType.AS_NUMBER_4OCTETS, asPath.getAsType());
		Assert.assertEquals(0, asPath.getPathSegments().size());
	}

	@Test
	public void testASPath4ASSequenceOneASNumberPacket() throws Exception {
		UpdatePacket packet = safeDowncast(decoder.decodeUpdatePacket(buildProtocolPacket(new byte[] {
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				// (byte)0x00, (byte)0x35, // length 53 octets 
				// (byte)0x02, // type code 2 (UPDATE) 
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x0a, // path attributes length (10 octets)
				(byte)0x50, (byte)0x11, (byte)0x00, (byte)0x06, // Path attribute: 6 octets AS_PATH 
				0x02, 0x01, 0x00, 0x00, 0x12, 0x34, // AS_SEQUENCE 0x00001234 
		})), UpdatePacket.class);
		
		Assert.assertEquals(2, packet.getType());
		Assert.assertEquals(0, packet.getWithdrawnRoutes().size());
		Assert.assertEquals(1, packet.getPathAttributes().size());
		Assert.assertEquals(0, packet.getNlris().size());		
		
		ASPathAttribute asPath = (ASPathAttribute)packet.getPathAttributes().remove(0);
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS4_PATH, asPath.getTypeCode());
		Assert.assertEquals(ASType.AS_NUMBER_4OCTETS, asPath.getAsType());
		Assert.assertEquals(1, asPath.getPathSegments().size());
		
		ASPathAttribute.PathSegment segment = asPath.getPathSegments().remove(0);
		
		Assert.assertEquals(ASPathAttribute.PathSegmentType.AS_SEQUENCE, segment.getPathSegmentType());
		Assert.assertEquals(1, segment.getAses().size());
		Assert.assertEquals((Integer)0x1234, segment.getAses().remove(0));
	}

	@Test
	public void testASPath4ASSequenceTwoASNumberPacket() throws Exception {
		UpdatePacket packet = safeDowncast(decoder.decodeUpdatePacket(buildProtocolPacket(new byte[] {
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				// (byte)0x00, (byte)0x35, // length 53 octets 
				// (byte)0x02, // type code 2 (UPDATE) 
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x0e, // path attributes length (14 octets)
				(byte)0x50, (byte)0x11, (byte)0x00, (byte)0x0a, // Path attribute: 10 octets AS4_PATH 
				0x02, 0x02, 0x00, 0x00, 0x12, 0x34, 0x00, 0x00, 0x56, 0x78, // AS_SEQUENCE 0x1234 0x5678 
		})), UpdatePacket.class);
		
		Assert.assertEquals(2, packet.getType());
		Assert.assertEquals(0, packet.getWithdrawnRoutes().size());
		Assert.assertEquals(1, packet.getPathAttributes().size());
		Assert.assertEquals(0, packet.getNlris().size());		
		
		ASPathAttribute asPath = (ASPathAttribute)packet.getPathAttributes().remove(0);
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS4_PATH, asPath.getTypeCode());
		Assert.assertEquals(ASType.AS_NUMBER_4OCTETS, asPath.getAsType());
		Assert.assertEquals(1, asPath.getPathSegments().size());
		
		ASPathAttribute.PathSegment segment = asPath.getPathSegments().remove(0);
		
		Assert.assertEquals(ASPathAttribute.PathSegmentType.AS_SEQUENCE, segment.getPathSegmentType());
		Assert.assertEquals(2, segment.getAses().size());
		Assert.assertEquals((Integer)0x1234, segment.getAses().remove(0));
		Assert.assertEquals((Integer)0x5678, segment.getAses().remove(0));
	}

	@Test
	public void testASPath4ASSetOneASNumberPacket() throws Exception {
		UpdatePacket packet = safeDowncast(decoder.decodeUpdatePacket(buildProtocolPacket(new byte[] {
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				// (byte)0x00, (byte)0x35, // length 53 octets 
				// (byte)0x02, // type code 2 (UPDATE) 
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x0a, // path attributes length (10 octets)
				(byte)0x50, (byte)0x11, (byte)0x00, (byte)0x06, // Path attribute: 6 octets AS4_PATH 
				0x01, 0x01, 0x00, 0x00, 0x12, 0x34, // AS_SET 0x1234 
		})), UpdatePacket.class);
		
		Assert.assertEquals(2, packet.getType());
		Assert.assertEquals(0, packet.getWithdrawnRoutes().size());
		Assert.assertEquals(1, packet.getPathAttributes().size());
		Assert.assertEquals(0, packet.getNlris().size());		
		
		ASPathAttribute asPath = (ASPathAttribute)packet.getPathAttributes().remove(0);
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS4_PATH, asPath.getTypeCode());
		Assert.assertEquals(ASType.AS_NUMBER_4OCTETS, asPath.getAsType());
		Assert.assertEquals(1, asPath.getPathSegments().size());
		
		ASPathAttribute.PathSegment segment = asPath.getPathSegments().remove(0);
		
		Assert.assertEquals(ASPathAttribute.PathSegmentType.AS_SET, segment.getPathSegmentType());
		Assert.assertEquals(1, segment.getAses().size());
		Assert.assertEquals((Integer)0x1234, segment.getAses().remove(0));
	}

	@Test
	public void testASPath4ASSetTwoASNumberPacket() throws Exception {
		UpdatePacket packet = safeDowncast(decoder.decodeUpdatePacket(buildProtocolPacket(new byte[] {
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				// (byte)0x00, (byte)0x35, // length 53 octets 
				// (byte)0x02, // type code 2 (UPDATE) 
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x0e, // path attributes length (14 octets)
				(byte)0x50, (byte)0x11, (byte)0x00, (byte)0x0a, // Path attribute: 10 octets AS4_PATH 
				0x01, 0x02, 0x00, 0x00, 0x12, 0x34, 0x00, 0x00, 0x56, 0x78, // AS_SET 0x1234 0x5678 
		})), UpdatePacket.class);
		
		Assert.assertEquals(2, packet.getType());
		Assert.assertEquals(0, packet.getWithdrawnRoutes().size());
		Assert.assertEquals(1, packet.getPathAttributes().size());
		Assert.assertEquals(0, packet.getNlris().size());		
		
		ASPathAttribute asPath = (ASPathAttribute)packet.getPathAttributes().remove(0);
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS4_PATH, asPath.getTypeCode());
		Assert.assertEquals(ASType.AS_NUMBER_4OCTETS, asPath.getAsType());
		Assert.assertEquals(1, asPath.getPathSegments().size());
		
		ASPathAttribute.PathSegment segment = asPath.getPathSegments().remove(0);
		
		Assert.assertEquals(ASPathAttribute.PathSegmentType.AS_SET, segment.getPathSegmentType());
		Assert.assertEquals(2, segment.getAses().size());
		Assert.assertEquals((Integer)0x1234, segment.getAses().remove(0));
		Assert.assertEquals((Integer)0x5678, segment.getAses().remove(0));
	}

	@Test
	public void testASPath4ASSeqeunceOneASNumberASSeqeunceOneASNumberPacket() throws Exception {
		UpdatePacket packet = safeDowncast(decoder.decodeUpdatePacket(buildProtocolPacket(new byte[] {
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				// (byte)0x00, (byte)0x35, // length 53 octets 
				// (byte)0x02, // type code 2 (UPDATE) 
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x10, // path attributes length (16 octets)
				(byte)0x50, (byte)0x11, (byte)0x00, (byte)0x0c, // Path attribute: 12 octets AS4_PATH
				0x02, 0x01, 0x00, 0x00, 0x12, 0x34, // AS_SEQUENCE 0x1234
				0x02, 0x01, 0x00, 0x00, 0x56, 0x78, // AS_SEQUENCE 0x5678 
		})), UpdatePacket.class);
		
		Assert.assertEquals(2, packet.getType());
		Assert.assertEquals(0, packet.getWithdrawnRoutes().size());
		Assert.assertEquals(1, packet.getPathAttributes().size());
		Assert.assertEquals(0, packet.getNlris().size());		
		
		ASPathAttribute asPath = (ASPathAttribute)packet.getPathAttributes().remove(0);
		ASPathAttribute.PathSegment segment;
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS4_PATH, asPath.getTypeCode());
		Assert.assertEquals(ASType.AS_NUMBER_4OCTETS, asPath.getAsType());
		Assert.assertEquals(2, asPath.getPathSegments().size());
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(ASPathAttribute.PathSegmentType.AS_SEQUENCE, segment.getPathSegmentType());
		Assert.assertEquals(1, segment.getAses().size());
		Assert.assertEquals((Integer)0x1234, segment.getAses().remove(0));
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(ASPathAttribute.PathSegmentType.AS_SEQUENCE, segment.getPathSegmentType());
		Assert.assertEquals(1, segment.getAses().size());
		Assert.assertEquals((Integer)0x5678, segment.getAses().remove(0));
	}

	@Test
	public void testASPath4ASSeqeunceTw0ASNumberASSeqeunceOneASNumberPacket() throws Exception {
		UpdatePacket packet = safeDowncast(decoder.decodeUpdatePacket(buildProtocolPacket(new byte[] {
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				// (byte)0x00, (byte)0x35, // length 53 octets 
				// (byte)0x02, // type code 2 (UPDATE) 
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x14, // path attributes length (20 octets)
				(byte)0x50, (byte)0x11, (byte)0x00, (byte)0x10, // Path attribute: 16 octets AS4_PATH
				0x02, 0x02, 0x00, 0x00, 0x12, 0x34, 0x00, 0x00, (byte)0x89, (byte)0xab, // AS_SEQUENCE 0x1234 0x89ab
				0x02, 0x01, 0x00, 0x00, 0x56, 0x78, // AS_SEQUENCE 0x5678 
		})), UpdatePacket.class);
		
		Assert.assertEquals(2, packet.getType());
		Assert.assertEquals(0, packet.getWithdrawnRoutes().size());
		Assert.assertEquals(1, packet.getPathAttributes().size());
		Assert.assertEquals(0, packet.getNlris().size());		
		
		ASPathAttribute asPath = (ASPathAttribute)packet.getPathAttributes().remove(0);
		ASPathAttribute.PathSegment segment;
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS4_PATH, asPath.getTypeCode());
		Assert.assertEquals(ASType.AS_NUMBER_4OCTETS, asPath.getAsType());
		Assert.assertEquals(2, asPath.getPathSegments().size());
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(ASPathAttribute.PathSegmentType.AS_SEQUENCE, segment.getPathSegmentType());
		Assert.assertEquals(2, segment.getAses().size());
		Assert.assertEquals((Integer)0x1234, segment.getAses().remove(0));
		Assert.assertEquals((Integer)0x89ab, segment.getAses().remove(0));
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(ASPathAttribute.PathSegmentType.AS_SEQUENCE, segment.getPathSegmentType());
		Assert.assertEquals(1, segment.getAses().size());
		Assert.assertEquals((Integer)0x5678, segment.getAses().remove(0));
	}

	@Test
	public void testASPath4ASSeqeunceTw0ASNumberASSeqeunceTwoASNumberPacket() throws Exception {
		UpdatePacket packet = safeDowncast(decoder.decodeUpdatePacket(buildProtocolPacket(new byte[] {
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				// (byte)0x00, (byte)0x35, // length 53 octets 
				// (byte)0x02, // type code 2 (UPDATE) 
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x18, // path attributes length (24 octets)
				(byte)0x50, (byte)0x11, (byte)0x00, (byte)0x14, // Path attribute: 20 octets AS4_PATH
				0x02, 0x02, 0x00, 0x00, 0x12, 0x34, 0x00, 0x00, (byte)0x89, (byte)0xab, // AS_SEQUENCE 0x1234 0x89ab
				0x02, 0x02, 0x00, 0x00, 0x56, 0x78, 0x00, 0x00, (byte)0xcd, (byte)0xef // AS_SEQUENCE 0x5678 0xcdef 
		})), UpdatePacket.class);
		
		Assert.assertEquals(2, packet.getType());
		Assert.assertEquals(0, packet.getWithdrawnRoutes().size());
		Assert.assertEquals(1, packet.getPathAttributes().size());
		Assert.assertEquals(0, packet.getNlris().size());		
		
		ASPathAttribute asPath = (ASPathAttribute)packet.getPathAttributes().remove(0);
		ASPathAttribute.PathSegment segment;
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS4_PATH, asPath.getTypeCode());
		Assert.assertEquals(ASType.AS_NUMBER_4OCTETS, asPath.getAsType());
		Assert.assertEquals(2, asPath.getPathSegments().size());
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(ASPathAttribute.PathSegmentType.AS_SEQUENCE, segment.getPathSegmentType());
		Assert.assertEquals(2, segment.getAses().size());
		Assert.assertEquals((Integer)0x1234, segment.getAses().remove(0));
		Assert.assertEquals((Integer)0x89ab, segment.getAses().remove(0));
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(ASPathAttribute.PathSegmentType.AS_SEQUENCE, segment.getPathSegmentType());
		Assert.assertEquals(2, segment.getAses().size());
		Assert.assertEquals((Integer)0x5678, segment.getAses().remove(0));
		Assert.assertEquals((Integer)0xcdef, segment.getAses().remove(0));
	}

	@Test
	public void testASPath4ASSeqeunceOneASNumberASSeqeunceTwoASNumberPacket() throws Exception {
		UpdatePacket packet = safeDowncast(decoder.decodeUpdatePacket(buildProtocolPacket(new byte[] {
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				// (byte)0x00, (byte)0x35, // length 53 octets 
				// (byte)0x02, // type code 2 (UPDATE) 
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x14, // path attributes length (20 octets)
				(byte)0x50, (byte)0x11, (byte)0x00, (byte)0x10, // Path attribute: 16 octets AS4_PATH
				0x02, 0x01, 0x00, 0x00, 0x12, 0x34, // AS_SEQUENCE 0x1234 
				0x02, 0x02, 0x00, 0x00, 0x56, 0x78, 0x00, 0x00, (byte)0xcd, (byte)0xef // AS_SEQUENCE 0x5678 0xcdef 
		})), UpdatePacket.class);
		
		Assert.assertEquals(2, packet.getType());
		Assert.assertEquals(0, packet.getWithdrawnRoutes().size());
		Assert.assertEquals(1, packet.getPathAttributes().size());
		Assert.assertEquals(0, packet.getNlris().size());		
		
		ASPathAttribute asPath = (ASPathAttribute)packet.getPathAttributes().remove(0);
		ASPathAttribute.PathSegment segment;
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS4_PATH, asPath.getTypeCode());
		Assert.assertEquals(ASType.AS_NUMBER_4OCTETS, asPath.getAsType());
		Assert.assertEquals(2, asPath.getPathSegments().size());
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(ASPathAttribute.PathSegmentType.AS_SEQUENCE, segment.getPathSegmentType());
		Assert.assertEquals(1, segment.getAses().size());
		Assert.assertEquals((Integer)0x1234, segment.getAses().remove(0));
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(ASPathAttribute.PathSegmentType.AS_SEQUENCE, segment.getPathSegmentType());
		Assert.assertEquals(2, segment.getAses().size());
		Assert.assertEquals((Integer)0x5678, segment.getAses().remove(0));
		Assert.assertEquals((Integer)0xcdef, segment.getAses().remove(0));
	}
	
	@Test
	public void testASPath4ASSeqeunceOneASNumberASSetOneASNumberPacket() throws Exception {
		UpdatePacket packet = safeDowncast(decoder.decodeUpdatePacket(buildProtocolPacket(new byte[] {
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				// (byte)0x00, (byte)0x35, // length 53 octets 
				// (byte)0x02, // type code 2 (UPDATE) 
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x10, // path attributes length (16 octets)
				(byte)0x50, (byte)0x11, (byte)0x00, (byte)0x0c, // Path attribute: 12 octets AS_PATH
				0x02, 0x01, 0x00, 0x00, 0x12, 0x34, // AS_SEQUENCE 0x1234
				0x01, 0x01, 0x00, 0x00, 0x56, 0x78, // AS_SEQUENCE 0x5678 
		})), UpdatePacket.class);
		
		Assert.assertEquals(2, packet.getType());
		Assert.assertEquals(0, packet.getWithdrawnRoutes().size());
		Assert.assertEquals(1, packet.getPathAttributes().size());
		Assert.assertEquals(0, packet.getNlris().size());		
		
		ASPathAttribute asPath = (ASPathAttribute)packet.getPathAttributes().remove(0);
		ASPathAttribute.PathSegment segment;
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS4_PATH, asPath.getTypeCode());
		Assert.assertEquals(ASType.AS_NUMBER_4OCTETS, asPath.getAsType());
		Assert.assertEquals(2, asPath.getPathSegments().size());
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(ASPathAttribute.PathSegmentType.AS_SEQUENCE, segment.getPathSegmentType());
		Assert.assertEquals(1, segment.getAses().size());
		Assert.assertEquals((Integer)0x1234, segment.getAses().remove(0));
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(ASPathAttribute.PathSegmentType.AS_SET, segment.getPathSegmentType());
		Assert.assertEquals(1, segment.getAses().size());
		Assert.assertEquals((Integer)0x5678, segment.getAses().remove(0));
	}

	@Test
	public void testASPath4ASSeqeunceTw0ASNumberASSetASNumberPacket() throws Exception {
		UpdatePacket packet = safeDowncast(decoder.decodeUpdatePacket(buildProtocolPacket(new byte[] {
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				// (byte)0x00, (byte)0x35, // length 53 octets 
				// (byte)0x02, // type code 2 (UPDATE) 
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x14, // path attributes length (20 octets)
				(byte)0x50, (byte)0x11, (byte)0x00, (byte)0x10, // Path attribute: 16 octets AS4_PATH
				0x02, 0x02, 0x00, 0x00, 0x12, 0x34, 0x00, 0x00, (byte)0x89, (byte)0xab, // AS_SEQUENCE 0x1234 0x89ab
				0x01, 0x01, 0x00, 0x00, 0x56, 0x78, // AS_SEQUENCE 0x5678 
		})), UpdatePacket.class);
		
		Assert.assertEquals(2, packet.getType());
		Assert.assertEquals(0, packet.getWithdrawnRoutes().size());
		Assert.assertEquals(1, packet.getPathAttributes().size());
		Assert.assertEquals(0, packet.getNlris().size());		
		
		ASPathAttribute asPath = (ASPathAttribute)packet.getPathAttributes().remove(0);
		ASPathAttribute.PathSegment segment;
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS4_PATH, asPath.getTypeCode());
		Assert.assertEquals(ASType.AS_NUMBER_4OCTETS, asPath.getAsType());
		Assert.assertEquals(2, asPath.getPathSegments().size());
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(ASPathAttribute.PathSegmentType.AS_SEQUENCE, segment.getPathSegmentType());
		Assert.assertEquals(2, segment.getAses().size());
		Assert.assertEquals((Integer)0x1234, segment.getAses().remove(0));
		Assert.assertEquals((Integer)0x89ab, segment.getAses().remove(0));
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(ASPathAttribute.PathSegmentType.AS_SET, segment.getPathSegmentType());
		Assert.assertEquals(1, segment.getAses().size());
		Assert.assertEquals((Integer)0x5678, segment.getAses().remove(0));
	}

	@Test
	public void testASPath4ASSeqeunceTw0ASNumberASSetTwoASNumberPacket() throws Exception {
		UpdatePacket packet = safeDowncast(decoder.decodeUpdatePacket(buildProtocolPacket(new byte[] {
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				// (byte)0x00, (byte)0x35, // length 53 octets 
				// (byte)0x02, // type code 2 (UPDATE) 
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x18, // path attributes length (24 octets)
				(byte)0x50, (byte)0x11, (byte)0x00, (byte)0x14, // Path attribute: 20 octets AS4_PATH
				0x02, 0x02, 0x00, 0x00, 0x12, 0x34, 0x00, 0x00, (byte)0x89, (byte)0xab, // AS_SEQUENCE 0x1234 0x89ab
				0x01, 0x02, 0x00, 0x00, 0x56, 0x78, 0x00, 0x00, (byte)0xcd, (byte)0xef // AS_SEQUENCE 0x5678 0xcdef 
		})), UpdatePacket.class);
		
		Assert.assertEquals(2, packet.getType());
		Assert.assertEquals(0, packet.getWithdrawnRoutes().size());
		Assert.assertEquals(1, packet.getPathAttributes().size());
		Assert.assertEquals(0, packet.getNlris().size());		
		
		ASPathAttribute asPath = (ASPathAttribute)packet.getPathAttributes().remove(0);
		ASPathAttribute.PathSegment segment;
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS4_PATH, asPath.getTypeCode());
		Assert.assertEquals(ASType.AS_NUMBER_4OCTETS, asPath.getAsType());
		Assert.assertEquals(2, asPath.getPathSegments().size());
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(ASPathAttribute.PathSegmentType.AS_SEQUENCE, segment.getPathSegmentType());
		Assert.assertEquals(2, segment.getAses().size());
		Assert.assertEquals((Integer)0x1234, segment.getAses().remove(0));
		Assert.assertEquals((Integer)0x89ab, segment.getAses().remove(0));
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(ASPathAttribute.PathSegmentType.AS_SET, segment.getPathSegmentType());
		Assert.assertEquals(2, segment.getAses().size());
		Assert.assertEquals((Integer)0x5678, segment.getAses().remove(0));
		Assert.assertEquals((Integer)0xcdef, segment.getAses().remove(0));
	}

	@Test
	public void testASPath4ASSeqeunceOneASNumberASSetTwoASNumberPacket() throws Exception {
		UpdatePacket packet = safeDowncast(decoder.decodeUpdatePacket(buildProtocolPacket(new byte[] {
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				// (byte)0x00, (byte)0x35, // length 53 octets 
				// (byte)0x02, // type code 2 (UPDATE) 
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x14, // path attributes length (20 octets)
				(byte)0x50, (byte)0x11, (byte)0x00, (byte)0x10, // Path attribute: 16 octets AS4_PATH
				0x02, 0x01, 0x00, 0x00, 0x12, 0x34, // AS_SEQUENCE 0x1234 
				0x01, 0x02, 0x00, 0x00, 0x56, 0x78, 0x00, 0x00, (byte)0xcd, (byte)0xef // AS_SEQUENCE 0x5678 0xcdef 
		})), UpdatePacket.class);
		
		Assert.assertEquals(2, packet.getType());
		Assert.assertEquals(0, packet.getWithdrawnRoutes().size());
		Assert.assertEquals(1, packet.getPathAttributes().size());
		Assert.assertEquals(0, packet.getNlris().size());		
		
		ASPathAttribute asPath = (ASPathAttribute)packet.getPathAttributes().remove(0);
		ASPathAttribute.PathSegment segment;
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS4_PATH, asPath.getTypeCode());
		Assert.assertEquals(ASType.AS_NUMBER_4OCTETS, asPath.getAsType());
		Assert.assertEquals(2, asPath.getPathSegments().size());
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(ASPathAttribute.PathSegmentType.AS_SEQUENCE, segment.getPathSegmentType());
		Assert.assertEquals(1, segment.getAses().size());
		Assert.assertEquals((Integer)0x1234, segment.getAses().remove(0));
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(ASPathAttribute.PathSegmentType.AS_SET, segment.getPathSegmentType());
		Assert.assertEquals(2, segment.getAses().size());
		Assert.assertEquals((Integer)0x5678, segment.getAses().remove(0));
		Assert.assertEquals((Integer)0xcdef, segment.getAses().remove(0));
	}

	// ----
	@Test
	public void testASPath4ASSetOneASNumberASSeqeunceOneASNumberPacket() throws Exception {
		UpdatePacket packet = safeDowncast(decoder.decodeUpdatePacket(buildProtocolPacket(new byte[] {
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				// (byte)0x00, (byte)0x35, // length 53 octets 
				// (byte)0x02, // type code 2 (UPDATE) 
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x10, // path attributes length (16 octets)
				(byte)0x50, (byte)0x11, (byte)0x00, (byte)0x0c, // Path attribute: 12 octets AS4_PATH
				0x01, 0x01, 0x00, 0x00, 0x12, 0x34, // AS_SEQUENCE 0x1234
				0x02, 0x01, 0x00, 0x00, 0x56, 0x78, // AS_SEQUENCE 0x5678 
		})), UpdatePacket.class);
		
		Assert.assertEquals(2, packet.getType());
		Assert.assertEquals(0, packet.getWithdrawnRoutes().size());
		Assert.assertEquals(1, packet.getPathAttributes().size());
		Assert.assertEquals(0, packet.getNlris().size());		
		
		ASPathAttribute asPath = (ASPathAttribute)packet.getPathAttributes().remove(0);
		ASPathAttribute.PathSegment segment;
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS4_PATH, asPath.getTypeCode());
		Assert.assertEquals(ASType.AS_NUMBER_4OCTETS, asPath.getAsType());
		Assert.assertEquals(2, asPath.getPathSegments().size());
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(ASPathAttribute.PathSegmentType.AS_SET, segment.getPathSegmentType());
		Assert.assertEquals(1, segment.getAses().size());
		Assert.assertEquals((Integer)0x1234, segment.getAses().remove(0));
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(ASPathAttribute.PathSegmentType.AS_SEQUENCE, segment.getPathSegmentType());
		Assert.assertEquals(1, segment.getAses().size());
		Assert.assertEquals((Integer)0x5678, segment.getAses().remove(0));
	}

	@Test
	public void testASPath4ASSetTw0ASNumberASSeqeunceOneASNumberPacket() throws Exception {
		UpdatePacket packet = safeDowncast(decoder.decodeUpdatePacket(buildProtocolPacket(new byte[] {
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				// (byte)0x00, (byte)0x35, // length 53 octets 
				// (byte)0x02, // type code 2 (UPDATE) 
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x14, // path attributes length (20 octets)
				(byte)0x50, (byte)0x11, (byte)0x00, (byte)0x10, // Path attribute: 16 octets AS4_PATH
				0x01, 0x02, 0x00, 0x00, 0x12, 0x34, 0x00, 0x00, (byte)0x89, (byte)0xab, // AS_SEQUENCE 0x1234 0x89ab
				0x02, 0x01, 0x00, 0x00, 0x56, 0x78, // AS_SEQUENCE 0x5678 
		})), UpdatePacket.class);
		
		Assert.assertEquals(2, packet.getType());
		Assert.assertEquals(0, packet.getWithdrawnRoutes().size());
		Assert.assertEquals(1, packet.getPathAttributes().size());
		Assert.assertEquals(0, packet.getNlris().size());		
		
		ASPathAttribute asPath = (ASPathAttribute)packet.getPathAttributes().remove(0);
		ASPathAttribute.PathSegment segment;
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS4_PATH, asPath.getTypeCode());
		Assert.assertEquals(ASType.AS_NUMBER_4OCTETS, asPath.getAsType());
		Assert.assertEquals(2, asPath.getPathSegments().size());
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(ASPathAttribute.PathSegmentType.AS_SET, segment.getPathSegmentType());
		Assert.assertEquals(2, segment.getAses().size());
		Assert.assertEquals((Integer)0x1234, segment.getAses().remove(0));
		Assert.assertEquals((Integer)0x89ab, segment.getAses().remove(0));
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(ASPathAttribute.PathSegmentType.AS_SEQUENCE, segment.getPathSegmentType());
		Assert.assertEquals(1, segment.getAses().size());
		Assert.assertEquals((Integer)0x5678, segment.getAses().remove(0));
	}

	@Test
	public void testASPath4ASSetTw0ASNumberASSeqeunceTwoASNumberPacket() throws Exception {
		UpdatePacket packet = safeDowncast(decoder.decodeUpdatePacket(buildProtocolPacket(new byte[] {
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				// (byte)0x00, (byte)0x35, // length 53 octets 
				// (byte)0x02, // type code 2 (UPDATE) 
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x18, // path attributes length (24 octets)
				(byte)0x50, (byte)0x11, (byte)0x00, (byte)0x14, // Path attribute: 20 octets AS4_PATH
				0x01, 0x02, 0x00, 0x00, 0x12, 0x34, 0x00, 0x00, (byte)0x89, (byte)0xab, // AS_SEQUENCE 0x1234 0x89ab
				0x02, 0x02, 0x00, 0x00, 0x56, 0x78, 0x00, 0x00, (byte)0xcd, (byte)0xef // AS_SEQUENCE 0x5678 0xcdef 
		})), UpdatePacket.class);
		
		Assert.assertEquals(2, packet.getType());
		Assert.assertEquals(0, packet.getWithdrawnRoutes().size());
		Assert.assertEquals(1, packet.getPathAttributes().size());
		Assert.assertEquals(0, packet.getNlris().size());		
		
		ASPathAttribute asPath = (ASPathAttribute)packet.getPathAttributes().remove(0);
		ASPathAttribute.PathSegment segment;
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS4_PATH, asPath.getTypeCode());
		Assert.assertEquals(ASType.AS_NUMBER_4OCTETS, asPath.getAsType());
		Assert.assertEquals(2, asPath.getPathSegments().size());
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(ASPathAttribute.PathSegmentType.AS_SET, segment.getPathSegmentType());
		Assert.assertEquals(2, segment.getAses().size());
		Assert.assertEquals((Integer)0x1234, segment.getAses().remove(0));
		Assert.assertEquals((Integer)0x89ab, segment.getAses().remove(0));
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(ASPathAttribute.PathSegmentType.AS_SEQUENCE, segment.getPathSegmentType());
		Assert.assertEquals(2, segment.getAses().size());
		Assert.assertEquals((Integer)0x5678, segment.getAses().remove(0));
		Assert.assertEquals((Integer)0xcdef, segment.getAses().remove(0));
	}

	@Test
	public void testASPath4ASSetOneASNumberASSeqeunceTwoASNumberPacket() throws Exception {
		UpdatePacket packet = safeDowncast(decoder.decodeUpdatePacket(buildProtocolPacket(new byte[] {
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				// (byte)0x00, (byte)0x35, // length 53 octets 
				// (byte)0x02, // type code 2 (UPDATE) 
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x14, // path attributes length (20 octets)
				(byte)0x50, (byte)0x11, (byte)0x00, (byte)0x10, // Path attribute: 16 octets AS4_PATH
				0x01, 0x01, 0x00, 0x00, 0x12, 0x34, // AS_SEQUENCE 0x1234 
				0x02, 0x02, 0x00, 0x00, 0x56, 0x78, 0x00, 0x00, (byte)0xcd, (byte)0xef // AS_SEQUENCE 0x5678 0xcdef 
		})), UpdatePacket.class);
		
		Assert.assertEquals(2, packet.getType());
		Assert.assertEquals(0, packet.getWithdrawnRoutes().size());
		Assert.assertEquals(1, packet.getPathAttributes().size());
		Assert.assertEquals(0, packet.getNlris().size());		
		
		ASPathAttribute asPath = (ASPathAttribute)packet.getPathAttributes().remove(0);
		ASPathAttribute.PathSegment segment;
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS4_PATH, asPath.getTypeCode());
		Assert.assertEquals(ASType.AS_NUMBER_4OCTETS, asPath.getAsType());
		Assert.assertEquals(2, asPath.getPathSegments().size());
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(ASPathAttribute.PathSegmentType.AS_SET, segment.getPathSegmentType());
		Assert.assertEquals(1, segment.getAses().size());
		Assert.assertEquals((Integer)0x1234, segment.getAses().remove(0));
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(ASPathAttribute.PathSegmentType.AS_SEQUENCE, segment.getPathSegmentType());
		Assert.assertEquals(2, segment.getAses().size());
		Assert.assertEquals((Integer)0x5678, segment.getAses().remove(0));
		Assert.assertEquals((Integer)0xcdef, segment.getAses().remove(0));
	}

	@Test
	public void testASPath4BadPathTypeOneASNumberPacket() throws Exception {
		(new AssertExecption() {
			
			@Override
			protected void doExecute() {
				safeDowncast(decoder.decodeUpdatePacket(buildProtocolPacket(new byte[] {
						// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
						// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
						// (byte)0x00, (byte)0x35, // length 53 octets 
						// (byte)0x02, // type code 2 (UPDATE) 
						(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
						(byte)0x00, (byte)0x0a, // path attributes length (10 octets)
						(byte)0x50, (byte)0x11, (byte)0x00, (byte)0x06, // Path attribute: 6 octets AS_PATH  
						0x03, 0x01, 0x00, 0x00, 0x12, 0x34, // Invalid 0x1234 
				})), UpdatePacket.class);	
			}
		}).execute(MalformedASPathAttributeException.class);
	}

	@Test
	public void testASPath4ASSequenceTwoASNumberOneMissingPacket() throws Exception {
		(new AssertExecption() {
			
			@Override
			protected void doExecute() {
				safeDowncast(decoder.decodeUpdatePacket(buildProtocolPacket(new byte[] {
						// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
						// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
						// (byte)0x00, (byte)0x35, // length 53 octets 
						// (byte)0x02, // type code 2 (UPDATE) 
						(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
						(byte)0x00, (byte)0x0a, // path attributes length (8 octets)
						(byte)0x50, (byte)0x11, (byte)0x00, (byte)0x06, // Path attribute: 4 octets AS_PATH  
						0x02, 0x02, 0x00, 0x00, 0x12, 0x34, // Invalid 0x1234 
				})), UpdatePacket.class);	
			}
		}).execute(MalformedASPathAttributeException.class);
	}
	
	@Test
	public void testASPath2EmptyPacket() throws Exception {
		UpdatePacket packet = safeDowncast(decoder.decodeUpdatePacket(buildProtocolPacket(new byte[] {
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				// (byte)0x00, (byte)0x35, // length 53 octets 
				// (byte)0x02, // type code 2 (UPDATE) 
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x04, // path attributes length (29 octets)
				(byte)0x50, (byte)0x02, (byte)0x00, (byte)0x00, // Path attribute: AS_PATH emtpy 
		})), UpdatePacket.class);
		
		Assert.assertEquals(2, packet.getType());
		Assert.assertEquals(0, packet.getWithdrawnRoutes().size());
		Assert.assertEquals(1, packet.getPathAttributes().size());
		Assert.assertEquals(0, packet.getNlris().size());		
		
		ASPathAttribute asPath = (ASPathAttribute)packet.getPathAttributes().remove(0);
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS_PATH, asPath.getTypeCode());
		Assert.assertEquals(ASType.AS_NUMBER_2OCTETS, asPath.getAsType());
		Assert.assertEquals(0, asPath.getPathSegments().size());
	}

	@Test
	public void testASPath2ASSequenceOneASNumberPacket() throws Exception {
		UpdatePacket packet = safeDowncast(decoder.decodeUpdatePacket(buildProtocolPacket(new byte[] {
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				// (byte)0x00, (byte)0x35, // length 53 octets 
				// (byte)0x02, // type code 2 (UPDATE) 
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x08, // path attributes length (8 octets)
				(byte)0x50, (byte)0x02, (byte)0x00, (byte)0x04, 0x02, 0x01, 0x12, 0x34, // Path attribute: 4 octets AS_PATH AS_SEQUENCE 0x1234 
		})), UpdatePacket.class);
		
		Assert.assertEquals(2, packet.getType());
		Assert.assertEquals(0, packet.getWithdrawnRoutes().size());
		Assert.assertEquals(1, packet.getPathAttributes().size());
		Assert.assertEquals(0, packet.getNlris().size());		
		
		ASPathAttribute asPath = (ASPathAttribute)packet.getPathAttributes().remove(0);
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS_PATH, asPath.getTypeCode());
		Assert.assertEquals(ASType.AS_NUMBER_2OCTETS, asPath.getAsType());
		Assert.assertEquals(1, asPath.getPathSegments().size());
		
		ASPathAttribute.PathSegment segment = asPath.getPathSegments().remove(0);
		
		Assert.assertEquals(ASPathAttribute.PathSegmentType.AS_SEQUENCE, segment.getPathSegmentType());
		Assert.assertEquals(1, segment.getAses().size());
		Assert.assertEquals((Integer)0x1234, segment.getAses().remove(0));
	}

	@Test
	public void testASPath2ASSequenceTwoASNumberPacket() throws Exception {
		UpdatePacket packet = safeDowncast(decoder.decodeUpdatePacket(buildProtocolPacket(new byte[] {
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				// (byte)0x00, (byte)0x35, // length 53 octets 
				// (byte)0x02, // type code 2 (UPDATE) 
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x0a, // path attributes length (10 octets)
				(byte)0x50, (byte)0x02, (byte)0x00, (byte)0x06, 0x02, 0x02, 0x12, 0x34, 0x56, 0x78, // Path attribute: 6 octets AS_PATH AS_SEQUENCE 0x1234 0x5678 
		})), UpdatePacket.class);
		
		Assert.assertEquals(2, packet.getType());
		Assert.assertEquals(0, packet.getWithdrawnRoutes().size());
		Assert.assertEquals(1, packet.getPathAttributes().size());
		Assert.assertEquals(0, packet.getNlris().size());		
		
		ASPathAttribute asPath = (ASPathAttribute)packet.getPathAttributes().remove(0);
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS_PATH, asPath.getTypeCode());
		Assert.assertEquals(ASType.AS_NUMBER_2OCTETS, asPath.getAsType());
		Assert.assertEquals(1, asPath.getPathSegments().size());
		
		ASPathAttribute.PathSegment segment = asPath.getPathSegments().remove(0);
		
		Assert.assertEquals(ASPathAttribute.PathSegmentType.AS_SEQUENCE, segment.getPathSegmentType());
		Assert.assertEquals(2, segment.getAses().size());
		Assert.assertEquals((Integer)0x1234, segment.getAses().remove(0));
		Assert.assertEquals((Integer)0x5678, segment.getAses().remove(0));
	}

	@Test
	public void testASPath2ASSetOneASNumberPacket() throws Exception {
		UpdatePacket packet = safeDowncast(decoder.decodeUpdatePacket(buildProtocolPacket(new byte[] {
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				// (byte)0x00, (byte)0x35, // length 53 octets 
				// (byte)0x02, // type code 2 (UPDATE) 
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x08, // path attributes length (8 octets)
				(byte)0x50, (byte)0x02, (byte)0x00, (byte)0x04, 0x01, 0x01, 0x12, 0x34, // Path attribute: 4 octets AS_PATH AS_SET 0x1234 
		})), UpdatePacket.class);
		
		Assert.assertEquals(2, packet.getType());
		Assert.assertEquals(0, packet.getWithdrawnRoutes().size());
		Assert.assertEquals(1, packet.getPathAttributes().size());
		Assert.assertEquals(0, packet.getNlris().size());		
		
		ASPathAttribute asPath = (ASPathAttribute)packet.getPathAttributes().remove(0);
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS_PATH, asPath.getTypeCode());
		Assert.assertEquals(ASType.AS_NUMBER_2OCTETS, asPath.getAsType());
		Assert.assertEquals(1, asPath.getPathSegments().size());
		
		ASPathAttribute.PathSegment segment = asPath.getPathSegments().remove(0);
		
		Assert.assertEquals(ASPathAttribute.PathSegmentType.AS_SET, segment.getPathSegmentType());
		Assert.assertEquals(1, segment.getAses().size());
		Assert.assertEquals((Integer)0x1234, segment.getAses().remove(0));
	}

	@Test
	public void testASPath2ASSetTwoASNumberPacket() throws Exception {
		UpdatePacket packet = safeDowncast(decoder.decodeUpdatePacket(buildProtocolPacket(new byte[] {
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				// (byte)0x00, (byte)0x35, // length 53 octets 
				// (byte)0x02, // type code 2 (UPDATE) 
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x0a, // path attributes length (10 octets)
				(byte)0x50, (byte)0x02, (byte)0x00, (byte)0x06, 0x01, 0x02, 0x12, 0x34, 0x56, 0x78, // Path attribute: 6 octets AS_PATH AS_SET 0x1234 0x5678 
		})), UpdatePacket.class);
		
		Assert.assertEquals(2, packet.getType());
		Assert.assertEquals(0, packet.getWithdrawnRoutes().size());
		Assert.assertEquals(1, packet.getPathAttributes().size());
		Assert.assertEquals(0, packet.getNlris().size());		
		
		ASPathAttribute asPath = (ASPathAttribute)packet.getPathAttributes().remove(0);
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS_PATH, asPath.getTypeCode());
		Assert.assertEquals(ASType.AS_NUMBER_2OCTETS, asPath.getAsType());
		Assert.assertEquals(1, asPath.getPathSegments().size());
		
		ASPathAttribute.PathSegment segment = asPath.getPathSegments().remove(0);
		
		Assert.assertEquals(ASPathAttribute.PathSegmentType.AS_SET, segment.getPathSegmentType());
		Assert.assertEquals(2, segment.getAses().size());
		Assert.assertEquals((Integer)0x1234, segment.getAses().remove(0));
		Assert.assertEquals((Integer)0x5678, segment.getAses().remove(0));
	}

	@Test
	public void testASPath2ASSeqeunceOneASNumberASSeqeunceOneASNumberPacket() throws Exception {
		UpdatePacket packet = safeDowncast(decoder.decodeUpdatePacket(buildProtocolPacket(new byte[] {
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				// (byte)0x00, (byte)0x35, // length 53 octets 
				// (byte)0x02, // type code 2 (UPDATE) 
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x0c, // path attributes length (12 octets)
				(byte)0x50, (byte)0x02, (byte)0x00, (byte)0x08, // Path attribute: 8 octets AS_PATH
				0x02, 0x01, 0x12, 0x34, // AS_SEQUENCE 0x1234
				0x02, 0x01, 0x56, 0x78, // AS_SEQUENCE 0x5678 
		})), UpdatePacket.class);
		
		Assert.assertEquals(2, packet.getType());
		Assert.assertEquals(0, packet.getWithdrawnRoutes().size());
		Assert.assertEquals(1, packet.getPathAttributes().size());
		Assert.assertEquals(0, packet.getNlris().size());		
		
		ASPathAttribute asPath = (ASPathAttribute)packet.getPathAttributes().remove(0);
		ASPathAttribute.PathSegment segment;
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS_PATH, asPath.getTypeCode());
		Assert.assertEquals(ASType.AS_NUMBER_2OCTETS, asPath.getAsType());
		Assert.assertEquals(2, asPath.getPathSegments().size());
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(ASPathAttribute.PathSegmentType.AS_SEQUENCE, segment.getPathSegmentType());
		Assert.assertEquals(1, segment.getAses().size());
		Assert.assertEquals((Integer)0x1234, segment.getAses().remove(0));
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(ASPathAttribute.PathSegmentType.AS_SEQUENCE, segment.getPathSegmentType());
		Assert.assertEquals(1, segment.getAses().size());
		Assert.assertEquals((Integer)0x5678, segment.getAses().remove(0));
	}

	@Test
	public void testASPath2ASSeqeunceTw0ASNumberASSeqeunceOneASNumberPacket() throws Exception {
		UpdatePacket packet = safeDowncast(decoder.decodeUpdatePacket(buildProtocolPacket(new byte[] {
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				// (byte)0x00, (byte)0x35, // length 53 octets 
				// (byte)0x02, // type code 2 (UPDATE) 
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x0e, // path attributes length (14 octets)
				(byte)0x50, (byte)0x02, (byte)0x00, (byte)0x0a, // Path attribute: 10 octets AS_PATH
				0x02, 0x02, 0x12, 0x34, (byte)0x89, (byte)0xab, // AS_SEQUENCE 0x1234 0x89ab
				0x02, 0x01, 0x56, 0x78, // AS_SEQUENCE 0x5678 
		})), UpdatePacket.class);
		
		Assert.assertEquals(2, packet.getType());
		Assert.assertEquals(0, packet.getWithdrawnRoutes().size());
		Assert.assertEquals(1, packet.getPathAttributes().size());
		Assert.assertEquals(0, packet.getNlris().size());		
		
		ASPathAttribute asPath = (ASPathAttribute)packet.getPathAttributes().remove(0);
		ASPathAttribute.PathSegment segment;
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS_PATH, asPath.getTypeCode());
		Assert.assertEquals(ASType.AS_NUMBER_2OCTETS, asPath.getAsType());
		Assert.assertEquals(2, asPath.getPathSegments().size());
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(ASPathAttribute.PathSegmentType.AS_SEQUENCE, segment.getPathSegmentType());
		Assert.assertEquals(2, segment.getAses().size());
		Assert.assertEquals((Integer)0x1234, segment.getAses().remove(0));
		Assert.assertEquals((Integer)0x89ab, segment.getAses().remove(0));
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(ASPathAttribute.PathSegmentType.AS_SEQUENCE, segment.getPathSegmentType());
		Assert.assertEquals(1, segment.getAses().size());
		Assert.assertEquals((Integer)0x5678, segment.getAses().remove(0));
	}

	@Test
	public void testASPath2ASSeqeunceTw0ASNumberASSeqeunceTwoASNumberPacket() throws Exception {
		UpdatePacket packet = safeDowncast(decoder.decodeUpdatePacket(buildProtocolPacket(new byte[] {
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				// (byte)0x00, (byte)0x35, // length 53 octets 
				// (byte)0x02, // type code 2 (UPDATE) 
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x10, // path attributes length (16 octets)
				(byte)0x50, (byte)0x02, (byte)0x00, (byte)0x0c, // Path attribute: 12 octets AS_PATH
				0x02, 0x02, 0x12, 0x34, (byte)0x89, (byte)0xab, // AS_SEQUENCE 0x1234 0x89ab
				0x02, 0x02, 0x56, 0x78, (byte)0xcd, (byte)0xef // AS_SEQUENCE 0x5678 0xcdef 
		})), UpdatePacket.class);
		
		Assert.assertEquals(2, packet.getType());
		Assert.assertEquals(0, packet.getWithdrawnRoutes().size());
		Assert.assertEquals(1, packet.getPathAttributes().size());
		Assert.assertEquals(0, packet.getNlris().size());		
		
		ASPathAttribute asPath = (ASPathAttribute)packet.getPathAttributes().remove(0);
		ASPathAttribute.PathSegment segment;
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS_PATH, asPath.getTypeCode());
		Assert.assertEquals(ASType.AS_NUMBER_2OCTETS, asPath.getAsType());
		Assert.assertEquals(2, asPath.getPathSegments().size());
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(ASPathAttribute.PathSegmentType.AS_SEQUENCE, segment.getPathSegmentType());
		Assert.assertEquals(2, segment.getAses().size());
		Assert.assertEquals((Integer)0x1234, segment.getAses().remove(0));
		Assert.assertEquals((Integer)0x89ab, segment.getAses().remove(0));
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(ASPathAttribute.PathSegmentType.AS_SEQUENCE, segment.getPathSegmentType());
		Assert.assertEquals(2, segment.getAses().size());
		Assert.assertEquals((Integer)0x5678, segment.getAses().remove(0));
		Assert.assertEquals((Integer)0xcdef, segment.getAses().remove(0));
	}

	@Test
	public void testASPath2ASSeqeunceOneASNumberASSeqeunceTwoASNumberPacket() throws Exception {
		UpdatePacket packet = safeDowncast(decoder.decodeUpdatePacket(buildProtocolPacket(new byte[] {
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				// (byte)0x00, (byte)0x35, // length 53 octets 
				// (byte)0x02, // type code 2 (UPDATE) 
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x0e, // path attributes length (16 octets)
				(byte)0x50, (byte)0x02, (byte)0x00, (byte)0x0a, // Path attribute: 10 octets AS_PATH
				0x02, 0x01, 0x12, 0x34, // AS_SEQUENCE 0x1234 
				0x02, 0x02, 0x56, 0x78, (byte)0xcd, (byte)0xef // AS_SEQUENCE 0x5678 0xcdef 
		})), UpdatePacket.class);
		
		Assert.assertEquals(2, packet.getType());
		Assert.assertEquals(0, packet.getWithdrawnRoutes().size());
		Assert.assertEquals(1, packet.getPathAttributes().size());
		Assert.assertEquals(0, packet.getNlris().size());		
		
		ASPathAttribute asPath = (ASPathAttribute)packet.getPathAttributes().remove(0);
		ASPathAttribute.PathSegment segment;
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS_PATH, asPath.getTypeCode());
		Assert.assertEquals(ASType.AS_NUMBER_2OCTETS, asPath.getAsType());
		Assert.assertEquals(2, asPath.getPathSegments().size());
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(ASPathAttribute.PathSegmentType.AS_SEQUENCE, segment.getPathSegmentType());
		Assert.assertEquals(1, segment.getAses().size());
		Assert.assertEquals((Integer)0x1234, segment.getAses().remove(0));
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(ASPathAttribute.PathSegmentType.AS_SEQUENCE, segment.getPathSegmentType());
		Assert.assertEquals(2, segment.getAses().size());
		Assert.assertEquals((Integer)0x5678, segment.getAses().remove(0));
		Assert.assertEquals((Integer)0xcdef, segment.getAses().remove(0));
	}
	
	@Test
	public void testASPath2ASSeqeunceOneASNumberASSetOneASNumberPacket() throws Exception {
		UpdatePacket packet = safeDowncast(decoder.decodeUpdatePacket(buildProtocolPacket(new byte[] {
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				// (byte)0x00, (byte)0x35, // length 53 octets 
				// (byte)0x02, // type code 2 (UPDATE) 
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x0c, // path attributes length (12 octets)
				(byte)0x50, (byte)0x02, (byte)0x00, (byte)0x08, // Path attribute: 8 octets AS_PATH
				0x02, 0x01, 0x12, 0x34, // AS_SEQUENCE 0x1234
				0x01, 0x01, 0x56, 0x78, // AS_SEQUENCE 0x5678 
		})), UpdatePacket.class);
		
		Assert.assertEquals(2, packet.getType());
		Assert.assertEquals(0, packet.getWithdrawnRoutes().size());
		Assert.assertEquals(1, packet.getPathAttributes().size());
		Assert.assertEquals(0, packet.getNlris().size());		
		
		ASPathAttribute asPath = (ASPathAttribute)packet.getPathAttributes().remove(0);
		ASPathAttribute.PathSegment segment;
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS_PATH, asPath.getTypeCode());
		Assert.assertEquals(ASType.AS_NUMBER_2OCTETS, asPath.getAsType());
		Assert.assertEquals(2, asPath.getPathSegments().size());
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(ASPathAttribute.PathSegmentType.AS_SEQUENCE, segment.getPathSegmentType());
		Assert.assertEquals(1, segment.getAses().size());
		Assert.assertEquals((Integer)0x1234, segment.getAses().remove(0));
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(ASPathAttribute.PathSegmentType.AS_SET, segment.getPathSegmentType());
		Assert.assertEquals(1, segment.getAses().size());
		Assert.assertEquals((Integer)0x5678, segment.getAses().remove(0));
	}

	@Test
	public void testASPath2ASSeqeunceTw0ASNumberASSetASNumberPacket() throws Exception {
		UpdatePacket packet = safeDowncast(decoder.decodeUpdatePacket(buildProtocolPacket(new byte[] {
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				// (byte)0x00, (byte)0x35, // length 53 octets 
				// (byte)0x02, // type code 2 (UPDATE) 
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x0e, // path attributes length (14 octets)
				(byte)0x50, (byte)0x02, (byte)0x00, (byte)0x0a, // Path attribute: 10 octets AS_PATH
				0x02, 0x02, 0x12, 0x34, (byte)0x89, (byte)0xab, // AS_SEQUENCE 0x1234 0x89ab
				0x01, 0x01, 0x56, 0x78, // AS_SEQUENCE 0x5678 
		})), UpdatePacket.class);
		
		Assert.assertEquals(2, packet.getType());
		Assert.assertEquals(0, packet.getWithdrawnRoutes().size());
		Assert.assertEquals(1, packet.getPathAttributes().size());
		Assert.assertEquals(0, packet.getNlris().size());		
		
		ASPathAttribute asPath = (ASPathAttribute)packet.getPathAttributes().remove(0);
		ASPathAttribute.PathSegment segment;
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS_PATH, asPath.getTypeCode());
		Assert.assertEquals(ASType.AS_NUMBER_2OCTETS, asPath.getAsType());
		Assert.assertEquals(2, asPath.getPathSegments().size());
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(ASPathAttribute.PathSegmentType.AS_SEQUENCE, segment.getPathSegmentType());
		Assert.assertEquals(2, segment.getAses().size());
		Assert.assertEquals((Integer)0x1234, segment.getAses().remove(0));
		Assert.assertEquals((Integer)0x89ab, segment.getAses().remove(0));
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(ASPathAttribute.PathSegmentType.AS_SET, segment.getPathSegmentType());
		Assert.assertEquals(1, segment.getAses().size());
		Assert.assertEquals((Integer)0x5678, segment.getAses().remove(0));
	}

	@Test
	public void testASPath2ASSeqeunceTw0ASNumberASSetTwoASNumberPacket() throws Exception {
		UpdatePacket packet = safeDowncast(decoder.decodeUpdatePacket(buildProtocolPacket(new byte[] {
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				// (byte)0x00, (byte)0x35, // length 53 octets 
				// (byte)0x02, // type code 2 (UPDATE) 
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x10, // path attributes length (16 octets)
				(byte)0x50, (byte)0x02, (byte)0x00, (byte)0x0c, // Path attribute: 12 octets AS_PATH
				0x02, 0x02, 0x12, 0x34, (byte)0x89, (byte)0xab, // AS_SEQUENCE 0x1234 0x89ab
				0x01, 0x02, 0x56, 0x78, (byte)0xcd, (byte)0xef // AS_SEQUENCE 0x5678 0xcdef 
		})), UpdatePacket.class);
		
		Assert.assertEquals(2, packet.getType());
		Assert.assertEquals(0, packet.getWithdrawnRoutes().size());
		Assert.assertEquals(1, packet.getPathAttributes().size());
		Assert.assertEquals(0, packet.getNlris().size());		
		
		ASPathAttribute asPath = (ASPathAttribute)packet.getPathAttributes().remove(0);
		ASPathAttribute.PathSegment segment;
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS_PATH, asPath.getTypeCode());
		Assert.assertEquals(ASType.AS_NUMBER_2OCTETS, asPath.getAsType());
		Assert.assertEquals(2, asPath.getPathSegments().size());
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(ASPathAttribute.PathSegmentType.AS_SEQUENCE, segment.getPathSegmentType());
		Assert.assertEquals(2, segment.getAses().size());
		Assert.assertEquals((Integer)0x1234, segment.getAses().remove(0));
		Assert.assertEquals((Integer)0x89ab, segment.getAses().remove(0));
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(ASPathAttribute.PathSegmentType.AS_SET, segment.getPathSegmentType());
		Assert.assertEquals(2, segment.getAses().size());
		Assert.assertEquals((Integer)0x5678, segment.getAses().remove(0));
		Assert.assertEquals((Integer)0xcdef, segment.getAses().remove(0));
	}

	@Test
	public void testASPath2ASSeqeunceOneASNumberASSetTwoASNumberPacket() throws Exception {
		UpdatePacket packet = safeDowncast(decoder.decodeUpdatePacket(buildProtocolPacket(new byte[] {
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				// (byte)0x00, (byte)0x35, // length 53 octets 
				// (byte)0x02, // type code 2 (UPDATE) 
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x0e, // path attributes length (16 octets)
				(byte)0x50, (byte)0x02, (byte)0x00, (byte)0x0a, // Path attribute: 10 octets AS_PATH
				0x02, 0x01, 0x12, 0x34, // AS_SEQUENCE 0x1234 
				0x01, 0x02, 0x56, 0x78, (byte)0xcd, (byte)0xef // AS_SEQUENCE 0x5678 0xcdef 
		})), UpdatePacket.class);
		
		Assert.assertEquals(2, packet.getType());
		Assert.assertEquals(0, packet.getWithdrawnRoutes().size());
		Assert.assertEquals(1, packet.getPathAttributes().size());
		Assert.assertEquals(0, packet.getNlris().size());		
		
		ASPathAttribute asPath = (ASPathAttribute)packet.getPathAttributes().remove(0);
		ASPathAttribute.PathSegment segment;
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS_PATH, asPath.getTypeCode());
		Assert.assertEquals(ASType.AS_NUMBER_2OCTETS, asPath.getAsType());
		Assert.assertEquals(2, asPath.getPathSegments().size());
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(ASPathAttribute.PathSegmentType.AS_SEQUENCE, segment.getPathSegmentType());
		Assert.assertEquals(1, segment.getAses().size());
		Assert.assertEquals((Integer)0x1234, segment.getAses().remove(0));
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(ASPathAttribute.PathSegmentType.AS_SET, segment.getPathSegmentType());
		Assert.assertEquals(2, segment.getAses().size());
		Assert.assertEquals((Integer)0x5678, segment.getAses().remove(0));
		Assert.assertEquals((Integer)0xcdef, segment.getAses().remove(0));
	}

	// ----
	@Test
	public void testASPath2ASSetOneASNumberASSeqeunceOneASNumberPacket() throws Exception {
		UpdatePacket packet = safeDowncast(decoder.decodeUpdatePacket(buildProtocolPacket(new byte[] {
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				// (byte)0x00, (byte)0x35, // length 53 octets 
				// (byte)0x02, // type code 2 (UPDATE) 
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x0c, // path attributes length (12 octets)
				(byte)0x50, (byte)0x02, (byte)0x00, (byte)0x08, // Path attribute: 8 octets AS_PATH
				0x01, 0x01, 0x12, 0x34, // AS_SEQUENCE 0x1234
				0x02, 0x01, 0x56, 0x78, // AS_SEQUENCE 0x5678 
		})), UpdatePacket.class);
		
		Assert.assertEquals(2, packet.getType());
		Assert.assertEquals(0, packet.getWithdrawnRoutes().size());
		Assert.assertEquals(1, packet.getPathAttributes().size());
		Assert.assertEquals(0, packet.getNlris().size());		
		
		ASPathAttribute asPath = (ASPathAttribute)packet.getPathAttributes().remove(0);
		ASPathAttribute.PathSegment segment;
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS_PATH, asPath.getTypeCode());
		Assert.assertEquals(ASType.AS_NUMBER_2OCTETS, asPath.getAsType());
		Assert.assertEquals(2, asPath.getPathSegments().size());
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(ASPathAttribute.PathSegmentType.AS_SET, segment.getPathSegmentType());
		Assert.assertEquals(1, segment.getAses().size());
		Assert.assertEquals((Integer)0x1234, segment.getAses().remove(0));
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(ASPathAttribute.PathSegmentType.AS_SEQUENCE, segment.getPathSegmentType());
		Assert.assertEquals(1, segment.getAses().size());
		Assert.assertEquals((Integer)0x5678, segment.getAses().remove(0));
	}

	@Test
	public void testASPath2ASSetTw0ASNumberASSeqeunceOneASNumberPacket() throws Exception {
		UpdatePacket packet = safeDowncast(decoder.decodeUpdatePacket(buildProtocolPacket(new byte[] {
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				// (byte)0x00, (byte)0x35, // length 53 octets 
				// (byte)0x02, // type code 2 (UPDATE) 
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x0e, // path attributes length (14 octets)
				(byte)0x50, (byte)0x02, (byte)0x00, (byte)0x0a, // Path attribute: 10 octets AS_PATH
				0x01, 0x02, 0x12, 0x34, (byte)0x89, (byte)0xab, // AS_SEQUENCE 0x1234 0x89ab
				0x02, 0x01, 0x56, 0x78, // AS_SEQUENCE 0x5678 
		})), UpdatePacket.class);
		
		Assert.assertEquals(2, packet.getType());
		Assert.assertEquals(0, packet.getWithdrawnRoutes().size());
		Assert.assertEquals(1, packet.getPathAttributes().size());
		Assert.assertEquals(0, packet.getNlris().size());		
		
		ASPathAttribute asPath = (ASPathAttribute)packet.getPathAttributes().remove(0);
		ASPathAttribute.PathSegment segment;
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS_PATH, asPath.getTypeCode());
		Assert.assertEquals(ASType.AS_NUMBER_2OCTETS, asPath.getAsType());
		Assert.assertEquals(2, asPath.getPathSegments().size());
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(ASPathAttribute.PathSegmentType.AS_SET, segment.getPathSegmentType());
		Assert.assertEquals(2, segment.getAses().size());
		Assert.assertEquals((Integer)0x1234, segment.getAses().remove(0));
		Assert.assertEquals((Integer)0x89ab, segment.getAses().remove(0));
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(ASPathAttribute.PathSegmentType.AS_SEQUENCE, segment.getPathSegmentType());
		Assert.assertEquals(1, segment.getAses().size());
		Assert.assertEquals((Integer)0x5678, segment.getAses().remove(0));
	}

	@Test
	public void testASPath2ASSetTw0ASNumberASSeqeunceTwoASNumberPacket() throws Exception {
		UpdatePacket packet = safeDowncast(decoder.decodeUpdatePacket(buildProtocolPacket(new byte[] {
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				// (byte)0x00, (byte)0x35, // length 53 octets 
				// (byte)0x02, // type code 2 (UPDATE) 
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x10, // path attributes length (16 octets)
				(byte)0x50, (byte)0x02, (byte)0x00, (byte)0x0c, // Path attribute: 12 octets AS_PATH
				0x01, 0x02, 0x12, 0x34, (byte)0x89, (byte)0xab, // AS_SEQUENCE 0x1234 0x89ab
				0x02, 0x02, 0x56, 0x78, (byte)0xcd, (byte)0xef // AS_SEQUENCE 0x5678 0xcdef 
		})), UpdatePacket.class);
		
		Assert.assertEquals(2, packet.getType());
		Assert.assertEquals(0, packet.getWithdrawnRoutes().size());
		Assert.assertEquals(1, packet.getPathAttributes().size());
		Assert.assertEquals(0, packet.getNlris().size());		
		
		ASPathAttribute asPath = (ASPathAttribute)packet.getPathAttributes().remove(0);
		ASPathAttribute.PathSegment segment;
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS_PATH, asPath.getTypeCode());
		Assert.assertEquals(ASType.AS_NUMBER_2OCTETS, asPath.getAsType());
		Assert.assertEquals(2, asPath.getPathSegments().size());
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(ASPathAttribute.PathSegmentType.AS_SET, segment.getPathSegmentType());
		Assert.assertEquals(2, segment.getAses().size());
		Assert.assertEquals((Integer)0x1234, segment.getAses().remove(0));
		Assert.assertEquals((Integer)0x89ab, segment.getAses().remove(0));
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(ASPathAttribute.PathSegmentType.AS_SEQUENCE, segment.getPathSegmentType());
		Assert.assertEquals(2, segment.getAses().size());
		Assert.assertEquals((Integer)0x5678, segment.getAses().remove(0));
		Assert.assertEquals((Integer)0xcdef, segment.getAses().remove(0));
	}

	@Test
	public void testASPath2ASSetOneASNumberASSeqeunceTwoASNumberPacket() throws Exception {
		UpdatePacket packet = safeDowncast(decoder.decodeUpdatePacket(buildProtocolPacket(new byte[] {
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				// (byte)0x00, (byte)0x35, // length 53 octets 
				// (byte)0x02, // type code 2 (UPDATE) 
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x0e, // path attributes length (16 octets)
				(byte)0x50, (byte)0x02, (byte)0x00, (byte)0x0a, // Path attribute: 10 octets AS_PATH
				0x01, 0x01, 0x12, 0x34, // AS_SEQUENCE 0x1234 
				0x02, 0x02, 0x56, 0x78, (byte)0xcd, (byte)0xef // AS_SEQUENCE 0x5678 0xcdef 
		})), UpdatePacket.class);
		
		Assert.assertEquals(2, packet.getType());
		Assert.assertEquals(0, packet.getWithdrawnRoutes().size());
		Assert.assertEquals(1, packet.getPathAttributes().size());
		Assert.assertEquals(0, packet.getNlris().size());		
		
		ASPathAttribute asPath = (ASPathAttribute)packet.getPathAttributes().remove(0);
		ASPathAttribute.PathSegment segment;
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS_PATH, asPath.getTypeCode());
		Assert.assertEquals(ASType.AS_NUMBER_2OCTETS, asPath.getAsType());
		Assert.assertEquals(2, asPath.getPathSegments().size());
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(ASPathAttribute.PathSegmentType.AS_SET, segment.getPathSegmentType());
		Assert.assertEquals(1, segment.getAses().size());
		Assert.assertEquals((Integer)0x1234, segment.getAses().remove(0));
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(ASPathAttribute.PathSegmentType.AS_SEQUENCE, segment.getPathSegmentType());
		Assert.assertEquals(2, segment.getAses().size());
		Assert.assertEquals((Integer)0x5678, segment.getAses().remove(0));
		Assert.assertEquals((Integer)0xcdef, segment.getAses().remove(0));
	}

	@Test
	public void testASPath2BadPathTypeOneASNumberPacket() throws Exception {
		(new AssertExecption() {
			
			@Override
			protected void doExecute() {
				safeDowncast(decoder.decodeUpdatePacket(buildProtocolPacket(new byte[] {
						// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
						// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
						// (byte)0x00, (byte)0x35, // length 53 octets 
						// (byte)0x02, // type code 2 (UPDATE) 
						(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
						(byte)0x00, (byte)0x08, // path attributes length (8 octets)
						(byte)0x50, (byte)0x02, (byte)0x00, (byte)0x04, // Path attribute: 4 octets AS_PATH  
						0x03, 0x01, 0x12, 0x34, // Invalid 0x1234 
				})), UpdatePacket.class);	
			}
		}).execute(MalformedASPathAttributeException.class);
	}

	@Test
	public void testASPath2ASSequenceTwoASNumberOneMissingPacket() throws Exception {
		(new AssertExecption() {
			
			@Override
			protected void doExecute() {
				safeDowncast(decoder.decodeUpdatePacket(buildProtocolPacket(new byte[] {
						// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
						// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
						// (byte)0x00, (byte)0x35, // length 53 octets 
						// (byte)0x02, // type code 2 (UPDATE) 
						(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
						(byte)0x00, (byte)0x08, // path attributes length (8 octets)
						(byte)0x50, (byte)0x02, (byte)0x00, (byte)0x04, // Path attribute: 4 octets AS_PATH  
						0x02, 0x02, 0x12, 0x34, // Invalid 0x1234 
				})), UpdatePacket.class);	
			}
		}).execute(MalformedASPathAttributeException.class);
	}
	
}
