package org.bgp4j.netty.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.LinkedList;
import java.util.List;

import org.bgp4j.net.events.BgpEvent;

public class UserEventInboundHandler extends ChannelInboundHandlerAdapter {

	private List<BgpEvent> events = new LinkedList<BgpEvent>();
	
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if(!(evt instanceof BgpEvent))
			throw new IllegalArgumentException();
		
		events.add((BgpEvent)evt);
	}
	
	public List<BgpEvent> events() {
		return this.events;
	}
	
	public BgpEvent readEvent() {
		if(this.events.isEmpty())
			return null;
		
		return this.events.remove(0);
	}
}