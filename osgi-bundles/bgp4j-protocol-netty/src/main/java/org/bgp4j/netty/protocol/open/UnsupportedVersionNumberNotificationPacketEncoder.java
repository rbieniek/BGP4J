/**
 * 
 */
package org.bgp4j.netty.protocol.open;

import io.netty.buffer.ByteBuf;

import org.bgp4j.net.packets.open.UnsupportedVersionNumberNotificationPacket;
import org.bgp4j.netty.protocol.NotificationPacketEncoder;

/**
 * @author rainer
 *
 */
public class UnsupportedVersionNumberNotificationPacketEncoder extends NotificationPacketEncoder<UnsupportedVersionNumberNotificationPacket> {

	/* (non-Javadoc)
	 * @see org.bgp4j.netty.protocol.NotificationPacketEncoder#encodeNotificationPayload(org.bgp4j.netty.protocol.NotificationPacket, io.netty.buffer.ByteBuf)
	 */
	@Override
	protected void encodeNotificationPayload(UnsupportedVersionNumberNotificationPacket packet, ByteBuf buffer) {
		buffer.writeShort(packet.getSupportedProtocolVersion());
	}

}
