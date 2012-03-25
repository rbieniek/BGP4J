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
 * File: org.bgp4.config.nodes.impl.BgpServerConfigurationImplTest.java 
 */
package org.bgp4.config.nodes.impl;

import java.net.InetAddress;

import junit.framework.Assert;

import org.junit.Test;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class HttpServerConfigurationImplTest {

	@Test
	public void testEquals() throws Exception {
		HttpServerConfigurationImpl server1 = new HttpServerConfigurationImpl(new ServerConfigurationImpl(InetAddress.getByName("192.168.4.1"), 100));
		HttpServerConfigurationImpl server2 = new HttpServerConfigurationImpl(new ServerConfigurationImpl(InetAddress.getByName("192.168.4.1"), 100));
		HttpServerConfigurationImpl server3 = new HttpServerConfigurationImpl(new ServerConfigurationImpl(InetAddress.getByName("192.168.4.2"), 100));
		HttpServerConfigurationImpl server4 = new HttpServerConfigurationImpl(new ServerConfigurationImpl(InetAddress.getByName("192.168.4.1"), 200));

		Assert.assertTrue(server1.equals(server2));
		Assert.assertFalse(server1.equals(server3));
		Assert.assertFalse(server1.equals(server4));
	}

	@Test
	public void testHashCode() throws Exception {
		HttpServerConfigurationImpl server1 = new HttpServerConfigurationImpl(new ServerConfigurationImpl(InetAddress.getByName("192.168.4.1"), 100));
		HttpServerConfigurationImpl server2 = new HttpServerConfigurationImpl(new ServerConfigurationImpl(InetAddress.getByName("192.168.4.1"), 100));
		HttpServerConfigurationImpl server3 = new HttpServerConfigurationImpl(new ServerConfigurationImpl(InetAddress.getByName("192.168.4.2"), 100));
		HttpServerConfigurationImpl server4 = new HttpServerConfigurationImpl(new ServerConfigurationImpl(InetAddress.getByName("192.168.4.1"), 200));

		Assert.assertEquals(server1.hashCode(), server2.hashCode());
		Assert.assertFalse(server1.hashCode() == server3.hashCode());
		Assert.assertFalse(server1.hashCode() == server4.hashCode());
	}
}
