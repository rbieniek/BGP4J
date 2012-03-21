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

import junit.framework.Assert;

import org.bgp4j.netty.FSMState;
import org.bgp4j.netty.LocalhostNetworkChannelBGPv4TestBase;
import org.bgp4j.netty.drools.DroolsChannelHandler;
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

	// -- end of test messages
	
}
