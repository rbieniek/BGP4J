package org.bgp4j.netty.fsm;

import org.mockito.ArgumentMatcher;

class FSMChannelMatcher extends ArgumentMatcher<FSMChannel> {
	private FSMChannel matchedChannel;
	
	FSMChannelMatcher(FSMChannel matchedChannel) {
		this.matchedChannel = matchedChannel; 
	}

	@Override
	public boolean matches(Object argument) {
		return ((FSMChannel)argument) == matchedChannel;
	}

	FSMChannel getMatchedChannel() {
		return getMatchedChannel();
	}
	
}