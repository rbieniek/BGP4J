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
 * File: org.bgp4j.management.web.WebManagementServiceTest.java 
 */
package org.bgp4j.management.web;

import java.net.InetSocketAddress;

import javax.servlet.http.HttpServletResponse;

import junit.framework.Assert;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.bgp4j.management.web.service.WebManagementServer;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class WebManagementServerTest extends WebManagementTestBase {

	@Before
	public void before() {
		server = obtainInstance(WebManagementServer.class);
	}
	
	private WebManagementServer server;
	
	@Test
	public void testStartServer() throws Exception {
		server.setConfiguration(httpServerConfiguration);
		server.startServer();
		
		Thread.sleep(1000);
		
		InetSocketAddress serverAddress = httpServerConfiguration.getServerConfiguration().getListenAddress();
		HttpClient client = new HttpClient();
		HttpMethod method = new GetMethod("http://" + serverAddress.getAddress().getHostAddress() + ":" + serverAddress.getPort() + "/rest/ping");
		
		Assert.assertEquals(HttpServletResponse.SC_OK, client.executeMethod(method));
		Assert.assertTrue(method.getResponseBodyAsString().startsWith("{ \"Time\":"));
		
		server.stopServer();
	}
}
