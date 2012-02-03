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

import org.bgp4j.netty.BGPv4Constants;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class NextHopPathAttribute extends Attribute {

	private Inet4Address nextHop;
	
	/* (non-Javadoc)
	 * @see org.bgp4j.netty.protocol.PathAttribute#getTypeCode()
	 */
	@Override
	protected int getTypeCode() {
		return BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_NEXT_HOP;
	}

	/* (non-Javadoc)
	 * @see org.bgp4j.netty.protocol.PathAttribute#getValueLength()
	 */
	@Override
	protected int getValueLength() {
		return 4;
	}

	/* (non-Javadoc)
	 * @see org.bgp4j.netty.protocol.PathAttribute#encodeValue()
	 */
	@Override
	protected ChannelBuffer encodeValue() {
		ChannelBuffer buffer = ChannelBuffers.buffer(4);
		
		buffer.writeBytes(this.nextHop.getAddress());
		
		return buffer;
	}

	/**
	 * @return the nextHop
	 */
	public Inet4Address getNextHop() {
		return nextHop;
	}

	/**
	 * @param nextHop the nextHop to set
	 */
	public void setNextHop(Inet4Address nextHop) {
		this.nextHop = nextHop;
	}

}
