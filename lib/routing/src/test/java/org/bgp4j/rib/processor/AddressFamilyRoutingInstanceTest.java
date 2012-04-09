/**
 * 
 */
package org.bgp4j.rib.processor;

import java.net.Inet4Address;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.bgp4j.config.nodes.AddressFamilyRoutingPeerConfiguration;
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
import org.bgp4j.rib.PeerRoutingInformationBase;
import org.bgp4j.rib.PeerRoutingInformationBaseManager;
import org.bgp4j.rib.Route;
import org.bgp4j.rib.RoutingInformationBase;
import org.bgp4j.weld.WeldTestCaseBase;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author rainer
 *
 */
public class AddressFamilyRoutingInstanceTest extends WeldTestCaseBase {

	private static final String FIRST_PEER_NAME = "first_peer";
	private static final String SECOND_PEER_NAME = "second_peer";
	
	@Before
	public void before() throws Exception {
		instance = obtainInstance(AddressFamilyRoutingInstance.class);
		pribManager = obtainInstance(PeerRoutingInformationBaseManager.class);
		pribManager.resetManager();
		
		firstPrib = pribManager.peerRoutingInformationBase(FIRST_PEER_NAME);
		secondPrib = pribManager.peerRoutingInformationBase(SECOND_PEER_NAME);
		
		firstPrib.allocateRoutingInformationBase(RIBSide.Local, AddressFamilyKey.IPV4_UNICAST_FORWARDING);
		firstPrib.allocateRoutingInformationBase(RIBSide.Remote, AddressFamilyKey.IPV4_UNICAST_FORWARDING);
		secondPrib.allocateRoutingInformationBase(RIBSide.Local, AddressFamilyKey.IPV4_UNICAST_FORWARDING);
		secondPrib.allocateRoutingInformationBase(RIBSide.Remote, AddressFamilyKey.IPV4_UNICAST_FORWARDING);
		
		firstLocalRib = firstPrib.routingBase(RIBSide.Local, AddressFamilyKey.IPV4_UNICAST_FORWARDING);
		firstRemoteRib = firstPrib.routingBase(RIBSide.Remote, AddressFamilyKey.IPV4_UNICAST_FORWARDING);
		
		secondLocalRib = secondPrib.routingBase(RIBSide.Local, AddressFamilyKey.IPV4_UNICAST_FORWARDING);
		secondRemoteRib = secondPrib.routingBase(RIBSide.Remote, AddressFamilyKey.IPV4_UNICAST_FORWARDING);

		RoutingInformationBase ribs[] = new RoutingInformationBase[] { firstLocalRib, firstRemoteRib, secondLocalRib, secondRemoteRib };
		
		for(int i=0; i<ribs.length; i++) {
			for(int j=0; j<ribs.length; j++) {
				if(i != j) {
					Assert.assertFalse(ribs[i].getRibID().equals(ribs[j].getRibID()));
				}
			}
		}

		firstFilteredPrefixes = new HashSet<NetworkLayerReachabilityInformation>();
		firstInjectedPathAttributes = new HashSet<PathAttribute>();
		secondFilteredPrefixes = new HashSet<NetworkLayerReachabilityInformation>();
		secondInjectedPathAttributes = new HashSet<PathAttribute>();

		firstConfig = new AddressFamilyRoutingPeerConfiguration() {
			
			@Override
			public int compareTo(AddressFamilyRoutingPeerConfiguration o) {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public Set<RoutingFilterConfiguration> getRemoteRoutingFilters() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public PathAttributeConfiguration getRemoteDefaultPathAttributes() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Set<RoutingFilterConfiguration> getLocalRoutingFilters() {
				Set<RoutingFilterConfiguration> configs = new HashSet<RoutingFilterConfiguration>();
				
				configs.add(new PrefixRoutingFilterConfiguration() {
					
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
						return firstFilteredPrefixes;
					}
				});
				
				return configs;
			}
			
			@Override
			public PathAttributeConfiguration getLocalDefaultPathAttributes() {
				return new PathAttributeConfiguration() {
					
					@Override
					public int compareTo(PathAttributeConfiguration o) {
						// TODO Auto-generated method stub
						return 0;
					}
					
					@Override
					public Set<PathAttribute> getAttributes() {
						return firstInjectedPathAttributes;
					}
				};
			}
			
			@Override
			public AddressFamilyKey getAddressFamilyKey() {
				return AddressFamilyKey.IPV4_UNICAST_FORWARDING;
			}
		};

		secondConfig = new AddressFamilyRoutingPeerConfiguration() {
			
			@Override
			public int compareTo(AddressFamilyRoutingPeerConfiguration o) {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public Set<RoutingFilterConfiguration> getRemoteRoutingFilters() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public PathAttributeConfiguration getRemoteDefaultPathAttributes() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Set<RoutingFilterConfiguration> getLocalRoutingFilters() {
				Set<RoutingFilterConfiguration> configs = new HashSet<RoutingFilterConfiguration>();
				
				configs.add(new PrefixRoutingFilterConfiguration() {
					
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
						return secondFilteredPrefixes;
					}
				});
				
				return configs;
			}
			
			@Override
			public PathAttributeConfiguration getLocalDefaultPathAttributes() {
				return new PathAttributeConfiguration() {
					
					@Override
					public int compareTo(PathAttributeConfiguration o) {
						// TODO Auto-generated method stub
						return 0;
					}
					
					@Override
					public Set<PathAttribute> getAttributes() {
						return secondInjectedPathAttributes;
					}
				};
			}
			
			@Override
			public AddressFamilyKey getAddressFamilyKey() {
				return AddressFamilyKey.IPV4_UNICAST_FORWARDING;
			}
		};

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
		pribManager = null;
	}
	
