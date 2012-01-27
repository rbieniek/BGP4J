/**
 * 
 */
package org.bgp4j.netty.protocol;

import java.util.LinkedList;
import java.util.List;

import org.bgp4j.netty.BGPv4Constants;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

/**
 * @author rainer
 *
 */
public class UpdatePacket extends BGPv4Packet {

	private List<WithdrawnRoute> withdrawnRoutes = new LinkedList<WithdrawnRoute>();
	private List<NetworkLayerReachabilityInformation> nlris = new LinkedList<NetworkLayerReachabilityInformation>();
	
	/* (non-Javadoc)
	 * @see org.bgp4j.netty.protocol.BGPv4Packet#encodePayload()
	 */
	@Override
	protected ChannelBuffer encodePayload() {
		ChannelBuffer buffer = ChannelBuffers.buffer(BGPv4Constants.BGP_PACKET_MAX_LENGTH);
		ChannelBuffer withdrawnBuffer = encodeWithdrawnRoutes();
		ChannelBuffer pathAttributesBuffer = encodePathAttributes();
		
		buffer.writeShort(withdrawnBuffer.readableBytes());
		buffer.writeBytes(withdrawnBuffer);
		buffer.writeShort(pathAttributesBuffer.readableBytes());
		buffer.writeBytes(pathAttributesBuffer);
		buffer.writeBytes(encodeNlris());
		
		return buffer;
	}

	public int calculatePacketSize() {
		int size = BGPv4Constants.BGP_PACKET_MIN_SIZE_UPDATE;

		size += calculateSizeWithdrawnRoutes();
		// path attributes go here
		size += calculateSizeNlris();
		
		return size;
	}
	
	private ChannelBuffer encodeWithdrawnRoutes() {
		ChannelBuffer buffer = ChannelBuffers.buffer(BGPv4Constants.BGP_PACKET_MAX_LENGTH);

		if(this.withdrawnRoutes != null) {
			for (WithdrawnRoute route : withdrawnRoutes) {
				buffer.writeBytes(route.encodeWithdrawnRoute());
			}
		}
		
		return buffer;
	}

	private ChannelBuffer encodePathAttributes() {
		ChannelBuffer buffer = ChannelBuffers.buffer(BGPv4Constants.BGP_PACKET_MAX_LENGTH);
		
		// attribute handling goes here
		
		return buffer;
	}
	
	private ChannelBuffer encodeNlris() {
		ChannelBuffer buffer = ChannelBuffers.buffer(BGPv4Constants.BGP_PACKET_MAX_LENGTH);

		if(this.nlris != null) {
			for (NetworkLayerReachabilityInformation nlri : nlris) {
				buffer.writeBytes(nlri.encodeWithdrawnRoute());
			}
		}
		
		return buffer;
	}

	private int calculateSizeWithdrawnRoutes() {
		int size = 0;

		if(this.withdrawnRoutes != null) {
			for (WithdrawnRoute route : withdrawnRoutes) {
				size += route.calculatePacketSize();
			}
		}

		return size;
	}
	
	private int calculateSizeNlris() {
		int size = 0;

		if(this.nlris != null) {
			for (NetworkLayerReachabilityInformation nlri : nlris) {
				size += nlri.calculatePacketSize();
			}
		}

		return size;
	}
	
	/* (non-Javadoc)
	 * @see org.bgp4j.netty.protocol.BGPv4Packet#getType()
	 */
	@Override
	protected int getType() {
		return BGPv4Constants.BGP_PACKET_TYPE_UPDATE;
	}

	/**
	 * @return the withdrawnRoutes
	 */
	public List<WithdrawnRoute> getWithdrawnRoutes() {
		return withdrawnRoutes;
	}

	/**
	 * @param withdrawnRoutes the withdrawnRoutes to set
	 */
	public void setWithdrawnRoutes(List<WithdrawnRoute> withdrawnRoutes) {
		this.withdrawnRoutes = withdrawnRoutes;
	}

	/**
	 * @return the nlris
	 */
	public List<NetworkLayerReachabilityInformation> getNlris() {
		return nlris;
	}

	/**
	 * @param nlris the nlris to set
	 */
	public void setNlris(List<NetworkLayerReachabilityInformation> nlris) {
		this.nlris = nlris;
	}

}
