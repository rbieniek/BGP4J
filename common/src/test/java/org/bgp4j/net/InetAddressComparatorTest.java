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
 * File: org.bgp4j.net.attributes.InetAddressComparatorTest.java 
 */
package org.bgp4j.net;

import java.net.InetAddress;

import org.bgp4j.net.InetAddressComparator;
import org.junit.Test;

import junit.framework.Assert;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class InetAddressComparatorTest {

	@Test
	public void testEqualsIPv4IPv4() throws Exception {
		InetAddress a = InetAddress.getByAddress(new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x01, (byte)0x01 });
		InetAddress b = InetAddress.getByAddress(new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x01, (byte)0x01 });
		
		Assert.assertEquals(0, (new InetAddressComparator()).compare(a, b));
	}

	@Test
	public void testSmallerIPv4IPv4() throws Exception {
		InetAddress a = InetAddress.getByAddress(new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x01, (byte)0x01 });
		InetAddress b = InetAddress.getByAddress(new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x01, (byte)0x02 });
		
		Assert.assertTrue((new InetAddressComparator()).compare(a, b)  < 0);
	}

	@Test
	public void testLargerIPv4IPv4() throws Exception {
		InetAddress a = InetAddress.getByAddress(new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x01, (byte)0x02 });
		InetAddress b = InetAddress.getByAddress(new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x01, (byte)0x01 });
		
		Assert.assertTrue((new InetAddressComparator()).compare(a, b)  > 0);
	}

	// fe80::222:15ff:fe85:d9e
	@Test
	public void testEqualsIPv6IPv6() throws Exception {
		InetAddress a = InetAddress.getByAddress(new byte[] { 
				(byte)0xfe, (byte)0x80, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
				(byte)0x02, (byte)0x22, (byte)0x15, (byte)0xff, (byte)0xfe, (byte)0x85, (byte)0x0d, (byte)0x9e });
		InetAddress b = InetAddress.getByAddress(new byte[] { 
				(byte)0xfe, (byte)0x80, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
				(byte)0x02, (byte)0x22, (byte)0x15, (byte)0xff, (byte)0xfe, (byte)0x85, (byte)0x0d, (byte)0x9e });
		
		Assert.assertEquals(0, (new InetAddressComparator()).compare(a, b));
	}
	
	@Test
	public void testSmallerIPv6IPv6() throws Exception {
		InetAddress a = InetAddress.getByAddress(new byte[] { 
				(byte)0xfe, (byte)0x80, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
				(byte)0x02, (byte)0x22, (byte)0x15, (byte)0xff, (byte)0xfe, (byte)0x85, (byte)0x0d, (byte)0x9e });
		InetAddress b = InetAddress.getByAddress(new byte[] { 
				(byte)0xfe, (byte)0x80, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
				(byte)0x02, (byte)0x22, (byte)0x15, (byte)0xff, (byte)0xfe, (byte)0x85, (byte)0x0d, (byte)0x9f });
		
		Assert.assertTrue((new InetAddressComparator()).compare(a, b)  < 0);
	}

	@Test
	public void testLargerIPv6IPv6() throws Exception {
		InetAddress a = InetAddress.getByAddress(new byte[] { 
				(byte)0xfe, (byte)0x80, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
				(byte)0x02, (byte)0x22, (byte)0x15, (byte)0xff, (byte)0xfe, (byte)0x85, (byte)0x0d, (byte)0x9f });
		InetAddress b = InetAddress.getByAddress(new byte[] { 
				(byte)0xfe, (byte)0x80, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
				(byte)0x02, (byte)0x22, (byte)0x15, (byte)0xff, (byte)0xfe, (byte)0x85, (byte)0x0d, (byte)0x9e });
		
		Assert.assertTrue((new InetAddressComparator()).compare(a, b)  > 0);
	}

	@Test
	public void testSmallerIPv4IPv6() throws Exception {
		InetAddress a = InetAddress.getByAddress(new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x01, (byte)0x01 });
		InetAddress b = InetAddress.getByAddress(new byte[] { 
				(byte)0xfe, (byte)0x80, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
				(byte)0x02, (byte)0x22, (byte)0x15, (byte)0xff, (byte)0xfe, (byte)0x85, (byte)0x0d, (byte)0x9f });
		
		Assert.assertTrue((new InetAddressComparator()).compare(a, b)  < 0);
	}

	@Test
	public void testLargerIPv6IPv4() throws Exception {
		InetAddress a = InetAddress.getByAddress(new byte[] { 
				(byte)0xfe, (byte)0x80, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
				(byte)0x02, (byte)0x22, (byte)0x15, (byte)0xff, (byte)0xfe, (byte)0x85, (byte)0x0d, (byte)0x9f });
		InetAddress b = InetAddress.getByAddress(new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x01, (byte)0x01 });
		
		Assert.assertTrue((new InetAddressComparator()).compare(a, b)  > 0);
	}
}
