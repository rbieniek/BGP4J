/**
 * 
 */
package org.bgp4j.netty.protocol;


/**
 * @author rainer
 *
 */
public class UnspecificOpenNotificationPacket extends	OpenNotificationPacket {

	protected UnspecificOpenNotificationPacket() {
		super(OpenNotificationPacket.SUBCODE_UNSPECIFIC);
	}

}
