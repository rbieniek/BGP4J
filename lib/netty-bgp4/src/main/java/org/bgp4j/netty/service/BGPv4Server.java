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
 */
package org.bgp4j.netty.service;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import org.bgp4.config.global.ApplicationConfiguration;
import org.bgp4j.netty.handlers.BGPv4Codec;
import org.bgp4j.netty.handlers.BGPv4Reframer;
import org.bgp4j.netty.handlers.BGPv4ServerEndpoint;
import org.bgp4j.netty.handlers.InboundOpenCapabilitiesProcessor;
import org.bgp4j.netty.handlers.ValidateServerIdentifier;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.slf4j.Logger;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class BGPv4Server {

	private @Inject Logger log;
	private @Inject ApplicationConfiguration applicationConfiguration;
	private @Inject BGPv4ServerEndpoint serverEndpoint;
	private @Inject BGPv4Codec codec;
	private @Inject InboundOpenCapabilitiesProcessor inboundOpenCapProcessor;
	private @Inject ValidateServerIdentifier validateServer;
	private @Inject BGPv4Reframer reframer;
	private Channel serverChannel;
	private ChannelFactory serverChannelFactory;

	public void startServer() {
		serverChannelFactory = new NioServerSocketChannelFactory(
				Executors.newCachedThreadPool(),
				Executors.newCachedThreadPool());
				    
		ServerBootstrap bootstrap = new ServerBootstrap(serverChannelFactory);
				    
		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			
			@Override
			public ChannelPipeline getPipeline() throws Exception {
				ChannelPipeline pipeline = Channels.pipeline();
				
				pipeline.addLast(BGPv4Reframer.HANDLER_NAME, reframer);
				pipeline.addLast(BGPv4Codec.HANDLER_NAME, codec);
				pipeline.addLast(InboundOpenCapabilitiesProcessor.HANDLER_NAME, inboundOpenCapProcessor);
				pipeline.addLast(ValidateServerIdentifier.HANDLER_NAME, validateServer);
				pipeline.addLast(BGPv4ServerEndpoint.HANDLER_NAME, serverEndpoint);
				
				return pipeline;
			}
		});
		
		bootstrap.setOption("child.tcpNoDelay", true);
		bootstrap.setOption("child.keepAlive", true);
		
		InetSocketAddress serverSocketAddress = applicationConfiguration.getBgpServerConfiguration().getServerConfiguration().getListenAddress();
		
		log.info("starting locall server on " + serverSocketAddress);
		serverChannel = bootstrap.bind(serverSocketAddress);
	}
	
	public void stopServer() {
		log.info("closing all child connections");
		serverEndpoint.getTrackedChannels().close().awaitUninterruptibly();

		if(serverChannel != null) {
			log.info("stopping local server");
			
			serverChannel.close();
			serverChannel.getCloseFuture().awaitUninterruptibly();
		}

		log.info("cleaning up server resources");
		serverChannelFactory.releaseExternalResources();
	}

}
