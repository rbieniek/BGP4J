/**
 * 
 */
package org.bgp4j.net.events.open;

import org.bgp4j.net.EChannelDirection;
import org.bgp4j.net.events.NotificationEvent;

/**
 * @author rainer
 *
 */
public class OpenNotificationEvent extends NotificationEvent {

	/**
	 * @param direction
	 */
	protected OpenNotificationEvent(EChannelDirection direction) {
		super(direction);
	}

}
