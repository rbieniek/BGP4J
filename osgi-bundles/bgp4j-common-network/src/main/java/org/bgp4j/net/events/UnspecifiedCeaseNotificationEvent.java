/**
 * 
 */
package org.bgp4j.net.events;

import org.bgp4j.net.EChannelDirection;

/**
 * @author rainer
 *
 */
public class UnspecifiedCeaseNotificationEvent extends CeaseNotificationEvent {

	/**
	 * @param direction
	 */
	public UnspecifiedCeaseNotificationEvent(EChannelDirection direction) {
		super(direction);
	}

}
