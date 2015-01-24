/**
 * 
 */
package org.bgp4j.netty.protocol;

import org.bgp4j.net.packets.BGPv4Packet;

import io.netty.buffer.ByteBuf;

/**
 * @author rainer
 *
 */
public class NoPayloadPacketEncoder extends BGPv4PacketEncoder<BGPv4Packet> {

	/* (non-Javadoc)
	 * @see org.bgp4j.netty.protocol.BGPv4PacketEncoder#encodePayload(org.bgp4j.netty.protocol.BGPv4Packet, io.netty.buffer.ByteBuf)
	 */
	@Override
	protected void encodePayload(BGPv4Packet packet, ByteBuf buffer) {
	}

}
