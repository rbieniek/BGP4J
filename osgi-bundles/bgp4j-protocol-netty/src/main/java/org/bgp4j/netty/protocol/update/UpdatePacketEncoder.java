/**
 * 
 */
package org.bgp4j.netty.protocol.update;

import io.netty.buffer.ByteBuf;

import org.bgp4j.net.NetworkLayerReachabilityInformation;
import org.bgp4j.net.attributes.PathAttribute;
import org.bgp4j.net.packets.update.UpdatePacket;
import org.bgp4j.netty.NLRICodec;
import org.bgp4j.netty.protocol.BGPv4PacketEncoder;

/**
 * @author rainer
 *
 */
public class UpdatePacketEncoder extends BGPv4PacketEncoder<UpdatePacket> {

	/* (non-Javadoc)
	 * @see org.bgp4j.netty.protocol.BGPv4PacketEncoder#encodePayload(org.bgp4j.netty.protocol.BGPv4Packet, io.netty.buffer.ByteBuf)
	 */
	@Override
	protected void encodePayload(UpdatePacket packet, ByteBuf buffer) {
		int withdrawnRoutesLengthIndex = buffer.writerIndex();
		
		buffer.writeShort(0); // placeholder for withdrawn routes length
		encodeWithdrawnRoutes(packet, buffer);
		buffer.setShort(withdrawnRoutesLengthIndex, buffer.writerIndex() - withdrawnRoutesLengthIndex -2);
		
		int pathAttributesLengthIndex = buffer.writerIndex();
		
		buffer.writeShort(0); // placeholder for path attributes length
		encodePathAttributes(packet, buffer);
		buffer.setShort(pathAttributesLengthIndex, buffer.writerIndex() - pathAttributesLengthIndex -2);
		
		encodeNlris(packet, buffer);
	}
	
	private void encodeWithdrawnRoutes(UpdatePacket packet, ByteBuf buffer) {
		if(packet.getWithdrawnRoutes() != null) {
			for (NetworkLayerReachabilityInformation route : packet.getWithdrawnRoutes()) {
				NLRICodec.encodeNLRI(buffer, route);
			}
		}
	}

	private void encodePathAttributes(UpdatePacket packet, ByteBuf buffer) {
		if(packet.getPathAttributes() != null) {
			for(PathAttribute pathAttribute : packet.getPathAttributes()) {
				PathAttributeCodec.encodePathAttribute(buffer, pathAttribute);
			}
		}
	}
	
	private void encodeNlris(UpdatePacket packet, ByteBuf buffer) {
		if(packet.getNlris() != null) {
			for (NetworkLayerReachabilityInformation nlri : packet.getNlris()) {
				NLRICodec.encodeNLRI(buffer, nlri);
			}
		}
	}
}
