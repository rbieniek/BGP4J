/**
 * 
 */
package org.bgp4j.netty.protocol;

import org.jboss.netty.buffer.ChannelBuffer;

/**
 * @author rainer
 *
 */
public class BadMessageLengthNotificationPacket extends MessageHeaderErrorNotificationPacket {

	private int length;
	
	public BadMessageLengthNotificationPacket() {
		super(MessageHeaderErrorNotificationPacket.SUBCODE_BAD_MESSAGE_LENGTH);
	}

	public BadMessageLengthNotificationPacket(int length) {
		super(MessageHeaderErrorNotificationPacket.SUBCODE_BAD_MESSAGE_LENGTH);
		
		setLength(length);
	}

	/**
	 * @return the length
	 */
	public int getLength() {
		return length;
	}

	/**
	 * @param length the length to set
	 */
	public void setLength(int length) {
		this.length = length;
	}

	/* (non-Javadoc)
	 * @see org.bgp4j.netty.protocol.NotificationPacket#encodePayload()
	 */
	@Override
	protected ChannelBuffer encodePayload() {
		ChannelBuffer buffer = super.encodePayload();
		
		buffer.writeShort(getLength());
		
		return buffer;
	}
}
