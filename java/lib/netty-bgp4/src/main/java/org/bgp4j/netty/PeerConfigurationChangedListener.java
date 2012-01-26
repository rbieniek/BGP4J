/**
 * 
 */
package org.bgp4j.netty;

/**
 * This event is fired whenevner the peer configuration changes
 * 
 * @author rainer
 *
 */
public interface PeerConfigurationChangedListener {

	/**
	 * this event gets fired when a peer is added
	 * 
	 * @param peer
	 */
	public void peerAdded(BGPv4PeerConfiguration peer);
	
	/**
	 * this event gets fired when a peer is removed
	 * @param peer
	 */
	public void peerRemoved(BGPv4PeerConfiguration peer);
}
