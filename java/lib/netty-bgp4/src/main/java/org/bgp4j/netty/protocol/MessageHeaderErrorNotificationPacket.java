/**
 * 
 */
package org.bgp4j.netty.protocol;

import org.bgp4j.netty.BGPv4Constants;

/**
 * @author rainer
 *
 */
public class MessageHeaderErrorNotificationPacket extends NotificationPacket {

	protected static final int SUBCODE_CONNECTION_NOT_SYNCHRONIZED = 1;
	protected static final int SUBCODE_BAD_MESSAGE_LENGTH = 2;
	protected static final int SUBCODE_BAD_MESSAGE_TYPE = 3;
	
	protected MessageHeaderErrorNotificationPacket(int subcode) {
		super(BGPv4Constants.BGP_ERROR_CODE_MESSAGE_HEADER, subcode);
	}
}
