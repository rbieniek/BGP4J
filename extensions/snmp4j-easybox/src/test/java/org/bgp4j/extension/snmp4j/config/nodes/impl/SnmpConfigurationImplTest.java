/**
 * 
 */
package org.bgp4j.extension.snmp4j.config.nodes.impl;

import java.net.InetAddress;

import junit.framework.Assert;

import org.bgp4j.extension.snmp4j.config.nodes.SnmpConfiguration;
import org.junit.Test;

/**
 * @author rainer
 *
 */
public class SnmpConfigurationImplTest {

	@Test
	public void testEquals() throws Exception {
		SnmpConfiguration s1 = new SnmpConfigurationImpl(InetAddress.getByAddress(new byte[] { (byte)0xc0, (byte)0xa8, 0x01, 0x01 }), "dummy");
		SnmpConfiguration s2 = new SnmpConfigurationImpl(InetAddress.getByAddress(new byte[] { (byte)0xc0, (byte)0xa8, 0x01, 0x01 }), "dummy");
		SnmpConfiguration s3 = new SnmpConfigurationImpl(InetAddress.getByAddress(new byte[] { (byte)0xc0, (byte)0xa8, 0x01, 0x02 }), "dummy");
		SnmpConfiguration s4 = new SnmpConfigurationImpl(InetAddress.getByAddress(new byte[] { (byte)0xc0, (byte)0xa8, 0x01, 0x01 }), "foo");
		
		Assert.assertTrue(s1.equals(s2));
		Assert.assertFalse(s1.equals(s3));
		Assert.assertFalse(s1.equals(s4));
	}

	@Test
	public void testHashcode() throws Exception {
		SnmpConfiguration s1 = new SnmpConfigurationImpl(InetAddress.getByAddress(new byte[] { (byte)0xc0, (byte)0xa8, 0x01, 0x01 }), "dummy");
		SnmpConfiguration s2 = new SnmpConfigurationImpl(InetAddress.getByAddress(new byte[] { (byte)0xc0, (byte)0xa8, 0x01, 0x01 }), "dummy");
		SnmpConfiguration s3 = new SnmpConfigurationImpl(InetAddress.getByAddress(new byte[] { (byte)0xc0, (byte)0xa8, 0x01, 0x02 }), "dummy");
		SnmpConfiguration s4 = new SnmpConfigurationImpl(InetAddress.getByAddress(new byte[] { (byte)0xc0, (byte)0xa8, 0x01, 0x01 }), "foo");
		
		Assert.assertTrue(s1.hashCode() == s2.hashCode());
		Assert.assertFalse(s1.hashCode() == s3.hashCode());
		Assert.assertFalse(s1.hashCode() == s4.hashCode());
	}
}
