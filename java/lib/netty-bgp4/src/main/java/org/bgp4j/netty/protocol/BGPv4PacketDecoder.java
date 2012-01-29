/**
 * 
 */
package org.bgp4j.netty.protocol;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;

import org.bgp4j.netty.BGPv4Constants;
import org.bgp4j.netty.protocol.OriginPathAttribute.Origin;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

/**
 * @author rainer
 *
 */
public class BGPv4PacketDecoder {

	BGPv4Packet decodePacket(ChannelBuffer buffer) {
		int type = buffer.readUnsignedByte();
		BGPv4Packet packet = null;
		
		switch (type) {
		case BGPv4Constants.BGP_PACKET_TYPE_OPEN:
			packet = decodeOpenPacket(buffer);
			break;
		case BGPv4Constants.BGP_PACKET_TYPE_UPDATE:
			packet = decodeUpdatePacket(buffer);
			break;
		case BGPv4Constants.BGP_PACKET_TYPE_NOTIFICATION:
			packet = decodeNotificationPacket(buffer);
			break;
		case BGPv4Constants.BGP_PACKET_TYPE_KEEPALIVE:
			packet = decodeKeepalivePacket(buffer);
			break;
		case BGPv4Constants.BGP_PACKET_TYPE_ROUTE_REFRESH:
			break;
		default:
			throw new ProtocolTypeException(type);
		}
		
		return packet;
	}
	
	private BGPv4Packet decodeUpdatePacket(ChannelBuffer buffer) {
		UpdatePacket packet = new UpdatePacket();
		
		verifyPacketSize(buffer, BGPv4Constants.BGP_PACKET_MIN_SIZE_UPDATE, -1);
		
		int totalAvailable = buffer.readableBytes();
		
		// handle withdrawn routes
		int withdrawnOctets = buffer.readUnsignedShort();
		
		if(withdrawnOctets > 0) {
			ChannelBuffer withdrawnBuffer = ChannelBuffers.buffer(withdrawnOctets);
			
			buffer.readBytes(withdrawnBuffer);
			
			try {
				packet.getWithdrawnRoutes().addAll(decodeWithdrawnRoutes(withdrawnBuffer));
			} catch(IndexOutOfBoundsException e) {
				throw new MessageLengthException(withdrawnOctets);
			}
		}
		
		// handle path attributes
		int pathAttributeOctets =  buffer.readUnsignedShort();
		
		if(pathAttributeOctets > 0) {
			ChannelBuffer pathAttributesBuffer = ChannelBuffers.buffer(pathAttributeOctets);
			
			buffer.readBytes(pathAttributesBuffer);
			try {
				packet.getPathAttributes().addAll(decodePathAttributes(pathAttributesBuffer));
			} catch(IndexOutOfBoundsException ex) {
				throw new MalformedAttributeListException();
			}
			
			// now decode path attributes
		}
		
		// handle network layer reachability information
		try {
			while(buffer.readable()) {
				int prefixLength = buffer.readUnsignedByte();
				NetworkLayerReachabilityInformation nlri = new NetworkLayerReachabilityInformation();
				
				nlri.setPrefixLength(prefixLength);
				
				if(prefixLength > 0) {
					int prefixBytes = ((prefixLength-1)/8)+1;
					byte[] addressBytes = new byte[4];
					
					buffer.readBytes(addressBytes, 0, prefixBytes);
					
					try {
						nlri.setPrefix((Inet4Address)Inet4Address.getByAddress(addressBytes));
					} catch (UnknownHostException e) {
					}
				}
				
				packet.getNlris().add(nlri);
				
			}
		} catch (IndexOutOfBoundsException e) {
			throw new MessageLengthException(totalAvailable - (withdrawnOctets + pathAttributeOctets));
		}

		return packet;
	}
	
