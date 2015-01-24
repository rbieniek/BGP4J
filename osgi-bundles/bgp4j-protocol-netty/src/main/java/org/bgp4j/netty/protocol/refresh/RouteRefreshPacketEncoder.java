/**
 * 
 */
package org.bgp4j.netty.protocol.refresh;

import io.netty.buffer.ByteBuf;

import org.bgp4j.net.ORFEntry;
import org.bgp4j.net.ORFType;
import org.bgp4j.net.packets.refresh.RouteRefreshPacket;
import org.bgp4j.netty.protocol.BGPv4PacketEncoder;

/**
 * @author rainer
 *
 */
public class RouteRefreshPacketEncoder extends
		BGPv4PacketEncoder<RouteRefreshPacket> {

	/* (non-Javadoc)
	 * @see org.bgp4j.netty.protocol.BGPv4PacketEncoder#encodePayload(org.bgp4j.netty.protocol.BGPv4Packet, io.netty.buffer.ByteBuf)
	 */
	@Override
	protected void encodePayload(RouteRefreshPacket packet, ByteBuf buffer) {

		buffer.writeShort(packet.getAddressFamily().toCode());
		buffer.writeByte(0);
		buffer.writeByte(packet.getSubsequentAddressFamily().toCode());
		
		if(packet.getOutboundRouteFilter() != null) {
			buffer.writeByte(packet.getOutboundRouteFilter().getRefreshType().toCode());
			
			for(ORFType type : packet.getOutboundRouteFilter().getEntries().keySet()) {
				buffer.writeByte(type.toCode());
				
				int entriesLengthIndex = buffer.writerIndex();

				buffer.writeShort(0); // placeholder for entries length

				for(ORFEntry entry : packet.getOutboundRouteFilter().getEntries().get(type)) {
					ORFEntryCodec.encodeORFEntry(buffer, entry);
				}

				buffer.setShort(entriesLengthIndex, buffer.writerIndex() - entriesLengthIndex -2);
			}
		}
	}

}
