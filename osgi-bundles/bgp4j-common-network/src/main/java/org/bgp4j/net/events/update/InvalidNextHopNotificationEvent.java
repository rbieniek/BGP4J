/**
 * 
 */
package org.bgp4j.net.events.update;

import org.bgp4j.net.EChannelDirection;

/**
 * @author rainer
 *
 */
public class InvalidNextHopNotificationEvent extends AttributeNotificationEvent {

	/**
	 * @param direction
	 */
	public InvalidNextHopNotificationEvent(EChannelDirection direction) {
		super(direction);
	}

}
