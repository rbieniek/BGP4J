/**
 * 
 */
package org.bgp4j.netty.protocol;

import org.bgp4j.netty.BGPv4Constants;

/**
 * @author rainer
 *
 */
public class OpenNotificationPacket extends NotificationPacket {

	protected static final int SUBCODE_UNSPECIFIC = 0;
	protected static final int SUBCODE_UNSUPPORTED_VERSION_NUMBER = 1;
	protected static final int SUBCODE_BAD_PEER_AS = 2;
	protected static final int SUBCODE_BAD_BGP_IDENTIFIER = 3;
	protected static final int SUBCODE_UNSUPPORTED_OPTIONAL_PARAMETER = 4;
	protected static final int SUBCODE_UNACCEPTABLE_HOLD_TIMER = 6;
	
	protected OpenNotificationPacket(int subcode) {
		super(BGPv4Constants.BGP_ERROR_CODE_OPEN, subcode);
	}
}
