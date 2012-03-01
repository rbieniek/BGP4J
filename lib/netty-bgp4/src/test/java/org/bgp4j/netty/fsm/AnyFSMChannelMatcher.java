package org.bgp4j.netty.fsm;

import org.mockito.ArgumentMatcher;

class AnyFSMChannelMatcher extends ArgumentMatcher<FSMChannel> {
	@Override
	public boolean matches(Object argument) {
		return true;
	}
	
}