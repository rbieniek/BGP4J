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

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class UnknownCapability extends Capability {
	private int capabilityType;
	private byte[] value;
	
	/* (non-Javadoc)
	 * @see org.bgp4j.netty.protocol.Capability#encodeParameterValue()
	 */
	@Override
	protected ChannelBuffer encodeParameterValue() {
		ChannelBuffer buffer = null;
		
		if(value != null && value.length > 0) {
			buffer = ChannelBuffers.buffer(value.length);
			
			buffer.writeBytes(getValue());
		}
		
		return buffer;
	}

	/* (non-Javadoc)
	 * @see org.bgp4j.netty.protocol.Capability#getCapabilityType()
	 */
	@Override
	public int getCapabilityType() {
		return capabilityType;
	}

	/**
	 * @return the value
	 */
	public byte[] getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(byte[] value) {
		this.value = value;
	}

	/**
	 * @param capabilityType the capabilityType to set
	 */
	public void setCapabilityType(int capabilityType) {
		this.capabilityType = capabilityType;
	}

	@Override
	protected void decodeParameterValue(ChannelBuffer buffer) {
		int parameterLength = buffer.readUnsignedByte();
		
		if(parameterLength > 0) {
			value = new byte[parameterLength];
			
			buffer.readBytes(value);
		}
		
	}

}
