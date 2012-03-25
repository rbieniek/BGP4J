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

import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Set;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.bgp4.config.nodes.PeerConfiguration;
import org.bgp4j.net.ASType;
import org.bgp4j.net.AddressFamily;
import org.bgp4j.net.AddressFamilyKey;
import org.bgp4j.net.InetAddressNextHop;
import org.bgp4j.net.SubsequentAddressFamily;
import org.bgp4j.net.attributes.MultiProtocolReachableNLRI;
import org.bgp4j.net.attributes.MultiProtocolUnreachableNLRI;
import org.bgp4j.net.attributes.NextHopPathAttribute;
import org.bgp4j.net.attributes.PathAttribute;
import org.bgp4j.net.capabilities.Capability;
import org.bgp4j.net.capabilities.MultiProtocolCapability;
import org.bgp4j.netty.BGPv4Constants;
import org.bgp4j.netty.FSMState;
import org.bgp4j.netty.PeerConnectionInformation;
import org.bgp4j.netty.handlers.BgpEvent;
import org.bgp4j.netty.handlers.NotificationEvent;
import org.bgp4j.netty.protocol.BGPv4Packet;
import org.bgp4j.netty.protocol.FiniteStateMachineErrorNotificationPacket;
import org.bgp4j.netty.protocol.HoldTimerExpiredNotificationPacket;
import org.bgp4j.netty.protocol.KeepalivePacket;
import org.bgp4j.netty.protocol.NotificationPacket;
import org.bgp4j.netty.protocol.UnspecifiedCeaseNotificationPacket;
import org.bgp4j.netty.protocol.open.OpenNotificationPacket;
import org.bgp4j.netty.protocol.open.OpenPacket;
import org.bgp4j.netty.protocol.open.UnsupportedVersionNumberNotificationPacket;
import org.bgp4j.netty.protocol.update.InvalidNextHopException;
import org.bgp4j.netty.protocol.update.UpdateNotificationPacket;
import org.bgp4j.netty.protocol.update.UpdatePacket;
import org.bgp4j.netty.service.BGPv4Client;
import org.bgp4j.rib.PeerRoutingInformationBase;
import org.bgp4j.rib.PeerRoutingInformationBaseManager;
import org.bgp4j.rib.RIBSide;
import org.bgp4j.rib.RouteAdded;
import org.bgp4j.rib.RouteWithdrawn;
import org.jboss.netty.channel.Channel;
import org.quartz.SchedulerException;
import org.slf4j.Logger;


