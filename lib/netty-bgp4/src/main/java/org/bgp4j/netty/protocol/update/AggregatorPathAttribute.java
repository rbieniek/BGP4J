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
 * File: org.bgp4j.netty.protocol.update.AggregatorPathAttribute.java 
 */
package org.bgp4j.netty.protocol.update;

import java.net.Inet4Address;

import org.bgp4j.netty.ASType;
import org.bgp4j.netty.BGPv4Constants;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class AggregatorPathAttribute extends Attribute {

	private ASType asType;
	private int asNumber;
	private Inet4Address aggregator;

	public AggregatorPathAttribute(ASType asType) {
		super(Category.OPTIONAL_TRANSITIVE);

		this.asType = asType;
	}

	public AggregatorPathAttribute(ASType asType, int asNumber, Inet4Address aggregator) {
		this(asType);
		
		this.asNumber = asNumber;
		this.aggregator = aggregator;
	}

	/**
	 * @return the fourByteASNumber
	 */
	public boolean isFourByteASNumber() {
		return (this.asType == ASType.AS_NUMBER_4OCTETS);
	}

	/**
	 * @return the asType
	 */
	public ASType getAsType() {
		return asType;
	}

	/* (non-Javadoc)
	 * @see org.bgp4j.netty.protocol.update.Attribute#getTypeCode()
	 */
	@Override
	protected int getTypeCode() {
		return (isFourByteASNumber() 
				? BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS4_AGGREGATOR 
						: BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AGGREGATOR);
	}

	/* (non-Javadoc)
	 * @see org.bgp4j.netty.protocol.update.Attribute#getValueLength()
	 */
	@Override
	protected int getValueLength() {
		return (isFourByteASNumber() ? 8 : 6);
	}

	/* (non-Javadoc)
	 * @see org.bgp4j.netty.protocol.update.Attribute#encodeValue()
	 */
	@Override
	protected ChannelBuffer encodeValue() {
		ChannelBuffer buffer = ChannelBuffers.buffer(getValueLength());
		
		if(isFourByteASNumber())
			buffer.writeInt(this.asNumber);
		else
			buffer.writeShort(this.asNumber);
		
		buffer.writeBytes(aggregator.getAddress());
		
		return buffer;
	}

	/**
	 * @return the asNumber
	 */
	public int getAsNumber() {
		return asNumber;
	}

	/**
	 * @param asNumber the asNumber to set
	 */
	public void setAsNumber(int asNumber) {
		this.asNumber = asNumber;
	}

	/**
	 * @return the aggregator
	 */
	public Inet4Address getAggregator() {
		return aggregator;
	}

	/**
	 * @param aggregator the aggregator to set
	 */
	public void setAggregator(Inet4Address aggregator) {
		this.aggregator = aggregator;
	}

}
