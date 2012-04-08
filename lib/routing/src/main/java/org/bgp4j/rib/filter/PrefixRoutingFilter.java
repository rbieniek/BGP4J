/**
 * 
 */
package org.bgp4j.rib.filter;

import java.util.Set;
import java.util.TreeSet;

import org.bgp4j.config.nodes.PrefixRoutingFilterConfiguration;
import org.bgp4j.net.NetworkLayerReachabilityInformation;
import org.bgp4j.net.NextHop;
import org.bgp4j.net.attributes.PathAttribute;
import org.bgp4j.rib.Route;

/**
 * @author rainer
 *
 */
public class PrefixRoutingFilter implements RoutingFilter {

	private Set<NetworkLayerReachabilityInformation> filterPrefixes = new TreeSet<NetworkLayerReachabilityInformation>();

	public void configure(PrefixRoutingFilterConfiguration configuration) {
		if(configuration != null)
			filterPrefixes.addAll(configuration.getFilterPrefixes());
	}
	
	/* (non-Javadoc)
	 * @see org.bgp4j.rib.filter.RoutingFilter#matchFilter(org.bgp4j.net.NetworkLayerReachabilityInformation, org.bgp4j.net.NextHop, java.util.Set)
	 */
	@Override
	public boolean matchFilter(Route route) {
		boolean match = false;
		
		for(NetworkLayerReachabilityInformation filterPrefix : filterPrefixes) {
			if(filterPrefix.isPrefixOf(route.getNlri())) {
				match = true;
				break;
			}
		}
		
		return match;
	}

}
