/**
 * 
 */
package org.bgp4j.netty;

import org.bgp4j.definitions.fsm.BGPv4FSM;
import org.bgp4j.definitions.fsm.BGPv4FSMRegistry;
import org.bgp4j.definitions.peer.PeerConnectionInformation;
import org.bgp4j.definitions.peer.PeerConnectionInformationRegistry;
import org.bgp4j.net.EChannelDirection;

import io.netty.util.AttributeKey;

/**
 * @author rainer
 *
 */
public class Attributes {

	static {
		peerInfoKey = AttributeKey.valueOf("peer-connection-information");
		peerConnectionInformationRegistry = AttributeKey.valueOf("peer-connection-information-registry");
		channelDirectionKey = AttributeKey.valueOf("channel-direction");
		fsmRegistryKey = AttributeKey.valueOf("finite-state-machine-registry");
		fsmKey = AttributeKey.valueOf("finite-state-machine");
	}
	
	/**
	 * 
	 */
	public static final AttributeKey<PeerConnectionInformation> peerInfoKey;

	/**
	 * 
	 */
	public static final AttributeKey<EChannelDirection> channelDirectionKey;

	/**
	 * 
	 */
	public static final AttributeKey<BGPv4FSMRegistry> fsmRegistryKey;
	
	/**
	 * 
	 */
	public static final AttributeKey<PeerConnectionInformationRegistry> peerConnectionInformationRegistry;
	
	/**
	 * 
	 */
	public static final AttributeKey<BGPv4FSM> fsmKey;
}
