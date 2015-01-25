/**
 * 
 */
package org.bgp4j.net.events.open;

import org.bgp4j.net.EChannelDirection;

/**
 * @author rainer
 *
 */
public class BadBgpIdentifierNotificationEvent extends OpenNotificationEvent {

	/**
	 * @param direction
	 */
	public BadBgpIdentifierNotificationEvent(EChannelDirection direction) {
		super(direction);
		// TODO Auto-generated constructor stub
	}

}
