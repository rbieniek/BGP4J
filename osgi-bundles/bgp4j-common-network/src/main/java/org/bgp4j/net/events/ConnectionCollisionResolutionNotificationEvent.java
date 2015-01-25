/**
 * 
 */
package org.bgp4j.net.events;

import org.bgp4j.net.EChannelDirection;

/**
 * @author rainer
 *
 */
public class ConnectionCollisionResolutionNotificationEvent extends
		CeaseNotificationEvent {

	/**
	 * @param direction
	 */
	public ConnectionCollisionResolutionNotificationEvent(EChannelDirection direction) {
		super(direction);
	}

}
