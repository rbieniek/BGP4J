/**
 * 
 */
package org.bgp4j.net.events.update;

import org.bgp4j.net.EChannelDirection;

/**
 * @author rainer
 *
 */
public class InvalidOriginNotificationEvent extends AttributeNotificationEvent {

	/**
	 * @param direction
	 */
	public InvalidOriginNotificationEvent(EChannelDirection direction) {
		super(direction);
	}

}
