/**
 * 
 */
package org.bgp4j.extension.snmp4j.config.nodes.impl;

import java.net.InetAddress;

import junit.framework.Assert;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.bgp4j.config.ConfigTestBase;
import org.bgp4j.config.nodes.RoutingConfiguration;
import org.bgp4j.extension.snmp4j.config.nodes.EasyboxConfiguration;
import org.bgp4j.extension.snmp4j.config.nodes.SnmpConfiguration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author rainer
 *
 */
public class EasyBoxConfigurationParserTest extends ConfigTestBase {

	@Before
	public void before() throws Exception {
		this.parser = obtainInstance(EasyBoxConfigurationParser.class);
		this.config = loadConfiguration("config/nodes/EasyBoxConfig.xml");
	}
	
	@After
	public void after() {
		this.parser = null;
		this.config = null;
	}
	
	private XMLConfiguration config;
	private EasyBoxConfigurationParser parser;
	
	@Test
	public void testCorrectConfiguration() throws Exception {
		EasyboxConfiguration eb = parser.parseConfguration(config.configurationAt("EasyBox(0)"));

		Assert.assertEquals("test1", eb.getName());
		Assert.assertEquals("00:00", eb.getInterfaceMacAddress());
		
		SnmpConfiguration snmp = eb.getSnmpConfiguration();
		
		Assert.assertNotNull(snmp);
		Assert.assertEquals("public", snmp.getCommunity());
		Assert.assertEquals(InetAddress.getByAddress(new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x01, (byte)0x01 }), snmp.getTargetAddress());
		
		RoutingConfiguration routing = eb.getRoutingConfiguration();
		
		Assert.assertNotNull(routing);
		Assert.assertEquals(1, routing.getRoutingConfigurations().size());
	}
	
	@Test(expected=ConfigurationException.class)
	public void testMissingName() throws Exception {
		parser.parseConfguration(config.configurationAt("EasyBox(1)"));
	}
	
	@Test(expected=ConfigurationException.class)
	public void testBlankName() throws Exception {
		parser.parseConfguration(config.configurationAt("EasyBox(2)"));
	}
	
	@Test(expected=ConfigurationException.class)
	public void testMissingInterfaceMacAddress() throws Exception {
		parser.parseConfguration(config.configurationAt("EasyBox(3)"));
	}
	
	@Test(expected=ConfigurationException.class)
	public void testBlankInterfaceMacAddress() throws Exception {
		parser.parseConfguration(config.configurationAt("EasyBox(4)"));
	}
	
	@Test(expected=ConfigurationException.class)
	public void testMissingSnmp() throws Exception {
		parser.parseConfguration(config.configurationAt("EasyBox(5)"));
	}
	
	@Test(expected=ConfigurationException.class)
	public void testMissingRouting() throws Exception {
		parser.parseConfguration(config.configurationAt("EasyBox(6)"));
	}
}
