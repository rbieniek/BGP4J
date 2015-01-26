/**
 * 
 */
package org.bgp4j.net.events.update;

import org.bgp4j.net.EChannelDirection;

/**
 * @author rainer
 *
 */
public class AttributeLengthNotificationEvent extends AttributeNotificationEvent {

	/**
	 * @param direction
	 */
	public AttributeLengthNotificationEvent(EChannelDirection direction) {
		super(direction);
	}

}
