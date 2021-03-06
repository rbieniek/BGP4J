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
package org.bgp4j.netty.protocol;


import io.netty.buffer.ByteBuf;

import org.bgp4j.net.AddressFamily;
import org.bgp4j.net.BGPv4Constants;
import org.bgp4j.net.SubsequentAddressFamily;
import org.bgp4j.net.packets.AdministrativeResetNotificationPacket;
import org.bgp4j.net.packets.AdministrativeShutdownNotificationPacket;
import org.bgp4j.net.packets.BGPv4Packet;
import org.bgp4j.net.packets.BadMessageLengthNotificationPacket;
import org.bgp4j.net.packets.BadMessageTypeNotificationPacket;
import org.bgp4j.net.packets.CeaseNotificationPacket;
import org.bgp4j.net.packets.ConnectionCollisionResolutionNotificationPacket;
import org.bgp4j.net.packets.ConnectionNotSynchronizedNotificationPacket;
import org.bgp4j.net.packets.ConnectionRejectedNotificationPacket;
import org.bgp4j.net.packets.FiniteStateMachineErrorNotificationPacket;
import org.bgp4j.net.packets.HoldTimerExpiredNotificationPacket;
import org.bgp4j.net.packets.KeepalivePacket;
import org.bgp4j.net.packets.MaximumNumberOfPrefixesReachedNotificationPacket;
import org.bgp4j.net.packets.MessageHeaderErrorNotificationPacket;
import org.bgp4j.net.packets.NotificationPacket;
import org.bgp4j.net.packets.OtherConfigurationChangeNotificationPacket;
import org.bgp4j.net.packets.OutOfResourcesNotificationPacket;
import org.bgp4j.net.packets.PeerDeconfiguredNotificationPacket;
import org.bgp4j.net.packets.UnspecifiedCeaseNotificationPacket;
import org.bgp4j.netty.protocol.open.OpenPacketDecoder;
import org.bgp4j.netty.protocol.refresh.RouteRefreshPacketDecoder;
import org.bgp4j.netty.protocol.update.UpdatePacketDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 * Decode byte buffers into BGP v4 packet objects
 */
public class BGPv4PacketDecoder {
	private Logger log = LoggerFactory.getLogger(BGPv4PacketDecoder.class);
	private OpenPacketDecoder openPacketDecoder = new OpenPacketDecoder();
	private UpdatePacketDecoder updatePacketDecoder = new UpdatePacketDecoder();
	private RouteRefreshPacketDecoder routeRefreshPacketDecoder = new RouteRefreshPacketDecoder();
	
	public BGPv4Packet decodePacket(ByteBuf buffer) {
		int type = buffer.readUnsignedByte();
		BGPv4Packet packet = null;
		
		switch (type) {
		case BGPv4Constants.BGP_PACKET_TYPE_OPEN:
			packet = openPacketDecoder.decodeOpenPacket(buffer);
			break;
		case BGPv4Constants.BGP_PACKET_TYPE_UPDATE:
			packet = updatePacketDecoder.decodeUpdatePacket(buffer);
			break;
		case BGPv4Constants.BGP_PACKET_TYPE_NOTIFICATION:
			packet = decodeNotificationPacket(buffer);
			break;
		case BGPv4Constants.BGP_PACKET_TYPE_KEEPALIVE:
			packet = decodeKeepalivePacket(buffer);
			break;
		case BGPv4Constants.BGP_PACKET_TYPE_ROUTE_REFRESH:
			packet = routeRefreshPacketDecoder.decodeRouteRefreshPacket(buffer);
			break;
		default:
			throw new BadMessageTypeException(type);
		}
		
		return packet;
	}
	
	/**
	 * decode the NOTIFICATION network packet. The NOTIFICATION packet must be at least 2 octets large at this point.
	 * 
	 * @param buffer the buffer containing the data. 
	 * @return
	 */
	private BGPv4Packet decodeNotificationPacket(ByteBuf buffer) {
		NotificationPacket packet = null;
		
		ProtocolPacketUtils.verifyPacketSize(buffer, BGPv4Constants.BGP_PACKET_MIN_SIZE_NOTIFICATION, -1);
		
		int errorCode = buffer.readUnsignedByte();
		int errorSubcode = buffer.readUnsignedByte();
		
		switch(errorCode) {
		case BGPv4Constants.BGP_ERROR_CODE_MESSAGE_HEADER:
			packet = decodeMessageHeaderNotificationPacket(buffer, errorSubcode);
			break;
		case BGPv4Constants.BGP_ERROR_CODE_OPEN:
			packet = openPacketDecoder.decodeOpenNotificationPacket(buffer, errorSubcode);
			break;
		case BGPv4Constants.BGP_ERROR_CODE_UPDATE:
			packet = updatePacketDecoder.decodeUpdateNotification(buffer, errorSubcode);
			break;
		case BGPv4Constants.BGP_ERROR_CODE_HOLD_TIMER_EXPIRED:
			packet = new HoldTimerExpiredNotificationPacket();
			break;
		case BGPv4Constants.BGP_ERROR_CODE_FINITE_STATE_MACHINE_ERROR:
			packet = new FiniteStateMachineErrorNotificationPacket();
			break;
		case BGPv4Constants.BGP_ERROR_CODE_CEASE:
			packet = decodeCeaseNotificationPacket(buffer, errorSubcode);
			break;
		}
		
		return packet;
	}

