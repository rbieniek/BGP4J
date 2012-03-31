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
 * File: org.bgp4j.netty.fsm.BGPv4FSMTest.java 
 */
package org.bgp4j.netty.fsm;

import java.net.Inet4Address;
import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.bgp4j.net.AddressFamilyKey;
import org.bgp4j.net.InetAddressNextHop;
import org.bgp4j.net.NetworkLayerReachabilityInformation;
import org.bgp4j.net.RIBSide;
import org.bgp4j.net.attributes.LocalPrefPathAttribute;
import org.bgp4j.net.attributes.MultiExitDiscPathAttribute;
import org.bgp4j.net.attributes.NextHopPathAttribute;
import org.bgp4j.net.attributes.PathAttribute;
import org.bgp4j.netty.FSMState;
import org.bgp4j.netty.LocalhostNetworkChannelBGPv4TestBase;
import org.bgp4j.netty.drools.DroolsChannelHandler;
import org.bgp4j.netty.protocol.update.UpdatePacket;
import org.bgp4j.rib.PeerRoutingInformationBase;
import org.bgp4j.rib.PeerRoutingInformationBaseManager;
import org.bgp4j.rib.RoutingInformationBase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class BGPv4FSMTest extends LocalhostNetworkChannelBGPv4TestBase {

	@Before
	public void before() {
		pribManager = obtainInstance(PeerRoutingInformationBaseManager.class);
		pribManager.resetManager();

		drlHandler = obtainInstance(DroolsChannelHandler.class);
		fsm = obtainInstance(BGPv4FSM.class);
		fsmRegistry = obtainInstance(FSMRegistry.class);
	}
	
	@After
	public void after() {
		fsm.destroyFSM();
		fsm = null;
		
		fsmRegistry = null;
		
		drlHandler.shutdown();
		drlHandler = null;
	}
	
	private BGPv4FSM fsm;
	private DroolsChannelHandler drlHandler;
	private FSMRegistry fsmRegistry;
	private PeerRoutingInformationBaseManager  pribManager;
	
	// -- begin of test messages
	@Test
	public void testDialogUntilEstablished() throws Exception {
		drlHandler.loadRulesFile("org/bgp4j/netty/fsm/BGPv4FSM-Move-To-Established.drl");
		drlHandler.initialize(loadConfiguration("org/bgp4j/netty/fsm/BGPv4FSM-Client-Server-Config.xml").getPeer("drools1"));
		serverProxyChannelHandler.setProxiedHandler(drlHandler);
		
		fsm.configure(buildServerPortAwarePeerConfiguration(loadConfiguration("org/bgp4j/netty/fsm/BGPv4FSM-Client-Server-Config.xml").getPeer("fsm1")));
		fsmRegistry.registerFSM(fsm);
		fsm.startFSMAutomatic();
				
		Thread.sleep(5000L);
		
		Assert.assertEquals(FSMState.Established, fsm.getState());
	}
	
	@Test
	public void testDialogMismatchOnASNumberUntilIdle() throws Exception {
		drlHandler.loadRulesFile("org/bgp4j/netty/fsm/BGPv4FSM-Move-To-Established.drl");
		drlHandler.initialize(loadConfiguration("org/bgp4j/netty/fsm/BGPv4FSM-Client-Server-Config.xml").getPeer("drools2"));
		serverProxyChannelHandler.setProxiedHandler(drlHandler);
		
		fsm.configure(buildServerPortAwarePeerConfiguration(loadConfiguration("org/bgp4j/netty/fsm/BGPv4FSM-Client-Server-Config.xml").getPeer("fsm1")));
		fsmRegistry.registerFSM(fsm);
		fsm.startFSMAutomatic();
		
		Thread.sleep(5000L);
		Assert.assertEquals(FSMState.Idle, fsm.getState());
	}
	
	@Test
	public void testDialogMismatchOnBgpIdentifierUntilIdle() throws Exception {
		drlHandler.loadRulesFile("org/bgp4j/netty/fsm/BGPv4FSM-Move-To-Established.drl");
		drlHandler.initialize(loadConfiguration("org/bgp4j/netty/fsm/BGPv4FSM-Client-Server-Config.xml").getPeer("drools3"));
		serverProxyChannelHandler.setProxiedHandler(drlHandler);
		
		fsm.configure(buildServerPortAwarePeerConfiguration(loadConfiguration("org/bgp4j/netty/fsm/BGPv4FSM-Client-Server-Config.xml").getPeer("fsm1")));
		fsmRegistry.registerFSM(fsm);
		fsm.startFSMAutomatic();
		
		for(int i=0; i<10; i++) {
			if(fsm.getState() == FSMState.Idle)
				break;
			Thread.sleep(1000L);
		}
		
		Assert.assertEquals(FSMState.Idle, fsm.getState());
	}
	
	@Test
	public void testDialogIncompatibleRequiredCapabilitiesUntilIdle() throws Exception {
		drlHandler.loadRulesFile("org/bgp4j/netty/fsm/BGPv4FSM-Return-OpenPacket-With-Capabilities.drl");
		drlHandler.initialize(loadConfiguration("org/bgp4j/netty/fsm/BGPv4FSM-Client-Server-Config.xml").getPeer("drools4"));
		serverProxyChannelHandler.setProxiedHandler(drlHandler);
		
		fsm.configure(buildServerPortAwarePeerConfiguration(loadConfiguration("org/bgp4j/netty/fsm/BGPv4FSM-Client-Server-Config.xml").getPeer("fsm2")));
		fsmRegistry.registerFSM(fsm);
		fsm.startFSMAutomatic();
		
		for(int i=0; i<10; i++) {
			if(fsm.getState() == FSMState.Idle)
				break;
			Thread.sleep(1000L);
		}
		
		Assert.assertEquals(FSMState.Idle, fsm.getState());
	}
	
	
	@Test
	public void testDialogIncompatibleOptionalCapabilitiesUntilEstablished() throws Exception {
		drlHandler.loadRulesFile("org/bgp4j/netty/fsm/BGPv4FSM-Return-OpenPacket-With-Capabilities.drl");
		drlHandler.initialize(loadConfiguration("org/bgp4j/netty/fsm/BGPv4FSM-Client-Server-Config.xml").getPeer("drools4"));
		serverProxyChannelHandler.setProxiedHandler(drlHandler);
		
		fsm.configure(buildServerPortAwarePeerConfiguration(loadConfiguration("org/bgp4j/netty/fsm/BGPv4FSM-Client-Server-Config.xml").getPeer("fsm4")));
		fsmRegistry.registerFSM(fsm);
		fsm.startFSMAutomatic();
		
		for(int i=0; i<10; i++) {
			if(fsm.getState() == FSMState.Established)
				break;
			Thread.sleep(1000L);
		}
		
		Assert.assertEquals(FSMState.Established, fsm.getState());
	}
	
	@Test
	public void testDialogUntilEstablishedWithServerIPv4ClientIpv4() throws Exception {
		drlHandler.loadRulesFile("org/bgp4j/netty/fsm/BGPv4FSM-Return-OpenPacket-With-Capabilities.drl");
		drlHandler.initialize(loadConfiguration("org/bgp4j/netty/fsm/BGPv4FSM-Client-Server-Config.xml").getPeer("drools5"));
		serverProxyChannelHandler.setProxiedHandler(drlHandler);
		
		fsm.configure(buildServerPortAwarePeerConfiguration(loadConfiguration("org/bgp4j/netty/fsm/BGPv4FSM-Client-Server-Config.xml").getPeer("fsm5")));
		fsmRegistry.registerFSM(fsm);
		fsm.startFSMAutomatic();
				
		Thread.sleep(5000L);
		
		Assert.assertEquals(FSMState.Established, fsm.getState());
	}
	
	@Test
	public void testDialogUntilEstablishedWithServerIPv4ClientIpv4Ipv6() throws Exception {
		drlHandler.loadRulesFile("org/bgp4j/netty/fsm/BGPv4FSM-Return-OpenPacket-With-Capabilities.drl");
		drlHandler.initialize(loadConfiguration("org/bgp4j/netty/fsm/BGPv4FSM-Client-Server-Config.xml").getPeer("drools6"));
		serverProxyChannelHandler.setProxiedHandler(drlHandler);
		
		fsm.configure(buildServerPortAwarePeerConfiguration(loadConfiguration("org/bgp4j/netty/fsm/BGPv4FSM-Client-Server-Config.xml").getPeer("fsm2")));
		fsmRegistry.registerFSM(fsm);
		fsm.startFSMAutomatic();
				
		Thread.sleep(5000L);
		
		Assert.assertEquals(FSMState.Established, fsm.getState());
	}
	
	@Test
	public void testDialogUntilIdleWithServerIPv4IPv6ClientIpv4() throws Exception {
		drlHandler.loadRulesFile("org/bgp4j/netty/fsm/BGPv4FSM-Return-OpenPacket-With-Capabilities.drl");
		drlHandler.initialize(loadConfiguration("org/bgp4j/netty/fsm/BGPv4FSM-Client-Server-Config.xml").getPeer("drools5"));
		serverProxyChannelHandler.setProxiedHandler(drlHandler);
		
		fsm.configure(buildServerPortAwarePeerConfiguration(loadConfiguration("org/bgp4j/netty/fsm/BGPv4FSM-Client-Server-Config.xml").getPeer("fsm3")));
		fsmRegistry.registerFSM(fsm);
		fsm.startFSMAutomatic();
				
		for(int i=0; i<10; i++) {
			if(fsm.getState() == FSMState.Idle)
				break;
			Thread.sleep(1000L);
		}
		
		Assert.assertEquals(FSMState.Idle, fsm.getState());
	}
	
	@Test
	public void testDialogUntilEstablishedWithServerIPv4IPv6ClientIpv4IPv6() throws Exception {
		drlHandler.loadRulesFile("org/bgp4j/netty/fsm/BGPv4FSM-Return-OpenPacket-With-Capabilities.drl");
		drlHandler.initialize(loadConfiguration("org/bgp4j/netty/fsm/BGPv4FSM-Client-Server-Config.xml").getPeer("drools6"));
		serverProxyChannelHandler.setProxiedHandler(drlHandler);
		
		fsm.configure(buildServerPortAwarePeerConfiguration(loadConfiguration("org/bgp4j/netty/fsm/BGPv4FSM-Client-Server-Config.xml").getPeer("fsm3")));
		fsmRegistry.registerFSM(fsm);
		fsm.startFSMAutomatic();
				
		Thread.sleep(5000L);
		
		Assert.assertEquals(FSMState.Established, fsm.getState());
	}

	@Test
	public void testDialogUntilEstablishedSendLocalRoutingUpdates() throws Exception {
		drlHandler.loadRulesFile("org/bgp4j/netty/fsm/BGPv4FSM-Move-To-Established.drl");
		drlHandler.initialize(loadConfiguration("org/bgp4j/netty/fsm/BGPv4FSM-Client-Server-Config.xml").getPeer("drools1"));
		serverProxyChannelHandler.setProxiedHandler(drlHandler);
		
		fsm.configure(buildServerPortAwarePeerConfiguration(loadConfiguration("org/bgp4j/netty/fsm/BGPv4FSM-Client-Server-Config.xml").getPeer("fsm1")));
		fsmRegistry.registerFSM(fsm);
		fsm.startFSMAutomatic();
				
		Thread.sleep(5000L);
		
		Assert.assertEquals(FSMState.Established, fsm.getState());
		
		PeerRoutingInformationBase prib = pribManager.peerRoutingInformationBase("fsm1");
		RoutingInformationBase  rib = prib.routingBase(RIBSide.Local, AddressFamilyKey.IPV4_UNICAST_FORWARDING);
		
		Assert.assertNotNull(rib);
		
		NetworkLayerReachabilityInformation nlri = new NetworkLayerReachabilityInformation(24, new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x01});
		InetAddressNextHop<Inet4Address> gateway = new InetAddressNextHop<Inet4Address>((Inet4Address)Inet4Address.getByAddress(new byte[] {
				(byte)0xc0, (byte)0xa8, (byte)0x02, (byte)0x01}));
		PathAttribute localPref = new LocalPrefPathAttribute(100);
		PathAttribute multiExit = new MultiExitDiscPathAttribute(10);
		PathAttribute nextHop = new NextHopPathAttribute(gateway);

		rib.addRoutes(Arrays.asList(nlri), 
				Arrays.asList(localPref, multiExit), 
				gateway);
		
		Thread.sleep(10000L);
		
		List<UpdatePacket> updatePackets = drlHandler.selectAllReceivedPackets(UpdatePacket.class);

		Assert.assertEquals(1, updatePackets.size());
		
		assertUpdatePacket(updatePackets.remove(0), Arrays.asList(nlri), null, Arrays.asList(localPref, multiExit, nextHop));
	}

	// -- end of test messages
	
}
