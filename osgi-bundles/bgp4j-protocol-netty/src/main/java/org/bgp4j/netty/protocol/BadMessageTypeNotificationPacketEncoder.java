/**
 * 
 */
package org.bgp4j.netty.protocol;

import org.bgp4j.net.packets.BadMessageTypeNotificationPacket;

import io.netty.buffer.ByteBuf;

/**
 * @author rainer
 *
 */
public class BadMessageTypeNotificationPacketEncoder extends
		NotificationPacketEncoder<BadMessageTypeNotificationPacket> {

	/* (non-Javadoc)
	 * @see org.bgp4j.netty.protocol.NotificationPacketEncoder#encodeNotificationPayload(org.bgp4j.netty.protocol.NotificationPacket, io.netty.buffer.ByteBuf)
	 */
	@Override
	protected void encodeNotificationPayload(BadMessageTypeNotificationPacket packet, ByteBuf buffer) {
		buffer.writeByte(packet.getUnknownMessageType());
	}

}
