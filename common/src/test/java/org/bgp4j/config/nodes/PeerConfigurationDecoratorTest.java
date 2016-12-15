package org.bgp4j.config.nodes;

import java.net.InetAddress;

import junit.framework.Assert;

import org.bgp4j.config.nodes.PeerConfiguration;
import org.bgp4j.config.nodes.PeerConfigurationDecorator;
import org.bgp4j.config.nodes.impl.ClientConfigurationImpl;
import org.bgp4j.config.nodes.impl.PeerConfigurationImpl;
import org.junit.Test;

public class PeerConfigurationDecoratorTest {
	public static class MockPeerConfgurationDecorator extends PeerConfigurationDecorator {
		MockPeerConfgurationDecorator(PeerConfiguration decorated) {
			super(decorated);
		}
	}
	
	@Test
	public void testEquals() throws Exception {
		PeerConfiguration cd1 = new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				60, // connect retry time
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
				false);
		PeerConfiguration cp1 = new MockPeerConfgurationDecorator(cd1); // connect retry interval
		PeerConfiguration cd2 = new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				60, // connect retry time
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
				false);
		PeerConfiguration cp2 = new MockPeerConfgurationDecorator(cd2); // connect retry interval
		
		PeerConfiguration cd3 = new PeerConfigurationImpl("bar", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				60, // connect retry time
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
				false);
		PeerConfiguration cp3 = new MockPeerConfgurationDecorator(cd3); // connect retry interval
		PeerConfiguration cd4 = new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.5.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				60, // connect retry time
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
				false);
		PeerConfiguration cp4 = new MockPeerConfgurationDecorator(cd4); // connect retry interval
		PeerConfiguration cd5 = new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24577, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				60, // connect retry time
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
				false);
		PeerConfiguration cp5 = new MockPeerConfgurationDecorator(cd5); // connect retry interval
		PeerConfiguration cd6 = new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32769, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				60, // connect retry time
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
				false);
		PeerConfiguration cp6 = new MockPeerConfgurationDecorator(cd6); // connect retry interval
		PeerConfiguration cd7 = new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80402L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				60, // connect retry time
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
				false);
		PeerConfiguration cp7 = new MockPeerConfgurationDecorator(cd7); // connect retry interval
		PeerConfiguration cd8 = new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80502L, // remote BGP identifier
				60, // connect retry time
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
				false);
		PeerConfiguration cp8 = new MockPeerConfgurationDecorator(cd8); // connect retry interval
		PeerConfiguration cd9 = new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				61, // connect retry time
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
				false);
		PeerConfiguration cp9 = new MockPeerConfgurationDecorator(cd9); // connect retry interval
		PeerConfiguration cd10 = new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				60, // connect retry time
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
				false);
		PeerConfiguration cp10 = new MockPeerConfgurationDecorator(cd10); // connect retry interval
		PeerConfiguration cd11 = new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				60, // connect retry time
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
				false);
		PeerConfiguration cp11 = new MockPeerConfgurationDecorator(cd11); // connect retry interval
		PeerConfiguration cd12 = new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				60, // connect retry time
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
				false);
		PeerConfiguration cp12 = new MockPeerConfgurationDecorator(cd12); // connect retry interval
		PeerConfiguration cd13 = new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				60, // connect retry time
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
				false);
		PeerConfiguration cp13 = new MockPeerConfgurationDecorator(cd13); // connect retry interval
		PeerConfiguration cd14 = new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				60, // connect retry time
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
				false);
		PeerConfiguration cp14 = new MockPeerConfgurationDecorator(cd14); // connect retry interval
		PeerConfiguration cd15 = new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				60, // connect retry time
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
				false);
		PeerConfiguration cp15 = new MockPeerConfgurationDecorator(cd15); // connect retry interval
		PeerConfiguration cd16 = new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				60, // connect retry time
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
				false);
		PeerConfiguration cp16 = new MockPeerConfgurationDecorator(cd16); // connect retry interval
		PeerConfiguration cd17 = new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				60, // connect retry time
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
				false);
		PeerConfiguration cp17 = new MockPeerConfgurationDecorator(cd17); // connect retry interval
		PeerConfiguration cd18 = new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				60, // connect retry time
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
				false);
		PeerConfiguration cp18 = new MockPeerConfgurationDecorator(cd18); // connect retry interval
		PeerConfiguration cd19 = new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				60, // connect retry time
				300, // hold time
				false, // hold timer disabled
				30, // idle hold time
				false, // allow automatic start
				false, // allow automatic stop
				0, // automatic start interval
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				181, // delay open time
				false);
		PeerConfiguration cp19 = new MockPeerConfgurationDecorator(cd19); // connect retry interval
		PeerConfiguration cd20 = new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				60, // connect retry time
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
				true);
		PeerConfiguration cp20 = new MockPeerConfgurationDecorator(cd20); // connect retry interval

		Assert.assertTrue(cp1.equals(cp2));
		Assert.assertFalse(cp1.equals(cp3));
		Assert.assertFalse(cp1.equals(cp4));
		Assert.assertFalse(cp1.equals(cp5));
		Assert.assertFalse(cp1.equals(cp6));
		Assert.assertFalse(cp1.equals(cp7));
		Assert.assertFalse(cp1.equals(cp8));
		Assert.assertFalse(cp1.equals(cp9));
		Assert.assertFalse(cp1.equals(cp10));
		Assert.assertFalse(cp1.equals(cp11));
		Assert.assertFalse(cp1.equals(cp12));
		Assert.assertFalse(cp1.equals(cp13));
		Assert.assertFalse(cp1.equals(cp14));
		Assert.assertFalse(cp1.equals(cp15));
		Assert.assertFalse(cp1.equals(cp16));
		Assert.assertFalse(cp1.equals(cp17));
		Assert.assertFalse(cp1.equals(cp18));
		Assert.assertFalse(cp1.equals(cp19));
		Assert.assertFalse(cp1.equals(cp20));
		
		Assert.assertTrue(cp1.equals(cd1));
		Assert.assertTrue(cp2.equals(cd2));
		Assert.assertTrue(cp3.equals(cd3));
		Assert.assertTrue(cp4.equals(cd4));
		Assert.assertTrue(cp5.equals(cd5));
		Assert.assertTrue(cp6.equals(cd6));
		Assert.assertTrue(cp7.equals(cd7));
		Assert.assertTrue(cp8.equals(cd8));
		Assert.assertTrue(cp9.equals(cd9));
		Assert.assertTrue(cp10.equals(cd10));
		Assert.assertTrue(cp11.equals(cd11));
		Assert.assertTrue(cp12.equals(cd12));
		Assert.assertTrue(cp13.equals(cd13));
		Assert.assertTrue(cp14.equals(cd14));
		Assert.assertTrue(cp15.equals(cd15));
		Assert.assertTrue(cp16.equals(cd16));
		Assert.assertTrue(cp17.equals(cd17));
		Assert.assertTrue(cp18.equals(cd18));
		Assert.assertTrue(cp19.equals(cd19));
		Assert.assertTrue(cp20.equals(cd20));
	}

	@Test
	public void testHashCode() throws Exception {
		PeerConfiguration cd1 = new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				60, // connect retry time
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
				false);
		PeerConfiguration cp1 = new MockPeerConfgurationDecorator(cd1); // connect retry interval
		PeerConfiguration cd2 = new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				60, // connect retry time
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
				false);
		PeerConfiguration cp2 = new MockPeerConfgurationDecorator(cd2); // connect retry interval
		
		PeerConfiguration cd3 = new PeerConfigurationImpl("bar", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				60, // connect retry time
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
				false);
		PeerConfiguration cp3 = new MockPeerConfgurationDecorator(cd3); // connect retry interval
		PeerConfiguration cd4 = new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.5.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				60, // connect retry time
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
				false);
		PeerConfiguration cp4 = new MockPeerConfgurationDecorator(cd4); // connect retry interval
		PeerConfiguration cd5 = new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24577, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				60, // connect retry time
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
				false);
		PeerConfiguration cp5 = new MockPeerConfgurationDecorator(cd5); // connect retry interval
		PeerConfiguration cd6 = new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32769, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				60, // connect retry time
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
				false);
		PeerConfiguration cp6 = new MockPeerConfgurationDecorator(cd6); // connect retry interval
		PeerConfiguration cd7 = new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80402L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				60, // connect retry time
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
				false);
		PeerConfiguration cp7 = new MockPeerConfgurationDecorator(cd7); // connect retry interval
		PeerConfiguration cd8 = new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80502L, // remote BGP identifier
				60, // connect retry time
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
				false);
		PeerConfiguration cp8 = new MockPeerConfgurationDecorator(cd8); // connect retry interval
		PeerConfiguration cd9 = new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				61, // connect retry time
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
				false);
		PeerConfiguration cp9 = new MockPeerConfgurationDecorator(cd9); // connect retry interval
		PeerConfiguration cd10 = new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				60, // connect retry time
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
				false);
		PeerConfiguration cp10 = new MockPeerConfgurationDecorator(cd10); // connect retry interval
		PeerConfiguration cd11 = new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				60, // connect retry time
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
				false);
		PeerConfiguration cp11 = new MockPeerConfgurationDecorator(cd11); // connect retry interval
		PeerConfiguration cd12 = new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				60, // connect retry time
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
				false);
		PeerConfiguration cp12 = new MockPeerConfgurationDecorator(cd12); // connect retry interval
		PeerConfiguration cd13 = new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				60, // connect retry time
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
				false);
		PeerConfiguration cp13 = new MockPeerConfgurationDecorator(cd13); // connect retry interval
		PeerConfiguration cd14 = new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				60, // connect retry time
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
				false);
		PeerConfiguration cp14 = new MockPeerConfgurationDecorator(cd14); // connect retry interval
		PeerConfiguration cd15 = new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				60, // connect retry time
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
				false);
		PeerConfiguration cp15 = new MockPeerConfgurationDecorator(cd15); // connect retry interval
		PeerConfiguration cd16 = new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				60, // connect retry time
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
				false);
		PeerConfiguration cp16 = new MockPeerConfgurationDecorator(cd16); // connect retry interval
		PeerConfiguration cd17 = new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				60, // connect retry time
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
				false);
		PeerConfiguration cp17 = new MockPeerConfgurationDecorator(cd17); // connect retry interval
		PeerConfiguration cd18 = new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				60, // connect retry time
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
				false);
		PeerConfiguration cp18 = new MockPeerConfgurationDecorator(cd18); // connect retry interval
		PeerConfiguration cd19 = new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				60, // connect retry time
				300, // hold time
				false, // hold timer disabled
				30, // idle hold time
				false, // allow automatic start
				false, // allow automatic stop
				0, // automatic start interval
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				181, // delay open time
				false);
		PeerConfiguration cp19 = new MockPeerConfgurationDecorator(cd19); // connect retry interval
		PeerConfiguration cd20 = new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				60, // connect retry time
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
				true);
		PeerConfiguration cp20 = new MockPeerConfgurationDecorator(cd20); // connect retry interval
		
		Assert.assertEquals(cp1.hashCode(), cp2.hashCode());
		Assert.assertFalse(cp1.hashCode() == cp3.hashCode());
		Assert.assertFalse(cp1.hashCode() == cp4.hashCode());
		Assert.assertFalse(cp1.hashCode() == cp5.hashCode());
		Assert.assertFalse(cp1.hashCode() == cp6.hashCode());
		Assert.assertFalse(cp1.hashCode() == cp7.hashCode());
		Assert.assertFalse(cp1.hashCode() == cp8.hashCode());
		Assert.assertFalse(cp1.hashCode() == cp9.hashCode());
		Assert.assertFalse(cp1.hashCode() == cp10.hashCode());
		Assert.assertFalse(cp1.hashCode() == cp11.hashCode());
		Assert.assertFalse(cp1.hashCode() == cp12.hashCode());
		Assert.assertFalse(cp1.hashCode() == cp13.hashCode());
		Assert.assertFalse(cp1.hashCode() == cp14.hashCode());
		Assert.assertFalse(cp1.hashCode() == cp15.hashCode());
		Assert.assertFalse(cp1.hashCode() == cp16.hashCode());
		Assert.assertFalse(cp1.hashCode() == cp17.hashCode());
		Assert.assertFalse(cp1.hashCode() == cp18.hashCode());
		Assert.assertFalse(cp1.hashCode() == cp19.hashCode());
		Assert.assertFalse(cp1.hashCode() == cp20.hashCode());

		Assert.assertEquals(cp1.hashCode(), cd1.hashCode());
		Assert.assertEquals(cp2.hashCode(), cd2.hashCode());
		Assert.assertEquals(cp3.hashCode(), cd3.hashCode());
		Assert.assertEquals(cp4.hashCode(), cd4.hashCode());
		Assert.assertEquals(cp5.hashCode(), cd5.hashCode());
		Assert.assertEquals(cp6.hashCode(), cd6.hashCode());
		Assert.assertEquals(cp7.hashCode(), cd7.hashCode());
		Assert.assertEquals(cp8.hashCode(), cd8.hashCode());
		Assert.assertEquals(cp9.hashCode(), cd9.hashCode());
		Assert.assertEquals(cp10.hashCode(), cd10.hashCode());
		Assert.assertEquals(cp11.hashCode(), cd11.hashCode());
		Assert.assertEquals(cp12.hashCode(), cd12.hashCode());
		Assert.assertEquals(cp13.hashCode(), cd13.hashCode());
		Assert.assertEquals(cp14.hashCode(), cd14.hashCode());
		Assert.assertEquals(cp15.hashCode(), cd15.hashCode());
		Assert.assertEquals(cp16.hashCode(), cd16.hashCode());
		Assert.assertEquals(cp17.hashCode(), cd17.hashCode());
		Assert.assertEquals(cp18.hashCode(), cd18.hashCode());
		Assert.assertEquals(cp19.hashCode(), cd19.hashCode());
		Assert.assertEquals(cp20.hashCode(), cd20.hashCode());
	}
}
