package org.bgp4.config.nodes;

import java.net.InetAddress;

import junit.framework.Assert;

import org.bgp4.config.nodes.PeerConfiguration;
import org.bgp4.config.nodes.PeerConfigurationDecorator;
import org.bgp4.config.nodes.impl.ClientConfigurationImpl;
import org.bgp4.config.nodes.impl.PeerConfigurationImpl;
import org.junit.Test;

public class PeerConfigurationDecoratorTest {
	public static class MockPeerConfgurationDecorator extends PeerConfigurationDecorator {
		MockPeerConfgurationDecorator(PeerConfiguration decorated) {
			super(decorated);
		}
	}
	
	@Test
	public void testEquals() throws Exception {
		PeerConfiguration c1 = new MockPeerConfgurationDecorator(new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				300, // hold time
				30, // connect retry interval
				false, // allow automatic start
				false, // allow automatic stop
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				180, // delay open time
				false)); // connect retry interval
		PeerConfiguration c2 = new MockPeerConfgurationDecorator(new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				300, // hold time
				30, // connect retry interval
				false, // allow automatic start
				false, // allow automatic stop
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				180, // delay open time
				false)); // connect retry interval
		PeerConfiguration c3 = new MockPeerConfgurationDecorator(new PeerConfigurationImpl("bar", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				300, // hold time
				30, // connect retry interval
				false, // allow automatic start
				false, // allow automatic stop
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				180, // delay open time
				false)); // connect retry interval
		PeerConfiguration c4 = new MockPeerConfgurationDecorator(new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.5.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				300, // hold time
				30, // connect retry interval
				false, // allow automatic start
				false, // allow automatic stop
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				180, // delay open time
				false)); // connect retry interval
		PeerConfiguration c5 = new MockPeerConfgurationDecorator(new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24577, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				300, // hold time
				30, // connect retry interval
				false, // allow automatic start
				false, // allow automatic stop
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				180, // delay open time
				false)); // connect retry interval
		PeerConfiguration c6 = new MockPeerConfgurationDecorator(new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32769, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				300, // hold time
				30, // connect retry interval
				false, // allow automatic start
				false, // allow automatic stop
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				180, // delay open time
				false)); // connect retry interval
		PeerConfiguration c7 = new MockPeerConfgurationDecorator(new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80402L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				300, // hold time
				30, // connect retry interval
				false, // allow automatic start
				false, // allow automatic stop
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				180, // delay open time
				false)); // connect retry interval
		PeerConfiguration c8 = new MockPeerConfgurationDecorator(new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80502L, // remote BGP identifier
				300, // hold time
				30, // connect retry interval
				false, // allow automatic start
				false, // allow automatic stop
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				180, // delay open time
				false)); // connect retry interval
		PeerConfiguration c9 = new MockPeerConfgurationDecorator(new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				301, // hold time
				30, // connect retry interval
				false, // allow automatic start
				false, // allow automatic stop
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				180, // delay open time
				false)); // connect retry interval
		PeerConfiguration c10 = new MockPeerConfgurationDecorator(new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				300, // hold time
				31, // connect retry interval
				false, // allow automatic start
				false, // allow automatic stop
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				180, // delay open time
				false)); // connect retry interval
		PeerConfiguration c11 = new MockPeerConfgurationDecorator(new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				300, // hold time
				31, // connect retry interval
				true, // allow automatic start
				false, // allow automatic stop
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				180, // delay open time
				false)); // connect retry interval
		PeerConfiguration c12 = new MockPeerConfgurationDecorator(new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				300, // hold time
				31, // connect retry interval
				false, // allow automatic start
				true, // allow automatic stop
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				180, // delay open time
				false)); // connect retry interval
		PeerConfiguration c13 = new MockPeerConfgurationDecorator(new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				300, // hold time
				31, // connect retry interval
				false, // allow automatic start
				false, // allow automatic stop
				true, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				180, // delay open time
				false)); // connect retry interval
		PeerConfiguration c14 = new MockPeerConfgurationDecorator(new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				300, // hold time
				31, // connect retry interval
				false, // allow automatic start
				false, // allow automatic stop
				false, // damp peer oscillation
				true, // passive tcp establishment
				false, // delay open
				180, // delay open time
				false)); // connect retry interval
		PeerConfiguration c15 = new MockPeerConfgurationDecorator(new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				300, // hold time
				31, // connect retry interval
				false, // allow automatic start
				false, // allow automatic stop
				false, // damp peer oscillation
				false, // passive tcp establishment
				true, // delay open
				180, // delay open time
				false)); // connect retry interval
		PeerConfiguration c16 = new MockPeerConfgurationDecorator(new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				300, // hold time
				31, // connect retry interval
				false, // allow automatic start
				false, // allow automatic stop
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				240, // delay open time
				false)); // connect retry interval
		PeerConfiguration c17 = new MockPeerConfgurationDecorator(new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				300, // hold time
				31, // connect retry interval
				false, // allow automatic start
				false, // allow automatic stop
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				180, // delay open time
				true)); // connect retry interval

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
	}

	@Test
	public void testHashCode() throws Exception {
		PeerConfiguration c1 = new MockPeerConfgurationDecorator(new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				300, // hold time
				30, // connect retry interval
				false, // allow automatic start
				false, // allow automatic stop
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				180, // delay open time
				false)); // connect retry interval
		PeerConfiguration c2 = new MockPeerConfgurationDecorator(new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				300, // hold time
				30, // connect retry interval
				false, // allow automatic start
				false, // allow automatic stop
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				180, // delay open time
				false)); // connect retry interval
		PeerConfiguration c3 = new MockPeerConfgurationDecorator(new PeerConfigurationImpl("bar", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				300, // hold time
				30, // connect retry interval
				false, // allow automatic start
				false, // allow automatic stop
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				180, // delay open time
				false)); // connect retry interval
		PeerConfiguration c4 = new MockPeerConfgurationDecorator(new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.5.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				300, // hold time
				30, // connect retry interval
				false, // allow automatic start
				false, // allow automatic stop
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				180, // delay open time
				false)); // connect retry interval
		PeerConfiguration c5 = new MockPeerConfgurationDecorator(new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24577, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				300, // hold time
				30, // connect retry interval
				false, // allow automatic start
				false, // allow automatic stop
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				180, // delay open time
				false)); // connect retry interval
		PeerConfiguration c6 = new MockPeerConfgurationDecorator(new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32769, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				300, // hold time
				30, // connect retry interval
				false, // allow automatic start
				false, // allow automatic stop
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				180, // delay open time
				false)); // connect retry interval
		PeerConfiguration c7 = new MockPeerConfgurationDecorator(new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80402L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				300, // hold time
				30, // connect retry interval
				false, // allow automatic start
				false, // allow automatic stop
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				180, // delay open time
				false)); // connect retry interval
		PeerConfiguration c8 = new MockPeerConfgurationDecorator(new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80502L, // remote BGP identifier
				300, // hold time
				30, // connect retry interval
				false, // allow automatic start
				false, // allow automatic stop
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				180, // delay open time
				false)); // connect retry interval
		PeerConfiguration c9 = new MockPeerConfgurationDecorator(new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				301, // hold time
				30, // connect retry interval
				false, // allow automatic start
				false, // allow automatic stop
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				180, // delay open time
				false)); // connect retry interval
		PeerConfiguration c10 = new MockPeerConfgurationDecorator(new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				300, // hold time
				31, // connect retry interval
				false, // allow automatic start
				false, // allow automatic stop
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				180, // delay open time
				false)); // connect retry interval
		PeerConfiguration c11 = new MockPeerConfgurationDecorator(new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				300, // hold time
				31, // connect retry interval
				true, // allow automatic start
				false, // allow automatic stop
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				180, // delay open time
				false)); // connect retry interval
		PeerConfiguration c12 = new MockPeerConfgurationDecorator(new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				300, // hold time
				31, // connect retry interval
				false, // allow automatic start
				true, // allow automatic stop
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				180, // delay open time
				false)); // connect retry interval
		PeerConfiguration c13 = new MockPeerConfgurationDecorator(new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				300, // hold time
				31, // connect retry interval
				false, // allow automatic start
				false, // allow automatic stop
				true, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				180, // delay open time
				false)); // connect retry interval
		PeerConfiguration c14 = new MockPeerConfgurationDecorator(new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				300, // hold time
				31, // connect retry interval
				false, // allow automatic start
				false, // allow automatic stop
				false, // damp peer oscillation
				true, // passive tcp establishment
				false, // delay open
				180, // delay open time
				false)); // connect retry interval
		PeerConfiguration c15 = new MockPeerConfgurationDecorator(new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				300, // hold time
				31, // connect retry interval
				false, // allow automatic start
				false, // allow automatic stop
				false, // damp peer oscillation
				false, // passive tcp establishment
				true, // delay open
				180, // delay open time
				false)); // connect retry interval
		PeerConfiguration c16 = new MockPeerConfgurationDecorator(new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				300, // hold time
				31, // connect retry interval
				false, // allow automatic start
				false, // allow automatic stop
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				240, // delay open time
				false)); // connect retry interval
		PeerConfiguration c17 = new MockPeerConfgurationDecorator(new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				300, // hold time
				31, // connect retry interval
				false, // allow automatic start
				false, // allow automatic stop
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				180, // delay open time
				true)); // connect retry interval
		
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
	}
}
