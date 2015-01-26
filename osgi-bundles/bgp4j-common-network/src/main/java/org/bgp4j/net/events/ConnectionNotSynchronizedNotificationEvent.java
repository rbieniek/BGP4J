/**
 * 
 */
package org.bgp4j.net.events;

import org.bgp4j.net.EChannelDirection;

/**
 * @author rainer
 *
 */
public class ConnectionNotSynchronizedNotificationEvent extends
		MessageHeaderErrorNotificationEvent {

	/**
	 * @param direction
	 */
	public ConnectionNotSynchronizedNotificationEvent(
			EChannelDirection direction) {
		super(direction);
	}

}
