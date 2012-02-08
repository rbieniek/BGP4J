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
package org.bgp4j.netty.protocol.update;

import org.bgp4j.netty.BGPv4Constants;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class OriginatorIDPathAttribute extends Attribute {

	public OriginatorIDPathAttribute() {
		super(Category.OPTIONAL_NON_TRANSITIVE);
	}

	public OriginatorIDPathAttribute(int originatorID) {
		super(Category.OPTIONAL_NON_TRANSITIVE);
		
		setOriginatorID(originatorID);
	}

	private int originatorID;
	
	/* (non-Javadoc)
	 * @see org.bgp4j.netty.protocol.PathAttribute#getTypeCode()
	 */
	@Override
	protected int getTypeCode() {
		return BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_ORIGINATOR_ID;
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
		
		buffer.writeInt(this.originatorID);
		
		return buffer;
	}

	/**
	 * @return the nextHop
	 */
	public int getOriginatorID() {
		return originatorID;
	}

	/**
	 * 
	 * @param nextHop
	 */
	public void setOriginatorID(int nextHop) {
		this.originatorID = nextHop;
	}

}
