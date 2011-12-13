/**
 * 
 */
package de.urb.quagga.netty;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.replay.ReplayingDecoder;
import org.jboss.netty.handler.codec.replay.VoidEnum;

/**
 * @author rainer
 *
 */
public class QuaggaPacketReframer extends ReplayingDecoder<VoidEnum> {

	/* (non-Javadoc)
	 * @see org.jboss.netty.handler.codec.replay.ReplayingDecoder#decode(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.Channel, org.jboss.netty.buffer.ChannelBuffer, java.lang.Enum)
	 */
	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel,
			ChannelBuffer buffer, VoidEnum state) throws Exception {
		return buffer.readBytes(buffer.readShort() - 2);
	}

}
