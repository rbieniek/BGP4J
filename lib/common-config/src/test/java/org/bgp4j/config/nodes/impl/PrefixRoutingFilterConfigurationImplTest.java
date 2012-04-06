/**
 * 
 */
package org.bgp4j.config.nodes.impl;

import java.util.Arrays;

import junit.framework.Assert;

import org.bgp4j.config.nodes.PrefixRoutingFilterConfiguration;
import org.bgp4j.net.NetworkLayerReachabilityInformation;
import org.junit.Test;

/**
 * @author rainer
 *
 */
public class PrefixRoutingFilterConfigurationImplTest {

	@Test
	public void testEquals() {
		PrefixRoutingFilterConfiguration a = new PrefixRoutingFilterConfigurationImpl("foo",
				Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x01}), 
						new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x02})));
		PrefixRoutingFilterConfiguration b = new PrefixRoutingFilterConfigurationImpl("foo",
				Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x02}), 
						new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x01})));
		
		Assert.assertTrue(a.equals(b));
		Assert.assertTrue(a.hashCode() == b.hashCode());
		Assert.assertTrue(a.compareTo(b) == 0);		
	}

	@Test
	public void testSmallerName() {
		PrefixRoutingFilterConfiguration a = new PrefixRoutingFilterConfigurationImpl("bar",
				Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x01}), 
						new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x02})));
		PrefixRoutingFilterConfiguration b = new PrefixRoutingFilterConfigurationImpl("foo",
				Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x02}), 
						new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x01})));
		
		Assert.assertFalse(a.equals(b));
		Assert.assertFalse(a.hashCode() == b.hashCode());
		Assert.assertTrue(a.compareTo(b) < 0);		
	}

	@Test
	public void testContentSize() {
		PrefixRoutingFilterConfiguration a = new PrefixRoutingFilterConfigurationImpl("foo",
				Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x01})));
		PrefixRoutingFilterConfiguration b = new PrefixRoutingFilterConfigurationImpl("foo",
				Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x02}), 
						new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x01})));
		
		Assert.assertFalse(a.equals(b));
		Assert.assertFalse(a.hashCode() == b.hashCode());
		Assert.assertTrue(a.compareTo(b) < 0);		
	}

	@Test
	public void testSmallerContet() {
		PrefixRoutingFilterConfiguration a = new PrefixRoutingFilterConfigurationImpl("foo",
				Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x01}), 
						new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x02})));
		PrefixRoutingFilterConfiguration b = new PrefixRoutingFilterConfigurationImpl("foo",
				Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x03}), 
						new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x01})));
		
		Assert.assertFalse(a.equals(b));
		Assert.assertFalse(a.hashCode() == b.hashCode());
		Assert.assertTrue(a.compareTo(b) < 0);		
	}
}
