/**
 * 
 */
package org.bgp4j.definitions.peer;

import java.net.InetAddress;
import java.util.Collection;

/**
 * @author rainer
 *
 */
public interface PeerConnectionInformationRegistry {
	/**
	 * list all available peers
	 * 
	 * @return
	 */
	public Collection<PeerConnectionInformation> peers();
	
	/**
	 * 
	 * @param peerAddress
	 * @return
	 */
	public PeerConnectionInformation findPeer(InetAddress peerAddress);
}
