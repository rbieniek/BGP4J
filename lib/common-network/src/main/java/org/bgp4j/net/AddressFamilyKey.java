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
 * File: org.bgp4j.rib.RoutingInformationBaseKey.java 
 */
package org.bgp4j.net;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;


/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class AddressFamilyKey implements Comparable<AddressFamilyKey> {

	private AddressFamily addressFamily;
	private SubsequentAddressFamily subsequentAddressFamily;
	
	public AddressFamilyKey(AddressFamily addressFamily, SubsequentAddressFamily subsequentAddressFamily) {
		this.addressFamily = addressFamily;
		this.subsequentAddressFamily = subsequentAddressFamily;
	}

	/**
	 * @return the addressFamily
	 */
	public AddressFamily getAddressFamily() {
		return addressFamily;
	}

	/**
	 * @return the subsequentAddressFamily
	 */
	public SubsequentAddressFamily getSubsequentAddressFamily() {
		return subsequentAddressFamily;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return (new HashCodeBuilder()).append(this.addressFamily).append(this.subsequentAddressFamily).toHashCode();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof AddressFamilyKey))
			return false;
		
		AddressFamilyKey o = (AddressFamilyKey)obj;
		
		return (new EqualsBuilder())
				.append(addressFamily, o.addressFamily)
				.append(subsequentAddressFamily, o.subsequentAddressFamily)
				.isEquals();
	}

	@Override
	public int compareTo(AddressFamilyKey o) {
		return (new CompareToBuilder())
				.append(addressFamily, o.addressFamily)
				.append(subsequentAddressFamily, o.subsequentAddressFamily)
				.toComparison();
	}
}
