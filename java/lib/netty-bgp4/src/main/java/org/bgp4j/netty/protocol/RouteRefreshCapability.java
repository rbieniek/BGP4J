/**
 * 
 */
package org.bgp4j.netty.protocol;

import org.bgp4j.netty.BGPv4Constants;
import org.jboss.netty.buffer.ChannelBuffer;

/**
 * @author rainer
 *
 */
public class RouteRefreshCapability extends Capability {

	/* (non-Javadoc)
	 * @see org.bgp4j.netty.protocol.Capability#encodeParameterValue()
	 */
	@Override
	protected ChannelBuffer encodeParameterValue() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.bgp4j.netty.protocol.Capability#getCapabilityType()
	 */
	@Override
	public int getCapabilityType() {
		return BGPv4Constants.BGP_CAPABILITY_TYPE_ROUTE_REFRESH;
	}

	@Override
	protected void decodeParameterValue(ChannelBuffer buffer) {
		assertEmptyParameter(buffer);
	}
}
