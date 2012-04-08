/**
 * 
 */
package org.bgp4j.rib.processor;

import java.util.Set;

import org.bgp4j.config.nodes.PathAttributeConfiguration;
import org.bgp4j.config.nodes.RoutingFilterConfiguration;
import org.bgp4j.rib.RouteAdded;
import org.bgp4j.rib.RouteWithdrawn;
import org.bgp4j.rib.RoutingEventListener;
import org.bgp4j.rib.RoutingInformationBase;

/**
 * @author rainer
 *
 */
public class RouteTransportListener implements RoutingEventListener {

	private RoutingInformationBase target;
	private RoutingInformationBase source;
	
	/* (non-Javadoc)
	 * @see org.bgp4j.rib.RoutingEventListener#routeAdded(org.bgp4j.rib.RouteAdded)
	 */
	@Override
	public void routeAdded(RouteAdded event) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.bgp4j.rib.RoutingEventListener#routeWithdrawn(org.bgp4j.rib.RouteWithdrawn)
	 */
	@Override
	public void routeWithdrawn(RouteWithdrawn event) {
		// TODO Auto-generated method stub

	}

	/**
	 * @param target the target to set
	 */
	void setTarget(RoutingInformationBase target) {
		this.target = target;
	}

	/**
	 * @return the source
	 */
	RoutingInformationBase getSource() {
		return source;
	}

	/**
	 * @param source the source to set
	 */
	void setSource(RoutingInformationBase source) {
		this.source = source;
	}


}
