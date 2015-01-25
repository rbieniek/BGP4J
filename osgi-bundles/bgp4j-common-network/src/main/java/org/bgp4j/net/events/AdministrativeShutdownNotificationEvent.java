/**
 * 
 */
package org.bgp4j.net.events;

import org.bgp4j.net.EChannelDirection;

/**
 * @author rainer
 *
 */
public class AdministrativeShutdownNotificationEvent extends
		CeaseNotificationEvent {

	/**
	 * @param direction
	 */
	public AdministrativeShutdownNotificationEvent(EChannelDirection direction) {
		super(direction);
		// TODO Auto-generated constructor stub
	}

}
