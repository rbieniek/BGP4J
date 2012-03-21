package org.bgp4.config.nodes.impl;

import java.net.InetAddress;

import junit.framework.Assert;

import org.apache.commons.configuration.ConfigurationException;
import org.bgp4.config.nodes.PeerConfiguration;
import org.bgp4.config.nodes.PeerConfigurationTimerDecorator;
import org.bgp4j.net.capabilities.AutonomousSystem4Capability;
import org.bgp4j.net.capabilities.Capability;
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

		Assert.assertEquals(0, decorated.getDelayOpenTime());
		Assert.assertEquals(15, decorator.getDelayOpenTime());
	}
	
	@Test
	public void testUndecoratedTimers() throws ConfigurationException {
		PeerConfigurationImpl decorated = new PeerConfigurationImpl();
		PeerConfigurationTimerDecorator decorator = new FixedDefaultsPeerConfigurationTimerDecorator(decorated);
		
		decorated.setHoldTime(45);
		decorated.setIdleHoldTime(300);
		decorated.setDelayOpenTime(60);
		
		Assert.assertEquals(45, decorated.getHoldTime());
		Assert.assertEquals(45, decorator.getHoldTime());
		Assert.assertEquals(300, decorated.getIdleHoldTime());
		Assert.assertEquals(300, decorator.getIdleHoldTime());
		Assert.assertEquals(60, decorated.getDelayOpenTime());
		Assert.assertEquals(60, decorator.getDelayOpenTime());
	}
	
	@Test
	public void testEquals() throws Exception {
		PeerConfiguration cp1 = new FixedDefaultsPeerConfigurationTimerDecorator(new PeerConfigurationImpl("foo", // peer name 
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
				120, // automatic start interval
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				15, // delay open time
				false)); 
		PeerConfiguration cp2 = new FixedDefaultsPeerConfigurationTimerDecorator(new PeerConfigurationImpl("foo", // peer name 
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
				120, // automatic start interval
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				15, // delay open time
				false)); 
		PeerConfiguration cp3 = new FixedDefaultsPeerConfigurationTimerDecorator(new PeerConfigurationImpl("foo", // peer name 
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
				120, // automatic start interval
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				15, // delay open time
				false)); 
		PeerConfiguration cp4 = new FixedDefaultsPeerConfigurationTimerDecorator(new PeerConfigurationImpl("foo", // peer name 
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
				120, // automatic start interval
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				15, // delay open time
				false)); 
		PeerConfiguration cp5 = new FixedDefaultsPeerConfigurationTimerDecorator(new PeerConfigurationImpl("foo", // peer name 
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
				120, // automatic start interval
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				15, // delay open time
				false)); 
		PeerConfiguration cp6 = new FixedDefaultsPeerConfigurationTimerDecorator(new PeerConfigurationImpl("foo", // peer name 
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
				121, // automatic start interval
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				15, // delay open time
				false)); 
		PeerConfiguration cp7 = new FixedDefaultsPeerConfigurationTimerDecorator(new PeerConfigurationImpl("foo", // peer name 
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
				120, // automatic start interval
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				16, // delay open time
				false)); 
		PeerConfiguration cp8 = new FixedDefaultsPeerConfigurationTimerDecorator(new PeerConfigurationImpl("foo", // peer name 
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
				120, // automatic start interval
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				15, // delay open time
				false,
				new CapabilitiesImpl(new Capability[] { new AutonomousSystem4Capability(16) }))); 
		
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
				120, // automatic start interval
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				15, // delay open time
				false); 
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
				120, // automatic start interval
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				15, // delay open time
				false); 
		PeerConfiguration cd3 = new PeerConfigurationImpl("foo", // peer name 
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
				120, // automatic start interval
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				15, // delay open time
				false); 
		PeerConfiguration cd4 = new PeerConfigurationImpl("foo", // peer name 
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
				120, // automatic start interval
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				15, // delay open time
				false); 
		PeerConfiguration cd5 = new PeerConfigurationImpl("foo", // peer name 
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
				120, // automatic start interval
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				15, // delay open time
				false); 
		PeerConfiguration cd6 = new PeerConfigurationImpl("foo", // peer name 
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
				121, // automatic start interval
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				15, // delay open time
				false); 
		PeerConfiguration cd7 = new PeerConfigurationImpl("foo", // peer name 
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
				120, // automatic start interval
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				16, // delay open time
				false); 
		PeerConfiguration cd8 = new PeerConfigurationImpl("foo", // peer name 
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
				120, // automatic start interval
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				15, // delay open time
				false,
				new CapabilitiesImpl(new Capability[] { new AutonomousSystem4Capability(16) })); 
		
		Assert.assertTrue(cp1.equals(cp2));
		Assert.assertTrue(cd1.equals(cd2));
		Assert.assertTrue(cp1.equals(cd1));
		Assert.assertTrue(cp2.equals(cd2));
		Assert.assertTrue(cp3.equals(cd3));
		Assert.assertTrue(cp4.equals(cd4));
		Assert.assertTrue(cp5.equals(cd5));
		Assert.assertTrue(cp6.equals(cd6));
		Assert.assertTrue(cp7.equals(cd7));
		Assert.assertTrue(cp8.equals(cd8));

		Assert.assertFalse(cp1.equals(cp3));
		Assert.assertFalse(cp1.equals(cp4));
		Assert.assertFalse(cp1.equals(cp5));
		Assert.assertFalse(cp1.equals(cp6));
		Assert.assertFalse(cp1.equals(cp7));
		Assert.assertFalse(cp1.equals(cp8));

		Assert.assertFalse(cd1.equals(cd3));
		Assert.assertFalse(cd1.equals(cd4));
		Assert.assertFalse(cd1.equals(cd5));
		Assert.assertFalse(cd1.equals(cd6));
		Assert.assertFalse(cd1.equals(cd7));
		Assert.assertFalse(cd1.equals(cd8));
	}

	@Test
	public void testHashCode() throws Exception {
		PeerConfiguration cp1 = new FixedDefaultsPeerConfigurationTimerDecorator(new PeerConfigurationImpl("foo", // peer name 
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
				120, // automatic start interval
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				15, // delay open time
				false)); 
		PeerConfiguration cp2 = new FixedDefaultsPeerConfigurationTimerDecorator(new PeerConfigurationImpl("foo", // peer name 
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
				120, // automatic start interval
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				15, // delay open time
				false)); 
		PeerConfiguration cp3 = new FixedDefaultsPeerConfigurationTimerDecorator(new PeerConfigurationImpl("foo", // peer name 
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
				120, // automatic start interval
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				15, // delay open time
				false)); 
		PeerConfiguration cp4 = new FixedDefaultsPeerConfigurationTimerDecorator(new PeerConfigurationImpl("foo", // peer name 
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
				120, // automatic start interval
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				15, // delay open time
				false)); 
		PeerConfiguration cp5 = new FixedDefaultsPeerConfigurationTimerDecorator(new PeerConfigurationImpl("foo", // peer name 
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
				120, // automatic start interval
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				15, // delay open time
				false)); 
		PeerConfiguration cp6 = new FixedDefaultsPeerConfigurationTimerDecorator(new PeerConfigurationImpl("foo", // peer name 
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
				121, // automatic start interval
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				15, // delay open time
				false)); 
		PeerConfiguration cp7 = new FixedDefaultsPeerConfigurationTimerDecorator(new PeerConfigurationImpl("foo", // peer name 
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
				120, // automatic start interval
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				16, // delay open time
				false)); 
		PeerConfiguration cp8 = new FixedDefaultsPeerConfigurationTimerDecorator(new PeerConfigurationImpl("foo", // peer name 
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
				120, // automatic start interval
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				15, // delay open time
				false,
				new CapabilitiesImpl(new Capability[] { new AutonomousSystem4Capability(16) }))); 
		
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
				120, // automatic start interval
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				15, // delay open time
				false); 
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
				120, // automatic start interval
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				15, // delay open time
				false); 
		PeerConfiguration cd3 = new PeerConfigurationImpl("foo", // peer name 
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
				120, // automatic start interval
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				15, // delay open time
				false); 
		PeerConfiguration cd4 = new PeerConfigurationImpl("foo", // peer name 
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
				120, // automatic start interval
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				15, // delay open time
				false); 
		PeerConfiguration cd5 = new PeerConfigurationImpl("foo", // peer name 
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
				120, // automatic start interval
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				15, // delay open time
				false); 
		PeerConfiguration cd6 = new PeerConfigurationImpl("foo", // peer name 
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
				121, // automatic start interval
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				15, // delay open time
				false); 
		PeerConfiguration cd7 = new PeerConfigurationImpl("foo", // peer name 
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
				120, // automatic start interval
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				16, // delay open time
				false); 
		PeerConfiguration cd8 = new PeerConfigurationImpl("foo", // peer name 
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
				120, // automatic start interval
				false, // damp peer oscillation
				false, // passive tcp establishment
				false, // delay open
				15, // delay open time
				false,
				new CapabilitiesImpl(new Capability[] { new AutonomousSystem4Capability(16) })); 
		
		Assert.assertEquals(cp1.hashCode(), cp2.hashCode());
		Assert.assertEquals(cd1.hashCode(), cd2.hashCode());
		Assert.assertEquals(cp1.hashCode(), cd1.hashCode());
		Assert.assertEquals(cp2.hashCode(), cd2.hashCode());
		Assert.assertEquals(cp3.hashCode(), cd3.hashCode());
		Assert.assertEquals(cp4.hashCode(), cd4.hashCode());
		Assert.assertEquals(cp5.hashCode(), cd5.hashCode());
		Assert.assertEquals(cp6.hashCode(), cd6.hashCode());
		Assert.assertEquals(cp7.hashCode(), cd7.hashCode());
		Assert.assertEquals(cp8.hashCode(), cd8.hashCode());
		
		Assert.assertFalse(cp1.hashCode() == cp3.hashCode());
		Assert.assertFalse(cp1.hashCode() == cp4.hashCode());
		Assert.assertFalse(cp1.hashCode() == cp5.hashCode());
		Assert.assertFalse(cp1.hashCode() == cp6.hashCode());
		Assert.assertFalse(cp1.hashCode() == cd3.hashCode());
		Assert.assertFalse(cp1.hashCode() == cd4.hashCode());
		Assert.assertFalse(cp1.hashCode() == cd5.hashCode());
		Assert.assertFalse(cp1.hashCode() == cd6.hashCode());
		Assert.assertFalse(cp1.hashCode() == cd7.hashCode());
		Assert.assertFalse(cp1.hashCode() == cd8.hashCode());
	}

}
