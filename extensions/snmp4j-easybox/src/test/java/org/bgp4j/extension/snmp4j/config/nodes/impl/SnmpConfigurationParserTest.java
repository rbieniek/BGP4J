/**
 * 
 */
package org.bgp4j.extension.snmp4j.config.nodes.impl;

import java.net.InetAddress;

import junit.framework.Assert;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.bgp4.config.ConfigTestBase;
import org.bgp4j.extension.snmp4j.config.nodes.SnmpConfiguration;
import org.bgp4j.extension.snmp4j.config.nodes.impl.SnmpConfigurationParser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author rainer
 *
 */
public class SnmpConfigurationParserTest extends ConfigTestBase {

	@Before
	public void before() throws Exception {
		this.parser = obtainInstance(SnmpConfigurationParser.class);
		this.config = loadConfiguration("config/nodes/SnmpConfig.xml");
	}
	
	@After
	public void after() {
		this.parser = null;
		this.config = null;
	}
	
	private XMLConfiguration config;
	private SnmpConfigurationParser parser;
	
	@Test
	public void testCorrectConfiguration() throws Exception {
		SnmpConfiguration snmp = parser.parseConfiguration(config.configurationAt("SnmpConfiguration(0)"));
		
		Assert.assertEquals(InetAddress.getByAddress(new byte[] { (byte)0xc0, (byte)0xa8, 0x01, 0x01 }), snmp.getAddress());
		Assert.assertEquals("public", snmp.getCommunity());
	}
	
	@Test(expected=ConfigurationException.class)
	public void testMissingAddress() throws Exception {
		parser.parseConfiguration(config.configurationAt("SnmpConfiguration(1)"));
	}
	
	@Test(expected=ConfigurationException.class)
	public void testMissingCommunity() throws Exception {
		parser.parseConfiguration(config.configurationAt("SnmpConfiguration(2)"));
	}
	
	@Test(expected=ConfigurationException.class)
	public void testBlankCommunity() throws Exception {
		parser.parseConfiguration(config.configurationAt("SnmpConfiguration(3)"));
	}
}
