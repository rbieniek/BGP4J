/**
 * 
 */
package de.urb.quagga.netty;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.slf4j.Logger;

/**
 * @author rainer
 *
 */
public class MockChannelHandler extends SimpleChannelHandler {
	@Inject Logger log;

	private List<ChannelEvent> events = new LinkedList<ChannelEvent>();
	
	/* (non-Javadoc)
	 * @see org.jboss.netty.channel.SimpleChannelHandler#messageReceived(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.MessageEvent)
	 */
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		events.add(e);
	}

	/**
	 * @return the events
	 */
	public List<ChannelEvent> getEvents() {
		return events;
	}

	@SuppressWarnings("unchecked")
	public <T extends ChannelEvent> T nextEvent() {
		if(!events.isEmpty())
			return (T)events.remove(0);
		else
			return null;
	}
}
