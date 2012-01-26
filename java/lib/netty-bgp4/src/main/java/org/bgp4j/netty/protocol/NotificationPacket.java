/**
 * 
 */
package org.bgp4j.netty.protocol;

import org.bgp4j.netty.BGPv4Constants;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

/**
 * @author rainer
 *
 */
public class NotificationPacket extends BGPv4Packet {
	private int errorCode;
	private int errorSubcode;
	
	protected NotificationPacket(int errorCode, int errorSubcode) {
		this.errorCode = errorCode;
		this.errorSubcode = errorSubcode;
	}
	
	@Override
	protected ChannelBuffer encodePayload() {
		ChannelBuffer buffer = ChannelBuffers.buffer(BGPv4Constants.BGP_PACKET_MAX_LENGTH);
		
		buffer.writeByte(errorCode);
		buffer.writeByte(errorSubcode);
		
		return buffer;
	}

	@Override
	protected int getType() {
		return BGPv4Constants.BGP_PACKET_TYPE_NOTIFICATION;
	}

	/**
	 * @return the errorCode
	 */
	public int getErrorCode() {
		return errorCode;
	}

	/**
	 * @return the errorSubcode
	 */
	public int getErrorSubcode() {
		return errorSubcode;
	}	
	
	
}
