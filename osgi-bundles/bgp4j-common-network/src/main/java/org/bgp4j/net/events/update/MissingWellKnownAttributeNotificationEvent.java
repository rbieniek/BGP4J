/**
 * 
 */
package org.bgp4j.net.events.update;

import org.bgp4j.net.EChannelDirection;

/**
 * @author rainer
 *
 */
public class MissingWellKnownAttributeNotificationEvent extends
		UpdateNotificationEvent {

	/**
	 * @param direction
	 */
	public MissingWellKnownAttributeNotificationEvent(
			EChannelDirection direction) {
		super(direction);
		// TODO Auto-generated constructor stub
	}

}
