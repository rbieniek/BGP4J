/**
 * 
 */
package org.bgp4j.netty.protocol;

import org.jboss.netty.buffer.ChannelBuffer;

/**
 * @author rainer
 *
 */
public class BadMessageTypeNotificationPacket extends MessageHeaderErrorNotificationPacket {

	private int type;
	
	public BadMessageTypeNotificationPacket() {
		super(MessageHeaderErrorNotificationPacket.SUBCODE_BAD_MESSAGE_LENGTH);
	}

	public BadMessageTypeNotificationPacket(int length) {
		super(MessageHeaderErrorNotificationPacket.SUBCODE_BAD_MESSAGE_LENGTH);
		
		setType(length);
	}

	/**
	 * @return the length
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param length the length to set
	 */
	public void setType(int length) {
		this.type = length;
	}

	/* (non-Javadoc)
	 * @see org.bgp4j.netty.protocol.NotificationPacket#encodePayload()
	 */
	@Override
	protected ChannelBuffer encodePayload() {
		ChannelBuffer buffer = super.encodePayload();
		
		buffer.writeByte(getType());
		
		return buffer;
	}
}
