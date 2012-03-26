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
 * File: org.bgp4j.rib.TopologicalTreeSortingKey.java 
 */
package org.bgp4j.rib;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.bgp4j.net.AddressFamily;
import org.bgp4j.net.AddressFamilyKey;
import org.bgp4j.net.SubsequentAddressFamily;
import org.bgp4j.net.attributes.PathAttribute;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class TopologicalTreeSortingKey implements Comparable<TopologicalTreeSortingKey> {
	private AddressFamilyKey addressFamilyKey;
	private Set<PathAttribute> pathAttributes = new TreeSet<PathAttribute>();
	
	public TopologicalTreeSortingKey(AddressFamily afi, SubsequentAddressFamily safi, Collection<PathAttribute> attributes) {
		this.addressFamilyKey = new AddressFamilyKey(afi, safi);
		if(attributes != null)
			pathAttributes.addAll(attributes);
		
		pathAttributes = Collections.unmodifiableSet(pathAttributes);
	}

	public TopologicalTreeSortingKey(AddressFamilyKey addressFamilyKey, Collection<PathAttribute> attributes) {
		this.addressFamilyKey = addressFamilyKey;
		
		if(attributes != null)
			pathAttributes.addAll(attributes);
		
		pathAttributes = Collections.unmodifiableSet(pathAttributes);
	}

	/**
	 * @return the addressFamilyKey
	 */
	public AddressFamilyKey getAddressFamilyKey() {
		return addressFamilyKey;
	}

	/**
	 * @return the pathAttributes
	 */
	public Set<PathAttribute> getPathAttributes() {
		return pathAttributes;
	}

	@Override
	public int compareTo(TopologicalTreeSortingKey o) {
		CompareToBuilder builder = (new CompareToBuilder())
				.append(getAddressFamilyKey(), o.getAddressFamilyKey())
				.append(getPathAttributes().size(), o.getPathAttributes().size());
		
		if(builder.toComparison() == 0) {
			Iterator<PathAttribute> lit = getPathAttributes().iterator();
			Iterator<PathAttribute> rit = o.getPathAttributes().iterator();
			
			while(lit.hasNext())
				builder.append(lit.next(), rit.next());		
		}
		
		return builder.toComparison();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		HashCodeBuilder builder = (new HashCodeBuilder())
				.append(getAddressFamilyKey());
				
		for(PathAttribute attr : getPathAttributes())
			builder.append(attr);
		
		return builder.toHashCode();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof TopologicalTreeSortingKey))
			return false;
		
		TopologicalTreeSortingKey o = (TopologicalTreeSortingKey)obj;
		
		EqualsBuilder builder = (new EqualsBuilder())
				.append(getAddressFamilyKey(), o.getAddressFamilyKey())
				.append(getPathAttributes().size(), o.getPathAttributes().size());
		
		if(builder.isEquals()) {
			Iterator<PathAttribute> lit = getPathAttributes().iterator();
			Iterator<PathAttribute> rit = o.getPathAttributes().iterator();
			
			while(lit.hasNext())
				builder.append(lit.next(), rit.next());		
		}
		
		return builder.isEquals();
	}
}
