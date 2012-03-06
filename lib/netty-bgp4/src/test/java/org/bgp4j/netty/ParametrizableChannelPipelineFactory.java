package org.bgp4j.netty;

import java.util.ArrayList;

import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;

public  class ParametrizableChannelPipelineFactory implements ChannelPipelineFactory {

	private ArrayList<ChannelHandler> handlers = new ArrayList<ChannelHandler>();
	
	public void addChannelHandler(ChannelHandler handler) {
		handlers.add(handler);
	}
	
	@Override
	public ChannelPipeline getPipeline() throws Exception {
		return Channels.pipeline(handlers.toArray(new ChannelHandler[0]));
	}
	
}