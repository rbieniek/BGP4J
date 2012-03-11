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
 * File: org.bgp4j.netty.LocalhostNetworkChannelBGPv4ClientTestBase.java 
 */
package org.bgp4j.netty;

import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import junit.framework.Assert;

import org.apache.commons.configuration.XMLConfiguration;
import org.bgp4.config.Configuration;
import org.bgp4.config.ConfigurationParser;
import org.bgp4j.netty.handlers.BGPv4Codec;
import org.bgp4j.netty.handlers.BGPv4Reframer;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.ClientSocketChannelFactory;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.junit.After;
import org.junit.Before;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class LocalhostNetworkChannelBGPv4ClientTestBase extends
		LocalhostNetworkChannelBGPv4TestBase {

	protected ClientBootstrap clientBootstrap;
	protected Channel clientChannel;
	protected ClientSocketChannelFactory clientFactory;
	protected ProxyChannelHandler clientProxyHander;
	protected ConfigurationParser parser;

	@After
	public void after() {
		if(clientChannel != null)
			clientChannel.close();
		clientChannel = null;
		
		if(clientBootstrap != null)
			clientBootstrap.releaseExternalResources();
		
		clientBootstrap = null;
		
		clientFactory.releaseExternalResources();
		clientFactory = null;
	
		parser = null;
	}

	@Before
	public void before() throws Exception {
		parser = obtainInstance(ConfigurationParser.class);
	
		clientFactory = new NioClientSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool());
		clientBootstrap = new ClientBootstrap(clientFactory);
		clientProxyHander = obtainInstance(ProxyChannelHandler.class);
		
		clientBootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			
			@Override
			public ChannelPipeline getPipeline() throws Exception {
				return Channels.pipeline(obtainInstance(BGPv4Reframer.class),
						obtainInstance(BGPv4Codec.class),
						clientProxyHander);
			}
		});
	}

	protected void connectServer() throws Exception {
		final Object waitLock = new Object();
		
		clientBootstrap.connect(new InetSocketAddress(Inet4Address.getLocalHost(), serverPort)).addListener(new ChannelFutureListener() {
			
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				if(future.isSuccess())
					clientChannel = future.getChannel();
	
				synchronized (waitLock) {
					waitLock.notifyAll();
				}
			}
		});
		
		synchronized (waitLock) {
			waitLock.wait();
		}
		
		Assert.assertNotNull(clientChannel);		
	}

	// -- end of test messages
	protected Configuration loadConfiguration(String fileName) throws Exception {
		return parser.parseConfiguration(new XMLConfiguration(fileName));
	}

}
