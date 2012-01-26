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
public class KeepalivePacket extends BGPv4Packet {

	@Override
	protected ChannelBuffer encodePayload() {
		return null;
	}

	@Override
	protected int getType() {
		return BGPv4Constants.BGP_PACKET_TYPE_KEEPALIVE;
	}

}
