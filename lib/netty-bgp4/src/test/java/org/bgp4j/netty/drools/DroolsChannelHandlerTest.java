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
 * File: org.bgp4j.netty.drools.DroolsChannelHandlerTest.java 
 */
package org.bgp4j.netty.drools;

import junit.framework.Assert;

import org.bgp4j.config.nodes.PeerConfiguration;
import org.bgp4j.netty.BGPv4Constants;
import org.bgp4j.netty.LocalhostNetworkChannelBGPv4ClientTestBase;
import org.bgp4j.netty.MessageRecordingChannelHandler;
import org.bgp4j.netty.protocol.open.BadBgpIdentifierNotificationPacket;
import org.bgp4j.netty.protocol.open.BadPeerASNotificationPacket;
import org.bgp4j.netty.protocol.open.OpenPacket;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class DroolsChannelHandlerTest extends LocalhostNetworkChannelBGPv4ClientTestBase {

	@Before
	public void before() {
		drlHandler = obtainInstance(DroolsChannelHandler.class);
		recorder = obtainInstance(MessageRecordingChannelHandler.class);
	}
	
	@After
	public void after() {
		drlHandler.shutdown();
		drlHandler = null;
		recorder = null;
	}
	
	private DroolsChannelHandler drlHandler;
	private MessageRecordingChannelHandler recorder;
	
	@Test
	public void testConnectWaitForOpenPacket() throws Exception {
		PeerConfiguration serverConfig = loadConfiguration("org/bgp4j/netty/drools/DroolsChannelHandler-Config-With-BgpPeers.xml").getPeer("server");
		drlHandler.loadRulesFile("org/bgp4j/netty/drools/Connect-SendOpen.drl");
		drlHandler.initialize(serverConfig);
		serverProxyChannelHandler.setProxiedHandler(drlHandler);

		clientProxyHander.setProxiedHandler(recorder);
		
		connectServer();
		
		Assert.assertTrue(recorder.waitOnMessageReceived(10));
		
		OpenPacket packet = safeDowncast(safeExtractChannelEvent(recorder.nextEvent(clientChannel)), OpenPacket.class);
		
		Assert.assertEquals(serverConfig.getLocalAS(), packet.getAutonomousSystem());
		Assert.assertEquals(serverConfig.getLocalBgpIdentifier(), packet.getBgpIdentifier());
	}
	
	@Test
	public void testConnectAndSendOpenPacketWaitForOpenPacket() throws Exception {
		PeerConfiguration clientConfig = loadConfiguration("org/bgp4j/netty/drools/DroolsChannelHandler-Config-With-BgpPeers.xml").getPeer("client");
		drlHandler.loadRulesFile("org/bgp4j/netty/drools/Connect-ReceiveOpen-SendOpen.drl");
		drlHandler.initialize(loadConfiguration("org/bgp4j/netty/drools/DroolsChannelHandler-Config-With-BgpPeers.xml").getPeer("server"));
		serverProxyChannelHandler.setProxiedHandler(drlHandler);

		clientProxyHander.setProxiedHandler(recorder);
		
		connectServer();
		clientChannel.write(new OpenPacket(BGPv4Constants.BGP_VERSION, clientConfig.getLocalAS(), clientConfig.getLocalBgpIdentifier()));
		
		Assert.assertTrue(recorder.waitOnMessageReceived(10));
		
		OpenPacket packet = safeDowncast(safeExtractChannelEvent(recorder.nextEvent(clientChannel)), OpenPacket.class);
		
		Assert.assertEquals(clientConfig.getRemoteAS(), packet.getAutonomousSystem());
		Assert.assertEquals(clientConfig.getRemoteBgpIdentifier(), packet.getBgpIdentifier());
	}
	
	@Test
	public void testConnectAndSendOpenPacketWithBadASWaitForBadPeerASNotificationPacket() throws Exception {
		PeerConfiguration clientConfig = loadConfiguration("org/bgp4j/netty/drools/DroolsChannelHandler-Config-With-BgpPeers.xml").getPeer("client");
		drlHandler.loadRulesFile("org/bgp4j/netty/drools/Connect-ReceiveOpen-SendOpen.drl");
		drlHandler.initialize(loadConfiguration("org/bgp4j/netty/drools/DroolsChannelHandler-Config-With-BgpPeers.xml").getPeer("server"));
		serverProxyChannelHandler.setProxiedHandler(drlHandler);

		clientProxyHander.setProxiedHandler(recorder);
		
		connectServer();
		clientChannel.write(new OpenPacket(BGPv4Constants.BGP_VERSION, clientConfig.getLocalAS()+1, clientConfig.getLocalBgpIdentifier()));
		
		Assert.assertTrue(recorder.waitOnMessageReceived(10));

		safeDowncast(safeExtractChannelEvent(recorder.nextEvent(clientChannel)), BadPeerASNotificationPacket.class);
	}

	
	@Test
	public void testConnectAndSendOpenPacketWithBadBgpIdentifierWaitForBadBgpIdentifierNotificationPacket() throws Exception {
		PeerConfiguration clientConfig = loadConfiguration("org/bgp4j/netty/drools/DroolsChannelHandler-Config-With-BgpPeers.xml").getPeer("client");
		drlHandler.loadRulesFile("org/bgp4j/netty/drools/Connect-ReceiveOpen-SendOpen.drl");
		drlHandler.initialize(loadConfiguration("org/bgp4j/netty/drools/DroolsChannelHandler-Config-With-BgpPeers.xml").getPeer("server"));
		serverProxyChannelHandler.setProxiedHandler(drlHandler);

		clientProxyHander.setProxiedHandler(recorder);
		
		connectServer();
		clientChannel.write(new OpenPacket(BGPv4Constants.BGP_VERSION, clientConfig.getLocalAS(), clientConfig.getLocalBgpIdentifier()+1));
		
		Assert.assertTrue(recorder.waitOnMessageReceived(10));

		safeDowncast(safeExtractChannelEvent(recorder.nextEvent(clientChannel)), BadBgpIdentifierNotificationPacket.class);
	}
}
