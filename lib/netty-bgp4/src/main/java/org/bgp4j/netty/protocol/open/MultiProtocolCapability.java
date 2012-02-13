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
 * File: org.bgp4j.netty.protocol.MultiProtocolCapability.java 
 */
package org.bgp4j.netty.protocol.open;

import org.bgp4j.netty.AddressFamily;
import org.bgp4j.netty.BGPv4Constants;
import org.bgp4j.netty.SubsequentAddressFamily;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class MultiProtocolCapability extends Capability {

	private AddressFamily afi;
	private SubsequentAddressFamily safi;
	
	/* (non-Javadoc)
	 * @see org.bgp4j.netty.protocol.Capability#getCapabilityType()
	 */
	@Override
	public int getCapabilityType() {
		return BGPv4Constants.BGP_CAPABILITY_TYPE_MULTIPROTOCOL;
	}

	/* (non-Javadoc)
	 * @see org.bgp4j.netty.protocol.Capability#encodeParameterValue()
	 */
	@Override
	protected ChannelBuffer encodeParameterValue() {
		ChannelBuffer buffer = ChannelBuffers.buffer(4);
		
		if(afi != null)
			buffer.writeShort(afi.toCode());
		else
			buffer.writeShort(AddressFamily.RESERVED.toCode());
		
		buffer.writeByte(0); // reserved
		
		if(safi != null)
			buffer.writeByte(safi.toCode());
		else
			buffer.writeByte(0);
		
		return buffer;
	}

	/* (non-Javadoc)
	 * @see org.bgp4j.netty.protocol.Capability#decodeParameterValue(org.jboss.netty.buffer.ChannelBuffer)
	 */
	@Override
	protected void decodeParameterValue(ChannelBuffer buffer) {
		assertFixedLength(buffer, BGPv4Constants.BGP_CAPABILITY_LENGTH_MULTIPROTOCOL);
		
		setAfi(AddressFamily.fromCode(buffer.readShort()));
		buffer.readByte(); // reserved
		setSafi(SubsequentAddressFamily.fromCode(buffer.readByte()));
	}

	/**
	 * @return the afi
	 */
	public AddressFamily getAfi() {
		return afi;
	}

	/**
	 * @param afi the afi to set
	 */
	public void setAfi(AddressFamily afi) {
		this.afi = afi;
	}

	/**
	 * @return the safi
	 */
	public SubsequentAddressFamily getSafi() {
		return safi;
	}

	/**
	 * @param safi the safi to set
	 */
	public void setSafi(SubsequentAddressFamily safi) {
		this.safi = safi;
	}

}
