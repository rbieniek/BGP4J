package org.bgp4j.netty.fsm;

public class FireSendKeepalive extends FireEventTimeJob {
	public FireSendKeepalive() {
		super(FSMEvent.keepaliveTimerExpires());
	}
}