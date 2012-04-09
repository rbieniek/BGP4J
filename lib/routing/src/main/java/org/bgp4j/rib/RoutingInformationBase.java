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
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.bgp4j.net.AddressFamilyKey;
import org.bgp4j.net.NetworkLayerReachabilityInformation;
import org.bgp4j.net.NextHop;
import org.bgp4j.net.RIBSide;
import org.bgp4j.net.attributes.PathAttribute;

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
	private Collection<RoutingEventListener> listeners;
	private List<RoutingEventListener> perRibListeners = Collections.synchronizedList(new LinkedList<RoutingEventListener>());
	private UUID ribID = UUID.randomUUID();
	
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
	
	/**
	 * Add a NLRI collection sharing a common collection of path attributes to the routing tree 
	 * 
	 * @param nlris
	 * @param pathAttributes
	 */
	public void addRoutes(Collection<NetworkLayerReachabilityInformation> nlris, Collection<PathAttribute> pathAttributes, NextHop nextHop) {
		for(NetworkLayerReachabilityInformation nlri : nlris) {
			Route route = new Route(getRibID(), getAddressFamilyKey(), nlri, pathAttributes, nextHop);

			if(routingTree.addRoute(route)) {
				RouteAdded event = new RouteAdded(getPeerName(), 
						getSide(), 
						route);
				
				routeAddedEvent.fire(event);
				
				if(listeners != null) {
					for(RoutingEventListener listener : listeners)
						listener.routeAdded(event);
				}
				for(RoutingEventListener listener : perRibListeners)
					listener.routeAdded(event);
			}
		}
	}

	/**
	 * Withdraw a NLRI collection from the routing tree 
	 * 
	 * @param nlris
	 * @param pathAttributes
	 */
	public void withdrawRoutes(Collection<NetworkLayerReachabilityInformation> nlris) {
		for(NetworkLayerReachabilityInformation nlri : nlris) {
			Route route = new Route(getRibID(), getAddressFamilyKey(), nlri, null, null);
			
			if(routingTree.withdrawRoute(route)) {
				RouteWithdrawn event = new RouteWithdrawn(getPeerName(), getSide(), route);
				
				routeWithdrawnEvent.fire(event);
				
				if(listeners != null) {
					for(RoutingEventListener listener : listeners)
						listener.routeWithdrawn(event);
				}
				for(RoutingEventListener listener : perRibListeners)
					listener.routeWithdrawn(event);
			}
		}
	}
	
	/**
	 * Lookup a route by a NLRI prefix. The lookup process may result in a specific, less specific route or no route at all
	 * 
	 * @param nlri prefix to look up
	 * @return the result or <code>null</code> if no result can be found.
	 */
	public LookupResult lookupRoute(NetworkLayerReachabilityInformation nlri) {
		return routingTree.lookupRoute(nlri);
	}
	
	/**
	 * Visit all nodes in the routing tree
	 * 
	 * @param visitor
	 */
	public void visitRoutingNodes(final RoutingInformationBaseVisitor visitor) {
		this.routingTree.visitTree(new RoutingTreeVisitor() {
			
			@Override
			public void visitRouteTreeNode(Route route) {
				visitor.visitRouteNode(getPeerName(), getSide(), route);
			}
		});
	}

	public void addPerRibListener(RoutingEventListener listener) {
		this.perRibListeners.add(listener);
	}
	
	public void removePerRibListener(RoutingEventListener listener) {
		this.perRibListeners.remove(listener);
	}
	
	/**
	 * @param listeners the listeners to set
	 */
	void setListeners(Collection<RoutingEventListener> listeners) {
		this.listeners = listeners;
	}

	/**
	 * @return the ribID
	 */
	public UUID getRibID() {
		return ribID;
	}

	public void withdrawRoute(Route route) {
		if(route.getRibID() == null)
			route = new Route(getRibID(), route.getAddressFamilyKey(), route.getNlri(), route.getPathAttributes(), route.getNextHop());
		
		if(routingTree.withdrawRoute(route)) {
			RouteWithdrawn event = new RouteWithdrawn(getPeerName(), getSide(), route);
			
			routeWithdrawnEvent.fire(event);
			
			if(listeners != null) {
				for(RoutingEventListener listener : listeners)
					listener.routeWithdrawn(event);
			}
			for(RoutingEventListener listener : perRibListeners)
				listener.routeWithdrawn(event);
		}
	}

	public void addRoute(Route route) {
		if(route.getRibID() == null)
			route = new Route(getRibID(), route.getAddressFamilyKey(), route.getNlri(), route.getPathAttributes(), route.getNextHop());
		
		if(routingTree.addRoute(route)) {
			RouteAdded event = new RouteAdded(getPeerName(), 
					getSide(), 
					route);
			
			routeAddedEvent.fire(event);
			
			if(listeners != null) {
				for(RoutingEventListener listener : listeners)
					listener.routeAdded(event);
			}
			for(RoutingEventListener listener : perRibListeners)
				listener.routeAdded(event);
		}
	}
}
