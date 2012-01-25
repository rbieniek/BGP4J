/**
 * 
 */
package de.urb.netty.bgp4.client;

import javax.inject.Inject;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.slf4j.Logger;

import de.urb.netty.bgp4.protocol.OpenPacket;

/**
 * This channel handler checks if the OPEN packet contains the configured remote BGP identifier and autonomous system. If the configured
 * and the received BGP identifier or the configured and the received autonomous system do not match, the connection is closed.
 * The peer identifier verification is moved out of the finite state machine to make it generic for both client and server case.
 * 
 * @author rainer
 *
 */
public class ValidateServerIdentifier extends SimpleChannelUpstreamHandler {
	private @Inject Logger log;

	private BGPv4ClientConfiguration configuration;
	private boolean isClosed;
	
	/* (non-Javadoc)
	 * @see org.jboss.netty.channel.SimpleChannelUpstreamHandler#messageReceived(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.MessageEvent)
	 */
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
		boolean doClose = false;

		if(isClosed) {
			log.info("channel is closed, dropping packet {}", e.getMessage().getClass().getName());
			
			return;
		}
		
		if(e.getMessage() instanceof OpenPacket) {
			OpenPacket openPacket = (OpenPacket)e.getMessage();
			
			if(openPacket.getBgpIdentifier() != configuration.getRemoteBgpIdentitifer()) {	
				log.error("expected remote BGP identifier {}, received BGP identifier {}", configuration.getRemoteBgpIdentitifer(), openPacket.getBgpIdentifier());
				
				doClose = true;
			}

			if(openPacket.getAutonomuosSystem() != configuration.getRemoteAutonomousSystem()) {	
				log.error("expected remote autonomous systemr {}, received autonomous system {}", 
						configuration.getRemoteAutonomousSystem(), 
						openPacket.getAutonomuosSystem());
				
				doClose = true;
			}
		}
		
		if(doClose) {
			isClosed = true;
			
			ctx.getChannel().close();
		} else {
			ctx.sendUpstream(e);
		}
	}


	/**
	 * @param configuration the configuration to set
	 */
	public void setConfiguration(BGPv4ClientConfiguration configuration) {
		this.configuration = configuration;
	}

}
