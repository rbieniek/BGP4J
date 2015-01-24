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
public abstract class NotificationPacketEncoder<T extends NotificationPacket> extends BGPv4PacketEncoder<T> {

	/* (non-Javadoc)
	 * @see org.bgp4j.netty.protocol.BGPv4PacketEncoder#encodePayload(org.bgp4j.netty.protocol.BGPv4Packet, io.netty.buffer.ByteBuf)
	 */
	@Override
	protected final void encodePayload(T packet, ByteBuf buffer) {
		buffer.writeByte(packet.getErrorCode());
		buffer.writeByte(packet.getErrorSubcode());
		
		encodeNotificationPayload(packet, buffer);
	}

	protected abstract void encodeNotificationPayload(T packet, ByteBuf buffer);
}
