/**
 *  Copyright 2012 Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * 
 * File: org.bgp4j.rib.RoutingInformationBaseTest.java 
 */
package org.bgp4j.rib;

import java.net.InetAddress;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import junit.framework.Assert;

import org.bgp4j.net.AddressFamily;
import org.bgp4j.net.AddressFamilyKey;
import org.bgp4j.net.NetworkLayerReachabilityInformation;
import org.bgp4j.net.SubsequentAddressFamily;
import org.bgp4j.net.attributes.PathAttribute;
import org.bgp4j.weld.WeldTestCaseBase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class RoutingInformationBaseTest extends WeldTestCaseBase {

	private static final String RIB_NAME = "rib1";
	private static final AddressFamilyKey RIB_AFK = new AddressFamilyKey(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING);
	private static final RIBSide RIB_SIDE = RIBSide.Local;
	private static final NetworkLayerReachabilityInformation LESS_NLRI = new NetworkLayerReachabilityInformation(24, 
			new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x04 });
	private static final NetworkLayerReachabilityInformation MORE_NLRI_1 = new NetworkLayerReachabilityInformation(28, 
			new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x04, (byte)0x10 });
	private static final NetworkLayerReachabilityInformation MORE_NLRI_2 = new NetworkLayerReachabilityInformation(28, 
			new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x04, (byte)0x20 });	
	
	@Before
	public void before() {
		catcher = obtainInstance(RouteEventCatcher.class);
		catcher.reset();
		
		rib = obtainInstance(RoutingInformationBase.class);
		rib.setPeerName(RIB_NAME);
		rib.setAddressFamilyKey(RIB_AFK);
		rib.setSide(RIB_SIDE);
		
		attrs = new HashSet<PathAttribute>();
	}
	
	@After
	public void after() {
		catcher = null;
		
		rib.destroyRIB();
		rib = null;
		
		attrs = null;
	}
	
	private RouteEventCatcher catcher;
	private RoutingInformationBase rib;
	private Collection<PathAttribute> attrs;
	
	@Test
	public void testAddSinglePrefix() {
		rib.addRoutes(Arrays.asList(LESS_NLRI), attrs, null);

		Assert.assertEquals(1, catcher.getRouteAddedEvents().size());
		Assert.assertEquals(0, catcher.getRouteWithdrawnEvents().size());
		catcher.getRouteAddedEvents().contains(new RouteAdded(RIB_NAME, RIB_SIDE, RIB_AFK, LESS_NLRI, attrs, null));
	}

	@Test
	public void testAddSinglePrefixNextHop() throws Exception {
		InetAddressNextHop<InetAddress> nextHop = new InetAddressNextHop<InetAddress>(
				InetAddress.getByAddress(new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x01, (byte)0x01 }));
		
		rib.addRoutes(Arrays.asList(LESS_NLRI), attrs, nextHop);

		Assert.assertEquals(1, catcher.getRouteAddedEvents().size());
		Assert.assertEquals(0, catcher.getRouteWithdrawnEvents().size());
		catcher.getRouteAddedEvents().contains(new RouteAdded(RIB_NAME, RIB_SIDE, RIB_AFK, LESS_NLRI, attrs, nextHop));
	}

	@Test
	public void testAddTwoPrefixFirstLessSecondMore() {
		rib.addRoutes(Arrays.asList(LESS_NLRI, MORE_NLRI_1), attrs, null);
		
		Assert.assertEquals(2, catcher.getRouteAddedEvents().size());
		Assert.assertEquals(0, catcher.getRouteWithdrawnEvents().size());
		catcher.getRouteAddedEvents().contains(new RouteAdded(RIB_NAME, RIB_SIDE, RIB_AFK, LESS_NLRI, attrs, null));
		catcher.getRouteAddedEvents().contains(new RouteAdded(RIB_NAME, RIB_SIDE, RIB_AFK, MORE_NLRI_1, attrs, null));
	}
	
	@Test
	public void testAddTwoPrefixFirstMoreSecondLess() {
		rib.addRoutes(Arrays.asList(MORE_NLRI_1, LESS_NLRI), attrs, null);
		
		Assert.assertEquals(2, catcher.getRouteAddedEvents().size());
		Assert.assertEquals(0, catcher.getRouteWithdrawnEvents().size());
		catcher.getRouteAddedEvents().contains(new RouteAdded(RIB_NAME, RIB_SIDE, RIB_AFK, LESS_NLRI, attrs, null));
		catcher.getRouteAddedEvents().contains(new RouteAdded(RIB_NAME, RIB_SIDE, RIB_AFK, MORE_NLRI_1, attrs, null));
	}
	
	@Test
	public void testAddThreePrefix() {
		rib.addRoutes(Arrays.asList(MORE_NLRI_1, MORE_NLRI_2, LESS_NLRI), attrs, null);
		
		Assert.assertEquals(3, catcher.getRouteAddedEvents().size());
		Assert.assertEquals(0, catcher.getRouteWithdrawnEvents().size());
		catcher.getRouteAddedEvents().contains(new RouteAdded(RIB_NAME, RIB_SIDE, RIB_AFK, LESS_NLRI, attrs, null));
		catcher.getRouteAddedEvents().contains(new RouteAdded(RIB_NAME, RIB_SIDE, RIB_AFK, MORE_NLRI_1, attrs, null));
		catcher.getRouteAddedEvents().contains(new RouteAdded(RIB_NAME, RIB_SIDE, RIB_AFK, MORE_NLRI_2, attrs, null));
	}
	
	@Test
	public void testAddThreePrefixRemoveOne() {
		rib.addRoutes(Arrays.asList(MORE_NLRI_1, MORE_NLRI_2, LESS_NLRI), attrs, null);
		
		Assert.assertEquals(3, catcher.getRouteAddedEvents().size());
		Assert.assertEquals(0, catcher.getRouteWithdrawnEvents().size());
		catcher.getRouteAddedEvents().contains(new RouteAdded(RIB_NAME, RIB_SIDE, RIB_AFK, LESS_NLRI, attrs, null));
		catcher.getRouteAddedEvents().contains(new RouteAdded(RIB_NAME, RIB_SIDE, RIB_AFK, MORE_NLRI_1, attrs, null));
		catcher.getRouteAddedEvents().contains(new RouteAdded(RIB_NAME, RIB_SIDE, RIB_AFK, MORE_NLRI_2, attrs, null));
		
		rib.withdrawRoutes(Arrays.asList(MORE_NLRI_2));
		Assert.assertEquals(3, catcher.getRouteAddedEvents().size());
		Assert.assertEquals(1, catcher.getRouteWithdrawnEvents().size());
		catcher.getRouteWithdrawnEvents().contains(new RouteAdded(RIB_NAME, RIB_SIDE, RIB_AFK, MORE_NLRI_2, attrs, null));
	}
}
