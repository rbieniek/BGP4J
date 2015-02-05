/**
 * 
 */
package org.bgp4j.definitions.fsm;

import org.bgp4j.net.packets.BGPv4Packet;

/**
 * This interface defines an inteface that allows finite state machine to actively send packets down 
 * to a peer
 * 
 * @author rainer
 *
 */
public interface MessageWriter {
	/**
	 * 
	 * @param packet
	 */
	void sendPacket(BGPv4Packet packet);
}
