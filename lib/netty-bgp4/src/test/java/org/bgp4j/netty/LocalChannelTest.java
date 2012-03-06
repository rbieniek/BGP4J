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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
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

		serverPipeline = Channels.pipeline();
		serverChannel = serverFactory.newChannel(serverPipeline);
		serverChannel.bind(address);
		
		clientPipeline = Channels.pipeline();
		clientChannel = clientFactory.newChannel(clientPipeline);
		clientChannel.connect(address);
	}
	
	@After
	public void after() {
		address = null;
		serverPipeline = null;
		serverChannel.close();
		serverChannel = null;
		
		clientPipeline = null;
		clientChannel.close();
		clientChannel = null;
	}
	
	private static DefaultLocalClientChannelFactory clientFactory;
	private static DefaultLocalServerChannelFactory serverFactory;
	private LocalAddress address;
	private LocalChannel clientChannel;
	private LocalServerChannel serverChannel;
	private ChannelPipeline clientPipeline;
	private ChannelPipeline serverPipeline;

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
		
		serverPipeline.addLast("recording", handler);
		
		// clientPipeline.sendUpstream(new UpstreamMessageEvent(clientChannel, message, address));
		clientChannel.write(message);
		
		Iterator<Object> it = handler.getMessages().iterator();
		
		Assert.assertTrue(it.hasNext());
		Assert.assertEquals(message, it.next());
		Assert.assertFalse(it.hasNext());
	}
}
