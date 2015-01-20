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
 * File: org.bgp4j.netty.NLRICodec.java 
 */
package org.bgp4j.netty;

import io.netty.buffer.ByteBuf;

import org.bgp4j.net.NetworkLayerReachabilityInformation;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 * Codec for handling network-layer reachability information
 */
public class NLRICodec {

	/**
	 * Decode a network-layer reachability information from a byte buffer
	 * 
	 * @param buffer byte buffer
	 * @return decoded network layer reachability information
	 */
	public static NetworkLayerReachabilityInformation decodeNLRI(ByteBuf buffer) {
		NetworkLayerReachabilityInformation nlri = new NetworkLayerReachabilityInformation();
		int prefixLength = buffer.readUnsignedByte();
		byte[] prefixBytes = null;
		
		if(prefixLength > 0) {
			prefixBytes = new byte[NetworkLayerReachabilityInformation.calculateOctetsForPrefixLength(prefixLength)];
			
			buffer.readBytes(prefixBytes);
		}
		nlri.setPrefix(prefixLength, prefixBytes);
		
		return nlri;
	}

	/**
	 * Calculate the length of the binary representation of a netwotk-lyer reachability information
	 * 
	 * @param nlri
	 * @return
	 */
	public static int calculateEncodedNLRILength(NetworkLayerReachabilityInformation nlri) {
		return NetworkLayerReachabilityInformation.calculateOctetsForPrefixLength(nlri.getPrefixLength()) + 1;
	}

	/**
	 * Encode a network-layer reachability information to its binary representation.
	 * 
	 * @param allocator the buffer allocator 
	 * @param nlri the information to encode
	 * @return the binary representation of the passed information
	 */
	public static void encodeNLRI(ByteBuf buffer, NetworkLayerReachabilityInformation nlri) {
		buffer.writeByte(nlri.getPrefixLength());

		if(nlri.getPrefixLength() > 0) {
			buffer.writeBytes(nlri.getPrefix());
		}
	}

}
