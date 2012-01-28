/**
 * 
 */
package org.bgp4j.netty.protocol;

import org.bgp4j.netty.BGPv4Constants;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

/**
 * @author rainer
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