	private List<PathAttribute> decodePathAttributes(ChannelBuffer buffer) {
		List<PathAttribute> attributes = new LinkedList<PathAttribute>();
		
		while(buffer.readable()) {
			buffer.markReaderIndex();

			try {
				int flagsType = buffer.readUnsignedShort();
				boolean optional = ((flagsType & BGPv4Constants.BGP_PATH_ATTRIBUTE_OPTIONAL_BIT) != 0);
				boolean transitive = ((flagsType & BGPv4Constants.BGP_PATH_ATTRIBUTE_TRANSITIVE_BIT) != 0);
				boolean partial = ((flagsType & BGPv4Constants.BGP_PATH_ATTRIBUTE_PARTIAL_BIT) != 0);
				int typeCode = (flagsType & BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_MASK);
				int valueLength = 0;

				if ((flagsType & BGPv4Constants.BGP_PATH_ATTRIBUTE_EXTENDED_LENGTH_BIT) != 0)
					valueLength = buffer.readUnsignedShort();
				else
					valueLength = buffer.readUnsignedByte();

				ChannelBuffer valueBuffer = ChannelBuffers.buffer(valueLength);

				buffer.readBytes(valueBuffer);

				PathAttribute attr = null;
			
				switch (typeCode) {
				case BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AGGREGATOR:
					break;
				case BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS4_AGGREGATOR:
					break;
				case BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS4_PATH:
					break;
				case BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS_PATH:
					break;
				case BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_ATOMIC_AGGREGATE:
					break;
				case BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_COMMUNITIES:
					break;
				case BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_LOCAL_PREF:
					break;
				case BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_MULTI_EXIT_DISC:
					break;
				case BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_NEXT_HOP:
					break;
				case BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_ORIGIN:
					attr = decodeOriginPathAttribute(valueBuffer);
					break;
				default:
					attr = new UnknownPathAttribute(typeCode, valueBuffer);
					break;
				}
				attr.setOptional(optional);
				attr.setTransitive(transitive);
				attr.setPartial(partial);
				
				attributes.add(attr);
			} catch(PathAttributeException ex) {
				int endReadIndex = buffer.readerIndex();
				
				buffer.resetReaderIndex();
				
				int attributeLength = endReadIndex - buffer.readerIndex();
				byte[] packet = new byte[attributeLength];
				
				buffer.readBytes(packet);
				ex.setOffendingAttribute(packet);
				
				throw ex;
			} catch(IndexOutOfBoundsException ex) {
				int endReadIndex = buffer.readerIndex();
				
				buffer.resetReaderIndex();
				
				int attributeLength = endReadIndex - buffer.readerIndex();
				byte[] packet = new byte[attributeLength];
				
				buffer.readBytes(packet);

				throw new PathAttributeLengthException(packet);
			}
			
		}
		
		return attributes;
	}
	
	private OriginPathAttribute decodeOriginPathAttribute(ChannelBuffer buffer) {
		OriginPathAttribute attr = new OriginPathAttribute();
		
		if(buffer.readableBytes() != 1)
			throw new PathAttributeLengthException();
		
		try {
			attr.setOrigin(Origin.fromCode(buffer.readUnsignedByte()));
		} catch(IllegalArgumentException e) {
			throw new InvalidOriginException(e);
		}
		
		return attr;
	}
	
	private List<WithdrawnRoute> decodeWithdrawnRoutes(ChannelBuffer buffer)  {
		List<WithdrawnRoute> routes = new LinkedList<WithdrawnRoute>();
		
		while(buffer.readable()) {
			int prefixLength = buffer.readUnsignedByte();
			WithdrawnRoute route = new WithdrawnRoute();
			
			route.setPrefixLength(prefixLength);
			
			if(prefixLength > 0) {
				int prefixBytes = ((prefixLength-1)/8)+1;
				byte[] addressBytes = new byte[4];
				
				buffer.readBytes(addressBytes, 0, prefixBytes);
				
				try {
					route.setPrefix((Inet4Address)Inet4Address.getByAddress(addressBytes));
				} catch (UnknownHostException e) {
				}
			}
			
			routes.add(route);
			
		}
		return routes;
	}
	
	/**
	 * decode the NOTIFICATION network packet. The NOTIFICATION packet must be at least 2 octets large at this point.
	 * 
	 * @param buffer the buffer containing the data. 
	 * @return
	 */
	private BGPv4Packet decodeNotificationPacket(ChannelBuffer buffer) {
		NotificationPacket packet = null;
		
		verifyPacketSize(buffer, BGPv4Constants.BGP_PACKET_MIN_SIZE_NOTIFICATION, -1);
		
		int errorCode = buffer.readUnsignedByte();
		int errorSubcode = buffer.readUnsignedByte();
		
		switch(errorCode) {
		case BGPv4Constants.BGP_ERROR_CODE_MESSAGE_HEADER:
			packet = decodeMessageHeaderNotificationPacket(buffer, errorSubcode);
			break;
		case BGPv4Constants.BGP_ERROR_CODE_OPEN:
			packet = decodeOpenNotificationPacket(buffer, errorSubcode);
			break;
		case BGPv4Constants.BGP_ERROR_CODE_UPDATE:
			break;
		case BGPv4Constants.BGP_ERROR_CODE_HOLD_TIMER_EXPIRED:
			packet = new HoldTimerExpiredNotificationPacket();
			break;
		case BGPv4Constants.BGP_ERROR_CODE_FINITE_STATE_MACHINE_ERROR:
			packet = new FiniteStateMachineErrorNotificationPacket();
			break;
		case BGPv4Constants.BGP_ERROR_CODE_CEASE:
			packet = new CeaseNotificationPacket();
			break;
		}
		
		return packet;
	}

