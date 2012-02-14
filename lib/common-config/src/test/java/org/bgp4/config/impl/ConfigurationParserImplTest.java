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
 * File: org.bgp4.config.impl.ConfigurationParserimplTest.java 
 */
package org.bgp4.config.impl;

import java.net.InetAddress;

import junit.framework.Assert;

import org.apache.commons.configuration.ConfigurationException;
import org.bgp4.config.ConfigTestBase;
import org.bgp4.config.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class ConfigurationParserImplTest extends ConfigTestBase {

	@Before
	public void before() {
		parser = obtainInstance(ConfigurationParserImpl.class);
	}
	
	@After
	public void after() {
		parser = null;
	}
	
	private ConfigurationParserImpl parser;

	@Test
	public void testEmptyConfiguration() throws Exception {
		Configuration config = parser.parseConfiguration(loadConfiguration("config/Empty-Config.xml"));
		
		Assert.assertNotNull(config);
		Assert.assertNull(config.getBgpServerConfiguration());
	}

	@Test(expected=ConfigurationException.class)
	public void testConfigurationDuplicateBgpServerConfiguration() throws Exception {
		Configuration config = parser.parseConfiguration(loadConfiguration("config/Config-With-DuplicateBgpServer.xml"));
		
		Assert.assertNotNull(config);
		Assert.assertNull(config.getBgpServerConfiguration());
	}
	
	@Test
	public void testConfigurationWithBgpServerConfiguration() throws Exception {
		Configuration config = parser.parseConfiguration(loadConfiguration("config/Config-With-BgpServer.xml"));
		
		Assert.assertNotNull(config);
		Assert.assertNotNull(config.getBgpServerConfiguration());
		Assert.assertEquals(17179, config.getBgpServerConfiguration().getServerConfiguration().getListenAddress().getPort());
		Assert.assertEquals(InetAddress.getByName("192.168.4.1"), config.getBgpServerConfiguration().getServerConfiguration().getListenAddress().getAddress());
	}
}
