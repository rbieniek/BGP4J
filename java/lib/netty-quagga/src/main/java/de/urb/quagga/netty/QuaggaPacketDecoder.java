/**
 * 
 */
package de.urb.quagga.netty;

import javax.inject.Inject;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.UpstreamMessageEvent;
import org.slf4j.Logger;

import de.urb.quagga.netty.protocol.QuaggaPacket;
import de.urb.quagga.netty.protocol.ZServAddInterfacePacket;

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
		QuaggaPacket result = null;
		
		if(bytesReadable < 1) {
			throw new MalformedPacketException();
		}
		
		byte markerOrCommand = buffer.readByte();
		
		if(markerOrCommand == QuaggaConstants.ZEBRA_HEADER_MARKER) {
			// possible protocal version 1 or above
			bytesReadable = buffer.readableBytes();
			
			if(bytesReadable < 3)
				throw new MalformedPacketException();
			
			byte version = buffer.readByte();
			if(version != QuaggaConstants.ZEBRA_PROTOCOL_VERSION)
				throw new UnsupportedProtocolVersionException(version);
			
			short command = buffer.readShort();

			// protocol version 0 packet
			switch(command) {
			case QuaggaConstants.ZEBRA_INTERFACE_ADD:
				result = decodeZServAddInterfacePacket(buffer);
				break;
			default:
				log.info("cannot handle protocol packet, command ", command);
				break;
			}

		} else {
			// protocol version 0 packet
			switch(markerOrCommand) {
			case QuaggaConstants.ZEBRA_INTERFACE_ADD:
				result = decodeZServAddInterfacePacket(buffer);
				break;
			default:
				log.info("cannot handle protocol packet, command ", markerOrCommand);
				break;
			}
		}
		
		if(result != null)
			ctx.sendUpstream(new UpstreamMessageEvent(e.getChannel(), result, e.getRemoteAddress()));
	}

	private ZServAddInterfacePacket decodeZServAddInterfacePacket(ChannelBuffer buffer) {
		return null;
	}
}
