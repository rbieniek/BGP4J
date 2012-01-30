/**
 * 
 */
package org.bgp4j.netty.protocol;

import java.net.Inet4Address;

import org.bgp4j.netty.BGPv4Constants;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

/**
 * @author rainer
 *
 */
public class NextHopPathAttribute extends PathAttribute {

	private Inet4Address nextHop;
	
	/* (non-Javadoc)
	 * @see org.bgp4j.netty.protocol.PathAttribute#getTypeCode()
	 */
	@Override
	protected int getTypeCode() {
		return BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_NEXT_HOP;
	}

	/* (non-Javadoc)
	 * @see org.bgp4j.netty.protocol.PathAttribute#getValueLength()
	 */
	@Override
	protected int getValueLength() {
		return 4;
	}

	/* (non-Javadoc)
	 * @see org.bgp4j.netty.protocol.PathAttribute#encodeValue()
	 */
	@Override
	protected ChannelBuffer encodeValue() {
		ChannelBuffer buffer = ChannelBuffers.buffer(4);
		
		buffer.writeBytes(this.nextHop.getAddress());
		
		return buffer;
	}

	/**
	 * @return the nextHop
	 */
	public Inet4Address getNextHop() {
		return nextHop;
	}

	/**
	 * @param nextHop the nextHop to set
	 */
	public void setNextHop(Inet4Address nextHop) {
		this.nextHop = nextHop;
	}

}
