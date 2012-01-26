/**
 * 
 */
package de.urb.netty.bgp4.service;

import java.util.UUID;
import java.util.concurrent.Executors;

import javax.enterprise.event.Event;
import javax.enterprise.inject.New;
import javax.inject.Inject;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.slf4j.Logger;

import de.urb.netty.bgp4.BGPv4PeerConfiguration;
import de.urb.netty.bgp4.fsm.BGPv4FSM;
import de.urb.netty.bgp4.protocol.BGPv4Codec;
import de.urb.netty.bgp4.protocol.BGPv4Reframer;

/**
 * @author rainer
 *
 */
public class BGPv4Client {
	private @Inject Logger log;

	private @Inject @New BGPv4FSM bgp4fsm;
	private @Inject BGPv4Codec codec;
	private @Inject @New ValidateServerIdentifier validateServer;
	private @Inject BGPv4Reframer reframer;
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
