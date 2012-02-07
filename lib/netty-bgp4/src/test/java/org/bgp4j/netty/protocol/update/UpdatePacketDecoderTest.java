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
import org.bgp4j.netty.protocol.update.ASPathAttribute.PathSegmentType;
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
	public void testEncodeOriginPathAttribute() {
		assertBufferContents(new byte[] {
				(byte)0x40, (byte)0x01, (byte)0x01, (byte)0x00, // Path attribute: ORIGIN IGP
		}, (new OriginPathAttribute(Origin.IGP)).encodePathAttribute());

		assertBufferContents(new byte[] {
				(byte)0x40, (byte)0x01, (byte)0x01, (byte)0x01, // Path attribute: ORIGIN EGP
		}, (new OriginPathAttribute(Origin.EGP)).encodePathAttribute());

		assertBufferContents(new byte[] {
				(byte)0x40, (byte)0x01, (byte)0x01, (byte)0x02, // Path attribute: ORIGIN INCOMPLETE
		}, (new OriginPathAttribute(Origin.INCOMPLETE)).encodePathAttribute());
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
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_ORIGIN, origin.getTypeCode());
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
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_ORIGIN, origin.getTypeCode());
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
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_ORIGIN, origin.getTypeCode());
		Assert.assertEquals(Origin.INCOMPLETE, origin.getOrigin());
	}
	
	@Test
	public void testEncodeNextHopPathAttribute() throws Exception {
		assertBufferContents(new byte[] {
				(byte)0x40, (byte)0x03, (byte)0x04, (byte)0xc0, (byte)0xa8, (byte)0x04, (byte)0x02, // Path attribute: NEXT_HOP 192.168.4.2
		}, (new NextHopPathAttribute((Inet4Address)Inet4Address.getByAddress(new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x04, (byte)0x02 })).encodePathAttribute()));
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
	public void testEncodeMultiExitDiscPathAttribute() {
		assertBufferContents(new byte[] {
				(byte)0x80, (byte)0x04, (byte)0x04, (byte)0x00, (byte)0x00, (byte)0x08, (byte)0x00, // Path attribute: MULT_EXIT_DISC 2048
		}, (new MultiExitDiscPathAttribute(2048)).encodePathAttribute());
		
	}
	
	@Test
	public void testEncodeAS2PathAttribute() {
		assertBufferContents(new byte[] {
				(byte)0x40, (byte)0x02, (byte)0x00, // Path attribute: AS_PATH emtpy 
		}, (new ASPathAttribute(ASType.AS_NUMBER_2OCTETS)).encodePathAttribute());

		assertBufferContents(new byte[] {
				(byte)0x40, (byte)0x02, (byte)0x04, // Path attribute: AS_PATH  
				0x01, 0x01, 0x12, 0x34, // AS_SET 1 AS 0x1234
		}, (new ASPathAttribute(ASType.AS_NUMBER_2OCTETS, new ASPathAttribute.PathSegment[] {
				new ASPathAttribute.PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_SET, new int[] {
						0x1234
				}),
		})).encodePathAttribute());

		assertBufferContents(new byte[] {
				(byte)0x40, (byte)0x02, (byte)0x06, // Path attribute: AS_PATH  
				0x01, 0x02, 0x12, 0x34, 0x56, 0x78 // AS_SET 2 AS 0x1234 0x5678
		}, (new ASPathAttribute(ASType.AS_NUMBER_2OCTETS, new ASPathAttribute.PathSegment[] {
				new ASPathAttribute.PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_SET, new int[] {
						0x1234, 0x5678
				}),
		})).encodePathAttribute());

		assertBufferContents(new byte[] {
				(byte)0x40, (byte)0x02, (byte)0x08, // Path attribute: AS_PATH  
				0x01, 0x01, 0x12, 0x34, // AS_SET 1 AS 0x1234 
				0x01, 0x01, 0x56, 0x78, // AS_SET 1 AS 0x5678
		}, (new ASPathAttribute(ASType.AS_NUMBER_2OCTETS, new ASPathAttribute.PathSegment[] {
				new ASPathAttribute.PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_SET, new int[] {
						0x1234, 
				}),
				new ASPathAttribute.PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_SET, new int[] {
						0x5678, 
				}),
		})).encodePathAttribute());

		assertBufferContents(new byte[] {
				(byte)0x40, (byte)0x02, (byte)0x0a, // Path attribute: AS_PATH  
				0x01, 0x02, 0x12, 0x34, (byte)0x9a, (byte)0xbc, // AS_SET 2 AS 0x1234 0x9abc 
				0x01, 0x01, 0x56, 0x78, // AS_SET 1 AS 0x5678
		}, (new ASPathAttribute(ASType.AS_NUMBER_2OCTETS, new ASPathAttribute.PathSegment[] {
				new ASPathAttribute.PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_SET, new int[] {
						0x1234, 0x9abc,
				}),
				new ASPathAttribute.PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_SET, new int[] {
						0x5678, 
				}),
		})).encodePathAttribute());

		assertBufferContents(new byte[] {
				(byte)0x40, (byte)0x02, (byte)0x0c, // Path attribute: AS_PATH  
				0x01, 0x02, 0x12, 0x34, (byte)0x9a, (byte)0xbc, // AS_SET 2 AS 0x1234 0x9abc 
				0x01, 0x02, 0x56, 0x78, (byte)0x9a, (byte)0xbc, // AS_SET 2 AS 0x5678 0x9abc
		}, (new ASPathAttribute(ASType.AS_NUMBER_2OCTETS, new ASPathAttribute.PathSegment[] {
				new ASPathAttribute.PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_SET, new int[] {
						0x1234, 0x9abc,
				}),
				new ASPathAttribute.PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_SET, new int[] {
						0x5678, 0x9abc,
				}),
		})).encodePathAttribute());
		
		assertBufferContents(new byte[] {
				(byte)0x40, (byte)0x02, (byte)0x04, // Path attribute: AS_PATH  
				0x02, 0x01, 0x12, 0x34, // AS_SEQUENCE 1 AS 0x1234
		}, (new ASPathAttribute(ASType.AS_NUMBER_2OCTETS, new ASPathAttribute.PathSegment[] {
				new ASPathAttribute.PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_SEQUENCE, new int[] {
						0x1234
				}),
		})).encodePathAttribute());

		assertBufferContents(new byte[] {
				(byte)0x40, (byte)0x02, (byte)0x06, // Path attribute: AS_PATH  
				0x02, 0x02, 0x12, 0x34, 0x56, 0x78 // AS_SEQUENCE 2 AS 0x1234 0x5678
		}, (new ASPathAttribute(ASType.AS_NUMBER_2OCTETS, new ASPathAttribute.PathSegment[] {
				new ASPathAttribute.PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_SEQUENCE, new int[] {
						0x1234, 0x5678
				}),
		})).encodePathAttribute());

		assertBufferContents(new byte[] {
				(byte)0x40, (byte)0x02, (byte)0x08, // Path attribute: AS_PATH  
				0x02, 0x01, 0x12, 0x34, // AS_SET 1 AS 0x1234 
				0x02, 0x01, 0x56, 0x78, // AS_SET 1 AS 0x5678
		}, (new ASPathAttribute(ASType.AS_NUMBER_2OCTETS, new ASPathAttribute.PathSegment[] {
				new ASPathAttribute.PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_SEQUENCE, new int[] {
						0x1234, 
				}),
				new ASPathAttribute.PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_SEQUENCE, new int[] {
						0x5678, 
				}),
		})).encodePathAttribute());

		assertBufferContents(new byte[] {
				(byte)0x40, (byte)0x02, (byte)0x0a, // Path attribute: AS_PATH  
				0x02, 0x02, 0x12, 0x34, (byte)0x9a, (byte)0xbc, // AS_SEQUENCE 2 AS 0x1234 0x9abc 
				0x02, 0x01, 0x56, 0x78, // AS_SEQUENCE 1 AS 0x5678
		}, (new ASPathAttribute(ASType.AS_NUMBER_2OCTETS, new ASPathAttribute.PathSegment[] {
				new ASPathAttribute.PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_SEQUENCE, new int[] {
						0x1234, 0x9abc,
				}),
				new ASPathAttribute.PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_SEQUENCE, new int[] {
						0x5678, 
				}),
		})).encodePathAttribute());

		assertBufferContents(new byte[] {
				(byte)0x40, (byte)0x02, (byte)0x0c, // Path attribute: AS_PATH  
				0x02, 0x02, 0x12, 0x34, (byte)0x9a, (byte)0xbc, // AS_SEQUENCE 2 AS 0x1234 0x9abc 
				0x02, 0x02, 0x56, 0x78, (byte)0x9a, (byte)0xbc, // AS_SEQUENCE 2 AS 0x5678 0x9abc
		}, (new ASPathAttribute(ASType.AS_NUMBER_2OCTETS, new ASPathAttribute.PathSegment[] {
				new ASPathAttribute.PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_SEQUENCE, new int[] {
						0x1234, 0x9abc,
				}),
				new ASPathAttribute.PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_SEQUENCE, new int[] {
						0x5678, 0x9abc,
				}),
		})).encodePathAttribute());

	}

	@Test
	public void testEncodeAS4PathAttribute() {
		assertBufferContents(new byte[] {
				(byte)0x40, (byte)0x11, (byte)0x00, // Path attribute: AS4_PATH emtpy 
		}, (new ASPathAttribute(ASType.AS_NUMBER_4OCTETS)).encodePathAttribute());

		assertBufferContents(new byte[] {
				(byte)0x40, (byte)0x11, (byte)0x06, // Path attribute: AS4_PATH  
				0x01, 0x01, 0x12, 0x34, 0x12, 0x34, // AS_SET 1 AS 0x12341234
		}, (new ASPathAttribute(ASType.AS_NUMBER_4OCTETS, new ASPathAttribute.PathSegment[] {
				new ASPathAttribute.PathSegment(ASType.AS_NUMBER_4OCTETS, PathSegmentType.AS_SET, new int[] {
						0x12341234
				}),
		})).encodePathAttribute());

		assertBufferContents(new byte[] {
				(byte)0x40, (byte)0x11, (byte)0x0a, // Path attribute: AS4_PATH  
				0x01, 0x02, 0x12, 0x34, 0x12, 0x34, 0x56, 0x78, 0x56, 0x78, // AS_SET 2 AS 0x12341234 0x56785678
		}, (new ASPathAttribute(ASType.AS_NUMBER_4OCTETS, new ASPathAttribute.PathSegment[] {
				new ASPathAttribute.PathSegment(ASType.AS_NUMBER_4OCTETS, PathSegmentType.AS_SET, new int[] {
						0x12341234, 0x56785678
				}),
		})).encodePathAttribute());

		assertBufferContents(new byte[] {
				(byte)0x40, (byte)0x11, (byte)0x0c, // Path attribute: AS4_PATH  
				0x01, 0x01, 0x12, 0x34, 0x12, 0x34, // AS_SET 1 AS 0x12341234 
				0x01, 0x01, 0x56, 0x78, 0x56, 0x78, // AS_SET 1 AS 0x56785678
		}, (new ASPathAttribute(ASType.AS_NUMBER_4OCTETS, new ASPathAttribute.PathSegment[] {
				new ASPathAttribute.PathSegment(ASType.AS_NUMBER_4OCTETS, PathSegmentType.AS_SET, new int[] {
						0x12341234, 
				}),
				new ASPathAttribute.PathSegment(ASType.AS_NUMBER_4OCTETS, PathSegmentType.AS_SET, new int[] {
						0x56785678, 
				}),
		})).encodePathAttribute());

		assertBufferContents(new byte[] {
				(byte)0x40, (byte)0x11, (byte)0x10, // Path attribute: AS4_PATH  
				0x01, 0x02, 0x12, 0x34, 0x12, 0x34, (byte)0x9a, (byte)0xbc, (byte)0x9a, (byte)0xbc, // AS_SET 2 AS 0x12341234 0x9abc9abc 
				0x01, 0x01, 0x56, 0x78, 0x56, 0x78, // AS_SET 1 AS 0x56785678
		}, (new ASPathAttribute(ASType.AS_NUMBER_4OCTETS, new ASPathAttribute.PathSegment[] {
				new ASPathAttribute.PathSegment(ASType.AS_NUMBER_4OCTETS, PathSegmentType.AS_SET, new int[] {
						0x12341234, 0x9abc9abc,
				}),
				new ASPathAttribute.PathSegment(ASType.AS_NUMBER_4OCTETS, PathSegmentType.AS_SET, new int[] {
						0x56785678, 
				}),
		})).encodePathAttribute());

		assertBufferContents(new byte[] {
				(byte)0x40, (byte)0x11, (byte)0x14, // Path attribute: AS4_PATH  
				0x01, 0x02, 0x12, 0x34, 0x12, 0x34, (byte)0x9a, (byte)0xbc, (byte)0x9a, (byte)0xbc, // AS_SET 2 AS 0x1234 0x9abc 
				0x01, 0x02, 0x56, 0x78, 0x56, 0x78, (byte)0x9a, (byte)0xbc, (byte)0x9a, (byte)0xbc, // AS_SET 2 AS 0x5678 0x9abc
		}, (new ASPathAttribute(ASType.AS_NUMBER_4OCTETS, new ASPathAttribute.PathSegment[] {
				new ASPathAttribute.PathSegment(ASType.AS_NUMBER_4OCTETS, PathSegmentType.AS_SET, new int[] {
						0x12341234, 0x9abc9abc,
				}),
				new ASPathAttribute.PathSegment(ASType.AS_NUMBER_4OCTETS, PathSegmentType.AS_SET, new int[] {
						0x56785678, 0x9abc9abc,
				}),
		})).encodePathAttribute());
		
		assertBufferContents(new byte[] {
				(byte)0x40, (byte)0x11, (byte)0x06, // Path attribute: AS4_PATH  
				0x02, 0x01, 0x12, 0x34, 0x12, 0x34, // AS_SEQUENCE 1 AS 0x12341234
		}, (new ASPathAttribute(ASType.AS_NUMBER_4OCTETS, new ASPathAttribute.PathSegment[] {
				new ASPathAttribute.PathSegment(ASType.AS_NUMBER_4OCTETS, PathSegmentType.AS_SEQUENCE, new int[] {
						0x12341234
				}),
		})).encodePathAttribute());

		assertBufferContents(new byte[] {
				(byte)0x40, (byte)0x11, (byte)0x0a, // Path attribute: AS4_PATH  
				0x02, 0x02, 0x12, 0x34, 0x12, 0x34, 0x56, 0x78, 0x56, 0x78, // AS_SEQUENCE 2 AS 0x12341234 0x56785678
		}, (new ASPathAttribute(ASType.AS_NUMBER_4OCTETS, new ASPathAttribute.PathSegment[] {
				new ASPathAttribute.PathSegment(ASType.AS_NUMBER_4OCTETS, PathSegmentType.AS_SEQUENCE, new int[] {
						0x12341234, 0x56785678
				}),
		})).encodePathAttribute());

		assertBufferContents(new byte[] {
				(byte)0x40, (byte)0x11, (byte)0x0c, // Path attribute: AS4_PATH  
				0x02, 0x01, 0x12, 0x34, 0x12, 0x34, // AS_SET 1 AS 0x12341234 
				0x02, 0x01, 0x56, 0x78, 0x56, 0x78, // AS_SET 1 AS 0x56785678
		}, (new ASPathAttribute(ASType.AS_NUMBER_4OCTETS, new ASPathAttribute.PathSegment[] {
				new ASPathAttribute.PathSegment(ASType.AS_NUMBER_4OCTETS, PathSegmentType.AS_SEQUENCE, new int[] {
						0x12341234, 
				}),
				new ASPathAttribute.PathSegment(ASType.AS_NUMBER_4OCTETS, PathSegmentType.AS_SEQUENCE, new int[] {
						0x56785678, 
				}),
		})).encodePathAttribute());

		assertBufferContents(new byte[] {
				(byte)0x40, (byte)0x11, (byte)0x10, // Path attribute: AS4_PATH  
				0x02, 0x02, 0x12, 0x34, 0x12, 0x34, (byte)0x9a, (byte)0xbc, (byte)0x9a, (byte)0xbc, // AS_SEQUENCE 2 AS 0x12341234 0x9abc 9abc
				0x02, 0x01, 0x56, 0x78, 0x56, 0x78, // AS_SEQUENCE 1 AS 0x56785678
		}, (new ASPathAttribute(ASType.AS_NUMBER_4OCTETS, new ASPathAttribute.PathSegment[] {
				new ASPathAttribute.PathSegment(ASType.AS_NUMBER_4OCTETS, PathSegmentType.AS_SEQUENCE, new int[] {
						0x12341234, 0x9abc9abc,
				}),
				new ASPathAttribute.PathSegment(ASType.AS_NUMBER_4OCTETS, PathSegmentType.AS_SEQUENCE, new int[] {
						0x56785678, 
				}),
		})).encodePathAttribute());

		assertBufferContents(new byte[] {
				(byte)0x40, (byte)0x11, (byte)0x14, // Path attribute: AS4_PATH  
				0x02, 0x02, 0x12, 0x34, 0x12, 0x34, (byte)0x9a, (byte)0xbc, (byte)0x9a, (byte)0xbc, // AS_SEQUENCE 2 AS 0x12341234 0x9abc9abc 
				0x02, 0x02, 0x56, 0x78, 0x56, 0x78, (byte)0x9a, (byte)0xbc, (byte)0x9a, (byte)0xbc, // AS_SEQUENCE 2 AS 0x56785678 0x9abc9abc
		}, (new ASPathAttribute(ASType.AS_NUMBER_4OCTETS, new ASPathAttribute.PathSegment[] {
				new ASPathAttribute.PathSegment(ASType.AS_NUMBER_4OCTETS, PathSegmentType.AS_SEQUENCE, new int[] {
						0x12341234, 0x9abc9abc,
				}),
				new ASPathAttribute.PathSegment(ASType.AS_NUMBER_4OCTETS, PathSegmentType.AS_SEQUENCE, new int[] {
						0x56785678, 0x9abc9abc,
				}),
		})).encodePathAttribute());

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
		}).execute(BadMessageLengthException.class);
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
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS4_PATH, asPath.getTypeCode());
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
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS4_PATH, asPath.getTypeCode());
		Assert.assertEquals(ASType.AS_NUMBER_4OCTETS, asPath.getAsType());
		Assert.assertEquals(1, asPath.getPathSegments().size());
		
		ASPathAttribute.PathSegment segment = asPath.getPathSegments().remove(0);
		
		Assert.assertEquals(ASPathAttribute.PathSegmentType.AS_SEQUENCE, segment.getPathSegmentType());
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
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS4_PATH, asPath.getTypeCode());
		Assert.assertEquals(ASType.AS_NUMBER_4OCTETS, asPath.getAsType());
		Assert.assertEquals(1, asPath.getPathSegments().size());
		
		ASPathAttribute.PathSegment segment = asPath.getPathSegments().remove(0);
		
		Assert.assertEquals(ASPathAttribute.PathSegmentType.AS_SET, segment.getPathSegmentType());
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
						0x03, 0x01, 0x00, 0x00, 0x12, 0x34, // Invalid 0x1234 
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
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS_PATH, asPath.getTypeCode());
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
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS_PATH, asPath.getTypeCode());
		Assert.assertEquals(ASType.AS_NUMBER_2OCTETS, asPath.getAsType());
		Assert.assertEquals(1, asPath.getPathSegments().size());
		
		ASPathAttribute.PathSegment segment = asPath.getPathSegments().remove(0);
		
		Assert.assertEquals(ASPathAttribute.PathSegmentType.AS_SEQUENCE, segment.getPathSegmentType());
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
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS_PATH, asPath.getTypeCode());
		Assert.assertEquals(ASType.AS_NUMBER_2OCTETS, asPath.getAsType());
		Assert.assertEquals(1, asPath.getPathSegments().size());
		
		ASPathAttribute.PathSegment segment = asPath.getPathSegments().remove(0);
		
		Assert.assertEquals(ASPathAttribute.PathSegmentType.AS_SET, segment.getPathSegmentType());
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
						0x03, 0x01, 0x12, 0x34, // Invalid 0x1234 
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

	/*
	 * ------------------------------------------------------------------------------------------------------------------------ 
	 */
	
	@Test
	public void testEncodeEmptyUpdatePacket() {
		UpdatePacket update = new UpdatePacket();
		
		assertBufferContents(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0x00, (byte)0x17, // length 23
				(byte)0x02, // type code UPDATE
				0x00, 0x00, // withdrawn routes length (0 octets)
				0x00, 0x00, // Total path attributes length  (0 octets)
		}, update.encodePacket());
	}	
	
	@Test
	public void testEncodeCompletePacket() throws Exception {
		UpdatePacket update = new UpdatePacket();
		
		update.getPathAttributes().add(new OriginPathAttribute(Origin.INCOMPLETE));
		update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_2OCTETS));
		update.getPathAttributes().add(new NextHopPathAttribute((Inet4Address)Inet4Address.getByAddress(new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x04, (byte)0x02 })));
		update.getPathAttributes().add(new MultiExitDiscPathAttribute(2048));
		update.getPathAttributes().add(new LocalPrefPathAttribute(100));
		update.getNlris().add(new NetworkLayerReachabilityInformation(0, null));
		
		assertBufferContents(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0x00, (byte)0x34, // length 52
				(byte)0x02, // type code UPDATE
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x1c, // path attributes length (28 octets)
				(byte)0x40, (byte)0x01, (byte)0x01, (byte)0x02, // Path attribute: ORIGIN INCOMPLETE  
				(byte)0x40, (byte)0x02, (byte)0x00, // Path attribute: AS_PATH emtpy 
				(byte)0x40, (byte)0x03, (byte)0x04, (byte)0xc0, (byte)0xa8, (byte)0x04, (byte)0x02, // Path attribute: NEXT_HOP 192.168.4.2
				(byte)0x80, (byte)0x04, (byte)0x04, (byte)0x00, (byte)0x00, (byte)0x08, (byte)0x00, // Path attribute: MULT_EXIT_DISC 2048
				(byte)0x40, (byte)0x05, (byte)0x04, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x64, // Path attribute: LOCAL_PREF 100
				(byte)0x00 // NLRI: 0.0.0.0/0	
		}, update.encodePacket());
	}
	
	@Test
	public void testEncodeWithdrawnDefaultPrefixUpdatePacket() {
		UpdatePacket update = new UpdatePacket();
		
		update.getWithdrawnRoutes().add(new NetworkLayerReachabilityInformation(0, null));
		
		assertBufferContents(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0x00, (byte)0x18, // length 24
				(byte)0x02, // type code UPDATE
				0x00, 0x01, // withdrawn routes length (1 octets)
				0x00,       // withdrawn 0/0 prefix
				0x00, 0x00, // Total path attributes length  (0 octets)
		}, update.encodePacket());
	}	
	
	@Test
	public void testEncodeWithdrawnFourBitPrefixPrefixUpdatePacket() {
		UpdatePacket update = new UpdatePacket();
		
		update.getWithdrawnRoutes().add(new NetworkLayerReachabilityInformation(4, new byte[] { (byte)0xc0 }));
		
		assertBufferContents(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0x00, (byte)0x19, // length 25
				(byte)0x02, // type code UPDATE
				0x00, 0x02, // withdrawn routes length (2 octets)
				0x04, (byte)0xc0, // withdrawn 192.0.0.0/4 prefix
				0x00, 0x00, // Total path attributes length  (0 octets)
		}, update.encodePacket());
	}	
	
	@Test
	public void testEncodeWithdrawnEightBitPrefixPrefixUpdatePacket() {
		UpdatePacket update = new UpdatePacket();
		
		update.getWithdrawnRoutes().add(new NetworkLayerReachabilityInformation(8, new byte[] { (byte)0xc8 }));
		
		assertBufferContents(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0x00, (byte)0x19, // length 25
				(byte)0x02, // type code UPDATE
				0x00, 0x02, // withdrawn routes length (2 octets)
				0x08, (byte)0xc8, // withdrawn 200.0.0.0/8 prefix
				0x00, 0x00, // Total path attributes length  (0 octets)
		}, update.encodePacket());
	}	
	
	@Test
	public void testEncodeWithdrawnTwelveBitPrefixPrefixUpdatePacket() {
		UpdatePacket update = new UpdatePacket();
		
		update.getWithdrawnRoutes().add(new NetworkLayerReachabilityInformation(12, new byte[] { (byte)0xc0, (byte)0xe0, }));
		
		assertBufferContents(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0x00, (byte)0x1a, // length 26
				(byte)0x02, // type code UPDATE
				0x00, 0x03, // withdrawn routes length (3 octets)
				0x0c, (byte)0xc0, (byte)0xe0, // withdrawn 192.224.0.0/12 prefix
				0x00, 0x00, // Total path attributes length  (0 octets)
		}, update.encodePacket());
	}	
	
	@Test
	public void testEncodeWithdrawnSixteenBitPrefixPrefixUpdatePacket() {
		UpdatePacket update = new UpdatePacket();
		
		update.getWithdrawnRoutes().add(new NetworkLayerReachabilityInformation(16, new byte[] { (byte)0xc0, (byte)0xef, }));
		
		assertBufferContents(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0x00, (byte)0x1a, // length 26
				(byte)0x02, // type code UPDATE
				0x00, 0x03, // withdrawn routes length (3 octets)
				0x10, (byte)0xc0, (byte)0xef, // withdrawn 192.239.0.0/12 prefix
				0x00, 0x00, // Total path attributes length  (0 octets)
		}, update.encodePacket());
	}	
	
	@Test
	public void testEncodeOriginIgpPacket() throws Exception {
		UpdatePacket update = new UpdatePacket();
		
		update.getPathAttributes().add(new OriginPathAttribute(Origin.IGP));

		assertBufferContents(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0x00, (byte)0x1b, // length 27
				(byte)0x02, // type code UPDATE
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x04, // path attributes length (49 octets)
				(byte)0x40, (byte)0x01, (byte)0x01, (byte)0x00, // Path attribute: ORIGIN IGP  
		}, update.encodePacket());
	}
	
	@Test
	public void testEncodeOriginEgpPacket() throws Exception {
		UpdatePacket update = new UpdatePacket();
		
		update.getPathAttributes().add(new OriginPathAttribute(Origin.EGP));

		assertBufferContents(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0x00, (byte)0x1b, // length 27
				(byte)0x02, // type code UPDATE
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x04, // path attributes length (49 octets)
				(byte)0x40, (byte)0x01, (byte)0x01, (byte)0x01, // Path attribute: ORIGIN EGP  
		}, update.encodePacket());
	}
	
	@Test
	public void testEncodeOriginIncompletePacket() throws Exception {
		UpdatePacket update = new UpdatePacket();
		
		update.getPathAttributes().add(new OriginPathAttribute(Origin.INCOMPLETE));

		assertBufferContents(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0x00, (byte)0x1b, // length 27
				(byte)0x02, // type code UPDATE
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x04, // path attributes length (49 octets)
				(byte)0x40, (byte)0x01, (byte)0x01, (byte)0x02, // Path attribute: ORIGIN INCOMPLETE  
		}, update.encodePacket());
	}
	
	@Test
	public void testEncodeASPath4EmptyPacket() throws Exception {
		UpdatePacket update = new UpdatePacket();
		
		update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_4OCTETS));

		assertBufferContents(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0x00, (byte)0x1a, // length 26
				(byte)0x02, // type code UPDATE
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x03, // path attributes length (3 octets)
				(byte)0x40, (byte)0x11, (byte)0x00, // Path attribute: AS4_PATH emtpy 
		}, update.encodePacket());
		
	}

	@Test
	public void testEncodeASPath4ASSequenceOneASNumberPacket() throws Exception {
		UpdatePacket update = new UpdatePacket();
		
		update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_4OCTETS, new ASPathAttribute.PathSegment[] {
				new ASPathAttribute.PathSegment(ASType.AS_NUMBER_4OCTETS, ASPathAttribute.PathSegmentType.AS_SEQUENCE, new int[] {
						0x00001234,
				})
		}));

		assertBufferContents(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0x00, (byte)0x20, // length 32
				(byte)0x02, // type code UPDATE
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x09, // path attributes length (9 octets)
				(byte)0x40, (byte)0x11, (byte)0x06, // Path attribute: 6 octets AS_PATH 
				0x02, 0x01, 0x00, 0x00, 0x12, 0x34, // AS_SEQUENCE 0x00001234 
		}, update.encodePacket());
	}

	@Test
	public void testEncodeASPath4ASSequenceTwoASNumberPacket() throws Exception {
		UpdatePacket update = new UpdatePacket();
		
		update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_4OCTETS, new ASPathAttribute.PathSegment[] {
				new ASPathAttribute.PathSegment(ASType.AS_NUMBER_4OCTETS, ASPathAttribute.PathSegmentType.AS_SEQUENCE, new int[] {
						0x00001234, 0x00005678
				})
		}));

		assertBufferContents(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0x00, (byte)0x24, // length 36
				(byte)0x02, // type code UPDATE
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x0d, // path attributes length (13 octets)
				(byte)0x40, (byte)0x11, (byte)0x0a, // Path attribute: 10 octets AS4_PATH 
				0x02, 0x02, 0x00, 0x00, 0x12, 0x34, 0x00, 0x00, 0x56, 0x78, // AS_SEQUENCE 0x1234 0x5678 
		}, update.encodePacket());
	}

	@Test
	public void testEncodeASPath4ASSetOneASNumberPacket() throws Exception {
		UpdatePacket update = new UpdatePacket();
		
		update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_4OCTETS, new ASPathAttribute.PathSegment[] {
				new ASPathAttribute.PathSegment(ASType.AS_NUMBER_4OCTETS, ASPathAttribute.PathSegmentType.AS_SET, new int[] {
						0x00001234,
				})
		}));

		assertBufferContents(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0x00, (byte)0x20, // length 32
				(byte)0x02, // type code UPDATE
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x09, // path attributes length (9 octets)
				(byte)0x40, (byte)0x11, (byte)0x06, // Path attribute: 6 octets AS4_PATH 
				0x01, 0x01, 0x00, 0x00, 0x12, 0x34, // AS_SET 0x1234 
		}, update.encodePacket());
	}

	@Test
	public void testEncodeASPath4ASSetTwoASNumberPacket() throws Exception {
		UpdatePacket update = new UpdatePacket();
		
		update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_4OCTETS, new ASPathAttribute.PathSegment[] {
				new ASPathAttribute.PathSegment(ASType.AS_NUMBER_4OCTETS, ASPathAttribute.PathSegmentType.AS_SET, new int[] {
						0x00001234, 0x00005678
				})
		}));

		assertBufferContents(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0x00, (byte)0x24, // length 36
				(byte)0x02, // type code UPDATE
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x0d, // path attributes length (13 octets)
				(byte)0x40, (byte)0x11, (byte)0x0a, // Path attribute: 10 octets AS4_PATH 
				0x01, 0x02, 0x00, 0x00, 0x12, 0x34, 0x00, 0x00, 0x56, 0x78, // AS_SET 0x1234 0x5678 
		}, update.encodePacket());
	}

	@Test
	public void testEncodeASPath4ASSeqeunceOneASNumberASSeqeunceOneASNumberPacket() throws Exception {
		UpdatePacket update = new UpdatePacket();
		
		update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_4OCTETS, new ASPathAttribute.PathSegment[] {
				new ASPathAttribute.PathSegment(ASType.AS_NUMBER_4OCTETS, ASPathAttribute.PathSegmentType.AS_SEQUENCE, new int[] {
						0x00001234, 
				}),
				new ASPathAttribute.PathSegment(ASType.AS_NUMBER_4OCTETS, ASPathAttribute.PathSegmentType.AS_SEQUENCE, new int[] {
						0x00005678
				})
		}));

		assertBufferContents(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0x00, (byte)0x26, // length 38
				(byte)0x02, // type code UPDATE
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x0f, // path attributes length (15 octets)
				(byte)0x40, (byte)0x11, (byte)0x0c, // Path attribute: 12 octets AS4_PATH
				0x02, 0x01, 0x00, 0x00, 0x12, 0x34, // AS_SEQUENCE 0x1234
				0x02, 0x01, 0x00, 0x00, 0x56, 0x78, // AS_SEQUENCE 0x5678 
		}, update.encodePacket());
	}

	@Test
	public void testEncodeASPath4ASSeqeunceTw0ASNumberASSeqeunceOneASNumberPacket() throws Exception {
		UpdatePacket update = new UpdatePacket();
		
		update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_4OCTETS, new ASPathAttribute.PathSegment[] {
				new ASPathAttribute.PathSegment(ASType.AS_NUMBER_4OCTETS, ASPathAttribute.PathSegmentType.AS_SEQUENCE, new int[] {
						0x00001234, 0x000089ab
				}),
				new ASPathAttribute.PathSegment(ASType.AS_NUMBER_4OCTETS, ASPathAttribute.PathSegmentType.AS_SEQUENCE, new int[] {
						0x00005678
				})
		}));

		assertBufferContents(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0x00, (byte)0x2a, // length 42
				(byte)0x02, // type code UPDATE
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x13, // path attributes length (19 octets)
				(byte)0x40, (byte)0x11, (byte)0x10, // Path attribute: 16 octets AS4_PATH
				0x02, 0x02, 0x00, 0x00, 0x12, 0x34, 0x00, 0x00, (byte)0x89, (byte)0xab, // AS_SEQUENCE 0x1234 0x89ab
				0x02, 0x01, 0x00, 0x00, 0x56, 0x78, // AS_SEQUENCE 0x5678 
		}, update.encodePacket());
	}

	@Test
	public void testEncodeASPath4ASSeqeunceTw0ASNumberASSeqeunceTwoASNumberPacket() throws Exception {
		UpdatePacket update = new UpdatePacket();
		
		update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_4OCTETS, new ASPathAttribute.PathSegment[] {
				new ASPathAttribute.PathSegment(ASType.AS_NUMBER_4OCTETS, ASPathAttribute.PathSegmentType.AS_SEQUENCE, new int[] {
						0x00001234, 0x000089ab
				}),
				new ASPathAttribute.PathSegment(ASType.AS_NUMBER_4OCTETS, ASPathAttribute.PathSegmentType.AS_SEQUENCE, new int[] {
						0x00005678, 0x0000cdef
				})
		}));

		assertBufferContents(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0x00, (byte)0x2e, // length 46
				(byte)0x02, // type code UPDATE
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x17, // path attributes length (23 octets)
				(byte)0x40, (byte)0x11, (byte)0x14, // Path attribute: 20 octets AS4_PATH
				0x02, 0x02, 0x00, 0x00, 0x12, 0x34, 0x00, 0x00, (byte)0x89, (byte)0xab, // AS_SEQUENCE 0x1234 0x89ab
				0x02, 0x02, 0x00, 0x00, 0x56, 0x78, 0x00, 0x00, (byte)0xcd, (byte)0xef // AS_SEQUENCE 0x5678 0xcdef 
		}, update.encodePacket());
	}

	@Test
	public void testEncodeASPath4ASSeqeunceOneASNumberASSeqeunceTwoASNumberPacket() throws Exception {
		UpdatePacket update = new UpdatePacket();
		
		update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_4OCTETS, new ASPathAttribute.PathSegment[] {
				new ASPathAttribute.PathSegment(ASType.AS_NUMBER_4OCTETS, ASPathAttribute.PathSegmentType.AS_SEQUENCE, new int[] {
						0x00001234, 
				}),
				new ASPathAttribute.PathSegment(ASType.AS_NUMBER_4OCTETS, ASPathAttribute.PathSegmentType.AS_SEQUENCE, new int[] {
						0x00005678, 0x0000cdef
				})
		}));

		assertBufferContents(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0x00, (byte)0x2a, // length 42
				(byte)0x02, // type code UPDATE
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x13, // path attributes length (19 octets)
				(byte)0x40, (byte)0x11, (byte)0x10, // Path attribute: 16 octets AS4_PATH
				0x02, 0x01, 0x00, 0x00, 0x12, 0x34, // AS_SEQUENCE 0x1234 
				0x02, 0x02, 0x00, 0x00, 0x56, 0x78, 0x00, 0x00, (byte)0xcd, (byte)0xef // AS_SEQUENCE 0x5678 0xcdef 
		}, update.encodePacket());
	}
	
	@Test
	public void testEncodeASPath4ASSeqeunceOneASNumberASSetOneASNumberPacket() throws Exception {
		UpdatePacket update = new UpdatePacket();
		
		update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_4OCTETS, new ASPathAttribute.PathSegment[] {
				new ASPathAttribute.PathSegment(ASType.AS_NUMBER_4OCTETS, ASPathAttribute.PathSegmentType.AS_SEQUENCE, new int[] {
						0x00001234, 
				}),
				new ASPathAttribute.PathSegment(ASType.AS_NUMBER_4OCTETS, ASPathAttribute.PathSegmentType.AS_SET, new int[] {
						0x00005678, 
				})
		}));

		assertBufferContents(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0x00, (byte)0x26, // length 38
				(byte)0x02, // type code UPDATE
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x0f, // path attributes length (15 octets)
				(byte)0x40, (byte)0x11, (byte)0x0c, // Path attribute: 12 octets AS_PATH
				0x02, 0x01, 0x00, 0x00, 0x12, 0x34, // AS_SEQUENCE 0x1234
				0x01, 0x01, 0x00, 0x00, 0x56, 0x78, // AS_SET 0x5678 
		}, update.encodePacket());
	}

	@Test
	public void testEncodeASPath4ASSeqeunceTw0ASNumberASSetASNumberPacket() throws Exception {
		UpdatePacket update = new UpdatePacket();
		
		update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_4OCTETS, new ASPathAttribute.PathSegment[] {
				new ASPathAttribute.PathSegment(ASType.AS_NUMBER_4OCTETS, ASPathAttribute.PathSegmentType.AS_SEQUENCE, new int[] {
						0x00001234, 0x000089ab
				}),
				new ASPathAttribute.PathSegment(ASType.AS_NUMBER_4OCTETS, ASPathAttribute.PathSegmentType.AS_SET, new int[] {
						0x00005678, 
				})
		}));

		assertBufferContents(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0x00, (byte)0x2a, // length 42
				(byte)0x02, // type code UPDATE
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x13, // path attributes length (19 octets)
				(byte)0x40, (byte)0x11, (byte)0x10, // Path attribute: 16 octets AS4_PATH
				0x02, 0x02, 0x00, 0x00, 0x12, 0x34, 0x00, 0x00, (byte)0x89, (byte)0xab, // AS_SEQUENCE 0x1234 0x89ab
				0x01, 0x01, 0x00, 0x00, 0x56, 0x78, // AS_SET 0x5678 
		}, update.encodePacket());
	}

	@Test
	public void testEncodeASPath4ASSeqeunceTw0ASNumberASSetTwoASNumberPacket() throws Exception {
		UpdatePacket update = new UpdatePacket();
		
		update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_4OCTETS, new ASPathAttribute.PathSegment[] {
				new ASPathAttribute.PathSegment(ASType.AS_NUMBER_4OCTETS, ASPathAttribute.PathSegmentType.AS_SEQUENCE, new int[] {
						0x00001234, 0x000089ab
				}),
				new ASPathAttribute.PathSegment(ASType.AS_NUMBER_4OCTETS, ASPathAttribute.PathSegmentType.AS_SET, new int[] {
						0x00005678, 0x0000cdef
				})
		}));

		assertBufferContents(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0x00, (byte)0x2e, // length 46
				(byte)0x02, // type code UPDATE
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x17, // path attributes length (23 octets)
				(byte)0x40, (byte)0x11, (byte)0x14, // Path attribute: 20 octets AS4_PATH
				0x02, 0x02, 0x00, 0x00, 0x12, 0x34, 0x00, 0x00, (byte)0x89, (byte)0xab, // AS_SEQUENCE 0x1234 0x89ab
				0x01, 0x02, 0x00, 0x00, 0x56, 0x78, 0x00, 0x00, (byte)0xcd, (byte)0xef // AS_SET 0x5678 0xcdef 
		}, update.encodePacket());
	}

	@Test
	public void testEncodeASPath4ASSeqeunceOneASNumberASSetTwoASNumberPacket() throws Exception {
		UpdatePacket update = new UpdatePacket();
		
		update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_4OCTETS, new ASPathAttribute.PathSegment[] {
				new ASPathAttribute.PathSegment(ASType.AS_NUMBER_4OCTETS, ASPathAttribute.PathSegmentType.AS_SEQUENCE, new int[] {
						0x00001234,
				}),
				new ASPathAttribute.PathSegment(ASType.AS_NUMBER_4OCTETS, ASPathAttribute.PathSegmentType.AS_SET, new int[] {
						0x00005678, 0x0000cdef
				})
		}));

		assertBufferContents(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0x00, (byte)0x2a, // length 42
				(byte)0x02, // type code UPDATE
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x13, // path attributes length (19 octets)
				(byte)0x40, (byte)0x11, (byte)0x10, // Path attribute: 16 octets AS4_PATH
				0x02, 0x01, 0x00, 0x00, 0x12, 0x34, // AS_SEQUENCE 0x1234 
				0x01, 0x02, 0x00, 0x00, 0x56, 0x78, 0x00, 0x00, (byte)0xcd, (byte)0xef // AS_SET 0x5678 0xcdef 
		}, update.encodePacket());
	}

	@Test
	public void testEncodeASPath4ASSetOneASNumberASSeqeunceOneASNumberPacket() throws Exception {
		UpdatePacket update = new UpdatePacket();
		
		update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_4OCTETS, new ASPathAttribute.PathSegment[] {
				new ASPathAttribute.PathSegment(ASType.AS_NUMBER_4OCTETS, ASPathAttribute.PathSegmentType.AS_SET, new int[] {
						0x00001234,
				}),
				new ASPathAttribute.PathSegment(ASType.AS_NUMBER_4OCTETS, ASPathAttribute.PathSegmentType.AS_SEQUENCE, new int[] {
						0x00005678, 
				})
		}));

		assertBufferContents(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0x00, (byte)0x26, // length 38
				(byte)0x02, // type code UPDATE
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x0f, // path attributes length (15 octets)
				(byte)0x40, (byte)0x11, (byte)0x0c, // Path attribute: 12 octets AS4_PATH
				0x01, 0x01, 0x00, 0x00, 0x12, 0x34, // AS_SET 0x1234
				0x02, 0x01, 0x00, 0x00, 0x56, 0x78, // AS_SEQUENCE 0x5678 
		}, update.encodePacket());
	}

	@Test
	public void testEncodeASPath4ASSetTw0ASNumberASSeqeunceOneASNumberPacket() throws Exception {
		UpdatePacket update = new UpdatePacket();
		
		update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_4OCTETS, new ASPathAttribute.PathSegment[] {
				new ASPathAttribute.PathSegment(ASType.AS_NUMBER_4OCTETS, ASPathAttribute.PathSegmentType.AS_SET, new int[] {
						0x00001234, 0x000089ab,
				}),
				new ASPathAttribute.PathSegment(ASType.AS_NUMBER_4OCTETS, ASPathAttribute.PathSegmentType.AS_SEQUENCE, new int[] {
						0x00005678, 
				})
		}));

		assertBufferContents(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0x00, (byte)0x2a, // length 42
				(byte)0x02, // type code UPDATE
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x13, // path attributes length (19 octets)
				(byte)0x40, (byte)0x11, (byte)0x10, // Path attribute: 16 octets AS4_PATH
				0x01, 0x02, 0x00, 0x00, 0x12, 0x34, 0x00, 0x00, (byte)0x89, (byte)0xab, // AS_SET 0x1234 0x89ab
				0x02, 0x01, 0x00, 0x00, 0x56, 0x78, // AS_SEQUENCE 0x5678 
		}, update.encodePacket());
	}

	@Test
	public void testEncodeASPath4ASSetTw0ASNumberASSeqeunceTwoASNumberPacket() throws Exception {
		UpdatePacket update = new UpdatePacket();
		
		update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_4OCTETS, new ASPathAttribute.PathSegment[] {
				new ASPathAttribute.PathSegment(ASType.AS_NUMBER_4OCTETS, ASPathAttribute.PathSegmentType.AS_SET, new int[] {
						0x00001234, 0x000089ab,
				}),
				new ASPathAttribute.PathSegment(ASType.AS_NUMBER_4OCTETS, ASPathAttribute.PathSegmentType.AS_SEQUENCE, new int[] {
						0x00005678, 0x0000cdef,
				})
		}));

		assertBufferContents(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0x00, (byte)0x2e, // length 46
				(byte)0x02, // type code UPDATE
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x17, // path attributes length (23 octets)
				(byte)0x40, (byte)0x11, (byte)0x14, // Path attribute: 20 octets AS4_PATH
				0x01, 0x02, 0x00, 0x00, 0x12, 0x34, 0x00, 0x00, (byte)0x89, (byte)0xab, // AS_SET 0x1234 0x89ab
				0x02, 0x02, 0x00, 0x00, 0x56, 0x78, 0x00, 0x00, (byte)0xcd, (byte)0xef // AS_SEQUENCE 0x5678 0xcdef 
		}, update.encodePacket());
	}

	@Test
	public void testEncodeASPath4ASSetOneASNumberASSeqeunceTwoASNumberPacket() throws Exception {
		UpdatePacket update = new UpdatePacket();
		
		update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_4OCTETS, new ASPathAttribute.PathSegment[] {
				new ASPathAttribute.PathSegment(ASType.AS_NUMBER_4OCTETS, ASPathAttribute.PathSegmentType.AS_SET, new int[] {
						0x00001234, 
				}),
				new ASPathAttribute.PathSegment(ASType.AS_NUMBER_4OCTETS, ASPathAttribute.PathSegmentType.AS_SEQUENCE, new int[] {
						0x00005678, 0x0000cdef,
				})
		}));

		assertBufferContents(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0x00, (byte)0x2a, // length 42
				(byte)0x02, // type code UPDATE
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x13, // path attributes length (19 octets)
				(byte)0x40, (byte)0x11, (byte)0x10, // Path attribute: 16 octets AS4_PATH
				0x01, 0x01, 0x00, 0x00, 0x12, 0x34, // AS_SET 0x1234 
				0x02, 0x02, 0x00, 0x00, 0x56, 0x78, 0x00, 0x00, (byte)0xcd, (byte)0xef // AS_SEQUENCE 0x5678 0xcdef 
		}, update.encodePacket());
	}
	
	@Test
	public void testEncodeASPath2EmptyPacket() throws Exception {
		UpdatePacket update = new UpdatePacket();
		
		update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_2OCTETS));

		assertBufferContents(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0x00, (byte)0x1a, // length 26
				(byte)0x02, // type code UPDATE
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x03, // path attributes length (3 octets)
				(byte)0x40, (byte)0x02, (byte)0x00, // Path attribute: AS_PATH emtpy 
		}, update.encodePacket());
	}

	@Test
	public void testEncodeASPath2ASSequenceOneASNumberPacket() throws Exception {
		UpdatePacket update = new UpdatePacket();
		
		update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_2OCTETS, new ASPathAttribute.PathSegment[] {
				new ASPathAttribute.PathSegment(ASType.AS_NUMBER_2OCTETS, ASPathAttribute.PathSegmentType.AS_SEQUENCE, new int[] {
						0x1234, 
				}),
		}));

		assertBufferContents(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0x00, (byte)0x1e, // length 30
				(byte)0x02, // type code UPDATE
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x07, // path attributes length (7 octets)
				(byte)0x40, (byte)0x02, (byte)0x04, 0x02, 0x01, 0x12, 0x34, // Path attribute: 4 octets AS_PATH AS_SEQUENCE 0x1234 
		}, update.encodePacket());
	}

	@Test
	public void testEncodeASPath2ASSequenceTwoASNumberPacket() throws Exception {
		UpdatePacket update = new UpdatePacket();
		
		update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_2OCTETS, new ASPathAttribute.PathSegment[] {
				new ASPathAttribute.PathSegment(ASType.AS_NUMBER_2OCTETS, ASPathAttribute.PathSegmentType.AS_SEQUENCE, new int[] {
						0x1234, 0x5678,
				}),
		}));

		assertBufferContents(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0x00, (byte)0x20, // length 32
				(byte)0x02, // type code UPDATE
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x09, // path attributes length (9 octets)
				(byte)0x40, (byte)0x02, (byte)0x06, 0x02, 0x02, 0x12, 0x34, 0x56, 0x78, // Path attribute: 6 octets AS_PATH AS_SEQUENCE 0x1234 0x5678 
		}, update.encodePacket());
	}

	@Test
	public void testEncodeASPath2ASSetOneASNumberPacket() throws Exception {
		UpdatePacket update = new UpdatePacket();
		
		update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_2OCTETS, new ASPathAttribute.PathSegment[] {
				new ASPathAttribute.PathSegment(ASType.AS_NUMBER_2OCTETS, ASPathAttribute.PathSegmentType.AS_SET, new int[] {
						0x1234,
				}),
		}));

		assertBufferContents(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0x00, (byte)0x1e, // length 30
				(byte)0x02, // type code UPDATE
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x07, // path attributes length (7 octets)
				(byte)0x40, (byte)0x02, (byte)0x04, 0x01, 0x01, 0x12, 0x34, // Path attribute: 4 octets AS_PATH AS_SET 0x1234 
		}, update.encodePacket());
	}

	@Test
	public void testEncodeASPath2ASSetTwoASNumberPacket() throws Exception {
		UpdatePacket update = new UpdatePacket();
		
		update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_2OCTETS, new ASPathAttribute.PathSegment[] {
				new ASPathAttribute.PathSegment(ASType.AS_NUMBER_2OCTETS, ASPathAttribute.PathSegmentType.AS_SET, new int[] {
						0x1234,
				}),
		}));

		assertBufferContents(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0x00, (byte)0x1e, // length 30
				(byte)0x02, // type code UPDATE
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x07, // path attributes length (7 octets)
				(byte)0x40, (byte)0x02, (byte)0x04, 0x01, 0x01, 0x12, 0x34, // Path attribute: 4 octets AS_PATH AS_SET 0x1234 
		}, update.encodePacket());
	}

	@Test
	public void testEncodeASPath2ASSeqeunceOneASNumberASSeqeunceOneASNumberPacket() throws Exception {
		UpdatePacket update = new UpdatePacket();
		
		update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_2OCTETS, new ASPathAttribute.PathSegment[] {
				new ASPathAttribute.PathSegment(ASType.AS_NUMBER_2OCTETS, ASPathAttribute.PathSegmentType.AS_SEQUENCE, new int[] {
						0x1234,
				}),
				new ASPathAttribute.PathSegment(ASType.AS_NUMBER_2OCTETS, ASPathAttribute.PathSegmentType.AS_SEQUENCE, new int[] {
						0x5678,
				}),
		}));

		assertBufferContents(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0x00, (byte)0x22, // length 34
				(byte)0x02, // type code UPDATE
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x0b, // path attributes length (11 octets)
				(byte)0x40, (byte)0x02, (byte)0x08, // Path attribute: 7 octets AS_PATH
				0x02, 0x01, 0x12, 0x34, // AS_SEQUENCE 0x1234
				0x02, 0x01, 0x56, 0x78, // AS_SEQUENCE 0x5678 
		}, update.encodePacket());
	}

	@Test
	public void testEncodeASPath2ASSeqeunceTw0ASNumberASSeqeunceOneASNumberPacket() throws Exception {
		UpdatePacket update = new UpdatePacket();
		
		update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_2OCTETS, new ASPathAttribute.PathSegment[] {
				new ASPathAttribute.PathSegment(ASType.AS_NUMBER_2OCTETS, ASPathAttribute.PathSegmentType.AS_SEQUENCE, new int[] {
						0x1234, 0x89ab,
				}),
				new ASPathAttribute.PathSegment(ASType.AS_NUMBER_2OCTETS, ASPathAttribute.PathSegmentType.AS_SEQUENCE, new int[] {
						0x5678,
				}),
		}));

		assertBufferContents(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0x00, (byte)0x24, // length 36
				(byte)0x02, // type code UPDATE
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x0d, // path attributes length (13 octets)
				(byte)0x40, (byte)0x02, (byte)0x0a, // Path attribute: 10 octets AS_PATH
				0x02, 0x02, 0x12, 0x34, (byte)0x89, (byte)0xab, // AS_SEQUENCE 0x1234 0x89ab
				0x02, 0x01, 0x56, 0x78, // AS_SEQUENCE 0x5678 
		}, update.encodePacket());
	}

	@Test
	public void testEncodeASPath2ASSeqeunceTw0ASNumberASSeqeunceTwoASNumberPacket() throws Exception {
		UpdatePacket update = new UpdatePacket();
		
		update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_2OCTETS, new ASPathAttribute.PathSegment[] {
				new ASPathAttribute.PathSegment(ASType.AS_NUMBER_2OCTETS, ASPathAttribute.PathSegmentType.AS_SEQUENCE, new int[] {
						0x1234, 0x89ab,
				}),
				new ASPathAttribute.PathSegment(ASType.AS_NUMBER_2OCTETS, ASPathAttribute.PathSegmentType.AS_SEQUENCE, new int[] {
						0x5678, 0xcdef,
				}),
		}));

		assertBufferContents(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0x00, (byte)0x26, // length 38
				(byte)0x02, // type code UPDATE
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x0f, // path attributes length (15 octets)
				(byte)0x40, (byte)0x02, (byte)0x0c, // Path attribute: 12 octets AS_PATH
				0x02, 0x02, 0x12, 0x34, (byte)0x89, (byte)0xab, // AS_SEQUENCE 0x1234 0x89ab
				0x02, 0x02, 0x56, 0x78, (byte)0xcd, (byte)0xef // AS_SEQUENCE 0x5678 0xcdef 
		}, update.encodePacket());
	}

	@Test
	public void testEncodeASPath2ASSeqeunceOneASNumberASSeqeunceTwoASNumberPacket() throws Exception {
		UpdatePacket update = new UpdatePacket();
		
		update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_2OCTETS, new ASPathAttribute.PathSegment[] {
				new ASPathAttribute.PathSegment(ASType.AS_NUMBER_2OCTETS, ASPathAttribute.PathSegmentType.AS_SEQUENCE, new int[] {
						0x1234,
				}),
				new ASPathAttribute.PathSegment(ASType.AS_NUMBER_2OCTETS, ASPathAttribute.PathSegmentType.AS_SEQUENCE, new int[] {
						0x5678, 0xcdef,
				}),
		}));

		assertBufferContents(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0x00, (byte)0x24, // length 36
				(byte)0x02, // type code UPDATE
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x0d, // path attributes length (13 octets)
				(byte)0x40, (byte)0x02, (byte)0x0a, // Path attribute: 10 octets AS_PATH
				0x02, 0x01, 0x12, 0x34, // AS_SEQUENCE 0x1234 
				0x02, 0x02, 0x56, 0x78, (byte)0xcd, (byte)0xef // AS_SEQUENCE 0x5678 0xcdef 
		}, update.encodePacket());
	}
	
	@Test
	public void testEncodeASPath2ASSeqeunceOneASNumberASSetOneASNumberPacket() throws Exception {
		UpdatePacket update = new UpdatePacket();
		
		update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_2OCTETS, new ASPathAttribute.PathSegment[] {
				new ASPathAttribute.PathSegment(ASType.AS_NUMBER_2OCTETS, ASPathAttribute.PathSegmentType.AS_SEQUENCE, new int[] {
						0x1234,
				}),
				new ASPathAttribute.PathSegment(ASType.AS_NUMBER_2OCTETS, ASPathAttribute.PathSegmentType.AS_SET, new int[] {
						0x5678, 
				}),
		}));

		assertBufferContents(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0x00, (byte)0x22, // length 34
				(byte)0x02, // type code UPDATE
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x0b, // path attributes length (11 octets)
				(byte)0x40, (byte)0x02, (byte)0x08, // Path attribute: 8 octets AS_PATH
				0x02, 0x01, 0x12, 0x34, // AS_SEQUENCE 0x1234
				0x01, 0x01, 0x56, 0x78, // AS_SET 0x5678 
		}, update.encodePacket());
	}

	@Test
	public void testEncodeASPath2ASSeqeunceTw0ASNumberASSetASNumberPacket() throws Exception {
		UpdatePacket update = new UpdatePacket();
		
		update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_2OCTETS, new ASPathAttribute.PathSegment[] {
				new ASPathAttribute.PathSegment(ASType.AS_NUMBER_2OCTETS, ASPathAttribute.PathSegmentType.AS_SEQUENCE, new int[] {
						0x1234, 0x89ab,
				}),
				new ASPathAttribute.PathSegment(ASType.AS_NUMBER_2OCTETS, ASPathAttribute.PathSegmentType.AS_SET, new int[] {
						0x5678, 
				}),
		}));

		assertBufferContents(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0x00, (byte)0x24, // length 36
				(byte)0x02, // type code UPDATE
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x0d, // path attributes length (13 octets)
				(byte)0x40, (byte)0x02, (byte)0x0a, // Path attribute: 10 octets AS_PATH
				0x02, 0x02, 0x12, 0x34, (byte)0x89, (byte)0xab, // AS_SEQUENCE 0x1234 0x89ab
				0x01, 0x01, 0x56, 0x78, // AS_SET 0x5678 
		}, update.encodePacket());
	}

	@Test
	public void testEncodeASPath2ASSeqeunceTw0ASNumberASSetTwoASNumberPacket() throws Exception {
		UpdatePacket update = new UpdatePacket();
		
		update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_2OCTETS, new ASPathAttribute.PathSegment[] {
				new ASPathAttribute.PathSegment(ASType.AS_NUMBER_2OCTETS, ASPathAttribute.PathSegmentType.AS_SEQUENCE, new int[] {
						0x1234, 0x89ab,
				}),
				new ASPathAttribute.PathSegment(ASType.AS_NUMBER_2OCTETS, ASPathAttribute.PathSegmentType.AS_SET, new int[] {
						0x5678, 0xcdef,
				}),
		}));

		assertBufferContents(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0x00, (byte)0x26, // length 38
				(byte)0x02, // type code UPDATE
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x0f, // path attributes length (15 octets)
				(byte)0x40, (byte)0x02, (byte)0x0c, // Path attribute: 12 octets AS_PATH
				0x02, 0x02, 0x12, 0x34, (byte)0x89, (byte)0xab, // AS_SEQUENCE 0x1234 0x89ab
				0x01, 0x02, 0x56, 0x78, (byte)0xcd, (byte)0xef // AS_SET 0x5678 0xcdef 
		}, update.encodePacket());
	}

	@Test
	public void testEncodeASPath2ASSeqeunceOneASNumberASSetTwoASNumberPacket() throws Exception {
		UpdatePacket update = new UpdatePacket();
		
		update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_2OCTETS, new ASPathAttribute.PathSegment[] {
				new ASPathAttribute.PathSegment(ASType.AS_NUMBER_2OCTETS, ASPathAttribute.PathSegmentType.AS_SEQUENCE, new int[] {
						0x1234, 
				}),
				new ASPathAttribute.PathSegment(ASType.AS_NUMBER_2OCTETS, ASPathAttribute.PathSegmentType.AS_SET, new int[] {
						0x5678, 0xcdef,
				}),
		}));

		assertBufferContents(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0x00, (byte)0x24, // length 36
				(byte)0x02, // type code UPDATE
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x0d, // path attributes length (13 octets)
				(byte)0x40, (byte)0x02, (byte)0x0a, // Path attribute: 10 octets AS_PATH
				0x02, 0x01, 0x12, 0x34, // AS_SEQUENCE 0x1234 
				0x01, 0x02, 0x56, 0x78, (byte)0xcd, (byte)0xef // AS_SEQUENCE 0x5678 0xcdef 
		}, update.encodePacket());
	}

	@Test
	public void testEncodeASPath2ASSetOneASNumberASSeqeunceOneASNumberPacket() throws Exception {
		UpdatePacket update = new UpdatePacket();
		
		update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_2OCTETS, new ASPathAttribute.PathSegment[] {
				new ASPathAttribute.PathSegment(ASType.AS_NUMBER_2OCTETS, ASPathAttribute.PathSegmentType.AS_SET, new int[] {
						0x1234, 
				}),
				new ASPathAttribute.PathSegment(ASType.AS_NUMBER_2OCTETS, ASPathAttribute.PathSegmentType.AS_SEQUENCE, new int[] {
						0x5678,
				}),
		}));

		assertBufferContents(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0x00, (byte)0x22, // length 34
				(byte)0x02, // type code UPDATE
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x0b, // path attributes length (11 octets)
				(byte)0x40, (byte)0x02, (byte)0x08, // Path attribute: 8 octets AS_PATH
				0x01, 0x01, 0x12, 0x34, // AS_SET 0x1234
				0x02, 0x01, 0x56, 0x78, // AS_SEQUENCE 0x5678 
		}, update.encodePacket());
	}

	@Test
	public void testEncodeASPath2ASSetTw0ASNumberASSeqeunceOneASNumberPacket() throws Exception {
		UpdatePacket update = new UpdatePacket();
		
		update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_2OCTETS, new ASPathAttribute.PathSegment[] {
				new ASPathAttribute.PathSegment(ASType.AS_NUMBER_2OCTETS, ASPathAttribute.PathSegmentType.AS_SET, new int[] {
						0x1234, 0x89ab,
				}),
				new ASPathAttribute.PathSegment(ASType.AS_NUMBER_2OCTETS, ASPathAttribute.PathSegmentType.AS_SEQUENCE, new int[] {
						0x5678,
				}),
		}));

		assertBufferContents(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0x00, (byte)0x24, // length 36
				(byte)0x02, // type code UPDATE
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x0d, // path attributes length (13 octets)
				(byte)0x40, (byte)0x02, (byte)0x0a, // Path attribute: 10 octets AS_PATH
				0x01, 0x02, 0x12, 0x34, (byte)0x89, (byte)0xab, // AS_SET 0x1234 0x89ab
				0x02, 0x01, 0x56, 0x78, // AS_SEQUENCE 0x5678 
		}, update.encodePacket());
	}

	@Test
	public void testEncodeASPath2ASSetTw0ASNumberASSeqeunceTwoASNumberPacket() throws Exception {
		UpdatePacket update = new UpdatePacket();
		
		update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_2OCTETS, new ASPathAttribute.PathSegment[] {
				new ASPathAttribute.PathSegment(ASType.AS_NUMBER_2OCTETS, ASPathAttribute.PathSegmentType.AS_SET, new int[] {
						0x1234, 0x89ab,
				}),
				new ASPathAttribute.PathSegment(ASType.AS_NUMBER_2OCTETS, ASPathAttribute.PathSegmentType.AS_SEQUENCE, new int[] {
						0x5678, 0xcdef,
				}),
		}));

		assertBufferContents(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0x00, (byte)0x26, // length 38
				(byte)0x02, // type code UPDATE
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x0f, // path attributes length (15 octets)
				(byte)0x40, (byte)0x02, (byte)0x0c, // Path attribute: 12 octets AS_PATH
				0x01, 0x02, 0x12, 0x34, (byte)0x89, (byte)0xab, // AS_SET 0x1234 0x89ab
				0x02, 0x02, 0x56, 0x78, (byte)0xcd, (byte)0xef // AS_SEQUENCE 0x5678 0xcdef 
		}, update.encodePacket());
	}

	@Test
	public void testEncodeASPath2ASSetOneASNumberASSeqeunceTwoASNumberPacket() throws Exception {
		UpdatePacket update = new UpdatePacket();
		
		update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_2OCTETS, new ASPathAttribute.PathSegment[] {
				new ASPathAttribute.PathSegment(ASType.AS_NUMBER_2OCTETS, ASPathAttribute.PathSegmentType.AS_SET, new int[] {
						0x1234,
				}),
				new ASPathAttribute.PathSegment(ASType.AS_NUMBER_2OCTETS, ASPathAttribute.PathSegmentType.AS_SEQUENCE, new int[] {
						0x5678, 0xcdef,
				}),
		}));

		assertBufferContents(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0x00, (byte)0x24, // length 36
				(byte)0x02, // type code UPDATE
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x0d, // path attributes length (13 octets)
				(byte)0x40, (byte)0x02, (byte)0x0a, // Path attribute: 10 octets AS_PATH
				0x01, 0x01, 0x12, 0x34, // AS_SET 0x1234 
				0x02, 0x02, 0x56, 0x78, (byte)0xcd, (byte)0xef // AS_SEQUENCE 0x5678 0xcdef 
		}, update.encodePacket());
	}
	
	@Test
	public void testEncodeAggregatorPathAttribute() throws Exception {
		UpdatePacket update = new UpdatePacket();

		update.getPathAttributes().add(new AggregatorPathAttribute(ASType.AS_NUMBER_2OCTETS,0x1234, 
				(Inet4Address)Inet4Address.getByAddress(new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x04, (byte)0x02, })));

		assertBufferContents(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0x00, (byte)0x20, // length 32
				(byte)0x02, // type code UPDATE
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x09, // path attributes length (5 octets)
				(byte)0xc0, (byte)0x07, (byte)0x06, // Path attribute: AS_AGGREGATOR 
				0x12, 0x34, // AS Number 0x1234
				(byte)0xc0, (byte)0xa8, (byte)0x04, (byte)0x02, // Aggregator IP 192.168.4.2
		}, update.encodePacket());
	
		update = new UpdatePacket();

		update.getPathAttributes().add(new AggregatorPathAttribute(ASType.AS_NUMBER_4OCTETS,0x56781234, 
				(Inet4Address)Inet4Address.getByAddress(new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x04, (byte)0x02, })));

		assertBufferContents(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0x00, (byte)0x22, // length 34
				(byte)0x02, // type code UPDATE
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x0b, // path attributes length (7 octets)
				(byte)0xc0, (byte)0x12, (byte)0x08,  // Path attribute: AS4_AGGREGATOR 
				0x56, 0x78, 0x12, 0x34, // AS Number 0x56781234
				(byte)0xc0, (byte)0xa8, (byte)0x04, (byte)0x02, // Aggregator IP 192.168.4.2
		}, update.encodePacket());
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
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AGGREGATOR, aggregator.getTypeCode());
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
		
		Assert.assertEquals(BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS4_AGGREGATOR, aggregator.getTypeCode());
		Assert.assertEquals(0x56781234, aggregator.getAsNumber());
		Assert.assertEquals(ASType.AS_NUMBER_4OCTETS, aggregator.getAsType());
		Assert.assertEquals(Inet4Address.getByAddress(new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x04, (byte)0x02, }), aggregator.getAggregator());
	}

	
	@Test
	public void testEncodeAtomicAggregatePathAttribute() throws Exception {
		UpdatePacket update = new UpdatePacket();

		update.getPathAttributes().add(new AtomicAggregatePathAttribute());

		assertBufferContents(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0x00, (byte)0x1a, // length 26
				(byte)0x02, // type code UPDATE
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x03, // path attributes length (5 octets)
				(byte)0x40, (byte)0x06, (byte)0x00, // Path attribute: ATOMIC_AGGREGATE
		}, update.encodePacket());
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
}
