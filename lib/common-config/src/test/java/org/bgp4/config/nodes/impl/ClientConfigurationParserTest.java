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

import junit.framework.Assert;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.bgp4.config.ConfigTestBase;
import org.bgp4.config.nodes.ClientConfiguration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class ClientConfigurationParserTest extends ConfigTestBase {
	@Before
	public void before() throws Exception {
		this.config = loadConfiguration("config/nodes/ClientConfig.xml");
		this.parser = obtainInstance(ClientConfigurationParser.class);
	}
	
	@After
	public void after() {
		this.config = null;
		this.parser = null;
	}
	
	private XMLConfiguration config;
	private ClientConfigurationParser parser;
		
	@Test
	public void testAddressOnlyClientConfig() throws ConfigurationException {
		ClientConfiguration clientConfig = parser.parseConfig(config.configurationAt("Client(0)"));
		
		Assert.assertEquals(0, clientConfig.getRemoteAddress().getPort());
		Assert.assertEquals("192.168.4.1", clientConfig.getRemoteAddress().getHostName());
	}
	
	@Test
	public void testAddressPortClientConfig() throws ConfigurationException {
		ClientConfiguration clientConfig = parser.parseConfig(config.configurationAt("Client(1)"));
		
		Assert.assertEquals(17179, clientConfig.getRemoteAddress().getPort());
		Assert.assertEquals("192.168.4.1", clientConfig.getRemoteAddress().getHostName());
	}

	@Test(expected=ConfigurationException.class)
	public void testDefaultClientConfig() throws ConfigurationException {
		parser.parseConfig(config.configurationAt("Client(2)"));
	}
	
	@Test(expected=ConfigurationException.class)
	public void testPortOnlyClientConfig() throws ConfigurationException {
		parser.parseConfig(config.configurationAt("Client(3)"));
	}
}
