/**
 * 
 */
package org.bgp4j.net.events.update;

import org.bgp4j.net.EChannelDirection;

/**
 * @author rainer
 *
 */
public class AttributeFlagsNotificationEvent extends AttributeNotificationEvent {

	/**
	 * @param direction
	 */
	public AttributeFlagsNotificationEvent(EChannelDirection direction) {
		super(direction);
	}

}
