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
 * File: org.bgp4j.netty.LocalChannelTest.java 
 */
package org.bgp4j.netty;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.UpstreamMessageEvent;
import org.jboss.netty.channel.local.DefaultLocalClientChannelFactory;
import org.jboss.netty.channel.local.DefaultLocalServerChannelFactory;
import org.jboss.netty.channel.local.LocalAddress;
import org.jboss.netty.channel.local.LocalChannel;
import org.jboss.netty.channel.local.LocalServerChannel;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class LocalChannelTest extends BGPv4TestBase {

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
	private ParametrizableChannelPipelineFactory serverPipelineFactory;
	private ParametrizableChannelPipelineFactory clientPipelineFactory;
	private LocalAddress address;
	private ServerBootstrap serverBootstrap;
	private ClientBootstrap clientBootstrap;
	private Channel clientChannel;

	private class ParametrizableChannelPipelineFactory implements ChannelPipelineFactory {

		private ArrayList<ChannelHandler> handlers = new ArrayList<ChannelHandler>();
		
		public void addChannelHandler(ChannelHandler handler) {
			handlers.add(handler);
		}
		
		@Override
		public ChannelPipeline getPipeline() throws Exception {
			return Channels.pipeline(handlers.toArray(new ChannelHandler[0]));
		}
		
	}
	
	private class SimpleRecordingChannelHandler extends SimpleChannelHandler {

		private List<Object> messages = new LinkedList<Object>();
		
		/* (non-Javadoc)
		 * @see org.jboss.netty.channel.SimpleChannelHandler#messageReceived(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.MessageEvent)
		 */
		@Override
		public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
			messages.add(e.getMessage());
		}

		/**
		 * @return the messages
		 */
		public List<Object> getMessages() {
			return messages;
		}
		
	}
	
	@Test
	public void testSimpleWriteClientReadServer() throws Exception {
		SimpleRecordingChannelHandler handler = new SimpleRecordingChannelHandler();
		Object message = new Integer(1);
		
		serverPipelineFactory.addChannelHandler(handler);
		
		serverBootstrap.bind(address);
		clientChannel = clientBootstrap.connect(address).getChannel();

		// clientPipeline.sendUpstream(new UpstreamMessageEvent(clientChannel, message, address));
		clientChannel.write(message);
		
		Iterator<Object> it = handler.getMessages().iterator();
		
		Assert.assertTrue(it.hasNext());
		Assert.assertEquals(message, it.next());
		Assert.assertFalse(it.hasNext());
	}
}
