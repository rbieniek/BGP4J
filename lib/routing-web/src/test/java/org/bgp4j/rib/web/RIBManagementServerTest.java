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
 * File: org.bgp4j.management.web.WebManagementServiceTest.java 
 */
package org.bgp4j.rib.web;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import junit.framework.Assert;

import org.bgp4j.management.web.WebManagementTestBase;
import org.bgp4j.management.web.application.ManagementApplication;
import org.bgp4j.management.web.service.WebManagementServer;
import org.bgp4j.net.ASType;
import org.bgp4j.net.AddressFamily;
import org.bgp4j.net.AddressFamilyKey;
import org.bgp4j.net.BinaryNextHop;
import org.bgp4j.net.InetAddressNextHop;
import org.bgp4j.net.NetworkLayerReachabilityInformation;
import org.bgp4j.net.Origin;
import org.bgp4j.net.PathSegment;
import org.bgp4j.net.PathSegmentType;
import org.bgp4j.net.RIBSide;
import org.bgp4j.net.SubsequentAddressFamily;
import org.bgp4j.net.attributes.ASPathAttribute;
import org.bgp4j.net.attributes.AggregatorPathAttribute;
import org.bgp4j.net.attributes.AtomicAggregatePathAttribute;
import org.bgp4j.net.attributes.ClusterListPathAttribute;
import org.bgp4j.net.attributes.CommunityMember;
import org.bgp4j.net.attributes.CommunityPathAttribute;
import org.bgp4j.net.attributes.LocalPrefPathAttribute;
import org.bgp4j.net.attributes.MultiExitDiscPathAttribute;
import org.bgp4j.net.attributes.MultiProtocolReachableNLRI;
import org.bgp4j.net.attributes.MultiProtocolUnreachableNLRI;
import org.bgp4j.net.attributes.OriginPathAttribute;
import org.bgp4j.net.attributes.OriginatorIDPathAttribute;
import org.bgp4j.net.attributes.PathAttribute;
import org.bgp4j.net.attributes.UnknownPathAttribute;
import org.bgp4j.rib.PeerRoutingInformationBase;
import org.bgp4j.rib.PeerRoutingInformationBaseManager;
import org.bgp4j.rib.RoutingInformationBase;
import org.bgp4j.rib.web.dto.RIBCollection;
import org.bgp4j.rib.web.dto.RouteInformationBaseDTO;
import org.bgp4j.rib.web.dto.RouteCollection;
import org.bgp4j.rib.web.dto.RouteDTO;
import org.bgp4j.rib.web.interfaces.RIBManagement;
import org.bgp4j.rib.web.server.RIBManagementServer;
import org.jboss.resteasy.client.ProxyFactory;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class RIBManagementServerTest extends WebManagementTestBase {

	@Before
	public void before() {
		server = obtainInstance(WebManagementServer.class);
		pribManager = obtainInstance(PeerRoutingInformationBaseManager.class);
		pribManager.resetManager();
		
		management = obtainInstance(RIBManagementServer.class);
		ManagementApplication.addRegisteredSingleton(management);
	}
	
	private WebManagementServer server;
	private PeerRoutingInformationBaseManager pribManager;
	private RIBManagementServer management;
		
	@Test
	public void testListEmptyRoutingBase() throws Exception {
		server.setConfiguration(httpServerConfiguration);
		server.startServer();
		
		Thread.sleep(1000);
		
		InetSocketAddress serverAddress = httpServerConfiguration.getServerConfiguration().getListenAddress();
		
		RIBManagement client = ProxyFactory.create(RIBManagement.class, "http://" + serverAddress.getAddress().getHostAddress() + ":" + serverAddress.getPort() + "/rest");

		RIBCollection ribCollection = client.ribs();
		
		Assert.assertNotNull(ribCollection);
		Assert.assertEquals(0, ribCollection.getEntries().size());

//		Assert.assertNull(client.routes(null, null, null, null));
//		Assert.assertNull(client.routes("foo", null, null, null));
//		Assert.assertNull(client.routes("foo", RIBSide.Local, null, null));
//		Assert.assertNull(client.routes("foo", RIBSide.Local, AddressFamily.IPv4, null));
		Assert.assertNull(client.routes("foo", RIBSide.Local, AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING));
		
		server.stopServer();
	}
	
	@Test
	public void testListOnePeerLocalIPv4UnicastRoutingBase() throws Exception {
		PeerRoutingInformationBase prib = pribManager.peerRoutingInformationBase("foo");
		
		prib.allocateRoutingInformationBase(RIBSide.Local, AddressFamilyKey.IPV4_UNICAST_FORWARDING);
		
		server.setConfiguration(httpServerConfiguration);
		server.startServer();
		
		Thread.sleep(1000);
		
		InetSocketAddress serverAddress = httpServerConfiguration.getServerConfiguration().getListenAddress();
		
		RIBManagement client = ProxyFactory.create(RIBManagement.class, "http://" + serverAddress.getAddress().getHostAddress() + ":" + serverAddress.getPort() + "/rest");

		RIBCollection ribCollection = client.ribs();

		Assert.assertNotNull(ribCollection);
		
		Iterator<RouteInformationBaseDTO> it = ribCollection.getEntries().iterator();

		Assert.assertTrue(it.hasNext());
		
		RouteInformationBaseDTO entry = it.next();

		Assert.assertEquals("foo", entry.getName());
		Assert.assertEquals(RIBSide.Local, entry.getSide());
		Assert.assertEquals(AddressFamily.IPv4, entry.getAfi());
		Assert.assertEquals(SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, entry.getSafi());
		
		Assert.assertFalse(it.hasNext());

		server.stopServer();
	}
	
	@Test
	public void testListOnePeerLocalIPv4UnicastRemoteIPv4UnicastRoutingBase() throws Exception {
		PeerRoutingInformationBase prib = pribManager.peerRoutingInformationBase("foo");
		
		prib.allocateRoutingInformationBase(RIBSide.Local, AddressFamilyKey.IPV4_UNICAST_FORWARDING);
		prib.allocateRoutingInformationBase(RIBSide.Remote, AddressFamilyKey.IPV4_UNICAST_FORWARDING);
		
		server.setConfiguration(httpServerConfiguration);
		server.startServer();
		
		Thread.sleep(1000);
		
		InetSocketAddress serverAddress = httpServerConfiguration.getServerConfiguration().getListenAddress();
		
		RIBManagement client = ProxyFactory.create(RIBManagement.class, "http://" + serverAddress.getAddress().getHostAddress() + ":" + serverAddress.getPort() + "/rest");

		RIBCollection ribCollection = client.ribs();

		Assert.assertNotNull(ribCollection);
		
		Iterator<RouteInformationBaseDTO> it = ribCollection.getEntries().iterator();
		RouteInformationBaseDTO entry;
		
		Assert.assertTrue(it.hasNext());
		
		entry = it.next();

		Assert.assertEquals("foo", entry.getName());
		Assert.assertEquals(RIBSide.Local, entry.getSide());
		Assert.assertEquals(AddressFamily.IPv4, entry.getAfi());
		Assert.assertEquals(SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, entry.getSafi());
		
		Assert.assertTrue(it.hasNext());
		
		entry = it.next();

		Assert.assertEquals("foo", entry.getName());
		Assert.assertEquals(RIBSide.Remote, entry.getSide());
		Assert.assertEquals(AddressFamily.IPv4, entry.getAfi());
		Assert.assertEquals(SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, entry.getSafi());
		
		Assert.assertFalse(it.hasNext());

		server.stopServer();
	}

	@Test
	public void testListOnePeerLocalIPv4UnicastLocalIPv6UnicastRoutingBase() throws Exception {
		PeerRoutingInformationBase prib = pribManager.peerRoutingInformationBase("foo");
		RouteInformationBaseDTO entry;
		
		prib.allocateRoutingInformationBase(RIBSide.Local, AddressFamilyKey.IPV4_UNICAST_FORWARDING);
		prib.allocateRoutingInformationBase(RIBSide.Local, AddressFamilyKey.IPV6_UNICAST_FORWARDING);
		
		server.setConfiguration(httpServerConfiguration);
		server.startServer();
		
		Thread.sleep(1000);
		
		InetSocketAddress serverAddress = httpServerConfiguration.getServerConfiguration().getListenAddress();
		
		RIBManagement client = ProxyFactory.create(RIBManagement.class, "http://" + serverAddress.getAddress().getHostAddress() + ":" + serverAddress.getPort() + "/rest");

		RIBCollection ribCollection = client.ribs();

		Assert.assertNotNull(ribCollection);
		
		Iterator<RouteInformationBaseDTO> it = ribCollection.getEntries().iterator();
		
		Assert.assertTrue(it.hasNext());
		
		entry = it.next();

		Assert.assertEquals("foo", entry.getName());
		Assert.assertEquals(RIBSide.Local, entry.getSide());
		Assert.assertEquals(AddressFamily.IPv4, entry.getAfi());
		Assert.assertEquals(SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, entry.getSafi());
		
		Assert.assertTrue(it.hasNext());
		
		entry = it.next();

		Assert.assertEquals("foo", entry.getName());
		Assert.assertEquals(RIBSide.Local, entry.getSide());
		Assert.assertEquals(AddressFamily.IPv6, entry.getAfi());
		Assert.assertEquals(SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, entry.getSafi());
		
		Assert.assertFalse(it.hasNext());

		server.stopServer();
	}

	@Test
	public void testListTwoPeerLocalIPv4UnicastRoutingBase() throws Exception {
		PeerRoutingInformationBase prib;
		RouteInformationBaseDTO entry;
		
		prib = pribManager.peerRoutingInformationBase("foo");
		prib.allocateRoutingInformationBase(RIBSide.Local, AddressFamilyKey.IPV4_UNICAST_FORWARDING);
		
		prib = pribManager.peerRoutingInformationBase("bar");
		prib.allocateRoutingInformationBase(RIBSide.Local, AddressFamilyKey.IPV4_UNICAST_FORWARDING);

		server.setConfiguration(httpServerConfiguration);
		server.startServer();
		
		Thread.sleep(1000);
		
		InetSocketAddress serverAddress = httpServerConfiguration.getServerConfiguration().getListenAddress();
		
		RIBManagement client = ProxyFactory.create(RIBManagement.class, "http://" + serverAddress.getAddress().getHostAddress() + ":" + serverAddress.getPort() + "/rest");

		RIBCollection ribCollection = client.ribs();

		Assert.assertNotNull(ribCollection);
		
		Iterator<RouteInformationBaseDTO> it = ribCollection.getEntries().iterator();
		
		Assert.assertTrue(it.hasNext());
		
		entry = it.next();

		Assert.assertEquals("bar", entry.getName());
		Assert.assertEquals(RIBSide.Local, entry.getSide());
		Assert.assertEquals(AddressFamily.IPv4, entry.getAfi());
		Assert.assertEquals(SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, entry.getSafi());
		
		Assert.assertTrue(it.hasNext());
				
		entry = it.next();

		Assert.assertEquals("foo", entry.getName());
		Assert.assertEquals(RIBSide.Local, entry.getSide());
		Assert.assertEquals(AddressFamily.IPv4, entry.getAfi());
		Assert.assertEquals(SubsequentAddressFamily.NLRI_UNICAST_FORWARDING, entry.getSafi());
		
		Assert.assertFalse(it.hasNext());

		server.stopServer();
	}

	@Test
	public void testListOnePeerLocalIPv4UnicastRoutingBaseOneRoute() throws Exception {
		NetworkLayerReachabilityInformation nlri1 = new NetworkLayerReachabilityInformation(24, 
				new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x01 });
		PeerRoutingInformationBase prib = pribManager.peerRoutingInformationBase("foo");
		Set<PathAttribute> pathAttributes = new TreeSet<PathAttribute>();
		InetAddressNextHop<Inet4Address> nextHop = new InetAddressNextHop<Inet4Address>(
				(Inet4Address)InetAddress.getByAddress(new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x02, (byte)0x01 }));
		AggregatorPathAttribute aggregator = new AggregatorPathAttribute(ASType.AS_NUMBER_2OCTETS, 32, 
				((Inet4Address)Inet4Address.getByAddress(new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x03, (byte)0x01})));
		ASPathAttribute asPath = new ASPathAttribute(ASType.AS_NUMBER_2OCTETS, new PathSegment[] {
				new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_SEQUENCE, new int[] { 1, 2 ,3 }),
				new PathSegment(ASType.AS_NUMBER_2OCTETS, PathSegmentType.AS_SET, new int[] { 4, 5, 6 }),
		});
		AtomicAggregatePathAttribute atomicAggregate = new AtomicAggregatePathAttribute();
		ClusterListPathAttribute clusterList = new ClusterListPathAttribute(new int[] {64, 128, 256, 512});
		CommunityPathAttribute community = new CommunityPathAttribute(1234, Arrays.asList(new CommunityMember(16, 255), new CommunityMember(32, 512)));
		LocalPrefPathAttribute localPref = new LocalPrefPathAttribute(100);
		MultiExitDiscPathAttribute multiExistDisc = new MultiExitDiscPathAttribute(50);
		MultiProtocolReachableNLRI mpr = new MultiProtocolReachableNLRI(AddressFamily.IPv6, 
				SubsequentAddressFamily.NLRI_UNICAST_FORWARDING,
				new BinaryNextHop(new byte[] { 0x10, 0x020, 0x40 }),
				Arrays.asList(new NetworkLayerReachabilityInformation(16, new byte[] { 0x08, 0x010 }), 
						new NetworkLayerReachabilityInformation(16, new byte[] { 0x08, 0x20 })));
		MultiProtocolUnreachableNLRI mpu = new MultiProtocolUnreachableNLRI(AddressFamily.IPv6, 
				SubsequentAddressFamily.NLRI_UNICAST_FORWARDING,
				Arrays.asList(new NetworkLayerReachabilityInformation(16, new byte[] { 0x10, 0x010 }), 
						new NetworkLayerReachabilityInformation(16, new byte[] { 0x10, 0x20 })));
		OriginPathAttribute origin = new OriginPathAttribute(Origin.INCOMPLETE);
		OriginatorIDPathAttribute originatorID = new OriginatorIDPathAttribute(23456);
		UnknownPathAttribute unknown = new UnknownPathAttribute(128, new byte[] { 0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40 });
		
		pathAttributes.add(aggregator);
		pathAttributes.add(asPath);
		pathAttributes.add(atomicAggregate);		
		pathAttributes.add(clusterList);
		pathAttributes.add(community);
		pathAttributes.add(localPref);
		pathAttributes.add(multiExistDisc);
		pathAttributes.add(mpr);
		pathAttributes.add(mpu);
		pathAttributes.add(origin);
		pathAttributes.add(originatorID);
		pathAttributes.add(unknown);
		
		prib.allocateRoutingInformationBase(RIBSide.Local, AddressFamilyKey.IPV4_UNICAST_FORWARDING);

		RoutingInformationBase rib = prib.routingBase(RIBSide.Local, AddressFamilyKey.IPV4_UNICAST_FORWARDING);
		
		rib.addRoutes(Arrays.asList(nlri1), pathAttributes, nextHop);
		
		server.setConfiguration(httpServerConfiguration);
		server.startServer();
		
		Thread.sleep(1000);
		
		InetSocketAddress serverAddress = httpServerConfiguration.getServerConfiguration().getListenAddress();
		
		RIBManagement client = ProxyFactory.create(RIBManagement.class, "http://" + serverAddress.getAddress().getHostAddress() + ":" + serverAddress.getPort() + "/rest");

		RouteCollection routeCollection = client.routes("foo", RIBSide.Local, AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING);

		Assert.assertNotNull(routeCollection);
		
		Iterator<RouteDTO> it = routeCollection.getEntries().iterator();

		Assert.assertTrue(it.hasNext());
		
		RouteDTO entry = it.next();

		Assert.assertEquals(nlri1, entry.getNlri());
		Assert.assertEquals(nextHop, entry.getNextHop());
		
		Iterator<PathAttribute> paIt = entry.getPathAttributes().iterator();
		
		Assert.assertTrue(paIt.hasNext());
		Assert.assertEquals(aggregator, paIt.next());
		Assert.assertTrue(paIt.hasNext());
		Assert.assertEquals(asPath, paIt.next());
		Assert.assertTrue(paIt.hasNext());
		Assert.assertEquals(atomicAggregate, paIt.next());
		Assert.assertTrue(paIt.hasNext());
		Assert.assertEquals(clusterList, paIt.next());
		Assert.assertTrue(paIt.hasNext());
		Assert.assertEquals(community, paIt.next());
		Assert.assertTrue(paIt.hasNext());
		Assert.assertEquals(localPref, paIt.next());
		Assert.assertTrue(paIt.hasNext());
		Assert.assertEquals(multiExistDisc, paIt.next());
		Assert.assertTrue(paIt.hasNext());
		Assert.assertEquals(mpr, paIt.next());
		Assert.assertTrue(paIt.hasNext());
		Assert.assertEquals(mpu, paIt.next());
		Assert.assertTrue(paIt.hasNext());
		Assert.assertEquals(originatorID, paIt.next());
		Assert.assertTrue(paIt.hasNext());
		Assert.assertEquals(origin, paIt.next());
		Assert.assertTrue(paIt.hasNext());
		Assert.assertEquals(unknown, paIt.next());

		Assert.assertFalse(paIt.hasNext());
		
		Assert.assertFalse(it.hasNext());

		server.stopServer();
	}

	@Test
	public void testListOnePeerLocalIPv4UnicastRoutingBaseTwoRoute() throws Exception {
		NetworkLayerReachabilityInformation nlri1 = new NetworkLayerReachabilityInformation(24, 
				new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x01 });
		NetworkLayerReachabilityInformation nlri2 = new NetworkLayerReachabilityInformation(24, 
				new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x03 });
		PeerRoutingInformationBase prib = pribManager.peerRoutingInformationBase("foo");
		Set<PathAttribute> pathAttributes = new TreeSet<PathAttribute>();
		InetAddressNextHop<Inet4Address> nextHop = new InetAddressNextHop<Inet4Address>(
				(Inet4Address)InetAddress.getByAddress(new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x02, (byte)0x01 }));
		LocalPrefPathAttribute localPref = new LocalPrefPathAttribute(100);
		MultiExitDiscPathAttribute multiExistDisc = new MultiExitDiscPathAttribute(50);
		OriginPathAttribute origin = new OriginPathAttribute(Origin.INCOMPLETE);
		
		pathAttributes.add(localPref);
		pathAttributes.add(multiExistDisc);
		pathAttributes.add(origin);
		
		prib.allocateRoutingInformationBase(RIBSide.Local, AddressFamilyKey.IPV4_UNICAST_FORWARDING);

		RoutingInformationBase rib = prib.routingBase(RIBSide.Local, AddressFamilyKey.IPV4_UNICAST_FORWARDING);
		
		rib.addRoutes(Arrays.asList(nlri1, nlri2), pathAttributes, nextHop);
		
		server.setConfiguration(httpServerConfiguration);
		server.startServer();
		
		Thread.sleep(1000);
		
		InetSocketAddress serverAddress = httpServerConfiguration.getServerConfiguration().getListenAddress();
		
		RIBManagement client = ProxyFactory.create(RIBManagement.class, "http://" + serverAddress.getAddress().getHostAddress() + ":" + serverAddress.getPort() + "/rest");

		RouteCollection routeCollection = client.routes("foo", RIBSide.Local, AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING);

		Assert.assertNotNull(routeCollection);
		
		Iterator<RouteDTO> it = routeCollection.getEntries().iterator();

		Assert.assertTrue(it.hasNext());
		
		RouteDTO entry = it.next();

		Assert.assertEquals(nlri1, entry.getNlri());
		Assert.assertEquals(nextHop, entry.getNextHop());
		
		Iterator<PathAttribute> paIt = entry.getPathAttributes().iterator();
		
		Assert.assertTrue(paIt.hasNext());
		Assert.assertEquals(localPref, paIt.next());
		Assert.assertTrue(paIt.hasNext());
		Assert.assertEquals(multiExistDisc, paIt.next());
		Assert.assertTrue(paIt.hasNext());
		Assert.assertEquals(origin, paIt.next());

		Assert.assertFalse(paIt.hasNext());
		
		entry = it.next();

		Assert.assertEquals(nlri2, entry.getNlri());
		Assert.assertEquals(nextHop, entry.getNextHop());
		
		paIt = entry.getPathAttributes().iterator();
		
		Assert.assertTrue(paIt.hasNext());
		Assert.assertEquals(localPref, paIt.next());
		Assert.assertTrue(paIt.hasNext());
		Assert.assertEquals(multiExistDisc, paIt.next());
		Assert.assertTrue(paIt.hasNext());
		Assert.assertEquals(origin, paIt.next());

		Assert.assertFalse(paIt.hasNext());

		Assert.assertFalse(it.hasNext());

		server.stopServer();
	}

	@Test
	public void testListOnePeerLocalIPv4UnicastRoutingBaseTwoRouteWithDefault() throws Exception {
		NetworkLayerReachabilityInformation nlri1 = new NetworkLayerReachabilityInformation(0, null);
		NetworkLayerReachabilityInformation nlri2 = new NetworkLayerReachabilityInformation(24, 
				new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x01 });
		PeerRoutingInformationBase prib = pribManager.peerRoutingInformationBase("foo");
		Set<PathAttribute> pathAttributes = new TreeSet<PathAttribute>();
		InetAddressNextHop<Inet4Address> nextHop = new InetAddressNextHop<Inet4Address>(
				(Inet4Address)InetAddress.getByAddress(new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x02, (byte)0x01 }));
		LocalPrefPathAttribute localPref = new LocalPrefPathAttribute(100);
		MultiExitDiscPathAttribute multiExistDisc = new MultiExitDiscPathAttribute(50);
		OriginPathAttribute origin = new OriginPathAttribute(Origin.INCOMPLETE);
		
		pathAttributes.add(localPref);
		pathAttributes.add(multiExistDisc);
		pathAttributes.add(origin);
		
		prib.allocateRoutingInformationBase(RIBSide.Local, AddressFamilyKey.IPV4_UNICAST_FORWARDING);

		RoutingInformationBase rib = prib.routingBase(RIBSide.Local, AddressFamilyKey.IPV4_UNICAST_FORWARDING);
		
		rib.addRoutes(Arrays.asList(nlri1, nlri2), pathAttributes, nextHop);
		
		server.setConfiguration(httpServerConfiguration);
		server.startServer();
		
		Thread.sleep(1000);
		
		InetSocketAddress serverAddress = httpServerConfiguration.getServerConfiguration().getListenAddress();
		
		RIBManagement client = ProxyFactory.create(RIBManagement.class, "http://" + serverAddress.getAddress().getHostAddress() + ":" + serverAddress.getPort() + "/rest");

		RouteCollection routeCollection = client.routes("foo", RIBSide.Local, AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING);

		Assert.assertNotNull(routeCollection);
		
		Iterator<RouteDTO> it = routeCollection.getEntries().iterator();

		Assert.assertTrue(it.hasNext());
		
		RouteDTO entry = it.next();

		Assert.assertEquals(nlri1, entry.getNlri());
		Assert.assertEquals(nextHop, entry.getNextHop());
		
		Iterator<PathAttribute> paIt = entry.getPathAttributes().iterator();
		
		Assert.assertTrue(paIt.hasNext());
		Assert.assertEquals(localPref, paIt.next());
		Assert.assertTrue(paIt.hasNext());
		Assert.assertEquals(multiExistDisc, paIt.next());
		Assert.assertTrue(paIt.hasNext());
		Assert.assertEquals(origin, paIt.next());

		Assert.assertFalse(paIt.hasNext());
		
		entry = it.next();

		Assert.assertEquals(nlri2, entry.getNlri());
		Assert.assertEquals(nextHop, entry.getNextHop());
		
		paIt = entry.getPathAttributes().iterator();
		
		Assert.assertTrue(paIt.hasNext());
		Assert.assertEquals(localPref, paIt.next());
		Assert.assertTrue(paIt.hasNext());
		Assert.assertEquals(multiExistDisc, paIt.next());
		Assert.assertTrue(paIt.hasNext());
		Assert.assertEquals(origin, paIt.next());

		Assert.assertFalse(paIt.hasNext());

		Assert.assertFalse(it.hasNext());

		server.stopServer();
	}
}
