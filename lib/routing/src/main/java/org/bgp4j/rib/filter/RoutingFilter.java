/**
 * 
 */
package org.bgp4j.rib.filter;

import org.bgp4j.rib.Route;

/**
 * @author rainer
 *
 */
public interface RoutingFilter {

	public boolean matchFilter(Route route);
}
