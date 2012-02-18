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

import javax.enterprise.inject.Instance;
import javax.enterprise.inject.New;
import javax.inject.Inject;

import org.bgp4.config.nodes.PeerConfiguration;
import org.bgp4j.netty.ASType;
import org.bgp4j.netty.PeerConnectionInformation;
import org.bgp4j.netty.protocol.BGPv4Packet;
import org.bgp4j.netty.service.BGPv4Client;
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
	private @Inject @New Instance<BGPv4Client> clientProvider;
	
	public void configure(PeerConfiguration peerConfig) {
		this.remotePeerAddress = peerConfig.getClientConfig().getRemoteAddress().getAddress();

		pci.setAsTypeInUse(ASType.AS_NUMBER_2OCTETS); // default value, subject to negotiation
		pci.setLocalAS(peerConfig.getLocalAS());
		pci.setLocalBgpIdentifier(peerConfig.getLocalBgpIdentifier());
		pci.setRemoteAS(peerConfig.getRemoteAS());
		pci.setRemoteBgpIdentifier(peerConfig.getRemoteBgpIdentifier());
	}

	public InetAddress getRemotePeerAddress() {
		return this.remotePeerAddress;
	}

	public PeerConnectionInformation getPeerConnectionInformation() {
		return pci;
	}
	
	public void startFSM() {
		// TODO Auto-generated method stub
		
	}

	public void destroyFSM() {
		// TODO Auto-generated method stub
		
	}

	public void handleClientMessage(Channel channel, BGPv4Packet message) {
		log.info("received message " + message);
	}

	public void handleClientConnected() {
		// TODO Auto-generated method stub
		
	}

	public void handleClientClosed() {
		// TODO Auto-generated method stub
		
	}

	public void handleClientDisconnected() {
		// TODO Auto-generated method stub
		
	}

	public void handleServerMessage(Channel channel, BGPv4Packet message) {
		// TODO Auto-generated method stub
		
	}

	public void handleServerOpened() {
		// TODO Auto-generated method stub
		
	}

	public void handleServerDisconnected() {
		// TODO Auto-generated method stub
		
	}

}
