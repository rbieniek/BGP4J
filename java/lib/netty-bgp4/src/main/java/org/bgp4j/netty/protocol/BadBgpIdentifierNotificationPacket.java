/**
 * 
 */
package org.bgp4j.netty.protocol;


/**
 * @author rainer
 *
 */
public class BadBgpIdentifierNotificationPacket extends	OpenNotificationPacket {

	protected BadBgpIdentifierNotificationPacket() {
		super(OpenNotificationPacket.SUBCODE_BAD_BGP_IDENTIFIER);
	}

}
