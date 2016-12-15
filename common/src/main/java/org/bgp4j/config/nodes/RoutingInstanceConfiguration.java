/**
 * 
 */
package org.bgp4j.config.nodes;

/**
 * @author rainer
 *
 */
public interface RoutingInstanceConfiguration extends Comparable<RoutingInstanceConfiguration> {

	public RoutingPeerConfiguration getFirstPeer();
	
	public RoutingPeerConfiguration getSecondPeer();
}
