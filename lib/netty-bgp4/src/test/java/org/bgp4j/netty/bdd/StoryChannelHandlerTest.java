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
 * File: org.bgp4j.netty.StoryChannelHandlerTest.java 
 */
package org.bgp4j.netty.bdd;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import junit.framework.Assert;

import org.bgp4.config.nodes.PeerConfiguration;
import org.bgp4j.netty.BGPv4Constants;
import org.bgp4j.netty.LocalhostNetworkChannelBGPv4ClientTestBase;
import org.bgp4j.netty.MessageRecordingChannelHandler;
import org.bgp4j.netty.protocol.open.OpenPacket;
import org.junit.Test;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class StoryChannelHandlerTest extends LocalhostNetworkChannelBGPv4ClientTestBase {

	// -- test methods
	@Test
	public void testBgpReceiveOpen() throws Exception {
		StoryChannelHandler serverHandler = new StoryChannelHandler();
		MessageRecordingChannelHandler clientHandler = new MessageRecordingChannelHandler();
		PeerConfiguration clientConfiguration = loadConfiguration("org/bgp4j/netty/bdd/StoryChannelHandler-Config-With-BgpPeers.xml").getPeer("client");
		
		serverProxyChannelHandler.setProxiedHandler(serverHandler);
		clientProxyHander.setProxiedHandler(clientHandler);

		final Lock lock = new ReentrantLock();
		final Condition doneCondition = lock.newCondition();
		final AtomicBoolean done = new AtomicBoolean(false);
		
		serverHandler.runStory("org/bgp4j/netty/bdd/story-channel-handler-wait-for-client-open.story", 
				new StoryChannelHandler.ClientCallbacks() {
			
			@Override
			public void stopClient() throws Exception {
				clientChannel.close();
			}
			
			@Override
			public void startClient() throws Exception {
				connectServer();
			}

			@Override
			public void clientDone() {
				done.set(true);
				lock.lock();
				
				try {
					doneCondition.signal();
				} finally {
					lock.unlock();
				}
			}
		}, loadConfiguration("org/bgp4j/netty/bdd/StoryChannelHandler-Config-With-BgpPeers.xml").getPeer("server"));
		
		lock.lock();
		try {
			doneCondition.await(5, TimeUnit.SECONDS);
		} finally {
			lock.unlock();
		}
		
		Assert.assertTrue(done.get());
		
		OpenPacket serverPacket = safeDowncast(safeExtractChannelEvent(clientHandler.nextEvent(clientChannel)), OpenPacket.class);
		
		Assert.assertEquals(clientConfiguration.getRemoteAS(), serverPacket.getAutonomousSystem());
		Assert.assertEquals(clientConfiguration.getRemoteBgpIdentifier() , serverPacket.getBgpIdentifier());
	}
	
	@Test
	public void testBgpEchoOpen() throws Exception {
		StoryChannelHandler serverHandler = new StoryChannelHandler();
		MessageRecordingChannelHandler clientHandler = new MessageRecordingChannelHandler();
		final PeerConfiguration clientConfiguration = loadConfiguration("org/bgp4j/netty/bdd/StoryChannelHandler-Config-With-BgpPeers.xml").getPeer("client");
		
		serverProxyChannelHandler.setProxiedHandler(serverHandler);
		clientProxyHander.setProxiedHandler(clientHandler);

		final Lock lock = new ReentrantLock();
		final Condition doneCondition = lock.newCondition();
		final AtomicBoolean done = new AtomicBoolean(false);
		
		serverHandler.runStory("org/bgp4j/netty/bdd/story-channel-handler-echo-open.story", 
				new StoryChannelHandler.ClientCallbacks() {
			
			@Override
			public void stopClient() throws Exception {
				clientChannel.close();
			}
			
			@Override
			public void startClient() throws Exception {
				connectServer();
				
				OpenPacket openPacket = new OpenPacket();
				
				openPacket.setProtocolVersion(BGPv4Constants.BGP_VERSION);
				openPacket.setBgpIdentifier((int)clientConfiguration.getLocalBgpIdentifier());
				openPacket.setAutonomousSystem(clientConfiguration.getLocalAS());
				
				clientChannel.write(openPacket);
			}

			@Override
			public void clientDone() {
				done.set(true);
				lock.lock();
				
				try {
					doneCondition.signal();
				} finally {
					lock.unlock();
				}
			}
		}, loadConfiguration("org/bgp4j/netty/bdd/StoryChannelHandler-Config-With-BgpPeers.xml").getPeer("server"));
		
		lock.lock();
		try {
			doneCondition.await(5, TimeUnit.SECONDS);
		} finally {
			lock.unlock();
		}
		
		Assert.assertTrue(done.get());

		OpenPacket serverPacket = safeDowncast(safeExtractChannelEvent(clientHandler.nextEvent(clientChannel)), OpenPacket.class);
		
		Assert.assertEquals(clientConfiguration.getRemoteAS(), serverPacket.getAutonomousSystem());
		Assert.assertEquals(clientConfiguration.getRemoteBgpIdentifier() , serverPacket.getBgpIdentifier());
	}

}
