/**
 * 
 */
package org.bgp4j.netty.protocol;

import java.net.SocketAddress;

import org.jboss.netty.channel.AbstractChannel;
import org.jboss.netty.channel.ChannelConfig;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.DefaultChannelConfig;

/**
 * @author rainer
 *
 */
public class MockChannel extends AbstractChannel {

	private class MockChannelConfig extends DefaultChannelConfig {
		
	}
	
	private MockChannelConfig config = new MockChannelConfig();
	
	public MockChannel(ChannelPipeline pipeline, MockChannelSink sink) {
		super(null, null, pipeline, sink);
	}
	
	/* (non-Javadoc)
	 * @see org.jboss.netty.channel.Channel#getConfig()
	 */
	@Override
	public ChannelConfig getConfig() {
		return config;
	}

	/* (non-Javadoc)
	 * @see org.jboss.netty.channel.Channel#isBound()
	 */
	@Override
	public boolean isBound() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.jboss.netty.channel.Channel#isConnected()
	 */
	@Override
	public boolean isConnected() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.jboss.netty.channel.Channel#getLocalAddress()
	 */
	@Override
	public SocketAddress getLocalAddress() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.jboss.netty.channel.Channel#getRemoteAddress()
	 */
	@Override
	public SocketAddress getRemoteAddress() {
		// TODO Auto-generated method stub
		return null;
	}

}
