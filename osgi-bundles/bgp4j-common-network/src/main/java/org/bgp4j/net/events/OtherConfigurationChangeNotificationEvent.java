/**
 * 
 */
package org.bgp4j.net.events;

import org.bgp4j.net.EChannelDirection;

/**
 * @author rainer
 *
 */
public class OtherConfigurationChangeNotificationEvent extends
		CeaseNotificationEvent {

	/**
	 * @param direction
	 */
	public OtherConfigurationChangeNotificationEvent(EChannelDirection direction) {
		super(direction);
		// TODO Auto-generated constructor stub
	}

}
