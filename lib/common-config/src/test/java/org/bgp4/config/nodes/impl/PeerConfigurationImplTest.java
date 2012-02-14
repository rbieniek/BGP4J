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
 * File: org.bgp4.config.nodes.impl.PeerConfigurationImplTest.java 
 */
package org.bgp4.config.nodes.impl;

import java.net.InetAddress;

import junit.framework.Assert;

import org.apache.commons.configuration.ConfigurationException;
import org.bgp4.config.nodes.PeerConfiguration;
import org.junit.Test;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class PeerConfigurationImplTest {

	@Test
	public void testAcceptedPeerConfiguration() throws Exception {
		PeerConfiguration config = new PeerConfigurationImpl("foo", new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), 10, 11);
		
		Assert.assertEquals(10, config.getLocalAS());
		Assert.assertEquals(11, config.getRemoteAS());
		Assert.assertEquals(InetAddress.getByName("192.168.4.1"), config.getClientConfig().getRemoteAddress().getAddress());
		Assert.assertEquals(179, config.getClientConfig().getRemoteAddress().getPort());
	}

	@Test(expected=ConfigurationException.class)
	public void testBogusPeerConfigurationNullClientConfiguration() throws Exception {
		@SuppressWarnings("unused")
		PeerConfiguration config = new PeerConfigurationImpl("foo", null, 10, 11);		
	}

	@Test(expected=ConfigurationException.class)
	public void testBogusPeerConfigurationNegativeLocalAS() throws Exception {
		@SuppressWarnings("unused")
		PeerConfiguration config = new PeerConfigurationImpl("foo", new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), -10, 11);		
	}

	@Test(expected=ConfigurationException.class)
	public void testBogusPeerConfigurationNegativeRemoteAS() throws Exception {
		@SuppressWarnings("unused")
		PeerConfiguration config = new PeerConfigurationImpl("foo", new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), 10, -11);		
	}

	@Test(expected=ConfigurationException.class)
	public void testBogusPeerConfigurationEmptyName() throws Exception {
		@SuppressWarnings("unused")
		PeerConfiguration config = new PeerConfigurationImpl("", new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), 10, 11);		
	}
}
