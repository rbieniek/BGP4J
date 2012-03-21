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
 * File: org.bgp4j.rib.InetAddressNextHopTest.java 
 */
package org.bgp4j.rib;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class InetAddressNextHopTest {

	// Inet4 Inet4
	@Test
	public void testEqualsInet4AddressInet4Address() throws UnknownHostException {
		InetAddressNextHop<Inet4Address> a = new InetAddressNextHop<Inet4Address>((Inet4Address)Inet4Address.getByAddress(new byte[] { 
				(byte)0xc0, (byte)0xa8, (byte)0x01, (byte)0x01}));
		InetAddressNextHop<Inet4Address> b = new InetAddressNextHop<Inet4Address>((Inet4Address)Inet4Address.getByAddress(new byte[] { 
				(byte)0xc0, (byte)0xa8, (byte)0x01, (byte)0x01}));
		
		Assert.assertTrue(a.equals(b));
	}

	@Test
	public void testNotEqualsInet4AddressInet4Address() throws UnknownHostException {
		InetAddressNextHop<Inet4Address> a = new InetAddressNextHop<Inet4Address>((Inet4Address)Inet4Address.getByAddress(new byte[] { 
				(byte)0xc0, (byte)0xa8, (byte)0x01, (byte)0x01}));
		InetAddressNextHop<Inet4Address> b = new InetAddressNextHop<Inet4Address>((Inet4Address)Inet4Address.getByAddress(new byte[] { 
				(byte)0xc0, (byte)0xa8, (byte)0x01, (byte)0x02}));
		
		Assert.assertFalse(a.equals(b));
	}

	@Test
	public void testSameHashCodeInet4AddressInet4Address() throws UnknownHostException {
		InetAddressNextHop<Inet4Address> a = new InetAddressNextHop<Inet4Address>((Inet4Address)Inet4Address.getByAddress(new byte[] { 
				(byte)0xc0, (byte)0xa8, (byte)0x01, (byte)0x01}));
		InetAddressNextHop<Inet4Address> b = new InetAddressNextHop<Inet4Address>((Inet4Address)Inet4Address.getByAddress(new byte[] { 
				(byte)0xc0, (byte)0xa8, (byte)0x01, (byte)0x01}));
		
		Assert.assertTrue(a.hashCode() == b.hashCode());
	}


	@Test
	public void testDifferentHashCodeInet4AddressInet4Address() throws UnknownHostException {
		InetAddressNextHop<Inet4Address> a = new InetAddressNextHop<Inet4Address>((Inet4Address)Inet4Address.getByAddress(new byte[] { 
				(byte)0xc0, (byte)0xa8, (byte)0x01, (byte)0x01}));
		InetAddressNextHop<Inet4Address> b = new InetAddressNextHop<Inet4Address>((Inet4Address)Inet4Address.getByAddress(new byte[] { 
				(byte)0xc0, (byte)0xa8, (byte)0x01, (byte)0x02}));
		
		Assert.assertFalse(a.hashCode() == b.hashCode());
	}

	// Inet6 Inet6
	@Test
	public void testEqualsInet6AddressInet6Address() throws UnknownHostException {
		InetAddressNextHop<Inet6Address> a = new InetAddressNextHop<Inet6Address>((Inet6Address)Inet6Address.getByAddress(new byte[] { 
				(byte)0xfe, (byte)0x80, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
				(byte)0x02, (byte)0x22, (byte)0x15, (byte)0xff, (byte)0xfe, (byte)0x85, (byte)0xd9, (byte)0xe1
		}));
		InetAddressNextHop<Inet6Address> b = new InetAddressNextHop<Inet6Address>((Inet6Address)Inet6Address.getByAddress(new byte[] { 
				(byte)0xfe, (byte)0x80, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
				(byte)0x02, (byte)0x22, (byte)0x15, (byte)0xff, (byte)0xfe, (byte)0x85, (byte)0xd9, (byte)0xe1
		}));
		
		Assert.assertTrue(a.equals(b));
	}

	@Test
	public void testNotEqualsInet6AddressInet6Address() throws UnknownHostException {
		InetAddressNextHop<Inet6Address> a = new InetAddressNextHop<Inet6Address>((Inet6Address)Inet6Address.getByAddress(new byte[] { 
				(byte)0xfe, (byte)0x80, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
				(byte)0x02, (byte)0x22, (byte)0x15, (byte)0xff, (byte)0xfe, (byte)0x85, (byte)0xd9, (byte)0xe1
		}));
		InetAddressNextHop<Inet6Address> b = new InetAddressNextHop<Inet6Address>((Inet6Address)Inet6Address.getByAddress(new byte[] { 
				(byte)0xfe, (byte)0x80, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
				(byte)0x02, (byte)0x22, (byte)0x15, (byte)0xff, (byte)0xfe, (byte)0x85, (byte)0xd9, (byte)0xe2
		}));
		
		Assert.assertFalse(a.equals(b));
	}

	@Test
	public void testSameHashCodeInet6AddressInet6Address() throws UnknownHostException {
		InetAddressNextHop<Inet6Address> a = new InetAddressNextHop<Inet6Address>((Inet6Address)Inet6Address.getByAddress(new byte[] { 
				(byte)0xfe, (byte)0x80, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
				(byte)0x02, (byte)0x22, (byte)0x15, (byte)0xff, (byte)0xfe, (byte)0x85, (byte)0xd9, (byte)0xe1
		}));
		InetAddressNextHop<Inet6Address> b = new InetAddressNextHop<Inet6Address>((Inet6Address)Inet6Address.getByAddress(new byte[] { 
				(byte)0xfe, (byte)0x80, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
				(byte)0x02, (byte)0x22, (byte)0x15, (byte)0xff, (byte)0xfe, (byte)0x85, (byte)0xd9, (byte)0xe1
		}));
		
		Assert.assertTrue(a.hashCode() == b.hashCode());
	}

	@Test
	public void testDifferentHashCodeInet6AddressInet6Address() throws UnknownHostException {
		InetAddressNextHop<Inet6Address> a = new InetAddressNextHop<Inet6Address>((Inet6Address)Inet6Address.getByAddress(new byte[] { 
				(byte)0xfe, (byte)0x80, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
				(byte)0x02, (byte)0x22, (byte)0x15, (byte)0xff, (byte)0xfe, (byte)0x85, (byte)0xd9, (byte)0xe1
		}));
		InetAddressNextHop<Inet6Address> b = new InetAddressNextHop<Inet6Address>((Inet6Address)Inet6Address.getByAddress(new byte[] { 
				(byte)0xfe, (byte)0x80, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
				(byte)0x02, (byte)0x22, (byte)0x15, (byte)0xff, (byte)0xfe, (byte)0x85, (byte)0xd9, (byte)0xe2
		}));
		
		Assert.assertFalse(a.hashCode() == b.hashCode());
	}

	// Inet4 Inet
	@Test
	public void testEqualsInet4AddressInetAddress() throws UnknownHostException {
		InetAddressNextHop<Inet4Address> a = new InetAddressNextHop<Inet4Address>((Inet4Address)Inet4Address.getByAddress(new byte[] { 
				(byte)0xc0, (byte)0xa8, (byte)0x01, (byte)0x01}));
		InetAddressNextHop<InetAddress> b = new InetAddressNextHop<InetAddress>(InetAddress.getByAddress(new byte[] { 
				(byte)0xc0, (byte)0xa8, (byte)0x01, (byte)0x01}));
		
		Assert.assertTrue(a.equals(b));
	}

	@Test
	public void testNotEqualsInet4AddressInetAddress() throws UnknownHostException {
		InetAddressNextHop<Inet4Address> a = new InetAddressNextHop<Inet4Address>((Inet4Address)Inet4Address.getByAddress(new byte[] { 
				(byte)0xc0, (byte)0xa8, (byte)0x01, (byte)0x01}));
		InetAddressNextHop<InetAddress> b = new InetAddressNextHop<InetAddress>(InetAddress.getByAddress(new byte[] { 
				(byte)0xc0, (byte)0xa8, (byte)0x01, (byte)0x02}));
		
		Assert.assertFalse(a.equals(b));
	}

	@Test
	public void testSameHashCodeInet4AddressInetAddress() throws UnknownHostException {
		InetAddressNextHop<Inet4Address> a = new InetAddressNextHop<Inet4Address>((Inet4Address)Inet4Address.getByAddress(new byte[] { 
				(byte)0xc0, (byte)0xa8, (byte)0x01, (byte)0x01}));
		InetAddressNextHop<InetAddress> b = new InetAddressNextHop<InetAddress>(InetAddress.getByAddress(new byte[] { 
				(byte)0xc0, (byte)0xa8, (byte)0x01, (byte)0x01}));
		
		Assert.assertTrue(a.hashCode() == b.hashCode());
	}


	@Test
	public void testDifferentHashCodeInet4AddressInetAddress() throws UnknownHostException {
		InetAddressNextHop<Inet4Address> a = new InetAddressNextHop<Inet4Address>((Inet4Address)Inet4Address.getByAddress(new byte[] { 
				(byte)0xc0, (byte)0xa8, (byte)0x01, (byte)0x01}));
		InetAddressNextHop<InetAddress> b = new InetAddressNextHop<InetAddress>(InetAddress.getByAddress(new byte[] { 
				(byte)0xc0, (byte)0xa8, (byte)0x01, (byte)0x02}));
		
		Assert.assertFalse(a.hashCode() == b.hashCode());
	}

	// Inet6 Inet
	@Test
	public void testEqualsInet6AddressInetAddress() throws UnknownHostException {
		InetAddressNextHop<Inet6Address> a = new InetAddressNextHop<Inet6Address>((Inet6Address)Inet6Address.getByAddress(new byte[] { 
				(byte)0xfe, (byte)0x80, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
				(byte)0x02, (byte)0x22, (byte)0x15, (byte)0xff, (byte)0xfe, (byte)0x85, (byte)0xd9, (byte)0xe1
		}));
		InetAddressNextHop<InetAddress> b = new InetAddressNextHop<InetAddress>(InetAddress.getByAddress(new byte[] { 
				(byte)0xfe, (byte)0x80, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
				(byte)0x02, (byte)0x22, (byte)0x15, (byte)0xff, (byte)0xfe, (byte)0x85, (byte)0xd9, (byte)0xe1
		}));
		
		Assert.assertTrue(a.equals(b));
	}

	@Test
	public void testNotEqualsInet6AddressInetAddress() throws UnknownHostException {
		InetAddressNextHop<Inet6Address> a = new InetAddressNextHop<Inet6Address>((Inet6Address)Inet6Address.getByAddress(new byte[] { 
				(byte)0xfe, (byte)0x80, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
				(byte)0x02, (byte)0x22, (byte)0x15, (byte)0xff, (byte)0xfe, (byte)0x85, (byte)0xd9, (byte)0xe1
		}));
		InetAddressNextHop<InetAddress> b = new InetAddressNextHop<InetAddress>(InetAddress.getByAddress(new byte[] { 
				(byte)0xfe, (byte)0x80, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
				(byte)0x02, (byte)0x22, (byte)0x15, (byte)0xff, (byte)0xfe, (byte)0x85, (byte)0xd9, (byte)0xe2
		}));
		
		Assert.assertFalse(a.equals(b));
	}

	@Test
	public void testSameHashCodeInet6AddressInetAddress() throws UnknownHostException {
		InetAddressNextHop<Inet6Address> a = new InetAddressNextHop<Inet6Address>((Inet6Address)Inet6Address.getByAddress(new byte[] { 
				(byte)0xfe, (byte)0x80, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
				(byte)0x02, (byte)0x22, (byte)0x15, (byte)0xff, (byte)0xfe, (byte)0x85, (byte)0xd9, (byte)0xe1
		}));
		InetAddressNextHop<InetAddress> b = new InetAddressNextHop<InetAddress>(InetAddress.getByAddress(new byte[] { 
				(byte)0xfe, (byte)0x80, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
				(byte)0x02, (byte)0x22, (byte)0x15, (byte)0xff, (byte)0xfe, (byte)0x85, (byte)0xd9, (byte)0xe1
		}));
		
		Assert.assertTrue(a.hashCode() == b.hashCode());
	}

	@Test
	public void testDifferentHashCodeInet6AddressInetAddress() throws UnknownHostException {
		InetAddressNextHop<Inet6Address> a = new InetAddressNextHop<Inet6Address>((Inet6Address)Inet6Address.getByAddress(new byte[] { 
				(byte)0xfe, (byte)0x80, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
				(byte)0x02, (byte)0x22, (byte)0x15, (byte)0xff, (byte)0xfe, (byte)0x85, (byte)0xd9, (byte)0xe1
		}));
		InetAddressNextHop<InetAddress> b = new InetAddressNextHop<InetAddress>(InetAddress.getByAddress(new byte[] { 
				(byte)0xfe, (byte)0x80, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
				(byte)0x02, (byte)0x22, (byte)0x15, (byte)0xff, (byte)0xfe, (byte)0x85, (byte)0xd9, (byte)0xe2
		}));
		
		Assert.assertFalse(a.hashCode() == b.hashCode());
	}
}
