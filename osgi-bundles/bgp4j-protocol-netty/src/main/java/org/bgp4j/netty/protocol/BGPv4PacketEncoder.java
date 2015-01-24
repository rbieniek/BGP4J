/**
 * 
 */
package org.bgp4j.netty.protocol;

import io.netty.buffer.ByteBuf;

import org.bgp4j.net.BGPv4Constants;
import org.bgp4j.net.packets.BGPv4Packet;

/**
 * @author rainer
 *
 */
public abstract class BGPv4PacketEncoder<T extends BGPv4Packet> {
	/**
	 * build a binary representation of the protocol packet
	 * 
	 * @param buffer The buffer to store the binary packet representation into
	 */
	public void encodePacket(T packet, ByteBuf buffer) {
		for(int i=0; i<BGPv4Constants.BGP_PACKET_MARKER_LENGTH; i++)
			buffer.writeByte(0xff);

		int headerHeaderIndex = buffer.writerIndex();

		buffer.writeShort(BGPv4Constants.BGP_PACKET_HEADER_LENGTH);
		
		buffer.writeByte(packet.getType());
		encodePayload(packet, buffer);
		
		buffer.setShort(headerHeaderIndex, BGPv4Constants.BGP_PACKET_HEADER_LENGTH + (buffer.writerIndex() - headerHeaderIndex) - 3);
	}

	protected abstract void encodePayload(T packet, ByteBuf buffer);
}
