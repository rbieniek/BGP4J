/**
 * 
 */
package org.bgp4j.definitions.fsm;

import java.net.InetSocketAddress;

import org.bgp4j.definitions.peer.PeerConnectionInformation;
import org.bgp4j.net.EChannelDirection;
import org.bgp4j.net.events.BgpEvent;
import org.bgp4j.net.packets.BGPv4Packet;

/**
 * @author rainer
 *
 * External service interface for BGPv4 finite state machine implementation
 */
public interface BGPv4FSM {
	/**
	 * obtain the peer connection information for the FSM
	 * 
	 * @return the peer connection information data
	 */
	PeerConnectionInformation peerConnectionInformation();
	
	/**
	 * obtain the peer's remote network address
	 * 
	 * @return the peer's remote network address
	 */
	InetSocketAddress peerAddress();
	
	/**
	 * Type of channel this instance is assigned to
	 * 
	 * @return
	 */
	EChannelDirection direction();
	
	/**
	 * set the message writer for sending outbound packets
	 * 
	 * @param messageWriter
	 */
	void messageWriter(MessageWriter messageWriter);

	/**
	 * handle the initial OPEN sequence
	 */
	void handleConnectionOpened();

	/**
	 * handle the initial OPEN sequence
	 */
	void handleConnectionClosed();
	
	/**
	 * 
	 * @param packet
	 */
	void handlePacket(BGPv4Packet packet);
	
	/**
	 * 
	 * @param event
	 */
	void handleEvent(BgpEvent event);
	
	/**
	 * return the finite machine state value of this instance
	 * 
	 * @return
	 */
	BGPv4FSMState state();
}
