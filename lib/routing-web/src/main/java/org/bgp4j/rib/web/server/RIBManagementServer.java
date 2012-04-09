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
 * File: org.bgp4j.rib.web.server.RIBManagementServer.java 
 */
package org.bgp4j.rib.web.server;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.bgp4j.net.AddressFamily;
import org.bgp4j.net.AddressFamilyKey;
import org.bgp4j.net.RIBSide;
import org.bgp4j.net.SubsequentAddressFamily;
import org.bgp4j.rib.PeerRoutingInformationBase;
import org.bgp4j.rib.PeerRoutingInformationBaseManager;
import org.bgp4j.rib.PeerRoutingInformationBaseVisitor;
import org.bgp4j.rib.Route;
import org.bgp4j.rib.RoutingInformationBaseVisitor;
import org.bgp4j.rib.web.dto.RIBCollection;
import org.bgp4j.rib.web.dto.RouteCollection;
import org.bgp4j.rib.web.dto.RouteDTO;
import org.bgp4j.rib.web.dto.RouteInformationBaseDTO;
import org.bgp4j.rib.web.interfaces.RIBManagement;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
@Singleton
public class RIBManagementServer implements RIBManagement {

	private static class RouteCollectionBuilder implements RoutingInformationBaseVisitor {

		private RouteCollection routeCollection;
		
		@Override
		public void visitRouteNode(String ribName, RIBSide side, Route route) {
			if(routeCollection == null)
				routeCollection = new RouteCollection();
			
			routeCollection.getEntries().add(new RouteDTO(route));
		}

		/**
		 * @return the routeCollection
		 */
		public RouteCollection getRouteCollection() {
			return routeCollection;
		}
		
	}
	
	private static class RibFinder implements PeerRoutingInformationBaseVisitor {

		private UUID ribID;
		private String ribName;
		private AddressFamilyKey afk;
		private RIBSide side;
		private boolean found;
		
		public RibFinder(UUID ribID) {
			this.ribID = ribID;
		}
		
		@Override
		public void visitRoutingBase(String ribName, UUID ribID, AddressFamilyKey afk, RIBSide side) {
			if(this.ribID.equals(ribID)) {
				this.afk = afk;
				this.ribName = ribName;
				this.side = side;
				
				found = true;
			}
		}

		/**
		 * @return the ribName
		 */
		public String getRibName() {
			return ribName;
		}

		/**
		 * @return the afk
		 */
		public AddressFamilyKey getAfk() {
			return afk;
		}

		/**
		 * @return the found
		 */
		public boolean isFound() {
			return found;
		}

		/**
		 * @return the side
		 */
		public RIBSide getSide() {
			return side;
		}

		
	}

	private @Inject PeerRoutingInformationBaseManager pribManager; 
	
	@Override
	public RIBCollection ribs() {
		final RIBCollection result = new RIBCollection();
		
		pribManager.vistPeerRoutingBases(new PeerRoutingInformationBaseVisitor() {
			
			@Override
			public void visitRoutingBase(String ribName, UUID ribID, AddressFamilyKey afk, RIBSide side) {
				RouteInformationBaseDTO entry = new RouteInformationBaseDTO();

				entry.setName(ribName);
				entry.setRibID(ribID);
				entry.setAfi(afk.getAddressFamily());
				entry.setSafi(afk.getSubsequentAddressFamily());
				entry.setSide(side);
				
				result.getEntries().add(entry);
			}
		});
		
		return result;
	}

	@Override
	public RouteCollection routes(String peer, RIBSide side, AddressFamily afi,	SubsequentAddressFamily safi) {
		RouteCollectionBuilder builder = new RouteCollectionBuilder();

		PeerRoutingInformationBase prib = pribManager.peerRoutingInformationBase(peer);
		
		if(prib != null) {
			Set<AddressFamilyKey> keys = new HashSet<AddressFamilyKey>();
			
			keys.add(new AddressFamilyKey(afi, safi));
			
			prib.visitRoutingBases(side, builder, keys);
		}
		
		return builder.getRouteCollection();
	}

	@Override
	public RouteCollection routes(UUID uuid) {
		RibFinder finder = new RibFinder(uuid);
		RouteCollection result = null;
		
		pribManager.vistPeerRoutingBases(finder);
		
		if(finder.isFound())
			result = routes(finder.getRibName(), finder.getSide(), finder.getAfk().getAddressFamily(), finder.getAfk().getSubsequentAddressFamily());
		
		return result;
	}

}
