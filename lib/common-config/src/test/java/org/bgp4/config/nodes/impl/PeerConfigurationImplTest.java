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
 * File: org.bgp4.config.nodes.impl.PeerConfigurationImplTest.java 
 */
package org.bgp4.config.nodes.impl;

import java.net.InetAddress;

import junit.framework.Assert;

import org.apache.commons.configuration.ConfigurationException;
import org.bgp4.config.nodes.PeerConfiguration;
import org.junit.Test;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class PeerConfigurationImplTest {

	@Test
	public void testAcceptedPeerConfiguration() throws Exception {
		PeerConfiguration config =  new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				60, // connectRetryTime
				300, // hold time
				true, // hold timer disabled
				30, // idle hold time
				true, // allow automatic start
				true, // allow automatic stop
				120, // automatic start interval
				true, // damp peer oscillation
				true, // passive tcp establishment
				true, // delay open
				180, // delay open time
				true); // collision detect established state
		
		Assert.assertEquals(24576, config.getLocalAS());
		Assert.assertEquals(32768, config.getRemoteAS());
		Assert.assertEquals(InetAddress.getByName("192.168.4.1"), config.getClientConfig().getRemoteAddress().getAddress());
		Assert.assertEquals(179, config.getClientConfig().getRemoteAddress().getPort());
		Assert.assertEquals(0xc0a80401L, config.getLocalBgpIdentifier());
		Assert.assertEquals(0xc0a80501L, config.getRemoteBgpIdentifier());
		Assert.assertEquals(60, config.getConnectRetryTime());
		Assert.assertEquals(300, config.getHoldTime());
		Assert.assertTrue(config.isHoldTimerDisabled());
		Assert.assertEquals(30, config.getIdleHoldTime());
		Assert.assertEquals(120, config.getAutomaticStartInterval());
		Assert.assertTrue(config.isAllowAutomaticStart());
		Assert.assertTrue(config.isAllowAutomaticStop());
		Assert.assertTrue(config.isDampPeerOscillation());
		Assert.assertTrue(config.isPassiveTcpEstablishment());
		Assert.assertTrue(config.isDelayOpen());
		Assert.assertEquals(180, config.getDelayOpenTime());
		Assert.assertTrue(config.isCollisionDetectEstablishedState());
	}

	@Test(expected=ConfigurationException.class)
	public void testBogusPeerConfigurationNullClientConfiguration() throws Exception {
		@SuppressWarnings("unused")
		PeerConfiguration config = new PeerConfigurationImpl("foo", null, 10, 11);		
	}

	@Test(expected=ConfigurationException.class)
	public void testBogusPeerConfigurationNegativeLocalAS() throws Exception {
		@SuppressWarnings("unused")
		PeerConfiguration config = new PeerConfigurationImpl("foo", new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), -10, 11);		
	}

	@Test(expected=ConfigurationException.class)
	public void testBogusPeerConfigurationNegativeRemoteAS() throws Exception {
		@SuppressWarnings("unused")
		PeerConfiguration config = new PeerConfigurationImpl("foo", new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), 10, -11);		
	}

	@Test(expected=ConfigurationException.class)
	public void testBogusPeerConfigurationNegativeDelayOpenTime() throws Exception {
		@SuppressWarnings("unused")
		PeerConfiguration config =  new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				60, // connectRetryTime
				300, // hold time
				false, // hold timer disabled
				30, // idle hold time
				true, // allow automatic start
				true, // allow automatic stop
				120, // automatic start interval
				true, // damp peer oscillation
				true, // passive tcp establishment
				true, // delay open
				-180, // delay open time
				true); // collision detect established state		
	}

	@Test(expected=ConfigurationException.class)
	public void testBogusPeerConfigurationNegativeHoldTime() throws Exception {
		@SuppressWarnings("unused")
		PeerConfiguration config =  new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				60, // connectRetryTime
				-300, // hold time
				false, // hold timer disabled
				30, // idle hold time
				true, // allow automatic start
				true, // allow automatic stop
				120, // automatic start interval
				true, // damp peer oscillation
				true, // passive tcp establishment
				true, // delay open
				180, // delay open time
				true); // collision detect established state		
	}

	@Test(expected=ConfigurationException.class)
	public void testBogusPeerConfigurationNegativeIdleHoldTime() throws Exception {
		@SuppressWarnings("unused")
		PeerConfiguration config =  new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				60, // connectRetryTime
				300, // hold time
				false, // hold timer disabled
				-30, // idle hold time
				true, // allow automatic start
				true, // allow automatic stop
				120, // automatic start interval
				true, // damp peer oscillation
				true, // passive tcp establishment
				true, // delay open
				180, // delay open time
				true); // collision detect established state		
	}

	@Test(expected=ConfigurationException.class)
	public void testBogusPeerConfigurationEmptyName() throws Exception {
		@SuppressWarnings("unused")
		PeerConfiguration config = new PeerConfigurationImpl("", new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), 10, 11);		
	}
	
	@Test
	public void testEquals() throws Exception {
		PeerConfiguration c1 = new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				60, // connectRetryTime
				300, // hold time
				false, // hold timer disabled
				30, // idle hold time
				false, // allow automatic start
				false, // allow automatic stop
				0, // automatic start interval
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				180, // delay open time
				false); // connect retry interval
		PeerConfiguration c2 = new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				60, // connectRetryTime
				300, // hold time
				false, // hold timer disabled
				30, // idle hold time
				false, // allow automatic start
				false, // allow automatic stop
				0, // automatic start interval
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				180, // delay open time
				false); // connect retry interval
		PeerConfiguration c3 = new PeerConfigurationImpl("bar", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				60, // connectRetryTime
				300, // hold time
				false, // hold timer disabled
				30, // idle hold time
				false, // allow automatic start
				false, // allow automatic stop
				0, // automatic start interval
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				180, // delay open time
				false); // connect retry interval
		PeerConfiguration c4 = new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.5.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				60, // connectRetryTime
				300, // hold time
				false, // hold timer disabled
				30, // idle hold time
				false, // allow automatic start
				false, // allow automatic stop
				0, // automatic start interval
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				180, // delay open time
				false); // connect retry interval
		PeerConfiguration c5 = new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24577, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				60, // connectRetryTime
				300, // hold time
				false, // hold timer disabled
				30, // idle hold time
				false, // allow automatic start
				false, // allow automatic stop
				0, // automatic start interval
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				180, // delay open time
				false); // connect retry interval
		PeerConfiguration c6 = new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32769, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				60, // connectRetryTime
				300, // hold time
				false, // hold timer disabled
				30, // idle hold time
				false, // allow automatic start
				false, // allow automatic stop
				0, // automatic start interval
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				180, // delay open time
				false); // connect retry interval
		PeerConfiguration c7 = new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80402L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				60, // connectRetryTime
				300, // hold time
				false, // hold timer disabled
				30, // idle hold time
				false, // allow automatic start
				false, // allow automatic stop
				0, // automatic start interval
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				180, // delay open time
				false); // connect retry interval
		PeerConfiguration c8 = new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80502L, // remote BGP identifier
				60, // connectRetryTime
				300, // hold time
				false, // hold timer disabled
				30, // idle hold time
				false, // allow automatic start
				false, // allow automatic stop
				0, // automatic start interval
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				180, // delay open time
				false); // connect retry interval
		PeerConfiguration c9 = new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				60, // connectRetryTime
				301, // hold time
				false, // hold timer disabled
				30, // idle hold time
				false, // allow automatic start
				false, // allow automatic stop
				0, // automatic start interval
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				180, // delay open time
				false); // connect retry interval
		PeerConfiguration c10 = new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				60, // connectRetryTime
				300, // hold time
				true, // hold timer disabled
				30, // idle hold time
				false, // allow automatic start
				false, // allow automatic stop
				0, // automatic start interval
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				180, // delay open time
				false); // connect retry interval
		PeerConfiguration c11 = new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				60, // connectRetryTime
				300, // hold time
				false, // hold timer disabled
				31, // idle hold time
				false, // allow automatic start
				false, // allow automatic stop
				0, // automatic start interval
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				180, // delay open time
				false); // connect retry interval
		PeerConfiguration c12 = new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				60, // connectRetryTime
				300, // hold time
				false, // hold timer disabled
				30, // idle hold time
				true, // allow automatic start
				false, // allow automatic stop
				0, // automatic start interval
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				180, // delay open time
				false); // connect retry interval
		PeerConfiguration c13 = new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				60, // connectRetryTime
				300, // hold time
				false, // hold timer disabled
				30, // idle hold time
				false, // allow automatic start
				true, // allow automatic stop
				0, // automatic start interval
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				180, // delay open time
				false); // connect retry interval
		PeerConfiguration c14 = new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				60, // connectRetryTime
				300, // hold time
				false, // hold timer disabled
				30, // idle hold time
				false, // allow automatic start
				false, // allow automatic stop
				10, // automatic start interval
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				180, // delay open time
				false); // connect retry interval
		PeerConfiguration c15 = new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				60, // connectRetryTime
				300, // hold time
				false, // hold timer disabled
				30, // idle hold time
				false, // allow automatic start
				false, // allow automatic stop
				0, // automatic start interval
				true, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				180, // delay open time
				false); // connect retry interval
		PeerConfiguration c16 = new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				60, // connectRetryTime
				300, // hold time
				false, // hold timer disabled
				30, // idle hold time
				false, // allow automatic start
				false, // allow automatic stop
				0, // automatic start interval
				false, // damp peer oscillation
				true, // passive tcp establishment
				false, // delay open
				180, // delay open time
				false); // connect retry interval
		PeerConfiguration c17 = new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				60, // connectRetryTime
				300, // hold time
				false, // hold timer disabled
				30, // idle hold time
				false, // allow automatic start
				false, // allow automatic stop
				0, // automatic start interval
				false, // damp peer oscillation
				false, // passive tcp establishment
				true, // delay open
				180, // delay open time
				false); // connect retry interval
		PeerConfiguration c18 = new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				60, // connectRetryTime
				300, // hold time
				false, // hold timer disabled
				30, // idle hold time
				false, // allow automatic start
				false, // allow automatic stop
				0, // automatic start interval
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				240, // delay open time
				false); // connect retry interval
		PeerConfiguration c19 = new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				60, // connectRetryTime
				300, // hold time
				false, // hold timer disabled
				30, // idle hold time
				false, // allow automatic start
				false, // allow automatic stop
				0, // automatic start interval
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				180, // delay open time
				true); // connect retry interval
		PeerConfiguration c20 = new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				61, // connectRetryTime
				300, // hold time
				false, // hold timer disabled
				30, // idle hold time
				false, // allow automatic start
				false, // allow automatic stop
				0, // automatic start interval
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				180, // delay open time
				true); // connect retry interval

		Assert.assertTrue(c1.equals(c2));
		Assert.assertFalse(c1.equals(c3));
		Assert.assertFalse(c1.equals(c4));
		Assert.assertFalse(c1.equals(c5));
		Assert.assertFalse(c1.equals(c6));
		Assert.assertFalse(c1.equals(c7));
		Assert.assertFalse(c1.equals(c8));
		Assert.assertFalse(c1.equals(c9));
		Assert.assertFalse(c1.equals(c10));
		Assert.assertFalse(c1.equals(c11));
		Assert.assertFalse(c1.equals(c12));
		Assert.assertFalse(c1.equals(c13));
		Assert.assertFalse(c1.equals(c14));
		Assert.assertFalse(c1.equals(c15));
		Assert.assertFalse(c1.equals(c16));
		Assert.assertFalse(c1.equals(c17));
		Assert.assertFalse(c1.equals(c18));
		Assert.assertFalse(c1.equals(c19));
		Assert.assertFalse(c1.equals(c20));
	}

	@Test
	public void testHashCode() throws Exception {
		PeerConfiguration c1 = new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				60, // connectRetryTime
				300, // hold time
				false, // hold timer disabled
				30, // idle hold time
				false, // allow automatic start
				false, // allow automatic stop
				0, // automatic start interval
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				180, // delay open time
				false); // connect retry interval
		PeerConfiguration c2 = new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				60, // connectRetryTime
				300, // hold time
				false, // hold timer disabled
				30, // idle hold time
				false, // allow automatic start
				false, // allow automatic stop
				0, // automatic start interval
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				180, // delay open time
				false); // connect retry interval
		PeerConfiguration c3 = new PeerConfigurationImpl("bar", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				60, // connectRetryTime
				300, // hold time
				false, // hold timer disabled
				30, // idle hold time
				false, // allow automatic start
				false, // allow automatic stop
				0, // automatic start interval
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				180, // delay open time
				false); // connect retry interval
		PeerConfiguration c4 = new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.5.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				60, // connectRetryTime
				300, // hold time
				false, // hold timer disabled
				30, // idle hold time
				false, // allow automatic start
				false, // allow automatic stop
				0, // automatic start interval
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				180, // delay open time
				false); // connect retry interval
		PeerConfiguration c5 = new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24577, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				60, // connectRetryTime
				300, // hold time
				false, // hold timer disabled
				30, // idle hold time
				false, // allow automatic start
				false, // allow automatic stop
				0, // automatic start interval
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				180, // delay open time
				false); // connect retry interval
		PeerConfiguration c6 = new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32769, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				60, // connectRetryTime
				300, // hold time
				false, // hold timer disabled
				30, // idle hold time
				false, // allow automatic start
				false, // allow automatic stop
				0, // automatic start interval
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				180, // delay open time
				false); // connect retry interval
		PeerConfiguration c7 = new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80402L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				60, // connectRetryTime
				300, // hold time
				false, // hold timer disabled
				30, // idle hold time
				false, // allow automatic start
				false, // allow automatic stop
				0, // automatic start interval
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				180, // delay open time
				false); // connect retry interval
		PeerConfiguration c8 = new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80502L, // remote BGP identifier
				60, // connectRetryTime
				300, // hold time
				false, // hold timer disabled
				30, // idle hold time
				false, // allow automatic start
				false, // allow automatic stop
				0, // automatic start interval
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				180, // delay open time
				false); // connect retry interval
		PeerConfiguration c9 = new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				60, // connectRetryTime
				301, // hold time
				false, // hold timer disabled
				30, // idle hold time
				false, // allow automatic start
				false, // allow automatic stop
				0, // automatic start interval
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				180, // delay open time
				false); // connect retry interval
		PeerConfiguration c10 = new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				60, // connectRetryTime
				300, // hold time
				true, // hold timer disabled
				30, // idle hold time
				false, // allow automatic start
				false, // allow automatic stop
				0, // automatic start interval
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				180, // delay open time
				false); // connect retry interval
		PeerConfiguration c11 = new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				60, // connectRetryTime
				300, // hold time
				false, // hold timer disabled
				31, // idle hold time
				false, // allow automatic start
				false, // allow automatic stop
				0, // automatic start interval
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				180, // delay open time
				false); // connect retry interval
		PeerConfiguration c12 = new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				60, // connectRetryTime
				300, // hold time
				false, // hold timer disabled
				30, // idle hold time
				true, // allow automatic start
				false, // allow automatic stop
				0, // automatic start interval
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				180, // delay open time
				false); // connect retry interval
		PeerConfiguration c13 = new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				60, // connectRetryTime
				300, // hold time
				false, // hold timer disabled
				30, // idle hold time
				false, // allow automatic start
				true, // allow automatic stop
				0, // automatic start interval
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				180, // delay open time
				false); // connect retry interval
		PeerConfiguration c14 = new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				60, // connectRetryTime
				300, // hold time
				false, // hold timer disabled
				30, // idle hold time
				false, // allow automatic start
				false, // allow automatic stop
				10, // automatic start interval
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				180, // delay open time
				false); // connect retry interval
		PeerConfiguration c15 = new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				60, // connectRetryTime
				300, // hold time
				false, // hold timer disabled
				30, // idle hold time
				false, // allow automatic start
				false, // allow automatic stop
				0, // automatic start interval
				true, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				180, // delay open time
				false); // connect retry interval
		PeerConfiguration c16 = new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				60, // connectRetryTime
				300, // hold time
				false, // hold timer disabled
				30, // idle hold time
				false, // allow automatic start
				false, // allow automatic stop
				0, // automatic start interval
				false, // damp peer oscillation
				true, // passive tcp establishment
				false, // delay open
				180, // delay open time
				false); // connect retry interval
		PeerConfiguration c17 = new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				60, // connectRetryTime
				300, // hold time
				false, // hold timer disabled
				30, // idle hold time
				false, // allow automatic start
				false, // allow automatic stop
				0, // automatic start interval
				false, // damp peer oscillation
				false, // passive tcp establishment
				true, // delay open
				180, // delay open time
				false); // connect retry interval
		PeerConfiguration c18 = new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				60, // connectRetryTime
				300, // hold time
				false, // hold timer disabled
				30, // idle hold time
				false, // allow automatic start
				false, // allow automatic stop
				0, // automatic start interval
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				240, // delay open time
				false); // connect retry interval
		PeerConfiguration c19 = new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				60, // connectRetryTime
				300, // hold time
				false, // hold timer disabled
				30, // idle hold time
				false, // allow automatic start
				false, // allow automatic stop
				0, // automatic start interval
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				180, // delay open time
				true); // connect retry interval
		PeerConfiguration c20 = new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				61, // connectRetryTime
				300, // hold time
				false, // hold timer disabled
				30, // idle hold time
				false, // allow automatic start
				false, // allow automatic stop
				0, // automatic start interval
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				180, // delay open time
				true); // connect retry interval
		
		Assert.assertEquals(c1.hashCode(), c2.hashCode());
		Assert.assertFalse(c1.hashCode() == c3.hashCode());
		Assert.assertFalse(c1.hashCode() == c4.hashCode());
		Assert.assertFalse(c1.hashCode() == c5.hashCode());
		Assert.assertFalse(c1.hashCode() == c6.hashCode());
		Assert.assertFalse(c1.hashCode() == c7.hashCode());
		Assert.assertFalse(c1.hashCode() == c8.hashCode());
		Assert.assertFalse(c1.hashCode() == c9.hashCode());
		Assert.assertFalse(c1.hashCode() == c10.hashCode());
		Assert.assertFalse(c1.hashCode() == c11.hashCode());
		Assert.assertFalse(c1.hashCode() == c12.hashCode());
		Assert.assertFalse(c1.hashCode() == c13.hashCode());
		Assert.assertFalse(c1.hashCode() == c14.hashCode());
		Assert.assertFalse(c1.hashCode() == c15.hashCode());
		Assert.assertFalse(c1.hashCode() == c16.hashCode());
		Assert.assertFalse(c1.hashCode() == c17.hashCode());
		Assert.assertFalse(c1.hashCode() == c18.hashCode());
		Assert.assertFalse(c1.hashCode() == c19.hashCode());
		Assert.assertFalse(c1.hashCode() == c20.hashCode());
	}
}
