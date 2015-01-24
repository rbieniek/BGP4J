/**
 * 
 */
package org.bgp4j.netty.protocol;

import org.bgp4j.net.packets.NotificationPacket;

import io.netty.buffer.ByteBuf;

/**
 * @author rainer
 *
 */
public class NoPayloadNotificationPacketEncoder extends
		NotificationPacketEncoder<NotificationPacket> {

	/* (non-Javadoc)
	 * @see org.bgp4j.netty.protocol.NotificationPacketEncoder#encodeNotificationPayload(org.bgp4j.netty.protocol.NotificationPacket, io.netty.buffer.ByteBuf)
	 */
	@Override
	protected void encodeNotificationPayload(NotificationPacket packet, ByteBuf buffer) {
	}

}
