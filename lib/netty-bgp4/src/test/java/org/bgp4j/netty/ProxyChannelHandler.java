package org.bgp4j.netty;

import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.SimpleChannelHandler;

public class ProxyChannelHandler extends SimpleChannelHandler {
	
	SimpleChannelHandler proxiedHandler;

	/**
	 * @return the proxiedHandler
	 */
	public SimpleChannelHandler getProxiedHandler() {
		return proxiedHandler;
	}

	/**
	 * @param proxiedHandler the proxiedHandler to set
	 */
	public void setProxiedHandler(SimpleChannelHandler proxiedHandler) {
		this.proxiedHandler = proxiedHandler;
	}

	/**
	 * @param ctx
	 * @param e
	 * @throws Exception
	 * @see org.jboss.netty.channel.SimpleChannelHandler#handleUpstream(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.ChannelEvent)
	 */
	public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e)
			throws Exception {
		proxiedHandler.handleUpstream(ctx, e);
	}

}