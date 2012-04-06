/**
 * 
 */
package org.bgp4j.config.nodes.impl;

import junit.framework.Assert;

import org.apache.commons.configuration.XMLConfiguration;
import org.bgp4j.config.ConfigTestBase;
import org.bgp4j.config.nodes.AddressFamilyRoutingPeerConfiguration;
import org.bgp4j.config.nodes.RoutingPeerConfiguration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author rainer
 *
 */
public class RoutingPeerConfigurationParserTest  extends ConfigTestBase  {
	
	@Before
	public void before() throws Exception {
		this.config = loadConfiguration("config/nodes/RoutingPeerConfig.xml");
		this.parser = obtainInstance(RoutingPeerConfigurationParser.class);
		
		AddressFamilyRoutingPeerConfigurationParser peerParser = obtainInstance(AddressFamilyRoutingPeerConfigurationParser.class);
		
		ipv4Peer = peerParser.parseConfiguration(config.configurationAt("RoutingConfiguration(0)"));
		ipv6Peer = peerParser.parseConfiguration(config.configurationAt("RoutingConfiguration(1)"));
	}
	
	@After
	public void after() {
		this.config = null;
		this.parser = null;
	}
	
	private XMLConfiguration config;
	private RoutingPeerConfigurationParser parser;
	private AddressFamilyRoutingPeerConfiguration ipv4Peer;
	private AddressFamilyRoutingPeerConfiguration ipv6Peer;
	
	@Test
	public void testOneEntry() throws Exception {
		RoutingPeerConfiguration prc = parser.parseConfiguration(config.configurationAt("RoutingPeer(0)"));
		
		Assert.assertEquals("peer", prc.getPeerName());
		Assert.assertEquals(1, prc.getAddressFamilyConfigrations().size());
		Assert.assertTrue(prc.getAddressFamilyConfigrations().contains(ipv4Peer));
	}
	
	@Test
	public void testTwoEntry() throws Exception {
		RoutingPeerConfiguration prc = parser.parseConfiguration(config.configurationAt("RoutingPeer(1)"));
		
		Assert.assertEquals("extension_key", prc.getPeerName());
		Assert.assertEquals(2, prc.getAddressFamilyConfigrations().size());
		Assert.assertTrue(prc.getAddressFamilyConfigrations().contains(ipv4Peer));
		Assert.assertTrue(prc.getAddressFamilyConfigrations().contains(ipv6Peer));
	}
}
