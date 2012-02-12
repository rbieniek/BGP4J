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

import java.util.UUID;

import javax.enterprise.event.Event;
import javax.enterprise.inject.New;
import javax.inject.Inject;

import org.bgp4j.netty.BGPv4PeerConfiguration;
import org.bgp4j.netty.fsm.BGPv4FSM;
import org.bgp4j.netty.handlers.BGPv4Codec;
import org.bgp4j.netty.handlers.BGPv4Reframer;
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

	private @Inject @New BGPv4FSM bgp4fsm;
	private @Inject BGPv4Codec codec;
	private @Inject ValidateServerIdentifier validateServer;
	private @Inject BGPv4Reframer reframer;
	private @Inject DuplicateConnectionBlocker duplicateBlocker;
	private @Inject Event<ClientNeedReconnectEvent> reconnectEvent;
	private @Inject @ClientFactory ChannelFactory channelFactory;
	
	private Channel clientChannel;
	private BGPv4PeerConfiguration peerConfiguration;
	private String clientUuid = UUID.randomUUID().toString();
	private boolean closed = false;

	void startClient() {
		validateServer.setConfiguration(peerConfiguration);
		
		ClientBootstrap bootstrap = new ClientBootstrap(channelFactory);

		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			
			@Override
			public ChannelPipeline getPipeline() throws Exception {
				return Channels.pipeline(
						duplicateBlocker,
						reframer,
						codec,
						validateServer,
						bgp4fsm
						);
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
								reconnectEvent.fire(new ClientNeedReconnectEvent(clientUuid));
							}
						}
					});

					log.info("connected: " + printablePeer());
				} else {
					log.info("cant connect: " + printablePeer());

					if(!closed)
						reconnectEvent.fire(new ClientNeedReconnectEvent(clientUuid));					
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

	/**
	 * @return the clientUuid
	 */
	public String getClientUuid() {
		return clientUuid;
	}
	
	private String printablePeer() {
		StringBuilder builder = new StringBuilder();
		
		builder.append("client UUID: ");
		builder.append(clientUuid);
		
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
