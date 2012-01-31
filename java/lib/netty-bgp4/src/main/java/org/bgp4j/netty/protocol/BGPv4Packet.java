/**
 * 
 */
package org.bgp4j.netty.protocol;

import org.bgp4j.netty.BGPv4Constants;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

/**
 * base class for all BGPv4 protocol packets
 * 
 * @author rainer
 *
 */
public abstract class BGPv4Packet {
	/**
	 * build a binary representation of the protocol packet
	 * 
	 * @return the encoded packet
	 */
	public ChannelBuffer encodePacket() {
		return wrapBufferHeader(encodePayload(), getType());
	}
	
	/**
	 * encode the specific packet-type payload
	 * 
	 * @return the encoded packet payload
	 */
	protected abstract ChannelBuffer encodePayload();
	
	/**
	 * obtain the BGP packet type code.
	 * 
	 * @return
	 */
	protected abstract int getType();
	
	/**
	 * wrap the BGP payload in a BGPv4 header field
	 *  
	 * @param wrapped the packet payload
	 * @param type the packet type code
	 * @return the completely assembled BGPv4 packet
	 */
	private ChannelBuffer wrapBufferHeader(ChannelBuffer wrapped, int type) {
		int wrappedSize = (wrapped != null) ? wrapped.readableBytes() : 0;
		ChannelBuffer buffer = ChannelBuffers.buffer(wrappedSize + BGPv4Constants.BGP_PACKET_HEADER_LENGTH);
		
		for(int i=0; i<BGPv4Constants.BGP_PACKET_MARKER_LENGTH; i++)
			buffer.writeByte(0xff);
		
		buffer.writeShort(wrappedSize + BGPv4Constants.BGP_PACKET_HEADER_LENGTH);
		buffer.writeByte(type);
		
		if(wrapped != null)
			buffer.writeBytes(wrapped);
		
		return buffer;
	}

}
