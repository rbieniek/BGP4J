/**
 * 
 */
package org.bgp4j.net.events;

import org.bgp4j.net.EChannelDirection;

/**
 * @author rainer
 *
 */
public class ConnectionRejectedNotificationEvent extends CeaseNotificationEvent {

	/**
	 * @param direction
	 */
	public ConnectionRejectedNotificationEvent(EChannelDirection direction) {
		super(direction);
	}

}
