/**
 * 
 */
package org.bgp4j.netty.osgi;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.bgp4j.definitions.fsm.BGPv4FSM;
import org.bgp4j.definitions.fsm.BGPv4FSMRegistry;
import org.bgp4j.definitions.fsm.BGPv4FSMState;
import org.bgp4j.definitions.peer.EPeerDirection;
import org.bgp4j.definitions.peer.PeerConnectionInformation;
import org.bgp4j.definitions.peer.PeerConnectionInformationRegistry;
import org.bgp4j.net.EChannelDirection;
import org.bgp4j.netty.Attributes;
import org.bgp4j.netty.handlers.BGPv4ClientEndpoint;
import org.bgp4j.netty.handlers.BGPv4Codec;
import org.bgp4j.netty.handlers.BGPv4Reframer;
import org.bgp4j.netty.handlers.InboundOpenCapabilitiesProcessor;
import org.bgp4j.netty.handlers.PeerCollisionDetectionHandler;
import org.bgp4j.netty.handlers.UpdateAttributeChecker;
import org.bgp4j.netty.handlers.ValidateServerIdentifier;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.ScheduleBuilder;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author rainer
 *
 */
public class BGPv4ClientService {
	private static final Logger logger = LoggerFactory.getLogger(BGPv4ClientService.class);

	private static class ClientControlBlock {
		private PeerConnectionInformation peer;
		private Channel channel;
		
		private ClientControlBlock(PeerConnectionInformation peer) {
			this.peer = peer;
		}

		/**
		 * @return the serverAddress
		 */
		public InetSocketAddress clientAddress() {
			return peer.remoteAddress();
		}

		public PeerConnectionInformation peer() {
			return peer;
		}
		
		/**
		 * @return the channel
		 */
		public Channel channel() {
			return channel;
		}

		public void channel(Channel channel) {
			this.channel = channel;
		}

	}

	private static class ClientChannelInitializer extends ChannelInitializer<Channel> {

		@Override
		protected void initChannel(Channel ch) throws Exception {
			ch.pipeline().addLast(new BGPv4Reframer());
			ch.pipeline().addLast(new BGPv4Codec());
			ch.pipeline().addLast(new ValidateServerIdentifier());
			ch.pipeline().addLast(new InboundOpenCapabilitiesProcessor());
			ch.pipeline().addLast(new PeerCollisionDetectionHandler());
			ch.pipeline().addLast(new UpdateAttributeChecker());
			ch.pipeline().addLast(new BGPv4ClientEndpoint());
		}
		
	}
	
	private class ClientStartJob implements Job {

		@Override
		public void execute(JobExecutionContext context) throws JobExecutionException {
			for(ClientControlBlock ccb : clients) {
				if(ccb.channel() == null) {
					logger.info("checking if connection for peer {} must be opened", ccb.clientAddress());
					
					BGPv4FSM serverFSM = fsmRegistry.findByPeerAddressAndDirection(ccb.clientAddress().getAddress(), EChannelDirection.SERVER);

					if(serverFSM == null || serverFSM.state() != BGPv4FSMState.Established) {
						logger.info("creating connection for peer {}", ccb.clientAddress());
						
						Bootstrap b = new Bootstrap();
						
					    b.group(workerGroup)
					    	.channel(NioSocketChannel.class)
					    	.option(ChannelOption.SO_KEEPALIVE, true)
					    	.attr(Attributes.channelDirectionKey, EChannelDirection.CLIENT)
					    	.attr(Attributes.fsmRegistryKey, fsmRegistry)
					    	.attr(Attributes.peerConnectionInformationRegistry, peerRegistry)
					    	.handler(clientInitializer);
					    
					    b.connect(ccb.clientAddress()).addListener(new ChannelFutureListener() {
							
							@Override
							public void operationComplete(ChannelFuture future) throws Exception {
								final Channel channel = future.channel();

								ccb.channel(channel);
								
								channel.closeFuture().addListener(new ChannelFutureListener() {
									
									@Override
									public void operationComplete(ChannelFuture future) throws Exception {
										Optional<ClientControlBlock> opt = clients.stream()
												.filter((n) -> n.channel.equals(channel))
												.findFirst();
										
										if(opt.isPresent()) {
											opt.get().channel(null);
										}
									}
								});
							}
						});		    	
					}
				}
			}
		}
		
	}

