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

import java.net.Inet4Address;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class NetworkLayerReachabilityInformation {

	private int prefixLength;
	private Inet4Address prefix;
	
	public ChannelBuffer encodeRoute() {
		ChannelBuffer buffer = ChannelBuffers.buffer(5);
		
		buffer.writeByte(prefixLength);
		if(prefixLength > 0) {
			byte[] addr = prefix.getAddress();
			int prefixBytes = ((prefixLength-1) / 8) + 1;
			int maskBits = (32 - prefixLength) % 8;
			int mask=0;
			
			for(int i=0; i<maskBits; i++) {
				mask |= (1<<i);
			}
			addr[prefixBytes -1] &= ~mask;

			buffer.writeBytes(addr, 0, prefixBytes);			
		}
		
		return buffer;
	}

	public int calculatePacketSize() {
		int size = 1;
		
		if(prefixLength > 0)
			size += ((prefixLength-1) / 8) + 1;
		
		return size;
	}
	
	/**
	 * @return the prefixLength
	 */
	public int getPrefixLength() {
		return prefixLength;
	}

	/**
	 * @param prefixLength the prefixLength to set
	 */
	public void setPrefixLength(int prefixLength) {
		this.prefixLength = prefixLength;
	}

	/**
	 * @return the prefix
	 */
	public Inet4Address getPrefix() {
		return prefix;
	}

	/**
	 * @param prefix the prefix to set
	 */
	public void setPrefix(Inet4Address prefix) {
		this.prefix = prefix;
	}
}