	private AddressFamilyRoutingInstance instance;
	private PeerRoutingInformationBaseManager pribManager;
	private PeerRoutingInformationBase firstPrib;
	private PeerRoutingInformationBase secondPrib;
	private RoutingInformationBase firstLocalRib;
	private RoutingInformationBase firstRemoteRib;
	private RoutingInformationBase secondLocalRib;
	private RoutingInformationBase secondRemoteRib;
	private Route firstRoute;
	private Route secondRoute;
	private Route firstRouteFull;
	private Route secondRouteFull;
	private Set<NetworkLayerReachabilityInformation> firstFilteredPrefixes;
	private Set<PathAttribute> firstInjectedPathAttributes;
	private Set<NetworkLayerReachabilityInformation> secondFilteredPrefixes;
	private Set<PathAttribute> secondInjectedPathAttributes;
	private AddressFamilyRoutingPeerConfiguration firstConfig;
	private AddressFamilyRoutingPeerConfiguration secondConfig;
	
	@Test
	public void testTransparentConfiguration() {
		RouteChecker firstChecker = new RouteChecker(firstRemoteRib.getRibID(), firstRoute);
		RouteChecker secondChecker = new RouteChecker(secondRemoteRib.getRibID(), secondRoute);

		instance.configure(AddressFamilyKey.IPV4_UNICAST_FORWARDING, firstConfig, secondConfig);
		instance.startInstance(firstPrib, secondPrib);
		
		firstRemoteRib.addRoute(firstRoute);
		secondRemoteRib.addRoute(secondRoute);
		
		firstPrib.visitRoutingBases(RIBSide.Local, secondChecker);
		secondPrib.visitRoutingBases(RIBSide.Local, firstChecker);

		Assert.assertTrue(firstChecker.isFound());
		Assert.assertTrue(secondChecker.isFound());

		firstRemoteRib.withdrawRoute(firstRoute);
		secondRemoteRib.withdrawRoute(secondRoute);

		firstChecker.resetFound();
		secondChecker.resetFound();
		
		firstPrib.visitRoutingBases(RIBSide.Local, secondChecker);
		secondPrib.visitRoutingBases(RIBSide.Local, firstChecker);
		
		Assert.assertFalse(firstChecker.isFound());
		Assert.assertFalse(secondChecker.isFound());
	}

	@Test
	public void testPrefixFilterFirstToSecond() {
		RouteChecker firstChecker = new RouteChecker(firstRemoteRib.getRibID(), firstRoute);
		RouteChecker secondChecker = new RouteChecker(secondRemoteRib.getRibID(), secondRoute);

		secondFilteredPrefixes.add(firstRoute.getNlri());
		
		instance.configure(AddressFamilyKey.IPV4_UNICAST_FORWARDING, firstConfig, secondConfig);
		instance.startInstance(firstPrib, secondPrib);
		
		firstRemoteRib.addRoute(firstRoute);
		secondRemoteRib.addRoute(secondRoute);
		
		firstPrib.visitRoutingBases(RIBSide.Local, secondChecker);
		secondPrib.visitRoutingBases(RIBSide.Local, firstChecker);

		Assert.assertFalse(firstChecker.isFound());
		Assert.assertTrue(secondChecker.isFound());

		firstRemoteRib.withdrawRoute(firstRoute);
		secondRemoteRib.withdrawRoute(secondRoute);

		firstChecker.resetFound();
		secondChecker.resetFound();
		
		firstPrib.visitRoutingBases(RIBSide.Local, secondChecker);
		secondPrib.visitRoutingBases(RIBSide.Local, firstChecker);
		
		Assert.assertFalse(firstChecker.isFound());
		Assert.assertFalse(secondChecker.isFound());
	}


