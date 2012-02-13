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

import java.net.InetAddress;
import java.util.UUID;

import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.bgp4j.netty.BGPv4PeerConfiguration;
import org.bgp4j.netty.handlers.BGPv4ClientEndpoint;
import org.bgp4j.netty.handlers.BGPv4Codec;
import org.bgp4j.netty.handlers.BGPv4Reframer;
import org.bgp4j.netty.handlers.InboundOpenCapabilitiesProcessor;
import org.bgp4j.netty.handlers.ValidateServerIdentifier;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
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
	private @Inject Event<ClientNeedReconnectEvent> reconnectEvent;
	private @Inject @ClientFactory ChannelFactory channelFactory;
	
	private Channel clientChannel;
	private BGPv4PeerConfiguration peerConfiguration;
	private boolean closed = false;

	void startClient() {
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
		
		log.info("connecting: " + printablePeer());
		ChannelFuture future = bootstrap.connect(peerConfiguration.getRemotePeerAddress());

		future.addListener(new ChannelFutureListener() {
			
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				if(future.isSuccess()) {
					clientChannel = future.getChannel();
					
					clientChannel.getCloseFuture().addListener(new ChannelFutureListener() {
						
						@Override
						public void operationComplete(ChannelFuture future) throws Exception {
							log.info("connection closed: " + printablePeer());

							if(!closed) {
								reconnectEvent.fire(new ClientNeedReconnectEvent(getRemotePeerAddress()));
							}
						}
					});

					log.info("connected: " + printablePeer());
				} else {
					log.info("cant connect: " + printablePeer());

					if(!closed)
						reconnectEvent.fire(new ClientNeedReconnectEvent(getRemotePeerAddress()));					
				}
			}
		});		
	}

	void stopClient() {
		closed = true;
		
		if(clientChannel != null) {
			clientChannel.close();
			this.clientChannel = null;
		}
	}
	
	/**
	 * @return the peerConfiguration
	 */
	public BGPv4PeerConfiguration getPeerConfiguration() {
		return peerConfiguration;
	}

	/**
	 * @param peerConfiguration the peerConfiguration to set
	 */
	public void setPeerConfiguration(BGPv4PeerConfiguration peerConfiguration) {
		this.peerConfiguration = peerConfiguration;
	}

	public InetAddress getRemotePeerAddress() {
		return getPeerConfiguration().getRemotePeerAddress().getAddress();
	}
	
	private String printablePeer() {
		StringBuilder builder = new StringBuilder();
		
		builder.append("Peer address: ");
		builder.append(peerConfiguration.getRemotePeerAddress().getAddress());
		
		builder.append("| local BGP identifier: ");
		builder.append(peerConfiguration.getLocalBgpIdentifier());		
		builder.append(", local AS: ");
		builder.append(peerConfiguration.getLocalAutonomousSystem());

		builder.append("--> remote BGP identifier: ");
		builder.append(peerConfiguration.getRemoteBgpIdentitifer());		
		builder.append(", remote AS: ");
		builder.append(peerConfiguration.getRemoteAutonomousSystem());
		
		return builder.toString();
	}
}
