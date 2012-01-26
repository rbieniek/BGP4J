/**
 * 
 */
package org.bgp4j.netty.protocol;


/**
 * @author rainer
 *
 */
public class BadPeerASNotificationPacket extends	OpenNotificationPacket {

	protected BadPeerASNotificationPacket() {
		super(OpenNotificationPacket.SUBCODE_BAD_PEER_AS);
	}

}
