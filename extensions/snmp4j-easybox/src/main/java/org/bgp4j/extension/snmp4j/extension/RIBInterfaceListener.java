/**
 * 
 */
package org.bgp4j.extension.snmp4j.extension;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.inject.Inject;

import org.bgp4j.config.nodes.AddressFamilyRoutingConfiguration;
import org.bgp4j.config.nodes.RouteConfiguration;
import org.bgp4j.extension.snmp4j.service.EasyboxInstance;
import org.bgp4j.extension.snmp4j.service.EasyboxInterfaceEvent;
import org.bgp4j.extension.snmp4j.service.EasyboxInterfaceListener;
import org.bgp4j.net.InetAddressNextHop;
import org.bgp4j.net.NetworkLayerReachabilityInformation;
import org.bgp4j.net.attributes.PathAttribute;
import org.bgp4j.rib.RoutingInformationBase;
import org.slf4j.Logger;

/**
 * @author rainer
 *
 */
public class RIBInterfaceListener implements EasyboxInterfaceListener {

	private static class Route {
		private NetworkLayerReachabilityInformation nlri;
		private Set<PathAttribute> pathAttrbiutes = new TreeSet<PathAttribute>();
		
		public Route(NetworkLayerReachabilityInformation nlri, Set<PathAttribute> pathAttrbiutes) {
			this.nlri = nlri;
			this.pathAttrbiutes.addAll(pathAttrbiutes);
		}

		/**
		 * @return the nlri
		 */
		public NetworkLayerReachabilityInformation getNlri() {
			return nlri;
		}

		/**
		 * @return the pathAttrbiutes
		 */
		public Set<PathAttribute> getPathAttrbiutes() {
			return pathAttrbiutes;
		}
		
	}
	
	private List<Route> routes = new LinkedList<RIBInterfaceListener.Route>();
	private RoutingInformationBase rib;
	private @Inject Logger log;
	private InetAddressNextHop<Inet4Address> nextHop; 
	
	/* (non-Javadoc)
	 * @see org.bgp4j.extension.snmp4j.service.EasyboxInterfaceListener#interfaceChanged(org.bgp4j.extension.snmp4j.service.EasyboxInstance, org.bgp4j.extension.snmp4j.service.EasyboxInterfaceEvent)
	 */
	@Override
	public void interfaceChanged(EasyboxInstance instance, EasyboxInterfaceEvent event) {
		if(event.getDroppedInterface() != null) {
			log.info("Easybox " + instance.getName() + ": Interface with address " + event.getDroppedInterface().getAddress() + " went down");
			
			for(Route route : routes) {
				log.info("Easybox " + instance.getName() + ": withdrawing route for " + route.getNlri());
				
				rib.withdrawRoutes(Arrays.asList(route.nlri));
			}
		}
		if(event.getCurrentInterface() != null) {
			log.info("Easybox " + instance.getName() + ": Interface with address " + event.getCurrentInterface().getAddress() + " went up");

			for(Route route : routes) {
				log.info("Easybox " + instance.getName() + ": adding route for " + route.getNlri());
				
				rib.addRoutes(Arrays.asList(route.getNlri()), route.getPathAttrbiutes(), nextHop);
			}
			
			rib.addRoutes(Arrays.asList(new NetworkLayerReachabilityInformation(event.getCurrentInterface().getAddress())), new HashSet<PathAttribute>(), nextHop);
		}		
	}

	public void configureRouting(InetAddress nextHopAddress, AddressFamilyRoutingConfiguration routingConfig, RoutingInformationBase rib) {
		this.rib = rib;
		
		for(RouteConfiguration routeConfig : routingConfig.getRoutes()) {
			routes.add(new Route(routeConfig.getNlri(), routeConfig.getPathAttributes().getAttributes()));
		}
		
		this.nextHop = new InetAddressNextHop<Inet4Address>((Inet4Address)nextHopAddress);
	}

}
