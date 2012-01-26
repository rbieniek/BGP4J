/**
 * 
 */
package org.bgp4j.netty.protocol;

import org.bgp4j.netty.BGPv4Constants;

/**
 * @author rainer
 *
 */
public class HoldTimerExpiredNotificationPacket extends NotificationPacket {

	/**
	 * @param errorCode
	 * @param errorSubcode
	 */
	public HoldTimerExpiredNotificationPacket() {
		super(BGPv4Constants.BGP_ERROR_CODE_HOLD_TIMER_EXPIRED, 0);
	}

}
