/**
 * 
 */
package org.bgp4j.rib.filter;

import java.util.Set;

import org.bgp4j.net.NetworkLayerReachabilityInformation;
import org.bgp4j.net.NextHop;
import org.bgp4j.net.attributes.PathAttribute;

/**
 * @author rainer
 *
 */
public interface RoutingFilter {

	public boolean matchFilter(NetworkLayerReachabilityInformation prefix, NextHop nextHop, Set<PathAttribute> pathAttributes);
}
