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
 * File: org.bgp4j.netty.protocol.NetworkLayerReachabiityInformationTest.java 
 */
package org.bgp4j.netty.protocol;

import junit.framework.Assert;

import org.bgp4j.net.NetworkLayerReachabilityInformation;
import org.bgp4j.netty.BGPv4TestBase;
import org.bgp4j.netty.util.NLRICodec;
import org.junit.Test;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class NLRICodecTest extends BGPv4TestBase {

	@Test
	public void testEncodinglength() {
		NetworkLayerReachabilityInformation nlri = new NetworkLayerReachabilityInformation();
		
		nlri.setPrefix(0, null);
		Assert.assertEquals(1, NLRICodec.calculateEncodedNLRILength(nlri));

		nlri.setPrefix(0, new byte[0]);
		Assert.assertEquals(1, NLRICodec.calculateEncodedNLRILength(nlri));

		for(int byteSize=1; byteSize<16; byteSize++) {
			for(int inByteLength=1; inByteLength<8; inByteLength++) {
				int prefixLength = (byteSize-1) * 8 + inByteLength;
				
				nlri.setPrefix(prefixLength, new byte[byteSize]);
			
				Assert.assertEquals(byteSize+1, NLRICodec.calculateEncodedNLRILength(nlri));
			}			
		}
	}
	
	@Test
	public void testNullPrefixNonZeroPrefixLength() throws Exception {
		(new AssertExecption() {
			
			@Override
			protected void doExecute() {
				NetworkLayerReachabilityInformation nlri = new NetworkLayerReachabilityInformation();
				
				nlri.setPrefix(1, null);				
			}
		}).execute(IllegalArgumentException.class);
	}
	
	@Test
	public void testTooShortPrefixNonZeroPrefixLength() throws Exception {
		(new AssertExecption() {
			
			@Override
			protected void doExecute() {
				NetworkLayerReachabilityInformation nlri = new NetworkLayerReachabilityInformation();
				
				nlri.setPrefix(9, new byte[1]);				
			}
		}).execute(IllegalArgumentException.class);
	}
	
	@Test
	public void testTooLongPrefixNonZeroPrefixLength() throws Exception {
		(new AssertExecption() {
			
			@Override
			protected void doExecute() {
				NetworkLayerReachabilityInformation nlri = new NetworkLayerReachabilityInformation();
				
				nlri.setPrefix(9, new byte[3]);				
			}
		}).execute(IllegalArgumentException.class);
	}
	
	@Test
	public void testDecodeNLRIZeroPrefixLengthNullPrefix() {
		NetworkLayerReachabilityInformation nlri = NLRICodec.decodeNLRI(buildProtocolPacket(new byte[] {
				0x00, // prefix length 0
		}));
		
		Assert.assertEquals(0, nlri.getPrefixLength());
	}
	
	@Test
	public void testDecodeNLRIFourBitsPrefixLengthOneBytePrefix() {
		NetworkLayerReachabilityInformation nlri = NLRICodec.decodeNLRI(buildProtocolPacket(new byte[] {
				0x04, // prefix length 0
				(byte)0xac, // prefix value 172
		}));
		
		Assert.assertEquals(4, nlri.getPrefixLength());
		Assert.assertEquals(1, nlri.getPrefix().length);
		assertArraysEquals(new byte[] {
				(byte)0xa0, // mask is four bits so expect prefix value 0xa0 == 160 
			}, nlri.getPrefix());
	}
	
	@Test
	public void testDecodeNLRIEightBitsPrefixLengthOneBytePrefix() {
		NetworkLayerReachabilityInformation nlri = NLRICodec.decodeNLRI(buildProtocolPacket(new byte[] {
				0x08, // prefix length 0
				(byte)0xac, // prefix value 172
		}));
		
		Assert.assertEquals(8, nlri.getPrefixLength());
		Assert.assertEquals(1, nlri.getPrefix().length);
		assertArraysEquals(new byte[] {
				(byte)0xac, // prefix value 172 
			}, nlri.getPrefix());
	}

	@Test
	public void testDecodeNLRITwelveBitsPrefixLengthTwoBytePrefix() {
		NetworkLayerReachabilityInformation nlri = NLRICodec.decodeNLRI(buildProtocolPacket(new byte[] {
				0x0c, // prefix length 0
				(byte)0xac, 0x12 // prefix value 172.18
		}));
		
		Assert.assertEquals(12, nlri.getPrefixLength());
		Assert.assertEquals(2, nlri.getPrefix().length);
		assertArraysEquals(new byte[] {
				(byte)0xac, 0x10 // mask is twelve bits so expect prefix value 0xa0 == 172.16 
			}, nlri.getPrefix());
	}
	
	@Test
	public void testDecodeNLRISixteenBitsPrefixLengthTwoBytePrefix() {
		NetworkLayerReachabilityInformation nlri = NLRICodec.decodeNLRI(buildProtocolPacket(new byte[] {
				0x10, // prefix length 0
				(byte)0xac, 0x12 // prefix value 172.18
		}));
		
		Assert.assertEquals(16, nlri.getPrefixLength());
		Assert.assertEquals(2, nlri.getPrefix().length);
		assertArraysEquals(new byte[] {
				(byte)0xac, 0x12 // prefix value 172.18 
			}, nlri.getPrefix());
	}

	@Test
	public void testDecodeNLRITwentyBitsPrefixLengthThreeBytePrefix() {
		NetworkLayerReachabilityInformation nlri = NLRICodec.decodeNLRI(buildProtocolPacket(new byte[] {
				0x14, // prefix length 0
				(byte)0xac, 0x12, (byte)0xfe // prefix value 172.18.254
		}));
		
		Assert.assertEquals(20, nlri.getPrefixLength());
		Assert.assertEquals(3, nlri.getPrefix().length);
		assertArraysEquals(new byte[] {
				(byte)0xac, 0x12, (byte)0xf0 // prefix value 172.18.240 
			}, nlri.getPrefix());
	}
	
	@Test
	public void testDecodeNLRITwentyFourBitsPrefixLengthThreeBytePrefix() {
		NetworkLayerReachabilityInformation nlri = NLRICodec.decodeNLRI(buildProtocolPacket(new byte[] {
				0x18, // prefix length 0
				(byte)0xac, 0x12, (byte)0xfe // prefix value 172.18.254
		}));
		
		Assert.assertEquals(24, nlri.getPrefixLength());
		Assert.assertEquals(3, nlri.getPrefix().length);
		assertArraysEquals(new byte[] {
				(byte)0xac, 0x12, (byte)0xfe // prefix value 172.18.254 
			}, nlri.getPrefix());
	}

	@Test
	public void testDecodeNLRITwentyEightBitsPrefixLengthFourBytePrefix() {
		NetworkLayerReachabilityInformation nlri = NLRICodec.decodeNLRI(buildProtocolPacket(new byte[] {
				0x1c, // prefix length 0
				(byte)0xac, 0x12, (byte)0xfe, (byte)0xff // prefix value 172.18.254.255
		}));
		
		Assert.assertEquals(28, nlri.getPrefixLength());
		Assert.assertEquals(4, nlri.getPrefix().length);
		assertArraysEquals(new byte[] {
				(byte)0xac, 0x12, (byte)0xfe, (byte)0xf0 // prefix value 172.18.240 
			}, nlri.getPrefix());
	}
	
	@Test
	public void testDecodeNLRIThirtyTwoFourBitsPrefixLengthFourBytePrefix() {
		NetworkLayerReachabilityInformation nlri = NLRICodec.decodeNLRI(buildProtocolPacket(new byte[] {
				0x20, // prefix length 0
				(byte)0xac, 0x12, (byte)0xfe, (byte)0xff // prefix value 172.18.254.255
		}));
		
		Assert.assertEquals(32, nlri.getPrefixLength());
		Assert.assertEquals(4, nlri.getPrefix().length);
		assertArraysEquals(new byte[] {
				(byte)0xac, 0x12, (byte)0xfe, (byte)0xff // prefix value 172.18.254 
			}, nlri.getPrefix());
	}
}
