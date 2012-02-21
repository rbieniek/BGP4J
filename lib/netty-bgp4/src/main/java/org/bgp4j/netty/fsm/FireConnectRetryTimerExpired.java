package org.bgp4j.netty.fsm;

class FireConnectRetryTimerExpired extends FireEventTimeJob {
	FireConnectRetryTimerExpired() {
		super(FSMEvent.ConnectRetryTimer_Expires);
	}
}