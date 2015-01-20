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
 * File: org.bgp4j.netty.protocol.open.CapabilityCodec.java 
 */
package org.bgp4j.netty.protocol.open;

import io.netty.buffer.ByteBuf;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.bgp4j.net.AddressFamily;
import org.bgp4j.net.ORFSendReceive;
import org.bgp4j.net.ORFType;
import org.bgp4j.net.SubsequentAddressFamily;
import org.bgp4j.net.capabilities.AutonomousSystem4Capability;
import org.bgp4j.net.capabilities.Capability;
import org.bgp4j.net.capabilities.MultiProtocolCapability;
import org.bgp4j.net.capabilities.OutboundRouteFilteringCapability;
import org.bgp4j.net.capabilities.RouteRefreshCapability;
import org.bgp4j.net.capabilities.UnknownCapability;
import org.bgp4j.netty.BGPv4Constants;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class CapabilityCodec {

	public static List<Capability> decodeCapabilities(ByteBuf buffer) {
		List<Capability> caps = new LinkedList<Capability>();
		
		while(buffer.isReadable()) {
			caps.add(decodeCapability(buffer));
		}
		
		return caps;
	}

	public static Capability decodeCapability(ByteBuf buffer) { 
		Capability cap = null;
	
		try {
			buffer.markReaderIndex();
			
			int type = buffer.readUnsignedByte();
			
			switch(type) {
			case BGPv4Constants.BGP_CAPABILITY_TYPE_MULTIPROTOCOL:
				cap = decodeMultiProtocolCapability(buffer);
				break;
			case BGPv4Constants.BGP_CAPABILITY_TYPE_ROUTE_REFRESH:
				cap = decodeRouteRefreshCapability(buffer);
				break;
			case BGPv4Constants.BGP_CAPABILITY_TYPE_AS4_NUMBERS:
				cap = decodeAutonomousSystem4Capability(buffer);
				break;
			case BGPv4Constants.BGP_CAPABILITY_TYPE_OUTBOUND_ROUTE_FILTERING:
				cap = decodeOutboundRouteFilteringCapability(buffer);
				break;
			default:
				cap = decodeUnknownCapability(type, buffer);
				break;
			}
		} catch(CapabilityException e) {
			buffer.resetReaderIndex();
			
			int type = buffer.readUnsignedByte();
			int capLength = buffer.readUnsignedByte();
			
			byte[] capPacket = new byte[capLength+2];
			
			buffer.readBytes(capPacket, 2, capLength);
			capPacket[0] = (byte)type;
			capPacket[1] = (byte)capLength;
			
			e.setCapability(capPacket);
			throw e;
		}
		
		return cap;
	}

	private static Capability decodeUnknownCapability(int type, ByteBuf buffer) {
		UnknownCapability cap = new UnknownCapability();
		
		cap.setCapabilityType(type);
		int parameterLength = buffer.readUnsignedByte();
		
		if(parameterLength > 0) {
			byte[] value = new byte[parameterLength];
			
			buffer.readBytes(value);
			cap.setValue(value);
		}

		return cap;
	}

	private static Capability decodeOutboundRouteFilteringCapability(ByteBuf buffer) {
		OutboundRouteFilteringCapability cap = new OutboundRouteFilteringCapability();
		
		assertMinimalLength(buffer, 5); // 2 octest AFI + 1 octet reserved + 1 octet SAFI + 1 octet number of (ORF type, Send/Receive) tuples
		
		cap.setAddressFamily(AddressFamily.fromCode(buffer.readUnsignedShort()));
		buffer.readByte();
		cap.setSubsequentAddressFamily(SubsequentAddressFamily.fromCode(buffer.readUnsignedByte()));
		
		int orfs = buffer.readUnsignedByte();
		
		if(buffer.readableBytes() != 2*orfs)
			throw new UnspecificOpenPacketException("Expected " + (2*orfs) + " octets parameter, got " + buffer.readableBytes() + " octets");
		
		try {
			cap.getFilters().put(ORFType.fromCode(buffer.readUnsignedByte()), ORFSendReceive.fromCode(buffer.readUnsignedByte()));
		} catch(IllegalArgumentException e) {
			throw new UnspecificOpenPacketException(e);
		}
		return cap;
	}

	private static Capability decodeAutonomousSystem4Capability(ByteBuf buffer) {
		AutonomousSystem4Capability cap = new AutonomousSystem4Capability();
		
		assertFixedLength(buffer, BGPv4Constants.BGP_CAPABILITY_LENGTH_AS4_NUMBERS);
		cap.setAutonomousSystem((int)buffer.readUnsignedInt());

		return cap;
	}

	private static Capability decodeRouteRefreshCapability(ByteBuf buffer) {
		RouteRefreshCapability cap = new RouteRefreshCapability();
		
		assertEmptyParameter(buffer);

		return cap;
	}

	private static Capability decodeMultiProtocolCapability(ByteBuf buffer) {
		MultiProtocolCapability cap = new MultiProtocolCapability();
		
		assertFixedLength(buffer, BGPv4Constants.BGP_CAPABILITY_LENGTH_MULTIPROTOCOL);
		
		cap.setAfi(AddressFamily.fromCode(buffer.readShort()));
		buffer.readByte(); // reserved
		cap.setSafi(SubsequentAddressFamily.fromCode(buffer.readByte()));

		return cap;
	}

	public static void encodeCapabilities(ByteBuf buffer, Collection<Capability> caps) {		
		if(caps != null) {
			for (Capability cap : caps)
				encodeCapability(buffer, cap);
		}
	}

	public static void encodeCapability(ByteBuf buffer, Capability cap) {
		if(cap instanceof MultiProtocolCapability) {
			buffer.writeByte(BGPv4Constants.BGP_CAPABILITY_TYPE_MULTIPROTOCOL);			
			encodeMultiprotocolCapability(buffer, (MultiProtocolCapability)cap);
		} else if(cap instanceof RouteRefreshCapability) {
			buffer.writeByte(BGPv4Constants.BGP_CAPABILITY_TYPE_ROUTE_REFRESH);			
			encodeRouteRefreshCapability(buffer, (RouteRefreshCapability)cap);
		} else if(cap instanceof AutonomousSystem4Capability) {
			buffer.writeByte(BGPv4Constants.BGP_CAPABILITY_TYPE_AS4_NUMBERS);			
			encodeAutonomousSystem4Capability(buffer, (AutonomousSystem4Capability)cap);
		} else if(cap instanceof OutboundRouteFilteringCapability) {
			buffer.writeByte(BGPv4Constants.BGP_CAPABILITY_TYPE_OUTBOUND_ROUTE_FILTERING);			
			encodeOutboundRouteFilteringCapability(buffer, (OutboundRouteFilteringCapability)cap);
		} else if(cap instanceof UnknownCapability) {
			buffer.writeByte(((UnknownCapability)cap).getCapabilityType());			
			encodeUnknownCapability(buffer, (UnknownCapability)cap);
		}

	}

	private static void encodeUnknownCapability(ByteBuf buffer, UnknownCapability cap) {
		if(cap.getValue() != null && cap.getValue().length > 0) {
			buffer.writeByte(cap.getValue().length);			
			buffer.writeBytes(cap.getValue());
		}
	}

	private static void encodeOutboundRouteFilteringCapability(
			ByteBuf buffer, OutboundRouteFilteringCapability cap) {
		buffer.writeByte(5 + 2*cap.getFilters().size());
		
		buffer.writeShort(cap.getAddressFamily().toCode());
		buffer.writeByte(0);
		buffer.writeByte(cap.getSubsequentAddressFamily().toCode());
		buffer.writeByte(cap.getFilters().size());
		
		for(ORFType type : cap.getFilters().keySet()) {
			buffer.writeByte(type.toCode());
			buffer.writeByte(cap.getFilters().get(type).toCode());
		}
	}

	private static void encodeAutonomousSystem4Capability(ByteBuf buffer, AutonomousSystem4Capability cap) {
		buffer.writeByte(4);
		buffer.writeInt(cap.getAutonomousSystem());
	}

	private static void encodeRouteRefreshCapability(ByteBuf buffer, RouteRefreshCapability cap) {
		buffer.writeByte(0);
	}

	private static ByteBuf encodeMultiprotocolCapability(ByteBuf buffer, MultiProtocolCapability cap) {
		buffer.writeByte(4);
		
		if(cap.getAfi() != null)
			buffer.writeShort(cap.getAfi().toCode());
		else
			buffer.writeShort(AddressFamily.RESERVED.toCode());
		
		buffer.writeByte(0); // reserved
		
		if(cap.getSafi() != null)
			buffer.writeByte(cap.getSafi().toCode());
		else
			buffer.writeByte(0);
		
		return buffer;
	}
	
	private static final void assertEmptyParameter(ByteBuf buffer) {
		int parameterLength = buffer.readUnsignedByte();
		
		if(parameterLength != 0)
			throw new UnspecificOpenPacketException("Expected zero-length parameter, got " + parameterLength + " octets");
	}

	private static final void assertFixedLength(ByteBuf buffer, int length) {
		int parameterLength = buffer.readUnsignedByte();
		
		if(parameterLength != length)
			throw new UnspecificOpenPacketException("Expected " + length + " octets parameter, got " + parameterLength + " octets");
	}

	private static final void assertMinimalLength(ByteBuf buffer, int length) {
		int parameterLength = buffer.readUnsignedByte();
		
		if(parameterLength < length)
			throw new UnspecificOpenPacketException("Expected " + length + " octets parameter, got " + parameterLength + " octets");
	}

}
