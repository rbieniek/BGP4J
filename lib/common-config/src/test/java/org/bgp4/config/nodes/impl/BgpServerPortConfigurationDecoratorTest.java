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
 * File: org.bgp4.config.nodes.impl.BgpServerPortConfigurationDecoratorTest.java 
 */
package org.bgp4.config.nodes.impl;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class BgpServerPortConfigurationDecoratorTest {

	@Test
	public void testDefaultPortNoAddress() throws UnknownHostException {
		ServerConfigurationImpl impl = new ServerConfigurationImpl();
		BgpServerPortConfigurationDecorator decorator = new BgpServerPortConfigurationDecorator(impl);
		
		Assert.assertEquals(179, decorator.getListenAddress().getPort());
		Assert.assertEquals(InetAddress.getByName("0.0.0.0"), decorator.getListenAddress().getAddress());
	}

	@Test
	public void testDefaultPortGivenAddress() throws UnknownHostException {
		ServerConfigurationImpl impl = new ServerConfigurationImpl(InetAddress.getByName("192.168.4.1"));
		BgpServerPortConfigurationDecorator decorator = new BgpServerPortConfigurationDecorator(impl);
		
		Assert.assertEquals(179, decorator.getListenAddress().getPort());
		Assert.assertEquals(InetAddress.getByName("192.168.4.1"), decorator.getListenAddress().getAddress());
	}

	@Test
	public void testOtherPortNoAddress() throws UnknownHostException {
		ServerConfigurationImpl impl = new ServerConfigurationImpl(null, 17179);
		BgpServerPortConfigurationDecorator decorator = new BgpServerPortConfigurationDecorator(impl);
		
		Assert.assertEquals(17179, decorator.getListenAddress().getPort());
		Assert.assertEquals(InetAddress.getByName("0.0.0.0"), decorator.getListenAddress().getAddress());
	}

	@Test
	public void testOtherPortGivenAddress() throws UnknownHostException {
		ServerConfigurationImpl impl = new ServerConfigurationImpl(InetAddress.getByName("192.168.4.1"), 17179);
		BgpServerPortConfigurationDecorator decorator = new BgpServerPortConfigurationDecorator(impl);
		
		Assert.assertEquals(17179, decorator.getListenAddress().getPort());
		Assert.assertEquals(InetAddress.getByName("192.168.4.1"), decorator.getListenAddress().getAddress());
	}
}