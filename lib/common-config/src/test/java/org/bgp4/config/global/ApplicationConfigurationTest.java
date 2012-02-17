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
 * File: org.bgp4.config.global.ApplicationConfigurationTest.java 
 */
package org.bgp4.config.global;

import java.net.InetAddress;

import junit.framework.Assert;

import org.bgp4.config.ConfigTestBase;
import org.bgp4.config.nodes.BgpServerConfiguration;
import org.bgp4.config.nodes.impl.BgpServerConfigurationImpl;
import org.bgp4.config.nodes.impl.ServerConfigurationImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class ApplicationConfigurationTest extends ConfigTestBase {

	@Before
	public void before() {
		this.applicationConfig = obtainInstance(ApplicationConfiguration.class);
		this.applicationConfig.resetConfiguration();
		this.catcher = obtainInstance(ApplicationConfigurationEventCatcher.class);
		this.catcher.reset();
	}
	
	@After
	public void after() {
		this.applicationConfig = null;
		this.catcher = null;
	}
	
	private ApplicationConfiguration applicationConfig;
	private ApplicationConfigurationEventCatcher catcher;
	
	@Test
	public void testBgpServerConfigurationAdded() throws Exception {
		BgpServerConfiguration config = new BgpServerConfigurationImpl(new ServerConfigurationImpl(InetAddress.getByName("192.168.1.1")));

		Assert.assertNull(applicationConfig.getBgpServerConfiguration());
		applicationConfig.setBgpServerConfiguration(config);
		Assert.assertNotNull(applicationConfig.getBgpServerConfiguration());
		Assert.assertTrue(catcher.isBgpServerConfigurationEventFired());
		Assert.assertEquals(EventType.CONFIGURATION_ADDED, catcher.getBgpServerConfigurationEventType());
		Assert.assertEquals(config, catcher.getBgpServerConfiguration());
	}

	
	@Test
	public void testBgpServerNoConfigurationAdded() throws Exception {
		Assert.assertNull(applicationConfig.getBgpServerConfiguration());
		applicationConfig.setBgpServerConfiguration(null);
		Assert.assertFalse(catcher.isBgpServerConfigurationEventFired());
	}
	
	@Test
	public void testBgpServerConfigurationAddedAndReadded() throws Exception {
		BgpServerConfiguration config = new BgpServerConfigurationImpl(new ServerConfigurationImpl(InetAddress.getByName("192.168.1.1")));

		Assert.assertNull(applicationConfig.getBgpServerConfiguration());
		applicationConfig.setBgpServerConfiguration(config);
		Assert.assertNotNull(applicationConfig.getBgpServerConfiguration());
		Assert.assertTrue(catcher.isBgpServerConfigurationEventFired());
		Assert.assertEquals(EventType.CONFIGURATION_ADDED, catcher.getBgpServerConfigurationEventType());
		Assert.assertEquals(config, catcher.getBgpServerConfiguration());
		
		catcher.reset();
		applicationConfig.setBgpServerConfiguration(config);
		Assert.assertNotNull(applicationConfig.getBgpServerConfiguration());
		Assert.assertFalse(catcher.isBgpServerConfigurationEventFired());
		Assert.assertNull(catcher.getBgpServerConfigurationEventType());
		Assert.assertNull(catcher.getBgpServerConfiguration());
	}

	@Test
	public void testBgpServerConfigurationChanged() throws Exception {
		BgpServerConfiguration config;

		config = new BgpServerConfigurationImpl(new ServerConfigurationImpl(InetAddress.getByName("192.168.1.1")));
		Assert.assertNull(applicationConfig.getBgpServerConfiguration());
		applicationConfig.setBgpServerConfiguration(config);
		Assert.assertNotNull(applicationConfig.getBgpServerConfiguration());
		Assert.assertTrue(catcher.isBgpServerConfigurationEventFired());
		Assert.assertEquals(EventType.CONFIGURATION_ADDED, catcher.getBgpServerConfigurationEventType());
		Assert.assertEquals(config, catcher.getBgpServerConfiguration());

		catcher.reset();

		config = new BgpServerConfigurationImpl(new ServerConfigurationImpl(InetAddress.getByName("192.168.2.1")));
		applicationConfig.setBgpServerConfiguration(config);
		Assert.assertNotNull(applicationConfig.getBgpServerConfiguration());
		Assert.assertTrue(catcher.isBgpServerConfigurationEventFired());
		Assert.assertEquals(EventType.CONFIGURATION_CHANGED, catcher.getBgpServerConfigurationEventType());
		Assert.assertEquals(config, catcher.getBgpServerConfiguration());
	}


	@Test
	public void testBgpServerConfigurationRemoved() throws Exception {
		BgpServerConfiguration config;

		config = new BgpServerConfigurationImpl(new ServerConfigurationImpl(InetAddress.getByName("192.168.1.1")));
		Assert.assertNull(applicationConfig.getBgpServerConfiguration());
		applicationConfig.setBgpServerConfiguration(config);
		Assert.assertNotNull(applicationConfig.getBgpServerConfiguration());
		Assert.assertTrue(catcher.isBgpServerConfigurationEventFired());
		Assert.assertEquals(EventType.CONFIGURATION_ADDED, catcher.getBgpServerConfigurationEventType());
		Assert.assertEquals(config, catcher.getBgpServerConfiguration());

		catcher.reset();

		config = null;
		applicationConfig.setBgpServerConfiguration(config);
		Assert.assertNull(applicationConfig.getBgpServerConfiguration());
		Assert.assertTrue(catcher.isBgpServerConfigurationEventFired());
		Assert.assertEquals(EventType.CONFIGURATION_REMOVED, catcher.getBgpServerConfigurationEventType());
		Assert.assertNull(catcher.getBgpServerConfiguration());
	}
}
