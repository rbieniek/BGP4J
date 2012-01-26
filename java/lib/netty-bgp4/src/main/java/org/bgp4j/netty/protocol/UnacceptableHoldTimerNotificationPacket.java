/**
 * 
 */
package org.bgp4j.netty.protocol;


/**
 * @author rainer
 *
 */
public class UnacceptableHoldTimerNotificationPacket extends	OpenNotificationPacket {

	protected UnacceptableHoldTimerNotificationPacket() {
		super(OpenNotificationPacket.SUBCODE_UNACCEPTABLE_HOLD_TIMER);
	}

}
