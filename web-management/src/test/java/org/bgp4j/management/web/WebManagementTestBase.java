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
 * File: org.bgp4j.management.web.WebManagementTestBase.java 
 */
package org.bgp4j.management.web;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import org.bgp4.config.nodes.HttpServerConfiguration;
import org.bgp4.config.nodes.ServerConfiguration;
import org.bgp4j.weld.WeldTestCaseBase;
import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class WebManagementTestBase extends WeldTestCaseBase {

	public static class TestHttpServerConfiguration implements HttpServerConfiguration {

		public static class TestServerConfiguration implements ServerConfiguration {

			private InetSocketAddress serverAddress;
			
			public TestServerConfiguration(int serverPort) throws UnknownHostException {
				this.serverAddress = new InetSocketAddress(InetAddress.getLocalHost(), serverPort);
			}
			
			@Override
			public InetSocketAddress getListenAddress() {
				return this.serverAddress;
			}
			
		}

		private TestServerConfiguration serverConfig;
		
		public TestHttpServerConfiguration(int serverPort) throws UnknownHostException {
			this.serverConfig = new TestServerConfiguration(serverPort);
		}
		
		@Override
		public ServerConfiguration getServerConfiguration() {
			return this.serverConfig;
		}
		
	}
	
	@Before
	public void beforeWebManagementTest() throws Exception  {
		log = obtainInstance(Logger.class);
		
		for(int port = 32000; port<64000; port++) {
			try {
				new Socket(InetAddress.getLocalHost(), port);
			} catch(IOException e) {
				serverPort = port;
				httpServerConfiguration = new TestHttpServerConfiguration(port);
				
				log.info("assigning free port " + serverPort + " to HTTP server");
				break;
			}
		}
	}

	@After
	public void afterWebManagementTest() {
		this.log = null;
		this.httpServerConfiguration = null;
	}
	
	private Logger log;
	
	protected int serverPort;
	protected HttpServerConfiguration httpServerConfiguration;
}
