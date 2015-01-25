/**
 * 
 */
package org.bgp4j.net.events;

import org.bgp4j.net.EChannelDirection;

/**
 * @author rainer
 *
 */
public class MaximumNumberOfPrefixesReachedNotificationEvent extends
		CeaseNotificationEvent {

	/**
	 * @param direction
	 */
	public MaximumNumberOfPrefixesReachedNotificationEvent(EChannelDirection direction) {
		super(direction);
	}

}
