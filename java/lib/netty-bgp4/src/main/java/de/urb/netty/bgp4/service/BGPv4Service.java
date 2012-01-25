/**
 * 
 */
package de.urb.netty.bgp4.service;

import java.util.concurrent.Executors;

import javax.enterprise.inject.New;
import javax.inject.Inject;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
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
public class BGPv4Service {
	private @Inject Logger log;

	private @Inject @New BGPv4FSM bgp4fsm;
	private @Inject @New BGPv4Codec codec;
	private @Inject @New ValidateServerIdentifier validateServer;
	private @Inject BGPv4Reframer reframer;
	
	private Channel clientChannel;
	private ChannelFactory channelFactory;

	public void startClient(BGPv4PeerConfiguration configuration) {
		validateServer.setConfiguration(configuration);
		
		channelFactory = new NioClientSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool());
		
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
		
		boolean connected = false;
		
		while(!connected) {
			ChannelFuture future = bootstrap.connect(configuration.getBgpv4Server());

			try {
				future = future.await();
			} catch(InterruptedException e) {
				log.info("caught interrupt", e);
			}
			
			if(future.isDone()) {
				if(future.isSuccess()) {
					connected = true;
					this.clientChannel = future.getChannel();
				} else {
					log.warn("Cannot connect to zebra server", future.getCause());
					
					// sleep for one second and retry
					try {
						Thread.sleep(1000);
					} catch(InterruptedException e) {}
				}
			}
		}
	}

	public void stopClient() {
		if(clientChannel != null) {
			clientChannel.close().awaitUninterruptibly();
			this.clientChannel = null;
			
			channelFactory.releaseExternalResources();
			this.channelFactory = null;
		}
	}
	
	public void waitForChannelClose() {
		if(this.clientChannel != null) {
			this.clientChannel.getCloseFuture().awaitUninterruptibly();
		}
	}
}
