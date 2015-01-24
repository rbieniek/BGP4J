/**
 * 
 */
package org.bgp4j.netty.protocol.update;

import io.netty.buffer.ByteBuf;

import org.bgp4j.net.packets.update.MissingWellKnownAttributeNotificationPacket;
import org.bgp4j.netty.protocol.NotificationPacketEncoder;

/**
 * @author rainer
 *
 */
public class MissingWellKnownAttributeNotificationPacketEncoder extends
		NotificationPacketEncoder<MissingWellKnownAttributeNotificationPacket> {

	/* (non-Javadoc)
	 * @see org.bgp4j.netty.protocol.NotificationPacketEncoder#encodeNotificationPayload(org.bgp4j.netty.protocol.NotificationPacket, io.netty.buffer.ByteBuf)
	 */
	@Override
	protected void encodeNotificationPayload(MissingWellKnownAttributeNotificationPacket packet, ByteBuf buffer) {
		buffer.writeByte(packet.getAttributeCode());
	}

}
