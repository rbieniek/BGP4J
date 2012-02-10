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
package org.bgp4j.netty.protocol.open;

import java.util.HashMap;
import java.util.Map;

import org.bgp4j.netty.BGPv4Constants;
import org.bgp4j.netty.BGPv4Constants.AddressFamily;
import org.bgp4j.netty.BGPv4Constants.SubsequentAddressFamily;
import org.bgp4j.netty.protocol.refresh.ORFType;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class OutboundRouteFilteringCapability extends Capability {

	public OutboundRouteFilteringCapability() { }
	
	public OutboundRouteFilteringCapability(AddressFamily addressFamily, SubsequentAddressFamily subsequentAddressFamily) {
		this.addressFamily = addressFamily;
		this.subsequentAddressFamily = subsequentAddressFamily;
	}

	public enum SendReceive {
		RECEIVE,
		SEND,
		BOTH;
		
		public int toCode() {
			switch(this) {
			case RECEIVE:
				return 1;
			case SEND:
				return 2;
			case BOTH:
				return 3;
			default:
				throw new IllegalArgumentException("unknown Send/Receive type " + this);
			}
		}
		
		public static SendReceive fromCode(int code) {
			switch(code) {
			case 1:
				return RECEIVE;
			case 2:
				return SEND;
			case 3:
				return BOTH;
			default:
				throw new IllegalArgumentException("unknown Send/Receive type ode " + code);
			}
		}
	}

	private AddressFamily addressFamily;
	private SubsequentAddressFamily subsequentAddressFamily;
	private Map<ORFType, SendReceive> filters = new HashMap<ORFType, OutboundRouteFilteringCapability.SendReceive>();
	
	/* (non-Javadoc)
	 * @see org.bgp4j.netty.protocol.Capability#encodeParameterValue()
	 */
	@Override
	protected ChannelBuffer encodeParameterValue() {
		ChannelBuffer buffer = ChannelBuffers.buffer(5 + 2*filters.size());
		
		buffer.writeShort(addressFamily.toCode());
		buffer.writeByte(0);
		buffer.writeByte(subsequentAddressFamily.toCode());
		buffer.writeByte(filters.size());
		
		for(ORFType type : filters.keySet()) {
			buffer.writeByte(type.toCode());
			buffer.writeByte(filters.get(type).toCode());
		}
		
		return buffer;
	}

	/* (non-Javadoc)
	 * @see org.bgp4j.netty.protocol.Capability#getCapabilityType()
	 */
	@Override
	public int getCapabilityType() {
		return BGPv4Constants.BGP_CAPABILITY_TYPE_OUTBOUND_ROUTE_FILTERING;
	}

	@Override
	protected void decodeParameterValue(ChannelBuffer buffer) {
		assertMinimalLength(buffer, 5); // 2 octest AFI + 1 octet reserved + 1 octet SAFI + 1 octet number of (ORF type, Send/Receive) tuples
		
		setAddressFamily(AddressFamily.fromCode(buffer.readUnsignedShort()));
		buffer.readByte();
		setSubsequentAddressFamily(SubsequentAddressFamily.fromCode(buffer.readUnsignedByte()));
		
		int orfs = buffer.readUnsignedByte();
		
		if(buffer.readableBytes() != 2*orfs)
			throw new UnspecificOpenPacketException("Expected " + (2*orfs) + " octets parameter, got " + buffer.readableBytes() + " octets");
		
		try {
			filters.put(ORFType.fromCode(buffer.readUnsignedByte()), SendReceive.fromCode(buffer.readUnsignedByte()));
		} catch(IllegalArgumentException e) {
			throw new UnspecificOpenPacketException(e);
		}
	}
	

	/**
	 * @return the filters
	 */
	public Map<ORFType, SendReceive> getFilters() {
		return filters;
	}

	/**
	 * @param filters the filters to set
	 */
	public void setFilters(Map<ORFType, SendReceive> filters) {
		this.filters = filters;
	}

	/**
	 * @return the addressFamily
	 */
	public AddressFamily getAddressFamily() {
		return addressFamily;
	}

	/**
	 * @param addressFamily the addressFamily to set
	 */
	public void setAddressFamily(AddressFamily addressFamily) {
		this.addressFamily = addressFamily;
	}

	/**
	 * @return the subsequentAddressFamily
	 */
	public SubsequentAddressFamily getSubsequentAddressFamily() {
		return subsequentAddressFamily;
	}

	/**
	 * @param subsequentAddressFamily the subsequentAddressFamily to set
	 */
	public void setSubsequentAddressFamily(
			SubsequentAddressFamily subsequentAddressFamily) {
		this.subsequentAddressFamily = subsequentAddressFamily;
	}
}
