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
 * File: org.bgp4j.net.SubsequentAddressFamilyTest.java 
 */
package org.bgp4j.net;

import junit.framework.Assert;

import org.junit.Test;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class SubsequentAddressFamilyTest {

	@Test
	public void testSubsequentAddressFamilyFromCode() {
		Assert.assertEquals(SubsequentAddressFamily.fromCode(1), SubsequentAddressFamily.NLRI_UNICAST_FORWARDING);
		Assert.assertEquals(SubsequentAddressFamily.fromCode(2), SubsequentAddressFamily.NLRI_MULTICAST_FORWARDING);
		Assert.assertEquals(SubsequentAddressFamily.fromCode(3), SubsequentAddressFamily.NLRI_UNICAST_MULTICAST_FORWARDING);
	}

	@Test
	public void testSubsequentAddressFamilyToCode() {
		Assert.assertEquals(1, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING.toCode());
		Assert.assertEquals(2, SubsequentAddressFamily.NLRI_MULTICAST_FORWARDING.toCode());
		Assert.assertEquals(3, SubsequentAddressFamily.NLRI_UNICAST_MULTICAST_FORWARDING.toCode());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testUnknownSubsequentAddressFamily() {
		SubsequentAddressFamily.fromString("foo");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNullSubsequentAddressFamily() {
		SubsequentAddressFamily.fromString(null);
	}
	
	@Test
	public void testWellKnownSubsequentAddressFamilies() {
		Assert.assertEquals(SubsequentAddressFamily.fromString("unicast"), SubsequentAddressFamily.NLRI_UNICAST_FORWARDING);
		Assert.assertEquals(SubsequentAddressFamily.fromString("multicast"), SubsequentAddressFamily.NLRI_MULTICAST_FORWARDING);
		Assert.assertEquals(SubsequentAddressFamily.fromString("unicast_multicast"), SubsequentAddressFamily.NLRI_UNICAST_MULTICAST_FORWARDING);
	}
}
