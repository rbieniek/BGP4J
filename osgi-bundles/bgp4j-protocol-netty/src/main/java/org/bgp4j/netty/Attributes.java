/**
 * 
 */
package org.bgp4j.netty;

import org.bgp4j.definitions.PeerConnectionInformation;
import org.bgp4j.net.EChannelDirection;

import io.netty.util.AttributeKey;

/**
 * @author rainer
 *
 */
public class Attributes {

	static {
		peerInfoKey = AttributeKey.valueOf("peer-connection-information");
		channelDirectionKey = AttributeKey.valueOf("channel-direction");
	}
	
	/**
	 * 
	 */
	public static final AttributeKey<PeerConnectionInformation> peerInfoKey;

	/**
	 * 
	 */
	public static final AttributeKey<EChannelDirection> channelDirectionKey;
}
