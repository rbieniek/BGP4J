/**
 * 
 */
package de.urb.quagga.netty;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineException;
import org.jboss.netty.channel.ChannelSink;
import org.slf4j.Logger;

/**
 * @author rainer
 *
 */
public class MockChannelSink implements ChannelSink {
	@Inject Logger log;
	
	private List<ChannelEvent> events = new LinkedList<ChannelEvent>();
	
	@Override
	public void eventSunk(ChannelPipeline pipeline, ChannelEvent e) throws Exception {
		events.add(e);
	}

	@Override
	public void exceptionCaught(ChannelPipeline pipeline, ChannelEvent e,
			ChannelPipelineException cause) throws Exception {
		log.error("caught exception", cause);
		throw cause;
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
