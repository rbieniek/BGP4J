/**
 * 
 */
package org.bgp4j.net.events.open;

import org.bgp4j.net.EChannelDirection;

/**
 * @author rainer
 *
 */
public class UnsupportedCapabilityNotificationEvent extends
		OpenNotificationEvent {

	/**
	 * @param direction
	 */
	public UnsupportedCapabilityNotificationEvent(EChannelDirection direction) {
		super(direction);
	}

}
