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
package org.bgp4j.net;

import junit.framework.Assert;

import org.junit.Test;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class NetworkLayerReachabilityInformationTest  {

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