	@Test
	public void testPrefixFilterSecondToFirst() {
		RouteChecker firstChecker = new RouteChecker(firstRemoteRib.getRibID(), firstRoute);
		RouteChecker secondChecker = new RouteChecker(secondRemoteRib.getRibID(), secondRoute);

		firstFilteredPrefixes.add(secondRoute.getNlri());
		
		instance.configure(AddressFamilyKey.IPV4_UNICAST_FORWARDING, firstConfig, secondConfig);
		instance.startInstance(firstPrib, secondPrib);
		
		firstRemoteRib.addRoute(firstRoute);
		secondRemoteRib.addRoute(secondRoute);
		
		firstPrib.visitRoutingBases(RIBSide.Local, secondChecker);
		secondPrib.visitRoutingBases(RIBSide.Local, firstChecker);

		Assert.assertTrue(firstChecker.isFound());
		Assert.assertFalse(secondChecker.isFound());

		firstRemoteRib.withdrawRoute(firstRoute);
		secondRemoteRib.withdrawRoute(secondRoute);

		firstChecker.resetFound();
		secondChecker.resetFound();
		
		firstPrib.visitRoutingBases(RIBSide.Local, secondChecker);
		secondPrib.visitRoutingBases(RIBSide.Local, firstChecker);
		
		Assert.assertFalse(firstChecker.isFound());
		Assert.assertFalse(secondChecker.isFound());
	}

	@Test
	public void testInsertPathAttributesFirstToSecond() {
		RouteChecker firstChecker = new RouteChecker(firstRemoteRib.getRibID(), firstRouteFull);
		RouteChecker secondChecker = new RouteChecker(secondRemoteRib.getRibID(), secondRoute);

		secondInjectedPathAttributes.add(new MultiExitDiscPathAttribute(1));
		secondInjectedPathAttributes.add(new OriginPathAttribute(Origin.INCOMPLETE));
		
		instance.configure(AddressFamilyKey.IPV4_UNICAST_FORWARDING, firstConfig, secondConfig);
		instance.startInstance(firstPrib, secondPrib);
		
		firstRemoteRib.addRoute(firstRoute);
		secondRemoteRib.addRoute(secondRoute);
		
		firstPrib.visitRoutingBases(RIBSide.Local, secondChecker);
		secondPrib.visitRoutingBases(RIBSide.Local, firstChecker);

		Assert.assertTrue(firstChecker.isFound());
		Assert.assertTrue(secondChecker.isFound());

		firstRemoteRib.withdrawRoute(firstRoute);
		secondRemoteRib.withdrawRoute(secondRoute);

		firstChecker.resetFound();
		secondChecker.resetFound();
		
		firstPrib.visitRoutingBases(RIBSide.Local, secondChecker);
		secondPrib.visitRoutingBases(RIBSide.Local, firstChecker);
		
		Assert.assertFalse(firstChecker.isFound());
		Assert.assertFalse(secondChecker.isFound());
	}


	@Test
	public void testInsertPathAttributesSecondToFalse() {
		RouteChecker firstChecker = new RouteChecker(firstRemoteRib.getRibID(), firstRoute);
		RouteChecker secondChecker = new RouteChecker(secondRemoteRib.getRibID(), secondRouteFull);

		firstInjectedPathAttributes.add(new MultiExitDiscPathAttribute(1));
		firstInjectedPathAttributes.add(new OriginPathAttribute(Origin.INCOMPLETE));
		
		instance.configure(AddressFamilyKey.IPV4_UNICAST_FORWARDING, firstConfig, secondConfig);
		instance.startInstance(firstPrib, secondPrib);
		
		firstRemoteRib.addRoute(firstRoute);
		secondRemoteRib.addRoute(secondRoute);
		
		firstPrib.visitRoutingBases(RIBSide.Local, secondChecker);
		secondPrib.visitRoutingBases(RIBSide.Local, firstChecker);

		Assert.assertTrue(firstChecker.isFound());
		Assert.assertTrue(secondChecker.isFound());

		firstRemoteRib.withdrawRoute(firstRoute);
		secondRemoteRib.withdrawRoute(secondRoute);

		firstChecker.resetFound();
		secondChecker.resetFound();
		
		firstPrib.visitRoutingBases(RIBSide.Local, secondChecker);
		secondPrib.visitRoutingBases(RIBSide.Local, firstChecker);
		
		Assert.assertFalse(firstChecker.isFound());
		Assert.assertFalse(secondChecker.isFound());
	}
}
