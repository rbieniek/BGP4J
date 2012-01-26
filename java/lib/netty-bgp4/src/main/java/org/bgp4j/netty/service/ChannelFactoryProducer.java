/**
 * 
 */
package org.bgp4j.netty.service;

import java.util.concurrent.Executors;

import javax.enterprise.inject.Produces;

import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

/**
 * @author rainer
 *
 */
public class ChannelFactoryProducer {

	private ChannelFactory clientFactory = new NioClientSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool());
	
	@Produces @ClientFactory ChannelFactory produceClientChannelFactory() {
		return clientFactory;
	}
}
