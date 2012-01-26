package org.bgp4j.netty.protocol;


import javax.inject.Inject;

import org.bgp4j.netty.BGPv4Constants;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
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

	/**
	 * Upstream handler which takes care of the network packet to POJO translation
	 * 
	 * @param ctx the channel handler context
	 */
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
		if(e.getMessage() instanceof ChannelBuffer) {
			ChannelBuffer buffer = (ChannelBuffer)e.getMessage();
			int type = buffer.readUnsignedByte();
			BGPv4Packet packet = null;
			
			try {
				switch (type) {
				case BGPv4Constants.BGP_PACKET_TYPE_OPEN:
					packet = decodeOpenPacket(buffer);
					break;
				case BGPv4Constants.BGP_PACKET_TYPE_UPDATE:
					break;
				case BGPv4Constants.BGP_PACKET_TYPE_NOTIFICATION:
					break;
				case BGPv4Constants.BGP_PACKET_TYPE_KEEPALIVE:
					packet = decodeKeepalivePacket(buffer);
					break;
				case BGPv4Constants.BGP_PACKET_TYPE_ROUTE_REFRESH:
					break;
				}
			} catch(ProtocolPacketFormatException ex) {
				log.error("received malformed protocol packet, closing connection", ex);
				
				ctx.getChannel().close();
			}
			
			if(packet != null) {
				ctx.sendUpstream(new UpstreamMessageEvent(e.getChannel(), packet, e.getRemoteAddress()));
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
			ChannelBuffer buffer = null;
			BGPv4Packet packet = (BGPv4Packet)e.getMessage();
			
			if(packet instanceof OpenPacket)
				buffer = encodeOpenPacket((OpenPacket)packet);
			else if(packet instanceof KeepalivePacket)
				buffer = encodeKeepalivePacket((KeepalivePacket)packet);
			
			if(buffer != null) {
				ctx.sendDownstream(new DownstreamMessageEvent(e.getChannel(), e.getFuture(), buffer, e.getRemoteAddress()));
			}
		} else {
			log.error("expected a {} message payload, got a {} message payload", 
					BGPv4Packet.class.getName(), 
					e.getMessage().getClass().getName()); 
		}
	}

	/**
	 * decode the OPEN network packet. The OPEN packet must be at least 10 octets large at this point.
	 * 
	 * @param buffer the buffer containing the data. 
	 * @return
	 */
	private OpenPacket decodeOpenPacket(ChannelBuffer buffer) {
		OpenPacket packet = new OpenPacket();
		
		verifyPacketSize(buffer, BGPv4Constants.BGP_PACKET_MIN_SIZE_OPEN, -1);
		
		return packet;
	}

	/**
	 * encode the OPEN network packet.
	 * 
	 * @param packet the packet to encode
	 * @return the assembled packet
	 */
	private ChannelBuffer encodeOpenPacket(OpenPacket packet) {
		ChannelBuffer buffer = ChannelBuffers.buffer(BGPv4Constants.BGP_PACKET_MAX_LENGTH);

		return wrapBufferHeader(buffer, BGPv4Constants.BGP_PACKET_TYPE_OPEN);
	}
	
	/**
	 * decode the KEEPALIVE network packet. The OPEN packet must be exactly 0 octets large at this point.
	 * 
	 * @param buffer the buffer containing the data. 
	 * @return
	 */
	private KeepalivePacket decodeKeepalivePacket(ChannelBuffer buffer) {
		KeepalivePacket packet = new KeepalivePacket();
		
		verifyPacketSize(buffer, BGPv4Constants.BGP_PACKET_SIZE_KEEPALIVE, BGPv4Constants.BGP_PACKET_SIZE_KEEPALIVE);
		
		return packet;
	}

	/**
	 * encode the KEEPALIVE network packet.
	 * 
	 * @param packet the packet to encode
	 * @return the assembled packet
	 */
	private ChannelBuffer encodeKeepalivePacket(KeepalivePacket packet) {
		ChannelBuffer buffer = ChannelBuffers.buffer(0);
		
		return wrapBufferHeader(buffer, BGPv4Constants.BGP_PACKET_TYPE_KEEPALIVE);
	}
	
	/**
	 * verify the packet size.
	 * 
	 * @param minimumPacketSize the minimum size in octets the protocol packet must have to be well-formed. If <b>-1</b> is passed the check is not performed.
	 * @param maximumPacketSize the maximum size in octets the protocol packet may have to be well-formed. If <b>-1</b> is passed the check is not performed.
	 */
	private void verifyPacketSize(ChannelBuffer buffer, int minimumPacketSize, int maximumPacketSize) {
		if(minimumPacketSize != -1) {
			if(buffer.readableBytes() < (minimumPacketSize - BGPv4Constants.BGP_PACKET_HEADER_LENGTH)) {
				throw new ProtocolPacketFormatException("expected minimum " + (minimumPacketSize - BGPv4Constants.BGP_PACKET_HEADER_LENGTH) 
						+ "octest, received " + buffer.readableBytes() + "octets");
			}
		}
		if(maximumPacketSize != -1) {
			if(buffer.readableBytes() > (maximumPacketSize - BGPv4Constants.BGP_PACKET_HEADER_LENGTH)) {
				throw new ProtocolPacketFormatException("expected maximum " + (maximumPacketSize - BGPv4Constants.BGP_PACKET_HEADER_LENGTH) 
						+ "octest, received " + buffer.readableBytes() + "octets");
			}
		}
	}
	
	/**
	 * wrap the BGP payload in a BGPv4 header field
	 *  
	 * @param wrapped the packet payload
	 * @param type the packet type code
	 * @return the completely assembled BGPv4 packet
	 */
	private ChannelBuffer wrapBufferHeader(ChannelBuffer wrapped, int type) {
		ChannelBuffer buffer = ChannelBuffers.buffer(wrapped.readableBytes() + BGPv4Constants.BGP_PACKET_HEADER_LENGTH);
		
		for(int i=0; i<BGPv4Constants.BGP_PACKET_MARKER_LENGTH; i++)
			buffer.writeByte(0xff);
		
		buffer.writeShort(wrapped.readableBytes() + BGPv4Constants.BGP_PACKET_HEADER_LENGTH);
		buffer.writeBytes(wrapped);
		
		return buffer;
	}
}
