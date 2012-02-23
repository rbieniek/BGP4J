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
 * File: org.bgp4.config.global.ApplicationConfigurationTest.java 
 */
package org.bgp4.config.global;

import java.net.InetAddress;

import junit.framework.Assert;

import org.bgp4.config.ConfigTestBase;
import org.bgp4.config.nodes.BgpServerConfiguration;
import org.bgp4.config.nodes.PeerConfiguration;
import org.bgp4.config.nodes.impl.BgpServerConfigurationImpl;
import org.bgp4.config.nodes.impl.ClientConfigurationImpl;
import org.bgp4.config.nodes.impl.PeerConfigurationImpl;
import org.bgp4.config.nodes.impl.ServerConfigurationImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class ApplicationConfigurationTest extends ConfigTestBase {

	@Before
	public void before() {
		this.applicationConfig = obtainInstance(ApplicationConfiguration.class);
		this.applicationConfig.resetConfiguration();
		this.catcher = obtainInstance(ApplicationConfigurationEventCatcher.class);
		this.catcher.reset();
	}
	
	@After
	public void after() {
		this.applicationConfig = null;
		this.catcher = null;
	}
	
	private ApplicationConfiguration applicationConfig;
	private ApplicationConfigurationEventCatcher catcher;
	
	@Test
	public void testBgpServerConfigurationAdded() throws Exception {
		BgpServerConfiguration config = new BgpServerConfigurationImpl(new ServerConfigurationImpl(InetAddress.getByName("192.168.1.1")));

		Assert.assertNull(applicationConfig.getBgpServerConfiguration());
		applicationConfig.setBgpServerConfiguration(config);
		Assert.assertNotNull(applicationConfig.getBgpServerConfiguration());
		Assert.assertTrue(catcher.isBgpServerConfigurationEventFired());
		Assert.assertEquals(EventType.CONFIGURATION_ADDED, catcher.getBgpServerConfigurationEventType());
		Assert.assertEquals(config, catcher.getBgpServerConfiguration());
	}

	
	@Test
	public void testBgpServerNoConfigurationAdded() throws Exception {
		Assert.assertNull(applicationConfig.getBgpServerConfiguration());
		applicationConfig.setBgpServerConfiguration(null);
		Assert.assertFalse(catcher.isBgpServerConfigurationEventFired());
	}
	
	@Test
	public void testBgpServerConfigurationAddedAndReadded() throws Exception {
		BgpServerConfiguration config = new BgpServerConfigurationImpl(new ServerConfigurationImpl(InetAddress.getByName("192.168.1.1")));

		Assert.assertNull(applicationConfig.getBgpServerConfiguration());
		applicationConfig.setBgpServerConfiguration(config);
		Assert.assertNotNull(applicationConfig.getBgpServerConfiguration());
		Assert.assertTrue(catcher.isBgpServerConfigurationEventFired());
		Assert.assertEquals(EventType.CONFIGURATION_ADDED, catcher.getBgpServerConfigurationEventType());
		Assert.assertEquals(config, catcher.getBgpServerConfiguration());
		
		catcher.reset();
		applicationConfig.setBgpServerConfiguration(config);
		Assert.assertNotNull(applicationConfig.getBgpServerConfiguration());
		Assert.assertFalse(catcher.isBgpServerConfigurationEventFired());
		Assert.assertNull(catcher.getBgpServerConfigurationEventType());
		Assert.assertNull(catcher.getBgpServerConfiguration());
	}

	@Test
	public void testBgpServerConfigurationChanged() throws Exception {
		BgpServerConfiguration config;

		config = new BgpServerConfigurationImpl(new ServerConfigurationImpl(InetAddress.getByName("192.168.1.1")));
		Assert.assertNull(applicationConfig.getBgpServerConfiguration());
		applicationConfig.setBgpServerConfiguration(config);
		Assert.assertNotNull(applicationConfig.getBgpServerConfiguration());
		Assert.assertTrue(catcher.isBgpServerConfigurationEventFired());
		Assert.assertEquals(EventType.CONFIGURATION_ADDED, catcher.getBgpServerConfigurationEventType());
		Assert.assertEquals(config, catcher.getBgpServerConfiguration());

		catcher.reset();

		config = new BgpServerConfigurationImpl(new ServerConfigurationImpl(InetAddress.getByName("192.168.2.1")));
		applicationConfig.setBgpServerConfiguration(config);
		Assert.assertNotNull(applicationConfig.getBgpServerConfiguration());
		Assert.assertTrue(catcher.isBgpServerConfigurationEventFired());
		Assert.assertEquals(EventType.CONFIGURATION_CHANGED, catcher.getBgpServerConfigurationEventType());
		Assert.assertEquals(config, catcher.getBgpServerConfiguration());
	}


	@Test
	public void testBgpServerConfigurationRemoved() throws Exception {
		BgpServerConfiguration config;

		config = new BgpServerConfigurationImpl(new ServerConfigurationImpl(InetAddress.getByName("192.168.1.1")));
		Assert.assertNull(applicationConfig.getBgpServerConfiguration());
		applicationConfig.setBgpServerConfiguration(config);
		Assert.assertNotNull(applicationConfig.getBgpServerConfiguration());
		Assert.assertTrue(catcher.isBgpServerConfigurationEventFired());
		Assert.assertEquals(EventType.CONFIGURATION_ADDED, catcher.getBgpServerConfigurationEventType());
		Assert.assertEquals(config, catcher.getBgpServerConfiguration());

		catcher.reset();

		config = null;
		applicationConfig.setBgpServerConfiguration(config);
		Assert.assertNull(applicationConfig.getBgpServerConfiguration());
		Assert.assertTrue(catcher.isBgpServerConfigurationEventFired());
		Assert.assertEquals(EventType.CONFIGURATION_REMOVED, catcher.getBgpServerConfigurationEventType());
		Assert.assertNull(catcher.getBgpServerConfiguration());
	}
	
	@Test
	public void testPeerConfigurationAdded() throws Exception {
		PeerConfiguration peer; 
		
		peer = new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1"), 179), // client configuration 
				10, // local AS
				11, // remote AS
				0xc0a80301L, // local BGP identifier
				0xc0a80401L, // remote BGP identifier
				60, // connect retry time
				300, // hold time
				false, // hold timer disabled
				30); // idle hold time
		
		Assert.assertEquals(0, applicationConfig.listPeerConfigurations().size());
		applicationConfig.putPeer(peer);
		Assert.assertEquals(1, applicationConfig.listPeerConfigurations().size());
		Assert.assertTrue(applicationConfig.listPeerConfigurations().contains(peer));
		Assert.assertTrue(catcher.isPeerConfigurationEventFired());
		Assert.assertNull(catcher.getFormerPeerConfiguration());
		Assert.assertEquals(peer, catcher.getCurrentPeerConfiguration());
		Assert.assertEquals(EventType.CONFIGURATION_ADDED, catcher.getPeerConfigurationEventType());
	}

	@Test
	public void testPeerConfigurationChanged() throws Exception {
		PeerConfiguration peer; 
		PeerConfiguration formerPeer; 
		
		peer = new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1"), 179), // client configuration 
				10, // local AS
				11, // remote AS
				0xc0a80301L, // local BGP identifier
				0xc0a80401L, // remote BGP identifier
				60, // connect retry time
				300, // hold time
				false, // hold timer disabled
				30); // idle hold time
		
		Assert.assertEquals(0, applicationConfig.listPeerConfigurations().size());
		applicationConfig.putPeer(peer);
		Assert.assertEquals(1, applicationConfig.listPeerConfigurations().size());
		Assert.assertTrue(applicationConfig.listPeerConfigurations().contains(peer));
		Assert.assertTrue(catcher.isPeerConfigurationEventFired());
		Assert.assertNull(catcher.getFormerPeerConfiguration());
		Assert.assertEquals(peer, catcher.getCurrentPeerConfiguration());
		Assert.assertEquals(EventType.CONFIGURATION_ADDED, catcher.getPeerConfigurationEventType());
		
		catcher.reset();
		
		formerPeer = peer;
		peer = new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.6.1"), 179), // client configuration 
				10, // local AS
				11, // remote AS
				0xc0a80301L, // local BGP identifier
				0xc0a80601L, // remote BGP identifier
				60, // connect retry time
				300, // hold time
				false, // hold timer disabled
				30); // idle hold time

		applicationConfig.putPeer(peer);
		Assert.assertEquals(1, applicationConfig.listPeerConfigurations().size());
		Assert.assertTrue(applicationConfig.listPeerConfigurations().contains(peer));
		Assert.assertTrue(catcher.isPeerConfigurationEventFired());
		Assert.assertEquals(formerPeer, catcher.getFormerPeerConfiguration());
		Assert.assertEquals(peer, catcher.getCurrentPeerConfiguration());
		Assert.assertEquals(EventType.CONFIGURATION_CHANGED, catcher.getPeerConfigurationEventType());
	}

	@Test
	public void testPeerConfigurationAddedTwoPeers() throws Exception {
		PeerConfiguration peer; 
		PeerConfiguration otherPeer; 
		
		peer = new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1"), 179), // client configuration 
				10, // local AS
				11, // remote AS
				0xc0a80301L, // local BGP identifier
				0xc0a80401L, // remote BGP identifier
				60, // connect retry time
				300, // hold time
				false, // hold timer disabled
				30); // idle hold time
		
		Assert.assertEquals(0, applicationConfig.listPeerConfigurations().size());
		applicationConfig.putPeer(peer);
		Assert.assertEquals(1, applicationConfig.listPeerConfigurations().size());
		Assert.assertTrue(applicationConfig.listPeerConfigurations().contains(peer));
		Assert.assertTrue(catcher.isPeerConfigurationEventFired());
		Assert.assertNull(catcher.getFormerPeerConfiguration());
		Assert.assertEquals(peer, catcher.getCurrentPeerConfiguration());
		Assert.assertEquals(EventType.CONFIGURATION_ADDED, catcher.getPeerConfigurationEventType());
		
		catcher.reset();
		
		otherPeer = new PeerConfigurationImpl("bar", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.6.1"), 179), // client configuration 
				10, // local AS
				11, // remote AS
				0xc0a80301L, // local BGP identifier
				0xc0a80601L, // remote BGP identifier
				60, // connect retry time
				300, // hold time
				false, // hold timer disabled
				30); // idle hold time

		applicationConfig.putPeer(otherPeer);
		Assert.assertEquals(2, applicationConfig.listPeerConfigurations().size());
		Assert.assertTrue(applicationConfig.listPeerConfigurations().contains(peer));
		Assert.assertTrue(applicationConfig.listPeerConfigurations().contains(otherPeer));
		Assert.assertTrue(catcher.isPeerConfigurationEventFired());
		Assert.assertNull(catcher.getFormerPeerConfiguration());
		Assert.assertEquals(otherPeer, catcher.getCurrentPeerConfiguration());
		Assert.assertEquals(EventType.CONFIGURATION_ADDED, catcher.getPeerConfigurationEventType());
	}
	
	@Test
	public void testPeerConfigurationRemoved() throws Exception {
		PeerConfiguration peer; 
		
		peer = new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1"), 179), // client configuration 
				10, // local AS
				11, // remote AS
				0xc0a80301L, // local BGP identifier
				0xc0a80401L, // remote BGP identifier
				60, // connect retry time
				300, // hold time
				false, // hold timer disabled
				30); // idle hold time
		
		Assert.assertEquals(0, applicationConfig.listPeerConfigurations().size());
		applicationConfig.putPeer(peer);
		Assert.assertEquals(1, applicationConfig.listPeerConfigurations().size());
		Assert.assertTrue(applicationConfig.listPeerConfigurations().contains(peer));
		Assert.assertTrue(catcher.isPeerConfigurationEventFired());
		Assert.assertNull(catcher.getFormerPeerConfiguration());
		Assert.assertEquals(peer, catcher.getCurrentPeerConfiguration());
		Assert.assertEquals(EventType.CONFIGURATION_ADDED, catcher.getPeerConfigurationEventType());
		
		catcher.reset();
		applicationConfig.removePeer(peer.getPeerName());
		
		Assert.assertEquals(0, applicationConfig.listPeerConfigurations().size());
		Assert.assertFalse(applicationConfig.listPeerConfigurations().contains(peer));
		Assert.assertTrue(catcher.isPeerConfigurationEventFired());
		Assert.assertEquals(peer, catcher.getFormerPeerConfiguration());
		Assert.assertNull(catcher.getCurrentPeerConfiguration());
		Assert.assertEquals(EventType.CONFIGURATION_REMOVED, catcher.getPeerConfigurationEventType());
		
	}

	@Test
	public void testPeerConfigurationAddedTwoPeersOneRemoved() throws Exception {
		PeerConfiguration peer; 
		PeerConfiguration otherPeer; 
		
		peer = new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1"), 179), // client configuration 
				10, // local AS
				11, // remote AS
				0xc0a80301L, // local BGP identifier
				0xc0a80401L, // remote BGP identifier
				60, // connect retry time
				300, // hold time
				false, // hold timer disabled
				30); // idle hold time
		
		Assert.assertEquals(0, applicationConfig.listPeerConfigurations().size());
		applicationConfig.putPeer(peer);
		Assert.assertEquals(1, applicationConfig.listPeerConfigurations().size());
		Assert.assertTrue(applicationConfig.listPeerConfigurations().contains(peer));
		Assert.assertTrue(catcher.isPeerConfigurationEventFired());
		Assert.assertNull(catcher.getFormerPeerConfiguration());
		Assert.assertEquals(peer, catcher.getCurrentPeerConfiguration());
		Assert.assertEquals(EventType.CONFIGURATION_ADDED, catcher.getPeerConfigurationEventType());
		
		catcher.reset();
		
		otherPeer = new PeerConfigurationImpl("bar", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.6.1"), 179), // client configuration 
				10, // local AS
				11, // remote AS
				0xc0a80301L, // local BGP identifier
				0xc0a80601L, // remote BGP identifier
				60, // connect retry time
				300, // hold time
				false, // hold timer disabled
				30); // idle hold time

		applicationConfig.putPeer(otherPeer);
		Assert.assertEquals(2, applicationConfig.listPeerConfigurations().size());
		Assert.assertTrue(applicationConfig.listPeerConfigurations().contains(peer));
		Assert.assertTrue(applicationConfig.listPeerConfigurations().contains(otherPeer));
		Assert.assertTrue(catcher.isPeerConfigurationEventFired());
		Assert.assertNull(catcher.getFormerPeerConfiguration());
		Assert.assertEquals(otherPeer, catcher.getCurrentPeerConfiguration());
		Assert.assertEquals(EventType.CONFIGURATION_ADDED, catcher.getPeerConfigurationEventType());

		catcher.reset();
		applicationConfig.removePeer(peer.getPeerName());
		
		Assert.assertEquals(1, applicationConfig.listPeerConfigurations().size());
		Assert.assertFalse(applicationConfig.listPeerConfigurations().contains(peer));
		Assert.assertTrue(applicationConfig.listPeerConfigurations().contains(otherPeer));
		Assert.assertTrue(catcher.isPeerConfigurationEventFired());
		Assert.assertEquals(peer, catcher.getFormerPeerConfiguration());
		Assert.assertNull(catcher.getCurrentPeerConfiguration());
		Assert.assertEquals(EventType.CONFIGURATION_REMOVED, catcher.getPeerConfigurationEventType());
	}
}
