package org.bgp4j.netty.fsm;

public class FireIdleHoldTimerExpired extends FireEventTimeJob {
	public FireIdleHoldTimerExpired() {
		super(FSMEvent.IdleHoldTimer_Expires);
	}
}