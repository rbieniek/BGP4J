package org.bgp4j.config.nodes.impl;

import java.util.Arrays;

import junit.framework.Assert;

import org.bgp4j.config.nodes.RouteConfiguration;
import org.bgp4j.config.nodes.impl.PathAttributeConfigurationImpl;
import org.bgp4j.config.nodes.impl.RouteConfigurationImpl;
import org.bgp4j.net.NetworkLayerReachabilityInformation;
import org.bgp4j.net.attributes.LocalPrefPathAttribute;
import org.bgp4j.net.attributes.PathAttribute;
import org.junit.Test;

public class RouteConfigurationImplTest {

	@Test
	public void testEquals() {
		RouteConfiguration a = new RouteConfigurationImpl(
				new NetworkLayerReachabilityInformation(24, new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x01}),
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)(new LocalPrefPathAttribute(100)))));
		RouteConfiguration b = new RouteConfigurationImpl(
				new NetworkLayerReachabilityInformation(24, new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x01}),
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)(new LocalPrefPathAttribute(100)))));
		
		Assert.assertTrue(a.equals(b));
	}

	@Test
	public void testHashCode() {
		RouteConfiguration a = new RouteConfigurationImpl(
				new NetworkLayerReachabilityInformation(24, new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x01}),
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)(new LocalPrefPathAttribute(100)))));
		RouteConfiguration b = new RouteConfigurationImpl(
				new NetworkLayerReachabilityInformation(24, new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x01}),
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)(new LocalPrefPathAttribute(100)))));
		
		Assert.assertTrue(a.hashCode() == b.hashCode());
	}
}
