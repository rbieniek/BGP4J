package org.bgp4j.netty.protocol;

import org.bgp4j.net.packets.MaximumNumberOfPrefixesReachedNotificationPacket;

import io.netty.buffer.ByteBuf;

public class MaximumNumberOfPrefixesReachedNotificationPacketEncoder
		extends
		NotificationPacketEncoder<MaximumNumberOfPrefixesReachedNotificationPacket> {

	@Override
	protected void encodeNotificationPayload(MaximumNumberOfPrefixesReachedNotificationPacket packet,ByteBuf buffer) {
		if(packet.getAddressFamily() != null) {
			buffer.writeShort(packet.getAddressFamily().toCode());
			buffer.writeByte(packet.getSubsequentAddressFamily().toCode());
			buffer.writeInt(packet.getPrefixUpperBound());
		}
	}

}
