/**
 * 
 */
package org.bgp4j.config.nodes.impl;

import java.util.Iterator;

import junit.framework.Assert;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.bgp4j.config.ConfigTestBase;
import org.bgp4j.config.nodes.AddressFamilyRoutingConfiguration;
import org.bgp4j.config.nodes.RouteConfiguration;
import org.bgp4j.config.nodes.impl.AddressFamilyRoutingConfigurationParser;
import org.bgp4j.net.AddressFamily;
import org.bgp4j.net.NetworkLayerReachabilityInformation;
import org.bgp4j.net.SubsequentAddressFamily;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author rainer
 *
 */
public class AddressFamilyRoutingConfigurationParserTest extends ConfigTestBase{
	@Before
	public void before() throws Exception {
		this.config = loadConfiguration("config/nodes/AddressFamilyRoutingConfig.xml");
		this.parser = obtainInstance(AddressFamilyRoutingConfigurationParser.class);
	}
	
	@After
	public void after() {
		this.config = null;
		this.parser = null;
	}
	
	private XMLConfiguration config;
	private AddressFamilyRoutingConfigurationParser parser;

	@Test
	public void testParseCompleteWithOneRoute() throws Exception {
		AddressFamilyRoutingConfiguration result = parser.parseConfiguration(config.configurationAt("AddressFamily(0)"));
		
		Assert.assertEquals(AddressFamily.IPv4, result.getKey().getAddressFamily());
		Assert.assertEquals(SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, result.getKey().getSubsequentAddressFamily());
		
		Iterator<RouteConfiguration> it = result.getRoutes().iterator();
		
		Assert.assertTrue(it.hasNext());
		
		RouteConfiguration route = it.next();

		Assert.assertEquals(new NetworkLayerReachabilityInformation(24, new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x01 }), route.getNlri());
		
		Assert.assertFalse(it.hasNext());
		
	}

	@Test
	public void testParseCompleteWithTwoRoute() throws Exception {
		AddressFamilyRoutingConfiguration result = parser.parseConfiguration(config.configurationAt("AddressFamily(1)"));
		
		Assert.assertEquals(AddressFamily.IPv4, result.getKey().getAddressFamily());
		Assert.assertEquals(SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, result.getKey().getSubsequentAddressFamily());
		
		Iterator<RouteConfiguration> it = result.getRoutes().iterator();
		
		Assert.assertTrue(it.hasNext());
		
		RouteConfiguration route = it.next();

		Assert.assertEquals(new NetworkLayerReachabilityInformation(24, new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x01 }), route.getNlri());
		
		Assert.assertTrue(it.hasNext());
		route = it.next();

		Assert.assertEquals(new NetworkLayerReachabilityInformation(24, new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x02 }), route.getNlri());

		Assert.assertFalse(it.hasNext());
		
	}
	
	@Test(expected=ConfigurationException.class)
	public void testMissingAddressFamily() throws Exception {
		parser.parseConfiguration(config.configurationAt("AddressFamily(2)"));
	}
	
	@Test(expected=ConfigurationException.class)
	public void testMissingSubsequentAddressFamily() throws Exception {
		parser.parseConfiguration(config.configurationAt("AddressFamily(3)"));
	}
}
