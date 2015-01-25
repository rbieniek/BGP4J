/**
 * 
 */
package org.bgp4j.net.events.open;

import org.bgp4j.net.EChannelDirection;

/**
 * @author rainer
 *
 */
public class UnsupportedVersionNumberNotificationEvent extends
		OpenNotificationEvent {

	/**
	 * @param direction
	 */
	public UnsupportedVersionNumberNotificationEvent(EChannelDirection direction) {
		super(direction);
	}

}
