/**
 *  Copyright 2012 Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  
 */
package org.bgp4j.netty.protocol;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.bgp4j.netty.BGPv4Constants;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

/**
 * base class for all optional BGP4 protocol capabilities
 * 
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
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
	
	public static List<Capability> decodeCapabilities(ChannelBuffer buffer) {
		List<Capability> caps = new LinkedList<Capability>();
		
		while(buffer.readable()) {
			caps.add(decodeCapability(buffer));
		}
		
		return caps;
	}
	
	public static Capability decodeCapability(ChannelBuffer buffer) { 
		Capability cap = null;

		try {
			buffer.markReaderIndex();
			
			int type = buffer.readUnsignedByte();
			
			switch(type) {
			case BGPv4Constants.BGP_CAPABILITY_TYPE_MULTIPROTOCOL:
				cap = new MultiProtocolCapability();
				break;
			case BGPv4Constants.BGP_CAPABILITY_TYPE_ROUTE_REFRESH:
				cap = new RouteRefreshCapability();
				break;
			case BGPv4Constants.BGP_CAPABILITY_TYPE_AS4_NUMBERS:
				cap = new AutonomousSystem4Capability();
				break;
			default:
				cap = new UnknownCapability();
				((UnknownCapability)cap).setCapabilityType(type);
				break;
			}
			
			cap.decodeParameterValue(buffer);
		} catch(CapabilityException e) {
			buffer.resetReaderIndex();
			
			int type = buffer.readUnsignedByte();
			int capLength = buffer.readUnsignedByte();
			
			byte[] capPacket = new byte[capLength+2];
			
			buffer.readBytes(capPacket, 2, capLength);
			capPacket[0] = (byte)type;
			capPacket[1] = (byte)capLength;
			
			e.setCapability(capPacket);
			throw e;
		}
		
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
			throw new UnspecificOpenPacketException("Expected zero-length parameter, got " + parameterLength + " octets");
	}

	protected void assertFixedLength(ChannelBuffer buffer, int length) {
		int parameterLength = buffer.readUnsignedByte();
		
		if(parameterLength != length)
			throw new UnspecificOpenPacketException("Expected " + length + " octets parameter, got " + parameterLength + " octets");
	}
}
