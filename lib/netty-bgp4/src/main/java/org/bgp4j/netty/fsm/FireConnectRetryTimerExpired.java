package org.bgp4j.netty.fsm;

public class FireConnectRetryTimerExpired extends FireEventTimeJob {
	public FireConnectRetryTimerExpired() {
		super(FSMEvent.ConnectRetryTimer_Expires);
	}
}