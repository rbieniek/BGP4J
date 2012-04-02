/**
 * 
 */
package org.bgp4.config.nodes.impl;

import junit.framework.Assert;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.bgp4.config.ConfigTestBase;
import org.bgp4.config.nodes.RouteConfiguration;
import org.bgp4j.net.NetworkLayerReachabilityInformation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author rainer
 *
 */
public class RouteConfigurationParserTest extends ConfigTestBase {

	@Before
	public void before() throws Exception {
		this.config = loadConfiguration("config/nodes/RouteConfig.xml");
		this.parser = obtainInstance(RouteConfigurationParser.class);
	}
	
	@After
	public void after() {
		this.config = null;
		this.parser = null;
	}
	
	private XMLConfiguration config;
	private RouteConfigurationParser parser;

	@Test
	public void testParseIpV4NoDefault() throws Exception {
		RouteConfiguration route = parser.parseConfiguration(config.configurationAt("Route(0)"));
		
		Assert.assertNotNull(route.getNlri());
		
		Assert.assertEquals(new NetworkLayerReachabilityInformation(24, new byte[] {
			(byte)0xc0, (byte)0xa8, (byte)0x01	
		}), route.getNlri());
		
		Assert.assertNotNull(route.getPathAttributes());
		
		Assert.assertEquals(1, route.getPathAttributes().getAttributes().size());
	}
	
	@Test
	public void testParseIpV4Default() throws Exception {
		RouteConfiguration route = parser.parseConfiguration(config.configurationAt("Route(1)"));		

		
		Assert.assertNotNull(route.getNlri());
		
		Assert.assertEquals(new NetworkLayerReachabilityInformation(0, null), route.getNlri());
		
		Assert.assertNotNull(route.getPathAttributes());
		
		Assert.assertEquals(1, route.getPathAttributes().getAttributes().size());
	}
	
	@Test
	public void testBadPrefix() {
		for(int i=2; i<6; i++) {
			boolean caught = false;
			String key = "Route(" + i + ")";			
			
			try {
				parser.parseConfiguration(config.configurationAt(key));
			} catch(ConfigurationException e) {
				caught = true;
			}
			
			Assert.assertTrue(key, caught);
		}
	}
	
	@Test
	public void testBadIPv4Prefixe() {
		for(int i=6; i<13; i++) {
			boolean caught = false;
			String key = "Route(" + i + ")";			
			
			try {
				parser.parseConfiguration(config.configurationAt(key));
			} catch(ConfigurationException e) {
				caught = true;
			}
			
			Assert.assertTrue(key, caught);
		}
	}

	@Test
	public void testParseIpV6FullLength() throws Exception {
		RouteConfiguration route = parser.parseConfiguration(config.configurationAt("Route(13)"));
		
		Assert.assertNotNull(route.getNlri());
		
		Assert.assertEquals(new NetworkLayerReachabilityInformation(60, new byte[] {
			(byte)0x20, (byte)0x01, (byte)0x0d, (byte)0xb8, (byte)0x00, (byte)0x00, (byte)0xcd, (byte)0x30	
		}), route.getNlri());
		
		Assert.assertNotNull(route.getPathAttributes());
		
		Assert.assertEquals(1, route.getPathAttributes().getAttributes().size());
	}

	@Test
	public void testParseIpV6ShortLength() throws Exception {
		RouteConfiguration route = parser.parseConfiguration(config.configurationAt("Route(14)"));
		
		Assert.assertNotNull(route.getNlri());
		
		Assert.assertEquals(new NetworkLayerReachabilityInformation(60, new byte[] {
			(byte)0x20, (byte)0x01, (byte)0x0d, (byte)0xb8, (byte)0x00, (byte)0x00, (byte)0xcd, (byte)0x30	
		}), route.getNlri());
		
		Assert.assertNotNull(route.getPathAttributes());
		
		Assert.assertEquals(1, route.getPathAttributes().getAttributes().size());
	}
	
	@Test
	public void testParseIpV6Compact() throws Exception {
		RouteConfiguration route = parser.parseConfiguration(config.configurationAt("Route(15)"));
		
		Assert.assertNotNull(route.getNlri());
		
		Assert.assertEquals(new NetworkLayerReachabilityInformation(60, new byte[] {
			(byte)0x20, (byte)0x01, (byte)0x0d, (byte)0xb8, (byte)0x00, (byte)0x00, (byte)0xcd, (byte)0x30	
		}), route.getNlri());
		
		Assert.assertNotNull(route.getPathAttributes());
		
		Assert.assertEquals(1, route.getPathAttributes().getAttributes().size());
	}

	@Test
	public void testParseIpV6Default() throws Exception {
		RouteConfiguration route = parser.parseConfiguration(config.configurationAt("Route(16)"));
		
		Assert.assertNotNull(route.getNlri());
		
		Assert.assertEquals(new NetworkLayerReachabilityInformation(0, null), route.getNlri());
		
		Assert.assertNotNull(route.getPathAttributes());
		
		Assert.assertEquals(1, route.getPathAttributes().getAttributes().size());
	}

	@Test
	public void testBadIPv6Prefixe() {
		for(int i=17; i<22; i++) {
			boolean caught = false;
			String key = "Route(" + i + ")";			
			
			try {
				parser.parseConfiguration(config.configurationAt(key));
			} catch(ConfigurationException e) {
				caught = true;
			}
			
			Assert.assertTrue(key, caught);
		}
	}
	
	@Test
	public void testParseBinary() throws Exception {
		RouteConfiguration route = parser.parseConfiguration(config.configurationAt("Route(23)"));
		
		Assert.assertNotNull(route.getNlri());
		
		Assert.assertEquals(new NetworkLayerReachabilityInformation(60, new byte[] {
			(byte)0x20, (byte)0x01, (byte)0x0d, (byte)0xb8, (byte)0x00, (byte)0x00, (byte)0xcd, (byte)0x30	
		}), route.getNlri());
		
		Assert.assertNotNull(route.getPathAttributes());
		
		Assert.assertEquals(1, route.getPathAttributes().getAttributes().size());
	}

	@Test
	public void testBadBinaryPrefixe() {
		for(int i=24; i<27; i++) {
			boolean caught = false;
			String key = "Route(" + i + ")";			
			
			try {
				parser.parseConfiguration(config.configurationAt(key));
			} catch(ConfigurationException e) {
				caught = true;
			}
			
			Assert.assertTrue(key, caught);
		}
	}
	
}
