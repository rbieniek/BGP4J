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
 * File: org.bgp4j.net.attributes.AggregratorPathAttributeTest.java 
 */
package org.bgp4j.net.attributes;

import java.net.Inet4Address;

import org.bgp4j.net.ASType;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class AggregratorPathAttributeTest {

	@Test
	public void testAllEquals() throws Exception {
		AggregatorPathAttribute a = new AggregatorPathAttribute(ASType.AS_NUMBER_2OCTETS, 2345, 
				(Inet4Address)Inet4Address.getByAddress(new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x01, (byte)0x01 }));
		AggregatorPathAttribute b = new AggregatorPathAttribute(ASType.AS_NUMBER_2OCTETS, 2345, 
				(Inet4Address)Inet4Address.getByAddress(new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x01, (byte)0x01 }));
		
		Assert.assertTrue(a.equals(b));
		Assert.assertTrue(a.hashCode() == b.hashCode());
		Assert.assertTrue(a.compareTo(b) == 0);
	}

	@Test
	public void testASTypeSmaller() throws Exception {
		AggregatorPathAttribute a = new AggregatorPathAttribute(ASType.AS_NUMBER_2OCTETS, 2345, 
				(Inet4Address)Inet4Address.getByAddress(new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x01, (byte)0x01 }));
		AggregatorPathAttribute b = new AggregatorPathAttribute(ASType.AS_NUMBER_4OCTETS, 2345, 
				(Inet4Address)Inet4Address.getByAddress(new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x01, (byte)0x01 }));
		
		Assert.assertFalse(a.equals(b));
		Assert.assertFalse(a.hashCode() == b.hashCode());
		Assert.assertTrue(a.compareTo(b) < 0);
	}

	@Test
	public void testASTypeLarger() throws Exception {
		AggregatorPathAttribute a = new AggregatorPathAttribute(ASType.AS_NUMBER_4OCTETS, 2345, 
				(Inet4Address)Inet4Address.getByAddress(new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x01, (byte)0x01 }));
		AggregatorPathAttribute b = new AggregatorPathAttribute(ASType.AS_NUMBER_2OCTETS, 2345, 
				(Inet4Address)Inet4Address.getByAddress(new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x01, (byte)0x01 }));
		
		Assert.assertFalse(a.equals(b));
		Assert.assertFalse(a.hashCode() == b.hashCode());
		Assert.assertTrue(a.compareTo(b) > 0);
	}
	
	@Test
	public void testASNumberSmaller() throws Exception {
		AggregatorPathAttribute a = new AggregatorPathAttribute(ASType.AS_NUMBER_2OCTETS, 1234, 
				(Inet4Address)Inet4Address.getByAddress(new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x01, (byte)0x01 }));
		AggregatorPathAttribute b = new AggregatorPathAttribute(ASType.AS_NUMBER_2OCTETS, 2345, 
				(Inet4Address)Inet4Address.getByAddress(new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x01, (byte)0x01 }));
		
		Assert.assertFalse(a.equals(b));
		Assert.assertFalse(a.hashCode() == b.hashCode());
		Assert.assertTrue(a.compareTo(b) < 0);
	}
	
	@Test
	public void testASNumberLarger() throws Exception {
		AggregatorPathAttribute a = new AggregatorPathAttribute(ASType.AS_NUMBER_2OCTETS, 2345, 
				(Inet4Address)Inet4Address.getByAddress(new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x01, (byte)0x01 }));
		AggregatorPathAttribute b = new AggregatorPathAttribute(ASType.AS_NUMBER_2OCTETS, 1234, 
				(Inet4Address)Inet4Address.getByAddress(new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x01, (byte)0x01 }));
		
		Assert.assertFalse(a.equals(b));
		Assert.assertFalse(a.hashCode() == b.hashCode());
		Assert.assertTrue(a.compareTo(b) > 0);
	}

	@Test
	public void testASggregatorSmaller() throws Exception {
		AggregatorPathAttribute a = new AggregatorPathAttribute(ASType.AS_NUMBER_2OCTETS, 2345, 
				(Inet4Address)Inet4Address.getByAddress(new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x01, (byte)0x01 }));
		AggregatorPathAttribute b = new AggregatorPathAttribute(ASType.AS_NUMBER_2OCTETS, 2345, 
				(Inet4Address)Inet4Address.getByAddress(new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x01, (byte)0x02 }));
		
		Assert.assertFalse(a.equals(b));
		Assert.assertFalse(a.hashCode() == b.hashCode());
		Assert.assertTrue(a.compareTo(b) < 0);
	}

	@Test
	public void testASggregatorLarger() throws Exception {
		AggregatorPathAttribute a = new AggregatorPathAttribute(ASType.AS_NUMBER_2OCTETS, 2345, 
				(Inet4Address)Inet4Address.getByAddress(new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x01, (byte)0x02 }));
		AggregatorPathAttribute b = new AggregatorPathAttribute(ASType.AS_NUMBER_2OCTETS, 2345, 
				(Inet4Address)Inet4Address.getByAddress(new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x01, (byte)0x01 }));
		
		Assert.assertFalse(a.equals(b));
		Assert.assertFalse(a.hashCode() == b.hashCode());
		Assert.assertTrue(a.compareTo(b) > 0);
	}
}
