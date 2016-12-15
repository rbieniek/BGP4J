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
 * File: org.bgp4j.net.AddressFamilyTest.java 
 */
package org.bgp4j.net;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Test class for AddressFamily conversions
 * 
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class AddressFamilyTest {

	@Test(expected=IllegalArgumentException.class)
	public void testUnknownAddressFamily() {
		AddressFamily.fromString("foo");
	}

	@Test(expected=IllegalArgumentException.class)
	public void testNullAddressFamily() {
		AddressFamily.fromString(null);
	}
	
	@Test
	public void testParseWellKnownAddressFamilies() {
		Assert.assertEquals(AddressFamily.fromString("ipv4"), AddressFamily.IPv4);
		Assert.assertEquals(AddressFamily.fromString("ipv6"), AddressFamily.IPv6);
		Assert.assertEquals(AddressFamily.fromString("nsap"), AddressFamily.NSAP);
		Assert.assertEquals(AddressFamily.fromString("hdlc"), AddressFamily.HDLC);
		Assert.assertEquals(AddressFamily.fromString("bbn1882"), AddressFamily.BBN1882);
		Assert.assertEquals(AddressFamily.fromString("ieee802"), AddressFamily.IEEE802);
		Assert.assertEquals(AddressFamily.fromString("e163"), AddressFamily.E163);
		Assert.assertEquals(AddressFamily.fromString("e164"), AddressFamily.E164);
		Assert.assertEquals(AddressFamily.fromString("f69"), AddressFamily.F69);
		Assert.assertEquals(AddressFamily.fromString("x121"), AddressFamily.X121);
		Assert.assertEquals(AddressFamily.fromString("ipx"), AddressFamily.IPX);
		Assert.assertEquals(AddressFamily.fromString("appletalk"), AddressFamily.APPLETALK);
		Assert.assertEquals(AddressFamily.fromString("decnet4"), AddressFamily.DECNET4);
		Assert.assertEquals(AddressFamily.fromString("banyan"), AddressFamily.BANYAN);
	}

	@Test
	public void testWellKnownAddressFamiliesFromCode() {
		Assert.assertEquals(AddressFamily.fromCode(1), AddressFamily.IPv4);
		Assert.assertEquals(AddressFamily.fromCode(2), AddressFamily.IPv6);
		Assert.assertEquals(AddressFamily.fromCode(3), AddressFamily.NSAP);
		Assert.assertEquals(AddressFamily.fromCode(4), AddressFamily.HDLC);
		Assert.assertEquals(AddressFamily.fromCode(5), AddressFamily.BBN1882);
		Assert.assertEquals(AddressFamily.fromCode(6), AddressFamily.IEEE802);
		Assert.assertEquals(AddressFamily.fromCode(7), AddressFamily.E163);
		Assert.assertEquals(AddressFamily.fromCode(8), AddressFamily.E164);
		Assert.assertEquals(AddressFamily.fromCode(9), AddressFamily.F69);
		Assert.assertEquals(AddressFamily.fromCode(10), AddressFamily.X121);
		Assert.assertEquals(AddressFamily.fromCode(11), AddressFamily.IPX);
		Assert.assertEquals(AddressFamily.fromCode(12), AddressFamily.APPLETALK);
		Assert.assertEquals(AddressFamily.fromCode(13), AddressFamily.DECNET4);
		Assert.assertEquals(AddressFamily.fromCode(14), AddressFamily.BANYAN);
	}

	@Test
	public void testWellKnownAddressFamiliesToCode() {
		Assert.assertEquals(1, AddressFamily.IPv4.toCode());
		Assert.assertEquals(2, AddressFamily.IPv6.toCode());
		Assert.assertEquals(3, AddressFamily.NSAP.toCode());
		Assert.assertEquals(4, AddressFamily.HDLC.toCode());
		Assert.assertEquals(5, AddressFamily.BBN1882.toCode());
		Assert.assertEquals(6, AddressFamily.IEEE802.toCode());
		Assert.assertEquals(7, AddressFamily.E163.toCode());
		Assert.assertEquals(8, AddressFamily.E164.toCode());
		Assert.assertEquals(9, AddressFamily.F69.toCode());
		Assert.assertEquals(10, AddressFamily.X121.toCode());
		Assert.assertEquals(11, AddressFamily.IPX.toCode());
		Assert.assertEquals(12, AddressFamily.APPLETALK.toCode());
		Assert.assertEquals(13, AddressFamily.DECNET4.toCode());
		Assert.assertEquals(14, AddressFamily.BANYAN.toCode());
	}
}
