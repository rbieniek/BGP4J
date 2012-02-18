package org.bgp4.config.nodes.impl;

import java.net.InetAddress;

import junit.framework.Assert;

import org.apache.commons.configuration.ConfigurationException;
import org.bgp4.config.nodes.PeerConfiguration;
import org.bgp4.config.nodes.PeerConfigurationTimerDecorator;
import org.junit.Test;

public class FixedDefaultsPeerConfigurationTimerDecoratorTest {

	@Test
	public void testDecoratedTimers() {
		PeerConfiguration decorated = new PeerConfigurationImpl();
		PeerConfigurationTimerDecorator decorator = new FixedDefaultsPeerConfigurationTimerDecorator(decorated);
		
		Assert.assertEquals(0, decorated.getHoldTime());
		Assert.assertEquals(120, decorator.getHoldTime());
		Assert.assertEquals(0, decorated.getIdleHoldTime());
		Assert.assertEquals(30, decorator.getIdleHoldTime());
	}
	
	@Test
	public void testUndecoratedTimers() throws ConfigurationException {
		PeerConfigurationImpl decorated = new PeerConfigurationImpl();
		PeerConfigurationTimerDecorator decorator = new FixedDefaultsPeerConfigurationTimerDecorator(decorated);
		
		decorated.setHoldTime(45);
		decorated.setIdleHoldTime(300);
		
		Assert.assertEquals(45, decorated.getHoldTime());
		Assert.assertEquals(45, decorator.getHoldTime());
		Assert.assertEquals(300, decorated.getIdleHoldTime());
		Assert.assertEquals(300, decorator.getIdleHoldTime());
	}
	
	@Test
	public void testEquals() throws Exception {
		PeerConfiguration c1 = new FixedDefaultsPeerConfigurationTimerDecorator(new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				300, // hold time
				30)); // connect retry interval
		PeerConfiguration c2 = new FixedDefaultsPeerConfigurationTimerDecorator(new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				300, // hold time
				30)); // connect retry interval
		PeerConfiguration c3 = new FixedDefaultsPeerConfigurationTimerDecorator(new PeerConfigurationImpl("bar", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				300, // hold time
				30)); // connect retry interval
		PeerConfiguration c4 = new FixedDefaultsPeerConfigurationTimerDecorator(new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.5.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				300, // hold time
				30)); // connect retry interval
		PeerConfiguration c5 = new FixedDefaultsPeerConfigurationTimerDecorator(new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24577, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				300, // hold time
				30)); // connect retry interval
		PeerConfiguration c6 = new FixedDefaultsPeerConfigurationTimerDecorator(new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32769, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				300, // hold time
				30)); // connect retry interval
		PeerConfiguration c7 = new FixedDefaultsPeerConfigurationTimerDecorator(new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80402L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				300, // hold time
				30)); // connect retry interval
		PeerConfiguration c8 = new FixedDefaultsPeerConfigurationTimerDecorator(new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80502L, // remote BGP identifier
				300, // hold time
				30)); // connect retry interval
		PeerConfiguration c9 = new FixedDefaultsPeerConfigurationTimerDecorator(new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				301, // hold time
				30)); // connect retry interval
		PeerConfiguration c10 = new FixedDefaultsPeerConfigurationTimerDecorator(new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				300, // hold time
				31)); // connect retry interval
		PeerConfiguration c11 = new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				300, // hold time
				30); // connect retry interval
		
		Assert.assertTrue(c1.equals(c2));
		Assert.assertTrue(c1.equals(c11));
		Assert.assertFalse(c1.equals(c3));
		Assert.assertFalse(c1.equals(c4));
		Assert.assertFalse(c1.equals(c5));
		Assert.assertFalse(c1.equals(c6));
		Assert.assertFalse(c1.equals(c7));
		Assert.assertFalse(c1.equals(c8));
		Assert.assertFalse(c1.equals(c9));
		Assert.assertFalse(c1.equals(c10));
	}

	@Test
	public void testHashCode() throws Exception {
		PeerConfiguration c1 = new FixedDefaultsPeerConfigurationTimerDecorator(new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				300, // hold time
				30)); // connect retry interval
		PeerConfiguration c2 = new FixedDefaultsPeerConfigurationTimerDecorator(new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				300, // hold time
				30)); // connect retry interval
		PeerConfiguration c3 = new FixedDefaultsPeerConfigurationTimerDecorator(new PeerConfigurationImpl("bar", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				300, // hold time
				30)); // connect retry interval
		PeerConfiguration c4 = new FixedDefaultsPeerConfigurationTimerDecorator(new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.5.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				300, // hold time
				30)); // connect retry interval
		PeerConfiguration c5 = new FixedDefaultsPeerConfigurationTimerDecorator(new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24577, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				300, // hold time
				30)); // connect retry interval
		PeerConfiguration c6 = new FixedDefaultsPeerConfigurationTimerDecorator(new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32769, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				300, // hold time
				30)); // connect retry interval
		PeerConfiguration c7 = new FixedDefaultsPeerConfigurationTimerDecorator(new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80402L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				300, // hold time
				30)); // connect retry interval
		PeerConfiguration c8 = new FixedDefaultsPeerConfigurationTimerDecorator(new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80502L, // remote BGP identifier
				300, // hold time
				30)); // connect retry interval
		PeerConfiguration c9 = new FixedDefaultsPeerConfigurationTimerDecorator(new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				301, // hold time
				30)); // connect retry interval
		PeerConfiguration c10 = new FixedDefaultsPeerConfigurationTimerDecorator(new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				300, // hold time
				31)); // connect retry interval
		PeerConfiguration c11 = new PeerConfigurationImpl("foo", // peer name 
				new ClientConfigurationImpl(InetAddress.getByName("192.168.4.1")), // peer address  
				24576, // local AS
				32768, // remote AS
				0xc0a80401L, // local BGP identitifer
				0xc0a80501L, // remote BGP identifier
				300, // hold time
				30); // connect retry interval
		
		Assert.assertEquals(c1.hashCode(), c2.hashCode());
		Assert.assertEquals(c1.hashCode(), c11.hashCode());
		Assert.assertFalse(c1.hashCode() == c3.hashCode());
		Assert.assertFalse(c1.hashCode() == c4.hashCode());
		Assert.assertFalse(c1.hashCode() == c5.hashCode());
		Assert.assertFalse(c1.hashCode() == c6.hashCode());
		Assert.assertFalse(c1.hashCode() == c7.hashCode());
		Assert.assertFalse(c1.hashCode() == c8.hashCode());
		Assert.assertFalse(c1.hashCode() == c9.hashCode());
		Assert.assertFalse(c1.hashCode() == c10.hashCode());
	}

}
