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
 */
package org.bgp4j.netty.fsm;

import java.net.InetAddress;

import javax.inject.Inject;

import org.bgp4j.netty.ASType;
import org.bgp4j.netty.BGPv4PeerConfiguration;
import org.bgp4j.netty.PeerConnectionInformation;
import org.bgp4j.netty.protocol.BGPv4Packet;
import org.jboss.netty.channel.Channel;
import org.slf4j.Logger;


/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class BGPv4FSM {

	private @Inject Logger log;
	
	private InetAddress remotePeerAddress;
	private PeerConnectionInformation pci = new PeerConnectionInformation();
	
	public void configure(BGPv4PeerConfiguration peerConfig) {
		this.remotePeerAddress = peerConfig.getRemotePeerAddress().getAddress();

		pci.setAsTypeInUse(ASType.AS_NUMBER_2OCTETS); // default value, subject to negotiation
		pci.setLocalAS(peerConfig.getLocalAutonomousSystem());
		pci.setLocalBgpIdentifier(peerConfig.getLocalBgpIdentifier());
		pci.setRemoteAS(peerConfig.getRemoteAutonomousSystem());
		pci.setRemoteBgpIdentifier(peerConfig.getRemoteBgpIdentitifer());
	}

	public InetAddress getRemotePeerAddress() {
		return this.remotePeerAddress;
	}

	public PeerConnectionInformation getPeerConnectionInformation() {
		return pci;
	}
	
	public void destroyFSM() {
		// TODO Auto-generated method stub
		
	}

	public void handleMessage(Channel channel, BGPv4Packet message) {
		log.info("received message " + message);
	}

}
