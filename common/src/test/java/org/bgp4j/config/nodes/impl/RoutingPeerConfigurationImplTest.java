/**
 * 
 */
package org.bgp4j.config.nodes.impl;

import java.util.Arrays;

import junit.framework.Assert;

import org.bgp4j.config.nodes.AddressFamilyRoutingPeerConfiguration;
import org.bgp4j.config.nodes.RoutingFilterConfiguration;
import org.bgp4j.config.nodes.RoutingPeerConfiguration;
import org.bgp4j.net.AddressFamilyKey;
import org.bgp4j.net.NetworkLayerReachabilityInformation;
import org.bgp4j.net.attributes.LocalPrefPathAttribute;
import org.bgp4j.net.attributes.MultiExitDiscPathAttribute;
import org.bgp4j.net.attributes.PathAttribute;
import org.junit.Test;

/**
 * @author rainer
 *
 */
public class RoutingPeerConfigurationImplTest {

	@Test
	public void testEquals() {
		AddressFamilyRoutingPeerConfiguration afrpc1a = new AddressFamilyRoutingPeerConfigurationImpl(AddressFamilyKey.IPV4_UNICAST_FORWARDING, 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x01})))), 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x02})))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), 
						(PathAttribute)new MultiExitDiscPathAttribute(1))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(200), 
						(PathAttribute)new MultiExitDiscPathAttribute(2))));
		AddressFamilyRoutingPeerConfiguration afrpc1b = new AddressFamilyRoutingPeerConfigurationImpl(AddressFamilyKey.IPV6_UNICAST_FORWARDING, 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x01})))), 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x02})))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), 
						(PathAttribute)new MultiExitDiscPathAttribute(1))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(200), 
						(PathAttribute)new MultiExitDiscPathAttribute(2))));
		AddressFamilyRoutingPeerConfiguration afrpc2a = new AddressFamilyRoutingPeerConfigurationImpl(AddressFamilyKey.IPV4_UNICAST_FORWARDING, 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x01})))), 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x02})))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), 
						(PathAttribute)new MultiExitDiscPathAttribute(1))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(200), 
						(PathAttribute)new MultiExitDiscPathAttribute(2))));
		AddressFamilyRoutingPeerConfiguration afrpc2b = new AddressFamilyRoutingPeerConfigurationImpl(AddressFamilyKey.IPV6_UNICAST_FORWARDING, 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x01})))), 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x02})))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), 
						(PathAttribute)new MultiExitDiscPathAttribute(1))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(200), 
						(PathAttribute)new MultiExitDiscPathAttribute(2))));

		RoutingPeerConfiguration rpc1 = new RoutingPeerConfigurationImpl("foo", Arrays.asList(afrpc1a, afrpc1b));
		RoutingPeerConfiguration rpc2 = new RoutingPeerConfigurationImpl("foo", Arrays.asList(afrpc2b, afrpc2a));
		
		Assert.assertTrue(rpc1.equals(rpc2));
		Assert.assertTrue(rpc1.hashCode() == rpc2.hashCode());
		Assert.assertTrue(rpc1.compareTo(rpc2) == 0);
	}

	@Test
	public void testSmallerName() {
		AddressFamilyRoutingPeerConfiguration afrpc1a = new AddressFamilyRoutingPeerConfigurationImpl(AddressFamilyKey.IPV4_UNICAST_FORWARDING, 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x01})))), 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x02})))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), 
						(PathAttribute)new MultiExitDiscPathAttribute(1))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(200), 
						(PathAttribute)new MultiExitDiscPathAttribute(2))));
		AddressFamilyRoutingPeerConfiguration afrpc1b = new AddressFamilyRoutingPeerConfigurationImpl(AddressFamilyKey.IPV6_UNICAST_FORWARDING, 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x01})))), 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x02})))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), 
						(PathAttribute)new MultiExitDiscPathAttribute(1))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(200), 
						(PathAttribute)new MultiExitDiscPathAttribute(2))));
		AddressFamilyRoutingPeerConfiguration afrpc2a = new AddressFamilyRoutingPeerConfigurationImpl(AddressFamilyKey.IPV4_UNICAST_FORWARDING, 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x01})))), 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x02})))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), 
						(PathAttribute)new MultiExitDiscPathAttribute(1))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(200), 
						(PathAttribute)new MultiExitDiscPathAttribute(2))));
		AddressFamilyRoutingPeerConfiguration afrpc2b = new AddressFamilyRoutingPeerConfigurationImpl(AddressFamilyKey.IPV6_UNICAST_FORWARDING, 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x01})))), 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x02})))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), 
						(PathAttribute)new MultiExitDiscPathAttribute(1))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(200), 
						(PathAttribute)new MultiExitDiscPathAttribute(2))));

		RoutingPeerConfiguration rpc1 = new RoutingPeerConfigurationImpl("bar", Arrays.asList(afrpc1a, afrpc1b));
		RoutingPeerConfiguration rpc2 = new RoutingPeerConfigurationImpl("foo", Arrays.asList(afrpc2b, afrpc2a));
		
		Assert.assertFalse(rpc1.equals(rpc2));
		Assert.assertFalse(rpc1.hashCode() == rpc2.hashCode());
		Assert.assertTrue(rpc1.compareTo(rpc2) < 0);
	}

	@Test
	public void testSmallerContentSize() {
		AddressFamilyRoutingPeerConfiguration afrpc1a = new AddressFamilyRoutingPeerConfigurationImpl(AddressFamilyKey.IPV4_UNICAST_FORWARDING, 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x01})))), 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x02})))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), 
						(PathAttribute)new MultiExitDiscPathAttribute(1))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(200), 
						(PathAttribute)new MultiExitDiscPathAttribute(2))));
		AddressFamilyRoutingPeerConfiguration afrpc2a = new AddressFamilyRoutingPeerConfigurationImpl(AddressFamilyKey.IPV4_UNICAST_FORWARDING, 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x01})))), 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x02})))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), 
						(PathAttribute)new MultiExitDiscPathAttribute(1))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(200), 
						(PathAttribute)new MultiExitDiscPathAttribute(2))));
		AddressFamilyRoutingPeerConfiguration afrpc2b = new AddressFamilyRoutingPeerConfigurationImpl(AddressFamilyKey.IPV6_UNICAST_FORWARDING, 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x01})))), 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x02})))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), 
						(PathAttribute)new MultiExitDiscPathAttribute(1))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(200), 
						(PathAttribute)new MultiExitDiscPathAttribute(2))));

		RoutingPeerConfiguration rpc1 = new RoutingPeerConfigurationImpl("foo", Arrays.asList(afrpc1a));
		RoutingPeerConfiguration rpc2 = new RoutingPeerConfigurationImpl("foo", Arrays.asList(afrpc2b, afrpc2a));
		
		Assert.assertFalse(rpc1.equals(rpc2));
		Assert.assertFalse(rpc1.hashCode() == rpc2.hashCode());
		Assert.assertTrue(rpc1.compareTo(rpc2) < 0);
	}


	@Test
	public void testSmallerContent() {
		AddressFamilyRoutingPeerConfiguration afrpc1a = new AddressFamilyRoutingPeerConfigurationImpl(AddressFamilyKey.IPV4_UNICAST_FORWARDING, 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x01})))), 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x02})))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), 
						(PathAttribute)new MultiExitDiscPathAttribute(1))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(200), 
						(PathAttribute)new MultiExitDiscPathAttribute(2))));
		AddressFamilyRoutingPeerConfiguration afrpc2b = new AddressFamilyRoutingPeerConfigurationImpl(AddressFamilyKey.IPV6_UNICAST_FORWARDING, 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x01})))), 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x02})))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), 
						(PathAttribute)new MultiExitDiscPathAttribute(1))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(200), 
						(PathAttribute)new MultiExitDiscPathAttribute(2))));

		RoutingPeerConfiguration rpc1 = new RoutingPeerConfigurationImpl("foo", Arrays.asList(afrpc1a));
		RoutingPeerConfiguration rpc2 = new RoutingPeerConfigurationImpl("foo", Arrays.asList(afrpc2b));
		
		Assert.assertFalse(rpc1.equals(rpc2));
		Assert.assertFalse(rpc1.hashCode() == rpc2.hashCode());
		Assert.assertTrue(rpc1.compareTo(rpc2) < 0);
	}
}
