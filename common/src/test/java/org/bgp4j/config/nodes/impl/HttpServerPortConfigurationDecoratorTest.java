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
 * File: org.Http4.config.nodes.impl.HttpServerPortConfigurationDecoratorTest.java 
 */
package org.bgp4j.config.nodes.impl;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.bgp4j.config.nodes.impl.HttpServerPortConfigurationDecorator;
import org.bgp4j.config.nodes.impl.ServerConfigurationImpl;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class HttpServerPortConfigurationDecoratorTest {

	@Test
	public void testDefaultPortNoAddress() throws UnknownHostException {
		ServerConfigurationImpl impl = new ServerConfigurationImpl();
		HttpServerPortConfigurationDecorator decorator = new HttpServerPortConfigurationDecorator(impl);
		
		Assert.assertEquals(8080, decorator.getListenAddress().getPort());
		Assert.assertEquals(InetAddress.getByName("0.0.0.0"), decorator.getListenAddress().getAddress());
	}

	@Test
	public void testDefaultPortGivenAddress() throws UnknownHostException {
		ServerConfigurationImpl impl = new ServerConfigurationImpl(InetAddress.getByName("192.168.4.1"));
		HttpServerPortConfigurationDecorator decorator = new HttpServerPortConfigurationDecorator(impl);
		
		Assert.assertEquals(8080, decorator.getListenAddress().getPort());
		Assert.assertEquals(InetAddress.getByName("192.168.4.1"), decorator.getListenAddress().getAddress());
	}

	@Test
	public void testOtherPortNoAddress() throws Exception {
		ServerConfigurationImpl impl = new ServerConfigurationImpl(null, 8888);
		HttpServerPortConfigurationDecorator decorator = new HttpServerPortConfigurationDecorator(impl);
		
		Assert.assertEquals(8888, decorator.getListenAddress().getPort());
		Assert.assertEquals(InetAddress.getByName("0.0.0.0"), decorator.getListenAddress().getAddress());
	}

	@Test
	public void testOtherPortGivenAddress() throws Exception {
		ServerConfigurationImpl impl = new ServerConfigurationImpl(InetAddress.getByName("192.168.4.1"), 8888);
		HttpServerPortConfigurationDecorator decorator = new HttpServerPortConfigurationDecorator(impl);
		
		Assert.assertEquals(8888, decorator.getListenAddress().getPort());
		Assert.assertEquals(InetAddress.getByName("192.168.4.1"), decorator.getListenAddress().getAddress());
	}
	
	@Test
	public void testEquals() throws Exception {
		HttpServerPortConfigurationDecorator dec1 = new HttpServerPortConfigurationDecorator(new ServerConfigurationImpl(InetAddress.getByName("192.168.4.1"), 8080));
		HttpServerPortConfigurationDecorator dec2 = new HttpServerPortConfigurationDecorator(new ServerConfigurationImpl(InetAddress.getByName("192.168.4.1"), 8080));
		HttpServerPortConfigurationDecorator dec3 = new HttpServerPortConfigurationDecorator(new ServerConfigurationImpl(InetAddress.getByName("192.168.4.1")));
		HttpServerPortConfigurationDecorator dec4 = new HttpServerPortConfigurationDecorator(new ServerConfigurationImpl(InetAddress.getByName("192.168.4.1"), 8888));
		HttpServerPortConfigurationDecorator dec5 = new HttpServerPortConfigurationDecorator(new ServerConfigurationImpl(InetAddress.getByName("192.168.4.2"), 8080));
		
		Assert.assertTrue(dec1.equals(dec2));
		Assert.assertTrue(dec1.equals(dec3));
		Assert.assertFalse(dec1.equals(dec4));
		Assert.assertFalse(dec1.equals(dec5));
	}

	@Test
	public void testHashCode() throws Exception {
		HttpServerPortConfigurationDecorator dec1 = new HttpServerPortConfigurationDecorator(new ServerConfigurationImpl(InetAddress.getByName("192.168.4.1"), 8080));
		HttpServerPortConfigurationDecorator dec2 = new HttpServerPortConfigurationDecorator(new ServerConfigurationImpl(InetAddress.getByName("192.168.4.1"), 8080));
		HttpServerPortConfigurationDecorator dec3 = new HttpServerPortConfigurationDecorator(new ServerConfigurationImpl(InetAddress.getByName("192.168.4.1")));
		HttpServerPortConfigurationDecorator dec4 = new HttpServerPortConfigurationDecorator(new ServerConfigurationImpl(InetAddress.getByName("192.168.4.1"), 8888));
		HttpServerPortConfigurationDecorator dec5 = new HttpServerPortConfigurationDecorator(new ServerConfigurationImpl(InetAddress.getByName("192.168.4.2"), 8080));
		
		Assert.assertEquals(dec1.hashCode(), dec2.hashCode());
		Assert.assertEquals(dec1.hashCode(), dec3.hashCode());
		Assert.assertFalse(dec1.hashCode() == dec4.hashCode());
		Assert.assertFalse(dec1.hashCode() == dec5.hashCode());
	}
}
