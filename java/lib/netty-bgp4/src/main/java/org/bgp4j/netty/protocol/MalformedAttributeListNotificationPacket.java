/**
 * 
 */
package org.bgp4j.netty.protocol;

/**
 * @author rainer
 *
 */
public class MalformedAttributeListNotificationPacket extends
		UpdateNotificationPacket {

	/**
	 * @param subcode
	 */
	public MalformedAttributeListNotificationPacket() {
		super(UpdateNotificationPacket.SUBCODE_MALFORMED_ATTRIBUTE_LIST);
	}

}
