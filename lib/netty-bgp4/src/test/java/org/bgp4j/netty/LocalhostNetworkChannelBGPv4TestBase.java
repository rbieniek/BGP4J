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
 * File: org.bgp4j.netty.LocalhostNetworkChannelBGPv4TestBase.java 
 */
package org.bgp4j.netty;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;

import junit.framework.Assert;

import org.apache.commons.configuration.XMLConfiguration;
import org.bgp4.config.Configuration;
import org.bgp4.config.ConfigurationParser;
import org.bgp4.config.nodes.ClientConfiguration;
import org.bgp4.config.nodes.ClientConfigurationDecorator;
import org.bgp4.config.nodes.PeerConfiguration;
import org.bgp4.config.nodes.PeerConfigurationDecorator;
import org.bgp4j.netty.handlers.BGPv4Codec;
import org.bgp4j.netty.handlers.BGPv4Reframer;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelException;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.ServerSocketChannelFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class LocalhostNetworkChannelBGPv4TestBase  extends BGPv4TestBase {

	private static class ServerPortAwarePeerConfiguration extends PeerConfigurationDecorator {

		private static class ServerPortClientConfig extends ClientConfigurationDecorator {
			private int serverPort;
			
			public ServerPortClientConfig(ClientConfiguration decorated, int serverPort) {
				super(decorated);
				this.serverPort = serverPort;
			}

			/* (non-Javadoc)
			 * @see org.bgp4.config.nodes.ClientConfigurationDecorator#getRemoteAddress()
			 */
			@Override
			public InetSocketAddress getRemoteAddress() {
				try {
					return new InetSocketAddress(InetAddress.getLocalHost(), this.serverPort);
				} catch (UnknownHostException e) {
					throw new RuntimeException(e);
				}
			}			
		}
		
		private int serverPort;
		
		public ServerPortAwarePeerConfiguration(PeerConfiguration peerConfiguration, int serverPort) {
			super(peerConfiguration);
			
			this.serverPort = serverPort;
		}

		/* (non-Javadoc)
		 * @see org.bgp4.config.nodes.PeerConfigurationDecorator#getClientConfig()
		 */
		@Override
		public ClientConfiguration getClientConfig() {
			return new ServerPortClientConfig(super.getClientConfig(), serverPort);
		}
	}
	
	public static Set<Integer> usedPorts = new HashSet<Integer>();
	
	@BeforeClass
	public static void beforeClassLocalhostNetworkChannelBGPv4TestBase() {
		usedPorts.clear();
	}
	
	@AfterClass
	public static void afterClassLocalhostNetworkChannelBGPv4TestBase() {
	}
	
	@Before
	public void beforeLocalhostNetworkChannelBGPv4TestBase() throws Exception {
		parser = obtainInstance(ConfigurationParser.class);
		
		serverProxyChannelHandler = obtainInstance(ProxyChannelHandler.class);
		serverSocketFactory = new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool());
		serverBootstrap = new ServerBootstrap(serverSocketFactory);
		serverBootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			
			@Override
			public ChannelPipeline getPipeline() throws Exception {
				return Channels.pipeline(
						obtainInstance(BGPv4Reframer.class),
						obtainInstance(BGPv4Codec.class),
						serverProxyChannelHandler
						);
			}
		});
	
		serverBootstrap.setOption("reuseAddress", true);
		serverBootstrap.setOption("tcpNoDelay", true);
		serverBootstrap.setOption("keepAlive", true);

		serverPort = -1;
		serverChannel = null;
		for(int i=16000; (i<64000) && (serverPort < 0); i++) {
			if(!usedPorts.contains(i)) {
				try {
					serverChannel = serverBootstrap.bind(new InetSocketAddress(Inet4Address.getLocalHost(), i));

					if (serverChannel != null) {
						serverPort = i;
						break;
					}
				} catch (ChannelException e) {
					// ignore bind failure
					e.printStackTrace();
				}
			}
		}
		
		Assert.assertTrue(serverPort > 0);
		usedPorts.add(serverPort);
		
		Assert.assertNotNull(serverChannel);
	}
	
	@After
	public void afterLocalhostNetworkChannelBGPv4TestBase() {
		if(serverChannel != null)
			serverChannel.close();
		serverChannel = null;
		
		if(serverBootstrap != null)
			serverBootstrap.releaseExternalResources();
		serverBootstrap = null;

		serverSocketFactory.releaseExternalResources();
		serverSocketFactory = null;
		
		parser = null;
	}
	
	private ServerSocketChannelFactory serverSocketFactory;
	private ServerBootstrap serverBootstrap;
	protected ProxyChannelHandler serverProxyChannelHandler;
	protected int serverPort;
	protected Channel serverChannel;
	protected ConfigurationParser parser;

	// -- end of test messages
	protected Configuration loadConfiguration(String fileName) throws Exception {
		return parser.parseConfiguration(new XMLConfiguration(fileName));
	}
	
	protected PeerConfiguration buildServerPortAwarePeerConfiguration(PeerConfiguration configuration) {
		return new ServerPortAwarePeerConfiguration(configuration, serverPort);
	}
}
