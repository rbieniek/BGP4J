/**
 * 
 */
package org.bgp4j.net.events.open;

import org.bgp4j.net.EChannelDirection;

/**
 * @author rainer
 *
 */
public class UnsupportedOptionalParameterNotificationEvent extends
		OpenNotificationEvent {

	/**
	 * @param direction
	 */
	public UnsupportedOptionalParameterNotificationEvent(
			EChannelDirection direction) {
		super(direction);
	}

}