	/**
	 * decode the NOTIFICATION network packet for error code "Message Header Error". 
	 * 
	 * @param buffer the buffer containing the data. 
	 * @return
	 */
	private NotificationPacket decodeMessageHeaderNotificationPacket(ByteBuf buffer, int errorSubcode) {
		NotificationPacket packet = null;
		
		switch(errorSubcode) {
		case MessageHeaderErrorNotificationPacket.SUBCODE_CONNECTION_NOT_SYNCHRONIZED:
			packet = new ConnectionNotSynchronizedNotificationPacket();
			break;
		case MessageHeaderErrorNotificationPacket.SUBCODE_BAD_MESSAGE_LENGTH:
			packet = new BadMessageLengthNotificationPacket(buffer.readUnsignedShort());
			break;
		case MessageHeaderErrorNotificationPacket.SUBCODE_BAD_MESSAGE_TYPE:
			packet = new BadMessageTypeNotificationPacket(buffer.readUnsignedByte());
			break;
		}
		
		return packet;
	}

	/**
	 * decode the NOTIFICATION network packet for error code "Cease". 
	 * 
	 * @param buffer the buffer containing the data. 
	 * @return
	 */
	private NotificationPacket decodeCeaseNotificationPacket(ByteBuf buffer, int errorSubcode) {
		NotificationPacket packet = null;
		
		switch(errorSubcode) {
		default:
			log.info("cannot handle cease notification subcode {}", errorSubcode);
		case CeaseNotificationPacket.SUBCODE_UNSPECIFIC:
			packet = new UnspecifiedCeaseNotificationPacket();
			break;
		case CeaseNotificationPacket.SUBCODE_MAXIMUM_NUMBER_OF_PREFIXES_REACHED:
			packet = new MaximumNumberOfPrefixesReachedNotificationPacket();

			try {
				AddressFamily afi = AddressFamily.fromCode(buffer.readUnsignedShort());
				SubsequentAddressFamily safi = SubsequentAddressFamily.fromCode(buffer.readUnsignedByte());
				int prefixUpperBounds = (int)buffer.readUnsignedInt();
				
				packet = new MaximumNumberOfPrefixesReachedNotificationPacket(afi, safi, prefixUpperBounds);
			} catch(RuntimeException e) {
				log.info("cannot decode specific reason for CEASE maximum number of prefixes reached notification", e);
			}
			break;
		case CeaseNotificationPacket.SUBCODE_ADMINSTRATIVE_SHUTDOWN:
			packet = new AdministrativeShutdownNotificationPacket();
			break;
		case CeaseNotificationPacket.SUBCODE_PEER_DECONFIGURED:
			packet = new PeerDeconfiguredNotificationPacket();
			break;
		case CeaseNotificationPacket.SUBCODE_ADMINSTRATIVE_RESET:
			packet = new AdministrativeResetNotificationPacket();
			break;
		case CeaseNotificationPacket.SUBCODE_CONNECTION_REJECTED:
			packet = new ConnectionRejectedNotificationPacket();
			break;
		case CeaseNotificationPacket.SUBCODE_OTHER_CONFIGURATION_CHANGE:
			packet = new OtherConfigurationChangeNotificationPacket();
			break;
		case CeaseNotificationPacket.SUBCODE_CONNECTION_COLLISION_RESOLUTION:
			packet = new ConnectionCollisionResolutionNotificationPacket();
			break;
		case CeaseNotificationPacket.SUBCODE_OUT_OF_RESOURCES:
			packet = new OutOfResourcesNotificationPacket();
			break;
		}
		
		return packet;
	}

	/**
	 * decode the KEEPALIVE network packet. The OPEN packet must be exactly 0 octets large at this point.
	 * 
	 * @param buffer the buffer containing the data. 
	 * @return
	 */
	private KeepalivePacket decodeKeepalivePacket(ByteBuf buffer) {
		KeepalivePacket packet = new KeepalivePacket();
		
		ProtocolPacketUtils.verifyPacketSize(buffer, BGPv4Constants.BGP_PACKET_SIZE_KEEPALIVE, BGPv4Constants.BGP_PACKET_SIZE_KEEPALIVE);
		
		return packet;
	}	
}
