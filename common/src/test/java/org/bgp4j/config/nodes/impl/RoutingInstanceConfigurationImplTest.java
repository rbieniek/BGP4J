/**
 * 
 */
package org.bgp4j.config.nodes.impl;

import java.util.Arrays;

import junit.framework.Assert;

import org.bgp4j.config.nodes.AddressFamilyRoutingPeerConfiguration;
import org.bgp4j.config.nodes.RoutingFilterConfiguration;
import org.bgp4j.config.nodes.RoutingInstanceConfiguration;
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
public class RoutingInstanceConfigurationImplTest {

	@Test
	public void testEquals() {
		AddressFamilyRoutingPeerConfiguration afrpc1aa = new AddressFamilyRoutingPeerConfigurationImpl(AddressFamilyKey.IPV4_UNICAST_FORWARDING, 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x01})))), 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x02})))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), 
						(PathAttribute)new MultiExitDiscPathAttribute(1))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(200), 
						(PathAttribute)new MultiExitDiscPathAttribute(2))));
		AddressFamilyRoutingPeerConfiguration afrpc1ab = new AddressFamilyRoutingPeerConfigurationImpl(AddressFamilyKey.IPV4_UNICAST_FORWARDING, 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x01})))), 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x02})))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), 
						(PathAttribute)new MultiExitDiscPathAttribute(1))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(200), 
						(PathAttribute)new MultiExitDiscPathAttribute(2))));
		AddressFamilyRoutingPeerConfiguration afrpc1ba = new AddressFamilyRoutingPeerConfigurationImpl(AddressFamilyKey.IPV6_UNICAST_FORWARDING, 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x01})))), 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x02})))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), 
						(PathAttribute)new MultiExitDiscPathAttribute(1))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(200), 
						(PathAttribute)new MultiExitDiscPathAttribute(2))));
		AddressFamilyRoutingPeerConfiguration afrpc1bb = new AddressFamilyRoutingPeerConfigurationImpl(AddressFamilyKey.IPV6_UNICAST_FORWARDING, 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x01})))), 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x02})))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), 
						(PathAttribute)new MultiExitDiscPathAttribute(1))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(200), 
						(PathAttribute)new MultiExitDiscPathAttribute(2))));
		AddressFamilyRoutingPeerConfiguration afrpc2aa = new AddressFamilyRoutingPeerConfigurationImpl(AddressFamilyKey.IPV4_UNICAST_FORWARDING, 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x01})))), 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x02})))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), 
						(PathAttribute)new MultiExitDiscPathAttribute(1))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(200), 
						(PathAttribute)new MultiExitDiscPathAttribute(2))));
		AddressFamilyRoutingPeerConfiguration afrpc2ab = new AddressFamilyRoutingPeerConfigurationImpl(AddressFamilyKey.IPV4_UNICAST_FORWARDING, 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x01})))), 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x02})))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), 
						(PathAttribute)new MultiExitDiscPathAttribute(1))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(200), 
						(PathAttribute)new MultiExitDiscPathAttribute(2))));
		AddressFamilyRoutingPeerConfiguration afrpc2ba = new AddressFamilyRoutingPeerConfigurationImpl(AddressFamilyKey.IPV6_UNICAST_FORWARDING, 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x01})))), 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x02})))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), 
						(PathAttribute)new MultiExitDiscPathAttribute(1))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(200), 
						(PathAttribute)new MultiExitDiscPathAttribute(2))));
		AddressFamilyRoutingPeerConfiguration afrpc2bb = new AddressFamilyRoutingPeerConfigurationImpl(AddressFamilyKey.IPV6_UNICAST_FORWARDING, 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x01})))), 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x02})))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), 
						(PathAttribute)new MultiExitDiscPathAttribute(1))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(200), 
						(PathAttribute)new MultiExitDiscPathAttribute(2))));

		RoutingPeerConfiguration rpc1a = new RoutingPeerConfigurationImpl("foo1", Arrays.asList(afrpc1aa, afrpc1ba));
		RoutingPeerConfiguration rpc1b = new RoutingPeerConfigurationImpl("foo2", Arrays.asList(afrpc1ab, afrpc1bb));
		RoutingPeerConfiguration rpc2a = new RoutingPeerConfigurationImpl("foo1", Arrays.asList(afrpc2aa, afrpc2ba));
		RoutingPeerConfiguration rpc2b = new RoutingPeerConfigurationImpl("foo2", Arrays.asList(afrpc2ab, afrpc2bb));

		Assert.assertTrue(rpc1a.equals(rpc2a));
		Assert.assertTrue(rpc1b.equals(rpc2b));
		Assert.assertTrue(rpc1b.hashCode() == rpc2b.hashCode());
		Assert.assertTrue(rpc1a.compareTo(rpc2a) == 0);
		Assert.assertTrue(rpc1b.compareTo(rpc2b) == 0);
		
		RoutingInstanceConfiguration ric1 = new RoutingInstanceConfigurationImpl(rpc1a, rpc1b);
		RoutingInstanceConfiguration ric2 = new RoutingInstanceConfigurationImpl(rpc2a, rpc2b);

		Assert.assertTrue(ric1.equals(ric2));
		Assert.assertTrue(ric1.hashCode() == ric2.hashCode());
		Assert.assertTrue(ric1.compareTo(ric2) == 0);
	}

	@Test
	public void testSmallerFirstPeer() {
		AddressFamilyRoutingPeerConfiguration afrpc1aa = new AddressFamilyRoutingPeerConfigurationImpl(AddressFamilyKey.IPV4_UNICAST_FORWARDING, 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x01})))), 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x02})))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), 
						(PathAttribute)new MultiExitDiscPathAttribute(1))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(200), 
						(PathAttribute)new MultiExitDiscPathAttribute(2))));
		AddressFamilyRoutingPeerConfiguration afrpc1ab = new AddressFamilyRoutingPeerConfigurationImpl(AddressFamilyKey.IPV4_UNICAST_FORWARDING, 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x01})))), 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x02})))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), 
						(PathAttribute)new MultiExitDiscPathAttribute(1))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(200), 
						(PathAttribute)new MultiExitDiscPathAttribute(2))));
		AddressFamilyRoutingPeerConfiguration afrpc1ba = new AddressFamilyRoutingPeerConfigurationImpl(AddressFamilyKey.IPV6_UNICAST_FORWARDING, 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x01})))), 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x02})))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), 
						(PathAttribute)new MultiExitDiscPathAttribute(1))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(200), 
						(PathAttribute)new MultiExitDiscPathAttribute(2))));
		AddressFamilyRoutingPeerConfiguration afrpc1bb = new AddressFamilyRoutingPeerConfigurationImpl(AddressFamilyKey.IPV6_UNICAST_FORWARDING, 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x01})))), 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x02})))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), 
						(PathAttribute)new MultiExitDiscPathAttribute(1))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(200), 
						(PathAttribute)new MultiExitDiscPathAttribute(2))));
		AddressFamilyRoutingPeerConfiguration afrpc2aa = new AddressFamilyRoutingPeerConfigurationImpl(AddressFamilyKey.IPV4_UNICAST_FORWARDING, 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x01})))), 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x02})))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), 
						(PathAttribute)new MultiExitDiscPathAttribute(1))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(200), 
						(PathAttribute)new MultiExitDiscPathAttribute(2))));
		AddressFamilyRoutingPeerConfiguration afrpc2ab = new AddressFamilyRoutingPeerConfigurationImpl(AddressFamilyKey.IPV4_UNICAST_FORWARDING, 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x01})))), 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x02})))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), 
						(PathAttribute)new MultiExitDiscPathAttribute(1))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(200), 
						(PathAttribute)new MultiExitDiscPathAttribute(2))));
		AddressFamilyRoutingPeerConfiguration afrpc2ba = new AddressFamilyRoutingPeerConfigurationImpl(AddressFamilyKey.IPV6_UNICAST_FORWARDING, 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x01})))), 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x02})))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), 
						(PathAttribute)new MultiExitDiscPathAttribute(1))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(200), 
						(PathAttribute)new MultiExitDiscPathAttribute(2))));
		AddressFamilyRoutingPeerConfiguration afrpc2bb = new AddressFamilyRoutingPeerConfigurationImpl(AddressFamilyKey.IPV6_UNICAST_FORWARDING, 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x01})))), 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x02})))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), 
						(PathAttribute)new MultiExitDiscPathAttribute(1))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(200), 
						(PathAttribute)new MultiExitDiscPathAttribute(2))));

		RoutingPeerConfiguration rpc1a = new RoutingPeerConfigurationImpl("foo0", Arrays.asList(afrpc1aa, afrpc1ba));
		RoutingPeerConfiguration rpc1b = new RoutingPeerConfigurationImpl("foo2", Arrays.asList(afrpc1ab, afrpc1bb));
		RoutingPeerConfiguration rpc2a = new RoutingPeerConfigurationImpl("foo1", Arrays.asList(afrpc2aa, afrpc2ba));
		RoutingPeerConfiguration rpc2b = new RoutingPeerConfigurationImpl("foo2", Arrays.asList(afrpc2ab, afrpc2bb));

		Assert.assertFalse(rpc1a.equals(rpc2a));
		Assert.assertTrue(rpc1b.equals(rpc2b));
		Assert.assertFalse(rpc1a.hashCode() == rpc2a.hashCode());
		Assert.assertTrue(rpc1b.hashCode() == rpc2b.hashCode());
		Assert.assertTrue(rpc1a.compareTo(rpc2a) < 0);
		Assert.assertTrue(rpc1b.compareTo(rpc2b) == 0);
		
		RoutingInstanceConfiguration ric1 = new RoutingInstanceConfigurationImpl(rpc1a, rpc1b);
		RoutingInstanceConfiguration ric2 = new RoutingInstanceConfigurationImpl(rpc2a, rpc2b);

		Assert.assertFalse(ric1.equals(ric2));
		Assert.assertFalse(ric1.hashCode() == ric2.hashCode());
		Assert.assertTrue(ric1.compareTo(ric2) < 0);
	}


	@Test
	public void testSmallerSecondPeer() {
		AddressFamilyRoutingPeerConfiguration afrpc1aa = new AddressFamilyRoutingPeerConfigurationImpl(AddressFamilyKey.IPV4_UNICAST_FORWARDING, 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x01})))), 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x02})))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), 
						(PathAttribute)new MultiExitDiscPathAttribute(1))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(200), 
						(PathAttribute)new MultiExitDiscPathAttribute(2))));
		AddressFamilyRoutingPeerConfiguration afrpc1ab = new AddressFamilyRoutingPeerConfigurationImpl(AddressFamilyKey.IPV4_UNICAST_FORWARDING, 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x01})))), 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x02})))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), 
						(PathAttribute)new MultiExitDiscPathAttribute(1))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(200), 
						(PathAttribute)new MultiExitDiscPathAttribute(2))));
		AddressFamilyRoutingPeerConfiguration afrpc1ba = new AddressFamilyRoutingPeerConfigurationImpl(AddressFamilyKey.IPV6_UNICAST_FORWARDING, 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x01})))), 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x02})))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), 
						(PathAttribute)new MultiExitDiscPathAttribute(1))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(200), 
						(PathAttribute)new MultiExitDiscPathAttribute(2))));
		AddressFamilyRoutingPeerConfiguration afrpc1bb = new AddressFamilyRoutingPeerConfigurationImpl(AddressFamilyKey.IPV6_UNICAST_FORWARDING, 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x01})))), 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x02})))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), 
						(PathAttribute)new MultiExitDiscPathAttribute(1))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(200), 
						(PathAttribute)new MultiExitDiscPathAttribute(2))));
		AddressFamilyRoutingPeerConfiguration afrpc2aa = new AddressFamilyRoutingPeerConfigurationImpl(AddressFamilyKey.IPV4_UNICAST_FORWARDING, 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x01})))), 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x02})))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), 
						(PathAttribute)new MultiExitDiscPathAttribute(1))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(200), 
						(PathAttribute)new MultiExitDiscPathAttribute(2))));
		AddressFamilyRoutingPeerConfiguration afrpc2ab = new AddressFamilyRoutingPeerConfigurationImpl(AddressFamilyKey.IPV4_UNICAST_FORWARDING, 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x01})))), 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x02})))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), 
						(PathAttribute)new MultiExitDiscPathAttribute(1))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(200), 
						(PathAttribute)new MultiExitDiscPathAttribute(2))));
		AddressFamilyRoutingPeerConfiguration afrpc2ba = new AddressFamilyRoutingPeerConfigurationImpl(AddressFamilyKey.IPV6_UNICAST_FORWARDING, 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x01})))), 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x02})))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), 
						(PathAttribute)new MultiExitDiscPathAttribute(1))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(200), 
						(PathAttribute)new MultiExitDiscPathAttribute(2))));
		AddressFamilyRoutingPeerConfiguration afrpc2bb = new AddressFamilyRoutingPeerConfigurationImpl(AddressFamilyKey.IPV6_UNICAST_FORWARDING, 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x01})))), 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x02})))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), 
						(PathAttribute)new MultiExitDiscPathAttribute(1))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(200), 
						(PathAttribute)new MultiExitDiscPathAttribute(2))));

		RoutingPeerConfiguration rpc1a = new RoutingPeerConfigurationImpl("foo1", Arrays.asList(afrpc1aa, afrpc1ba));
		RoutingPeerConfiguration rpc1b = new RoutingPeerConfigurationImpl("foo0", Arrays.asList(afrpc1ab, afrpc1bb));
		RoutingPeerConfiguration rpc2a = new RoutingPeerConfigurationImpl("foo1", Arrays.asList(afrpc2aa, afrpc2ba));
		RoutingPeerConfiguration rpc2b = new RoutingPeerConfigurationImpl("foo2", Arrays.asList(afrpc2ab, afrpc2bb));

		Assert.assertTrue(rpc1a.equals(rpc2a));
		Assert.assertFalse(rpc1b.equals(rpc2b));
		Assert.assertTrue(rpc1a.hashCode() == rpc2a.hashCode());
		Assert.assertFalse(rpc1b.hashCode() == rpc2b.hashCode());
		Assert.assertTrue(rpc1a.compareTo(rpc2a) == 0);
		Assert.assertTrue(rpc1b.compareTo(rpc2b) < 0);
		
		RoutingInstanceConfiguration ric1 = new RoutingInstanceConfigurationImpl(rpc1a, rpc1b);
		RoutingInstanceConfiguration ric2 = new RoutingInstanceConfigurationImpl(rpc2a, rpc2b);

		Assert.assertFalse(ric1.equals(ric2));
		Assert.assertFalse(ric1.hashCode() == ric2.hashCode());
		Assert.assertTrue(ric1.compareTo(ric2) < 0);
	}
}
