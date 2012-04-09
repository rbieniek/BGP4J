/**
 * 
 */
package org.bgp4j.rib.processor;

import java.net.Inet4Address;
import java.util.Arrays;
import java.util.UUID;

import junit.framework.Assert;

import org.bgp4j.net.AddressFamilyKey;
import org.bgp4j.net.InetAddressNextHop;
import org.bgp4j.net.NetworkLayerReachabilityInformation;
import org.bgp4j.net.RIBSide;
import org.bgp4j.net.attributes.LocalPrefPathAttribute;
import org.bgp4j.net.attributes.PathAttribute;
import org.bgp4j.rib.Route;
import org.bgp4j.rib.RoutingInformationBase;
import org.bgp4j.rib.RoutingInformationBaseVisitor;
import org.bgp4j.weld.WeldTestCaseBase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author rainer
 *
 */
public class RouteTransportListenerTest  extends WeldTestCaseBase {

	public static class RouteChecker implements RoutingInformationBaseVisitor {

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
		
	}

	@Before
	public void before() throws Exception {
		sourceRib = obtainInstance(RoutingInformationBase.class);
		targetRib = obtainInstance(RoutingInformationBase.class);
		listener = obtainInstance(RouteTransportListener.class);
		
		listener.setSource(sourceRib);
		sourceRib.addPerRibListener(listener);
		listener.setTarget(targetRib);

		firstRoute = new Route(AddressFamilyKey.IPV4_UNICAST_FORWARDING, 
				new NetworkLayerReachabilityInformation(24, new byte[] { (byte)0xc0, (byte)0xa8, 0x02}), 
				Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100)), 
				new InetAddressNextHop<Inet4Address>((Inet4Address)Inet4Address.getByAddress(new byte[] {(byte)0xc0, (byte)0xa8, 0x01, 0x01})));
		secondRoute = new Route(AddressFamilyKey.IPV4_UNICAST_FORWARDING, 
				new NetworkLayerReachabilityInformation(24, new byte[] { (byte)0xc0, (byte)0xa8, 0x03}), 
				Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100)), 
				new InetAddressNextHop<Inet4Address>((Inet4Address)Inet4Address.getByAddress(new byte[] {(byte)0xc0, (byte)0xa8, 0x01, 0x01})));
	}
	
	@After
	public void after() {
		sourceRib = null;
		targetRib = null;
		listener = null;
	}
	
	private RoutingInformationBase sourceRib;
	private RoutingInformationBase targetRib;
	private RouteTransportListener listener;
	Route firstRoute;
	Route secondRoute;
	
	@Test
	public void testTransparent() throws Exception {
		RouteChecker firstChecker = new RouteChecker(sourceRib.getRibID(), firstRoute);
		RouteChecker secondChecker = new RouteChecker(sourceRib.getRibID(), secondRoute);
		
		sourceRib.addRoute(firstRoute);
		sourceRib.addRoute(secondRoute);
		
		targetRib.visitRoutingNodes(firstChecker);
		targetRib.visitRoutingNodes(secondChecker);
		
		Assert.assertTrue(firstChecker.isFound());
		Assert.assertTrue(secondChecker.isFound());
	}
}
