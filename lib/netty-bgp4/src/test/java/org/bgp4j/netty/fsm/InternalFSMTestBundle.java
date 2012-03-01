package org.bgp4j.netty.fsm;

import static org.mockito.Matchers.argThat;

class InternalFSMTestBundle {
	private FSMChannelMatcher matcher;
	private FSMChannel channel;
	private boolean tcpConnectionAck;
	
	public InternalFSMTestBundle(FSMChannel channel, boolean tcpConnectionAck) {
		this.channel = channel;
		this.matcher = new FSMChannelMatcher(channel);
		this.tcpConnectionAck = tcpConnectionAck;
	}

	/**
	 * @return the matcher
	 */
	public FSMChannelMatcher getMatcher() {
		return matcher;
	}

	/**
	 * @return the matcher
	 */
	public FSMChannel getMatcherArg() {
		return argThat(matcher);
	}

	/**
	 * @return the channel
	 */
	public FSMChannel getChannel() {
		return channel;
	}

	/**
	 * @return the tcpConnectionAck
	 */
	public boolean isTcpConnectionAck() {
		return tcpConnectionAck;
	}
}