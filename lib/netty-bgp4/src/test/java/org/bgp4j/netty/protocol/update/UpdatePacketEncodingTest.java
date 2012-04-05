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
 * File: org.bgp4j.netty.protocol.update.UpdatePacketEncodingTest.java 
 */
package org.bgp4j.netty.protocol.update;

import java.net.Inet4Address;

import org.bgp4j.net.ASType;
import org.bgp4j.net.AddressFamily;
import org.bgp4j.net.BinaryNextHop;
import org.bgp4j.net.NetworkLayerReachabilityInformation;
import org.bgp4j.net.Origin;
import org.bgp4j.net.PathSegment;
import org.bgp4j.net.PathSegmentType;
import org.bgp4j.net.SubsequentAddressFamily;
import org.bgp4j.net.attributes.ASPathAttribute;
import org.bgp4j.net.attributes.AggregatorPathAttribute;
import org.bgp4j.net.attributes.AtomicAggregatePathAttribute;
import org.bgp4j.net.attributes.ClusterListPathAttribute;
import org.bgp4j.net.attributes.LocalPrefPathAttribute;
import org.bgp4j.net.attributes.MultiExitDiscPathAttribute;
import org.bgp4j.net.attributes.MultiProtocolReachableNLRI;
import org.bgp4j.net.attributes.MultiProtocolUnreachableNLRI;
import org.bgp4j.net.attributes.NextHopPathAttribute;
import org.bgp4j.net.attributes.OriginPathAttribute;
import org.bgp4j.net.attributes.OriginatorIDPathAttribute;
import org.bgp4j.netty.BGPv4TestBase;
import org.junit.Test;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class UpdatePacketEncodingTest extends BGPv4TestBase {

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
	public void testEncodeAS2PathAttribute() {
		assertBufferContents(new byte[] {
				(byte)0x40, (byte)0x02, (byte)0x00, // Path attribute: AS_PATH emtpy 
		}, PathAttributeCodec.encodePathAttribute(new ASPathAttribute(ASType.AS_NUMBER_2OCTETS)));
	
		assertBufferContents(new byte[] {
				(byte)0x40, (byte)0x02, (byte)0x04, // Path attribute: AS_PATH  
				0x01, 0x01, 0x12, 0x34, // AS_SET 1 AS 0x1234
		}, PathAttributeCodec.encodePathAttribute(new ASPathAttribute(ASType.AS_NUMBER_2OCTETS, new PathSegment[] {
				new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_SET, new int[] {
						0x1234
				}),
		})));
	
		assertBufferContents(new byte[] {
				(byte)0x40, (byte)0x02, (byte)0x06, // Path attribute: AS_PATH  
				0x01, 0x02, 0x12, 0x34, 0x56, 0x78 // AS_SET 2 AS 0x1234 0x5678
		}, PathAttributeCodec.encodePathAttribute(new ASPathAttribute(ASType.AS_NUMBER_2OCTETS, new PathSegment[] {
				new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_SET, new int[] {
						0x1234, 0x5678
				}),
		})));
	
		assertBufferContents(new byte[] {
				(byte)0x40, (byte)0x02, (byte)0x06, // Path attribute: AS_PATH  
				0x03, 0x02, 0x12, 0x34, 0x56, 0x78 // AS_CONFED_SEQUENCE 2 AS 0x1234 0x5678
		}, PathAttributeCodec.encodePathAttribute(new ASPathAttribute(ASType.AS_NUMBER_2OCTETS, new PathSegment[] {
				new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_CONFED_SEQUENCE, new int[] {
						0x1234, 0x5678
				}),
		})));
	
		assertBufferContents(new byte[] {
				(byte)0x40, (byte)0x02, (byte)0x06, // Path attribute: AS_PATH  
				0x04, 0x02, 0x12, 0x34, 0x56, 0x78 // AS_SET 2 AS 0x1234 0x5678
		}, PathAttributeCodec.encodePathAttribute(new ASPathAttribute(ASType.AS_NUMBER_2OCTETS, new PathSegment[] {
				new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_CONFED_SET, new int[] {
						0x1234, 0x5678
				}),
		})));
	
		assertBufferContents(new byte[] {
				(byte)0x40, (byte)0x02, (byte)0x08, // Path attribute: AS_PATH  
				0x01, 0x01, 0x12, 0x34, // AS_SET 1 AS 0x1234 
				0x01, 0x01, 0x56, 0x78, // AS_SET 1 AS 0x5678
		}, PathAttributeCodec.encodePathAttribute(new ASPathAttribute(ASType.AS_NUMBER_2OCTETS, new PathSegment[] {
				new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_SET, new int[] {
						0x1234, 
				}),
				new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_SET, new int[] {
						0x5678, 
				}),
		})));
	
		assertBufferContents(new byte[] {
				(byte)0x40, (byte)0x02, (byte)0x0a, // Path attribute: AS_PATH  
				0x01, 0x02, 0x12, 0x34, (byte)0x9a, (byte)0xbc, // AS_SET 2 AS 0x1234 0x9abc 
				0x01, 0x01, 0x56, 0x78, // AS_SET 1 AS 0x5678
		}, PathAttributeCodec.encodePathAttribute(new ASPathAttribute(ASType.AS_NUMBER_2OCTETS, new PathSegment[] {
				new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_SET, new int[] {
						0x1234, 0x9abc,
				}),
				new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_SET, new int[] {
						0x5678, 
				}),
		})));
	
		assertBufferContents(new byte[] {
				(byte)0x40, (byte)0x02, (byte)0x0c, // Path attribute: AS_PATH  
				0x01, 0x02, 0x12, 0x34, (byte)0x9a, (byte)0xbc, // AS_SET 2 AS 0x1234 0x9abc 
				0x01, 0x02, 0x56, 0x78, (byte)0x9a, (byte)0xbc, // AS_SET 2 AS 0x5678 0x9abc
		}, PathAttributeCodec.encodePathAttribute(new ASPathAttribute(ASType.AS_NUMBER_2OCTETS, new PathSegment[] {
				new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_SET, new int[] {
						0x1234, 0x9abc,
				}),
				new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_SET, new int[] {
						0x5678, 0x9abc,
				}),
		})));
		
		assertBufferContents(new byte[] {
				(byte)0x40, (byte)0x02, (byte)0x04, // Path attribute: AS_PATH  
				0x02, 0x01, 0x12, 0x34, // AS_SEQUENCE 1 AS 0x1234
		}, PathAttributeCodec.encodePathAttribute(new ASPathAttribute(ASType.AS_NUMBER_2OCTETS, new PathSegment[] {
				new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_SEQUENCE, new int[] {
						0x1234
				}),
		})));
	
		assertBufferContents(new byte[] {
				(byte)0x40, (byte)0x02, (byte)0x06, // Path attribute: AS_PATH  
				0x02, 0x02, 0x12, 0x34, 0x56, 0x78 // AS_SEQUENCE 2 AS 0x1234 0x5678
		}, PathAttributeCodec.encodePathAttribute(new ASPathAttribute(ASType.AS_NUMBER_2OCTETS, new PathSegment[] {
				new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_SEQUENCE, new int[] {
						0x1234, 0x5678
				}),
		})));
	
		assertBufferContents(new byte[] {
				(byte)0x40, (byte)0x02, (byte)0x08, // Path attribute: AS_PATH  
				0x02, 0x01, 0x12, 0x34, // AS_SET 1 AS 0x1234 
				0x02, 0x01, 0x56, 0x78, // AS_SET 1 AS 0x5678
		}, PathAttributeCodec.encodePathAttribute(new ASPathAttribute(ASType.AS_NUMBER_2OCTETS, new PathSegment[] {
				new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_SEQUENCE, new int[] {
						0x1234, 
				}),
				new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_SEQUENCE, new int[] {
						0x5678, 
				}),
		})));
	
		assertBufferContents(new byte[] {
				(byte)0x40, (byte)0x02, (byte)0x0a, // Path attribute: AS_PATH  
				0x02, 0x02, 0x12, 0x34, (byte)0x9a, (byte)0xbc, // AS_SEQUENCE 2 AS 0x1234 0x9abc 
				0x02, 0x01, 0x56, 0x78, // AS_SEQUENCE 1 AS 0x5678
		}, PathAttributeCodec.encodePathAttribute(new ASPathAttribute(ASType.AS_NUMBER_2OCTETS, new PathSegment[] {
				new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_SEQUENCE, new int[] {
						0x1234, 0x9abc,
				}),
				new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_SEQUENCE, new int[] {
						0x5678, 
				}),
		})));
	
		assertBufferContents(new byte[] {
				(byte)0x40, (byte)0x02, (byte)0x0c, // Path attribute: AS_PATH  
				0x02, 0x02, 0x12, 0x34, (byte)0x9a, (byte)0xbc, // AS_SEQUENCE 2 AS 0x1234 0x9abc 
				0x02, 0x02, 0x56, 0x78, (byte)0x9a, (byte)0xbc, // AS_SEQUENCE 2 AS 0x5678 0x9abc
		}, PathAttributeCodec.encodePathAttribute(new ASPathAttribute(ASType.AS_NUMBER_2OCTETS, new PathSegment[] {
				new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_SEQUENCE, new int[] {
						0x1234, 0x9abc,
				}),
				new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_SEQUENCE, new int[] {
						0x5678, 0x9abc,
				}),
		})));
	
	}

	@Test
	public void testEncodeAS4PathAttribute() {
		assertBufferContents(new byte[] {
				(byte)0x40, (byte)0x11, (byte)0x00, // Path attribute: AS4_PATH emtpy 
		}, PathAttributeCodec.encodePathAttribute(new ASPathAttribute(ASType.AS_NUMBER_4OCTETS)));
	
		assertBufferContents(new byte[] {
				(byte)0x40, (byte)0x11, (byte)0x06, // Path attribute: AS4_PATH  
				0x01, 0x01, 0x12, 0x34, 0x12, 0x34, // AS_SET 1 AS 0x12341234
		}, PathAttributeCodec.encodePathAttribute(new ASPathAttribute(ASType.AS_NUMBER_4OCTETS, new PathSegment[] {
				new PathSegment(ASType.AS_NUMBER_4OCTETS, PathSegmentType.AS_SET, new int[] {
						0x12341234
				}),
		})));
	
		assertBufferContents(new byte[] {
				(byte)0x40, (byte)0x11, (byte)0x0a, // Path attribute: AS4_PATH  
				0x01, 0x02, 0x12, 0x34, 0x12, 0x34, 0x56, 0x78, 0x56, 0x78, // AS_SET 2 AS 0x12341234 0x56785678
		}, PathAttributeCodec.encodePathAttribute(new ASPathAttribute(ASType.AS_NUMBER_4OCTETS, new PathSegment[] {
				new PathSegment(ASType.AS_NUMBER_4OCTETS, PathSegmentType.AS_SET, new int[] {
						0x12341234, 0x56785678
				}),
		})));
	
		assertBufferContents(new byte[] {
				(byte)0x40, (byte)0x11, (byte)0x0c, // Path attribute: AS4_PATH  
				0x01, 0x01, 0x12, 0x34, 0x12, 0x34, // AS_SET 1 AS 0x12341234 
				0x01, 0x01, 0x56, 0x78, 0x56, 0x78, // AS_SET 1 AS 0x56785678
		}, PathAttributeCodec.encodePathAttribute(new ASPathAttribute(ASType.AS_NUMBER_4OCTETS, new PathSegment[] {
				new PathSegment(ASType.AS_NUMBER_4OCTETS, PathSegmentType.AS_SET, new int[] {
						0x12341234, 
				}),
				new PathSegment(ASType.AS_NUMBER_4OCTETS, PathSegmentType.AS_SET, new int[] {
						0x56785678, 
				}),
		})));
	
		assertBufferContents(new byte[] {
				(byte)0x40, (byte)0x11, (byte)0x10, // Path attribute: AS4_PATH  
				0x01, 0x02, 0x12, 0x34, 0x12, 0x34, (byte)0x9a, (byte)0xbc, (byte)0x9a, (byte)0xbc, // AS_SET 2 AS 0x12341234 0x9abc9abc 
				0x01, 0x01, 0x56, 0x78, 0x56, 0x78, // AS_SET 1 AS 0x56785678
		}, PathAttributeCodec.encodePathAttribute(new ASPathAttribute(ASType.AS_NUMBER_4OCTETS, new PathSegment[] {
				new PathSegment(ASType.AS_NUMBER_4OCTETS, PathSegmentType.AS_SET, new int[] {
						0x12341234, 0x9abc9abc,
				}),
				new PathSegment(ASType.AS_NUMBER_4OCTETS, PathSegmentType.AS_SET, new int[] {
						0x56785678, 
				}),
		})));
	
		assertBufferContents(new byte[] {
				(byte)0x40, (byte)0x11, (byte)0x14, // Path attribute: AS4_PATH  
				0x01, 0x02, 0x12, 0x34, 0x12, 0x34, (byte)0x9a, (byte)0xbc, (byte)0x9a, (byte)0xbc, // AS_SET 2 AS 0x1234 0x9abc 
				0x01, 0x02, 0x56, 0x78, 0x56, 0x78, (byte)0x9a, (byte)0xbc, (byte)0x9a, (byte)0xbc, // AS_SET 2 AS 0x5678 0x9abc
		}, PathAttributeCodec.encodePathAttribute(new ASPathAttribute(ASType.AS_NUMBER_4OCTETS, new PathSegment[] {
				new PathSegment(ASType.AS_NUMBER_4OCTETS, PathSegmentType.AS_SET, new int[] {
						0x12341234, 0x9abc9abc,
				}),
				new PathSegment(ASType.AS_NUMBER_4OCTETS, PathSegmentType.AS_SET, new int[] {
						0x56785678, 0x9abc9abc,
				}),
		})));
		
		assertBufferContents(new byte[] {
				(byte)0x40, (byte)0x11, (byte)0x06, // Path attribute: AS4_PATH  
				0x02, 0x01, 0x12, 0x34, 0x12, 0x34, // AS_SEQUENCE 1 AS 0x12341234
		}, PathAttributeCodec.encodePathAttribute(new ASPathAttribute(ASType.AS_NUMBER_4OCTETS, new PathSegment[] {
				new PathSegment(ASType.AS_NUMBER_4OCTETS, PathSegmentType.AS_SEQUENCE, new int[] {
						0x12341234
				}),
		})));
	
		assertBufferContents(new byte[] {
				(byte)0x40, (byte)0x11, (byte)0x0a, // Path attribute: AS4_PATH  
				0x02, 0x02, 0x12, 0x34, 0x12, 0x34, 0x56, 0x78, 0x56, 0x78, // AS_SEQUENCE 2 AS 0x12341234 0x56785678
		}, PathAttributeCodec.encodePathAttribute(new ASPathAttribute(ASType.AS_NUMBER_4OCTETS, new PathSegment[] {
				new PathSegment(ASType.AS_NUMBER_4OCTETS, PathSegmentType.AS_SEQUENCE, new int[] {
						0x12341234, 0x56785678
				}),
		})));
	
		assertBufferContents(new byte[] {
				(byte)0x40, (byte)0x11, (byte)0x0c, // Path attribute: AS4_PATH  
				0x02, 0x01, 0x12, 0x34, 0x12, 0x34, // AS_SET 1 AS 0x12341234 
				0x02, 0x01, 0x56, 0x78, 0x56, 0x78, // AS_SET 1 AS 0x56785678
		}, PathAttributeCodec.encodePathAttribute(new ASPathAttribute(ASType.AS_NUMBER_4OCTETS, new PathSegment[] {
				new PathSegment(ASType.AS_NUMBER_4OCTETS, PathSegmentType.AS_SEQUENCE, new int[] {
						0x12341234, 
				}),
				new PathSegment(ASType.AS_NUMBER_4OCTETS, PathSegmentType.AS_SEQUENCE, new int[] {
						0x56785678, 
				}),
		})));
	
		assertBufferContents(new byte[] {
				(byte)0x40, (byte)0x11, (byte)0x10, // Path attribute: AS4_PATH  
				0x02, 0x02, 0x12, 0x34, 0x12, 0x34, (byte)0x9a, (byte)0xbc, (byte)0x9a, (byte)0xbc, // AS_SEQUENCE 2 AS 0x12341234 0x9abc 9abc
				0x02, 0x01, 0x56, 0x78, 0x56, 0x78, // AS_SEQUENCE 1 AS 0x56785678
		}, PathAttributeCodec.encodePathAttribute(new ASPathAttribute(ASType.AS_NUMBER_4OCTETS, new PathSegment[] {
				new PathSegment(ASType.AS_NUMBER_4OCTETS, PathSegmentType.AS_SEQUENCE, new int[] {
						0x12341234, 0x9abc9abc,
				}),
				new PathSegment(ASType.AS_NUMBER_4OCTETS, PathSegmentType.AS_SEQUENCE, new int[] {
						0x56785678, 
				}),
		})));
	
		assertBufferContents(new byte[] {
				(byte)0x40, (byte)0x11, (byte)0x14, // Path attribute: AS4_PATH  
				0x02, 0x02, 0x12, 0x34, 0x12, 0x34, (byte)0x9a, (byte)0xbc, (byte)0x9a, (byte)0xbc, // AS_SEQUENCE 2 AS 0x12341234 0x9abc9abc 
				0x02, 0x02, 0x56, 0x78, 0x56, 0x78, (byte)0x9a, (byte)0xbc, (byte)0x9a, (byte)0xbc, // AS_SEQUENCE 2 AS 0x56785678 0x9abc9abc
		}, PathAttributeCodec.encodePathAttribute(new ASPathAttribute(ASType.AS_NUMBER_4OCTETS, new PathSegment[] {
				new PathSegment(ASType.AS_NUMBER_4OCTETS, PathSegmentType.AS_SEQUENCE, new int[] {
						0x12341234, 0x9abc9abc,
				}),
				new PathSegment(ASType.AS_NUMBER_4OCTETS, PathSegmentType.AS_SEQUENCE, new int[] {
						0x56785678, 0x9abc9abc,
				}),
		})));
	
	}

	@Test
	public void testEncodeASPath2ASSeqeunceOneASNumberASSeqeunceOneASNumberPacket() throws Exception {
		UpdatePacket update = new UpdatePacket();
		
		update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_2OCTETS, new PathSegment[] {
				new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_SEQUENCE, new int[] {
						0x1234,
				}),
				new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_SEQUENCE, new int[] {
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
	public void testEncodeASPath2ASSeqeunceOneASNumberASSeqeunceTwoASNumberPacket() throws Exception {
		UpdatePacket update = new UpdatePacket();
		
		update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_2OCTETS, new PathSegment[] {
				new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_SEQUENCE, new int[] {
						0x1234,
				}),
				new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_SEQUENCE, new int[] {
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
		
		update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_2OCTETS, new PathSegment[] {
				new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_SEQUENCE, new int[] {
						0x1234,
				}),
				new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_SET, new int[] {
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
	public void testEncodeASPath2ASSeqeunceOneASNumberASSetTwoASNumberPacket() throws Exception {
		UpdatePacket update = new UpdatePacket();
		
		update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_2OCTETS, new PathSegment[] {
				new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_SEQUENCE, new int[] {
						0x1234, 
				}),
				new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_SET, new int[] {
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
	public void testEncodeASPath2ASSeqeunceTw0ASNumberASSeqeunceOneASNumberPacket() throws Exception {
		UpdatePacket update = new UpdatePacket();
		
		update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_2OCTETS, new PathSegment[] {
				new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_SEQUENCE, new int[] {
						0x1234, 0x89ab,
				}),
				new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_SEQUENCE, new int[] {
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
		
		update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_2OCTETS, new PathSegment[] {
				new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_SEQUENCE, new int[] {
						0x1234, 0x89ab,
				}),
				new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_SEQUENCE, new int[] {
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
	public void testEncodeASPath2ASSeqeunceTw0ASNumberASSetASNumberPacket() throws Exception {
		UpdatePacket update = new UpdatePacket();
		
		update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_2OCTETS, new PathSegment[] {
				new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_SEQUENCE, new int[] {
						0x1234, 0x89ab,
				}),
				new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_SET, new int[] {
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
		
		update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_2OCTETS, new PathSegment[] {
				new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_SEQUENCE, new int[] {
						0x1234, 0x89ab,
				}),
				new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_SET, new int[] {
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
	public void testEncodeASPath2ASSequenceOneASNumberPacket() throws Exception {
		UpdatePacket update = new UpdatePacket();
		
		update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_2OCTETS, new PathSegment[] {
				new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_SEQUENCE, new int[] {
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
		
		update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_2OCTETS, new PathSegment[] {
				new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_SEQUENCE, new int[] {
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
	public void testEncodeASPath2ASSetOneASNumberASSeqeunceOneASNumberPacket() throws Exception {
		UpdatePacket update = new UpdatePacket();
		
		update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_2OCTETS, new PathSegment[] {
				new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_SET, new int[] {
						0x1234, 
				}),
				new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_SEQUENCE, new int[] {
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
	public void testEncodeASPath2ASSetOneASNumberASSeqeunceTwoASNumberPacket() throws Exception {
		UpdatePacket update = new UpdatePacket();
		
		update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_2OCTETS, new PathSegment[] {
				new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_SET, new int[] {
						0x1234,
				}),
				new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_SEQUENCE, new int[] {
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
	public void testEncodeASPath2ASSetOneASNumberPacket() throws Exception {
		UpdatePacket update = new UpdatePacket();
		
		update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_2OCTETS, new PathSegment[] {
				new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_SET, new int[] {
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
	public void testEncodeASPath2ASSetTw0ASNumberASSeqeunceOneASNumberPacket() throws Exception {
		UpdatePacket update = new UpdatePacket();
		
		update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_2OCTETS, new PathSegment[] {
				new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_SET, new int[] {
						0x1234, 0x89ab,
				}),
				new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_SEQUENCE, new int[] {
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
		
		update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_2OCTETS, new PathSegment[] {
				new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_SET, new int[] {
						0x1234, 0x89ab,
				}),
				new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_SEQUENCE, new int[] {
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
	public void testEncodeASPath2ASSetTwoASNumberPacket() throws Exception {
		UpdatePacket update = new UpdatePacket();
		
		update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_2OCTETS, new PathSegment[] {
				new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_SET, new int[] {
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
	public void testEncodeASPath4ASSeqeunceOneASNumberASSeqeunceOneASNumberPacket() throws Exception {
		UpdatePacket update = new UpdatePacket();
		
		update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_4OCTETS, new PathSegment[] {
				new PathSegment(ASType.AS_NUMBER_4OCTETS, PathSegmentType.AS_SEQUENCE, new int[] {
						0x00001234, 
				}),
				new PathSegment(ASType.AS_NUMBER_4OCTETS, PathSegmentType.AS_SEQUENCE, new int[] {
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
	public void testEncodeASPath4ASSeqeunceOneASNumberASSeqeunceTwoASNumberPacket() throws Exception {
		UpdatePacket update = new UpdatePacket();
		
		update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_4OCTETS, new PathSegment[] {
				new PathSegment(ASType.AS_NUMBER_4OCTETS, PathSegmentType.AS_SEQUENCE, new int[] {
						0x00001234, 
				}),
				new PathSegment(ASType.AS_NUMBER_4OCTETS, PathSegmentType.AS_SEQUENCE, new int[] {
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
		
		update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_4OCTETS, new PathSegment[] {
				new PathSegment(ASType.AS_NUMBER_4OCTETS, PathSegmentType.AS_SEQUENCE, new int[] {
						0x00001234, 
				}),
				new PathSegment(ASType.AS_NUMBER_4OCTETS, PathSegmentType.AS_SET, new int[] {
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
	public void testEncodeASPath4ASSeqeunceOneASNumberASSetTwoASNumberPacket() throws Exception {
		UpdatePacket update = new UpdatePacket();
		
		update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_4OCTETS, new PathSegment[] {
				new PathSegment(ASType.AS_NUMBER_4OCTETS, PathSegmentType.AS_SEQUENCE, new int[] {
						0x00001234,
				}),
				new PathSegment(ASType.AS_NUMBER_4OCTETS, PathSegmentType.AS_SET, new int[] {
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
	public void testEncodeASPath4ASSeqeunceTw0ASNumberASSeqeunceOneASNumberPacket() throws Exception {
		UpdatePacket update = new UpdatePacket();
		
		update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_4OCTETS, new PathSegment[] {
				new PathSegment(ASType.AS_NUMBER_4OCTETS, PathSegmentType.AS_SEQUENCE, new int[] {
						0x00001234, 0x000089ab
				}),
				new PathSegment(ASType.AS_NUMBER_4OCTETS, PathSegmentType.AS_SEQUENCE, new int[] {
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
		
		update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_4OCTETS, new PathSegment[] {
				new PathSegment(ASType.AS_NUMBER_4OCTETS, PathSegmentType.AS_SEQUENCE, new int[] {
						0x00001234, 0x000089ab
				}),
				new PathSegment(ASType.AS_NUMBER_4OCTETS, PathSegmentType.AS_SEQUENCE, new int[] {
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
	public void testEncodeASPath4ASSeqeunceTw0ASNumberASSetASNumberPacket() throws Exception {
		UpdatePacket update = new UpdatePacket();
		
		update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_4OCTETS, new PathSegment[] {
				new PathSegment(ASType.AS_NUMBER_4OCTETS, PathSegmentType.AS_SEQUENCE, new int[] {
						0x00001234, 0x000089ab
				}),
				new PathSegment(ASType.AS_NUMBER_4OCTETS, PathSegmentType.AS_SET, new int[] {
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
		
		update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_4OCTETS, new PathSegment[] {
				new PathSegment(ASType.AS_NUMBER_4OCTETS, PathSegmentType.AS_SEQUENCE, new int[] {
						0x00001234, 0x000089ab
				}),
				new PathSegment(ASType.AS_NUMBER_4OCTETS, PathSegmentType.AS_SET, new int[] {
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
	public void testEncodeASPath4ASSequenceOneASNumberPacket() throws Exception {
		UpdatePacket update = new UpdatePacket();
		
		update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_4OCTETS, new PathSegment[] {
				new PathSegment(ASType.AS_NUMBER_4OCTETS, PathSegmentType.AS_SEQUENCE, new int[] {
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
		
		update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_4OCTETS, new PathSegment[] {
				new PathSegment(ASType.AS_NUMBER_4OCTETS, PathSegmentType.AS_SEQUENCE, new int[] {
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
	public void testEncodeASPath4ASSetOneASNumberASSeqeunceOneASNumberPacket() throws Exception {
		UpdatePacket update = new UpdatePacket();
		
		update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_4OCTETS, new PathSegment[] {
				new PathSegment(ASType.AS_NUMBER_4OCTETS, PathSegmentType.AS_SET, new int[] {
						0x00001234,
				}),
				new PathSegment(ASType.AS_NUMBER_4OCTETS, PathSegmentType.AS_SEQUENCE, new int[] {
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
	public void testEncodeASPath4ASSetOneASNumberASSeqeunceTwoASNumberPacket() throws Exception {
		UpdatePacket update = new UpdatePacket();
		
		update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_4OCTETS, new PathSegment[] {
				new PathSegment(ASType.AS_NUMBER_4OCTETS, PathSegmentType.AS_SET, new int[] {
						0x00001234, 
				}),
				new PathSegment(ASType.AS_NUMBER_4OCTETS, PathSegmentType.AS_SEQUENCE, new int[] {
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
	public void testEncodeASPath4ASSetOneASNumberPacket() throws Exception {
		UpdatePacket update = new UpdatePacket();
		
		update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_4OCTETS, new PathSegment[] {
				new PathSegment(ASType.AS_NUMBER_4OCTETS, PathSegmentType.AS_SET, new int[] {
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
	public void testEncodeASPath4ASSetTw0ASNumberASSeqeunceOneASNumberPacket() throws Exception {
		UpdatePacket update = new UpdatePacket();
		
		update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_4OCTETS, new PathSegment[] {
				new PathSegment(ASType.AS_NUMBER_4OCTETS, PathSegmentType.AS_SET, new int[] {
						0x00001234, 0x000089ab,
				}),
				new PathSegment(ASType.AS_NUMBER_4OCTETS, PathSegmentType.AS_SEQUENCE, new int[] {
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
		
		update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_4OCTETS, new PathSegment[] {
				new PathSegment(ASType.AS_NUMBER_4OCTETS, PathSegmentType.AS_SET, new int[] {
						0x00001234, 0x000089ab,
				}),
				new PathSegment(ASType.AS_NUMBER_4OCTETS, PathSegmentType.AS_SEQUENCE, new int[] {
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
	public void testEncodeASPath4ASSetTwoASNumberPacket() throws Exception {
		UpdatePacket update = new UpdatePacket();
		
		update.getPathAttributes().add(new ASPathAttribute(ASType.AS_NUMBER_4OCTETS, new PathSegment[] {
				new PathSegment(ASType.AS_NUMBER_4OCTETS, PathSegmentType.AS_SET, new int[] {
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
	public void testEncodeLocalPrefPathAttribute() throws Exception {
		UpdatePacket update = new UpdatePacket();
	
		update.getPathAttributes().add(new LocalPrefPathAttribute(100));
	
		assertBufferContents(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0x00, (byte)0x1e, // length 30
				(byte)0x02, // type code UPDATE
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x07, // path attributes length (5 octets)
				(byte)0x40, (byte)0x05, (byte)0x04, // Path attribute: LOCAL_PREF
				0x00, 0x00, 0x00, 0x64, // LOCAL_PREF 100
		}, update.encodePacket());
	}

	@Test
	public void testEncodeMpReachNlriUpdatePacketFourByteNextHopOneNlri() {
		UpdatePacket update = new UpdatePacket();
	
		update.getPathAttributes().add(new MultiProtocolReachableNLRI(AddressFamily.IPv4, 
				SubsequentAddressFamily.NLRI_UNICAST_FORWARDING,
				new byte[] { (byte)0xc0, (byte)0xa8, 0x04, 0x02 },
				new NetworkLayerReachabilityInformation[] {
				new NetworkLayerReachabilityInformation(12, new byte[] { (byte)0xac, (byte)0x10, }),
		}));
		
		assertBufferContents(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0x00, (byte)0x26, // length 38
				(byte)0x02, // type code UPDATE
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x0f, // path attributes length (15 octets)
				(byte)0x80, 0x0e, 0x0c, // MP_REACH_NLRI attribute (12 octets)
				0x00, 0x01, // AFI IPv4
				0x01, // safi unicast routing
				0x04, (byte)0xc0, (byte)0xa8, 0x04, 0x02, // next hop length 4 octets, next hop 192.168.4.2
				0x00, // reserved
				0x0c, (byte)0xac, (byte)0x10, // NLRI 172.16.0.0/12
		}, update.encodePacket());
	}

	@Test
	public void testEncodeMpReachNlriUpdatePacketFourByteNextHopTwoNlri() {
		UpdatePacket update = new UpdatePacket();
	
		update.getPathAttributes().add(new MultiProtocolReachableNLRI(AddressFamily.IPv4, 
				SubsequentAddressFamily.NLRI_UNICAST_FORWARDING,
				new byte[] { (byte)0xc0, (byte)0xa8, 0x04, 0x02 },
				new NetworkLayerReachabilityInformation[] {
				new NetworkLayerReachabilityInformation(12, new byte[] { (byte)0xac, (byte)0x10, }),
				new NetworkLayerReachabilityInformation(12, new byte[] { (byte)0xac, (byte)0x20, }),
		}));
		
		assertBufferContents(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0x00, (byte)0x29, // length 41
				(byte)0x02, // type code UPDATE
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x12, // path attributes length (18 octets)
				(byte)0x80, 0x0e, 0x0f, // MP_REACH_NLRI attribute (15 octets)
				0x00, 0x01, // AFI IPv4
				0x01, // safi unicast routing
				0x04, (byte)0xc0, (byte)0xa8, 0x04, 0x02, // next hop length 4 octets, next hop 192.168.4.2
				0x00, // reserved
				0x0c, (byte)0xac, (byte)0x10, // NLRI 172.16.0.0/12
				0x0c, (byte)0xac, (byte)0x20, // NLRI 172.32.0.0/12
		}, update.encodePacket());
	}

	@Test
	public void testEncodeMpReachNlriUpdatePacketFourByteNextHopZeroNlri() {
		UpdatePacket update = new UpdatePacket();
	
		update.getPathAttributes().add(new MultiProtocolReachableNLRI(AddressFamily.IPv4, 
				SubsequentAddressFamily.NLRI_UNICAST_FORWARDING,
				new byte[] { (byte)0xc0, (byte)0xa8, 0x04, 0x02 }));
		
		assertBufferContents(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0x00, (byte)0x23, // length 35
				(byte)0x02, // type code UPDATE
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x0c, // path attributes length (12 octets)
				(byte)0x80, 0x0e, 0x09, // MP_REACH_NLRI attribute (9 octets)
				0x00, 0x01, // AFI IPv4
				0x01, // safi unicast routing
				0x04, (byte)0xc0, (byte)0xa8, 0x04, 0x02, // next hop length 4 octets, next hop 192.168.4.2
				0x00, // reserved
		}, update.encodePacket());
	}

	@Test
	public void testEncodeMpReachNlriUpdatePacketNullNextHopOneNlri() {
		UpdatePacket update = new UpdatePacket();
	
		update.getPathAttributes().add(new MultiProtocolReachableNLRI(AddressFamily.IPv4, 
				SubsequentAddressFamily.NLRI_UNICAST_FORWARDING,
				(BinaryNextHop)null,
				new NetworkLayerReachabilityInformation[] {
				new NetworkLayerReachabilityInformation(12, new byte[] { (byte)0xac, (byte)0x10, }),
		}));
		
		assertBufferContents(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0x00, (byte)0x22, // length 34
				(byte)0x02, // type code UPDATE
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x0b, // path attributes length (11 octets)
				(byte)0x80, 0x0e, 0x08, // MP_REACH_NLRI attribute (8 octets)
				0x00, 0x01, // AFI IPv4
				0x01, // safi unicast routing
				0x00, // next hop length 0
				0x00, // reserved
				0x0c, (byte)0xac, (byte)0x10, // NLRI 172.16.0.0/12
		}, update.encodePacket());
	}

	@Test
	public void testEncodeMpReachNlriUpdatePacketNullNextHopZeroNlri() {
		UpdatePacket update = new UpdatePacket();
	
		update.getPathAttributes().add(new MultiProtocolReachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING));
		
		assertBufferContents(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0x00, (byte)0x1f, // length 31
				(byte)0x02, // type code UPDATE
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x08, // path attributes length (8 octets)
				(byte)0x80, 0x0e, 0x05, // MP_REACH_NLRI attribute (5 octets)
				0x00, 0x01, // AFI IPv4
				0x01, // safi unicast routing
				0x00, // next hop length
				0x00, // reserved
		}, update.encodePacket());
	}

	@Test
	public void testEncodeMultiExitDiscPathAttribute() {
		assertBufferContents(new byte[] {
				(byte)0x80, (byte)0x04, (byte)0x04, (byte)0x00, (byte)0x00, (byte)0x08, (byte)0x00, // Path attribute: MULT_EXIT_DISC 2048
		}, PathAttributeCodec.encodePathAttribute(new MultiExitDiscPathAttribute(2048)));
		
	}

	@Test
	public void testEncodeNextHopPathAttribute() throws Exception {
		assertBufferContents(new byte[] {
				(byte)0x40, (byte)0x03, (byte)0x04, (byte)0xc0, (byte)0xa8, (byte)0x04, (byte)0x02, // Path attribute: NEXT_HOP 192.168.4.2
		}, PathAttributeCodec.encodePathAttribute(new NextHopPathAttribute((Inet4Address)Inet4Address.getByAddress(new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x04, (byte)0x02 }))));
	}

	@Test
	public void testEncodeOneNlriUpdatePacket() throws Exception {
		UpdatePacket update = new UpdatePacket();
	
		update.getNlris().add(new NetworkLayerReachabilityInformation(16, new byte[] {(byte)0xac, 0x10, }));
	
		assertBufferContents(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0x00, (byte)0x1a, // length 26
				(byte)0x02, // type code UPDATE
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x00, // path attributes length (0 octets)
				0x10, (byte)0xac, 0x10, // NLRI 172.16/16
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
	public void testEncodeOriginPathAttribute() {
		assertBufferContents(new byte[] {
				(byte)0x40, (byte)0x01, (byte)0x01, (byte)0x00, // Path attribute: ORIGIN IGP
		}, PathAttributeCodec.encodePathAttribute(new OriginPathAttribute(Origin.IGP)));
	
		assertBufferContents(new byte[] {
				(byte)0x40, (byte)0x01, (byte)0x01, (byte)0x01, // Path attribute: ORIGIN EGP
		}, PathAttributeCodec.encodePathAttribute(new OriginPathAttribute(Origin.EGP)));
	
		assertBufferContents(new byte[] {
				(byte)0x40, (byte)0x01, (byte)0x01, (byte)0x02, // Path attribute: ORIGIN INCOMPLETE
		}, PathAttributeCodec.encodePathAttribute(new OriginPathAttribute(Origin.INCOMPLETE)));
	}

	@Test
	public void testEncodeTwoNlriUpdatePacket() throws Exception {
		UpdatePacket update = new UpdatePacket();
	
		update.getNlris().add(new NetworkLayerReachabilityInformation(16, new byte[] {(byte)0xac, 0x10, }));
		update.getNlris().add(new NetworkLayerReachabilityInformation(28, new byte[] {(byte)0xc0, (byte)0xa8, 0x20, 0 }));
	
		assertBufferContents(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0x00, (byte)0x1f, // length 31
				(byte)0x02, // type code UPDATE
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x00, // path attributes length (0 octets)
				0x10, (byte)0xac, 0x10, // NLRI 172.16/16
				0x1c, (byte)0xc0, (byte)0xa8, 0x20, 0x00 // NLRI 192.168.32/28 bogus one octet missing
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
	public void testEncodeMpUnreachNlriUpdatePacketHopOneNlri() {
		UpdatePacket update = new UpdatePacket();
	
		update.getPathAttributes().add(new MultiProtocolUnreachableNLRI(AddressFamily.IPv4, 
				SubsequentAddressFamily.NLRI_UNICAST_FORWARDING,
				new NetworkLayerReachabilityInformation[] {
				new NetworkLayerReachabilityInformation(12, new byte[] { (byte)0xac, (byte)0x10, }),
		}));
		
		assertBufferContents(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0x00, (byte)0x20, // length 32
				(byte)0x02, // type code UPDATE
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x09, // path attributes length (9 octets)
				(byte)0x80, 0x0f, 0x06, // MP_REACH_NLRI attribute (6 octets)
				0x00, 0x01, // AFI IPv4
				0x01, // safi unicast routing
				0x0c, (byte)0xac, (byte)0x10, // NLRI 172.16.0.0/12
		}, update.encodePacket());
	}

	@Test
	public void testEncodeMpUnreachNlriUpdatePacketHopTwoNlri() {
		UpdatePacket update = new UpdatePacket();
	
		update.getPathAttributes().add(new MultiProtocolUnreachableNLRI(AddressFamily.IPv4, 
				SubsequentAddressFamily.NLRI_UNICAST_FORWARDING,
				new NetworkLayerReachabilityInformation[] {
				new NetworkLayerReachabilityInformation(12, new byte[] { (byte)0xac, (byte)0x10, }),
				new NetworkLayerReachabilityInformation(12, new byte[] { (byte)0xac, (byte)0x20, }),
		}));
		
		assertBufferContents(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0x00, (byte)0x23, // length 35
				(byte)0x02, // type code UPDATE
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x0c, // path attributes length (12 octets)
				(byte)0x80, 0x0f, 0x09, // MP_REACH_NLRI attribute (9 octets)
				0x00, 0x01, // AFI IPv4
				0x01, // safi unicast routing
				0x0c, (byte)0xac, (byte)0x10, // NLRI 172.16.0.0/12
				0x0c, (byte)0xac, (byte)0x20, // NLRI 172.32.0.0/12
		}, update.encodePacket());
	}

	@Test
	public void testEncodeMpUnreachNlriUpdatePacketZeroNlri() {
		UpdatePacket update = new UpdatePacket();
	
		update.getPathAttributes().add(new MultiProtocolUnreachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING));
		
		assertBufferContents(new byte[] {
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, // marker
				(byte)0x00, (byte)0x1d, // length 39
				(byte)0x02, // type code UPDATE
				(byte)0x00, (byte)0x00, // withdrawn routes length (0 octets)
				(byte)0x00, (byte)0x06, // path attributes length (6 octets)
				(byte)0x80, 0x0f, 0x03, // MP_REACH_NLRI attribute (3 octets)
				0x00, 0x01, // AFI IPv4
				0x01, // safi unicast routing
		}, update.encodePacket());
	}

	@Test
	public void testEncodeOriginatorIDPathAttribute() throws Exception {
		assertBufferContents(new byte[] {
				(byte)0x80, (byte)0x09, (byte)0x04, (byte)0xc0, (byte)0xa8, (byte)0x04, (byte)0x02, // Path attribute: NEXT_HOP 192.168.4.2
		}, PathAttributeCodec.encodePathAttribute(new OriginatorIDPathAttribute(0xc0a80402)));
	}

	@Test
	public void testEncodeClusterListPathAttributeOneClusterID() throws Exception {
		assertBufferContents(new byte[] {
				(byte)0x80, (byte)0x0a, (byte)0x04, // Path attribute CLUSTER_LIST (4 octets)
				(byte)0xc0, (byte)0xa8, (byte)0x04, (byte)0x02, // Cluster ID NEXT_HOP 192.168.4.2
		}, PathAttributeCodec.encodePathAttribute(new ClusterListPathAttribute(new int[] { 0xc0a80402 })));
	}

	@Test
	public void testEncodeClusterListPathAttributeTwoClusterID() throws Exception {
		assertBufferContents(new byte[] {
				(byte)0x80, (byte)0x0a, (byte)0x08, // Path attribute CLUSTER_LIST (4 octets)
				(byte)0xc0, (byte)0xa8, (byte)0x04, (byte)0x02, // Cluster ID NEXT_HOP 192.168.4.2
				(byte)0xc0, (byte)0xa8, (byte)0x05, (byte)0x03, // Cluster ID NEXT_HOP 192.168.5.3
		}, PathAttributeCodec.encodePathAttribute(new ClusterListPathAttribute(new int[] { 0xc0a80402, 0xc0a80503 })));
	}
}
