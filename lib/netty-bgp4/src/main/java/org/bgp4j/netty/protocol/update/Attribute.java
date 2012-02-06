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
 * Superclass for all BGPv4 path attributes
 * 
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public abstract class Attribute {

	/**
	 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
	 *
	 */
	public enum Category {
		WELL_KNOWN_MANDATORY,
		WELL_KNOWN_DISCRETIONARY,
		OPTIONAL_TRANSITIVE,
		OPTIONAL_NON_TRANSITIVE,
	}

	private boolean optional;
	private boolean transitive;
	private boolean partial;
	private Category category;
	
	protected Attribute(Category category) {
		this.category = category;
		
		switch(category) {
		case OPTIONAL_NON_TRANSITIVE:
			setTransitive(false);
			setOptional(true);
			break;
		case OPTIONAL_TRANSITIVE:
			setTransitive(true);
			setOptional(true);
			break;
		case WELL_KNOWN_DISCRETIONARY:
			setTransitive(true);
			setOptional(false);
			break;
		case WELL_KNOWN_MANDATORY:
			setTransitive(true);
			setOptional(false);
			break;
		}
	}
	
	/**
	 * encode the path attribute for network transmission
	 * 
	 * @return an encoded formatted path attribute
	 */
	ChannelBuffer encodePathAttribute()  {
		ChannelBuffer buffer = ChannelBuffers.buffer(BGPv4Constants.BGP_PACKET_MAX_LENGTH);
		int valueLength = getValueLength();
		int attrFlagsCode = 0;
				
		if(isOptional())
			attrFlagsCode |= BGPv4Constants.BGP_PATH_ATTRIBUTE_OPTIONAL_BIT;
		
		if(isTransitive())
			attrFlagsCode |= BGPv4Constants.BGP_PATH_ATTRIBUTE_TRANSITIVE_BIT;

		if(isPartial())
			attrFlagsCode |= BGPv4Constants.BGP_PATH_ATTRIBUTE_PARTIAL_BIT;
		
		if(valueLength > 255)
			attrFlagsCode |= BGPv4Constants.BGP_PATH_ATTRIBUTE_EXTENDED_LENGTH_BIT;
		
		attrFlagsCode |= (getTypeCode() & BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_MASK);

		buffer.writeShort(attrFlagsCode);
		
		if(valueLength > 255)
			buffer.writeShort(valueLength);
		else
			buffer.writeByte(valueLength);
		
		buffer.writeBytes(encodeValue());
		
		return buffer;
	}
	
	int calculatePacketSize() {
		int size = 2; // attribute flags + type field;
		int valueLength = getValueLength();
		
		size += (valueLength > 255) ? 2 : 1; // length field;
		size += valueLength;
		
		return size;
	}
	
	/**
	 * get the specific type code (see RFC 4271)
	 * @return
	 */
	protected abstract int getTypeCode();

	/**
	 * get the attribute value length
	 * @return
	 */
	protected abstract int getValueLength();

	/**
	 * get the encoded attribute value
	 */
	protected abstract ChannelBuffer encodeValue();
	
	/**
	 * @return the partial
	 */
	public boolean isPartial() {
		return partial;
	}

	/**
	 * @param partial the partial to set
	 */
	public void setPartial(boolean partial) {
		this.partial = partial;
	}

	/**
	 * @return the optional
	 */
	public boolean isOptional() {
		return optional;
	}

	/**
	 * @return the optional
	 */
	public boolean isWellKnown() {
		return !isOptional();
	}

	/**
	 * @param optional the optional to set
	 */
	protected void setOptional(boolean optional) {
		this.optional = optional;
	}

	/**
	 * @param wellKnown the well known to set
	 */
	protected void setWellKnown(boolean wellKnown) {
		setOptional(!wellKnown);
	}
	
	/**
	 * @return the transitive
	 */
	public boolean isTransitive() {
		return transitive;
	}

	/**
	 * @param transitive the transitive to set
	 */
	public void setTransitive(boolean transitive) {
		this.transitive = transitive;
	}

	/**
	 * @return the category
	 */
	public Category getCategory() {
		return category;
	}
	
}
