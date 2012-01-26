/**
 * 
 */
package de.urb.netty.bgp4.service;

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
