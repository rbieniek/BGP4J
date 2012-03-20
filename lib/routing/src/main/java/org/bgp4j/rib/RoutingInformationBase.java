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
 * File: org.bgp4j.rib.RoutingInformationBase.java 
 */
package org.bgp4j.rib;

import java.util.Collection;

import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.bgp4j.net.AddressFamilyKey;
import org.bgp4j.net.NetworkLayerReachabilityInformation;
import org.bgp4j.net.PathAttribute;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class RoutingInformationBase {

	private String peerName;
	private RIBSide side;
	private AddressFamilyKey addressFamilyKey;
	private RoutingTree routingTree = new RoutingTree();
	private @Inject Event<RouteAdded> routeAddedEvent;
	private @Inject Event<RouteWithdrawn> routeWithdrawnEvent;
	
	RoutingInformationBase() {
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

	/**
	 * @return the addressFamilyKey
	 */
	public AddressFamilyKey getAddressFamilyKey() {
		return addressFamilyKey;
	}

	/**
	 * @param peerName the peerName to set
	 */
	void setPeerName(String peerName) {
		this.peerName = peerName;
	}

	/**
	 * @param side the side to set
	 */
	void setSide(RIBSide side) {
		this.side = side;
	}

	/**
	 * @param addressFamilyKey the addressFamilyKey to set
	 */
	void setAddressFamilyKey(AddressFamilyKey addressFamilyKey) {
		this.addressFamilyKey = addressFamilyKey;
	}

	void destroyRIB() {
		routingTree.destroy();
	}
	
	public void addRoutes(Collection<NetworkLayerReachabilityInformation> nlris, Collection<PathAttribute> pathAttributes) {
		for(NetworkLayerReachabilityInformation nlri : nlris)
			if(routingTree.addRoute(nlri, pathAttributes))
				routeAddedEvent.fire(new RouteAdded(getPeerName(), 
						getSide(), 
						getAddressFamilyKey(), 
						nlri, 
						pathAttributes));
	}

	public void removeRoutes(Collection<NetworkLayerReachabilityInformation> nlris) {
		for(NetworkLayerReachabilityInformation nlri : nlris)
			if(routingTree.withdrawRoute(nlri))
				routeWithdrawnEvent.fire(new RouteWithdrawn(getPeerName(), getSide(), getAddressFamilyKey(), nlri));
		
	}
}
