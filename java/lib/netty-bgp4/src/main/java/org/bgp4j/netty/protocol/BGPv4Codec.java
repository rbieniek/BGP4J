package org.bgp4j.netty.protocol;


import javax.inject.Inject;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.DownstreamMessageEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.UpstreamMessageEvent;
import org.slf4j.Logger;


/**
 * Protocol codec which translates between protocol network packets and protocol POJOs 
 * 
 * @author rainer
 *
 */
public class BGPv4Codec extends SimpleChannelHandler {
	private @Inject Logger log;
	private @Inject BGPv4PacketDecoder packetDecoder;

	/**
	 * Upstream handler which takes care of the network packet to POJO translation
	 * 
	 * @param ctx the channel handler context
	 */
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
		if(e.getMessage() instanceof ChannelBuffer) {
			ChannelBuffer buffer = (ChannelBuffer)e.getMessage();
			
			try {
				BGPv4Packet packet = packetDecoder.decodePacket(buffer);
				
				if(packet != null) {
					ctx.sendUpstream(new UpstreamMessageEvent(e.getChannel(), packet, e.getRemoteAddress()));
				}
			} catch(MessageLengthException ex) {
				log.error("received malformed protocol packet, closing connection", ex);

				NotificationHelper.sendNotificationAndCloseChannel(ctx, new BadMessageLengthNotificationPacket(ex.getLength()));
			} catch(ProtocolTypeException ex) {
				log.error("received malformed protocol packet, closing connection", ex);

				NotificationHelper.sendNotificationAndCloseChannel(ctx, new BadMessageTypeNotificationPacket(ex.getType()));
			} catch(ProtocolPacketFormatException ex) {
				log.error("received malformed protocol packet, closing connection", ex);
				
				ctx.getChannel().close();
			} catch(Exception ex) {
				log.error("generic decoding exception, closing connection", ex);
				
				ctx.getChannel().close();
			}
		} else {
			log.error("expected a {} message payload, got a {} message payload", 
					ChannelBuffer.class.getName(), 
					e.getMessage().getClass().getName()); 
		}
	}

	/**
	 * Downstream handler which takes care of the POJO to network packet translation
	 */
	@Override
	public void writeRequested(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
		if(e.getMessage() instanceof BGPv4Packet) {
			ChannelBuffer buffer = ((BGPv4Packet)e.getMessage()).encodePacket();
						
			if(buffer != null) {
				ctx.sendDownstream(new DownstreamMessageEvent(e.getChannel(), e.getFuture(), buffer, e.getRemoteAddress()));
			}
		} else {
			log.error("expected a {} message payload, got a {} message payload", 
					BGPv4Packet.class.getName(), 
					e.getMessage().getClass().getName()); 
		}
	}

}
