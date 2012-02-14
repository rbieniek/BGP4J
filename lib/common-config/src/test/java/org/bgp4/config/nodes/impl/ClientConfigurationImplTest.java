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
 * File: org.bgp4.config.nodes.impl.ClientConfigurationImplTest.java 
 */
package org.bgp4.config.nodes.impl;

import java.net.InetAddress;
import java.net.InetSocketAddress;

import org.apache.commons.configuration.ConfigurationException;
import org.bgp4.config.nodes.ClientConfiguration;
import org.junit.Test;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class ClientConfigurationImplTest {

	@Test(expected=ConfigurationException.class)
	public void testNullAddressRejected() throws Exception {
		@SuppressWarnings("unused")
		ClientConfiguration config = new ClientConfigurationImpl(null, 0);
	}

	@Test(expected=ConfigurationException.class)
	public void testAnyLocalAddressRejected() throws Exception {
		@SuppressWarnings("unused")
		ClientConfiguration config = new ClientConfigurationImpl(InetAddress.getByName("0.0.0.0"), 0);
	}


	@Test(expected=ConfigurationException.class)
	public void testAnyLocalSocketAddressRejected() throws Exception {
		@SuppressWarnings("unused")
		ClientConfiguration config = new ClientConfigurationImpl(new InetSocketAddress(InetAddress.getByName("0.0.0.0"), 0));
	}
	
	@Test(expected=ConfigurationException.class)
	public void testNegativePortNumber() throws Exception {
		@SuppressWarnings("unused")
		ClientConfiguration config = new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1"), -10);
	}

	@Test(expected=ConfigurationException.class)
	public void testLargePortNumber() throws Exception {
		@SuppressWarnings("unused")
		ClientConfiguration config = new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1"), 70000);
	}

	@Test
	public void testAcceptedPortNumber() throws Exception {
		@SuppressWarnings("unused")
		ClientConfiguration config = new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1"), 2048);
	}
	
}
