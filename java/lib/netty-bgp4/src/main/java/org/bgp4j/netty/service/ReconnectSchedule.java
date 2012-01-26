/**
 * 
 */
package org.bgp4j.netty.service;

/**
 * @author rainer
 *
 */
public class ReconnectSchedule {

	private BGPv4Client client;
	private long rescheduleWhen;
	
	public ReconnectSchedule(BGPv4Client client) {
		this.client = client;
		this.rescheduleWhen = System.currentTimeMillis() + (client.getPeerConfiguration().getConnectRetryInterval() * 1000L);
	}

	/**
	 * @return the client
	 */
	public BGPv4Client getClient() {
		return client;
	}

	/**
	 * @return the rescheduleWhen
	 */
	public long getRescheduleWhen() {
		return rescheduleWhen;
	}
}
