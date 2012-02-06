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
 * File: org.bgp4j.netty.protocol.update.MultiExitDiscPathAttribute.java 
 */
package org.bgp4j.netty.protocol.update;

import org.bgp4j.netty.BGPv4Constants;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class MultiExitDiscPathAttribute extends Attribute {

	public MultiExitDiscPathAttribute() {
		super(Category.OPTIONAL_NON_TRANSITIVE);
	}

	public MultiExitDiscPathAttribute(int discriminator) {
		super(Category.OPTIONAL_NON_TRANSITIVE);
		
		this.discriminator = discriminator;
	}

	private int discriminator;
	
	/* (non-Javadoc)
	 * @see org.bgp4j.netty.protocol.update.Attribute#getTypeCode()
	 */
	@Override
	protected int getTypeCode() {
		return BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_MULTI_EXIT_DISC;
	}

	/* (non-Javadoc)
	 * @see org.bgp4j.netty.protocol.update.Attribute#getValueLength()
	 */
	@Override
	protected int getValueLength() {
		return 4;
	}

	/* (non-Javadoc)
	 * @see org.bgp4j.netty.protocol.update.Attribute#encodeValue()
	 */
	@Override
	protected ChannelBuffer encodeValue() {
		ChannelBuffer buffer = ChannelBuffers.buffer(4);
		
		buffer.writeInt(discriminator);
		
		return buffer;
	}

	/**
	 * @return the discriminator
	 */
	public int getDiscriminator() {
		return discriminator;
	}

	/**
	 * @param discriminator the discriminator to set
	 */
	public void setDiscriminator(int discriminator) {
		this.discriminator = discriminator;
	}

}
