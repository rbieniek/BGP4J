/**
 * 
 */
package org.bgp4j.rib.processor;

/**
 * @author rainer
 *
 */
public enum RoutingInstanceState {
	STOPPED,
	STARTING,
	RUNNING,
	PARTLY_RUNNING,
	PEER_ROUTING_BASE_UNAVAILABLE,
	Address_FAMILY_ROUTING_BASE_UNAVAILABLE;
}
