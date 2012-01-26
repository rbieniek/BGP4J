/**
 * 
 */
package org.bgp4j.netty.protocol;

import org.bgp4j.netty.BGPv4Constants;

/**
 * @author rainer
 *
 */
public class UpdateNotificationPacket extends NotificationPacket {

	protected static final int SUBCODE_MALFORMED_ATTRIBUTE_LIST = 1;
	protected static final int SUBCODE_UNRECOGNIZED_WELL_KNOWN_ATTRIBUTE = 2;
	protected static final int SUBCODE_MISSING_WELL_KNOWN_ATTRIBUTE = 3;
	protected static final int SUBCODE_ATTRIBUTE_FLAGS_ERROR = 4;
	protected static final int SUBCODE_ATTRIBUTE_LENGTH_ERROR = 5;
	protected static final int SUBCODE_INVALID_ORIGIN_ATTRIBUTE = 6;
	protected static final int SUBCODE_INVALID_NEXT_HOP_ATTRIBUTE = 8;
	protected static final int SUBCODE_OPTIONAL_ATTRIBUTE_ERROR = 9;
	protected static final int SUBCODE_INVALID_NETWORK_FIELD = 10;
	protected static final int SUBCODE_MALFORMED_AS_PATH = 11;

	protected UpdateNotificationPacket(int subcode) {
		super(BGPv4Constants.BGP_ERROR_CODE_UPDATE, subcode);
	}
}
