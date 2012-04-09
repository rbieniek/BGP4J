/**
 * 
 */
package org.bgp4j.rib.processor;

import java.net.Inet4Address;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import junit.framework.Assert;

import org.bgp4j.config.nodes.PathAttributeConfiguration;
import org.bgp4j.config.nodes.PrefixRoutingFilterConfiguration;
import org.bgp4j.config.nodes.RoutingFilterConfiguration;
import org.bgp4j.net.AddressFamilyKey;
import org.bgp4j.net.InetAddressNextHop;
import org.bgp4j.net.NetworkLayerReachabilityInformation;
import org.bgp4j.net.Origin;
import org.bgp4j.net.RIBSide;
import org.bgp4j.net.attributes.LocalPrefPathAttribute;
import org.bgp4j.net.attributes.MultiExitDiscPathAttribute;
import org.bgp4j.net.attributes.OriginPathAttribute;
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
		
		public void resetFound() {
			found = false;
		}
	}

	@Before
	public void before() throws Exception {
		sourceRib = obtainInstance(RoutingInformationBase.class);
		targetRib = obtainInstance(RoutingInformationBase.class);
		listener = obtainInstance(RouteTransportListener.class);
		
		filterConfigs = new HashSet<RoutingFilterConfiguration>();
		injectedPathAttributes = new HashSet<PathAttribute>();
		
		listener.setSource(sourceRib);
		sourceRib.addPerRibListener(listener);
		listener.setTarget(targetRib);

		firstRoute = new Route(AddressFamilyKey.IPV4_UNICAST_FORWARDING, 
				new NetworkLayerReachabilityInformation(24, new byte[] { (byte)0xc0, (byte)0xa8, 0x02}), 
				Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100)), 
				new InetAddressNextHop<Inet4Address>((Inet4Address)Inet4Address.getByAddress(new byte[] {(byte)0xc0, (byte)0xa8, 0x01, 0x01})));
		firstRouteFull = new Route(AddressFamilyKey.IPV4_UNICAST_FORWARDING, 
				new NetworkLayerReachabilityInformation(24, new byte[] { (byte)0xc0, (byte)0xa8, 0x02}), 
				Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), 
						(PathAttribute)new MultiExitDiscPathAttribute(1), 
						(PathAttribute)new OriginPathAttribute(Origin.INCOMPLETE)), 
				new InetAddressNextHop<Inet4Address>((Inet4Address)Inet4Address.getByAddress(new byte[] {(byte)0xc0, (byte)0xa8, 0x01, 0x01})));
		secondRoute = new Route(AddressFamilyKey.IPV4_UNICAST_FORWARDING, 
				new NetworkLayerReachabilityInformation(24, new byte[] { (byte)0xc0, (byte)0xa8, 0x03}), 
				Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100)), 
				new InetAddressNextHop<Inet4Address>((Inet4Address)Inet4Address.getByAddress(new byte[] {(byte)0xc0, (byte)0xa8, 0x01, 0x01})));
		secondRouteFull = new Route(AddressFamilyKey.IPV4_UNICAST_FORWARDING, 
				new NetworkLayerReachabilityInformation(24, new byte[] { (byte)0xc0, (byte)0xa8, 0x03}), 
				Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), 
						(PathAttribute)new MultiExitDiscPathAttribute(1), 
						(PathAttribute)new OriginPathAttribute(Origin.INCOMPLETE)), 
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
	private Route firstRoute;
	private Route secondRoute;
	private Route firstRouteFull;
	private Route secondRouteFull;
	private Set<RoutingFilterConfiguration> filterConfigs;
	private Set<PathAttribute> injectedPathAttributes;

	
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

		sourceRib.withdrawRoute(firstRoute);
		sourceRib.withdrawRoute(secondRoute);
		
		firstChecker.resetFound();
		secondChecker.resetFound();
		
		targetRib.visitRoutingNodes(firstChecker);
		targetRib.visitRoutingNodes(secondChecker);
		
		Assert.assertFalse(firstChecker.isFound());
		Assert.assertFalse(secondChecker.isFound());
	}

	
	@Test
	public void testPrefixFilter() throws Exception {
		RouteChecker firstChecker = new RouteChecker(sourceRib.getRibID(), firstRoute);
		RouteChecker secondChecker = new RouteChecker(sourceRib.getRibID(), secondRoute);
		
		filterConfigs.add(new PrefixRoutingFilterConfiguration() {
			
			@Override
			public int compareTo(RoutingFilterConfiguration o) {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public String getName() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Set<NetworkLayerReachabilityInformation> getFilterPrefixes() {
				Set<NetworkLayerReachabilityInformation> filteredRoutes = new HashSet<NetworkLayerReachabilityInformation>();

				filteredRoutes.add(secondRoute.getNlri());
				
				return filteredRoutes;
			}
		});

		listener.configure(filterConfigs, new PathAttributeConfiguration() {
			
			@Override
			public int compareTo(PathAttributeConfiguration o) {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public Set<PathAttribute> getAttributes() {
				return injectedPathAttributes;
			}
		});

		sourceRib.addRoute(firstRoute);
		sourceRib.addRoute(secondRoute);
		
		targetRib.visitRoutingNodes(firstChecker);
		targetRib.visitRoutingNodes(secondChecker);
		
		Assert.assertTrue(firstChecker.isFound());
		Assert.assertFalse(secondChecker.isFound());

		sourceRib.withdrawRoute(firstRoute);
		sourceRib.withdrawRoute(secondRoute);
		
		firstChecker.resetFound();
		secondChecker.resetFound();
		
		targetRib.visitRoutingNodes(firstChecker);
		targetRib.visitRoutingNodes(secondChecker);
		
		Assert.assertFalse(firstChecker.isFound());
		Assert.assertFalse(secondChecker.isFound());
	}

	@Test
	public void testInjectAttributes() throws Exception {
		RouteChecker firstChecker = new RouteChecker(sourceRib.getRibID(), firstRouteFull);
		RouteChecker secondChecker = new RouteChecker(sourceRib.getRibID(), secondRouteFull);

		injectedPathAttributes.add(new MultiExitDiscPathAttribute(1));
		injectedPathAttributes.add(new OriginPathAttribute(Origin.INCOMPLETE));
		
		listener.configure(filterConfigs, new PathAttributeConfiguration() {
			
			@Override
			public int compareTo(PathAttributeConfiguration o) {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public Set<PathAttribute> getAttributes() {
				return injectedPathAttributes;
			}
		});

		sourceRib.addRoute(firstRoute);
		sourceRib.addRoute(secondRoute);
		
		targetRib.visitRoutingNodes(firstChecker);
		targetRib.visitRoutingNodes(secondChecker);
		
		Assert.assertTrue(firstChecker.isFound());
		Assert.assertTrue(secondChecker.isFound());

		sourceRib.withdrawRoute(firstRoute);
		sourceRib.withdrawRoute(secondRoute);
		
		firstChecker.resetFound();
		secondChecker.resetFound();
		
		targetRib.visitRoutingNodes(firstChecker);
		targetRib.visitRoutingNodes(secondChecker);
		
		Assert.assertFalse(firstChecker.isFound());
		Assert.assertFalse(secondChecker.isFound());
	}
	
	@Test
	public void testPathAttributesInjectAttributes() throws Exception {
		RouteChecker firstChecker = new RouteChecker(sourceRib.getRibID(), firstRouteFull);
		RouteChecker secondChecker = new RouteChecker(sourceRib.getRibID(), secondRouteFull);
		RouteChecker thirdChecker = new RouteChecker(sourceRib.getRibID(), firstRoute);
		RouteChecker fourthChecker = new RouteChecker(sourceRib.getRibID(), secondRoute);

		injectedPathAttributes.add(new MultiExitDiscPathAttribute(1));
		injectedPathAttributes.add(new OriginPathAttribute(Origin.INCOMPLETE));
		
		filterConfigs.add(new PrefixRoutingFilterConfiguration() {
			
			@Override
			public int compareTo(RoutingFilterConfiguration o) {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public String getName() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Set<NetworkLayerReachabilityInformation> getFilterPrefixes() {
				Set<NetworkLayerReachabilityInformation> filteredRoutes = new HashSet<NetworkLayerReachabilityInformation>();

				filteredRoutes.add(secondRoute.getNlri());
				
				return filteredRoutes;
			}
		});

		listener.configure(filterConfigs, new PathAttributeConfiguration() {
			
			@Override
			public int compareTo(PathAttributeConfiguration o) {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public Set<PathAttribute> getAttributes() {
				return injectedPathAttributes;
			}
		});

		sourceRib.addRoute(firstRoute);
		sourceRib.addRoute(secondRoute);
		
		targetRib.visitRoutingNodes(firstChecker);
		targetRib.visitRoutingNodes(secondChecker);
		targetRib.visitRoutingNodes(thirdChecker);
		targetRib.visitRoutingNodes(fourthChecker);
		
		Assert.assertTrue(firstChecker.isFound());
		Assert.assertFalse(secondChecker.isFound());
		Assert.assertFalse(thirdChecker.isFound());
		Assert.assertFalse(fourthChecker.isFound());

		sourceRib.withdrawRoute(firstRoute);
		sourceRib.withdrawRoute(secondRoute);
		
		firstChecker.resetFound();
		secondChecker.resetFound();
		
		targetRib.visitRoutingNodes(firstChecker);
		targetRib.visitRoutingNodes(secondChecker);
		targetRib.visitRoutingNodes(thirdChecker);
		targetRib.visitRoutingNodes(fourthChecker);
		
		Assert.assertFalse(firstChecker.isFound());
		Assert.assertFalse(secondChecker.isFound());
	}

}
