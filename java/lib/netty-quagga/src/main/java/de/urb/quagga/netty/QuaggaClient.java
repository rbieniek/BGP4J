/**
 * 
 */
package de.urb.quagga.netty;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

/**
 * @author rainer
 *
 */
public class QuaggaClient {
	private static final Log log = LogFactory.getLog(QuaggaClient.class);
	
	public static final int DEFAULT_ZEBRA_PORT = 2600;
		
	private int zebraPort = DEFAULT_ZEBRA_PORT;

	private Channel clientChannel;
	private ChannelFactory channelFactory;

	public void startClient() throws Exception {
		channelFactory = new NioClientSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool());
		
		ClientBootstrap bootstrap = new ClientBootstrap(channelFactory);
		
		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			
			@Override
			public ChannelPipeline getPipeline() throws Exception {
				return Channels.pipeline(new QuaggaChannelHandler());
			}
		});
		
		bootstrap.setOption("tcpnoDelay", true);
		bootstrap.setOption("keepAlive", true);
		
		ChannelFuture future = bootstrap.connect(new InetSocketAddress(InetAddress.getLocalHost(), this.zebraPort));
		boolean connected = false;
		
		while(!connected) {
			try {
				future = future.await();
			} catch(InterruptedException e) {
				log.info("caught interrupt", e);
			}
			
			if(future.isDone()) {
				if(future.isSuccess()) {
					connected = true;
				} else {
					log.warn("Cannot connect to zebra server", future.getCause());
					
					// sleep for one second and retry
					try {
						Thread.sleep(1000);
					} catch(InterruptedException e) {}
				}
			}
		}
		
		this.clientChannel = future.getChannel();
	}
	
	public void stopClient() {
		if(clientChannel != null) {
			clientChannel.getCloseFuture().awaitUninterruptibly();
			
			channelFactory.releaseExternalResources();
		}
	}
	
	/**
	 * @return the zebraPort
	 */
	public int getZebraPort() {
		return zebraPort;
	}

	/**
	 * @param zebraPort the zebraPort to set
	 */
	public void setZebraPort(int zebraPort) {
		this.zebraPort = zebraPort;
	}
	
}
