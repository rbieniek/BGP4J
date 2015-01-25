/**
 * 
 */
package org.bgp4j.net.events;

import org.bgp4j.net.EChannelDirection;

/**
 * @author rainer
 *
 */
public class FiniteStateMachineErrorNotificationEvent extends CeaseNotificationEvent {

	public FiniteStateMachineErrorNotificationEvent(EChannelDirection direction) {
		super(direction);
	}

}
