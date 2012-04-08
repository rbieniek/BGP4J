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
 * File: org.bgp4j.rib.RouteAdded.java 
 */
package org.bgp4j.rib;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.bgp4j.net.AddressFamilyKey;
import org.bgp4j.net.NetworkLayerReachabilityInformation;
import org.bgp4j.net.NextHop;
import org.bgp4j.net.attributes.PathAttribute;

/**
 * Event fired by a RoutingInformationBase instance when a route has been added to the RIB.
 * 
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class Route implements Comparable<Route> {

	private AddressFamilyKey addressFamilyKey;
	private NetworkLayerReachabilityInformation nlri;
	private Set<PathAttribute> pathAttributes = new TreeSet<PathAttribute>();
	private NextHop nextHop;
	private UUID ribID;
	
	public Route(AddressFamilyKey addressFamilyKey, NetworkLayerReachabilityInformation nlri, 
			Collection<PathAttribute> pathAttributes, NextHop nextHop) {
		this.addressFamilyKey = addressFamilyKey;
		this.nlri = nlri;
		if(pathAttributes != null)
			this.pathAttributes = new TreeSet<PathAttribute>(pathAttributes);
		this.nextHop = nextHop;
	}

	Route(UUID ribID, AddressFamilyKey addressFamilyKey, NetworkLayerReachabilityInformation nlri, 
			Collection<PathAttribute> pathAttributes, NextHop nextHop) {
		this(addressFamilyKey, nlri, pathAttributes, nextHop);
		
		setRibID(ribID);
	}

	/**
	 * buidl a new route object from a source route but with changed additional fields
	 * @param route
	 * @param pathAttributes
	 */
	public Route(Route route, NetworkLayerReachabilityInformation nlri,  Set<PathAttribute> pathAttributes, NextHop nextHop) {
		this(route.getRibID(), route.getAddressFamilyKey(), 
				nlri != null ? nlri : route.getNlri(), 
						pathAttributes != null ? pathAttributes : route.getPathAttributes(), 
								nextHop != null ? nextHop : route.getNextHop());
	}

	/**
	 * @return the addressFamilyKey
	 */
	public AddressFamilyKey getAddressFamilyKey() {
		return addressFamilyKey;
	}

	/**
	 * @return the nlri
	 */
	public NetworkLayerReachabilityInformation getNlri() {
		return nlri;
	}

	/**
	 * @return the pathAttributes
	 */
	public Set<PathAttribute> getPathAttributes() {
		return pathAttributes;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		HashCodeBuilder builder = (new HashCodeBuilder())
				.append(getAddressFamilyKey())
				.append(getNlri())
				.append(getNextHop())
				.append(getRibID());
		
		for(PathAttribute pa : getPathAttributes())
			builder.append(pa);
		
		return builder.toHashCode();
	}

	/**
	 * @return the nextHop
	 */
	public NextHop getNextHop() {
		return nextHop;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		Route o = (Route) obj;
		
		EqualsBuilder builder = (new EqualsBuilder())
				.append(getAddressFamilyKey(), o.getAddressFamilyKey())
				.append(getNlri(), o.getNlri())
				.append(getPathAttributes().size(), o.getPathAttributes().size())
				.append(getNextHop(), o.getNextHop())
				.append(getRibID(), o.getRibID());
		
		if(builder.isEquals()) {
			Iterator<PathAttribute> lit = getPathAttributes().iterator();
			Iterator<PathAttribute> rit = o.getPathAttributes().iterator();
			
			while(lit.hasNext())
				builder.append(lit.next(), rit.next());
		}
		
		return builder.isEquals();
	}

	@Override
	public int compareTo(Route o) {
		CompareToBuilder builder = (new CompareToBuilder())
				.append(getAddressFamilyKey(), o.getAddressFamilyKey())
				.append(getNlri(), o.getNlri())
				.append(getPathAttributes().size(), o.getPathAttributes().size())
				.append(getNextHop(), o.getNextHop())
				.append(getRibID(), o.getRibID());
		
		if(builder.toComparison() == 0) {
			Iterator<PathAttribute> lit = getPathAttributes().iterator();
			Iterator<PathAttribute> rit = o.getPathAttributes().iterator();
			
			while(lit.hasNext())
				builder.append(lit.next(), rit.next());
		}
		
		return builder.toComparison();
	}

	/**
	 * compare only the network / routeing relevant 
	 * @param o
	 * @return
	 */
	public int networkCompareTo(Route o) {
		CompareToBuilder builder = (new CompareToBuilder())
				.append(getAddressFamilyKey(), o.getAddressFamilyKey())
				.append(getNlri(), o.getNlri())
				.append(getPathAttributes().size(), o.getPathAttributes().size())
				.append(getNextHop(), o.getNextHop());
		
		if(builder.toComparison() == 0) {
			Iterator<PathAttribute> lit = getPathAttributes().iterator();
			Iterator<PathAttribute> rit = o.getPathAttributes().iterator();
			
			while(lit.hasNext())
				builder.append(lit.next(), rit.next());
		}
		
		return builder.toComparison();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean networkEquals(Route o) {
		EqualsBuilder builder = (new EqualsBuilder())
				.append(getAddressFamilyKey(), o.getAddressFamilyKey())
				.append(getNlri(), o.getNlri())
				.append(getPathAttributes().size(), o.getPathAttributes().size())
				.append(getNextHop(), o.getNextHop())
				.append(getRibID(), o.getRibID());
		
		if(builder.isEquals()) {
			Iterator<PathAttribute> lit = getPathAttributes().iterator();
			Iterator<PathAttribute> rit = o.getPathAttributes().iterator();
			
			while(lit.hasNext())
				builder.append(lit.next(), rit.next());
		}
		
		return builder.isEquals();
	}
	/**
	 * @return the ribID
	 */
	public UUID getRibID() {
		return ribID;
	}

	/**
	 * @param ribID the ribID to set
	 */
	void setRibID(UUID ribID) {
		this.ribID = ribID;
	}

}
