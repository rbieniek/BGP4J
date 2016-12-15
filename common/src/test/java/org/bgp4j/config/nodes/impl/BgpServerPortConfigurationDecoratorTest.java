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
package org.bgp4j.config.nodes.impl;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.bgp4j.config.nodes.impl.BgpServerPortConfigurationDecorator;
import org.bgp4j.config.nodes.impl.ServerConfigurationImpl;
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
	public void testOtherPortNoAddress() throws Exception {
		ServerConfigurationImpl impl = new ServerConfigurationImpl(null, 17179);
		BgpServerPortConfigurationDecorator decorator = new BgpServerPortConfigurationDecorator(impl);
		
		Assert.assertEquals(17179, decorator.getListenAddress().getPort());
		Assert.assertEquals(InetAddress.getByName("0.0.0.0"), decorator.getListenAddress().getAddress());
	}

	@Test
	public void testOtherPortGivenAddress() throws Exception {
		ServerConfigurationImpl impl = new ServerConfigurationImpl(InetAddress.getByName("192.168.4.1"), 17179);
		BgpServerPortConfigurationDecorator decorator = new BgpServerPortConfigurationDecorator(impl);
		
		Assert.assertEquals(17179, decorator.getListenAddress().getPort());
		Assert.assertEquals(InetAddress.getByName("192.168.4.1"), decorator.getListenAddress().getAddress());
	}
	
	@Test
	public void testEquals() throws Exception {
		BgpServerPortConfigurationDecorator dec1 = new BgpServerPortConfigurationDecorator(new ServerConfigurationImpl(InetAddress.getByName("192.168.4.1"), 179));
		BgpServerPortConfigurationDecorator dec2 = new BgpServerPortConfigurationDecorator(new ServerConfigurationImpl(InetAddress.getByName("192.168.4.1"), 179));
		BgpServerPortConfigurationDecorator dec3 = new BgpServerPortConfigurationDecorator(new ServerConfigurationImpl(InetAddress.getByName("192.168.4.1")));
		BgpServerPortConfigurationDecorator dec4 = new BgpServerPortConfigurationDecorator(new ServerConfigurationImpl(InetAddress.getByName("192.168.4.1"), 17179));
		BgpServerPortConfigurationDecorator dec5 = new BgpServerPortConfigurationDecorator(new ServerConfigurationImpl(InetAddress.getByName("192.168.4.2"), 179));
		
		Assert.assertTrue(dec1.equals(dec2));
		Assert.assertTrue(dec1.equals(dec3));
		Assert.assertFalse(dec1.equals(dec4));
		Assert.assertFalse(dec1.equals(dec5));
	}

	@Test
	public void testHashCode() throws Exception {
		BgpServerPortConfigurationDecorator dec1 = new BgpServerPortConfigurationDecorator(new ServerConfigurationImpl(InetAddress.getByName("192.168.4.1"), 179));
		BgpServerPortConfigurationDecorator dec2 = new BgpServerPortConfigurationDecorator(new ServerConfigurationImpl(InetAddress.getByName("192.168.4.1"), 179));
		BgpServerPortConfigurationDecorator dec3 = new BgpServerPortConfigurationDecorator(new ServerConfigurationImpl(InetAddress.getByName("192.168.4.1")));
		BgpServerPortConfigurationDecorator dec4 = new BgpServerPortConfigurationDecorator(new ServerConfigurationImpl(InetAddress.getByName("192.168.4.1"), 17179));
		BgpServerPortConfigurationDecorator dec5 = new BgpServerPortConfigurationDecorator(new ServerConfigurationImpl(InetAddress.getByName("192.168.4.2"), 179));
		
		Assert.assertEquals(dec1.hashCode(), dec2.hashCode());
		Assert.assertEquals(dec1.hashCode(), dec3.hashCode());
		Assert.assertFalse(dec1.hashCode() == dec4.hashCode());
		Assert.assertFalse(dec1.hashCode() == dec5.hashCode());
	}
}
