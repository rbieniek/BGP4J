/**
 * 
 */
package org.bgp4j.netty.protocol;

import org.bgp4j.net.BGPv4Constants;
import org.bgp4j.net.packets.BGPv4Packet;
import org.bgp4j.net.packets.CeaseNotificationPacket;
import org.bgp4j.net.packets.MessageHeaderErrorNotificationPacket;
import org.bgp4j.net.packets.NotificationPacket;
import org.bgp4j.net.packets.open.OpenNotificationPacket;
import org.bgp4j.net.packets.update.UpdateNotificationPacket;
import org.bgp4j.netty.protocol.open.OpenPacketEncoder;
import org.bgp4j.netty.protocol.open.UnsupportedCapabilityNotificationPacketEncoder;
import org.bgp4j.netty.protocol.open.UnsupportedVersionNumberNotificationPacketEncoder;
import org.bgp4j.netty.protocol.refresh.RouteRefreshPacketEncoder;
import org.bgp4j.netty.protocol.update.MissingWellKnownAttributeNotificationPacketEncoder;
import org.bgp4j.netty.protocol.update.UpdatePacketEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author rainer
 *
 */
public class BGPv4PacketEncoderFactory {
	private static final Logger logger = LoggerFactory.getLogger(BGPv4PacketEncoderFactory.class);
	
	private NoPayloadPacketEncoder noPayloadEncoder = new NoPayloadPacketEncoder();
	private UpdatePacketEncoder updatePacketEncoder = new UpdatePacketEncoder();
	private OpenPacketEncoder openPacketEncoder = new OpenPacketEncoder();
	private RouteRefreshPacketEncoder routeRefreshPacketEncoder = new RouteRefreshPacketEncoder(); 
	
	public BGPv4PacketEncoderFactory() {
	}
	
	@SuppressWarnings("unchecked")
	public <T extends BGPv4Packet> BGPv4PacketEncoder<T> encoderForPacket(T packet) {
		BGPv4PacketEncoder<T> encoder = null;
		
		switch(packet.getType()) {
		case BGPv4Constants.BGP_PACKET_TYPE_OPEN:
			encoder = (BGPv4PacketEncoder<T>)openPacketEncoder;
			break;
		case BGPv4Constants.BGP_PACKET_TYPE_UPDATE:
			encoder = (BGPv4PacketEncoder<T>)updatePacketEncoder;
			break;
		case BGPv4Constants.BGP_PACKET_TYPE_NOTIFICATION:
			encoder = (BGPv4PacketEncoder<T>)encoderForNotificationPacket((NotificationPacket)packet);
			break;
		case BGPv4Constants.BGP_PACKET_TYPE_KEEPALIVE:
			encoder = (BGPv4PacketEncoder<T>)noPayloadEncoder;
			break;
		case BGPv4Constants.BGP_PACKET_TYPE_ROUTE_REFRESH:
			encoder = (BGPv4PacketEncoder<T>)routeRefreshPacketEncoder;
			break;
		}
		
		if(encoder == null) {
			logger.error("cannot find encoder for packet {}", packet);
			
			throw new IllegalArgumentException();
		}

		return encoder;
	}
	
	private NoPayloadNotificationPacketEncoder noPayloadNotificationPacketEncoder = new NoPayloadNotificationPacketEncoder();
	private BadMessageLengthNotificationPacketEncoder badMessageLengthEncoder = new BadMessageLengthNotificationPacketEncoder();
	private BadMessageTypeNotificationPacketEncoder badMessageTypeEncoder = new BadMessageTypeNotificationPacketEncoder();
	private MaximumNumberOfPrefixesReachedNotificationPacketEncoder maxNumberPrefixesEncoder = new MaximumNumberOfPrefixesReachedNotificationPacketEncoder();
	private UnsupportedVersionNumberNotificationPacketEncoder unsupportedVersionNumberEncoder = new UnsupportedVersionNumberNotificationPacketEncoder();
	private UnsupportedCapabilityNotificationPacketEncoder unsupportedCapabilityEncoder = new UnsupportedCapabilityNotificationPacketEncoder();
	private MissingWellKnownAttributeNotificationPacketEncoder missingWellKnownAttributeEncoder = new MissingWellKnownAttributeNotificationPacketEncoder();
	
	@SuppressWarnings("unchecked")
	private <T extends NotificationPacket> BGPv4PacketEncoder<T> encoderForNotificationPacket(T packet) {
		NotificationPacketEncoder<T> encoder = null;
		
		switch(packet.getErrorCode()) {
		case BGPv4Constants.BGP_ERROR_CODE_MESSAGE_HEADER:
			switch(packet.getErrorSubcode()) {
			case MessageHeaderErrorNotificationPacket.SUBCODE_BAD_MESSAGE_LENGTH:
				encoder = (NotificationPacketEncoder<T>)badMessageLengthEncoder;
				break;
			case MessageHeaderErrorNotificationPacket.SUBCODE_BAD_MESSAGE_TYPE:
				encoder = (NotificationPacketEncoder<T>)badMessageTypeEncoder;
				break;
			}
			break;
		case BGPv4Constants.BGP_ERROR_CODE_CEASE:
			switch(packet.getErrorSubcode()) {
			case CeaseNotificationPacket.SUBCODE_MAXIMUM_NUMBER_OF_PREFIXES_REACHED:
				encoder = (NotificationPacketEncoder<T>)maxNumberPrefixesEncoder;
				break;
			}
			break;
		case BGPv4Constants.BGP_ERROR_CODE_OPEN:
			switch(packet.getErrorSubcode()) {
			case OpenNotificationPacket.SUBCODE_UNSUPPORTED_CAPABILITY:
				encoder = (NotificationPacketEncoder<T>)unsupportedCapabilityEncoder;
				break;
			case OpenNotificationPacket.SUBCODE_UNSUPPORTED_VERSION_NUMBER:
				encoder = (NotificationPacketEncoder<T>)unsupportedVersionNumberEncoder;
				break;
			}
			break;
		case BGPv4Constants.BGP_ERROR_CODE_UPDATE:
			switch(packet.getErrorSubcode()) {
			case UpdateNotificationPacket.SUBCODE_MISSING_WELL_KNOWN_ATTRIBUTE:
				encoder = (NotificationPacketEncoder<T>)missingWellKnownAttributeEncoder;
				break;
			case UpdateNotificationPacket.SUBCODE_ATTRIBUTE_LENGTH_ERROR:
			case UpdateNotificationPacket.SUBCODE_INVALID_NEXT_HOP_ATTRIBUTE:
			case UpdateNotificationPacket.SUBCODE_INVALID_ORIGIN_ATTRIBUTE:
			case UpdateNotificationPacket.SUBCODE_MALFORMED_AS_PATH:
			case UpdateNotificationPacket.SUBCODE_OPTIONAL_ATTRIBUTE_ERROR:
			case UpdateNotificationPacket.SUBCODE_UNRECOGNIZED_WELL_KNOWN_ATTRIBUTE:
				break;
			}
			break;
		}

		if(encoder == null)
			encoder = (NotificationPacketEncoder<T>)noPayloadNotificationPacketEncoder;
		
		return encoder;
	}
}
