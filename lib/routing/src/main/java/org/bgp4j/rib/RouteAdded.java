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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.bgp4j.net.RIBSide;

/**
 * Event fired by a RoutingInformationBase instance when a route has been added to the RIB.
 * 
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class RouteAdded {

	private String peerName;
	private RIBSide side;
	private Route route;
	
	RouteAdded(String peerName, RIBSide side, Route route) {
		this.peerName = peerName;
		this.side = side;
		this.route = route;
	}

	/**
	 * @return the peerName
	 */
	public String getPeerName() {
		return peerName;
	}

	/**
	 * @return the side
	 */
	public RIBSide getSide() {
		return side;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return (new HashCodeBuilder())
				.append(getPeerName())
				.append(getSide())
				.append(getRoute())
				.toHashCode();
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
		
		RouteAdded other = (RouteAdded) obj;
		
		return (new EqualsBuilder())
				.append(getPeerName(), other.getPeerName())
				.append(getSide(), other.getSide())
				.append(getRoute(), other.getRoute())
				.isEquals();
	}

	/**
	 * @return the route
	 */
	public Route getRoute() {
		return route;
	}

}
