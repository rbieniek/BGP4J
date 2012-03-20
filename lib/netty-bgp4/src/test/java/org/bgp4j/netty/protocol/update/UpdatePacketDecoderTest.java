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

import org.bgp4j.net.ASPathAttribute;
import org.bgp4j.net.ASType;
import org.bgp4j.net.AddressFamily;
import org.bgp4j.net.AggregatorPathAttribute;
import org.bgp4j.net.AtomicAggregatePathAttribute;
import org.bgp4j.net.ClusterListPathAttribute;
import org.bgp4j.net.LocalPrefPathAttribute;
import org.bgp4j.net.MultiExitDiscPathAttribute;
import org.bgp4j.net.MultiProtocolReachableNLRI;
import org.bgp4j.net.MultiProtocolUnreachableNLRI;
import org.bgp4j.net.NetworkLayerReachabilityInformation;
import org.bgp4j.net.NextHopPathAttribute;
import org.bgp4j.net.Origin;
import org.bgp4j.net.OriginPathAttribute;
import org.bgp4j.net.OriginatorIDPathAttribute;
import org.bgp4j.net.PathSegmentType;
import org.bgp4j.net.SubsequentAddressFamily;
import org.bgp4j.netty.BGPv4Constants;
import org.bgp4j.netty.BGPv4TestBase;
import org.bgp4j.netty.protocol.ConnectionNotSynchronizedException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class UpdatePacketDecoderTest extends BGPv4TestBase {
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
	public void testDecodeOriginIgpPacket() throws Exception {
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
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_ORIGIN, PathAttributeCodec.typeCode(origin));
		Assert.assertEquals(Origin.IGP, origin.getOrigin());
	}
	
	@Test
	public void testDecodeOriginEgpPacket() throws Exception {
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
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_ORIGIN, PathAttributeCodec.typeCode(origin));
		Assert.assertEquals(Origin.EGP, origin.getOrigin());
	}
	
	@Test
	public void testDecodeOriginIncompletePacket() throws Exception {
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
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_ORIGIN, PathAttributeCodec.typeCode(origin));
		Assert.assertEquals(Origin.INCOMPLETE, origin.getOrigin());
	}
	
	@Test
	public void testDecodeNextHopPacket() throws Exception {
		UpdatePacket packet = safeDowncast(decoder.decodeUpdatePacket(buildProtocolPacket(new byte[] {
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				// (byte)0x00, (byte)0x35, // length 53 octets 
				// (byte)0x02, // type code 2 (UPDATE) 
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x07, // path attributes length (29 octets)
				(byte)0x40, (byte)0x03, (byte)0x04, (byte)0xc0, (byte)0xa8, (byte)0x04, (byte)0x02, // Path attribute: NEXT_HOP 192.168.4.2
		})), UpdatePacket.class);
		
		Assert.assertEquals(2, packet.getType());
		Assert.assertEquals(0, packet.getWithdrawnRoutes().size());
		Assert.assertEquals(1, packet.getPathAttributes().size());
		Assert.assertEquals(0, packet.getNlris().size());		
		
		NextHopPathAttribute nextHop = (NextHopPathAttribute)packet.getPathAttributes().remove(0);
		
		Assert.assertEquals(Inet4Address.getByAddress(new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x04, (byte)0x02 }), nextHop.getNextHop());
	}
	
	@Test
	public void testDecodeNextHopPacketIpMulticastNextHop() throws Exception {
		(new AssertExecption() {
			
			@Override
			protected void doExecute() {
				safeDowncast(decoder.decodeUpdatePacket(buildProtocolPacket(new byte[] {
						// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
						// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
						// (byte)0x00, (byte)0x35, // length 53 octets 
						// (byte)0x02, // type code 2 (UPDATE) 
						(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
						(byte)0x00, (byte)0x07, // path attributes length (29 octets)
						(byte)0x40, (byte)0x03, (byte)0x04, (byte)0xe0, (byte)0x00, (byte)0x00, (byte)0x01, // Path attribute: NEXT_HOP 224.0.0.1
				})), UpdatePacket.class);
		}
		}).execute(InvalidNextHopException.class);
	}
	
	@Test
	public void testDecodeEmptyUpdatePacket() {
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
	public void testDecodeCompletePacket() throws Exception {
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
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_ORIGIN, PathAttributeCodec.typeCode(origin));
		Assert.assertEquals(Origin.INCOMPLETE, origin.getOrigin());
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS_PATH, PathAttributeCodec.typeCode(asPath));
		Assert.assertEquals(ASType.AS_NUMBER_2OCTETS, asPath.getAsType());
		Assert.assertEquals(0, asPath.getPathSegments().size());
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_NEXT_HOP, PathAttributeCodec.typeCode(nextHop));
		Assert.assertEquals(Inet4Address.getByAddress(new byte[] {(byte)0xc0, (byte)0xa8, (byte)0x04, (byte)0x02 }), nextHop.getNextHop());
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_MULTI_EXIT_DISC, PathAttributeCodec.typeCode(multiExitDisc));
		Assert.assertEquals(2048, multiExitDisc.getDiscriminator());
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_LOCAL_PREF, PathAttributeCodec.typeCode(localPref));
		Assert.assertEquals(100, localPref.getLocalPreference());
		
		Assert.assertEquals(0, nlri.getPrefixLength());
		Assert.assertNull(nlri.getPrefix());
	}
	
	@Test
	public void testDecodeWithdrawnDefaultPrefixUpdatePacket() {
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
	public void testDecodeWithdrawnFourBitPrefixPrefixUpdatePacket() {
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
	public void testDecodeWithdrawnEightBitPrefixPrefixUpdatePacket() {
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
	public void testDecodeWithdrawnTwelveBitPrefixPrefixUpdatePacket() {
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
	public void testDecodeWithdrawnSixteenBitPrefixPrefixUpdatePacket() {
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
	public void testDecodeMalformedWithdrawnRoutesNopathAttributeListUpdatePacket() throws Exception {
		(new AssertExecption() {
			
			@Override
			protected void doExecute() {
				decoder.decodeUpdatePacket(buildProtocolPacket(new byte[] {
						// (byte)0x03, // type code UPDATE
						0x00, 0x00, // bad withdrawn routes length (2 octets), points to end of packet 
				}));
			}
		}).execute(ConnectionNotSynchronizedException.class);
	}

	@Test
	public void testDecodeMalformedWithdrawnRoutesLengthOnPacketEndUpdatePacket() throws Exception {
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
	public void testDecodeMalformedWithdrawnRoutesLengthOverEndUpdatePacket() throws Exception {
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
	public void testDecodeAttributeListTooLongUpdatePacket() throws Exception {
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
	public void testDecodeOriginInvalidPacket() throws Exception {
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
	public void testDecodeOriginShortPacket() throws Exception {
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
	public void testDecodeASPath4EmptyPacket() throws Exception {
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
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS4_PATH, PathAttributeCodec.typeCode(asPath));
		Assert.assertEquals(ASType.AS_NUMBER_4OCTETS, asPath.getAsType());
		Assert.assertEquals(0, asPath.getPathSegments().size());
	}

	@Test
	public void testDecodeASPath4ASSequenceOneASNumberPacket() throws Exception {
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
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS4_PATH, PathAttributeCodec.typeCode(asPath));
		Assert.assertEquals(ASType.AS_NUMBER_4OCTETS, asPath.getAsType());
		Assert.assertEquals(1, asPath.getPathSegments().size());
		
		ASPathAttribute.PathSegment segment = asPath.getPathSegments().remove(0);
		
		Assert.assertEquals(PathSegmentType.AS_SEQUENCE, segment.getPathSegmentType());
		Assert.assertEquals(1, segment.getAses().size());
		Assert.assertEquals((Integer)0x1234, segment.getAses().remove(0));
	}

	@Test
	public void testDecodeASPath4ASSequenceTwoASNumberPacket() throws Exception {
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
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS4_PATH, PathAttributeCodec.typeCode(asPath));
		Assert.assertEquals(ASType.AS_NUMBER_4OCTETS, asPath.getAsType());
		Assert.assertEquals(1, asPath.getPathSegments().size());
		
		ASPathAttribute.PathSegment segment = asPath.getPathSegments().remove(0);
		
		Assert.assertEquals(PathSegmentType.AS_SEQUENCE, segment.getPathSegmentType());
		Assert.assertEquals(2, segment.getAses().size());
		Assert.assertEquals((Integer)0x1234, segment.getAses().remove(0));
		Assert.assertEquals((Integer)0x5678, segment.getAses().remove(0));
	}

	@Test
	public void testDecodeASPath4ASSetOneASNumberPacket() throws Exception {
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
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS4_PATH, PathAttributeCodec.typeCode(asPath));
		Assert.assertEquals(ASType.AS_NUMBER_4OCTETS, asPath.getAsType());
		Assert.assertEquals(1, asPath.getPathSegments().size());
		
		ASPathAttribute.PathSegment segment = asPath.getPathSegments().remove(0);
		
		Assert.assertEquals(PathSegmentType.AS_SET, segment.getPathSegmentType());
		Assert.assertEquals(1, segment.getAses().size());
		Assert.assertEquals((Integer)0x1234, segment.getAses().remove(0));
	}

	@Test
	public void testDecodeASPath4ASConfedSequenceOneASNumberPacket() throws Exception {
		UpdatePacket packet = safeDowncast(decoder.decodeUpdatePacket(buildProtocolPacket(new byte[] {
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				// (byte)0x00, (byte)0x35, // length 53 octets 
				// (byte)0x02, // type code 2 (UPDATE) 
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x0a, // path attributes length (10 octets)
				(byte)0x50, (byte)0x11, (byte)0x00, (byte)0x06, // Path attribute: 6 octets AS4_PATH 
				0x03, 0x01, 0x00, 0x00, 0x12, 0x34, // AS_SET 0x1234 
		})), UpdatePacket.class);
		
		Assert.assertEquals(2, packet.getType());
		Assert.assertEquals(0, packet.getWithdrawnRoutes().size());
		Assert.assertEquals(1, packet.getPathAttributes().size());
		Assert.assertEquals(0, packet.getNlris().size());		
		
		ASPathAttribute asPath = (ASPathAttribute)packet.getPathAttributes().remove(0);
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS4_PATH, PathAttributeCodec.typeCode(asPath));
		Assert.assertEquals(ASType.AS_NUMBER_4OCTETS, asPath.getAsType());
		Assert.assertEquals(1, asPath.getPathSegments().size());
		
		ASPathAttribute.PathSegment segment = asPath.getPathSegments().remove(0);
		
		Assert.assertEquals(PathSegmentType.AS_CONFED_SEQUENCE, segment.getPathSegmentType());
		Assert.assertEquals(1, segment.getAses().size());
		Assert.assertEquals((Integer)0x1234, segment.getAses().remove(0));
	}

	@Test
	public void testDecodeASPath4ASConfedSetOneASNumberPacket() throws Exception {
		UpdatePacket packet = safeDowncast(decoder.decodeUpdatePacket(buildProtocolPacket(new byte[] {
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				// (byte)0x00, (byte)0x35, // length 53 octets 
				// (byte)0x02, // type code 2 (UPDATE) 
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x0a, // path attributes length (10 octets)
				(byte)0x50, (byte)0x11, (byte)0x00, (byte)0x06, // Path attribute: 6 octets AS4_PATH 
				0x04, 0x01, 0x00, 0x00, 0x12, 0x34, // AS_SET 0x1234 
		})), UpdatePacket.class);
		
		Assert.assertEquals(2, packet.getType());
		Assert.assertEquals(0, packet.getWithdrawnRoutes().size());
		Assert.assertEquals(1, packet.getPathAttributes().size());
		Assert.assertEquals(0, packet.getNlris().size());		
		
		ASPathAttribute asPath = (ASPathAttribute)packet.getPathAttributes().remove(0);
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS4_PATH, PathAttributeCodec.typeCode(asPath));
		Assert.assertEquals(ASType.AS_NUMBER_4OCTETS, asPath.getAsType());
		Assert.assertEquals(1, asPath.getPathSegments().size());
		
		ASPathAttribute.PathSegment segment = asPath.getPathSegments().remove(0);
		
		Assert.assertEquals(PathSegmentType.AS_CONFED_SET, segment.getPathSegmentType());
		Assert.assertEquals(1, segment.getAses().size());
		Assert.assertEquals((Integer)0x1234, segment.getAses().remove(0));
	}

	@Test
	public void testDecodeASPath4ASSetTwoASNumberPacket() throws Exception {
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
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS4_PATH, PathAttributeCodec.typeCode(asPath));
		Assert.assertEquals(ASType.AS_NUMBER_4OCTETS, asPath.getAsType());
		Assert.assertEquals(1, asPath.getPathSegments().size());
		
		ASPathAttribute.PathSegment segment = asPath.getPathSegments().remove(0);
		
		Assert.assertEquals(PathSegmentType.AS_SET, segment.getPathSegmentType());
		Assert.assertEquals(2, segment.getAses().size());
		Assert.assertEquals((Integer)0x1234, segment.getAses().remove(0));
		Assert.assertEquals((Integer)0x5678, segment.getAses().remove(0));
	}

	@Test
	public void testDecodeASPath4ASSeqeunceOneASNumberASSeqeunceOneASNumberPacket() throws Exception {
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
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS4_PATH, PathAttributeCodec.typeCode(asPath));
		Assert.assertEquals(ASType.AS_NUMBER_4OCTETS, asPath.getAsType());
		Assert.assertEquals(2, asPath.getPathSegments().size());
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(PathSegmentType.AS_SEQUENCE, segment.getPathSegmentType());
		Assert.assertEquals(1, segment.getAses().size());
		Assert.assertEquals((Integer)0x1234, segment.getAses().remove(0));
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(PathSegmentType.AS_SEQUENCE, segment.getPathSegmentType());
		Assert.assertEquals(1, segment.getAses().size());
		Assert.assertEquals((Integer)0x5678, segment.getAses().remove(0));
	}

	@Test
	public void testDecodeASPath4ASSeqeunceTw0ASNumberASSeqeunceOneASNumberPacket() throws Exception {
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
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS4_PATH, PathAttributeCodec.typeCode(asPath));
		Assert.assertEquals(ASType.AS_NUMBER_4OCTETS, asPath.getAsType());
		Assert.assertEquals(2, asPath.getPathSegments().size());
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(PathSegmentType.AS_SEQUENCE, segment.getPathSegmentType());
		Assert.assertEquals(2, segment.getAses().size());
		Assert.assertEquals((Integer)0x1234, segment.getAses().remove(0));
		Assert.assertEquals((Integer)0x89ab, segment.getAses().remove(0));
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(PathSegmentType.AS_SEQUENCE, segment.getPathSegmentType());
		Assert.assertEquals(1, segment.getAses().size());
		Assert.assertEquals((Integer)0x5678, segment.getAses().remove(0));
	}

	@Test
	public void testDecodeASPath4ASSeqeunceTw0ASNumberASSeqeunceTwoASNumberPacket() throws Exception {
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
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS4_PATH, PathAttributeCodec.typeCode(asPath));
		Assert.assertEquals(ASType.AS_NUMBER_4OCTETS, asPath.getAsType());
		Assert.assertEquals(2, asPath.getPathSegments().size());
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(PathSegmentType.AS_SEQUENCE, segment.getPathSegmentType());
		Assert.assertEquals(2, segment.getAses().size());
		Assert.assertEquals((Integer)0x1234, segment.getAses().remove(0));
		Assert.assertEquals((Integer)0x89ab, segment.getAses().remove(0));
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(PathSegmentType.AS_SEQUENCE, segment.getPathSegmentType());
		Assert.assertEquals(2, segment.getAses().size());
		Assert.assertEquals((Integer)0x5678, segment.getAses().remove(0));
		Assert.assertEquals((Integer)0xcdef, segment.getAses().remove(0));
	}

	@Test
	public void testDecodeASPath4ASSeqeunceOneASNumberASSeqeunceTwoASNumberPacket() throws Exception {
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
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS4_PATH, PathAttributeCodec.typeCode(asPath));
		Assert.assertEquals(ASType.AS_NUMBER_4OCTETS, asPath.getAsType());
		Assert.assertEquals(2, asPath.getPathSegments().size());
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(PathSegmentType.AS_SEQUENCE, segment.getPathSegmentType());
		Assert.assertEquals(1, segment.getAses().size());
		Assert.assertEquals((Integer)0x1234, segment.getAses().remove(0));
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(PathSegmentType.AS_SEQUENCE, segment.getPathSegmentType());
		Assert.assertEquals(2, segment.getAses().size());
		Assert.assertEquals((Integer)0x5678, segment.getAses().remove(0));
		Assert.assertEquals((Integer)0xcdef, segment.getAses().remove(0));
	}
	
	@Test
	public void testDecodeASPath4ASSeqeunceOneASNumberASSetOneASNumberPacket() throws Exception {
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
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS4_PATH, PathAttributeCodec.typeCode(asPath));
		Assert.assertEquals(ASType.AS_NUMBER_4OCTETS, asPath.getAsType());
		Assert.assertEquals(2, asPath.getPathSegments().size());
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(PathSegmentType.AS_SEQUENCE, segment.getPathSegmentType());
		Assert.assertEquals(1, segment.getAses().size());
		Assert.assertEquals((Integer)0x1234, segment.getAses().remove(0));
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(PathSegmentType.AS_SET, segment.getPathSegmentType());
		Assert.assertEquals(1, segment.getAses().size());
		Assert.assertEquals((Integer)0x5678, segment.getAses().remove(0));
	}

	@Test
	public void testDecodeASPath4ASSeqeunceTw0ASNumberASSetASNumberPacket() throws Exception {
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
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS4_PATH, PathAttributeCodec.typeCode(asPath));
		Assert.assertEquals(ASType.AS_NUMBER_4OCTETS, asPath.getAsType());
		Assert.assertEquals(2, asPath.getPathSegments().size());
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(PathSegmentType.AS_SEQUENCE, segment.getPathSegmentType());
		Assert.assertEquals(2, segment.getAses().size());
		Assert.assertEquals((Integer)0x1234, segment.getAses().remove(0));
		Assert.assertEquals((Integer)0x89ab, segment.getAses().remove(0));
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(PathSegmentType.AS_SET, segment.getPathSegmentType());
		Assert.assertEquals(1, segment.getAses().size());
		Assert.assertEquals((Integer)0x5678, segment.getAses().remove(0));
	}

	@Test
	public void testDecodeASPath4ASSeqeunceTw0ASNumberASSetTwoASNumberPacket() throws Exception {
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
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS4_PATH, PathAttributeCodec.typeCode(asPath));
		Assert.assertEquals(ASType.AS_NUMBER_4OCTETS, asPath.getAsType());
		Assert.assertEquals(2, asPath.getPathSegments().size());
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(PathSegmentType.AS_SEQUENCE, segment.getPathSegmentType());
		Assert.assertEquals(2, segment.getAses().size());
		Assert.assertEquals((Integer)0x1234, segment.getAses().remove(0));
		Assert.assertEquals((Integer)0x89ab, segment.getAses().remove(0));
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(PathSegmentType.AS_SET, segment.getPathSegmentType());
		Assert.assertEquals(2, segment.getAses().size());
		Assert.assertEquals((Integer)0x5678, segment.getAses().remove(0));
		Assert.assertEquals((Integer)0xcdef, segment.getAses().remove(0));
	}

	@Test
	public void testDecodeASPath4ASSeqeunceOneASNumberASSetTwoASNumberPacket() throws Exception {
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
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS4_PATH, PathAttributeCodec.typeCode(asPath));
		Assert.assertEquals(ASType.AS_NUMBER_4OCTETS, asPath.getAsType());
		Assert.assertEquals(2, asPath.getPathSegments().size());
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(PathSegmentType.AS_SEQUENCE, segment.getPathSegmentType());
		Assert.assertEquals(1, segment.getAses().size());
		Assert.assertEquals((Integer)0x1234, segment.getAses().remove(0));
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(PathSegmentType.AS_SET, segment.getPathSegmentType());
		Assert.assertEquals(2, segment.getAses().size());
		Assert.assertEquals((Integer)0x5678, segment.getAses().remove(0));
		Assert.assertEquals((Integer)0xcdef, segment.getAses().remove(0));
	}

	@Test
	public void testDecodeASPath4ASSetOneASNumberASSeqeunceOneASNumberPacket() throws Exception {
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
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS4_PATH, PathAttributeCodec.typeCode(asPath));
		Assert.assertEquals(ASType.AS_NUMBER_4OCTETS, asPath.getAsType());
		Assert.assertEquals(2, asPath.getPathSegments().size());
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(PathSegmentType.AS_SET, segment.getPathSegmentType());
		Assert.assertEquals(1, segment.getAses().size());
		Assert.assertEquals((Integer)0x1234, segment.getAses().remove(0));
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(PathSegmentType.AS_SEQUENCE, segment.getPathSegmentType());
		Assert.assertEquals(1, segment.getAses().size());
		Assert.assertEquals((Integer)0x5678, segment.getAses().remove(0));
	}

	@Test
	public void testDecodeASPath4ASSetTw0ASNumberASSeqeunceOneASNumberPacket() throws Exception {
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
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS4_PATH, PathAttributeCodec.typeCode(asPath));
		Assert.assertEquals(ASType.AS_NUMBER_4OCTETS, asPath.getAsType());
		Assert.assertEquals(2, asPath.getPathSegments().size());
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(PathSegmentType.AS_SET, segment.getPathSegmentType());
		Assert.assertEquals(2, segment.getAses().size());
		Assert.assertEquals((Integer)0x1234, segment.getAses().remove(0));
		Assert.assertEquals((Integer)0x89ab, segment.getAses().remove(0));
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(PathSegmentType.AS_SEQUENCE, segment.getPathSegmentType());
		Assert.assertEquals(1, segment.getAses().size());
		Assert.assertEquals((Integer)0x5678, segment.getAses().remove(0));
	}

	@Test
	public void testDecodeASPath4ASSetTw0ASNumberASSeqeunceTwoASNumberPacket() throws Exception {
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
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS4_PATH, PathAttributeCodec.typeCode(asPath));
		Assert.assertEquals(ASType.AS_NUMBER_4OCTETS, asPath.getAsType());
		Assert.assertEquals(2, asPath.getPathSegments().size());
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(PathSegmentType.AS_SET, segment.getPathSegmentType());
		Assert.assertEquals(2, segment.getAses().size());
		Assert.assertEquals((Integer)0x1234, segment.getAses().remove(0));
		Assert.assertEquals((Integer)0x89ab, segment.getAses().remove(0));
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(PathSegmentType.AS_SEQUENCE, segment.getPathSegmentType());
		Assert.assertEquals(2, segment.getAses().size());
		Assert.assertEquals((Integer)0x5678, segment.getAses().remove(0));
		Assert.assertEquals((Integer)0xcdef, segment.getAses().remove(0));
	}

	@Test
	public void testDecodeASPath4ASSetOneASNumberASSeqeunceTwoASNumberPacket() throws Exception {
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
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS4_PATH, PathAttributeCodec.typeCode(asPath));
		Assert.assertEquals(ASType.AS_NUMBER_4OCTETS, asPath.getAsType());
		Assert.assertEquals(2, asPath.getPathSegments().size());
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(PathSegmentType.AS_SET, segment.getPathSegmentType());
		Assert.assertEquals(1, segment.getAses().size());
		Assert.assertEquals((Integer)0x1234, segment.getAses().remove(0));
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(PathSegmentType.AS_SEQUENCE, segment.getPathSegmentType());
		Assert.assertEquals(2, segment.getAses().size());
		Assert.assertEquals((Integer)0x5678, segment.getAses().remove(0));
		Assert.assertEquals((Integer)0xcdef, segment.getAses().remove(0));
	}

	@Test
	public void testDecodeASPath4BadPathTypeOneASNumberPacket() throws Exception {
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
						0x05, 0x01, 0x00, 0x00, 0x12, 0x34, // Invalid 0x1234 
				})), UpdatePacket.class);	
			}
		}).execute(MalformedASPathAttributeException.class);
	}

	@Test
	public void testDecodeASPath4ASSequenceTwoASNumberOneMissingPacket() throws Exception {
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
	public void testDecodeASPath2EmptyPacket() throws Exception {
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
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS_PATH, PathAttributeCodec.typeCode(asPath));
		Assert.assertEquals(ASType.AS_NUMBER_2OCTETS, asPath.getAsType());
		Assert.assertEquals(0, asPath.getPathSegments().size());
	}

	@Test
	public void testDecodeASPath2ASSequenceOneASNumberPacket() throws Exception {
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
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS_PATH, PathAttributeCodec.typeCode(asPath));
		Assert.assertEquals(ASType.AS_NUMBER_2OCTETS, asPath.getAsType());
		Assert.assertEquals(1, asPath.getPathSegments().size());
		
		ASPathAttribute.PathSegment segment = asPath.getPathSegments().remove(0);
		
		Assert.assertEquals(PathSegmentType.AS_SEQUENCE, segment.getPathSegmentType());
		Assert.assertEquals(1, segment.getAses().size());
		Assert.assertEquals((Integer)0x1234, segment.getAses().remove(0));
	}

	@Test
	public void testDecodeASPath2ASSequenceTwoASNumberPacket() throws Exception {
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
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS_PATH, PathAttributeCodec.typeCode(asPath));
		Assert.assertEquals(ASType.AS_NUMBER_2OCTETS, asPath.getAsType());
		Assert.assertEquals(1, asPath.getPathSegments().size());
		
		ASPathAttribute.PathSegment segment = asPath.getPathSegments().remove(0);
		
		Assert.assertEquals(PathSegmentType.AS_SEQUENCE, segment.getPathSegmentType());
		Assert.assertEquals(2, segment.getAses().size());
		Assert.assertEquals((Integer)0x1234, segment.getAses().remove(0));
		Assert.assertEquals((Integer)0x5678, segment.getAses().remove(0));
	}

	@Test
	public void testDecodeASPath2ASSetOneASNumberPacket() throws Exception {
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
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS_PATH, PathAttributeCodec.typeCode(asPath));
		Assert.assertEquals(ASType.AS_NUMBER_2OCTETS, asPath.getAsType());
		Assert.assertEquals(1, asPath.getPathSegments().size());
		
		ASPathAttribute.PathSegment segment = asPath.getPathSegments().remove(0);
		
		Assert.assertEquals(PathSegmentType.AS_SET, segment.getPathSegmentType());
		Assert.assertEquals(1, segment.getAses().size());
		Assert.assertEquals((Integer)0x1234, segment.getAses().remove(0));
	}

	@Test
	public void testDecodeASPath2ASSetTwoASNumberPacket() throws Exception {
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
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS_PATH, PathAttributeCodec.typeCode(asPath));
		Assert.assertEquals(ASType.AS_NUMBER_2OCTETS, asPath.getAsType());
		Assert.assertEquals(1, asPath.getPathSegments().size());
		
		ASPathAttribute.PathSegment segment = asPath.getPathSegments().remove(0);
		
		Assert.assertEquals(PathSegmentType.AS_SET, segment.getPathSegmentType());
		Assert.assertEquals(2, segment.getAses().size());
		Assert.assertEquals((Integer)0x1234, segment.getAses().remove(0));
		Assert.assertEquals((Integer)0x5678, segment.getAses().remove(0));
	}

	@Test
	public void testDecodeASPath2ASSeqeunceOneASNumberASSeqeunceOneASNumberPacket() throws Exception {
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
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS_PATH, PathAttributeCodec.typeCode(asPath));
		Assert.assertEquals(ASType.AS_NUMBER_2OCTETS, asPath.getAsType());
		Assert.assertEquals(2, asPath.getPathSegments().size());
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(PathSegmentType.AS_SEQUENCE, segment.getPathSegmentType());
		Assert.assertEquals(1, segment.getAses().size());
		Assert.assertEquals((Integer)0x1234, segment.getAses().remove(0));
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(PathSegmentType.AS_SEQUENCE, segment.getPathSegmentType());
		Assert.assertEquals(1, segment.getAses().size());
		Assert.assertEquals((Integer)0x5678, segment.getAses().remove(0));
	}

	@Test
	public void testDecodeASPath2ASSeqeunceTw0ASNumberASSeqeunceOneASNumberPacket() throws Exception {
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
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS_PATH, PathAttributeCodec.typeCode(asPath));
		Assert.assertEquals(ASType.AS_NUMBER_2OCTETS, asPath.getAsType());
		Assert.assertEquals(2, asPath.getPathSegments().size());
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(PathSegmentType.AS_SEQUENCE, segment.getPathSegmentType());
		Assert.assertEquals(2, segment.getAses().size());
		Assert.assertEquals((Integer)0x1234, segment.getAses().remove(0));
		Assert.assertEquals((Integer)0x89ab, segment.getAses().remove(0));
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(PathSegmentType.AS_SEQUENCE, segment.getPathSegmentType());
		Assert.assertEquals(1, segment.getAses().size());
		Assert.assertEquals((Integer)0x5678, segment.getAses().remove(0));
	}

	@Test
	public void testDecodeASPath2ASSeqeunceTw0ASNumberASSeqeunceTwoASNumberPacket() throws Exception {
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
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS_PATH, PathAttributeCodec.typeCode(asPath));
		Assert.assertEquals(ASType.AS_NUMBER_2OCTETS, asPath.getAsType());
		Assert.assertEquals(2, asPath.getPathSegments().size());
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(PathSegmentType.AS_SEQUENCE, segment.getPathSegmentType());
		Assert.assertEquals(2, segment.getAses().size());
		Assert.assertEquals((Integer)0x1234, segment.getAses().remove(0));
		Assert.assertEquals((Integer)0x89ab, segment.getAses().remove(0));
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(PathSegmentType.AS_SEQUENCE, segment.getPathSegmentType());
		Assert.assertEquals(2, segment.getAses().size());
		Assert.assertEquals((Integer)0x5678, segment.getAses().remove(0));
		Assert.assertEquals((Integer)0xcdef, segment.getAses().remove(0));
	}

	@Test
	public void testDecodeASPath2ASSeqeunceOneASNumberASSeqeunceTwoASNumberPacket() throws Exception {
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
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS_PATH, PathAttributeCodec.typeCode(asPath));
		Assert.assertEquals(ASType.AS_NUMBER_2OCTETS, asPath.getAsType());
		Assert.assertEquals(2, asPath.getPathSegments().size());
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(PathSegmentType.AS_SEQUENCE, segment.getPathSegmentType());
		Assert.assertEquals(1, segment.getAses().size());
		Assert.assertEquals((Integer)0x1234, segment.getAses().remove(0));
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(PathSegmentType.AS_SEQUENCE, segment.getPathSegmentType());
		Assert.assertEquals(2, segment.getAses().size());
		Assert.assertEquals((Integer)0x5678, segment.getAses().remove(0));
		Assert.assertEquals((Integer)0xcdef, segment.getAses().remove(0));
	}
	
	@Test
	public void testDecodeASPath2ASSeqeunceOneASNumberASSetOneASNumberPacket() throws Exception {
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
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS_PATH, PathAttributeCodec.typeCode(asPath));
		Assert.assertEquals(ASType.AS_NUMBER_2OCTETS, asPath.getAsType());
		Assert.assertEquals(2, asPath.getPathSegments().size());
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(PathSegmentType.AS_SEQUENCE, segment.getPathSegmentType());
		Assert.assertEquals(1, segment.getAses().size());
		Assert.assertEquals((Integer)0x1234, segment.getAses().remove(0));
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(PathSegmentType.AS_SET, segment.getPathSegmentType());
		Assert.assertEquals(1, segment.getAses().size());
		Assert.assertEquals((Integer)0x5678, segment.getAses().remove(0));
	}

	@Test
	public void testDecodeASPath2ASSeqeunceTw0ASNumberASSetASNumberPacket() throws Exception {
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
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS_PATH, PathAttributeCodec.typeCode(asPath));
		Assert.assertEquals(ASType.AS_NUMBER_2OCTETS, asPath.getAsType());
		Assert.assertEquals(2, asPath.getPathSegments().size());
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(PathSegmentType.AS_SEQUENCE, segment.getPathSegmentType());
		Assert.assertEquals(2, segment.getAses().size());
		Assert.assertEquals((Integer)0x1234, segment.getAses().remove(0));
		Assert.assertEquals((Integer)0x89ab, segment.getAses().remove(0));
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(PathSegmentType.AS_SET, segment.getPathSegmentType());
		Assert.assertEquals(1, segment.getAses().size());
		Assert.assertEquals((Integer)0x5678, segment.getAses().remove(0));
	}

	@Test
	public void testDecodeASPath2ASSeqeunceTw0ASNumberASSetTwoASNumberPacket() throws Exception {
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
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS_PATH, PathAttributeCodec.typeCode(asPath));
		Assert.assertEquals(ASType.AS_NUMBER_2OCTETS, asPath.getAsType());
		Assert.assertEquals(2, asPath.getPathSegments().size());
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(PathSegmentType.AS_SEQUENCE, segment.getPathSegmentType());
		Assert.assertEquals(2, segment.getAses().size());
		Assert.assertEquals((Integer)0x1234, segment.getAses().remove(0));
		Assert.assertEquals((Integer)0x89ab, segment.getAses().remove(0));
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(PathSegmentType.AS_SET, segment.getPathSegmentType());
		Assert.assertEquals(2, segment.getAses().size());
		Assert.assertEquals((Integer)0x5678, segment.getAses().remove(0));
		Assert.assertEquals((Integer)0xcdef, segment.getAses().remove(0));
	}

	@Test
	public void testDecodeASPath2ASSeqeunceOneASNumberASSetTwoASNumberPacket() throws Exception {
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
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS_PATH, PathAttributeCodec.typeCode(asPath));
		Assert.assertEquals(ASType.AS_NUMBER_2OCTETS, asPath.getAsType());
		Assert.assertEquals(2, asPath.getPathSegments().size());
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(PathSegmentType.AS_SEQUENCE, segment.getPathSegmentType());
		Assert.assertEquals(1, segment.getAses().size());
		Assert.assertEquals((Integer)0x1234, segment.getAses().remove(0));
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(PathSegmentType.AS_SET, segment.getPathSegmentType());
		Assert.assertEquals(2, segment.getAses().size());
		Assert.assertEquals((Integer)0x5678, segment.getAses().remove(0));
		Assert.assertEquals((Integer)0xcdef, segment.getAses().remove(0));
	}

	@Test
	public void testDecodeASPath2ASSetOneASNumberASSeqeunceOneASNumberPacket() throws Exception {
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
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS_PATH, PathAttributeCodec.typeCode(asPath));
		Assert.assertEquals(ASType.AS_NUMBER_2OCTETS, asPath.getAsType());
		Assert.assertEquals(2, asPath.getPathSegments().size());
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(PathSegmentType.AS_SET, segment.getPathSegmentType());
		Assert.assertEquals(1, segment.getAses().size());
		Assert.assertEquals((Integer)0x1234, segment.getAses().remove(0));
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(PathSegmentType.AS_SEQUENCE, segment.getPathSegmentType());
		Assert.assertEquals(1, segment.getAses().size());
		Assert.assertEquals((Integer)0x5678, segment.getAses().remove(0));
	}

	@Test
	public void testDecodeASPath2ASSetTw0ASNumberASSeqeunceOneASNumberPacket() throws Exception {
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
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS_PATH, PathAttributeCodec.typeCode(asPath));
		Assert.assertEquals(ASType.AS_NUMBER_2OCTETS, asPath.getAsType());
		Assert.assertEquals(2, asPath.getPathSegments().size());
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(PathSegmentType.AS_SET, segment.getPathSegmentType());
		Assert.assertEquals(2, segment.getAses().size());
		Assert.assertEquals((Integer)0x1234, segment.getAses().remove(0));
		Assert.assertEquals((Integer)0x89ab, segment.getAses().remove(0));
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(PathSegmentType.AS_SEQUENCE, segment.getPathSegmentType());
		Assert.assertEquals(1, segment.getAses().size());
		Assert.assertEquals((Integer)0x5678, segment.getAses().remove(0));
	}

	@Test
	public void testDecodeASPath2ASSetTw0ASNumberASSeqeunceTwoASNumberPacket() throws Exception {
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
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS_PATH, PathAttributeCodec.typeCode(asPath));
		Assert.assertEquals(ASType.AS_NUMBER_2OCTETS, asPath.getAsType());
		Assert.assertEquals(2, asPath.getPathSegments().size());
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(PathSegmentType.AS_SET, segment.getPathSegmentType());
		Assert.assertEquals(2, segment.getAses().size());
		Assert.assertEquals((Integer)0x1234, segment.getAses().remove(0));
		Assert.assertEquals((Integer)0x89ab, segment.getAses().remove(0));
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(PathSegmentType.AS_SEQUENCE, segment.getPathSegmentType());
		Assert.assertEquals(2, segment.getAses().size());
		Assert.assertEquals((Integer)0x5678, segment.getAses().remove(0));
		Assert.assertEquals((Integer)0xcdef, segment.getAses().remove(0));
	}

	@Test
	public void testDecodeASPath2ASSetOneASNumberASSeqeunceTwoASNumberPacket() throws Exception {
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
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS_PATH, PathAttributeCodec.typeCode(asPath));
		Assert.assertEquals(ASType.AS_NUMBER_2OCTETS, asPath.getAsType());
		Assert.assertEquals(2, asPath.getPathSegments().size());
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(PathSegmentType.AS_SET, segment.getPathSegmentType());
		Assert.assertEquals(1, segment.getAses().size());
		Assert.assertEquals((Integer)0x1234, segment.getAses().remove(0));
		
		segment = asPath.getPathSegments().remove(0);		
		Assert.assertEquals(PathSegmentType.AS_SEQUENCE, segment.getPathSegmentType());
		Assert.assertEquals(2, segment.getAses().size());
		Assert.assertEquals((Integer)0x5678, segment.getAses().remove(0));
		Assert.assertEquals((Integer)0xcdef, segment.getAses().remove(0));
	}

	@Test
	public void testDecodeASPath2BadPathTypeOneASNumberPacket() throws Exception {
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
						0x05, 0x01, 0x12, 0x34, // Invalid 0x1234 
				})), UpdatePacket.class);	
			}
		}).execute(MalformedASPathAttributeException.class);
	}

	@Test
	public void testDecodeASPath2ASSequenceTwoASNumberOneMissingPacket() throws Exception {
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

	@Test
	public void testDecodeAggregatorPathAttribute() throws Exception {
		UpdatePacket packet; 
		AggregatorPathAttribute aggregator;
		
		packet = safeDowncast(decoder.decodeUpdatePacket(buildProtocolPacket(new byte[] {
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				// (byte)0x00, (byte)0x35, // length 53 octets 
				// (byte)0x02, // type code 2 (UPDATE) 
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x09, // path attributes length (9 octets)
				(byte)0x40, (byte)0x07, (byte)0x06, // Path attribute: AS_AGGREGATOR
				0x12, 0x34, // AS number 0x1234
				(byte)0xc0, (byte)0xa8, (byte)0x04, (byte)0x02, // Aggregator IP 192.168.4.2
		})), UpdatePacket.class);
		
		Assert.assertEquals(2, packet.getType());
		Assert.assertEquals(0, packet.getWithdrawnRoutes().size());
		Assert.assertEquals(1, packet.getPathAttributes().size());
		Assert.assertEquals(0, packet.getNlris().size());		
		
		aggregator = (AggregatorPathAttribute)packet.getPathAttributes().remove(0);
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AGGREGATOR, PathAttributeCodec.typeCode(aggregator));
		Assert.assertEquals(0x1234, aggregator.getAsNumber());
		Assert.assertEquals(ASType.AS_NUMBER_2OCTETS, aggregator.getAsType());
		Assert.assertEquals(Inet4Address.getByAddress(new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x04, (byte)0x02, }), aggregator.getAggregator());
		
		packet = safeDowncast(decoder.decodeUpdatePacket(buildProtocolPacket(new byte[] {
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				// (byte)0x00, (byte)0x35, // length 53 octets 
				// (byte)0x02, // type code 2 (UPDATE) 
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x0b, // path attributes length (11 octets)
				(byte)0x40, (byte)0x12, (byte)0x08, // Path attribute: AS_AGGREGATOR
				0x56, 0x78, 0x12, 0x34, // AS number 0x56781234
				(byte)0xc0, (byte)0xa8, (byte)0x04, (byte)0x02, // Aggregator IP 192.168.4.2
		})), UpdatePacket.class);
		
		Assert.assertEquals(2, packet.getType());
		Assert.assertEquals(0, packet.getWithdrawnRoutes().size());
		Assert.assertEquals(1, packet.getPathAttributes().size());
		Assert.assertEquals(0, packet.getNlris().size());		
		
		aggregator = (AggregatorPathAttribute)packet.getPathAttributes().remove(0);
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS4_AGGREGATOR, PathAttributeCodec.typeCode(aggregator));
		Assert.assertEquals(0x56781234, aggregator.getAsNumber());
		Assert.assertEquals(ASType.AS_NUMBER_4OCTETS, aggregator.getAsType());
		Assert.assertEquals(Inet4Address.getByAddress(new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x04, (byte)0x02, }), aggregator.getAggregator());
	}

	@Test
	public void testDecodeAtomiAcggregatePathAttribute() throws Exception {
		UpdatePacket packet; 
		
		packet = safeDowncast(decoder.decodeUpdatePacket(buildProtocolPacket(new byte[] {
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				// (byte)0x00, (byte)0x35, // length 53 octets 
				// (byte)0x02, // type code 2 (UPDATE) 
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x03, // path attributes length (9 octets)
				(byte)0x40, (byte)0x06, (byte)0x00, // Path attribute: ATOMIC_AGGREGATE
		})), UpdatePacket.class);
		
		Assert.assertEquals(2, packet.getType());
		Assert.assertEquals(0, packet.getWithdrawnRoutes().size());
		Assert.assertEquals(1, packet.getPathAttributes().size());
		Assert.assertEquals(0, packet.getNlris().size());		
		
		Assert.assertTrue(packet.getPathAttributes().remove(0) instanceof AtomicAggregatePathAttribute);	
	}

	@Test
	public void testDecodeLocalPrefPathAttribute() throws Exception {
		UpdatePacket packet; 
		LocalPrefPathAttribute localPref;
		
		packet = safeDowncast(decoder.decodeUpdatePacket(buildProtocolPacket(new byte[] {
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				// (byte)0x00, (byte)0x35, // length 53 octets 
				// (byte)0x02, // type code 2 (UPDATE) 
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x07, // path attributes length (9 octets)
				(byte)0x40, (byte)0x05, (byte)0x04, // Path attribute: LOCAL_PREF
				0x00, 0x00, 0x00, 0x64, // LOCAL_PREF 100
		})), UpdatePacket.class);
		
		Assert.assertEquals(2, packet.getType());
		Assert.assertEquals(0, packet.getWithdrawnRoutes().size());
		Assert.assertEquals(1, packet.getPathAttributes().size());
		Assert.assertEquals(0, packet.getNlris().size());		
		
		localPref = (LocalPrefPathAttribute)packet.getPathAttributes().remove(0);
		
		Assert.assertEquals(100, localPref.getLocalPreference());
	}

	@Test
	public void testDecodeOneNlriUpdatePacket() {
		UpdatePacket packet; 
		NetworkLayerReachabilityInformation nlri;
		
		packet = safeDowncast(decoder.decodeUpdatePacket(buildProtocolPacket(new byte[] {
				// (byte)0x03, // type code UPDATE
				0x00, 0x00, // withdrawn routes length (0 octets)
				0x00, 0x00, // Total path attributes length  (0 octets)
				0x10, (byte)0xac, 0x10, // NLRI 172.16/16 
		})), UpdatePacket.class);

		Assert.assertEquals(2, packet.getType());
		Assert.assertEquals(0, packet.getWithdrawnRoutes().size());
		Assert.assertEquals(0, packet.getPathAttributes().size());
		Assert.assertEquals(1, packet.getNlris().size());
		
		nlri = packet.getNlris().remove(0);

		Assert.assertEquals(16, nlri.getPrefixLength());
		assertArraysEquals(new byte[] { (byte)0xac, 0x10} , nlri.getPrefix());
	}	

	@Test
	public void testDecodeTwoNlriUpdatePacket() {
		UpdatePacket packet; 
		NetworkLayerReachabilityInformation nlri;
		
		packet = safeDowncast(decoder.decodeUpdatePacket(buildProtocolPacket(new byte[] {
				// (byte)0x03, // type code UPDATE
				0x00, 0x00, // withdrawn routes length (0 octets)
				0x00, 0x00, // Total path attributes length  (0 octets)
				0x10, (byte)0xac, 0x10, // NLRI 172.16/16
				0x1c, (byte)0xc0, (byte)0xa8, 0x20, 0, // NLRI 192.168.32.0/28
		})), UpdatePacket.class);

		Assert.assertEquals(2, packet.getType());
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
	public void testDecodeBogusNlriUpdatePacket() throws Exception {
		(new AssertExecption() {			
			@Override
			protected void doExecute() {
				safeDowncast(decoder.decodeUpdatePacket(buildProtocolPacket(new byte[] {
						// (byte)0x03, // type code UPDATE
						0x00, 0x00, // withdrawn routes length (0 octets)
						0x00, 0x00, // Total path attributes length  (0 octets)
						0x10, (byte)0xac, 0x10, // NLRI 172.16/16
						0x1c, (byte)0xc0, (byte)0xa8, 0x20,  // NLRI 192.168.32/28 bogus one octet missing
				})), UpdatePacket.class);			}
		}).execute(InvalidNetworkFieldException.class);
	}	
	
	@Test
	public void testDecodeValidMpReachNlriNullNextHopZeroNlriUpdatePacket() {
		UpdatePacket packet; 
		
		packet = safeDowncast(decoder.decodeUpdatePacket(buildProtocolPacket(new byte[] {
				// (byte)0x03, // type code UPDATE
				0x00, 0x00, // withdrawn routes length (0 octets)
				0x00, 0x08, // Total path attributes length  (8 octets)
				(byte)0x80, 0x0e, 0x05, // Path Attribute MP_REACH_NLRI
				0x00, 0x01, 0x01, 0x00, 0x00, // AFI(IPv4) SAFI(UNICAT_ROUTING) NEXT_HOP length 0, null NLRI
		})), UpdatePacket.class);

		Assert.assertEquals(2, packet.getType());
		Assert.assertEquals(0, packet.getWithdrawnRoutes().size());
		Assert.assertEquals(1, packet.getPathAttributes().size());
		Assert.assertEquals(0, packet.getNlris().size());

		MultiProtocolReachableNLRI mp = (MultiProtocolReachableNLRI)packet.getPathAttributes().remove(0);
		
		Assert.assertEquals(AddressFamily.IPv4, mp.getAddressFamily());
		Assert.assertEquals(SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, mp.getSubsequentAddressFamily());
		Assert.assertNull(mp.getNextHopAddress());
		Assert.assertEquals(0, mp.getNlris().size());
	}	

	@Test
	public void testDecodeValidMpReachNlriFourByteNextHopZeroNlriUpdatePacket() {
		UpdatePacket packet; 
		
		packet = safeDowncast(decoder.decodeUpdatePacket(buildProtocolPacket(new byte[] {
				// (byte)0x03, // type code UPDATE
				0x00, 0x00, // withdrawn routes length (0 octets)
				0x00, 0x0c, // Total path attributes length  (12 octets)
				(byte)0x80, 0x0e, 0x09, // Path Attribute MP_REACH_NLRI
				0x00, 0x01, 0x01, 0x04, (byte)0xc0, (byte)0xa8, 0x04, 0x02, 0x00, // AFI(IPv4) SAFI(UNICAT_ROUTING) NEXT_HOP 4 octets 192.168.4.2, null NLRI
		})), UpdatePacket.class);

		Assert.assertEquals(2, packet.getType());
		Assert.assertEquals(0, packet.getWithdrawnRoutes().size());
		Assert.assertEquals(1, packet.getPathAttributes().size());
		Assert.assertEquals(0, packet.getNlris().size());

		MultiProtocolReachableNLRI mp = (MultiProtocolReachableNLRI)packet.getPathAttributes().remove(0);
		
		Assert.assertEquals(AddressFamily.IPv4, mp.getAddressFamily());
		Assert.assertEquals(SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, mp.getSubsequentAddressFamily());
		assertArraysEquals(new byte[] { (byte)0xc0, (byte)0xa8, 0x04, 0x02, }, mp.getNextHopAddress());
		Assert.assertEquals(0, mp.getNlris().size());
	}	

	@Test
	public void testDecodeValidMpReachNlriFourByteNextHopOneNlriUpdatePacket() {
		UpdatePacket packet; 
		
		packet = safeDowncast(decoder.decodeUpdatePacket(buildProtocolPacket(new byte[] {
				// (byte)0x03, // type code UPDATE
				0x00, 0x00, // withdrawn routes length (0 octets)
				0x00, 0x0f, // Total path attributes length  (15 octets)
				(byte)0x80, 0x0e, 0x0c, // Path Attribute MP_REACH_NLRI
				0x00, 0x01, 0x01, // AFI(IPv4) SAFI(UNICAT_ROUTING) 
				0x04, (byte)0xc0, (byte)0xa8, 0x04, 0x02, // NEXT_HOP 4 octets 192.168.4.2, 
				0x00, // reserved 
				0x0c, (byte)0xab, 0x10, //  NLRI 172.16/12
		})), UpdatePacket.class);

		Assert.assertEquals(2, packet.getType());
		Assert.assertEquals(0, packet.getWithdrawnRoutes().size());
		Assert.assertEquals(1, packet.getPathAttributes().size());
		Assert.assertEquals(0, packet.getNlris().size());

		MultiProtocolReachableNLRI mp = (MultiProtocolReachableNLRI)packet.getPathAttributes().remove(0);
		
		Assert.assertEquals(AddressFamily.IPv4, mp.getAddressFamily());
		Assert.assertEquals(SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, mp.getSubsequentAddressFamily());
		assertArraysEquals(new byte[] { (byte)0xc0, (byte)0xa8, 0x04, 0x02, }, mp.getNextHopAddress());
		Assert.assertEquals(1, mp.getNlris().size());
		
		NetworkLayerReachabilityInformation nlri = mp.getNlris().remove(0);
		
		Assert.assertEquals(12, nlri.getPrefixLength());
		assertArraysEquals(new byte[] { (byte)0xab, 0x10, } , nlri.getPrefix());
	}	

	@Test
	public void testDecodeValidMpReachNlriFourByteNextHopTwoNlriUpdatePacket() {
		UpdatePacket packet; 
		
		packet = safeDowncast(decoder.decodeUpdatePacket(buildProtocolPacket(new byte[] {
				// (byte)0x03, // type code UPDATE
				0x00, 0x00, // withdrawn routes length (0 octets)
				0x00, 0x13, // Total path attributes length  (19 octets)
				(byte)0x80, 0x0e, 0x10, // Path Attribute MP_REACH_NLRI
				0x00, 0x01, 0x01, // AFI(IPv4) SAFI(UNICAT_ROUTING) 
				0x04, (byte)0xc0, (byte)0xa8, 0x04, 0x02, // NEXT_HOP 4 octets 192.168.4.2, 
				0x00, // reserved 
				0x0c, (byte)0xab, 0x10, //  NLRI 172.16.0.0/12
				0x14, (byte)0xc0, (byte)0xa8, (byte)0xf0, //  NLRI 192.168.255.0/20
		})), UpdatePacket.class);

		Assert.assertEquals(2, packet.getType());
		Assert.assertEquals(0, packet.getWithdrawnRoutes().size());
		Assert.assertEquals(1, packet.getPathAttributes().size());
		Assert.assertEquals(0, packet.getNlris().size());

		MultiProtocolReachableNLRI mp = (MultiProtocolReachableNLRI)packet.getPathAttributes().remove(0);
		
		Assert.assertEquals(AddressFamily.IPv4, mp.getAddressFamily());
		Assert.assertEquals(SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, mp.getSubsequentAddressFamily());
		assertArraysEquals(new byte[] { (byte)0xc0, (byte)0xa8, 0x04, 0x02, }, mp.getNextHopAddress());
		Assert.assertEquals(2, mp.getNlris().size());

		NetworkLayerReachabilityInformation nlri = mp.getNlris().remove(0);
		
		Assert.assertEquals(12, nlri.getPrefixLength());
		assertArraysEquals(new byte[] { (byte)0xab, 0x10, } , nlri.getPrefix());
		
		nlri = mp.getNlris().remove(0);
		Assert.assertEquals(20, nlri.getPrefixLength());
		assertArraysEquals(new byte[] { (byte)0xc0, (byte)0xa8, (byte)0xf0, } , nlri.getPrefix());
	}	


	@Test
	public void testDecodeValidMpReachNlriNullNextHopOneNlriUpdatePacket() {
		UpdatePacket packet; 
		
		packet = safeDowncast(decoder.decodeUpdatePacket(buildProtocolPacket(new byte[] {
				// (byte)0x03, // type code UPDATE
				0x00, 0x00, // withdrawn routes length (0 octets)
				0x00, 0x0b, // Total path attributes length  (11 octets)
				(byte)0x80, 0x0e, 0x08, // Path Attribute MP_REACH_NLRI
				0x00, 0x01, 0x01, // AFI(IPv4) SAFI(UNICAT_ROUTING) 
				0x00, // NEXT_HOP 0 octets  
				0x00, // reserved 
				0x0c, (byte)0xab, 0x10, //  NLRI 172.16/12
		})), UpdatePacket.class);

		Assert.assertEquals(2, packet.getType());
		Assert.assertEquals(0, packet.getWithdrawnRoutes().size());
		Assert.assertEquals(1, packet.getPathAttributes().size());
		Assert.assertEquals(0, packet.getNlris().size());

		MultiProtocolReachableNLRI mp = (MultiProtocolReachableNLRI)packet.getPathAttributes().remove(0);
		
		Assert.assertEquals(AddressFamily.IPv4, mp.getAddressFamily());
		Assert.assertEquals(SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, mp.getSubsequentAddressFamily());
		Assert.assertNull(mp.getNextHopAddress());
		Assert.assertEquals(1, mp.getNlris().size());
		
		NetworkLayerReachabilityInformation nlri = mp.getNlris().remove(0);
		
		Assert.assertEquals(12, nlri.getPrefixLength());
		assertArraysEquals(new byte[] { (byte)0xab, 0x10, } , nlri.getPrefix());
	}	
	
	@Test
	public void testDecodeBogusAfiMpReachNlriUpdatePacket() throws Exception {
		(new AssertExecption() {
			
			@Override
			protected void doExecute() {
				decoder.decodeUpdatePacket(buildProtocolPacket(new byte[] {
						// (byte)0x03, // type code UPDATE
						0x00, 0x00, // withdrawn routes length (0 octets)
						0x00, 0x08, // Total path attributes length  (8 octets)
						(byte)0x80, 0x0e, 0x05, // Path Attribute MP_REACH_NLRI
						0x00, (byte)0xF1, // Bogus AFI 
						0x01, // SAFI(UNICAT_ROUTING) 
						0x00, // NEXT_HOP length 0
						0x00, // reserved
				}));
			}
		}).execute(OptionalAttributeErrorException.class);		
	}	

	@Test
	public void testDecodeBogusSafiMpReachNlriUpdatePacket() throws Exception {
		(new AssertExecption() {
			
			@Override
			protected void doExecute() {
				decoder.decodeUpdatePacket(buildProtocolPacket(new byte[] {
						// (byte)0x03, // type code UPDATE
						0x00, 0x00, // withdrawn routes length (0 octets)
						0x00, 0x08, // Total path attributes length  (8 octets)
						(byte)0x80, 0x0e, 0x05, // Path Attribute MP_REACH_NLRI
						0x00, (byte)0x01, // AFI(IPv4) 
						0x04, // Bogus SAFI 
						0x00, // NEXT_HOP length 0
						0x00, // reserved
				}));
			}
		}).execute(OptionalAttributeErrorException.class);		
	}	

	@Test
	public void testDecodeBogusNextHopMpReachNlriUpdatePacket() throws Exception {
		(new AssertExecption() {
			
			@Override
			protected void doExecute() {
				decoder.decodeUpdatePacket(buildProtocolPacket(new byte[] {
						// (byte)0x03, // type code UPDATE
						0x00, 0x00, // withdrawn routes length (0 octets)
						0x00, 0x08, // Total path attributes length  (8 octets)
						(byte)0x80, 0x0e, 0x05, // Path Attribute MP_REACH_NLRI
						0x00, (byte)0x01, // AFI(IPv4) 
						0x01, // SAFI(Unicast routing)
						0x02, // NEXT_HOP length 2
						0x00, // reserved
				}));
			}
		}).execute(OptionalAttributeErrorException.class);		
	}	

	@Test
	public void testDecodeMissingReservedMpReachNlriUpdatePacket() throws Exception {
		(new AssertExecption() {
			
			@Override
			protected void doExecute() {
				decoder.decodeUpdatePacket(buildProtocolPacket(new byte[] {
						// (byte)0x03, // type code UPDATE
						0x00, 0x00, // withdrawn routes length (0 octets)
						0x00, 0x07, // Total path attributes length  (8 octets)
						(byte)0x80, 0x0e, 0x04, // Path Attribute MP_REACH_NLRI
						0x00, (byte)0x01, // AFI(IPv4) 
						0x01, // SAFI(Unicast routing)
						0x00, // NEXT_HOP length 0
				}));
			}
		}).execute(OptionalAttributeErrorException.class);		

		(new AssertExecption() {
			
			@Override
			protected void doExecute() {
				decoder.decodeUpdatePacket(buildProtocolPacket(new byte[] {
						// (byte)0x03, // type code UPDATE
						0x00, 0x00, // withdrawn routes length (0 octets)
						0x00, 0x0c, // Total path attributes length  (12 octets)
						(byte)0x80, 0x0e, 0x09, // Path Attribute MP_REACH_NLRI
						0x00, (byte)0x01, // AFI(IPv4) 
						0x01, // SAFI(Unicast routing)
						0x02, // NEXT_HOP length 2
						0x00, 0x00,
						0x0c, (byte)0xab, 0x10, //  NLRI 172.16/12
				}));
			}
		}).execute(OptionalAttributeErrorException.class);		
	}	

	@Test
	public void testDecodeBadNlriMpReachNlriUpdatePacket() throws Exception {
		(new AssertExecption() {
			
			@Override
			protected void doExecute() {
				decoder.decodeUpdatePacket(buildProtocolPacket(new byte[] {
						// (byte)0x03, // type code UPDATE
						0x00, 0x00, // withdrawn routes length (0 octets)
						0x00, 0x0a, // Total path attributes length  (10 octets)
						(byte)0x80, 0x0e, 0x07, // Path Attribute MP_REACH_NLRI
						0x00, (byte)0x01, // AFI(IPv4) 
						0x01, // SAFI(Unicast routing)
						0x00, // NEXT_HOP length 0
						0x00, // reserved
						0x0c, (byte)0xab, // one octet missing on NLRI
				}));
			}
		}).execute(OptionalAttributeErrorException.class);		
	}
	
	@Test
	public void testDecodeValidMpUnreachNlriZeroNlriUpdatePacket() {
		UpdatePacket packet; 
		
		packet = safeDowncast(decoder.decodeUpdatePacket(buildProtocolPacket(new byte[] {
				// (byte)0x03, // type code UPDATE
				0x00, 0x00, // withdrawn routes length (0 octets)
				0x00, 0x06, // Total path attributes length  (8 octets)
				(byte)0x80, 0x0f, 0x03, // Path Attribute MP_UNREACH_NLRI
				0x00, 0x01, // AFI(IPv4)
				0x01, // SAFI(UNICAT_ROUTING)
		})), UpdatePacket.class);

		Assert.assertEquals(2, packet.getType());
		Assert.assertEquals(0, packet.getWithdrawnRoutes().size());
		Assert.assertEquals(1, packet.getPathAttributes().size());
		Assert.assertEquals(0, packet.getNlris().size());

		MultiProtocolUnreachableNLRI mp = (MultiProtocolUnreachableNLRI)packet.getPathAttributes().remove(0);
		
		Assert.assertEquals(AddressFamily.IPv4, mp.getAddressFamily());
		Assert.assertEquals(SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, mp.getSubsequentAddressFamily());
		Assert.assertEquals(0, mp.getNlris().size());
	}	

	@Test
	public void testDecodeValidMpUnreachNlriOneNlriUpdatePacket() {
		UpdatePacket packet; 
		
		packet = safeDowncast(decoder.decodeUpdatePacket(buildProtocolPacket(new byte[] {
				// (byte)0x03, // type code UPDATE
				0x00, 0x00, // withdrawn routes length (0 octets)
				0x00, 0x09, // Total path attributes length  (9 octets)
				(byte)0x80, 0x0f, 0x06, // Path Attribute MP_UNREACH_NLRI
				0x00, 0x01, // AFI(IPv4)
				0x01, // SAFI(UNICAT_ROUTING)
				0x0c, (byte)0xab, 0x10, //  NLRI 172.16.0.0/12
		})), UpdatePacket.class);

		Assert.assertEquals(2, packet.getType());
		Assert.assertEquals(0, packet.getWithdrawnRoutes().size());
		Assert.assertEquals(1, packet.getPathAttributes().size());
		Assert.assertEquals(0, packet.getNlris().size());

		MultiProtocolUnreachableNLRI mp = (MultiProtocolUnreachableNLRI)packet.getPathAttributes().remove(0);
		
		Assert.assertEquals(AddressFamily.IPv4, mp.getAddressFamily());
		Assert.assertEquals(SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, mp.getSubsequentAddressFamily());
		Assert.assertEquals(1, mp.getNlris().size());

		NetworkLayerReachabilityInformation nlri = mp.getNlris().remove(0);
		
		Assert.assertEquals(12, nlri.getPrefixLength());
		assertArraysEquals(new byte[] { (byte)0xab, 0x10, } , nlri.getPrefix());
	}	

	@Test
	public void testDecodeValidMpUnreachNlriTwoNlriUpdatePacket() {
		UpdatePacket packet; 
		
		packet = safeDowncast(decoder.decodeUpdatePacket(buildProtocolPacket(new byte[] {
				// (byte)0x03, // type code UPDATE
				0x00, 0x00, // withdrawn routes length (0 octets)
				0x00, 0x0d, // Total path attributes length  (13 octets)
				(byte)0x80, 0x0f, 0x0a, // Path Attribute MP_UNREACH_NLRI
				0x00, 0x01, // AFI(IPv4)
				0x01, // SAFI(UNICAT_ROUTING)
				0x0c, (byte)0xab, 0x10, //  NLRI 172.16.0.0/12
				0x14, (byte)0xc0, (byte)0xa8, (byte)0xf0, //  NLRI 192.168.255.0/20
		})), UpdatePacket.class);

		Assert.assertEquals(2, packet.getType());
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
	public void testDecodeBadNlriMpUnreachNlriUpdatePacket() throws Exception {
		(new AssertExecption() {
			
			@Override
			protected void doExecute() {
				decoder.decodeUpdatePacket(buildProtocolPacket(new byte[] {
						// (byte)0x03, // type code UPDATE
						0x00, 0x00, // withdrawn routes length (0 octets)
						0x00, 0x08, // Total path attributes length  (8 octets)
						(byte)0x80, 0x0f, 0x05, // Path Attribute MP_UNREACH_NLRI
						0x00, (byte)0x01, // AFI(IPv4) 
						0x01, // SAFI(Unicast routing)
						0x0c, (byte)0xab, // one octet missing on NLRI
				}));
			}
		}).execute(OptionalAttributeErrorException.class);		
	}	
	
	@Test
	public void testDecodeOriginatorIDPacket() throws Exception {
		UpdatePacket packet = safeDowncast(decoder.decodeUpdatePacket(buildProtocolPacket(new byte[] {
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				// (byte)0x00, (byte)0x35, // length 53 octets 
				// (byte)0x02, // type code 2 (UPDATE) 
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x07, // path attributes length (29 octets)
				(byte)0x80, (byte)0x09, (byte)0x04, (byte)0xc0, (byte)0xa8, (byte)0x04, (byte)0x02, // Path attribute: ORIGINATOR_ID 0xc0a80402
		})), UpdatePacket.class);
		
		Assert.assertEquals(2, packet.getType());
		Assert.assertEquals(0, packet.getWithdrawnRoutes().size());
		Assert.assertEquals(1, packet.getPathAttributes().size());
		Assert.assertEquals(0, packet.getNlris().size());		
		
		OriginatorIDPathAttribute originator = (OriginatorIDPathAttribute)packet.getPathAttributes().remove(0);
		
		Assert.assertEquals(0xc0a80402, originator.getOriginatorID());
	}
	
	@Test
	public void testDecodeClusterListPacketOneCLusterID() throws Exception {
		UpdatePacket packet = safeDowncast(decoder.decodeUpdatePacket(buildProtocolPacket(new byte[] {
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				// (byte)0x00, (byte)0x35, // length 53 octets 
				// (byte)0x02, // type code 2 (UPDATE) 
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x07, // path attributes length (29 octets)
				(byte)0x80, (byte)0x0a, (byte)0x04, // PAth Attribute Cluster List (4 octets)) 
				(byte)0xc0, (byte)0xa8, (byte)0x04, (byte)0x02, // cluster ID 0xc0a80402
		})), UpdatePacket.class);
		
		Assert.assertEquals(2, packet.getType());
		Assert.assertEquals(0, packet.getWithdrawnRoutes().size());
		Assert.assertEquals(1, packet.getPathAttributes().size());
		Assert.assertEquals(0, packet.getNlris().size());		
		
		ClusterListPathAttribute originator = (ClusterListPathAttribute)packet.getPathAttributes().remove(0);
		
		Assert.assertEquals(1, originator.getClusterIds().size());
		Assert.assertEquals(new Integer(0xc0a80402), originator.getClusterIds().remove(0));
	}
	
	@Test
	public void testDecodeClusterListPacketTwoCLusterID() throws Exception {
		UpdatePacket packet = safeDowncast(decoder.decodeUpdatePacket(buildProtocolPacket(new byte[] {
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
				// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				// (byte)0x00, (byte)0x35, // length 53 octets 
				// (byte)0x02, // type code 2 (UPDATE) 
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x0b, // path attributes length (29 octets)
				(byte)0x80, (byte)0x0a, (byte)0x08, // PAth Attribute Cluster List (4 octets)) 
				(byte)0xc0, (byte)0xa8, (byte)0x04, (byte)0x02, // cluster ID 0xc0a80402
				(byte)0xc0, (byte)0xa8, (byte)0x05, (byte)0x03, // cluster ID 0xc0a80503
		})), UpdatePacket.class);
		
		Assert.assertEquals(2, packet.getType());
		Assert.assertEquals(0, packet.getWithdrawnRoutes().size());
		Assert.assertEquals(1, packet.getPathAttributes().size());
		Assert.assertEquals(0, packet.getNlris().size());		
		
		ClusterListPathAttribute originator = (ClusterListPathAttribute)packet.getPathAttributes().remove(0);
		
		Assert.assertEquals(2, originator.getClusterIds().size());
		Assert.assertEquals(new Integer(0xc0a80402), originator.getClusterIds().remove(0));
		Assert.assertEquals(new Integer(0xc0a80503), originator.getClusterIds().remove(0));
	}
	
	@Test
	public void testDecodeClusterListPacketBrokenCLusterID() throws Exception {
		(new AssertExecption() {
			
			@Override
			protected void doExecute() {
				decoder.decodeUpdatePacket(buildProtocolPacket(new byte[] {
						// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker 
						// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
						// (byte)0x00, (byte)0x35, // length 53 octets 
						// (byte)0x02, // type code 2 (UPDATE) 
						(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
						(byte)0x00, (byte)0x05, // path attributes length (29 octets)
						(byte)0x80, (byte)0x0a, (byte)0x02, // PAth Attribute Cluster List (2 octets)) 
						(byte)0xc0, (byte)0xa8,  // cluster ID 0xc0a80402 2 octets missing
				}));
			}
		}).execute(OptionalAttributeErrorException.class);
	}
}
