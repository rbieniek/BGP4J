/**
 * 
 */
package org.bgp4j.config.nodes.impl;

import java.util.Arrays;

import junit.framework.Assert;

import org.bgp4j.config.nodes.AddressFamilyRoutingPeerConfiguration;
import org.bgp4j.config.nodes.RoutingFilterConfiguration;
import org.bgp4j.net.AddressFamilyKey;
import org.bgp4j.net.NetworkLayerReachabilityInformation;
import org.bgp4j.net.Origin;
import org.bgp4j.net.attributes.LocalPrefPathAttribute;
import org.bgp4j.net.attributes.MultiExitDiscPathAttribute;
import org.bgp4j.net.attributes.OriginPathAttribute;
import org.bgp4j.net.attributes.PathAttribute;
import org.junit.Test;

/**
 * @author rainer
 *
 */
public class AddressFamilyRoutingPeerConfigurationImplTest {

	@Test
	public void testEquals() {
		AddressFamilyRoutingPeerConfiguration a = new AddressFamilyRoutingPeerConfigurationImpl(AddressFamilyKey.IPV4_UNICAST_FORWARDING, 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x01})))), 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x02})))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), 
						(PathAttribute)new MultiExitDiscPathAttribute(1))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(200), 
						(PathAttribute)new MultiExitDiscPathAttribute(2))));
		AddressFamilyRoutingPeerConfiguration b = new AddressFamilyRoutingPeerConfigurationImpl(AddressFamilyKey.IPV4_UNICAST_FORWARDING, 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x01})))), 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x02})))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), 
						(PathAttribute)new MultiExitDiscPathAttribute(1))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(200), 
						(PathAttribute)new MultiExitDiscPathAttribute(2))));
		
		Assert.assertTrue(a.equals(b));
		Assert.assertTrue(a.hashCode() == b.hashCode());
		Assert.assertTrue(a.compareTo(b) == 0);
	}

	@Test
	public void testSmallerAddressFamilyKey() {
		AddressFamilyRoutingPeerConfiguration a = new AddressFamilyRoutingPeerConfigurationImpl(AddressFamilyKey.IPV4_UNICAST_FORWARDING, 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x01})))), 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x02})))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), 
						(PathAttribute)new MultiExitDiscPathAttribute(1))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(200), 
						(PathAttribute)new MultiExitDiscPathAttribute(2))));
		AddressFamilyRoutingPeerConfiguration b = new AddressFamilyRoutingPeerConfigurationImpl(AddressFamilyKey.IPV6_UNICAST_FORWARDING, 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x01})))), 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x02})))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), 
						(PathAttribute)new MultiExitDiscPathAttribute(1))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(200), 
						(PathAttribute)new MultiExitDiscPathAttribute(2))));
		
		Assert.assertFalse(a.equals(b));
		Assert.assertFalse(a.hashCode() == b.hashCode());
		Assert.assertTrue(a.compareTo(b) < 0);
	}

	@Test
	public void testSmallerLocalFilterName() {
		AddressFamilyRoutingPeerConfiguration a = new AddressFamilyRoutingPeerConfigurationImpl(AddressFamilyKey.IPV4_UNICAST_FORWARDING, 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("bar", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x01})))), 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x02})))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), 
						(PathAttribute)new MultiExitDiscPathAttribute(1))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(200), 
						(PathAttribute)new MultiExitDiscPathAttribute(2))));
		AddressFamilyRoutingPeerConfiguration b = new AddressFamilyRoutingPeerConfigurationImpl(AddressFamilyKey.IPV4_UNICAST_FORWARDING, 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x01})))), 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x02})))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), 
						(PathAttribute)new MultiExitDiscPathAttribute(1))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(200), 
						(PathAttribute)new MultiExitDiscPathAttribute(2))));
		
		Assert.assertFalse(a.equals(b));
		Assert.assertFalse(a.hashCode() == b.hashCode());
		Assert.assertTrue(a.compareTo(b) < 0);
	}

	@Test
	public void testSmallerLocalFilterContent() {
		AddressFamilyRoutingPeerConfiguration a = new AddressFamilyRoutingPeerConfigurationImpl(AddressFamilyKey.IPV4_UNICAST_FORWARDING, 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x00})))), 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x02})))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), 
						(PathAttribute)new MultiExitDiscPathAttribute(1))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(200), 
						(PathAttribute)new MultiExitDiscPathAttribute(2))));
		AddressFamilyRoutingPeerConfiguration b = new AddressFamilyRoutingPeerConfigurationImpl(AddressFamilyKey.IPV4_UNICAST_FORWARDING, 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x01})))), 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x02})))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), 
						(PathAttribute)new MultiExitDiscPathAttribute(1))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(200), 
						(PathAttribute)new MultiExitDiscPathAttribute(2))));
		
		Assert.assertFalse(a.equals(b));
		Assert.assertFalse(a.hashCode() == b.hashCode());
		Assert.assertTrue(a.compareTo(b) < 0);
	}

	@Test
	public void testSmallerLocalFilterContentSize() {
		AddressFamilyRoutingPeerConfiguration a = new AddressFamilyRoutingPeerConfigurationImpl(AddressFamilyKey.IPV4_UNICAST_FORWARDING, 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x01})))), 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x02})))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), 
						(PathAttribute)new MultiExitDiscPathAttribute(1))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(200), 
						(PathAttribute)new MultiExitDiscPathAttribute(2))));
		AddressFamilyRoutingPeerConfiguration b = new AddressFamilyRoutingPeerConfigurationImpl(AddressFamilyKey.IPV4_UNICAST_FORWARDING, 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x01}))),
						(RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("bar", 
								Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x03})))), 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x02})))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), 
						(PathAttribute)new MultiExitDiscPathAttribute(1))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(200), 
						(PathAttribute)new MultiExitDiscPathAttribute(2))));
		
		Assert.assertFalse(a.equals(b));
		Assert.assertFalse(a.hashCode() == b.hashCode());
		Assert.assertTrue(a.compareTo(b) < 0);
	}

	@Test
	public void testSmallerLocalFilterNull() {
		AddressFamilyRoutingPeerConfiguration a = new AddressFamilyRoutingPeerConfigurationImpl(AddressFamilyKey.IPV4_UNICAST_FORWARDING, 
				null, 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x02})))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), 
						(PathAttribute)new MultiExitDiscPathAttribute(1))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(200), 
						(PathAttribute)new MultiExitDiscPathAttribute(2))));
		AddressFamilyRoutingPeerConfiguration b = new AddressFamilyRoutingPeerConfigurationImpl(AddressFamilyKey.IPV4_UNICAST_FORWARDING, 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x01}))),
						(RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("bar", 
								Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x03})))), 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x02})))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), 
						(PathAttribute)new MultiExitDiscPathAttribute(1))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(200), 
						(PathAttribute)new MultiExitDiscPathAttribute(2))));
		
		Assert.assertFalse(a.equals(b));
		Assert.assertFalse(a.hashCode() == b.hashCode());
		Assert.assertTrue(a.compareTo(b) < 0);
	}

	@Test
	public void testSmallerRemoteFilterName() {
		AddressFamilyRoutingPeerConfiguration a = new AddressFamilyRoutingPeerConfigurationImpl(AddressFamilyKey.IPV4_UNICAST_FORWARDING, 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x01})))), 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("bar", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x02})))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), 
						(PathAttribute)new MultiExitDiscPathAttribute(1))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(200), 
						(PathAttribute)new MultiExitDiscPathAttribute(2))));
		AddressFamilyRoutingPeerConfiguration b = new AddressFamilyRoutingPeerConfigurationImpl(AddressFamilyKey.IPV4_UNICAST_FORWARDING, 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x01})))), 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x02})))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), 
						(PathAttribute)new MultiExitDiscPathAttribute(1))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(200), 
						(PathAttribute)new MultiExitDiscPathAttribute(2))));
		
		Assert.assertFalse(a.equals(b));
		Assert.assertFalse(a.hashCode() == b.hashCode());
		Assert.assertTrue(a.compareTo(b) < 0);
	}

	@Test
	public void testSmallerRemoteFilterContent() {
		AddressFamilyRoutingPeerConfiguration a = new AddressFamilyRoutingPeerConfigurationImpl(AddressFamilyKey.IPV4_UNICAST_FORWARDING, 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x01})))), 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x01})))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), 
						(PathAttribute)new MultiExitDiscPathAttribute(1))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(200), 
						(PathAttribute)new MultiExitDiscPathAttribute(2))));
		AddressFamilyRoutingPeerConfiguration b = new AddressFamilyRoutingPeerConfigurationImpl(AddressFamilyKey.IPV4_UNICAST_FORWARDING, 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x01})))), 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x02})))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), 
						(PathAttribute)new MultiExitDiscPathAttribute(1))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(200), 
						(PathAttribute)new MultiExitDiscPathAttribute(2))));
		
		Assert.assertFalse(a.equals(b));
		Assert.assertFalse(a.hashCode() == b.hashCode());
		Assert.assertTrue(a.compareTo(b) < 0);
	}

	@Test
	public void testSmallerRemoteFilterContentSize() {
		AddressFamilyRoutingPeerConfiguration a = new AddressFamilyRoutingPeerConfigurationImpl(AddressFamilyKey.IPV4_UNICAST_FORWARDING, 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x01})))), 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x02})))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), 
						(PathAttribute)new MultiExitDiscPathAttribute(1))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(200), 
						(PathAttribute)new MultiExitDiscPathAttribute(2))));
		AddressFamilyRoutingPeerConfiguration b = new AddressFamilyRoutingPeerConfigurationImpl(AddressFamilyKey.IPV4_UNICAST_FORWARDING, 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x01})))), 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x02}))),
						(RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("bar", 
								Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x03})))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), 
						(PathAttribute)new MultiExitDiscPathAttribute(1))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(200), 
						(PathAttribute)new MultiExitDiscPathAttribute(2))));
		
		Assert.assertFalse(a.equals(b));
		Assert.assertFalse(a.hashCode() == b.hashCode());
		Assert.assertTrue(a.compareTo(b) < 0);
	}


	@Test
	public void testSmallerRemoteFilterNull() {
		AddressFamilyRoutingPeerConfiguration a = new AddressFamilyRoutingPeerConfigurationImpl(AddressFamilyKey.IPV4_UNICAST_FORWARDING, 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x01})))), 
				null, 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), 
						(PathAttribute)new MultiExitDiscPathAttribute(1))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(200), 
						(PathAttribute)new MultiExitDiscPathAttribute(2))));
		AddressFamilyRoutingPeerConfiguration b = new AddressFamilyRoutingPeerConfigurationImpl(AddressFamilyKey.IPV4_UNICAST_FORWARDING, 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x01})))), 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x02}))),
						(RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("bar", 
								Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x03})))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), 
						(PathAttribute)new MultiExitDiscPathAttribute(1))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(200), 
						(PathAttribute)new MultiExitDiscPathAttribute(2))));
		
		Assert.assertFalse(a.equals(b));
		Assert.assertFalse(a.hashCode() == b.hashCode());
		Assert.assertTrue(a.compareTo(b) < 0);
	}

	@Test
	public void testSmallerLocalPathAttributeContent() {
		AddressFamilyRoutingPeerConfiguration a = new AddressFamilyRoutingPeerConfigurationImpl(AddressFamilyKey.IPV4_UNICAST_FORWARDING, 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x01})))), 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x02})))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(50), 
						(PathAttribute)new MultiExitDiscPathAttribute(1))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(200), 
						(PathAttribute)new MultiExitDiscPathAttribute(2))));
		AddressFamilyRoutingPeerConfiguration b = new AddressFamilyRoutingPeerConfigurationImpl(AddressFamilyKey.IPV4_UNICAST_FORWARDING, 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x01})))), 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x02})))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), 
						(PathAttribute)new MultiExitDiscPathAttribute(1))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(200), 
						(PathAttribute)new MultiExitDiscPathAttribute(2))));
		
		Assert.assertFalse(a.equals(b));
		Assert.assertFalse(a.hashCode() == b.hashCode());
		Assert.assertTrue(a.compareTo(b) < 0);
	}

	@Test
	public void testSmallerLocalPathAttributeContentSize() {
		AddressFamilyRoutingPeerConfiguration a = new AddressFamilyRoutingPeerConfigurationImpl(AddressFamilyKey.IPV4_UNICAST_FORWARDING, 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x01})))), 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x02})))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), 
						(PathAttribute)new MultiExitDiscPathAttribute(1))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(200), 
						(PathAttribute)new MultiExitDiscPathAttribute(2))));
		AddressFamilyRoutingPeerConfiguration b = new AddressFamilyRoutingPeerConfigurationImpl(AddressFamilyKey.IPV4_UNICAST_FORWARDING, 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x01})))), 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x02})))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), 
						(PathAttribute)new MultiExitDiscPathAttribute(1),
						(PathAttribute)new OriginPathAttribute(Origin.INCOMPLETE))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(200), 
						(PathAttribute)new MultiExitDiscPathAttribute(2))));
		
		Assert.assertFalse(a.equals(b));
		Assert.assertFalse(a.hashCode() == b.hashCode());
		Assert.assertTrue(a.compareTo(b) < 0);
	}

	@Test
	public void testSmallerLocalPathAttributeNull() {
		AddressFamilyRoutingPeerConfiguration a = new AddressFamilyRoutingPeerConfigurationImpl(AddressFamilyKey.IPV4_UNICAST_FORWARDING, 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x01})))), 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x02})))), 
				null, 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(200), 
						(PathAttribute)new MultiExitDiscPathAttribute(2))));
		AddressFamilyRoutingPeerConfiguration b = new AddressFamilyRoutingPeerConfigurationImpl(AddressFamilyKey.IPV4_UNICAST_FORWARDING, 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x01})))), 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x02})))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), 
						(PathAttribute)new MultiExitDiscPathAttribute(1))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(200), 
						(PathAttribute)new MultiExitDiscPathAttribute(2))));
		
		Assert.assertFalse(a.equals(b));
		Assert.assertFalse(a.hashCode() == b.hashCode());
		Assert.assertTrue(a.compareTo(b) < 0);
	}

	@Test
	public void testSmallerRemotePathAttributeContent() {
		AddressFamilyRoutingPeerConfiguration a = new AddressFamilyRoutingPeerConfigurationImpl(AddressFamilyKey.IPV4_UNICAST_FORWARDING, 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x01})))), 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x02})))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), 
						(PathAttribute)new MultiExitDiscPathAttribute(1))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(150), 
						(PathAttribute)new MultiExitDiscPathAttribute(2))));
		AddressFamilyRoutingPeerConfiguration b = new AddressFamilyRoutingPeerConfigurationImpl(AddressFamilyKey.IPV4_UNICAST_FORWARDING, 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x01})))), 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x02})))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), 
						(PathAttribute)new MultiExitDiscPathAttribute(1))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(200), 
						(PathAttribute)new MultiExitDiscPathAttribute(2))));
		
		Assert.assertFalse(a.equals(b));
		Assert.assertFalse(a.hashCode() == b.hashCode());
		Assert.assertTrue(a.compareTo(b) < 0);
	}

	@Test
	public void testSmallerRemotePathAttributeNull() {
		AddressFamilyRoutingPeerConfiguration a = new AddressFamilyRoutingPeerConfigurationImpl(AddressFamilyKey.IPV4_UNICAST_FORWARDING, 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x01})))), 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x02})))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), 
						(PathAttribute)new MultiExitDiscPathAttribute(1))), 
				null);
		AddressFamilyRoutingPeerConfiguration b = new AddressFamilyRoutingPeerConfigurationImpl(AddressFamilyKey.IPV4_UNICAST_FORWARDING, 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x01})))), 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x02})))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), 
						(PathAttribute)new MultiExitDiscPathAttribute(1))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(200), 
						(PathAttribute)new MultiExitDiscPathAttribute(2))));
		
		Assert.assertFalse(a.equals(b));
		Assert.assertFalse(a.hashCode() == b.hashCode());
		Assert.assertTrue(a.compareTo(b) < 0);
	}

	@Test
	public void testSmallerRemotePathAttributeContentSize() {
		AddressFamilyRoutingPeerConfiguration a = new AddressFamilyRoutingPeerConfigurationImpl(AddressFamilyKey.IPV4_UNICAST_FORWARDING, 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x01})))), 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x02})))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), 
						(PathAttribute)new MultiExitDiscPathAttribute(1))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(200), 
						(PathAttribute)new MultiExitDiscPathAttribute(2))));
		AddressFamilyRoutingPeerConfiguration b = new AddressFamilyRoutingPeerConfigurationImpl(AddressFamilyKey.IPV4_UNICAST_FORWARDING, 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x01})))), 
				Arrays.asList((RoutingFilterConfiguration)new PrefixRoutingFilterConfigurationImpl("foo", 
						Arrays.asList(new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x02})))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), 
						(PathAttribute)new MultiExitDiscPathAttribute(1))), 
				new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(200), 
						(PathAttribute)new MultiExitDiscPathAttribute(2),
						(PathAttribute)new OriginPathAttribute(Origin.INCOMPLETE))));
		
		Assert.assertFalse(a.equals(b));
		Assert.assertFalse(a.hashCode() == b.hashCode());
		Assert.assertTrue(a.compareTo(b) < 0);
	}
}
