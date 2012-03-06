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

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.local.DefaultLocalClientChannelFactory;
import org.jboss.netty.channel.local.DefaultLocalServerChannelFactory;
import org.junit.AfterClass;
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
	
	private static DefaultLocalClientChannelFactory clientFactory;
	private static DefaultLocalServerChannelFactory serverFactory;

	protected ServerBootstrap buildLocalServerBootstrap(final ChannelPipelineFactory pipelineFactory) {
		ServerBootstrap bootstrap = new ServerBootstrap(serverFactory);
		
		bootstrap.setPipelineFactory(pipelineFactory);
		
		return bootstrap;
	}
	
	protected ClientBootstrap buildLocalClientBootstrap(final ChannelPipeline pipeline) {
		ClientBootstrap bootstrap = new ClientBootstrap(clientFactory);
		
		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			
			@Override
			public ChannelPipeline getPipeline() throws Exception {
				return pipeline;
			}
		});
		
		return bootstrap;
	}
}
