/**
 * 
 */
package org.bgp4j.netty.osgi;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;
import java.util.LinkedList;
import java.util.List;

import org.bgp4j.definitions.config.ServerConfigurationProvider;
import org.bgp4j.definitions.fsm.BGPv4FSMRegistry;
import org.bgp4j.definitions.peer.PeerConnectionInformationRegistry;
import org.bgp4j.net.EChannelDirection;
import org.bgp4j.netty.Attributes;
import org.bgp4j.netty.handlers.BGPv4Codec;
import org.bgp4j.netty.handlers.BGPv4Reframer;
import org.bgp4j.netty.handlers.BGPv4ServerEndpoint;
import org.bgp4j.netty.handlers.InboundOpenCapabilitiesProcessor;
import org.bgp4j.netty.handlers.PeerCollisionDetectionHandler;
import org.bgp4j.netty.handlers.UpdateAttributeChecker;
import org.bgp4j.netty.handlers.ValidateServerIdentifier;
import org.osgi.service.blueprint.container.ServiceUnavailableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author rainer
 *
 */
public class BGPv4ServerService {
	private static final Logger logger = LoggerFactory.getLogger(BGPv4ServerService.class);
	
	private static class ServerControlBlock {
		private InetSocketAddress serverAddress;
		private Channel channel;
		
		private ServerControlBlock(InetSocketAddress serverAddress, Channel channel) {
			this.serverAddress = serverAddress;
			this.channel = channel;
		}

		/**
		 * @return the serverAddress
		 */
		public InetSocketAddress getServerAddress() {
			return serverAddress;
		}

		/**
		 * @return the channel
		 */
		public Channel getChannel() {
			return channel;
		}
	}
	
	private static class ChildChannelInitializer extends ChannelInitializer<Channel> {

		@Override
		protected void initChannel(Channel ch) throws Exception {
			ch.pipeline().addLast(new BGPv4Reframer());
			ch.pipeline().addLast(new BGPv4Codec());
			ch.pipeline().addLast(new ValidateServerIdentifier());
			ch.pipeline().addLast(new InboundOpenCapabilitiesProcessor());
			ch.pipeline().addLast(new PeerCollisionDetectionHandler());
			ch.pipeline().addLast(new UpdateAttributeChecker());
			ch.pipeline().addLast(new BGPv4ServerEndpoint());
		}
		
	}
	
	private BGPv4FSMRegistry fsmRegistry;
	private ServerConfigurationProvider serverConfigurationProvider;
	private PeerConnectionInformationRegistry peerRegistry;
	
	private boolean running = false;

	private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private ChildChannelInitializer childInitializer = new ChildChannelInitializer();
    
    private List<ServerControlBlock> servers = new LinkedList<BGPv4ServerService.ServerControlBlock>();
    
	/**
	 * 
	 * @throws Exception
	 */
	public void start() throws Exception {
		logger.info("starting service");
		
		serverStart();
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	public void stop() throws Exception {
		logger.info("stopping service");
		
		serverStop();
	}

	/**
	 * @param fsmRegistry the fsmRegistry to set
	 */
	public void setFsmRegistry(BGPv4FSMRegistry fsmRegistry) {
		this.fsmRegistry = fsmRegistry;
	}
	
	/**
	 * @param serverConfigurationProvider the serverConfigurationProvider to set
	 */
	public void setServerConfigurationProvider(ServerConfigurationProvider serverConfigurationProvider) {
		this.serverConfigurationProvider = serverConfigurationProvider;
	}
		
	/**
	 * @param peerRegistry the peerRegistry to set
	 */
	public void setPeerRegistry(PeerConnectionInformationRegistry peerRegistry) {
		this.peerRegistry = peerRegistry;
	}

	public void bindServerConfigurationProvider(ServerConfigurationProvider serverConfigurationProvider) {
		logger.info("binding server configuration provider");
		
		this.serverConfigurationProvider = serverConfigurationProvider;
		
		serverStart();
	}

	public void unbindServerConfigurationProvider(ServerConfigurationProvider serverConfigurationProvider) {
		logger.info("unbinding server configuration provider");

		this.serverConfigurationProvider = null;
		
		serverStop();
	}
	
	public void bindFsmRegistry(BGPv4FSMRegistry fsmRegistry) {
		logger.info("binding finite state machine registry");
		
		this.fsmRegistry = fsmRegistry;
		
		serverStart();
	}

	public void unbindFsmRegistry(BGPv4FSMRegistry fsmRegistry) {
		logger.info("binding finite state machine registry");

		this.fsmRegistry = null;
		
		serverStop();
	}
	
	public void bindPeerConnectionInformationRegistry(PeerConnectionInformationRegistry peerRegistry) {
		logger.info("binding peer connection information registry registry");

		this.peerRegistry = peerRegistry;
		
		serverStart();
	}

	public void unbindPeerConnectionInformationRegistry(PeerConnectionInformationRegistry peerRegistry) {
		logger.info("binding peer connection information registry registry");

		this.peerRegistry = null;
		
		serverStop();
	}

	private void serverStart() {
		if(!running) {
			for(InetSocketAddress serverAddress : serverConfigurationProvider.bindAddresses()) {
				try {
					bossGroup = new NioEventLoopGroup();
				    workerGroup = new NioEventLoopGroup();
					ServerBootstrap b = new ServerBootstrap();

					b.group(bossGroup, workerGroup)
		             .channel(NioServerSocketChannel.class)
		             .childHandler(childInitializer)
					.childAttr(Attributes.channelDirectionKey, EChannelDirection.SERVER)
					.childAttr(Attributes.fsmRegistryKey, fsmRegistry)
					.childAttr(Attributes.peerConnectionInformationRegistry, peerRegistry)
		             .option(ChannelOption.SO_BACKLOG, 128)
		             .childOption(ChannelOption.SO_KEEPALIVE, true)
		             .childOption(ChannelOption.SO_LINGER, 0);			
					b.bind(serverAddress).addListener(new ChannelFutureListener() {
						
						@Override
						public void operationComplete(ChannelFuture future) throws Exception {
							if(future.isDone()) {
								servers.add(new ServerControlBlock(serverAddress, future.channel()));
							} else if(future.isCancelled()) {
								logger.error("Failed to start service when binding to {}", serverAddress, future.cause());
							}
						}
					});
					
				} catch(ServiceUnavailableException e) {
					logger.error("Required service not available when binding to {}", serverAddress, e);
				}
			}
			
			this.running = true;			
		}
	}
	
	private void serverStop() {
		if(running) {
			for(ServerControlBlock scb : servers) {
				logger.info("Closing server for bind address {}", scb.getServerAddress());
	
				try {
					scb.getChannel().close().sync();
				} catch (InterruptedException e) {
					logger.warn("Interrupted while closing down BGP server channel");
				}
			}

			try {
				bossGroup.shutdownGracefully().sync();
			} catch (InterruptedException e) {
				logger.warn("Interrupted while closing down boss worker group");
			}
			try {
				workerGroup.shutdownGracefully().sync();
			} catch (InterruptedException e) {
				logger.warn("Interrupted while closing down server worker group");
			}
			
			servers.clear();
			
			this.running = false;
		}
	}

}
