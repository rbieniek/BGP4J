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
 * File: org.bgp4j.netty.protocol.update.UpdatePacketDecoder.java 
 */
package org.bgp4j.netty.protocol.update;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import org.bgp4j.netty.BGPv4Constants;
import org.bgp4j.netty.NetworkLayerReachabilityInformation;
import org.bgp4j.netty.protocol.BGPv4Packet;
import org.bgp4j.netty.protocol.BadMessageLengthException;
import org.bgp4j.netty.protocol.NotificationPacket;
import org.bgp4j.netty.protocol.ProtocolPacketUtils;
import org.bgp4j.netty.protocol.update.OriginPathAttribute.Origin;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.slf4j.Logger;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class UpdatePacketDecoder {
	private @Inject Logger log;

	private ASPathAttribute decodeASPathAttribute(ChannelBuffer buffer, boolean fourByteASNumbers) {
		ASPathAttribute attr = new ASPathAttribute(fourByteASNumbers);
		
		try {
			attr.setType(ASPathAttribute.Type.fromCode(buffer.readUnsignedByte()));
			
			int asCount = buffer.readUnsignedByte();
			
			for(int i=0; i<asCount; i++) {
				int as;
				
				if(fourByteASNumbers)
					as = (int)buffer.readUnsignedInt();
				else
					as = buffer.readUnsignedShort();
				
				attr.getAses().add(as);
			}
	
			// if there are more octets to read at this point, the packet is malformed
			if(buffer.readable())
				throw new MalformedASPathAttributeException();
		} catch(IllegalArgumentException e) {
			log.error("cannot convert AS_PATH type", e);
			
			throw new MalformedASPathAttributeException();
		} catch(IndexOutOfBoundsException e) {
			log.error("short AS_PATH attribute", e);
			
			throw new MalformedASPathAttributeException();
		}
		
		return attr;
	}

	private OriginPathAttribute decodeOriginPathAttribute(ChannelBuffer buffer) {
		OriginPathAttribute attr = new OriginPathAttribute();
		
		if(buffer.readableBytes() != 1)
			throw new AttributeLengthException();
		
		try {
			attr.setOrigin(Origin.fromCode(buffer.readUnsignedByte()));
		} catch(IllegalArgumentException e) {
			log.error("cannot convert ORIGIN code", e);
			
			throw new InvalidOriginException();
		}
		
		return attr;
	}

	private List<Attribute> decodePathAttributes(ChannelBuffer buffer) {
		List<Attribute> attributes = new LinkedList<Attribute>();
		
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
	
				Attribute attr = null;
			
				switch (typeCode) {
				case BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AGGREGATOR:
					break;
				case BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS4_AGGREGATOR:
					break;
				case BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS4_PATH:
					attr = decodeASPathAttribute(valueBuffer, true);
					break;
				case BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS_PATH:
					attr = decodeASPathAttribute(valueBuffer, false);
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
			} catch(AttributeException ex) {
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
	
				throw new AttributeLengthException(packet);
			}
			
		}
		
		return attributes;
	}

	public BGPv4Packet decodeUpdatePacket(ChannelBuffer buffer) {
		UpdatePacket packet = new UpdatePacket();
		
		ProtocolPacketUtils.verifyPacketSize(buffer, BGPv4Constants.BGP_PACKET_MIN_SIZE_UPDATE, -1);
		
		int totalAvailable = buffer.readableBytes();
		
		// handle withdrawn routes
		int withdrawnOctets = buffer.readUnsignedShort();
		
		if(withdrawnOctets > 0) {
			ChannelBuffer withdrawnBuffer = ChannelBuffers.buffer(withdrawnOctets);
			
			buffer.readBytes(withdrawnBuffer);
			
			try {
				packet.getWithdrawnRoutes().addAll(decodeWithdrawnRoutes(withdrawnBuffer));
			} catch(IndexOutOfBoundsException e) {
				throw new BadMessageLengthException(withdrawnOctets);
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
				packet.getNlris().add(NetworkLayerReachabilityInformation.decodeNLRI(buffer));
			}
		} catch (IndexOutOfBoundsException e) {
			throw new BadMessageLengthException(totalAvailable - (withdrawnOctets + pathAttributeOctets));
		}
	
		return packet;
	}

	private List<NetworkLayerReachabilityInformation> decodeWithdrawnRoutes(ChannelBuffer buffer)  {
		List<NetworkLayerReachabilityInformation> routes = new LinkedList<NetworkLayerReachabilityInformation>();
		
		while(buffer.readable()) {
			routes.add(NetworkLayerReachabilityInformation.decodeNLRI(buffer));			
		}
		return routes;
	}

	public NotificationPacket decodeUpdateNotification(ChannelBuffer buffer, int errorSubcode) {
		return null;
	}

}
