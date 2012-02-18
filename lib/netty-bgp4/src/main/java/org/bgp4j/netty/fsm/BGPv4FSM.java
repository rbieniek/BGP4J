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

	public class PeerConnectionInformationImpl implements PeerConnectionInformation {
		
		public ASType getAsTypeInUse() {
			return asTypeInUse;
		}

		/**
		 * 
		 * @return
		 */
		public int getLocalAS() {
			return peerConfig.getLocalAS();
		}
		
		/**
		 * 
		 * @return
		 */
		public int getRemoteAS() {
			return peerConfig.getRemoteAS();
		}
		
		/**
		 * Test if the connection describes an IBGP connection (peers in the same AS)
		 * 
		 * @return <code>true</code> if IBGP connection, <code>false</code> otherwise
		 */
		public boolean isIBGPConnection() {
			return (getRemoteAS() == getLocalAS());
		}

		/**
		 * Test if the connection describes an EBGP connection (peers in the same AS)
		 * 
		 * @return <code>true</code> if EBGP connection, <code>false</code> otherwise
		 */
		public boolean isEBGPConnection() {
			return (getRemoteAS() != getLocalAS());
		}
		
		/**
		 * Test if this connection uses 4 octet AS numbers
		 * 
		 * @return
		 */
		public boolean isAS4OctetsInUse() {
			return (asTypeInUse == ASType.AS_NUMBER_4OCTETS);
		}

		/**
		 * @return the localBgpIdentifier
		 */
		public long getLocalBgpIdentifier() {
			return peerConfig.getLocalBgpIdentifier();
		}

		/**
		 * @return the remoteBgpIdentifier
		 */
		public long getRemoteBgpIdentifier() {
			return peerConfig.getRemoteBgpIdentifier();
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("PeerConnectionInformation [localAS=").append(getLocalAS())
					.append(", remoteAS=").append(getRemoteAS())
					.append(", localBgpIdentifier=").append(getLocalBgpIdentifier())
					.append(", remoteBgpIdentifier=").append(getRemoteBgpIdentifier())
					.append(", ");
			if (getAsTypeInUse() != null)
				builder.append("asTypeInUse=").append(getAsTypeInUse());
			builder.append("]");
			return builder.toString();
		}
	}
	
	private @Inject Logger log;
	
	private InetAddress remotePeerAddress;
	private @Inject @New Instance<BGPv4Client> clientProvider;
	
	private PeerConfiguration peerConfig;
	private FSMState fsmState;
	private ASType asTypeInUse = ASType.AS_NUMBER_2OCTETS;
	
	public void configure(PeerConfiguration peerConfig) {
		this.remotePeerAddress = peerConfig.getClientConfig().getRemoteAddress().getAddress();
		this.peerConfig = peerConfig;
	}

	public InetAddress getRemotePeerAddress() {
		return this.remotePeerAddress;
	}

	public PeerConnectionInformation getPeerConnectionInformation() {
		return new PeerConnectionInformationImpl();
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

	/**
	 * @return the fsmState
	 */
	public FSMState getFsmState() {
		return fsmState;
	}

}
