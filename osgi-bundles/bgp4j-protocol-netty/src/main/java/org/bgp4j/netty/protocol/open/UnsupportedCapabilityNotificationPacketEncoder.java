/**
 * 
 */
package org.bgp4j.netty.protocol.open;

import io.netty.buffer.ByteBuf;

import org.bgp4j.net.packets.open.ByteArrayUnsupportedCapabilityNotificationPacket;
import org.bgp4j.net.packets.open.CapabilityListUnsupportedCapabilityNotificationPacket;
import org.bgp4j.net.packets.open.UnsupportedCapabilityNotificationPacket;
import org.bgp4j.netty.protocol.NotificationPacketEncoder;

/**
 * @author rainer
 *
 */
public class UnsupportedCapabilityNotificationPacketEncoder extends
		NotificationPacketEncoder<UnsupportedCapabilityNotificationPacket> {

	/* (non-Javadoc)
	 * @see org.bgp4j.netty.protocol.NotificationPacketEncoder#encodeNotificationPayload(org.bgp4j.netty.protocol.NotificationPacket, io.netty.buffer.ByteBuf)
	 */
	@Override
	protected void encodeNotificationPayload(UnsupportedCapabilityNotificationPacket packet, ByteBuf buffer) {
		if(packet instanceof ByteArrayUnsupportedCapabilityNotificationPacket) {
			buffer.writeBytes(((ByteArrayUnsupportedCapabilityNotificationPacket)packet).getCapability());
		} else if(packet instanceof CapabilityListUnsupportedCapabilityNotificationPacket) {
			CapabilityCodec.encodeCapabilities(buffer, ((CapabilityListUnsupportedCapabilityNotificationPacket)packet).getCapabilities());
		}
	}

}
