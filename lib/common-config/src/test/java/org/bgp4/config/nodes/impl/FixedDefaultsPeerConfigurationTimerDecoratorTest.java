package org.bgp4.config.nodes.impl;

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
		Assert.assertEquals(0, decorated.getConnectRetryInterval());
		Assert.assertEquals(30, decorator.getConnectRetryInterval());
	}
	
	@Test
	public void testUndecoratedTimers() throws ConfigurationException {
		PeerConfigurationImpl decorated = new PeerConfigurationImpl();
		PeerConfigurationTimerDecorator decorator = new FixedDefaultsPeerConfigurationTimerDecorator(decorated);
		
		decorated.setHoldTime(45);
		decorated.setConnectRetryInterval(300);
		
		Assert.assertEquals(45, decorated.getHoldTime());
		Assert.assertEquals(45, decorator.getHoldTime());
		Assert.assertEquals(300, decorated.getConnectRetryInterval());
		Assert.assertEquals(300, decorator.getConnectRetryInterval());
	}
	
}
