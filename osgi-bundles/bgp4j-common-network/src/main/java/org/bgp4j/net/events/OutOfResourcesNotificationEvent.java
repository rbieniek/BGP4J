/**
 * 
 */
package org.bgp4j.net.events;

import org.bgp4j.net.EChannelDirection;

/**
 * @author rainer
 *
 */
public class OutOfResourcesNotificationEvent extends CeaseNotificationEvent {

	/**
	 * @param direction
	 */
	public OutOfResourcesNotificationEvent(EChannelDirection direction) {
		super(direction);
	}

}
