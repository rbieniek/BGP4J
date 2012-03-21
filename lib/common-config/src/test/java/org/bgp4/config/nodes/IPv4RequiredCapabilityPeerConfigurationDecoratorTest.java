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

import junit.framework.Assert;

import org.bgp4.config.nodes.impl.CapabilitiesImpl;
import org.bgp4.config.nodes.impl.ClientConfigurationImpl;
import org.bgp4.config.nodes.impl.PeerConfigurationImpl;
import org.bgp4j.net.AddressFamily;
import org.bgp4j.net.SubsequentAddressFamily;
import org.bgp4j.net.capabilities.Capability;
import org.bgp4j.net.capabilities.MultiProtocolCapability;
import org.junit.Test;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class IPv4RequiredCapabilityPeerConfigurationDecoratorTest {
	@Test
	public void testNoCapabiltiesSet() throws Exception {
		PeerConfiguration peer = new IPv4RequiredCapabilityPeerConfigurationDecorator(new PeerConfigurationImpl("peer1", // peer name
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
		
		MultiProtocolCapability ipv4Cap = new MultiProtocolCapability(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING);
		
		Assert.assertTrue(peer.getCapabilities().getRequiredCapabilities().contains(ipv4Cap));
		Assert.assertFalse(peer.getCapabilities().getOptionalCapabilities().contains(ipv4Cap));
	}

	@Test
	public void testIPv4OptionalCapabiltiesSet() throws Exception {
		PeerConfiguration peer = new IPv4RequiredCapabilityPeerConfigurationDecorator(new PeerConfigurationImpl("peer1", // peer name
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
				new CapabilitiesImpl(null, new Capability[] {
						new MultiProtocolCapability(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING),
						new MultiProtocolCapability(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING)
				}))); // capabilities
		
		Assert.assertEquals(10, peer.getLocalAS());
		Assert.assertEquals(11, peer.getRemoteAS());
		
		MultiProtocolCapability ipv4Cap = new MultiProtocolCapability(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING);
		MultiProtocolCapability ipv6Cap = new MultiProtocolCapability(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING);
		
		Assert.assertTrue(peer.getCapabilities().getRequiredCapabilities().contains(ipv4Cap));
		Assert.assertFalse(peer.getCapabilities().getOptionalCapabilities().contains(ipv4Cap));
		Assert.assertFalse(peer.getCapabilities().getRequiredCapabilities().contains(ipv6Cap));
		Assert.assertTrue(peer.getCapabilities().getOptionalCapabilities().contains(ipv6Cap));
	}


	@Test
	public void testIPv4AnycastOptionalCapabiltiesSet() throws Exception {
		PeerConfiguration peer = new IPv4RequiredCapabilityPeerConfigurationDecorator(new PeerConfigurationImpl("peer1", // peer name
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
				new CapabilitiesImpl(null, new Capability[] {
						new MultiProtocolCapability(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_MULTICAST_FORWARDING),
						new MultiProtocolCapability(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING)
				}))); // capabilities
		
		Assert.assertEquals(10, peer.getLocalAS());
		Assert.assertEquals(11, peer.getRemoteAS());
		
		MultiProtocolCapability ipv4UnicastCap = new MultiProtocolCapability(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING);
		MultiProtocolCapability ipv4MulticastCap = new MultiProtocolCapability(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_MULTICAST_FORWARDING);
		MultiProtocolCapability ipv6Cap = new MultiProtocolCapability(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING);
		
		Assert.assertTrue(peer.getCapabilities().getRequiredCapabilities().contains(ipv4UnicastCap));
		Assert.assertFalse(peer.getCapabilities().getOptionalCapabilities().contains(ipv4UnicastCap));
		Assert.assertFalse(peer.getCapabilities().getRequiredCapabilities().contains(ipv6Cap));
		Assert.assertTrue(peer.getCapabilities().getOptionalCapabilities().contains(ipv6Cap));
		Assert.assertFalse(peer.getCapabilities().getRequiredCapabilities().contains(ipv4MulticastCap));
		Assert.assertTrue(peer.getCapabilities().getOptionalCapabilities().contains(ipv4MulticastCap));
	}

	@Test
	public void testIPv4RequiredCapabiltiesSet() throws Exception {
		PeerConfiguration peer = new IPv4RequiredCapabilityPeerConfigurationDecorator(new PeerConfigurationImpl("peer1", // peer name
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
				new CapabilitiesImpl(new Capability[] {
						new MultiProtocolCapability(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING)
				}))); // capabilities
		
		Assert.assertEquals(10, peer.getLocalAS());
		Assert.assertEquals(11, peer.getRemoteAS());
		
		MultiProtocolCapability ipv4Cap = new MultiProtocolCapability(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING);
		
		Assert.assertTrue(peer.getCapabilities().getRequiredCapabilities().contains(ipv4Cap));
		Assert.assertFalse(peer.getCapabilities().getOptionalCapabilities().contains(ipv4Cap));
	}

	@Test
	public void testIPv4AnycastRequiredCapabiltiesSet() throws Exception {
		PeerConfiguration peer = new IPv4RequiredCapabilityPeerConfigurationDecorator(new PeerConfigurationImpl("peer1", // peer name
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
				new CapabilitiesImpl(new Capability[] {
						new MultiProtocolCapability(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_MULTICAST_FORWARDING)
				}))); // capabilities
		
		Assert.assertEquals(10, peer.getLocalAS());
		Assert.assertEquals(11, peer.getRemoteAS());
		
		MultiProtocolCapability ipv4UnicastCap = new MultiProtocolCapability(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING);
		MultiProtocolCapability ipv4MulticastCap = new MultiProtocolCapability(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_MULTICAST_FORWARDING);
		
		Assert.assertTrue(peer.getCapabilities().getRequiredCapabilities().contains(ipv4UnicastCap));
		Assert.assertFalse(peer.getCapabilities().getOptionalCapabilities().contains(ipv4UnicastCap));
		Assert.assertTrue(peer.getCapabilities().getRequiredCapabilities().contains(ipv4MulticastCap));
		Assert.assertFalse(peer.getCapabilities().getOptionalCapabilities().contains(ipv4MulticastCap));
	}
}
