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
 * File: org.bgp4j.netty.LocalChannelBGPv4TestBase.java 
 */
package org.bgp4j.netty;

import java.util.UUID;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.local.DefaultLocalClientChannelFactory;
import org.jboss.netty.channel.local.DefaultLocalServerChannelFactory;
import org.jboss.netty.channel.local.LocalAddress;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class LocalChannelBGPv4TestBase extends BGPv4TestBase {
	
	@BeforeClass
	public static void beforeClass() {
		clientFactory = new DefaultLocalClientChannelFactory();
		serverFactory = new DefaultLocalServerChannelFactory();
	}

	@AfterClass
	public static void afterClass() {
		clientFactory.releaseExternalResources();
		serverFactory.releaseExternalResources();
	}
	
	@Before
	public void before() {
		address = new LocalAddress(UUID.randomUUID().toString());
		serverPipelineFactory = new ParametrizableChannelPipelineFactory();
		clientPipelineFactory = new ParametrizableChannelPipelineFactory();

		serverBootstrap = new ServerBootstrap(serverFactory);
		serverBootstrap.setPipelineFactory(serverPipelineFactory);

		clientBootstrap = new ClientBootstrap(clientFactory);
		clientBootstrap.setPipelineFactory(clientPipelineFactory);
	}
	
	@After
	public void after() {
		address = null;
	}

	private static DefaultLocalClientChannelFactory clientFactory;
	private static DefaultLocalServerChannelFactory serverFactory;
	protected ParametrizableChannelPipelineFactory serverPipelineFactory;
	protected ParametrizableChannelPipelineFactory clientPipelineFactory;
	protected LocalAddress address;
	protected ServerBootstrap serverBootstrap;
	protected ClientBootstrap clientBootstrap;

	protected void setupMessageRecordingClientPipeline() {
		clientRecorder = new MessageRecordingChannelHandler();
		
		clientPipelineFactory.addChannelHandler(clientRecorder);
	}

	protected MessageRecordingChannelHandler clientRecorder;
}
