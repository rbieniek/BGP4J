/**
 * 
 */
package org.bgp4j.net.events.update;

import org.bgp4j.net.EChannelDirection;

/**
 * @author rainer
 *
 */
public class OptionalAttributeErrorNotificationEvent extends AttributeNotificationEvent {

	/**
	 * @param direction
	 */
	public OptionalAttributeErrorNotificationEvent(EChannelDirection direction) {
		super(direction);
	}

}
