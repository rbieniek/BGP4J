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
package org.bgp4j.netty.protocol.open;

import java.util.LinkedList;
import java.util.List;

import org.bgp4j.netty.BGPv4Constants;
import org.bgp4j.netty.protocol.BGPv4Packet;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class OpenPacket extends BGPv4Packet {
	private int protocolVersion;
	private int autonomousSystem;
	private int as4AutonomousSystem = -1;
	private int holdTime;
	private int bgpIdentifier;
	private List<Capability> capabilities = new LinkedList<Capability>();
	
	/**
	 * @return the protocolVersion
	 */
	public int getProtocolVersion() {
		return protocolVersion;
	}
	
	/**
	 * @param protocolVersion the protocolVersion to set
	 */
	public void setProtocolVersion(int protocolVersion) {
		this.protocolVersion = protocolVersion;
	}
	
	/**
	 * @return the autonomuosSystem
	 */
	public int getAutonomousSystem() {
		return autonomousSystem;
	}
	
	/**
	 * @param autonomuosSystem the autonomuosSystem to set
	 */
	public void setAutonomousSystem(int autonomuosSystem) {
		this.autonomousSystem = autonomuosSystem;
	}
	
	/**
	 * @return the holdTime
	 */
	public int getHoldTime() {
		return holdTime;
	}
	
	/**
	 * @param holdTime the holdTime to set
	 */
	public void setHoldTime(int holdTime) {
		this.holdTime = holdTime;
	}
	
	/**
	 * @return the bgpIdentifier
	 */
	public int getBgpIdentifier() {
		return bgpIdentifier;
	}
	
	/**
	 * @param bgpIdentifier the bgpIdentifier to set
	 */
	public void setBgpIdentifier(int bgpIdentifier) {
		this.bgpIdentifier = bgpIdentifier;
	}
	
	/**
	 * @return the capabilities
	 */
	public List<Capability> getCapabilities() {
		return capabilities;
	}
	
	/**
	 * @param capabilities the capabilities to set
	 */
	public void setCapabilities(List<Capability> capabilities) {
		this.capabilities = capabilities;
	}

	@Override
	protected ChannelBuffer encodePayload() {
		ChannelBuffer buffer = ChannelBuffers.buffer(BGPv4Constants.BGP_PACKET_MAX_LENGTH);
		
		buffer.writeByte(getProtocolVersion());
		buffer.writeShort(getAutonomousSystem());
		buffer.writeShort(getHoldTime());
		buffer.writeInt(getBgpIdentifier());
		
		ChannelBuffer capabilities = Capability.encodeCapabilities(getCapabilities());
		
		if(capabilities.readableBytes() > 0) {
			buffer.writeByte(capabilities.readableBytes() + 2); // cap length + type byte + parameter length byte
			buffer.writeByte(BGPv4Constants.BGP_OPEN_PARAMETER_TYPE_CAPABILITY); // type byte
			buffer.writeByte(capabilities.readableBytes()); // parameter length
			buffer.writeBytes(capabilities);
		} else {
			buffer.writeByte(0); // no capabilites encoded --> optional parameter length equals 0
		}
		return buffer;
	}

	@Override
	protected int getType() {
		return BGPv4Constants.BGP_PACKET_TYPE_OPEN;
	}

	/**
	 * @return the as4AutonomousSystem
	 */
	public int getAs4AutonomousSystem() {
		return as4AutonomousSystem;
	}

	/**
	 * @param as4AutonomousSystem the as4AutonomousSystem to set
	 */
	public void setAs4AutonomousSystem(int as4AutonomousSystem) {
		this.as4AutonomousSystem = as4AutonomousSystem;
	}
	
	/**
	 * get the effective autonomous system number. RFC 4893 defines that the AS OPEN field carries the
	 * magic number AS_TRANS and the the four-byte AS number is carried in capability field if the speakers
	 * support four-byte AS numbers
	 * 
	 * @return
	 */
	public int getEffectiveAutonomousSystem() {
		if(getAutonomousSystem() == BGPv4Constants.BGP_AS_TRANS)
			return getAs4AutonomousSystem();
		else
			return getAutonomousSystem();
	}
}
