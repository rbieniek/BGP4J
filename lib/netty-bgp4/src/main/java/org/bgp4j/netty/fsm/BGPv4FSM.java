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
import org.quartz.SchedulerException;
import org.slf4j.Logger;


/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class BGPv4FSM {

	/**
	 * Internal proxy class to expose the peer connection information to interested handlers
	 * 
	 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
	 *
	 */
	private class PeerConnectionInformationImpl implements PeerConnectionInformation {
		
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
	
	/**
	 * Internal class to bind callbacks from the internal state machine to concrete actions 
	 * 
	 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
	 *
	 */
	private class InternalFSMCallbacksImpl implements InternalFSMCallbacks {

		@Override
		public void fireConnectRemotePeer() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void fireDisconnectRemotePeer() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void fireSendOpenMessage() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void fireSendInternalErrorNotification() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void fireSendKeepaliveMessage() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void fireReleaseBGPResources() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void fireCompleteBGPInitialization() {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	private @Inject Logger log;
	
	private InetAddress remotePeerAddress;
	private @Inject @New Instance<BGPv4Client> clientProvider;
	
	private PeerConfiguration peerConfig;
	private ASType asTypeInUse = ASType.AS_NUMBER_2OCTETS;

	private Channel peerChannel;
	private BGPv4Client peerClient;
	
	private @Inject InternalFSM internalFsm;
	
	public void configure(PeerConfiguration peerConfig) throws SchedulerException {
		this.remotePeerAddress = peerConfig.getClientConfig().getRemoteAddress().getAddress();
		this.peerConfig = peerConfig;
		
		internalFsm.setup(peerConfig, new InternalFSMCallbacksImpl());
	}

	public InetAddress getRemotePeerAddress() {
		return this.remotePeerAddress;
	}

	public PeerConnectionInformation getPeerConnectionInformation() {
		return new PeerConnectionInformationImpl();
	}
	
	public void startFSMAutomatic() {
//		if(peerConfig.isAllowAutomaticStart()) {
//			FSMEvent event = FSMEvent.AutomaticStart;
//			
//			if(peerConfig.isDampPeerOscillation()) {
//				if(peerConfig.isPassiveTcpEstablishment())
//					event = FSMEvent.AutomaticStart_with_DampPeerOscillations_and_PassiveTcpEstablishment;
//				else
//					event = FSMEvent.AutomaticStart_with_DampPeerOscillations;
//			} else if(peerConfig.isPassiveTcpEstablishment()) {
//				event = FSMEvent.AutomaticStart_with_PassiveTcpEstablishment;
//			}
//			
//			internalFsm.handleEvent(event);
//		}
		internalFsm.handleEvent(FSMEvent.automaticStart());
	}

	public void startFSMManual() {
//		FSMEvent event = FSMEvent.ManualStart;
//		
//		if(peerConfig.isPassiveTcpEstablishment()) {
//			event = FSMEvent.ManualStart_with_PassiveTcpEstablishment;
//		}
//		
//		internalFsm.handleEvent(event);		
		internalFsm.handleEvent(FSMEvent.manualStart());
	}

	public void stopFSM() {
		
	}
	
	public void destroyFSM() {
		
		internalFsm.destroyFSM();
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

	public void handleServerOpened(Channel channel) {
		// TODO Auto-generated method stub
		
	}

	public void handleServerDisconnected() {
		// TODO Auto-generated method stub
		
	}

	public boolean isCanAcceptConnection() {
		return internalFsm.isCanAcceptConnection();
	}
}
