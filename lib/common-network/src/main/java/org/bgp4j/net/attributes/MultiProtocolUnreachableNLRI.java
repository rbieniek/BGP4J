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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.bgp4j.net.AddressFamily;
import org.bgp4j.net.AddressFamilyKey;
import org.bgp4j.net.NetworkLayerReachabilityInformation;
import org.bgp4j.net.SubsequentAddressFamily;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class MultiProtocolUnreachableNLRI extends PathAttribute {

	private AddressFamily addressFamily;
	private SubsequentAddressFamily subsequentAddressFamily;
	private List<NetworkLayerReachabilityInformation> nlris = new LinkedList<NetworkLayerReachabilityInformation>();
	
	/**
	 * @param category
	 */
	public MultiProtocolUnreachableNLRI() {
		super(Category.OPTIONAL_NON_TRANSITIVE);
	}

	/**
	 * @param category
	 */
	public MultiProtocolUnreachableNLRI(AddressFamily addressFamily, SubsequentAddressFamily subsequentAddressFamily) {
		this();
		
		this.addressFamily = addressFamily;
		this.subsequentAddressFamily = subsequentAddressFamily;
	}
	
	/**
	 * @param category
	 */
	public MultiProtocolUnreachableNLRI(AddressFamily addressFamily, SubsequentAddressFamily subsequentAddressFamily, NetworkLayerReachabilityInformation[] nlris) {
		this(addressFamily, subsequentAddressFamily);
		
		for(NetworkLayerReachabilityInformation nlri : nlris)
			this.nlris.add(nlri);
	}
	
	/**
	 * @param category
	 */
	public MultiProtocolUnreachableNLRI(AddressFamily addressFamily, SubsequentAddressFamily subsequentAddressFamily, List<NetworkLayerReachabilityInformation> nlris) {
		this(addressFamily, subsequentAddressFamily);

		if(nlris != null)
			this.nlris = new LinkedList<NetworkLayerReachabilityInformation>(nlris);
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
		return PathAttributeType.MULTI_PROTOCOL_UNREACHABLE;
	}

	@Override
	protected boolean subclassEquals(PathAttribute obj) {
		MultiProtocolUnreachableNLRI o = (MultiProtocolUnreachableNLRI)obj;
		
		EqualsBuilder builer = (new EqualsBuilder())
				.append(getAddressFamily(), o.getAddressFamily())
				.append(getSubsequentAddressFamily(), o.getSubsequentAddressFamily())
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
	protected int subclassHashCode() {
		HashCodeBuilder builder = (new HashCodeBuilder())
				.append(getAddressFamily())
				.append(getSubsequentAddressFamily());
		Iterator<NetworkLayerReachabilityInformation> it = getNlris().iterator();
		
		while(it.hasNext())
			builder.append(it.next());
		
		return builder.toHashCode();
	}

	@Override
	protected int subclassCompareTo(PathAttribute obj) {
		MultiProtocolUnreachableNLRI o = (MultiProtocolUnreachableNLRI)obj;
		
		CompareToBuilder builer = (new CompareToBuilder())
				.append(getAddressFamily(), o.getAddressFamily())
				.append(getSubsequentAddressFamily(), o.getSubsequentAddressFamily())
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
