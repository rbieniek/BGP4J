/**
 * 
 */
package org.bgp4j.config.nodes.impl;

import junit.framework.Assert;

import org.apache.commons.configuration.XMLConfiguration;
import org.bgp4j.config.ConfigTestBase;
import org.bgp4j.config.nodes.RoutingInstanceConfiguration;
import org.bgp4j.config.nodes.RoutingProcessorConfiguration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author rainer
 *
 */
public class RoutingProcessorConfigurationParserTest extends ConfigTestBase {
	@Before
	public void before() throws Exception {
		this.config = loadConfiguration("config/nodes/RoutingProcessorConfig.xml");
		this.parser = obtainInstance(RoutingProcessorConfigurationParser.class);
		
		RoutingInstanceConfurationParser peerParser = obtainInstance(RoutingInstanceConfurationParser.class);
		
		firstInstance = peerParser.parseConfiguration(config.configurationAt("RoutingInstance(0)"));
		secondInstance = peerParser.parseConfiguration(config.configurationAt("RoutingInstance(1)"));
	}
	
	@After
	public void after() {
		this.config = null;
		this.parser = null;
	}
	
	private XMLConfiguration config;
	private RoutingProcessorConfigurationParser parser;
	private RoutingInstanceConfiguration  firstInstance;
	private RoutingInstanceConfiguration  secondInstance;
	
	@Test
	public void testOneEntry() throws Exception {
		RoutingProcessorConfiguration prc = parser.parseConfiguration(config.configurationAt("RoutingProcessor(0)"));

		Assert.assertEquals(2, prc.getRoutingInstances().size());
		Assert.assertTrue(prc.getRoutingInstances().contains(firstInstance));
		Assert.assertTrue(prc.getRoutingInstances().contains(secondInstance));
	}

}
