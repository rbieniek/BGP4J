package org.bgp4j.net.events.open;

import org.bgp4j.net.EChannelDirection;

public class UnacceptableHoldTimerNotificationEvent extends
		OpenNotificationEvent {

	public UnacceptableHoldTimerNotificationEvent(EChannelDirection direction) {
		super(direction);
	}

}