	private BGPv4FSMRegistry fsmRegistry;
	private PeerConnectionInformationRegistry peerRegistry;

	private boolean running = false;
    private EventLoopGroup workerGroup;
    private List<ClientControlBlock> clients = new LinkedList<BGPv4ClientService.ClientControlBlock>();
    private ClientChannelInitializer clientInitializer = new ClientChannelInitializer();
	
    private SchedulerFactory schedFac = new StdSchedulerFactory();
    private Scheduler scheduler;
    
	/**
	 * 
	 * @throws Exception
	 */
	public void start() throws Exception {
		logger.info("starting service");
		
		clientStart();
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	public void stop() throws Exception {
		logger.info("stopping service");
		
		clientStop();
	}

	/**
	 * @param fsmRegistry the fsmRegistry to set
	 */
	public void setFsmRegistry(BGPv4FSMRegistry fsmRegistry) {
		this.fsmRegistry = fsmRegistry;
	}
	
	/**
	 * @param peerRegistry the peerRegistry to set
	 */
	public void setPeerRegistry(PeerConnectionInformationRegistry peerRegistry) {
		this.peerRegistry = peerRegistry;
	}

	public void bindFsmRegistry(BGPv4FSMRegistry fsmRegistry) {
		logger.info("binding finite state machine registry");
		
		this.fsmRegistry = fsmRegistry;
		
		clientStart();
	}

	public void unbindFsmRegistry(BGPv4FSMRegistry fsmRegistry) {
		logger.info("binding finite state machine registry");

		this.fsmRegistry = null;
		
		clientStop();
	}
	
	public void bindPeerConnectionInformationRegistry(PeerConnectionInformationRegistry peerRegistry) {
		logger.info("binding peer connection information registry registry");

		this.peerRegistry = peerRegistry;
		
		clientStart();
	}

	public void unbindPeerConnectionInformationRegistry(PeerConnectionInformationRegistry peerRegistry) {
		logger.info("binding peer connection information registry registry");

		this.peerRegistry = null;
		
		clientStop();
	}

	private void clientStart() {
		if(!running) {
		    workerGroup = new NioEventLoopGroup();
		    
		    for(PeerConnectionInformation pci : peerRegistry.peers()) {
		    	if(pci.peerDirection().matches(EPeerDirection.Client)) {
		    		logger.info("creating client control block for peer {}", pci.remoteAddress());
		    		
		    		clients.add(new ClientControlBlock(pci));
		    	}
		    }
		    
		    try {
		    	scheduler = schedFac.getScheduler();

		    	JobDetail job = JobBuilder.newJob(ClientStartJob.class)
		    			.build();
		    	
		    	Trigger trigger = TriggerBuilder
		    			.newTrigger()
		    			.startNow()
		    			.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(30).repeatForever())
		    			.build();

		    	scheduler.scheduleJob(job, trigger);
		    	
		    	scheduler.start();
		    } catch(Exception e) {
		    	logger.error("failed to start clients", e);
		    }
		    		    
			this.running = true;			
		}
	}

	private void clientStop() {
		if(running) {
			try {
				if(scheduler != null)
					scheduler.shutdown();
			} catch(Exception e) {
				logger.error("problems shutting down clients", e);
			} finally {		
				scheduler = null;
			}
			
			for(ClientControlBlock ccb : clients) {
				logger.info("Closing client connection for peer {}", ccb.clientAddress());
	
				try {
					if(ccb.channel() != null)
						ccb.channel().close().await(5, TimeUnit.SECONDS);
				} catch (InterruptedException e) {
					logger.warn("Interrupted while closing down client channel to peer", ccb.clientAddress());
				}
			}			
			
			try {
				workerGroup.shutdownGracefully().await(10, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				logger.error("problems shutting down clients", e);
			} finally {
				workerGroup = null;
			}
			
			this.running = false;

		}
	}

}
