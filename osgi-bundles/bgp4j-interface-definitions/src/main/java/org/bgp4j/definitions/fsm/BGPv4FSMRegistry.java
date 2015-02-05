/**
 * 
 */
package org.bgp4j.definitions.fsm;

import java.net.InetAddress;
import java.util.Collection;

import org.bgp4j.net.EChannelDirection;

/**
 * @author rainer
 *
 * Registry for BGPv4 finite state machine implementations
 */
public interface BGPv4FSMRegistry {
	/**
	 * Find a BGPv4 FSM instance by the peer'S remote address
	 * 
	 * @param peerAddress the peer remote address
	 * @return the FSM instance or <code>null</code> if no instance can be found for the given peer address
	 */
	public BGPv4FSM findByPeerAddressAndDirection(InetAddress peerAddress, EChannelDirection direction);

	/**
	 * Create a new FSM instance
	 * 
	 * @param peerAddress peer address
	 * @param direction server or client channel
	 * @return the newly created FSM instance
	 * @exception FiniteStateMachineAlreadyExistsException a FSM instance for that peer and direction already exists
	 * @exception UnknownPeerException the peer does not exist
	 */
	public BGPv4FSM createFsm(InetAddress peerAddress, 
			EChannelDirection direction) throws FiniteStateMachineAlreadyExistsException, UnknownPeerException;
	
	/**
	 * Dispose a FSM instance
	 * 
	 * @param fsm
	 */
	public void disposeFSM(BGPv4FSM fsm);
	
	/**
	 * List all known FSM instances
	 * 
	 * @return
	 */
	public Collection<BGPv4FSM> listAllFsm();
}
