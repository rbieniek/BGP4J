/**
 * 
 */
package de.urb.quagga.netty;

import javax.inject.Inject;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.slf4j.Logger;

/**
 * @author rainer
 *
 */
public class QuaggaPacketDecoder extends SimpleChannelUpstreamHandler {

	@Inject Logger log;
	
	/* (non-Javadoc)
	 * @see org.jboss.netty.channel.SimpleChannelUpstreamHandler#messageReceived(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.MessageEvent)
	 */
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
		ChannelBuffer buffer = (ChannelBuffer)e.getMessage();
		int bytesReadable = buffer.readableBytes();
		
		if(bytesReadable < 1) {
			throw new MalformedPacketException();
		}
		
		byte markerOrCommand = buffer.readByte();
		
		if(markerOrCommand == QuaggaConstants.ZEBRA_HEADER_MARKER) {
			// possible protocal version 1 or above
		} else {
			// protocol version 0 packet
			switch(markerOrCommand) {
			case QuaggaConstants.ZEBRA_INTERFACE_ADD:
				break;
			default:
				log.info("cannot handle protocol packet, command ", markerOrCommand);
				break;
			}
		}
	}

}