	private NotificationPacket decodeOpenNotificationPacket(ChannelBuffer buffer, int errorSubcode) {
		NotificationPacket packet = null;
		
		switch(errorSubcode) {
		case OpenNotificationPacket.SUBCODE_BAD_BGP_IDENTIFIER:
			packet = new BadBgpIdentifierNotificationPacket();
			break;
		case OpenNotificationPacket.SUBCODE_BAD_PEER_AS:
			packet = new BadPeerASNotificationPacket();
			break;
		case OpenNotificationPacket.SUBCODE_UNACCEPTABLE_HOLD_TIMER:
			packet = new UnacceptableHoldTimerNotificationPacket();
			break;
		case OpenNotificationPacket.SUBCODE_UNSPECIFIC:
			packet = new UnspecificOpenNotificationPacket();
			break;
		case OpenNotificationPacket.SUBCODE_UNSUPPORTED_OPTIONAL_PARAMETER:
			packet = new UnsupportedOptionalParameterNotificationPacket();
			break;
		case OpenNotificationPacket.SUBCODE_UNSUPPORTED_VERSION_NUMBER:
			packet = new UnsupportedVersionNumberNotificationPacket(buffer.readUnsignedShort());
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
	private NotificationPacket decodeMessageHeaderNotificationPacket(ChannelBuffer buffer, int errorSubcode) {
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
	 * decode the OPEN network packet. The OPEN packet must be at least 10 octets large at this point.
	 * 
	 * @param buffer the buffer containing the data. 
	 * @return
	 */
	private OpenPacket decodeOpenPacket(ChannelBuffer buffer) {
		OpenPacket packet = new OpenPacket();
		
		verifyPacketSize(buffer, BGPv4Constants.BGP_PACKET_MIN_SIZE_OPEN, -1);
		
		packet.setProtocolVersion(buffer.readUnsignedByte());
		packet.setAutonomousSystem(buffer.readUnsignedShort());
		packet.setHoldTime(buffer.readUnsignedShort());
		packet.setAutonomousSystem(buffer.readInt());
		
		int capabilitiesLength = buffer.readUnsignedByte();
		
		if(capabilitiesLength > 0) {
			while(buffer.readable()) {
				packet.getCapabilities().add(Capability.decodeCapability(buffer));
			}
		}
		
		for(Capability cap : packet.getCapabilities()) {
			if(cap instanceof AutonomousSystem4Capability) {
				packet.setAs4AutonomousSystem(((AutonomousSystem4Capability)cap).getAutonomousSystem());
			}
		}
		
		return packet;
	}

	/**
	 * decode the KEEPALIVE network packet. The OPEN packet must be exactly 0 octets large at this point.
	 * 
	 * @param buffer the buffer containing the data. 
	 * @return
	 */
	private KeepalivePacket decodeKeepalivePacket(ChannelBuffer buffer) {
		KeepalivePacket packet = new KeepalivePacket();
		
		verifyPacketSize(buffer, BGPv4Constants.BGP_PACKET_SIZE_KEEPALIVE, BGPv4Constants.BGP_PACKET_SIZE_KEEPALIVE);
		
		return packet;
	}

	/**
	 * verify the packet size.
	 * 
	 * @param minimumPacketSize the minimum size in octets the protocol packet must have to be well-formed. If <b>-1</b> is passed the check is not performed.
	 * @param maximumPacketSize the maximum size in octets the protocol packet may have to be well-formed. If <b>-1</b> is passed the check is not performed.
	 */
	private void verifyPacketSize(ChannelBuffer buffer, int minimumPacketSize, int maximumPacketSize) {
		if(minimumPacketSize != -1) {
			if(buffer.readableBytes() < (minimumPacketSize - BGPv4Constants.BGP_PACKET_HEADER_LENGTH)) {
				throw new MessageLengthException("expected minimum " + (minimumPacketSize - BGPv4Constants.BGP_PACKET_HEADER_LENGTH) 
						+ "octest, received " + buffer.readableBytes() + "octets", buffer.readableBytes());
			}
		}
		if(maximumPacketSize != -1) {
			if(buffer.readableBytes() > (maximumPacketSize - BGPv4Constants.BGP_PACKET_HEADER_LENGTH)) {
				throw new MessageLengthException("expected maximum " + (maximumPacketSize - BGPv4Constants.BGP_PACKET_HEADER_LENGTH) 
						+ "octest, received " + buffer.readableBytes() + "octets", buffer.readableBytes());
			}
		}
	}	
}
