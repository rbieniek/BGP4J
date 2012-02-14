/**
 *  Copyright 2012 Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * 
 * File: org.bgp4.config.nodes.impl.ServerConfigurationParserTest.java 
 */
package org.bgp4.config.nodes.impl;

import java.net.InetAddress;

import junit.framework.Assert;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.bgp4.config.ConfigTestBase;
import org.bgp4.config.nodes.PeerConfiguration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class PeerConfigurationParserTest extends ConfigTestBase {

	@Before
	public void before() throws Exception {
		this.config = loadConfiguration("config/nodes/PeerConfig.xml");
		this.parser = obtainInstance(PeerConfigurationParser.class);
	}
	
	@After
	public void after() {
		this.config = null;
		this.parser = null;
	}
	
	private XMLConfiguration config;
	private PeerConfigurationParser parser;
		
	@Test
	public void testAcceptedMinimalConfiguration() throws Exception {
		PeerConfiguration peerConfig = parser.parseConfiguration(config.configurationAt("BgpPeer(0)"));
		
		Assert.assertEquals(InetAddress.getByName("192.168.4.1"), peerConfig.getClientConfig().getRemoteAddress().getAddress());
		Assert.assertEquals(179, peerConfig.getClientConfig().getRemoteAddress().getPort());
		Assert.assertEquals(10, peerConfig.getLocalAS());
		Assert.assertEquals(11, peerConfig.getRemoteAS());
		Assert.assertEquals("foo", peerConfig.getPeerName());
	}
	
	@Test(expected=ConfigurationException.class)
	public void testBogusConfigurationMissingClientConfiguration() throws Exception {
		@SuppressWarnings("unused")
		PeerConfiguration peerConfig = parser.parseConfiguration(config.configurationAt("BgpPeer(1)"));
	}
	
	@Test(expected=ConfigurationException.class)
	public void testBogusConfigurationInvalidLocalAS() throws Exception {
		@SuppressWarnings("unused")
		PeerConfiguration peerConfig = parser.parseConfiguration(config.configurationAt("BgpPeer(2)"));
	}
	
	@Test(expected=ConfigurationException.class)
	public void testBogusConfigurationInvalidRemoteAS() throws Exception {
		@SuppressWarnings("unused")
		PeerConfiguration peerConfig = parser.parseConfiguration(config.configurationAt("BgpPeer(3)"));
	}

	@Test(expected=ConfigurationException.class)
	public void testBogusConfigurationDuplicateClientConfiguration() throws Exception {
		@SuppressWarnings("unused")
		PeerConfiguration peerConfig = parser.parseConfiguration(config.configurationAt("BgpPeer(4)"));
	}
	
}
