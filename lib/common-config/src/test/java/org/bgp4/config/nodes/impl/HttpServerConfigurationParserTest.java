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
 * File: org.bgp4.config.impl.BgpServerConfigurationParserTest.java 
 */
package org.bgp4.config.nodes.impl;

import java.net.InetAddress;

import junit.framework.Assert;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.bgp4.config.ConfigTestBase;
import org.bgp4.config.nodes.HttpServerConfiguration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class HttpServerConfigurationParserTest extends ConfigTestBase {
	@Before
	public void before() throws Exception {
		this.config = loadConfiguration("config/nodes/HttpServerConfig.xml");
		this.parser = obtainInstance(HttpServerConfigurationParser.class);
	}
	
	@After
	public void after() {
		this.config = null;
		this.parser = null;
	}
	
	private XMLConfiguration config;
	private HttpServerConfigurationParser parser;

	@Test
	public void testHttpServerConfigurationWithoutServerConfiguration() throws Exception {
		HttpServerConfiguration HttpServerConfig = parser.parseConfiguration(config.configurationAt("HttpServer(0)"));
		
		Assert.assertEquals(8080, HttpServerConfig.getServerConfiguration().getListenAddress().getPort());
		Assert.assertEquals(InetAddress.getByName("0.0.0.0"), HttpServerConfig.getServerConfiguration().getListenAddress().getAddress());
	}
	
	@Test
	public void testHttpServerConfigurationWithEmptyServerConfiguration() throws Exception {
		HttpServerConfiguration HttpServerConfig = parser.parseConfiguration(config.configurationAt("HttpServer(1)"));
		
		Assert.assertEquals(8080, HttpServerConfig.getServerConfiguration().getListenAddress().getPort());
		Assert.assertEquals(InetAddress.getByName("0.0.0.0"), HttpServerConfig.getServerConfiguration().getListenAddress().getAddress());
	}

	@Test
	public void testHttpServerConfigurationWithServerPortConfiguration() throws Exception {
		HttpServerConfiguration HttpServerConfig = parser.parseConfiguration(config.configurationAt("HttpServer(2)"));
		
		Assert.assertEquals(17179, HttpServerConfig.getServerConfiguration().getListenAddress().getPort());
		Assert.assertEquals(InetAddress.getByName("0.0.0.0"), HttpServerConfig.getServerConfiguration().getListenAddress().getAddress());
	}

	@Test(expected=ConfigurationException.class)
	public void testHttpServerConfigurationDuplicateServerConfiguration() throws Exception {
		parser.parseConfiguration(config.configurationAt("HttpServer(3)"));
	}
	
}
