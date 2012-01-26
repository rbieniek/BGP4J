/**
 * 
 */
package org.bgp4j.netty.protocol;


/**
 * @author rainer
 *
 */
public class UnsupportedOptionalParameterNotificationPacket extends	OpenNotificationPacket {

	protected UnsupportedOptionalParameterNotificationPacket() {
		super(OpenNotificationPacket.SUBCODE_UNSUPPORTED_OPTIONAL_PARAMETER);
	}

}
