package org.bgp4j.rib.processor;

import java.util.UUID;

import org.bgp4j.net.RIBSide;
import org.bgp4j.rib.Route;
import org.bgp4j.rib.RoutingInformationBaseVisitor;

public class RouteChecker implements RoutingInformationBaseVisitor {

	public RouteChecker(UUID ribID, Route checkedRoute) {
		this.checkedRoute = checkedRoute;
		this.ribID = ribID;
	}
	
	private UUID ribID;
	private Route checkedRoute;
	private boolean found;
	
	
	@Override
	public void visitRouteNode(String ribName, RIBSide side, Route route) {
		if(route.getRibID().equals(ribID)) {
			if(route.networkEquals(checkedRoute))
				found = true;
		}
	}


	/**
	 * @return the found
	 */
	public boolean isFound() {
		return found;
	}
	
	public void resetFound() {
		found = false;
	}
}