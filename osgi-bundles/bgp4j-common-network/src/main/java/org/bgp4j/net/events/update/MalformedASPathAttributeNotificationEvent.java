/**
 * 
 */
package org.bgp4j.net.events.update;

import org.bgp4j.net.EChannelDirection;

/**
 * @author rainer
 *
 */
public class MalformedASPathAttributeNotificationEvent extends AttributeNotificationEvent {

	/**
	 * @param direction
	 */
	public MalformedASPathAttributeNotificationEvent(EChannelDirection direction) {
		super(direction);
	}

}
