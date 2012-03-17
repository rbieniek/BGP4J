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
 * File: org.bgp4j.netty.LocalhostNetworkChannelTest.java 
 */
package org.bgp4j.netty;

import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import junit.framework.Assert;

import org.bgp4j.netty.handlers.BGPv4Codec;
import org.bgp4j.netty.handlers.BGPv4Reframer;
import org.bgp4j.netty.protocol.open.OpenPacket;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.ChildChannelStateEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.socket.ClientSocketChannelFactory;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class LocalhostNetworkChannelTest extends LocalhostNetworkChannelBGPv4TestBase {

	@Before
	public void before() throws Exception {
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
	}
	
	private ClientSocketChannelFactory clientFactory;	
	private ProxyChannelHandler clientProxyHander;
	private ClientBootstrap clientBootstrap;
	private Channel clientChannel;
	
	public class OpenPingPongServerChannelHandler extends SimpleChannelHandler {

		private boolean childChannelOpened;
		
		/* (non-Javadoc)
		 * @see org.jboss.netty.channel.SimpleChannelHandler#messageReceived(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.MessageEvent)
		 */
		@Override
		public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
			OpenPacket packet = safeDowncast(safeExtractChannelEvent(e), OpenPacket.class);
			OpenPacket replyPacket = new OpenPacket();
			
			replyPacket.setProtocolVersion(packet.getProtocolVersion());
			replyPacket.setAutonomousSystem(packet.getAutonomousSystem()+1);
			replyPacket.setBgpIdentifier((int)(packet.getBgpIdentifier()+1));
			
			ctx.getChannel().write(replyPacket);
		}

		/* (non-Javadoc)
		 * @see org.jboss.netty.channel.SimpleChannelHandler#childChannelOpen(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.ChildChannelStateEvent)
		 */
		@Override
		public void childChannelOpen(ChannelHandlerContext ctx, ChildChannelStateEvent e) throws Exception {
			childChannelOpened=true;
		}

		/**
		 * @return the childChannelOpened
		 */
		public boolean isChildChannelOpened() {
			return childChannelOpened;
		}	
	}
	
	public class OpenPingPongClientChannelHandler extends SimpleChannelHandler {

		private OpenPacket openPacket;
		private Lock lock = new ReentrantLock();
		private Condition receivedCondition = lock.newCondition();
		
		public void issueWaitLock() throws InterruptedException {
			lock.lock();
			try {
				receivedCondition.await();
			} finally {
				lock.unlock();
			}
		}
		
		/* (non-Javadoc)
		 * @see org.jboss.netty.channel.SimpleChannelHandler#messageReceived(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.MessageEvent)
		 */
		@Override
		public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
			lock.lock();
			try {
				receivedCondition.signal();
				openPacket = safeDowncast(safeExtractChannelEvent(e), OpenPacket.class);
			} finally {
				lock.unlock();
			}
		}

		/**
		 * @return the openPacket
		 */
		public OpenPacket getOpenPacket() {
			return openPacket;
		}
	}
	
	@Test
	public void testOpenPacketPingPong() throws Exception {
		OpenPingPongServerChannelHandler serverHandler = new OpenPingPongServerChannelHandler();
		OpenPingPongClientChannelHandler clientHandler = new OpenPingPongClientChannelHandler();
		
		serverProxyChannelHandler.setProxiedHandler(serverHandler);
		clientProxyHander.setProxiedHandler(clientHandler);
		
		connectServer();
		
		OpenPacket pingPacket = new OpenPacket();
		
		pingPacket.setProtocolVersion(BGPv4Constants.BGP_VERSION);
		pingPacket.setAutonomousSystem(256);
		pingPacket.setBgpIdentifier(512);
		
		clientChannel.write(pingPacket);
		clientHandler.issueWaitLock();
//		Thread.sleep(1000); // TODO find better solution
		
		OpenPacket pongPacket = clientHandler.getOpenPacket();
		
		Assert.assertEquals(pingPacket.getAutonomousSystem()+1, pongPacket.getAutonomousSystem());
		Assert.assertEquals(pingPacket.getBgpIdentifier()+1, pongPacket.getBgpIdentifier());
	}
	
	private void connectServer() throws Exception {
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
}
