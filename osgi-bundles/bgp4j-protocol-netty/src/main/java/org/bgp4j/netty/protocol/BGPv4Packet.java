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

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.bgp4j.netty.BGPv4Constants;

/**
 * base class for all BGPv4 protocol packets
 * 
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public abstract class BGPv4Packet {
	/**
	 * build a binary representation of the protocol packet
	 * 
	 * @param buffer The buffer to store the binary packet representation into
	 */
	public void encodePacket(ByteBuf buffer) {
		for(int i=0; i<BGPv4Constants.BGP_PACKET_MARKER_LENGTH; i++)
			buffer.writeByte(0xff);

		int headerHeaderIndex = buffer.writerIndex();

		buffer.writeShort(BGPv4Constants.BGP_PACKET_HEADER_LENGTH);
		
		buffer.writeByte(getType());
		encodePayload(buffer);
		
		buffer.setShort(headerHeaderIndex, BGPv4Constants.BGP_PACKET_HEADER_LENGTH + (buffer.writerIndex() - headerHeaderIndex) - 3);
	}
	
	/**
	 * encode the specific packet-type payload
	 * 
	 * @return the encoded packet payload
	 */
	protected abstract void encodePayload(ByteBuf buffer);
	
	/**
	 * obtain the BGP packet type code.
	 * 
	 * @return
	 */
	public abstract int getType();
		
	@Override
	public String toString() {
		return (new ToStringBuilder(this)).append("type", getType()).toString();
	}
}
