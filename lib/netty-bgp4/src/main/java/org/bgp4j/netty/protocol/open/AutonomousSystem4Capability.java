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

import org.bgp4j.netty.BGPv4Constants;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class AutonomousSystem4Capability extends Capability {

	private int autonomousSystem;
	
	/* (non-Javadoc)
	 * @see org.bgp4j.netty.protocol.Capability#getCapabilityType()
	 */
	@Override
	public int getCapabilityType() {
		return BGPv4Constants.BGP_CAPABILITY_TYPE_AS4_NUMBERS;
	}

	/* (non-Javadoc)
	 * @see org.bgp4j.netty.protocol.Capability#encodeParameterValue()
	 */
	@Override
	protected ChannelBuffer encodeParameterValue() {
		ChannelBuffer buffer = ChannelBuffers.buffer(4);
		
		buffer.writeInt(getAutonomousSystem());
		
		return buffer;
	}

	/* (non-Javadoc)
	 * @see org.bgp4j.netty.protocol.Capability#decodeParameterValue(org.jboss.netty.buffer.ChannelBuffer)
	 */
	@Override
	protected void decodeParameterValue(ChannelBuffer buffer) {
		assertFixedLength(buffer, BGPv4Constants.BGP_CAPABILITY_LENGTH_AS4_NUMBERS);

		setAutonomousSystem((int)buffer.readUnsignedInt());
	}

	/**
	 * @return the autonomousSystem
	 */
	public int getAutonomousSystem() {
		return autonomousSystem;
	}

	/**
	 * @param autonomousSystem the autonomousSystem to set
	 */
	public void setAutonomousSystem(int autonomousSystem) {
		this.autonomousSystem = autonomousSystem;
	}

}
