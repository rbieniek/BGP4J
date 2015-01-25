/**
 * 
 */
package org.bgp4j.net.events;

import org.bgp4j.net.EChannelDirection;

/**
 * @author rainer
 *
 */
public class CeaseNotificationEvent extends NotificationEvent {

	protected CeaseNotificationEvent(EChannelDirection direction) {
		super(direction);
	}
}
