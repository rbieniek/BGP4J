package org.bgp4j.netty.fsm;

public class FireAutomaticStart extends FireEventTimeJob {
	public FireAutomaticStart() {
		super(FSMEvent.AutomaticStart);
	}
}