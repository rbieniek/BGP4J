/**
 * 
 */
package org.bgp4j.net.events;

import org.bgp4j.net.EChannelDirection;

/**
 * @author rainer
 *
 */
public class HoldTimerExpiredNotificationEvent extends NotificationEvent {

	/**
	 * @param direction
	 */
	public HoldTimerExpiredNotificationEvent(EChannelDirection direction) {
		super(direction);
	}

}
