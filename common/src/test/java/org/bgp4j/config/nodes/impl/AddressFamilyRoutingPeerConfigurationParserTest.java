package org.bgp4j.config.nodes.impl;

import java.util.Iterator;

import junit.framework.Assert;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.bgp4j.config.ConfigTestBase;
import org.bgp4j.config.nodes.AddressFamilyRoutingPeerConfiguration;
import org.bgp4j.config.nodes.PathAttributeConfiguration;
import org.bgp4j.config.nodes.PrefixRoutingFilterConfiguration;
import org.bgp4j.config.nodes.RoutingFilterConfiguration;
import org.bgp4j.net.AddressFamilyKey;
import org.bgp4j.net.NetworkLayerReachabilityInformation;
import org.bgp4j.net.Origin;
import org.bgp4j.net.attributes.LocalPrefPathAttribute;
import org.bgp4j.net.attributes.MultiExitDiscPathAttribute;
import org.bgp4j.net.attributes.OriginPathAttribute;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AddressFamilyRoutingPeerConfigurationParserTest extends ConfigTestBase {
	@Before
	public void before() throws Exception {
		this.config = loadConfiguration("config/nodes/AddressFamilyRoutingPeerConfig.xml");
		this.parser = obtainInstance(AddressFamilyRoutingPeerConfigurationParser.class);
	}
	
	@After
	public void after() {
		this.config = null;
		this.parser = null;
	}
	
	private XMLConfiguration config;
	private AddressFamilyRoutingPeerConfigurationParser parser;

	@Test
	public void testEmptyConfigration() throws Exception {
		AddressFamilyRoutingPeerConfiguration afrpc = parser.parseConfiguration(config.configurationAt("RoutingConfiguration(0)"));
		
		Assert.assertEquals(AddressFamilyKey.IPV4_UNICAST_FORWARDING, afrpc.getAddressFamilyKey());
		Assert.assertEquals(0, afrpc.getLocalDefaultPathAttributes().getAttributes().size());
		Assert.assertEquals(0, afrpc.getLocalRoutingFilters().size());
		Assert.assertEquals(0, afrpc.getRemoteDefaultPathAttributes().getAttributes().size());
		Assert.assertEquals(0, afrpc.getRemoteRoutingFilters().size());
	}
	
	@Test
	public void testLocalFilter() throws Exception {
		AddressFamilyRoutingPeerConfiguration afrpc = parser.parseConfiguration(config.configurationAt("RoutingConfiguration(1)"));
		Iterator<RoutingFilterConfiguration> filterIt;
		RoutingFilterConfiguration rfc;
		PrefixRoutingFilterConfiguration prfc;
		NetworkLayerReachabilityInformation nlri = new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x01});
		
		Assert.assertEquals(AddressFamilyKey.IPV4_UNICAST_FORWARDING, afrpc.getAddressFamilyKey());
		Assert.assertEquals(0, afrpc.getLocalDefaultPathAttributes().getAttributes().size());
		Assert.assertEquals(0, afrpc.getRemoteDefaultPathAttributes().getAttributes().size());
		Assert.assertEquals(0, afrpc.getRemoteRoutingFilters().size());

		filterIt = afrpc.getLocalRoutingFilters().iterator();
		Assert.assertTrue(filterIt.hasNext());
		rfc = filterIt.next();
		Assert.assertTrue(rfc instanceof PrefixRoutingFilterConfiguration);
		prfc = (PrefixRoutingFilterConfiguration)rfc;
		Assert.assertEquals("prefix", prfc.getName());
		Assert.assertEquals(1, prfc.getFilterPrefixes().size());
		Assert.assertTrue(prfc.getFilterPrefixes().contains(nlri));
	}
	
	@Test
	public void testLocalPathAttributes() throws Exception {
		AddressFamilyRoutingPeerConfiguration afrpc = parser.parseConfiguration(config.configurationAt("RoutingConfiguration(2)"));
		PathAttributeConfiguration attrs;
		LocalPrefPathAttribute localPreference = new LocalPrefPathAttribute(100);
		MultiExitDiscPathAttribute multiExitDisc = new MultiExitDiscPathAttribute(1);
		OriginPathAttribute origin = new OriginPathAttribute(Origin.INCOMPLETE);
		
		Assert.assertEquals(AddressFamilyKey.IPV4_UNICAST_FORWARDING, afrpc.getAddressFamilyKey());
		Assert.assertEquals(0, afrpc.getLocalRoutingFilters().size());
		Assert.assertEquals(0, afrpc.getRemoteDefaultPathAttributes().getAttributes().size());
		Assert.assertEquals(0, afrpc.getRemoteRoutingFilters().size());

		attrs = afrpc.getLocalDefaultPathAttributes();
		Assert.assertEquals(3, attrs.getAttributes().size());
		Assert.assertTrue(attrs.getAttributes().contains(localPreference));
		Assert.assertTrue(attrs.getAttributes().contains(multiExitDisc));
		Assert.assertTrue(attrs.getAttributes().contains(origin));
	}
		
	@Test
	public void testRemoteFilter() throws Exception {
		AddressFamilyRoutingPeerConfiguration afrpc = parser.parseConfiguration(config.configurationAt("RoutingConfiguration(3)"));
		Iterator<RoutingFilterConfiguration> filterIt;
		RoutingFilterConfiguration rfc;
		PrefixRoutingFilterConfiguration prfc;
		NetworkLayerReachabilityInformation nlri = new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x01});
		
		Assert.assertEquals(AddressFamilyKey.IPV4_UNICAST_FORWARDING, afrpc.getAddressFamilyKey());
		Assert.assertEquals(0, afrpc.getLocalDefaultPathAttributes().getAttributes().size());
		Assert.assertEquals(0, afrpc.getRemoteDefaultPathAttributes().getAttributes().size());
		Assert.assertEquals(0, afrpc.getLocalRoutingFilters().size());

		filterIt = afrpc.getRemoteRoutingFilters().iterator();
		Assert.assertTrue(filterIt.hasNext());
		rfc = filterIt.next();
		Assert.assertTrue(rfc instanceof PrefixRoutingFilterConfiguration);
		prfc = (PrefixRoutingFilterConfiguration)rfc;
		Assert.assertEquals("prefix", prfc.getName());
		Assert.assertEquals(1, prfc.getFilterPrefixes().size());
		Assert.assertTrue(prfc.getFilterPrefixes().contains(nlri));
	}

	@Test
	public void testRemotePathAttributes() throws Exception {
		AddressFamilyRoutingPeerConfiguration afrpc = parser.parseConfiguration(config.configurationAt("RoutingConfiguration(4)"));
		PathAttributeConfiguration attrs;
		LocalPrefPathAttribute localPreference = new LocalPrefPathAttribute(100);
		MultiExitDiscPathAttribute multiExitDisc = new MultiExitDiscPathAttribute(1);
		OriginPathAttribute origin = new OriginPathAttribute(Origin.INCOMPLETE);
		
		Assert.assertEquals(AddressFamilyKey.IPV4_UNICAST_FORWARDING, afrpc.getAddressFamilyKey());
		Assert.assertEquals(0, afrpc.getLocalRoutingFilters().size());
		Assert.assertEquals(0, afrpc.getLocalDefaultPathAttributes().getAttributes().size());
		Assert.assertEquals(0, afrpc.getRemoteRoutingFilters().size());

		attrs = afrpc.getRemoteDefaultPathAttributes();
		Assert.assertEquals(3, attrs.getAttributes().size());
		Assert.assertTrue(attrs.getAttributes().contains(localPreference));
		Assert.assertTrue(attrs.getAttributes().contains(multiExitDisc));
		Assert.assertTrue(attrs.getAttributes().contains(origin));
	}

	@Test
	public void testFull() throws Exception {
		AddressFamilyRoutingPeerConfiguration afrpc = parser.parseConfiguration(config.configurationAt("RoutingConfiguration(5)"));
		PathAttributeConfiguration attrs;
		LocalPrefPathAttribute localPreference = new LocalPrefPathAttribute(100);
		MultiExitDiscPathAttribute multiExitDisc = new MultiExitDiscPathAttribute(1);
		OriginPathAttribute origin = new OriginPathAttribute(Origin.INCOMPLETE);
		Iterator<RoutingFilterConfiguration> filterIt;
		RoutingFilterConfiguration rfc;
		PrefixRoutingFilterConfiguration prfc;
		NetworkLayerReachabilityInformation nlri = new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x01});
		
		Assert.assertEquals(AddressFamilyKey.IPV4_UNICAST_FORWARDING, afrpc.getAddressFamilyKey());

		attrs = afrpc.getRemoteDefaultPathAttributes();
		Assert.assertEquals(3, attrs.getAttributes().size());
		Assert.assertTrue(attrs.getAttributes().contains(localPreference));
		Assert.assertTrue(attrs.getAttributes().contains(multiExitDisc));
		Assert.assertTrue(attrs.getAttributes().contains(origin));

		attrs = afrpc.getLocalDefaultPathAttributes();
		Assert.assertEquals(3, attrs.getAttributes().size());
		Assert.assertTrue(attrs.getAttributes().contains(localPreference));
		Assert.assertTrue(attrs.getAttributes().contains(multiExitDisc));
		Assert.assertTrue(attrs.getAttributes().contains(origin));


		filterIt = afrpc.getRemoteRoutingFilters().iterator();
		Assert.assertTrue(filterIt.hasNext());
		rfc = filterIt.next();
		Assert.assertTrue(rfc instanceof PrefixRoutingFilterConfiguration);
		prfc = (PrefixRoutingFilterConfiguration)rfc;
		Assert.assertEquals("prefix", prfc.getName());
		Assert.assertEquals(1, prfc.getFilterPrefixes().size());
		Assert.assertTrue(prfc.getFilterPrefixes().contains(nlri));


		filterIt = afrpc.getLocalRoutingFilters().iterator();
		Assert.assertTrue(filterIt.hasNext());
		rfc = filterIt.next();
		Assert.assertTrue(rfc instanceof PrefixRoutingFilterConfiguration);
		prfc = (PrefixRoutingFilterConfiguration)rfc;
		Assert.assertEquals("prefix", prfc.getName());
		Assert.assertEquals(1, prfc.getFilterPrefixes().size());
		Assert.assertTrue(prfc.getFilterPrefixes().contains(nlri));
	}
	
	@Test(expected=ConfigurationException.class)
	public void testDuplicateLocalFilter() throws Exception {
		parser.parseConfiguration(config.configurationAt("RoutingConfiguration(6)"));
	}
	
	@Test(expected=ConfigurationException.class)
	public void testDuplicateLocalPathAttributes() throws Exception {
		parser.parseConfiguration(config.configurationAt("RoutingConfiguration(7)"));
	}
	
	@Test(expected=ConfigurationException.class)
	public void testDuplicateRemoteFilter() throws Exception {
		parser.parseConfiguration(config.configurationAt("RoutingConfiguration(8)"));
	}
	
	@Test(expected=ConfigurationException.class)
	public void testDuplicateRemotePathAttributes() throws Exception {
		parser.parseConfiguration(config.configurationAt("RoutingConfiguration(9)"));
	}

	@Test
	public void testFullIPv6() throws Exception {
		AddressFamilyRoutingPeerConfiguration afrpc = parser.parseConfiguration(config.configurationAt("RoutingConfiguration(10)"));
		PathAttributeConfiguration attrs;
		LocalPrefPathAttribute localPreference = new LocalPrefPathAttribute(100);
		MultiExitDiscPathAttribute multiExitDisc = new MultiExitDiscPathAttribute(1);
		OriginPathAttribute origin = new OriginPathAttribute(Origin.INCOMPLETE);
		Iterator<RoutingFilterConfiguration> filterIt;
		RoutingFilterConfiguration rfc;
		PrefixRoutingFilterConfiguration prfc;
		NetworkLayerReachabilityInformation nlri = new NetworkLayerReachabilityInformation(24, new byte[] {(byte)0xc0, (byte)0xa8, 0x01});
		
		Assert.assertEquals(AddressFamilyKey.IPV6_UNICAST_FORWARDING, afrpc.getAddressFamilyKey());

		attrs = afrpc.getRemoteDefaultPathAttributes();
		Assert.assertEquals(3, attrs.getAttributes().size());
		Assert.assertTrue(attrs.getAttributes().contains(localPreference));
		Assert.assertTrue(attrs.getAttributes().contains(multiExitDisc));
		Assert.assertTrue(attrs.getAttributes().contains(origin));

		attrs = afrpc.getLocalDefaultPathAttributes();
		Assert.assertEquals(3, attrs.getAttributes().size());
		Assert.assertTrue(attrs.getAttributes().contains(localPreference));
		Assert.assertTrue(attrs.getAttributes().contains(multiExitDisc));
		Assert.assertTrue(attrs.getAttributes().contains(origin));


		filterIt = afrpc.getRemoteRoutingFilters().iterator();
		Assert.assertTrue(filterIt.hasNext());
		rfc = filterIt.next();
		Assert.assertTrue(rfc instanceof PrefixRoutingFilterConfiguration);
		prfc = (PrefixRoutingFilterConfiguration)rfc;
		Assert.assertEquals("prefix", prfc.getName());
		Assert.assertEquals(1, prfc.getFilterPrefixes().size());
		Assert.assertTrue(prfc.getFilterPrefixes().contains(nlri));


		filterIt = afrpc.getLocalRoutingFilters().iterator();
		Assert.assertTrue(filterIt.hasNext());
		rfc = filterIt.next();
		Assert.assertTrue(rfc instanceof PrefixRoutingFilterConfiguration);
		prfc = (PrefixRoutingFilterConfiguration)rfc;
		Assert.assertEquals("prefix", prfc.getName());
		Assert.assertEquals(1, prfc.getFilterPrefixes().size());
		Assert.assertTrue(prfc.getFilterPrefixes().contains(nlri));
	}
	
}
