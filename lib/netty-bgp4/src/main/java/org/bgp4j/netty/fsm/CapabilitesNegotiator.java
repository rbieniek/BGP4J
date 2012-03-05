/**
 *  Copyright 2012 Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * 
 * File: org.bgp4j.netty.fsm.CapabilitesNegotiator.java 
 */
package org.bgp4j.netty.fsm;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.bgp4.config.nodes.PeerConfiguration;
import org.bgp4j.net.AutonomousSystem4Capability;
import org.bgp4j.net.Capability;
import org.bgp4j.netty.BGPv4Constants;
import org.bgp4j.netty.protocol.open.CapabilityListUnsupportedCapabilityNotificationPacket;
import org.bgp4j.netty.protocol.open.OpenPacket;

/**
 * This class negotiates the supported capabilities with a peer.
 * 
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class CapabilitesNegotiator {

	private Set<Capability> negotiatedCapabilities = new TreeSet<Capability>();
	private PeerConfiguration peerConfiguration;
	
	void setup(PeerConfiguration peerCofiguration) {
		this.peerConfiguration = peerCofiguration;
		
		this.negotiatedCapabilities.addAll((peerCofiguration.getCapabilities().getRequiredCapabilities()));
	}
	
	/**
	 * Insert the currently negotiated capabilities into the OPEN packet.
	 * 
	 * If the capabilities contain an AS number larger than 65536 then the 
	 * AS number in the OPEN packet is changed to AS_TRANS (23456)
	 * 
	 * @param packet
	 * @see BGPv4Constants#BGP_AS_TRANS
	 */
	void insertNegotiatedCapabilities(OpenPacket packet) {
		packet.getCapabilities().clear();
		
		for(Capability cap : negotiatedCapabilities) {
			if(cap instanceof AutonomousSystem4Capability) {
				AutonomousSystem4Capability as4cap = (AutonomousSystem4Capability)cap;
				
				if(as4cap.getAutonomousSystem() > 65536) {
					packet.setAs4AutonomousSystem(((AutonomousSystem4Capability) cap).getAutonomousSystem());
					packet.setAutonomousSystem(BGPv4Constants.BGP_AS_TRANS);
				}
			}
			
			packet.getCapabilities().add(cap);
		}
	}
	
	/**
	 * Handle the notification packet received from the peer about a capability that the peer
	 * cannot handle.
	 * 
	 * @param packet
	 */
	void handleUnsupportedPeerCapability(CapabilityListUnsupportedCapabilityNotificationPacket packet) {
		for(Capability cap : packet.getCapabilities())
			negotiatedCapabilities.remove(cap);
	}
	
	/**
	 * Check an OPEN packet received from a remote peer and match the capabilities contained in it with the
	 * configured capabilities
	 * 
	 * @param packet the open packet received from the remote peer
	 * @return a list of unsupported capabilities.
	 */
	List<Capability> checkOpenForUnsupportedCapabilities(OpenPacket packet) {
		List<Capability> unwantedCaps = new LinkedList<Capability>();
		
		for(Capability cap : packet.getCapabilities()) {
			if(!peerConfiguration.getCapabilities().getRequiredCapabilities().contains(cap))
				unwantedCaps.add(cap);
		}
		
		return unwantedCaps;
	}
}
