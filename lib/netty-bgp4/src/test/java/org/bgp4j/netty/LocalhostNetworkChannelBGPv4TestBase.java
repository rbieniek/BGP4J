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
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import junit.framework.Assert;

import org.bgp4j.netty.handlers.BGPv4Codec;
import org.bgp4j.netty.handlers.BGPv4Reframer;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelException;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.ChildChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
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

	private static ServerSocketChannelFactory serverSocketFactory;
	
	@BeforeClass
	public static void beforeClassLocalhostNetworkChannelBGPv4TestBase() {
		serverSocketFactory = new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool());
	}
	
	@AfterClass
	public static void afterClassLocalhostNetworkChannelBGPv4TestBase() {
		serverSocketFactory.releaseExternalResources();
		serverSocketFactory = null;
	}
	
	protected static class ProxyChannelHandler extends SimpleChannelHandler {
		
		SimpleChannelHandler proxiedHandler;

		/**
		 * @return the proxiedHandler
		 */
		public SimpleChannelHandler getProxiedHandler() {
			return proxiedHandler;
		}

		/**
		 * @param proxiedHandler the proxiedHandler to set
		 */
		public void setProxiedHandler(SimpleChannelHandler proxiedHandler) {
			this.proxiedHandler = proxiedHandler;
		}

		/**
		 * @param ctx
		 * @param e
		 * @throws Exception
		 * @see org.jboss.netty.channel.SimpleChannelHandler#messageReceived(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.MessageEvent)
		 */
		public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
				throws Exception {
			proxiedHandler.messageReceived(ctx, e);
		}

		/**
		 * @param ctx
		 * @param e
		 * @throws Exception
		 * @see org.jboss.netty.channel.SimpleChannelHandler#exceptionCaught(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.ExceptionEvent)
		 */
		public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
				throws Exception {
			proxiedHandler.exceptionCaught(ctx, e);
		}

		/**
		 * @param ctx
		 * @param e
		 * @throws Exception
		 * @see org.jboss.netty.channel.SimpleChannelHandler#childChannelOpen(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.ChildChannelStateEvent)
		 */
		public void childChannelOpen(ChannelHandlerContext ctx,
				ChildChannelStateEvent e) throws Exception {
			proxiedHandler.childChannelOpen(ctx, e);
		}
	}
	
	@Before
	public void beforeLocalhostNetworkChannelBGPv4TestBase() throws Exception {
		serverProxyChannelHandler = obtainInstance(ProxyChannelHandler.class);

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
	
		serverPort = -1;
		serverChannel = null;
		for(int i=16000; (i<64000) && (serverPort < 0); i++) {
			try {
				serverChannel = serverBootstrap.bind(new InetSocketAddress(Inet4Address.getLocalHost(), i));
				
				serverPort = i;
			} catch(ChannelException e) {
				// ignore bind failure
			}
		}
		
		Assert.assertNotNull(serverChannel);
		Assert.assertTrue(serverPort > 0);
	}
	
	@After
	public void afterLocalhostNetworkChannelBGPv4TestBase() {
		if(serverChannel != null)
			serverChannel.close();
		serverChannel = null;
		
		if(serverBootstrap != null)
			serverBootstrap.releaseExternalResources();
		serverBootstrap = null;
	}
	
	private ServerBootstrap serverBootstrap;
	protected ProxyChannelHandler serverProxyChannelHandler;
	protected int serverPort;
	protected Channel serverChannel;
}
