/**
 * 
 */
package org.bgp4.config.nodes.impl;

import java.util.Arrays;

import org.bgp4.config.nodes.AddressFamilyRoutingConfiguration;
import org.bgp4.config.nodes.RouteConfiguration;
import org.bgp4j.net.AddressFamilyKey;
import org.bgp4j.net.NetworkLayerReachabilityInformation;
import org.bgp4j.net.attributes.LocalPrefPathAttribute;
import org.bgp4j.net.attributes.MultiExitDiscPathAttribute;
import org.bgp4j.net.attributes.PathAttribute;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author rainer
 *
 */
public class AddressFamilyRoutingConfigurationImplTest {

	@Test
	public void testEquals() {
		AddressFamilyRoutingConfiguration a = new AddressFamilyRoutingConfigurationImpl(AddressFamilyKey.IPV4_UNICAST_FORWARDING, null);
		AddressFamilyRoutingConfiguration b = new AddressFamilyRoutingConfigurationImpl(AddressFamilyKey.IPV4_UNICAST_FORWARDING, null);
		AddressFamilyRoutingConfiguration c = new AddressFamilyRoutingConfigurationImpl(AddressFamilyKey.IPV4_UNICAST_FORWARDING, 
				Arrays.asList((RouteConfiguration)new RouteConfigurationImpl(new NetworkLayerReachabilityInformation(0, null), 
						new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), 
								(PathAttribute)new MultiExitDiscPathAttribute(1))))));
		AddressFamilyRoutingConfiguration d = new AddressFamilyRoutingConfigurationImpl(AddressFamilyKey.IPV4_UNICAST_FORWARDING, 
				Arrays.asList((RouteConfiguration)new RouteConfigurationImpl(new NetworkLayerReachabilityInformation(0, null), 
						new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new MultiExitDiscPathAttribute(1), (PathAttribute)new LocalPrefPathAttribute(100))))));
		AddressFamilyRoutingConfiguration e = new AddressFamilyRoutingConfigurationImpl(AddressFamilyKey.IPV4_UNICAST_FORWARDING, 
				Arrays.asList((RouteConfiguration)new RouteConfigurationImpl(new NetworkLayerReachabilityInformation(0, null), 
						new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), 
								(PathAttribute)new MultiExitDiscPathAttribute(1)))),
							(RouteConfiguration)new RouteConfigurationImpl(new NetworkLayerReachabilityInformation(16, new byte[] {(byte)0xc0, (byte)0xa8} ), 
									new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), 
											(PathAttribute)new MultiExitDiscPathAttribute(1))))));
		AddressFamilyRoutingConfiguration f = new AddressFamilyRoutingConfigurationImpl(AddressFamilyKey.IPV4_UNICAST_FORWARDING, 
				Arrays.asList((RouteConfiguration)new RouteConfigurationImpl(new NetworkLayerReachabilityInformation(16, new byte[] {(byte)0xc0, (byte)0xa8} ), 
						new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), 
								(PathAttribute)new MultiExitDiscPathAttribute(1)))),
							(RouteConfiguration)new RouteConfigurationImpl(new NetworkLayerReachabilityInformation(0, null), 
									new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), 
											(PathAttribute)new MultiExitDiscPathAttribute(1))))));
		
		Assert.assertTrue(a.equals(b));
		Assert.assertTrue(c.equals(d));
		Assert.assertTrue(e.equals(f));
		
		Assert.assertTrue(a.hashCode() == b.hashCode());
		Assert.assertTrue(c.hashCode() == d.hashCode());
		Assert.assertTrue(e.hashCode() == f.hashCode());

		Assert.assertTrue(a.compareTo(b) == 0);
		Assert.assertTrue(c.compareTo(d) == 0);
		Assert.assertTrue(e.compareTo(f) == 0);
	}

	@Test
	public void testSmallerKey() {
		AddressFamilyRoutingConfiguration a = new AddressFamilyRoutingConfigurationImpl(AddressFamilyKey.IPV4_UNICAST_FORWARDING, null);
		AddressFamilyRoutingConfiguration b = new AddressFamilyRoutingConfigurationImpl(AddressFamilyKey.IPV6_UNICAST_FORWARDING, null);
		AddressFamilyRoutingConfiguration c = new AddressFamilyRoutingConfigurationImpl(AddressFamilyKey.IPV4_UNICAST_FORWARDING, 
				Arrays.asList((RouteConfiguration)new RouteConfigurationImpl(new NetworkLayerReachabilityInformation(0, null), 
						new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), 
								(PathAttribute)new MultiExitDiscPathAttribute(1))))));
		AddressFamilyRoutingConfiguration d = new AddressFamilyRoutingConfigurationImpl(AddressFamilyKey.IPV6_UNICAST_FORWARDING, 
				Arrays.asList((RouteConfiguration)new RouteConfigurationImpl(new NetworkLayerReachabilityInformation(0, null), 
						new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new MultiExitDiscPathAttribute(1), (PathAttribute)new LocalPrefPathAttribute(100))))));
		AddressFamilyRoutingConfiguration e = new AddressFamilyRoutingConfigurationImpl(AddressFamilyKey.IPV4_UNICAST_FORWARDING, 
				Arrays.asList((RouteConfiguration)new RouteConfigurationImpl(new NetworkLayerReachabilityInformation(0, null), 
						new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), 
								(PathAttribute)new MultiExitDiscPathAttribute(1)))),
							(RouteConfiguration)new RouteConfigurationImpl(new NetworkLayerReachabilityInformation(16, new byte[] {(byte)0xc0, (byte)0xa8} ), 
									new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), 
											(PathAttribute)new MultiExitDiscPathAttribute(1))))));
		AddressFamilyRoutingConfiguration f = new AddressFamilyRoutingConfigurationImpl(AddressFamilyKey.IPV6_UNICAST_FORWARDING, 
				Arrays.asList((RouteConfiguration)new RouteConfigurationImpl(new NetworkLayerReachabilityInformation(16, new byte[] {(byte)0xc0, (byte)0xa8} ), 
						new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), 
								(PathAttribute)new MultiExitDiscPathAttribute(1)))),
							(RouteConfiguration)new RouteConfigurationImpl(new NetworkLayerReachabilityInformation(0, null), 
									new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), 
											(PathAttribute)new MultiExitDiscPathAttribute(1))))));
		
		Assert.assertFalse(a.equals(b));
		Assert.assertFalse(c.equals(d));
		Assert.assertFalse(e.equals(f));
		
		Assert.assertFalse(a.hashCode() == b.hashCode());
		Assert.assertFalse(c.hashCode() == d.hashCode());
		Assert.assertFalse(e.hashCode() == f.hashCode());

		Assert.assertTrue(a.compareTo(b) < 0);
		Assert.assertTrue(c.compareTo(d) < 0);
		Assert.assertTrue(e.compareTo(f) < 0);
	}

	@Test
	public void testSmallerContent() {
		AddressFamilyRoutingConfiguration a = new AddressFamilyRoutingConfigurationImpl(AddressFamilyKey.IPV4_UNICAST_FORWARDING, null);

		AddressFamilyRoutingConfiguration b = new AddressFamilyRoutingConfigurationImpl(AddressFamilyKey.IPV4_UNICAST_FORWARDING, 
				Arrays.asList((RouteConfiguration)new RouteConfigurationImpl(new NetworkLayerReachabilityInformation(0, null), 
						new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(50))))));

		AddressFamilyRoutingConfiguration c = new AddressFamilyRoutingConfigurationImpl(AddressFamilyKey.IPV4_UNICAST_FORWARDING, 
				Arrays.asList((RouteConfiguration)new RouteConfigurationImpl(new NetworkLayerReachabilityInformation(0, null), 
						new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100))))));

		AddressFamilyRoutingConfiguration d = new AddressFamilyRoutingConfigurationImpl(AddressFamilyKey.IPV4_UNICAST_FORWARDING, 
				Arrays.asList((RouteConfiguration)new RouteConfigurationImpl(new NetworkLayerReachabilityInformation(0, null), 
						new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new MultiExitDiscPathAttribute(1), (PathAttribute)new LocalPrefPathAttribute(100))))));

		AddressFamilyRoutingConfiguration e = new AddressFamilyRoutingConfigurationImpl(AddressFamilyKey.IPV4_UNICAST_FORWARDING, 
				Arrays.asList((RouteConfiguration)new RouteConfigurationImpl(new NetworkLayerReachabilityInformation(0, null), 
						new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), 
								(PathAttribute)new MultiExitDiscPathAttribute(1)))),
							(RouteConfiguration)new RouteConfigurationImpl(new NetworkLayerReachabilityInformation(16, new byte[] {(byte)0xc0, (byte)0xa8} ), 
									new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(50), 
											(PathAttribute)new MultiExitDiscPathAttribute(1))))));

		AddressFamilyRoutingConfiguration f = new AddressFamilyRoutingConfigurationImpl(AddressFamilyKey.IPV4_UNICAST_FORWARDING, 
				Arrays.asList((RouteConfiguration)new RouteConfigurationImpl(new NetworkLayerReachabilityInformation(16, new byte[] {(byte)0xc0, (byte)0xa8} ), 
						new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), 
								(PathAttribute)new MultiExitDiscPathAttribute(1)))),
							(RouteConfiguration)new RouteConfigurationImpl(new NetworkLayerReachabilityInformation(0, null), 
									new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), 
											(PathAttribute)new MultiExitDiscPathAttribute(1))))));
		
		Assert.assertFalse(a.equals(b));
		Assert.assertFalse(a.equals(c));
		Assert.assertFalse(c.equals(d));
		Assert.assertFalse(e.equals(f));
		
		Assert.assertFalse(a.hashCode() == c.hashCode());
		Assert.assertFalse(a.hashCode() == b.hashCode());
		Assert.assertFalse(c.hashCode() == d.hashCode());
		Assert.assertFalse(e.hashCode() == f.hashCode());

		Assert.assertTrue(a.compareTo(b) < 0);
		Assert.assertTrue(b.compareTo(c) < 0);
		Assert.assertTrue(a.compareTo(c) < 0);
		Assert.assertTrue(c.compareTo(d) < 0);
		Assert.assertTrue(d.compareTo(e) < 0);
		Assert.assertTrue(e.compareTo(f) < 0);
	}


	@Test
	public void testSmallerNumberOfRoutes() {
		AddressFamilyRoutingConfiguration d = new AddressFamilyRoutingConfigurationImpl(AddressFamilyKey.IPV4_UNICAST_FORWARDING, 
				Arrays.asList((RouteConfiguration)new RouteConfigurationImpl(new NetworkLayerReachabilityInformation(0, null), 
						new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new MultiExitDiscPathAttribute(1), (PathAttribute)new LocalPrefPathAttribute(100))))));

		AddressFamilyRoutingConfiguration e = new AddressFamilyRoutingConfigurationImpl(AddressFamilyKey.IPV4_UNICAST_FORWARDING, 
				Arrays.asList((RouteConfiguration)new RouteConfigurationImpl(new NetworkLayerReachabilityInformation(0, null), 
						new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(100), 
								(PathAttribute)new MultiExitDiscPathAttribute(1)))),
							(RouteConfiguration)new RouteConfigurationImpl(new NetworkLayerReachabilityInformation(16, new byte[] {(byte)0xc0, (byte)0xa8} ), 
									new PathAttributeConfigurationImpl(Arrays.asList((PathAttribute)new LocalPrefPathAttribute(50), 
											(PathAttribute)new MultiExitDiscPathAttribute(1))))));
		
		Assert.assertFalse(d.equals(e));
		
		Assert.assertFalse(d.hashCode() == e.hashCode());

		Assert.assertTrue(d.compareTo(e) < 0);
	}
}
