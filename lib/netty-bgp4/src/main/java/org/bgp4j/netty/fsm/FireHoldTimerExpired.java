package org.bgp4j.netty.fsm;

public class FireHoldTimerExpired extends FireEventTimeJob {
	public FireHoldTimerExpired() {
		super(FSMEvent.HoldTimer_Expires);
	}
}