/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class BGPv4FSM {

	private class FSMChannelImpl implements FSMChannel {
		private Channel channel;
		private BGPv4Client peerClient;

		public FSMChannelImpl(Channel channel) {
			this.channel = channel;
		}
		
		public FSMChannelImpl(Channel channel, BGPv4Client peerClient) {
			this(channel);
			
			this.peerClient = peerClient;
		}
		
		/**
		 * @return the channel
		 */
		private Channel getChannel() {
			return channel;
		}

		/**
		 * @return the peerClient
		 */
		private BGPv4Client getPeerClient() {
			return peerClient;
		}

	}
	
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
			BGPv4Client client = clientProvider.get();
			
			managedChannels.add(new FSMChannelImpl(client.startClient(peerConfig).getChannel(), client));
		}

		@Override
		public void fireDisconnectRemotePeer(FSMChannel channel) {
			if(managedChannels.contains(channel)) {
				((FSMChannelImpl)channel).getChannel().close();
				managedChannels.remove(channel);
			}			
			
		}

		@Override
		public void fireSendOpenMessage(FSMChannel channel) {
			if(managedChannels.contains(channel)) {
				OpenPacket packet = new OpenPacket();
				
				packet.setAutonomousSystem(peerConfig.getLocalAS());
				packet.setBgpIdentifier(peerConfig.getLocalBgpIdentifier());
				packet.setHoldTime(peerConfig.getHoldTime());
				packet.setProtocolVersion(BGPv4Constants.BGP_VERSION);
				
				capabilitiesNegotiator.insertLocalCapabilities(packet);
				
				((FSMChannelImpl)channel).getChannel().write(packet);
			}			
		}

		@Override
		public void fireSendInternalErrorNotification(FSMChannel channel) {
			if(managedChannels.contains(channel)) {
				((FSMChannelImpl)channel).getChannel().write(new FiniteStateMachineErrorNotificationPacket());
			}			
		}

		@Override
		public void fireSendCeaseNotification(FSMChannel channel) {
			if(managedChannels.contains(channel)) {
				((FSMChannelImpl)channel).getChannel().write(new UnspecifiedCeaseNotificationPacket());
			}			
		}

		@Override
		public void fireSendKeepaliveMessage(FSMChannel channel) {
			if(managedChannels.contains(channel)) {
				((FSMChannelImpl)channel).getChannel().write(new KeepalivePacket());
			}			
		}

		@Override
		public void fireReleaseBGPResources() {
			if(prib != null)
				prib.destroyAllRoutingInformationBases();
			prib = null;
		}

		@Override
		public void fireCompleteBGPLocalInitialization() {
			prib = pribManager.peerRoutingInformationBase(peerConfig.getPeerName());
			
			// allocate all RIBs for the local end which are configured
			for(MultiProtocolCapability mpcap : capabilitiesNegotiator.listLocalCapabilities(MultiProtocolCapability.class)) {
				prib.allocateRoutingInformationBase(RIBSide.Local, mpcap.toAddressFamilyKey());
			}
			
		}

		@Override
		public void fireCompleteBGPPeerInitialization() {
			// allocate all RIBs for the local end which are configured
			for(MultiProtocolCapability mpcap : capabilitiesNegotiator.listRemoteCapabilities(MultiProtocolCapability.class)) {
				prib.allocateRoutingInformationBase(RIBSide.Remote, mpcap.toAddressFamilyKey());
			}

			// build a routing update filter for outbound routing updates 
			for(MultiProtocolCapability mpcap : capabilitiesNegotiator.intersectLocalAndRemoteCapabilities(MultiProtocolCapability.class)) {
				outboundAddressFamilyMask.add(mpcap.toAddressFamilyKey());
			}

		}

		@Override
		public void fireSendHoldTimerExpiredNotification(FSMChannel channel) {
			if(managedChannels.contains(channel)) {
				((FSMChannelImpl)channel).getChannel().write(new HoldTimerExpiredNotificationPacket());
			}			
		}

		@Override
		public void fireSendUpdateErrorNotification(FSMChannel channel) {
		}

		@Override
		public void fireEstablished() {
			// TODO Auto-generated method stub
			
		}

	}
	
	private @Inject Logger log;
	
	private @Inject Instance<BGPv4Client> clientProvider;
	
	private PeerConfiguration peerConfig;
	private ASType asTypeInUse = ASType.AS_NUMBER_2OCTETS;

	private @Inject InternalFSM internalFsm;
	private @Inject CapabilitesNegotiator capabilitiesNegotiator;
	private @Inject PeerRoutingInformationBaseManager pribManager;
	
	private Set<FSMChannelImpl> managedChannels = new HashSet<FSMChannelImpl>();
	private PeerRoutingInformationBase prib;
	private Set<AddressFamilyKey> outboundAddressFamilyMask = new HashSet<AddressFamilyKey>();
	
	public void configure(PeerConfiguration peerConfig) throws SchedulerException {
		this.peerConfig = peerConfig;
		
		internalFsm.setup(peerConfig, new InternalFSMCallbacksImpl());
		capabilitiesNegotiator.setup(peerConfig);
		
	}

	public InetSocketAddress getRemotePeerAddress() {
		return peerConfig.getClientConfig().getRemoteAddress();
	}

	public PeerConnectionInformation getPeerConnectionInformation() {
		return new PeerConnectionInformationImpl();
	}
	
	public void startFSMAutomatic() {
		internalFsm.handleEvent(FSMEvent.automaticStart());
	}

	public void startFSMManual() {
		internalFsm.handleEvent(FSMEvent.manualStart());
	}

	public void stopFSM() {
		internalFsm.handleEvent(FSMEvent.automaticStop());
	}
	
	public void destroyFSM() {
		internalFsm.destroyFSM();
	}

	public void handleMessage(Channel channel, BGPv4Packet message) {
		log.info("received message " + message);

		if(message instanceof OpenPacket) {
			internalFsm.setPeerProposedHoldTime(((OpenPacket) message).getHoldTime());
			
			capabilitiesNegotiator.recordPeerCapabilities((OpenPacket)message);
			
			if(capabilitiesNegotiator.missingRequiredCapabilities().size() > 0) {
				for(Capability cap : capabilitiesNegotiator.missingRequiredCapabilities())
					log.error("Missing required capability: " + cap);
				
				internalFsm.handleEvent(FSMEvent.bgpOpenMessageError());
			} else
				internalFsm.handleEvent(FSMEvent.bgpOpen(findWrapperForChannel(channel)));
		} else if(message instanceof KeepalivePacket) {
			internalFsm.handleEvent(FSMEvent.keepAliveMessage());
		} else if(message instanceof UpdatePacket) {
			internalFsm.handleEvent(FSMEvent.updateMessage());
			
			try {
				processRemoteUpdate((UpdatePacket)message);
			} catch(Exception e) {
				log.error("error processing UPDATE packet from peer: " + peerConfig.getPeerName());

				internalFsm.handleEvent(FSMEvent.updateMessageError());
			}
		} else if(message instanceof UnsupportedVersionNumberNotificationPacket) {
			internalFsm.handleEvent(FSMEvent.notifyMessageVersionError());
		} else if(message instanceof OpenNotificationPacket) {
			internalFsm.handleEvent(FSMEvent.bgpOpenMessageError());
		} else if(message instanceof UpdateNotificationPacket) {
			internalFsm.handleEvent(FSMEvent.updateMessageError());
		} else if(message instanceof NotificationPacket) {
			internalFsm.handleEvent(FSMEvent.notifyMessage());
		}
	}

	public void handleEvent(Channel channel, BgpEvent message) {
		if(message instanceof NotificationEvent) {
			for(NotificationPacket packet :((NotificationEvent)message).getNotifications()) {
				if(packet instanceof UnsupportedVersionNumberNotificationPacket) {
					internalFsm.handleEvent(FSMEvent.notifyMessageVersionError());
				} else if(packet instanceof OpenNotificationPacket) {
					internalFsm.handleEvent(FSMEvent.bgpOpenMessageError());
				} else if(packet instanceof UpdateNotificationPacket) {
					internalFsm.handleEvent(FSMEvent.updateMessageError());
				} else
					internalFsm.handleEvent(FSMEvent.notifyMessage());
			}
		}
	}

	public void handleClientConnected(Channel channel) {
		FSMChannelImpl wrapper = findWrapperForChannel(channel);
		
		if(wrapper != null)
			internalFsm.handleEvent(FSMEvent.tcpConnectionRequestAcked(wrapper));
	}
	
	public void handleServerOpened(Channel channel) {
		FSMChannelImpl wrapper= new FSMChannelImpl(channel);
		
		managedChannels.add(wrapper);
		internalFsm.handleEvent(FSMEvent.tcpConnectionConfirmed(wrapper));
	}

	public void handleClosed(Channel channel) {
		FSMChannel wrapper = findWrapperForChannel(channel);
		
		if(wrapper != null)
			internalFsm.handleEvent(FSMEvent.tcpConnectionFails(wrapper));
	}

	public void handleDisconnected(Channel channel) {
	}

	public boolean isCanAcceptConnection() {
		return internalFsm.isCanAcceptConnection();
	}
	
	public FSMState getState() {
		return internalFsm.getState();
	}
	
	public void handleRouteAdded(@Observes RouteAdded event) {
		if(event.getSide() == RIBSide.Local) {
			if(StringUtils.equals(event.getPeerName(), peerConfig.getPeerName())) {
				// TODO enqueue route added update
			}
		}
	}
	
	public void handleRouteWithdrawn(@Observes RouteWithdrawn event) {
		if(event.getSide() == RIBSide.Local) {
			if(StringUtils.equals(event.getPeerName(), peerConfig.getPeerName())) {
				// TODO enqueue route withdrawn update
			}
		}
	}
	
	private FSMChannelImpl findWrapperForChannel(Channel channel) {
		FSMChannelImpl wrapper = null;
		
		for(FSMChannelImpl impl : managedChannels) {
			if(impl.getChannel().equals(channel)) {
				wrapper = impl;
				break;
			}
		}
		
		return wrapper;
	}
	
	/**
	 * process the UPDATE packet received from the remote peer
	 * 
	 * @param message
	 */
	@SuppressWarnings("unchecked")
	private void processRemoteUpdate(UpdatePacket message) {
		Set<MultiProtocolReachableNLRI> mpReachables = message.lookupPathAttributes(MultiProtocolReachableNLRI.class);
		Set<MultiProtocolUnreachableNLRI> mpUnreachables = message.lookupPathAttributes(MultiProtocolUnreachableNLRI.class);
		Set<PathAttribute> otherAttributes = message.filterPathAttributes(MultiProtocolReachableNLRI.class, 
				MultiProtocolUnreachableNLRI.class);
		AddressFamilyKey ipv4Unicast = new AddressFamilyKey(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING);
		
		if(mpReachables.size() > 0)
			processRemoteUpdateMultiProtocolReachables(mpReachables, otherAttributes);
		if(mpUnreachables.size() > 0)
			processRemoteUp(mpUnreachables, otherAttributes);
		
		// withdraw IPv4 prefixes
		prib.routingBase(RIBSide.Remote, ipv4Unicast).withdrawRoutes(message.getWithdrawnRoutes());
		
		Set<NextHopPathAttribute> nextHops = message.lookupPathAttributes(NextHopPathAttribute.class);
		
		if(nextHops.size() > 1)
			throw new InvalidNextHopException();
		
		prib.routingBase(RIBSide.Remote, ipv4Unicast).addRoutes(message.getNlris(), otherAttributes, nextHops.iterator().next().getNextHop());
		
	}

	private void processRemoteUp(Set<MultiProtocolUnreachableNLRI> mpUnreachables, Set<PathAttribute> attrs) {
		// TODO Auto-generated method stub
		
	}

	private void processRemoteUpdateMultiProtocolReachables(Set<MultiProtocolReachableNLRI> mpReachables, Set<PathAttribute> attrs) {
		// TODO Auto-generated method stub
		
	}
}
