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

import org.bgp4j.netty.NetworkLayerReachabilityInformation;
import org.junit.Test;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class NetworkLayerReachabilityInformationTest extends ProtocolPacketTestBase {

	@Test
	public void testEncodinglength() {
		NetworkLayerReachabilityInformation nlri = new NetworkLayerReachabilityInformation();
		
		nlri.setPrefix(0, null);
		Assert.assertEquals(0, nlri.calculatePacketSize());

		nlri.setPrefix(0, new byte[0]);
		Assert.assertEquals(0, nlri.calculatePacketSize());

		for(int byteSize=1; byteSize<16; byteSize++) {
			for(int inByteLength=1; inByteLength<8; inByteLength++) {
				int prefixLength = (byteSize-1) * 8 + inByteLength;
				
				nlri.setPrefix(prefixLength, new byte[byteSize]);
			
				Assert.assertEquals(byteSize, nlri.calculatePacketSize());
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
		NetworkLayerReachabilityInformation nlri = NetworkLayerReachabilityInformation.decodeNLRI(buildProtocolPacket(new byte[] {
				0x00, // prefix length 0
		}));
		
		Assert.assertEquals(0, nlri.getPrefixLength());
	}
	
	@Test
	public void testDecodeNLRIFourBitsPrefixLengthOneBytePrefix() {
		NetworkLayerReachabilityInformation nlri = NetworkLayerReachabilityInformation.decodeNLRI(buildProtocolPacket(new byte[] {
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
		NetworkLayerReachabilityInformation nlri = NetworkLayerReachabilityInformation.decodeNLRI(buildProtocolPacket(new byte[] {
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
		NetworkLayerReachabilityInformation nlri = NetworkLayerReachabilityInformation.decodeNLRI(buildProtocolPacket(new byte[] {
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
		NetworkLayerReachabilityInformation nlri = NetworkLayerReachabilityInformation.decodeNLRI(buildProtocolPacket(new byte[] {
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
		NetworkLayerReachabilityInformation nlri = NetworkLayerReachabilityInformation.decodeNLRI(buildProtocolPacket(new byte[] {
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
		NetworkLayerReachabilityInformation nlri = NetworkLayerReachabilityInformation.decodeNLRI(buildProtocolPacket(new byte[] {
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
		NetworkLayerReachabilityInformation nlri = NetworkLayerReachabilityInformation.decodeNLRI(buildProtocolPacket(new byte[] {
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
		NetworkLayerReachabilityInformation nlri = NetworkLayerReachabilityInformation.decodeNLRI(buildProtocolPacket(new byte[] {
				0x20, // prefix length 0
				(byte)0xac, 0x12, (byte)0xfe, (byte)0xff // prefix value 172.18.254.255
		}));
		
		Assert.assertEquals(32, nlri.getPrefixLength());
		Assert.assertEquals(4, nlri.getPrefix().length);
		assertArraysEquals(new byte[] {
				(byte)0xac, 0x12, (byte)0xfe, (byte)0xff // prefix value 172.18.254 
			}, nlri.getPrefix());
	}
	
	@Test
	public void testIsPrefixFourFive() {
		NetworkLayerReachabilityInformation first = new NetworkLayerReachabilityInformation(4, new byte[] { (byte)0xa0 });
		NetworkLayerReachabilityInformation second = new NetworkLayerReachabilityInformation(5, new byte[] { (byte)0xa8 });
		
		Assert.assertTrue(first.isPrefixOf(second));
		Assert.assertFalse(second.isPrefixOf(first));
	}

	@Test
	public void testIsPrefixEightEight() {
		NetworkLayerReachabilityInformation first = new NetworkLayerReachabilityInformation(8, new byte[] { (byte)0xa0 });
		NetworkLayerReachabilityInformation second = new NetworkLayerReachabilityInformation(8, new byte[] { (byte)0xa8 });
		
		Assert.assertFalse(first.isPrefixOf(second));
		Assert.assertFalse(second.isPrefixOf(first));
	}
	
	@Test
	public void testIsPrefixSevenEight() {
		NetworkLayerReachabilityInformation first = new NetworkLayerReachabilityInformation(7, new byte[] { (byte)0xa0 });
		NetworkLayerReachabilityInformation second = new NetworkLayerReachabilityInformation(8, new byte[] { (byte)0xa8 });
		
		Assert.assertFalse(first.isPrefixOf(second));
		Assert.assertFalse(second.isPrefixOf(first));
	}
	
	@Test
	public void testIsPrefixEightSixteen() {
		NetworkLayerReachabilityInformation first = new NetworkLayerReachabilityInformation(8, new byte[] { (byte)0xc8 });
		NetworkLayerReachabilityInformation second = new NetworkLayerReachabilityInformation(16, new byte[] { (byte)0xc8, (byte)0xa8 });
		
		Assert.assertTrue(first.isPrefixOf(second));
		Assert.assertFalse(second.isPrefixOf(first));
	}

	@Test
	public void testIsNoPrefixEightSixteen() {
		NetworkLayerReachabilityInformation first = new NetworkLayerReachabilityInformation(8, new byte[] { (byte)0xc0 });
		NetworkLayerReachabilityInformation second = new NetworkLayerReachabilityInformation(16, new byte[] { (byte)0xc8, (byte)0xa8 });
		
		Assert.assertFalse(first.isPrefixOf(second));
		Assert.assertFalse(second.isPrefixOf(first));
	}

	@Test
	public void testIsPrefixThirtyThirtytwo() {
		NetworkLayerReachabilityInformation first = new NetworkLayerReachabilityInformation(30, new byte[] { (byte)0xc8, (byte)0xa8, 0x09, 0x04 });
		NetworkLayerReachabilityInformation second = new NetworkLayerReachabilityInformation(32, new byte[] { (byte)0xc8, (byte)0xa8, 0x09, 0x07 });
		
		Assert.assertTrue(first.isPrefixOf(second));
		Assert.assertFalse(second.isPrefixOf(first));
	}

	@Test
	public void testIsPrefixZeroEight() {
		NetworkLayerReachabilityInformation first = new NetworkLayerReachabilityInformation(0, null);
		NetworkLayerReachabilityInformation second = new NetworkLayerReachabilityInformation(8, new byte[] { (byte)0xa8 });
		
		Assert.assertTrue(first.isPrefixOf(second));
		Assert.assertFalse(second.isPrefixOf(first));
	}	

	@Test
	public void testIsPrefixZeroZero() {
		NetworkLayerReachabilityInformation first = new NetworkLayerReachabilityInformation(0, null);
		NetworkLayerReachabilityInformation second = new NetworkLayerReachabilityInformation(0, null);
		
		Assert.assertFalse(first.isPrefixOf(second));
		Assert.assertFalse(second.isPrefixOf(first));
	}	


	@Test
	public void testIsPrefixZeroTwo() {
		NetworkLayerReachabilityInformation first = new NetworkLayerReachabilityInformation(0, null);
		NetworkLayerReachabilityInformation second = new NetworkLayerReachabilityInformation(2, new byte[] { (byte)0xc0 });
		
		Assert.assertTrue(first.isPrefixOf(second));
		Assert.assertFalse(second.isPrefixOf(first));
	}	
	
	@Test
	public void testCompareEqualsFourBits() {
		NetworkLayerReachabilityInformation first = new NetworkLayerReachabilityInformation(4, new byte[] { (byte)0xc0 });
		NetworkLayerReachabilityInformation second = new NetworkLayerReachabilityInformation(4, new byte[] { (byte)0xc0 });
		
		Assert.assertEquals(0, first.compareTo(second));
		
	}
	
	@Test
	public void testCompareEqualsGreaterBits() {
		NetworkLayerReachabilityInformation first = new NetworkLayerReachabilityInformation(4, new byte[] { (byte)0xd0 });
		NetworkLayerReachabilityInformation second = new NetworkLayerReachabilityInformation(4, new byte[] { (byte)0xc0 });
		
		Assert.assertEquals(1, first.compareTo(second));
		
	}
	
	@Test
	public void testCompareEqualsSmallerBits() {
		NetworkLayerReachabilityInformation first = new NetworkLayerReachabilityInformation(4, new byte[] { (byte)0xc0 });
		NetworkLayerReachabilityInformation second = new NetworkLayerReachabilityInformation(4, new byte[] { (byte)0xd0 });
		
		Assert.assertEquals(-1, first.compareTo(second));
		
	}
	
	@Test
	public void testCompareEqualsTwelveBits() {
		NetworkLayerReachabilityInformation first = new NetworkLayerReachabilityInformation(12, new byte[] { (byte)0xc0, (byte)0xf0 });
		NetworkLayerReachabilityInformation second = new NetworkLayerReachabilityInformation(12, new byte[] { (byte)0xc0, (byte)0xf0 });
		
		Assert.assertEquals(0, first.compareTo(second));
		
	}
	
	@Test
	public void testCompareGreaterTwelveBitsFirst() {
		NetworkLayerReachabilityInformation first = new NetworkLayerReachabilityInformation(12, new byte[] { (byte)0xd0, (byte)0xc0 });
		NetworkLayerReachabilityInformation second = new NetworkLayerReachabilityInformation(12, new byte[] { (byte)0xc0, (byte)0xc0 });
		
		Assert.assertEquals(1, first.compareTo(second));
		
	}
	
	@Test
	public void testCompareGreaterTwelveBitsSecond() {
		NetworkLayerReachabilityInformation first = new NetworkLayerReachabilityInformation(12, new byte[] { (byte)0xc0, (byte)0xd0 });
		NetworkLayerReachabilityInformation second = new NetworkLayerReachabilityInformation(12, new byte[] { (byte)0xc0, (byte)0xc0 });
		
		Assert.assertEquals(1, first.compareTo(second));
		
	}
	
	
	@Test
	public void testCompareSmallerTwelveBitsFirst() {
		NetworkLayerReachabilityInformation first = new NetworkLayerReachabilityInformation(12, new byte[] { (byte)0xc0, (byte)0xc0 });
		NetworkLayerReachabilityInformation second = new NetworkLayerReachabilityInformation(12, new byte[] { (byte)0xd0, (byte)0xc0 });
		
		Assert.assertEquals(-1, first.compareTo(second));
		
	}
	
	@Test
	public void testCompareSmallerTwelveBitsSecond() {
		NetworkLayerReachabilityInformation first = new NetworkLayerReachabilityInformation(12, new byte[] { (byte)0xc0, (byte)0x00 });
		NetworkLayerReachabilityInformation second = new NetworkLayerReachabilityInformation(12, new byte[] { (byte)0xc0, (byte)0xd0 });
		
		Assert.assertEquals(-1, first.compareTo(second));
		
	}

	@Test
	public void testCompareEqualsTwentyBits() {
		NetworkLayerReachabilityInformation first = new NetworkLayerReachabilityInformation(20, new byte[] { (byte)0xc0, (byte)0xf0, (byte)0xc0 });
		NetworkLayerReachabilityInformation second = new NetworkLayerReachabilityInformation(20, new byte[] { (byte)0xc0, (byte)0xf0, (byte)0xc0 });
		
		Assert.assertEquals(0, first.compareTo(second));
		
	}

	@Test
	public void testCompareGreaterTwentyBitsFirst() {
		NetworkLayerReachabilityInformation first = new NetworkLayerReachabilityInformation(20, new byte[] { (byte)0xd0, (byte)0xc0, (byte)0xc0 });
		NetworkLayerReachabilityInformation second = new NetworkLayerReachabilityInformation(20, new byte[] { (byte)0xc0, (byte)0xc0, (byte)0xc0 });
		
		Assert.assertEquals(1, first.compareTo(second));
		
	}

	@Test
	public void testCompareGreaterTwentyBitsSecond() {
		NetworkLayerReachabilityInformation first = new NetworkLayerReachabilityInformation(20, new byte[] { (byte)0xc0, (byte)0xd0, (byte)0xc0 });
		NetworkLayerReachabilityInformation second = new NetworkLayerReachabilityInformation(20, new byte[] { (byte)0xc0, (byte)0xc0, (byte)0xc0 });
		
		Assert.assertEquals(1, first.compareTo(second));
		
	}

	@Test
	public void testCompareGreaterTwentyBitsThird() {
		NetworkLayerReachabilityInformation first = new NetworkLayerReachabilityInformation(20, new byte[] { (byte)0xc0, (byte)0xc0, (byte)0xd0 });
		NetworkLayerReachabilityInformation second = new NetworkLayerReachabilityInformation(20, new byte[] { (byte)0xc0, (byte)0xc0, (byte)0xc0 });
		
		Assert.assertEquals(1, first.compareTo(second));
		
	}

	@Test
	public void testCompareSmallerTwentyBitsFirst() {
		NetworkLayerReachabilityInformation first = new NetworkLayerReachabilityInformation(20, new byte[] { (byte)0xc0, (byte)0xc0, (byte)0xc0 });
		NetworkLayerReachabilityInformation second = new NetworkLayerReachabilityInformation(20, new byte[] { (byte)0xd0, (byte)0xc0, (byte)0xc0 });
		
		Assert.assertEquals(-1, first.compareTo(second));
		
	}

	@Test
	public void testCompareSmallerTwentyBitsSecond() {
		NetworkLayerReachabilityInformation first = new NetworkLayerReachabilityInformation(20, new byte[] { (byte)0xc0, (byte)0xc0, (byte)0xc0 });
		NetworkLayerReachabilityInformation second = new NetworkLayerReachabilityInformation(20, new byte[] { (byte)0xc0, (byte)0xd0, (byte)0xc0 });
		
		Assert.assertEquals(-1, first.compareTo(second));
		
	}

	@Test
	public void testCompareSmallerTwentyBitsThird() {
		NetworkLayerReachabilityInformation first = new NetworkLayerReachabilityInformation(20, new byte[] { (byte)0xc0, (byte)0xc0, (byte)0xc0 });
		NetworkLayerReachabilityInformation second = new NetworkLayerReachabilityInformation(20, new byte[] { (byte)0xc0, (byte)0xc0, (byte)0xd0 });
		
		Assert.assertEquals(-1, first.compareTo(second));
	}
	
	@Test
	public void testCompareSmallersFourFiveBits() {
		NetworkLayerReachabilityInformation first = new NetworkLayerReachabilityInformation(4, new byte[] { (byte)0xc0 });
		NetworkLayerReachabilityInformation second = new NetworkLayerReachabilityInformation(5, new byte[] { (byte)0xc0 });
		
		Assert.assertEquals(-1, first.compareTo(second));
		
	}
	
	@Test
	public void testCompareGreaterFiveForBits() {
		NetworkLayerReachabilityInformation first = new NetworkLayerReachabilityInformation(5, new byte[] { (byte)0xc0 });
		NetworkLayerReachabilityInformation second = new NetworkLayerReachabilityInformation(4, new byte[] { (byte)0xc0 });
		
		Assert.assertEquals(1, first.compareTo(second));
		
	}

	@Test
	public void testCompareSmallerTwelveThirteenBits() {
		NetworkLayerReachabilityInformation first = new NetworkLayerReachabilityInformation(12, new byte[] { (byte)0xc0, (byte)0xc0 });
		NetworkLayerReachabilityInformation second = new NetworkLayerReachabilityInformation(13, new byte[] { (byte)0xc0, (byte)0xc0 });
		
		Assert.assertEquals(-1, first.compareTo(second));
		
	}
	
	@Test
	public void testCompareGreaterThirteenTwelveBits() {
		NetworkLayerReachabilityInformation first = new NetworkLayerReachabilityInformation(13, new byte[] { (byte)0xc0, (byte)0xc0 });
		NetworkLayerReachabilityInformation second = new NetworkLayerReachabilityInformation(12, new byte[] { (byte)0xc0, (byte)0xc0 });
		
		Assert.assertEquals(1, first.compareTo(second));
		
	}

	@Test
	public void testCompareSmallerTwentyTwentyoneBits() {
		NetworkLayerReachabilityInformation first = new NetworkLayerReachabilityInformation(20, new byte[] { (byte)0xc0, (byte)0xc0, (byte)0xc0 });
		NetworkLayerReachabilityInformation second = new NetworkLayerReachabilityInformation(21, new byte[] { (byte)0xc0, (byte)0xc0, (byte)0xc0 });
		
		Assert.assertEquals(-1, first.compareTo(second));
	}
	
	@Test
	public void testCompareGreaterTwentyoneTwentyBits() {
		NetworkLayerReachabilityInformation first = new NetworkLayerReachabilityInformation(21, new byte[] { (byte)0xc0, (byte)0xc0, (byte)0xc0 });
		NetworkLayerReachabilityInformation second = new NetworkLayerReachabilityInformation(20, new byte[] { (byte)0xc0, (byte)0xc0, (byte)0xc0 });
		
		Assert.assertEquals(1, first.compareTo(second));
	}
	
	@Test
	public void testZeroNonZero() {
		NetworkLayerReachabilityInformation first = new NetworkLayerReachabilityInformation(0, null);
		NetworkLayerReachabilityInformation second = new NetworkLayerReachabilityInformation(20, new byte[] { (byte)0xc0, (byte)0xc0, (byte)0xc0 });
		
		Assert.assertEquals(-1, first.compareTo(second));
	}
	
	@Test
	public void testNonZeroZero() {
		NetworkLayerReachabilityInformation first = new NetworkLayerReachabilityInformation(20, new byte[] { (byte)0xc0, (byte)0xc0, (byte)0xc0 });
		NetworkLayerReachabilityInformation second = new NetworkLayerReachabilityInformation(0, null);
		
		Assert.assertEquals(1, first.compareTo(second));
	}
	
	@Test
	public void testZeroZero() {
		NetworkLayerReachabilityInformation first = new NetworkLayerReachabilityInformation(0, null);
		NetworkLayerReachabilityInformation second = new NetworkLayerReachabilityInformation(0, null);
		
		Assert.assertEquals(0, first.compareTo(second));
	}
}
