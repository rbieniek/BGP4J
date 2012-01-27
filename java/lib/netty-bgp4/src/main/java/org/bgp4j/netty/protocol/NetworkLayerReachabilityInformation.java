/**
 * 
 */
package org.bgp4j.netty.protocol;

import java.net.Inet4Address;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

/**
 * @author rainer
 *
 */
public class NetworkLayerReachabilityInformation {

	private int prefixLength;
	private Inet4Address prefix;
	
	ChannelBuffer encodeWithdrawnRoute() {
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

	int calculatePacketSize() {
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
