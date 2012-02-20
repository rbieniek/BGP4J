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

import javax.inject.Inject;

import org.bgp4.config.nodes.PeerConfiguration;
import org.bgp4j.netty.handlers.BGPv4ClientEndpoint;
import org.bgp4j.netty.handlers.BGPv4Codec;
import org.bgp4j.netty.handlers.BGPv4Reframer;
import org.bgp4j.netty.handlers.InboundOpenCapabilitiesProcessor;
import org.bgp4j.netty.handlers.ValidateServerIdentifier;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.slf4j.Logger;


/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class BGPv4Client {
	private @Inject Logger log;

	private @Inject BGPv4ClientEndpoint clientEndpoint;
	private @Inject BGPv4Codec codec;
	private @Inject InboundOpenCapabilitiesProcessor inboundOpenCapProcessor;
	private @Inject ValidateServerIdentifier validateServer;
	private @Inject BGPv4Reframer reframer;
	private @Inject @ClientFactory ChannelFactory channelFactory;
	
	private Channel clientChannel;

	public ChannelFuture startClient(PeerConfiguration peerConfiguration) {
		ClientBootstrap bootstrap = new ClientBootstrap(channelFactory);

		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			
			@Override
			public ChannelPipeline getPipeline() throws Exception {
				ChannelPipeline pipeline = Channels.pipeline();
				
				pipeline.addLast(BGPv4Reframer.HANDLER_NAME, reframer);
				pipeline.addLast(BGPv4Codec.HANDLER_NAME, codec);
				pipeline.addLast(InboundOpenCapabilitiesProcessor.HANDLER_NAME, inboundOpenCapProcessor);
				pipeline.addLast(ValidateServerIdentifier.HANDLER_NAME, validateServer);
				pipeline.addLast(BGPv4ClientEndpoint.HANDLER_NAME, clientEndpoint);
				
				return pipeline;
			}
		});

		bootstrap.setOption("tcpnoDelay", true);
		bootstrap.setOption("keepAlive", true);
		
		log.info("connecting remote peer " + peerConfiguration.getPeerName() 
				+ " with address " + peerConfiguration.getClientConfig().getRemoteAddress());
		
		return bootstrap.connect(peerConfiguration.getClientConfig().getRemoteAddress());
	}

	public void stopClient() {
		if(clientChannel != null) {
			clientChannel.close();
			this.clientChannel = null;
		}
	}

	/**
	 * @return the clientChannel
	 */
	public Channel getClientChannel() {
		return clientChannel;
	}
}
