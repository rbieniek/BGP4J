/**
 * 
 */
package org.bgp4j.net.events;

import org.bgp4j.net.EChannelDirection;

/**
 * @author rainer
 *
 */
public class MessageHeaderErrorNotificationEvent extends NotificationEvent {

	/**
	 * @param direction
	 */
	public MessageHeaderErrorNotificationEvent(EChannelDirection direction) {
		super(direction);
	}

}
