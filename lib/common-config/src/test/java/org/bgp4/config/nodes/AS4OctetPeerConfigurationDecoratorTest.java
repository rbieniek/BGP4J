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
 * File: org.bgp4.config.nodes.AS4OctetPeerConfigurationDecoratorTest.java 
 */
package org.bgp4.config.nodes;

import java.net.InetAddress;
import java.util.Set;

import junit.framework.Assert;

import org.bgp4.config.nodes.impl.CapabilitiesImpl;
import org.bgp4.config.nodes.impl.ClientConfigurationImpl;
import org.bgp4.config.nodes.impl.PeerConfigurationImpl;
import org.bgp4j.net.AutonomousSystem4Capability;
import org.bgp4j.net.Capability;
import org.junit.Test;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class AS4OctetPeerConfigurationDecoratorTest {

	@Test
	public void test2OctetASNumber() throws Exception {
		PeerConfiguration peer = new AS4OctetPeerConfigurationDecorator(new PeerConfigurationImpl("peer1", // peer name
				new ClientConfigurationImpl(InetAddress.getLocalHost(), 17179),  // client config
				10, // local AS
				11, // remote AS
				1024, // local BGP identifier
				2048, // remote identifier
				15, // connect retry time
				15, // hold time
				false, // hold timer disabled
				30, // idle hold time
				true, // allow automatic start
				true, // allow automatic stop
				60,  // automatic start interval
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				0, // delay open time
				false, // collisition established detection
				null)); // capabilities
		
		Assert.assertEquals(10, peer.getLocalAS());
		Assert.assertEquals(11, peer.getRemoteAS());
		
		AutonomousSystem4Capability as4cap = findRequiredCapability(peer.getCapabilities(), AutonomousSystem4Capability.class);
		
		Assert.assertNull(as4cap);
	}
	
	@Test
	public void test2OctetASNumberAS4Cap() throws Exception {
		PeerConfiguration peer = new AS4OctetPeerConfigurationDecorator(new PeerConfigurationImpl("peer1", // peer name
				new ClientConfigurationImpl(InetAddress.getLocalHost(), 17179),  // client config
				10, // local AS
				11, // remote AS
				1024, // local BGP identifier
				2048, // remote identifier
				15, // connect retry time
				15, // hold time
				false, // hold timer disabled
				30, // idle hold time
				true, // allow automatic start
				true, // allow automatic stop
				60,  // automatic start interval
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				0, // delay open time
				false, // collisition established detection
				new CapabilitiesImpl(new Capability[] { new AutonomousSystem4Capability(10241024)}))); // capabilities
		
		Assert.assertEquals(10, peer.getLocalAS());
		Assert.assertEquals(11, peer.getRemoteAS());
		
		AutonomousSystem4Capability as4cap = findRequiredCapability(peer.getCapabilities(), AutonomousSystem4Capability.class);
		
		Assert.assertNotNull(as4cap);
		Assert.assertEquals(10241024L, as4cap.getAutonomousSystem());
	}
	
	@Test
	public void test4OctetASNumber() throws Exception {
		PeerConfiguration peer = new AS4OctetPeerConfigurationDecorator(new PeerConfigurationImpl("peer1", // peer name
				new ClientConfigurationImpl(InetAddress.getLocalHost(), 17179),  // client config
				131072, // local AS
				11, // remote AS
				1024, // local BGP identifier
				2048, // remote identifier
				15, // connect retry time
				15, // hold time
				false, // hold timer disabled
				30, // idle hold time
				true, // allow automatic start
				true, // allow automatic stop
				60,  // automatic start interval
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				0, // delay open time
				false, // collisition established detection
				null)); // capabilities
		
		Assert.assertEquals(131072, peer.getLocalAS());
		Assert.assertEquals(11, peer.getRemoteAS());
		
		AutonomousSystem4Capability as4cap = findRequiredCapability(peer.getCapabilities(), AutonomousSystem4Capability.class);
		
		Assert.assertNotNull(as4cap);
		Assert.assertEquals(131072, as4cap.getAutonomousSystem());
	}
	
	@Test
	public void test4OctetASNumberAS4Cap() throws Exception {
		PeerConfiguration peer = new AS4OctetPeerConfigurationDecorator(new PeerConfigurationImpl("peer1", // peer name
				new ClientConfigurationImpl(InetAddress.getLocalHost(), 17179),  // client config
				131072, // local AS
				11, // remote AS
				1024, // local BGP identifier
				2048, // remote identifier
				15, // connect retry time
				15, // hold time
				false, // hold timer disabled
				30, // idle hold time
				true, // allow automatic start
				true, // allow automatic stop
				60,  // automatic start interval
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				0, // delay open time
				false, // collisition established detection
				new CapabilitiesImpl(new Capability[] { new AutonomousSystem4Capability(262144)}))); // capabilities
		
		Assert.assertEquals(131072, peer.getLocalAS());
		Assert.assertEquals(11, peer.getRemoteAS());
		
		AutonomousSystem4Capability as4cap = findRequiredCapability(peer.getCapabilities(), AutonomousSystem4Capability.class);
		
		Assert.assertNotNull(as4cap);
		Assert.assertEquals(262144, as4cap.getAutonomousSystem());
	}
	
	@Test
	public void test2OctetASNumberSame() throws Exception {
		PeerConfiguration cd1 = new PeerConfigurationImpl("peer1", // peer name
				new ClientConfigurationImpl(InetAddress.getLocalHost(), 17179),  // client config
				10, // local AS
				11, // remote AS
				1024, // local BGP identifier
				2048, // remote identifier
				15, // connect retry time
				15, // hold time
				false, // hold timer disabled
				30, // idle hold time
				true, // allow automatic start
				true, // allow automatic stop
				60,  // automatic start interval
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				0, // delay open time
				false, // collisition established detection
				null);
		PeerConfiguration cp1 = new AS4OctetPeerConfigurationDecorator(cd1); // capabilities
		
		Assert.assertTrue(cd1.hashCode() == cp1.hashCode());
		Assert.assertTrue(cp1.equals(cd1)); 
		Assert.assertTrue(cd1.equals(cp1)); 
	}
	
	
	@Test
	public void test2OctetASNumberAS4CapSame() throws Exception {
		PeerConfiguration cd1 = new PeerConfigurationImpl("peer1", // peer name
				new ClientConfigurationImpl(InetAddress.getLocalHost(), 17179),  // client config
				10, // local AS
				11, // remote AS
				1024, // local BGP identifier
				2048, // remote identifier
				15, // connect retry time
				15, // hold time
				false, // hold timer disabled
				30, // idle hold time
				true, // allow automatic start
				true, // allow automatic stop
				60,  // automatic start interval
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				0, // delay open time
				false, // collisition established detection
				new CapabilitiesImpl(new Capability[] { new AutonomousSystem4Capability(10241024)}));
		PeerConfiguration cp1 = new AS4OctetPeerConfigurationDecorator(cd1); // capabilities
		
		Assert.assertTrue(cd1.hashCode() == cp1.hashCode());
		Assert.assertTrue(cp1.equals(cd1)); 
		Assert.assertTrue(cd1.equals(cp1)); 
	}

	private <T extends Capability> T findRequiredCapability(Capabilities caps, Class<T> capClass) {
		return findCapability(caps.getRequiredCapabilities(), capClass);
	}
	
	@SuppressWarnings("unchecked")
	private <T extends Capability> T findCapability(Set<Capability> caps, Class<T> capClass) {
		T result = null;
		
		for(Capability cap : caps) {
			if(cap.getClass().equals(capClass)) {
				result = (T)cap;
				break;
			}
		}
		return result;
	}

}
