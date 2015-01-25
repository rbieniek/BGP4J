/**
 * 
 */
package org.bgp4j.net.events.open;

import org.bgp4j.net.EChannelDirection;

/**
 * @author rainer
 *
 */
public class UnspecificOpenNotificationEvent extends OpenNotificationEvent {

	/**
	 * @param direction
	 */
	public UnspecificOpenNotificationEvent(EChannelDirection direction) {
		super(direction);
	}

}
