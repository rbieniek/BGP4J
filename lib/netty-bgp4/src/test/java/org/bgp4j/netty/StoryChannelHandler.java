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
 * File: org.bgp4j.netty.StoryChannelHandler.java 
 */
package org.bgp4j.netty;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import junit.framework.Assert;

import org.apache.commons.lang3.StringUtils;
import org.bgp4.config.nodes.PeerConfiguration;
import org.bgp4j.netty.protocol.BGPv4Packet;
import org.bgp4j.netty.protocol.open.OpenPacket;
import org.jbehave.core.annotations.Alias;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.configuration.MostUsefulConfiguration;
import org.jbehave.core.embedder.Embedder;
import org.jbehave.core.io.CodeLocations;
import org.jbehave.core.io.LoadFromClasspath;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.steps.CandidateSteps;
import org.jbehave.core.steps.InstanceStepsFactory;
import org.jbehave.core.steps.SilentStepMonitor;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ChildChannelStateEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class StoryChannelHandler extends SimpleChannelHandler {

	public static class ChannelHandlerEmbedder extends Embedder {
		private ChannelHandlerSteps channelHandlerSteps;
		
		@Override
	    public Configuration configuration() {
	        Class<?> embedderClass = this.getClass();
	        URL codeLocation = CodeLocations.codeLocationFromClass(embedderClass);
	        Configuration configuration = new MostUsefulConfiguration()
	             .useStoryLoader(new LoadFromClasspath(embedderClass))
	             .useStoryReporterBuilder(new StoryReporterBuilder()
	                .withCodeLocation(codeLocation)
	                .withDefaultFormats())
	            .useStepMonitor(new SilentStepMonitor());
	               
	        return configuration;
	    }
		
		@Override
	    public List<CandidateSteps> candidateSteps() {
	        return new InstanceStepsFactory(configuration(), channelHandlerSteps).createCandidateSteps();
	    }
		
		/**
		 * @param channelHandlerSteps the channelHandlerSteps to set
		 */
		public void setChannelHandlerSteps(ChannelHandlerSteps channelHandlerSteps) {
			this.channelHandlerSteps = channelHandlerSteps;
			
			useCandidateSteps(candidateSteps());
		}
	}
	
	public interface ClientCallbacks {
		/**
		 * start the client
		 */
		public void startClient() throws Exception;

		/**
		 * start the client
		 */
		public void stopClient() throws Exception;
		
		/**
		 * client done
		 */
		public void clientDone();
	}
	
	public class ChannelHandlerSteps {

		private ClientCallbacks clientCallbacks;
		private PeerConfiguration serverConfiguration;
		private Lock lock;
		private Condition channelConnected;
		private Condition channelDisconnected;
		private Condition packetReceived;
		private BGPv4Packet packet;
		
		public ChannelHandlerSteps(ClientCallbacks clientCallbacks, PeerConfiguration serverConfiguration) {
			this.clientCallbacks = clientCallbacks;
			this.serverConfiguration = serverConfiguration;
			
			lock = new ReentrantLock();
			channelConnected = lock.newCondition();
			channelDisconnected = lock.newCondition();
			packetReceived = lock.newCondition();
		}

		@Given("an unbound server")
		@Alias("client is disconnected")
		public void unboundServer() {
			Assert.assertNull(serverChannel);
		}
		
		@Given("client connect after $seconds")
		public void waitForClientConnectAfter(int seconds) {
			lock.lock();
			
			try {
				channelConnected.await(seconds, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				// ignore
			} finally {
				lock.unlock();
			}
			
			Assert.assertNotNull(serverChannel);
		}
		
		@Given("client is connected")
		public void checkClientConnected() {			
			Assert.assertNotNull(serverChannel);
		}
		
		@Given("client disconnect after $seconds")
		public void waitForClientDisconnectAfter(int seconds) {
			lock.lock();
			
			try {
				channelDisconnected.await(seconds, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				// ignore
			} finally {
				lock.unlock();
			}
			
			Assert.assertNull(serverChannel);
		}
		
		@When("server waited for BGP $type after $seconds")
		public void waitForBGPPacketAfter(String type, int seconds) {
			lock.lock();
			
			try {
				packetReceived.await(seconds, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				// ignore
			} finally {
				lock.unlock();
			}
			Assert.assertNotNull(packet);
			
			Class<? extends BGPv4Packet> packetClass = null;
			
			if(StringUtils.equalsIgnoreCase("open", type))
				packetClass = OpenPacket.class;
			
			Assert.assertNotNull(packetClass);
			Assert.assertEquals(packetClass, packet.getClass());
		}

		@When("waited for $seconds")
		public void waitedFor(int seconds) {
			try {
				Thread.sleep(seconds*1000L);
			} catch (InterruptedException e) {
			}
		}
		
		@Then("start client")
		public void startClient() throws Exception {
			clientCallbacks.startClient();
		}

		@Then("stop client")
		public void stopClient() throws Exception {
			clientCallbacks.stopClient();
		}
		
		@Then("signal done to client")
		public void signalClientDone() {
			clientCallbacks.clientDone();
		}
		
		@Then("send BGP $type")
		public void sendBGP(String type) {
			ChannelFuture writeFuture = null;
			
			if(StringUtils.equalsIgnoreCase("open", type)) {
				OpenPacket packet = new OpenPacket();
				
				packet.setProtocolVersion(BGPv4Constants.BGP_VERSION);
				packet.setAutonomousSystem(serverConfiguration.getLocalAS());
				packet.setBgpIdentifier((int)serverConfiguration.getLocalBgpIdentifier());
				packet.setHoldTime(serverConfiguration.getHoldTime());
				packet.getCapabilities().addAll(serverConfiguration.getCapabilities().getRequiredCapabilities());
				packet.getCapabilities().addAll(serverConfiguration.getCapabilities().getOptionalCapabilities());
				
				Assert.assertNotNull(serverChannel);
				writeFuture = serverChannel.write(packet);
			}
				
			Assert.assertNotNull(writeFuture);
		}

		void signalChannelConnected() {
			lock.lock();
			
			try {
				channelConnected.signal();
			} finally {
				lock.unlock();
			}
		}

		void signalChannelDisconnected() {
			lock.lock();
			
			try {
				channelDisconnected.signal();
			} finally {
				lock.unlock();
			}
		}
		
		void receivePacket(BGPv4Packet packet) {
			this.packet = packet;
			
			lock.lock();
			try {
				packetReceived.signal();
			} finally {
				lock.unlock();
			}
		}
	}
	
	private Channel serverChannel;
	private ChannelHandlerSteps channelHandlerSteps;
	
	public void runStory(String storyPath, ClientCallbacks clientCallbacks, PeerConfiguration serverConfiguration) {
		ChannelHandlerEmbedder embedder = new ChannelHandlerEmbedder();
		channelHandlerSteps = new ChannelHandlerSteps(clientCallbacks, serverConfiguration);
		
		embedder.setChannelHandlerSteps(channelHandlerSteps);
		embedder.runStoriesAsPaths(Arrays.asList(new String[] { storyPath }));
	}

	/* (non-Javadoc)
	 * @see org.jboss.netty.channel.SimpleChannelHandler#childChannelOpen(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.ChildChannelStateEvent)
	 */
	@Override
	public void childChannelOpen(ChannelHandlerContext ctx, ChildChannelStateEvent e) throws Exception {
		serverChannel = e.getChildChannel();
		
		channelHandlerSteps.signalChannelConnected();
	}

	/* (non-Javadoc)
	 * @see org.jboss.netty.channel.SimpleChannelHandler#childChannelClosed(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.ChildChannelStateEvent)
	 */
	@Override
	public void childChannelClosed(ChannelHandlerContext ctx, ChildChannelStateEvent e) throws Exception {
		serverChannel = null;
		
		channelHandlerSteps.signalChannelDisconnected();
	}

	/* (non-Javadoc)
	 * @see org.jboss.netty.channel.SimpleChannelHandler#messageReceived(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.MessageEvent)
	 */
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
		if(e.getMessage() instanceof BGPv4Packet) {
			channelHandlerSteps.receivePacket((BGPv4Packet)e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see org.jboss.netty.channel.SimpleChannelHandler#channelOpen(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.ChannelStateEvent)
	 */
	@Override
	public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		serverChannel = e.getChannel();
		
		channelHandlerSteps.signalChannelConnected();		
	}

	/* (non-Javadoc)
	 * @see org.jboss.netty.channel.SimpleChannelHandler#channelClosed(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.ChannelStateEvent)
	 */
	@Override
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		serverChannel = null;
		
		channelHandlerSteps.signalChannelDisconnected();
	}
}
