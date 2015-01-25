/**
 * 
 */
package org.bgp4j.net.events;

import org.bgp4j.net.EChannelDirection;

/**
 * @author rainer
 *
 */
public class PeerDeconfiguredNotificationEvent extends CeaseNotificationEvent {

	/**
	 * @param direction
	 */
	public PeerDeconfiguredNotificationEvent(EChannelDirection direction) {
		super(direction);
	}

}
