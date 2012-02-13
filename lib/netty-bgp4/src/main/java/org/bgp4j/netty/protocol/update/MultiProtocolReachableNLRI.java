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
 * File: org.bgp4j.netty.protocol.update.MultiProtocolReachableNLRI.java 
 */
package org.bgp4j.netty.protocol.update;

import java.util.LinkedList;
import java.util.List;

import org.bgp4j.net.AddressFamily;
import org.bgp4j.net.NetworkLayerReachabilityInformation;
import org.bgp4j.net.SubsequentAddressFamily;
import org.bgp4j.netty.BGPv4Constants;
import org.bgp4j.netty.NLRICodec;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class MultiProtocolReachableNLRI extends Attribute {

	private AddressFamily addressFamily;
	private SubsequentAddressFamily subsequentAddressFamily;
	private byte[] nextHopAddress;
	private List<NetworkLayerReachabilityInformation> nlris = new LinkedList<NetworkLayerReachabilityInformation>();
	
	/**
	 * @param category
	 */
	public MultiProtocolReachableNLRI() {
		super(Category.OPTIONAL_NON_TRANSITIVE);
	}

	/**
	 * @param category
	 */
	public MultiProtocolReachableNLRI(AddressFamily addressFamily, SubsequentAddressFamily subsequentAddressFamily) {
		this();
		
		this.addressFamily = addressFamily;
		this.subsequentAddressFamily = subsequentAddressFamily;
	}
	
	/**
	 * @param category
	 */
	public MultiProtocolReachableNLRI(AddressFamily addressFamily, SubsequentAddressFamily subsequentAddressFamily, byte[] nextHopAddress) {
		this(addressFamily, subsequentAddressFamily);
		
		this.nextHopAddress = nextHopAddress;
	}
	
	/**
	 * @param category
	 */
	public MultiProtocolReachableNLRI(AddressFamily addressFamily, SubsequentAddressFamily subsequentAddressFamily, byte[] nextHopAddress, 
			NetworkLayerReachabilityInformation[] nlris) {
		this(addressFamily, subsequentAddressFamily, nextHopAddress);
		
		for(NetworkLayerReachabilityInformation nlri : nlris)
			this.nlris.add(nlri);
	}
	
	/* (non-Javadoc)
	 * @see org.bgp4j.netty.protocol.update.Attribute#getTypeCode()
	 */
	@Override
	protected int getTypeCode() {
		return BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_MP_REACH_NLRI;
	}

	/* (non-Javadoc)
	 * @see org.bgp4j.netty.protocol.update.Attribute#getValueLength()
	 */
	@Override
	protected int getValueLength() {
		int size = 5; // 2 octets AFI +  1 octet SAFI + 1 octet NextHop address length + 1 octet reserved
		
		if(this.nextHopAddress != null)
			size += this.nextHopAddress.length;
		
		if(this.nlris != null) {
			for(NetworkLayerReachabilityInformation nlri : this.nlris)
				size += NLRICodec.calculateEncodedNLRILength(nlri);
		}
		
		return size;
	}

	/* (non-Javadoc)
	 * @see org.bgp4j.netty.protocol.update.Attribute#encodeValue()
	 */
	@Override
	protected ChannelBuffer encodeValue() {
		ChannelBuffer buffer = ChannelBuffers.buffer(getValueLength());
		
		buffer.writeShort(this.addressFamily.toCode());
		buffer.writeByte(this.subsequentAddressFamily.toCode());
		
		if(this.nextHopAddress != null) {
			buffer.writeByte(this.nextHopAddress.length);
			buffer.writeBytes(this.nextHopAddress);
		} else {
			buffer.writeByte(0);
		}

		buffer.writeByte(0); // write reserved field

		if(this.nlris != null) {
			for(NetworkLayerReachabilityInformation nlri : this.nlris)
				buffer.writeBytes(NLRICodec.encodeNLRI(nlri));
		}

		return buffer;
	}

	/**
	 * @return the addressFamily
	 */
	public AddressFamily getAddressFamily() {
		return addressFamily;
	}

	/**
	 * @param addressFamily the addressFamily to set
	 */
	public void setAddressFamily(AddressFamily addressFamily) {
		this.addressFamily = addressFamily;
	}

	/**
	 * @return the subsequentAddressFamily
	 */
	public SubsequentAddressFamily getSubsequentAddressFamily() {
		return subsequentAddressFamily;
	}

	/**
	 * @param subsequentAddressFamily the subsequentAddressFamily to set
	 */
	public void setSubsequentAddressFamily(
			SubsequentAddressFamily subsequentAddressFamily) {
		this.subsequentAddressFamily = subsequentAddressFamily;
	}

	/**
	 * @return the nextHopAddress
	 */
	public byte[] getNextHopAddress() {
		return nextHopAddress;
	}

	/**
	 * @param nextHopAddress the nextHopAddress to set
	 */
	public void setNextHopAddress(byte[] nextHopAddress) {
		this.nextHopAddress = nextHopAddress;
	}

	/**
	 * @return the nlris
	 */
	public List<NetworkLayerReachabilityInformation> getNlris() {
		return nlris;
	}

	/**
	 * @param nlris the nlris to set
	 */
	public void setNlris(List<NetworkLayerReachabilityInformation> nlris) {
		this.nlris = nlris;
	}

}
