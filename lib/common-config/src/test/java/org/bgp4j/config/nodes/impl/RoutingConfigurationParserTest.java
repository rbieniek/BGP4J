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
import org.bgp4j.config.nodes.RoutingConfiguration;
import org.bgp4j.config.nodes.impl.RoutingConfigurationParser;
import org.bgp4j.net.AddressFamilyKey;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author rainer
 *
 */
public class RoutingConfigurationParserTest extends ConfigTestBase {
	@Before
	public void before() throws Exception {
		this.config = loadConfiguration("config/nodes/RoutingConfig.xml");
		this.parser = obtainInstance(RoutingConfigurationParser.class);
	}
	
	@After
	public void after() {
		this.config = null;
		this.parser = null;
	}
	
	private XMLConfiguration config;
	private RoutingConfigurationParser parser;

	@Test
	public void testSingleAddressFamily() throws Exception {
		RoutingConfiguration routing = parser.parseConfiguration(config.configurationAt("Routing(0)"));
		AddressFamilyRoutingConfiguration afc;
		
		Iterator<AddressFamilyRoutingConfiguration> it = routing.getRoutingConfigurations().iterator();
		
		Assert.assertTrue(it.hasNext());
		afc = it.next();
		
		Assert.assertEquals(AddressFamilyKey.IPV4_UNICAST_FORWARDING, afc.getKey());
		Assert.assertEquals(2, afc.getRoutes().size());
		
		Assert.assertFalse(it.hasNext());
	}

	@Test
	public void testEqualsSingleAddressFamily() throws Exception {
		RoutingConfiguration r1 = parser.parseConfiguration(config.configurationAt("Routing(0)"));
		RoutingConfiguration r2 = parser.parseConfiguration(config.configurationAt("Routing(3)"));
		
		Assert.assertEquals(r1, r2);
		Assert.assertTrue(r1.hashCode() == r2.hashCode());
		Assert.assertTrue(r1.compareTo(r2) == 0);
	}
	
	@Test
	public void testSmallerByAddressFamily() throws Exception {
		RoutingConfiguration r1 = parser.parseConfiguration(config.configurationAt("Routing(0)"));
		RoutingConfiguration r2 = parser.parseConfiguration(config.configurationAt("Routing(6)"));
		
		Assert.assertFalse(r1.equals(r2));
		Assert.assertFalse(r1.hashCode() == r2.hashCode());
		Assert.assertTrue(r1.compareTo(r2) < 0);
	}

	@Test
	public void testSmallerByNumberOfAddressFamily() throws Exception {
		RoutingConfiguration r1 = parser.parseConfiguration(config.configurationAt("Routing(0)"));
		RoutingConfiguration r2 = parser.parseConfiguration(config.configurationAt("Routing(1)"));
		
		Assert.assertFalse(r1.equals(r2));
		Assert.assertFalse(r1.hashCode() == r2.hashCode());
		Assert.assertTrue(r1.compareTo(r2) < 0);
	}

	@Test
	public void testSmallerByRoutes() throws Exception {
		RoutingConfiguration r1 = parser.parseConfiguration(config.configurationAt("Routing(5)"));
		RoutingConfiguration r2 = parser.parseConfiguration(config.configurationAt("Routing(0)"));
		
		Assert.assertFalse(r1.equals(r2));
		Assert.assertFalse(r1.hashCode() == r2.hashCode());
		Assert.assertTrue(r1.compareTo(r2) < 0);
	}

	@Test
	public void testEqualsDoubleAddressFamily() throws Exception {
		RoutingConfiguration r1 = parser.parseConfiguration(config.configurationAt("Routing(1)"));
		RoutingConfiguration r2 = parser.parseConfiguration(config.configurationAt("Routing(4)"));
		
		Assert.assertEquals(r1, r2);
		Assert.assertTrue(r1.hashCode() == r2.hashCode());
		Assert.assertTrue(r1.compareTo(r2) == 0);
	}
	
	@Test
	public void testTwoAddressFamily() throws Exception {
		RoutingConfiguration routing = parser.parseConfiguration(config.configurationAt("Routing(1)"));
		AddressFamilyRoutingConfiguration afc;
		
		Iterator<AddressFamilyRoutingConfiguration> it = routing.getRoutingConfigurations().iterator();
		
		Assert.assertTrue(it.hasNext());
		afc = it.next();
		
		Assert.assertEquals(AddressFamilyKey.IPV4_UNICAST_FORWARDING, afc.getKey());
		Assert.assertEquals(2, afc.getRoutes().size());
		
		Assert.assertTrue(it.hasNext());
		afc = it.next();
		
		Assert.assertEquals(AddressFamilyKey.IPV6_UNICAST_FORWARDING, afc.getKey());
		Assert.assertEquals(2, afc.getRoutes().size());
		
		Assert.assertFalse(it.hasNext());
	}
	
	@Test(expected=ConfigurationException.class)
	public void testDuplicateAddressFamily() throws Exception {
		parser.parseConfiguration(config.configurationAt("Routing(2)"));
	}
}
