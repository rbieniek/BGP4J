/**
 * 
 */
package org.bgp4j.config.nodes;

import org.bgp4j.net.NetworkLayerReachabilityInformation;

/**
 * @author rainer
 *
 */
public interface RouteConfiguration extends Comparable<RouteConfiguration> {

	/**
	 * get the NLRI of the advertised route
	 * 
	 * @return
	 */
	public NetworkLayerReachabilityInformation getNlri();
	
	/**
	 * get the path attributes which should be advertised for a route
	 * 
	 * @return
	 */
	public PathAttributeConfiguration getPathAttributes();
}
