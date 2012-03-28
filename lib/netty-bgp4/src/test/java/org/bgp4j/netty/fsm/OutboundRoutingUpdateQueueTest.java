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
 * File: org.bgp4j.netty.fsm.OutboundRoutingUpdateQueueTest.java 
 */
package org.bgp4j.netty.fsm;

import java.net.Inet4Address;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.bgp4j.net.AddressFamilyKey;
import org.bgp4j.net.InetAddressNextHop;
import org.bgp4j.net.NetworkLayerReachabilityInformation;
import org.bgp4j.net.attributes.LocalPrefPathAttribute;
import org.bgp4j.net.attributes.MultiExitDiscPathAttribute;
import org.bgp4j.net.attributes.NextHopPathAttribute;
import org.bgp4j.net.attributes.PathAttribute;
import org.bgp4j.netty.BGPv4TestBase;
import org.bgp4j.netty.protocol.update.UpdatePacket;
import org.bgp4j.rib.PeerRoutingInformationBase;
import org.bgp4j.rib.PeerRoutingInformationBaseManager;
import org.bgp4j.rib.RIBSide;
import org.bgp4j.rib.RoutingInformationBase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class OutboundRoutingUpdateQueueTest extends BGPv4TestBase {

	private static final String PEER_NAME = "peer";
	
	public static class RecordingCallback implements OutboundRoutingUpdateCallback {

		private List<UpdatePacket> updates = new LinkedList<UpdatePacket>();
		
		@Override
		public void sendUpdates(List<UpdatePacket> updates) {
			this.updates.addAll(updates);
		}

		/**
		 * @return the updates
		 */
		public List<UpdatePacket> getUpdates() {
			return updates;
		}
		
	}
	
	@Before
	public void before() {
		oruq = obtainInstance(OutboundRoutingUpdateQueue.class);
		
		pribManager = obtainInstance(PeerRoutingInformationBaseManager.class);
		pribManager.resetManager();
		
		prib = pribManager.peerRoutingInformationBase(PEER_NAME);
		callback = new RecordingCallback();
		
		oruq.setPeerName(PEER_NAME);
		oruq.setCallback(callback);
	}
	
	@After
	public void after() throws Exception {
		oruq.shutdown();
		oruq = null;
		callback = null;
		
		prib.destroyAllRoutingInformationBases();
		prib = null;

		pribManager.destroyPeerRoutingInformationBase(PEER_NAME);
		pribManager.resetManager();
		pribManager = null;
	}
	
	private PeerRoutingInformationBaseManager pribManager;
	private PeerRoutingInformationBase prib;
	private OutboundRoutingUpdateQueue oruq;
	private OutboundRoutingUpdateCallback callback;
	
	@Test
	public void testBatchSingleRouteIPv4WhileInactive() throws Exception {
		Set<AddressFamilyKey> allowed = new HashSet<AddressFamilyKey>();
		NetworkLayerReachabilityInformation nlri = new NetworkLayerReachabilityInformation(24, new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x01});
		InetAddressNextHop<Inet4Address> gateway = new InetAddressNextHop<Inet4Address>((Inet4Address)Inet4Address.getByAddress(new byte[] {
				(byte)0xc0, (byte)0xa8, (byte)0x02, (byte)0x01}));
		PathAttribute localPref = new LocalPrefPathAttribute(100);
		PathAttribute multiExit = new MultiExitDiscPathAttribute(10);
		PathAttribute nextHop = new NextHopPathAttribute(gateway);
		
		prib.allocateRoutingInformationBase(RIBSide.Local, AddressFamilyKey.IPV4_UNICAST_FORWARDING);

		RoutingInformationBase rib = prib.routingBase(RIBSide.Local, AddressFamilyKey.IPV4_UNICAST_FORWARDING);
		
		rib.addRoutes(Arrays.asList(nlri), 
				Arrays.asList(localPref, multiExit), 
				gateway);

		allowed.add(AddressFamilyKey.IPV4_UNICAST_FORWARDING);
		oruq.setUpdateMask(allowed);
		prib.visitRoutingBases(RIBSide.Local, oruq.getImportVisitor(), allowed);
		
		Assert.assertEquals(1, oruq.getNumberOfPendingUpdates());
		
		List<UpdatePacket> updatePackets = oruq.buildUpdates();
		
		Assert.assertEquals(1, updatePackets.size());
		
		assertUpdatePacket(updatePackets.remove(0), Arrays.asList(nlri), null, Arrays.asList(localPref, multiExit, nextHop));
	}
	
	@Test
	public void testBatchMultipleRouteIPv4WhileInactive() throws Exception {
		Set<AddressFamilyKey> allowed = new HashSet<AddressFamilyKey>();
		List<NetworkLayerReachabilityInformation> nlris = new LinkedList<NetworkLayerReachabilityInformation>();
		InetAddressNextHop<Inet4Address> gateway = new InetAddressNextHop<Inet4Address>((Inet4Address)Inet4Address.getByAddress(new byte[] {
				(byte)0xc0, (byte)0xa8, (byte)0x02, (byte)0x01}));
		PathAttribute localPref = new LocalPrefPathAttribute(100);
		PathAttribute multiExit = new MultiExitDiscPathAttribute(10);
		PathAttribute nextHop = new NextHopPathAttribute(gateway);
		
		nlris.add(new NetworkLayerReachabilityInformation(24, new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x01}));
		nlris.add(new NetworkLayerReachabilityInformation(24, new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x02}));
		nlris.add(new NetworkLayerReachabilityInformation(24, new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x03}));
		nlris.add(new NetworkLayerReachabilityInformation(24, new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x04}));
		
		prib.allocateRoutingInformationBase(RIBSide.Local, AddressFamilyKey.IPV4_UNICAST_FORWARDING);

		RoutingInformationBase rib = prib.routingBase(RIBSide.Local, AddressFamilyKey.IPV4_UNICAST_FORWARDING);
		
		rib.addRoutes(nlris, 
				Arrays.asList(localPref, multiExit), 
				gateway);

		allowed.add(AddressFamilyKey.IPV4_UNICAST_FORWARDING);
		oruq.setUpdateMask(allowed);
		prib.visitRoutingBases(RIBSide.Local, oruq.getImportVisitor(), allowed);
		
		Assert.assertEquals(1, oruq.getNumberOfPendingUpdates());
		
		List<UpdatePacket> updatePackets = oruq.buildUpdates();
		
		Assert.assertEquals(1, updatePackets.size());
		
		assertUpdatePacket(updatePackets.remove(0), nlris, null, Arrays.asList(localPref, multiExit, nextHop));
	}
}
