/**
 * 
 */
package org.bgp4j.net.events.update;

import org.bgp4j.net.EChannelDirection;
import org.bgp4j.net.events.NotificationEvent;

/**
 * @author rainer
 *
 */
public class UpdateNotificationEvent extends NotificationEvent {

	/**
	 * @param direction
	 */
	protected UpdateNotificationEvent(EChannelDirection direction) {
		super(direction);
	}

}
