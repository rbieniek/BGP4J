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
 * File: org.bgp4j.net.AddressFamilyKeyTest.java 
 */
package org.bgp4j.net;

import org.junit.Test;

import junit.framework.Assert;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class AddressFamilyKeyTest {

	@Test
	public void testEquals() {
		AddressFamilyKey k1 = new AddressFamilyKey(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING);
		AddressFamilyKey k2 = new AddressFamilyKey(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING);
		
		Assert.assertTrue(k1.equals(k2));
		Assert.assertTrue(k1.hashCode() == k2.hashCode());
		Assert.assertTrue(k1.compareTo(k2) == 0);
	}

	@Test
	public void testDifferentAddressFamily() {
		AddressFamilyKey k1 = new AddressFamilyKey(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING);
		AddressFamilyKey k2 = new AddressFamilyKey(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING);
		
		Assert.assertFalse(k1.equals(k2));
		Assert.assertFalse(k1.hashCode() == k2.hashCode());
	}

	@Test
	public void testDifferentSubsequentAddressFamily() {
		AddressFamilyKey k1 = new AddressFamilyKey(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING);
		AddressFamilyKey k2 = new AddressFamilyKey(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_MULTICAST_FORWARDING);
		
		Assert.assertFalse(k1.equals(k2));
		Assert.assertFalse(k1.hashCode() == k2.hashCode());
	}

	@Test
	public void testSmallerAddressFamily() {
		AddressFamilyKey a = new AddressFamilyKey(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING);
		AddressFamilyKey b = new AddressFamilyKey(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING);

		Assert.assertFalse(a.equals(b));
		Assert.assertFalse(a.hashCode() == b.hashCode());
		Assert.assertTrue(a.compareTo(b) < 0);
	}

	@Test
	public void testLargerAddressFamily() {
		AddressFamilyKey a = new AddressFamilyKey(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING);
		AddressFamilyKey b = new AddressFamilyKey(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING);

		Assert.assertFalse(a.equals(b));
		Assert.assertFalse(a.hashCode() == b.hashCode());
		Assert.assertTrue(a.compareTo(b) > 0);
	}

	@Test
	public void testSmallerSubsequentAddressFamily() {
		AddressFamilyKey a = new AddressFamilyKey(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING);
		AddressFamilyKey b = new AddressFamilyKey(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_MULTICAST_FORWARDING);

		Assert.assertFalse(a.equals(b));
		Assert.assertFalse(a.hashCode() == b.hashCode());
		Assert.assertTrue(a.compareTo(b) < 0);
	}

	@Test
	public void testLargerSubsequentAddressFamily() {
		AddressFamilyKey a = new AddressFamilyKey(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_MULTICAST_FORWARDING);
		AddressFamilyKey b = new AddressFamilyKey(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING);

		Assert.assertFalse(a.equals(b));
		Assert.assertFalse(a.hashCode() == b.hashCode());
		Assert.assertTrue(a.compareTo(b) > 0);
	}
}
