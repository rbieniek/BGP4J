/**
 * 
 */
package org.bgp4j.netty.service;

/**
 * @author rainer
 *
 */
public class ClientNeedReconnectEvent {

	private String clientUuid;
	
	public ClientNeedReconnectEvent(String clientUuid) {
		this.clientUuid = clientUuid;
	}

	/**
	 * @return the clientUuid
	 */
	public String getClientUuid() {
		return clientUuid;
	}
}
