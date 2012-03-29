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
package org.bgp4j.net.attributes;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.bgp4j.net.AddressFamily;
import org.bgp4j.net.AddressFamilyKey;
import org.bgp4j.net.BinaryNextHop;
import org.bgp4j.net.NetworkLayerReachabilityInformation;
import org.bgp4j.net.SubsequentAddressFamily;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class MultiProtocolReachableNLRI extends PathAttribute {

	private AddressFamily addressFamily;
	private SubsequentAddressFamily subsequentAddressFamily;
	private BinaryNextHop nextHop;
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
		
		setNextHopAddress(nextHopAddress);
	}
	
	/**
	 * @param category
	 */
	public MultiProtocolReachableNLRI(AddressFamily addressFamily, SubsequentAddressFamily subsequentAddressFamily, BinaryNextHop nextHop) {
		this(addressFamily, subsequentAddressFamily);
		
		this.nextHop = nextHop;
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
	
	/**
	 * @param category
	 */
	public MultiProtocolReachableNLRI(AddressFamily addressFamily, SubsequentAddressFamily subsequentAddressFamily, BinaryNextHop nextHop, 
			NetworkLayerReachabilityInformation[] nlris) {
		this(addressFamily, subsequentAddressFamily, nextHop);
		
		for(NetworkLayerReachabilityInformation nlri : nlris)
			this.nlris.add(nlri);
	}
	
	/**
	 * @param category
	 */
	public MultiProtocolReachableNLRI(AddressFamily addressFamily, SubsequentAddressFamily subsequentAddressFamily, BinaryNextHop nextHop, 
			Collection<NetworkLayerReachabilityInformation> nlris) {
		this(addressFamily, subsequentAddressFamily, nextHop);
		
		if(nlris != null)
			this.nlris.addAll(nlris);
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
	public BinaryNextHop getNextHop() {
		return nextHop;
	}

	/**
	 * @param nextHopAddress the nextHopAddress to set
	 */
	public void setNextHopAddress(byte[] nextHopAddress) {
		if(nextHopAddress != null)
			this.nextHop = new BinaryNextHop(nextHopAddress);
		else
			this.nextHop = null;
	}

	/**
	 * @param nextHopAddress the nextHopAddress to set
	 */
	public void setNextHop(BinaryNextHop nextHop) {
		this.nextHop = nextHop;
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

	public AddressFamilyKey addressFamilyKey() {
		return new AddressFamilyKey(getAddressFamily(), getSubsequentAddressFamily());
	}

	@Override
	protected PathAttributeType internalType() {
		return PathAttributeType.MULTI_PROTOCOL_REACHABLE;
	}

	@Override
	protected boolean subclassEquals(PathAttribute obj) {
		MultiProtocolReachableNLRI o = (MultiProtocolReachableNLRI)obj;
		
		EqualsBuilder builer = (new EqualsBuilder())
				.append(getAddressFamily(), o.getAddressFamily())
				.append(getSubsequentAddressFamily(), o.getSubsequentAddressFamily())
				.append(getNextHop(), o.getNextHop())
				.append(getNlris().size(), o.getNlris().size());
		
		if(builer.isEquals()) {
			Iterator<NetworkLayerReachabilityInformation> lit = getNlris().iterator();
			Iterator<NetworkLayerReachabilityInformation> rit = o.getNlris().iterator();
			
			while(lit.hasNext())
				builer.append(lit.next(), rit.next());
		}
		
		return builer.isEquals();
	}

	@Override
	protected int sublcassHashCode() {
		HashCodeBuilder builder = (new HashCodeBuilder())
				.append(getAddressFamily())
				.append(getSubsequentAddressFamily())
				.append(getNextHop());
		Iterator<NetworkLayerReachabilityInformation> it = getNlris().iterator();
		
		while(it.hasNext())
			builder.append(it.next());
		
		return builder.toHashCode();
	}

	@Override
	protected int subclassCompareTo(PathAttribute obj) {
		MultiProtocolReachableNLRI o = (MultiProtocolReachableNLRI)obj;
		
		CompareToBuilder builer = (new CompareToBuilder())
				.append(getAddressFamily(), o.getAddressFamily())
				.append(getSubsequentAddressFamily(), o.getSubsequentAddressFamily())
				.append(getNextHop(), o.getNextHop())
				.append(getNlris().size(), o.getNlris().size());
		
		if(builer.toComparison() == 0) {
			Iterator<NetworkLayerReachabilityInformation> lit = getNlris().iterator();
			Iterator<NetworkLayerReachabilityInformation> rit = o.getNlris().iterator();
			
			while(lit.hasNext())
				builer.append(lit.next(), rit.next());
		}
		
		return builer.toComparison();
	}
	
}
