/**
 * 
 */
package org.bgp4j.netty.protocol;

import java.util.Collection;

import org.bgp4j.netty.BGPv4Constants;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

/**
 * base class for all optional BGP4 protocol capabilities
 * 
 * @author rainer
 *
 */
public abstract class Capability {

	public ChannelBuffer encodeCapability() {
		ChannelBuffer buffer = ChannelBuffers.buffer(BGPv4Constants.BGP_CAPABILITY_HEADER_LENGTH + BGPv4Constants.BGP_CAPABILITY_MAX_VALUE_LENGTH);
		ChannelBuffer value = encodeParameterValue();
		int valueSize = (value != null) ? value.readableBytes() : 0;
		
		buffer.writeByte(getCapabilityType());
		buffer.writeByte(valueSize);
		if(value != null)
			buffer.writeBytes(value);
		
		return buffer;
	}
	
	/**
	 * get the capability type
	 * 
	 * @return
	 */
	public abstract int getCapabilityType();
	
	public static ChannelBuffer encodeCapabilities(Collection<Capability> caps) {
		ChannelBuffer buffer = ChannelBuffers.buffer(BGPv4Constants.BGP_PACKET_MAX_LENGTH);
		
		if(caps != null) {
			for (Capability cap : caps)
				buffer.writeBytes(cap.encodeCapability());
		}
		
		return buffer;
	}
	
	public static Capability decodeCapability(ChannelBuffer buffer) { 
		int type = buffer.readUnsignedByte();
		Capability cap = null;
		
		switch(type) {
		case BGPv4Constants.BGP_CAPABILITY_TYPE_ROUTE_REFRESH:
			cap = new RouteRefreshCapability();
			break;
		default:
			cap = new UnknownCapability();
			break;
		}
		
		cap.decodeParameterValue(buffer);
		
		return cap;
	}
	
	/**
	 * encode the capability-specific parameter value
	 * 
	 * @return a channel buffer containing the encoded parameter value or null.
	 */
	protected abstract ChannelBuffer encodeParameterValue();
	
	/**
	 * decode the passed parameter value
	 * 
	 * @param buffer
	 */
	protected abstract void decodeParameterValue(ChannelBuffer buffer);
	
	protected void assertEmptyParameter(ChannelBuffer buffer) {
		int parameterLength = buffer.readUnsignedByte();
		
		if(parameterLength != 0)
			throw new ProtocolPacketFormatException("Expected zero-length parameter, got " + parameterLength + " octets");
	}
}
