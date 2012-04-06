package org.bgp4j.config.nodes.impl;

import java.util.Iterator;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.bgp4j.config.ConfigTestBase;
import org.bgp4j.config.nodes.PrefixRoutingFilterConfiguration;
import org.bgp4j.config.nodes.RoutingFilterConfiguration;
import org.bgp4j.net.NetworkLayerReachabilityInformation;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RoutingFilterConfigurationParserTest extends ConfigTestBase {

	@Before
	public void before() throws Exception {
		this.config = loadConfiguration("config/nodes/RoutingFilterConfig.xml");
		this.parser = obtainInstance(RoutingFilterConfigurationParser.class);
	}
	
	@After
	public void after() {
		this.config = null;
		this.parser = null;
	}
	
	private XMLConfiguration config;
	private RoutingFilterConfigurationParser parser;

	@Test
	public void testEmptyPrefixFilter() throws Exception {
		RoutingFilterConfiguration rfc = parser.parseConfiguration(config.configurationAt("Filter(0)"));
		
		Assert.assertTrue(rfc instanceof PrefixRoutingFilterConfiguration);
		
		PrefixRoutingFilterConfiguration prfc = (PrefixRoutingFilterConfiguration)rfc;
		Iterator<NetworkLayerReachabilityInformation> it = prfc.getFilterPrefixes().iterator();
		
		Assert.assertFalse(it.hasNext());
	}

	@Test
	public void testOnePrefixFilter() throws Exception {
		RoutingFilterConfiguration rfc = parser.parseConfiguration(config.configurationAt("Filter(1)"));
		
		Assert.assertTrue(rfc instanceof PrefixRoutingFilterConfiguration);
		
		PrefixRoutingFilterConfiguration prfc = (PrefixRoutingFilterConfiguration)rfc;
		Iterator<NetworkLayerReachabilityInformation> it = prfc.getFilterPrefixes().iterator();
		
		Assert.assertTrue(it.hasNext());
		Assert.assertEquals(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, (byte)0x01}), it.next());

		Assert.assertFalse(it.hasNext());
	}

	@Test
	public void testTwoPrefixFilter() throws Exception {
		RoutingFilterConfiguration rfc = parser.parseConfiguration(config.configurationAt("Filter(2)"));
		
		Assert.assertTrue(rfc instanceof PrefixRoutingFilterConfiguration);
		
		PrefixRoutingFilterConfiguration prfc = (PrefixRoutingFilterConfiguration)rfc;
		Iterator<NetworkLayerReachabilityInformation> it = prfc.getFilterPrefixes().iterator();
		
		Assert.assertTrue(it.hasNext());
		Assert.assertEquals(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, (byte)0x01}), it.next());
		Assert.assertTrue(it.hasNext());
		Assert.assertEquals(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, (byte)0x02}), it.next());

		Assert.assertFalse(it.hasNext());
	}
	
	@Test(expected=ConfigurationException.class)
	public void testNoFilterType() throws Exception {
		parser.parseConfiguration(config.configurationAt("Filter(3)"));
	}
	
	@Test(expected=ConfigurationException.class)
	public void testMisspelledFilterType() throws Exception {
		parser.parseConfiguration(config.configurationAt("Filter(4)"));
	}

	@Test(expected=ConfigurationException.class)
	public void testNoPrefixValue() throws Exception {
		parser.parseConfiguration(config.configurationAt("Filter(5)"));
	}
	
	@Test(expected=ConfigurationException.class)
	public void testBlankPrefixValue() throws Exception {
		parser.parseConfiguration(config.configurationAt("Filter(6)"));
	}
}
