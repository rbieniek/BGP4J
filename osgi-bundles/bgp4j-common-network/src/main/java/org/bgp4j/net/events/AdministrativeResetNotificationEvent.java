/**
 * 
 */
package org.bgp4j.net.events;

import org.bgp4j.net.EChannelDirection;

/**
 * @author rainer
 *
 */
public class AdministrativeResetNotificationEvent extends
		CeaseNotificationEvent {

	/**
	 * @param direction
	 */
	public AdministrativeResetNotificationEvent(EChannelDirection direction) {
		super(direction);
		// TODO Auto-generated constructor stub
	}

}
