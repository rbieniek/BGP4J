/**
 * 
 */
package org.bgp4j.netty.protocol.update;

import io.netty.buffer.ByteBuf;

import org.bgp4j.net.packets.update.AttributeNotificationPacket;
import org.bgp4j.netty.protocol.NotificationPacketEncoder;

/**
 * @author rainer
 *
 */
public class AttributeNotificationPacketEncoder extends
		NotificationPacketEncoder<AttributeNotificationPacket> {

	/* (non-Javadoc)
	 * @see org.bgp4j.netty.protocol.NotificationPacketEncoder#encodeNotificationPayload(org.bgp4j.netty.protocol.NotificationPacket, io.netty.buffer.ByteBuf)
	 */
	@Override
	protected void encodeNotificationPayload(AttributeNotificationPacket packet, ByteBuf buffer) {
		if(packet.getOffendingAttribute() != null)  {
			buffer.writeBytes(packet.getOffendingAttribute());
		}
	}

}
