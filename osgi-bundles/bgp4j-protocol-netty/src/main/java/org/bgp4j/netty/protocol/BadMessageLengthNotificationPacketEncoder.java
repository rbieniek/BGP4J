/**
 * 
 */
package org.bgp4j.netty.protocol;

import org.bgp4j.net.packets.BadMessageLengthNotificationPacket;

import io.netty.buffer.ByteBuf;

/**
 * @author rainer
 *
 */
public class BadMessageLengthNotificationPacketEncoder extends
		NotificationPacketEncoder<BadMessageLengthNotificationPacket> {

	/* (non-Javadoc)
	 * @see org.bgp4j.netty.protocol.NotificationPacketEncoder#encodeNotificationPayload(org.bgp4j.netty.protocol.NotificationPacket, io.netty.buffer.ByteBuf)
	 */
	@Override
	protected void encodeNotificationPayload(BadMessageLengthNotificationPacket packet, ByteBuf buffer) {
		buffer.writeShort(packet.getLength());
	}

}
