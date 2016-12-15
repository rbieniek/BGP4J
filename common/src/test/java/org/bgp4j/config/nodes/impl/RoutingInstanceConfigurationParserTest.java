/**
 * 
 */
package org.bgp4j.config.nodes.impl;

import junit.framework.Assert;

import org.apache.commons.configuration.XMLConfiguration;
import org.bgp4j.config.ConfigTestBase;
import org.bgp4j.config.nodes.RoutingInstanceConfiguration;
import org.bgp4j.config.nodes.RoutingPeerConfiguration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author rainer
 *
 */
public class RoutingInstanceConfigurationParserTest  extends ConfigTestBase  {
	
	@Before
	public void before() throws Exception {
		this.config = loadConfiguration("config/nodes/RoutingInstanceConfig.xml");
		this.parser = obtainInstance(RoutingInstanceConfurationParser.class);
		
		RoutingPeerConfigurationParser peerParser = obtainInstance(RoutingPeerConfigurationParser.class);
		
		firstPeer = peerParser.parseConfiguration(config.configurationAt("RoutingPeer(0)"));
		secondPeer = peerParser.parseConfiguration(config.configurationAt("RoutingPeer(1)"));
	}
	
	@After
	public void after() {
		this.config = null;
		this.parser = null;
	}
	
	private XMLConfiguration config;
	private RoutingInstanceConfurationParser parser;
	private RoutingPeerConfiguration  firstPeer;
	private RoutingPeerConfiguration  secondPeer;
	
	@Test
	public void testOneEntry() throws Exception {
		RoutingInstanceConfiguration prc = parser.parseConfiguration(config.configurationAt("RoutingInstance(0)"));

		Assert.assertEquals(firstPeer, prc.getFirstPeer());
		Assert.assertEquals(secondPeer, prc.getSecondPeer());
	}
	
}
