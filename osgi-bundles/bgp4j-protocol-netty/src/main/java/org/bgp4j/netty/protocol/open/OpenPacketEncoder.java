/**
 * 
 */
package org.bgp4j.netty.protocol.open;

import io.netty.buffer.ByteBuf;

import org.bgp4j.net.BGPv4Constants;
import org.bgp4j.net.packets.open.OpenPacket;
import org.bgp4j.netty.protocol.BGPv4PacketEncoder;

/**
 * @author rainer
 *
 */
public class OpenPacketEncoder extends BGPv4PacketEncoder<OpenPacket> {

	/* (non-Javadoc)
	 * @see org.bgp4j.netty.protocol.BGPv4PacketEncoder#encodePayload(org.bgp4j.netty.protocol.BGPv4Packet, io.netty.buffer.ByteBuf)
	 */
	@Override
	protected void encodePayload(OpenPacket packet, ByteBuf buffer) {
		buffer.writeByte(packet.getProtocolVersion());
		buffer.writeShort(packet.getAutonomousSystem());
		buffer.writeShort(packet.getHoldTime());
		buffer.writeInt((int)packet.getBgpIdentifier());
		
		if(!packet.getCapabilities().isEmpty()) {
			int capabilityLengthIndex = buffer.writerIndex();
			
			buffer.writeByte(0); // placeholder for capability length
			buffer.writeByte(BGPv4Constants.BGP_OPEN_PARAMETER_TYPE_CAPABILITY); // type byte
			
			int parameterLengthIndex = buffer.writerIndex();

			buffer.writeByte(0); // placeholder for parameter length
			CapabilityCodec.encodeCapabilities(buffer, packet.getCapabilities());			
			
			buffer.setByte(capabilityLengthIndex, buffer.writerIndex() - capabilityLengthIndex - 1);
			buffer.setByte(parameterLengthIndex, buffer.writerIndex() - parameterLengthIndex - 1);
		} else {
			buffer.writeByte(0); // no capabilites encoded --> optional parameter length equals 0
		}
	}

}
