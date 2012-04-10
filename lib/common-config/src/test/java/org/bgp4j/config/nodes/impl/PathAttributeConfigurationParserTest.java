/**
 * 
 */
package org.bgp4j.config.nodes.impl;

import java.util.Iterator;

import junit.framework.Assert;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.bgp4j.config.ConfigTestBase;
import org.bgp4j.config.nodes.PathAttributeConfiguration;
import org.bgp4j.config.nodes.impl.PathAttributeConfigurationParser;
import org.bgp4j.net.ASType;
import org.bgp4j.net.Origin;
import org.bgp4j.net.PathSegment;
import org.bgp4j.net.PathSegmentType;
import org.bgp4j.net.attributes.ASPathAttribute;
import org.bgp4j.net.attributes.LocalPrefPathAttribute;
import org.bgp4j.net.attributes.MultiExitDiscPathAttribute;
import org.bgp4j.net.attributes.OriginPathAttribute;
import org.bgp4j.net.attributes.PathAttribute;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author rainer
 *
 */
public class PathAttributeConfigurationParserTest extends ConfigTestBase {

	@Before
	public void before() throws Exception {
		this.config = loadConfiguration("config/nodes/PathAttributesConfig.xml");
		this.parser = obtainInstance(PathAttributeConfigurationParser.class);
	}
	
	@After
	public void after() {
		this.config = null;
		this.parser = null;
	}
	
	private XMLConfiguration config;
	private PathAttributeConfigurationParser parser;

	@Test
	public void testGoodLocalPreference() throws Exception {
		PathAttributeConfiguration pac = parser.parseConfiguration(config.configurationAt("PathAttributes(0)"));
		
		Assert.assertEquals(1, pac.getAttributes().size());
		
		Iterator<PathAttribute> it = pac.getAttributes().iterator();

		PathAttribute pa = it.next();
		Assert.assertEquals(LocalPrefPathAttribute.class, pa.getClass());
		Assert.assertEquals(100, ((LocalPrefPathAttribute)pa).getLocalPreference());
	}
	
	@Test(expected=ConfigurationException.class)
	public void testDuplicateLocalPreference() throws Exception {
		parser.parseConfiguration(config.configurationAt("PathAttributes(1)"));
	}

	@Test
	public void testGoodMultiExitDisc() throws Exception {
		PathAttributeConfiguration pac = parser.parseConfiguration(config.configurationAt("PathAttributes(2)"));
		
		Assert.assertEquals(1, pac.getAttributes().size());
		
		Iterator<PathAttribute> it = pac.getAttributes().iterator();

		PathAttribute pa = it.next();
		Assert.assertEquals(MultiExitDiscPathAttribute.class, pa.getClass());
		Assert.assertEquals(1, ((MultiExitDiscPathAttribute)pa).getDiscriminator());
	}

	@Test(expected=ConfigurationException.class)
	public void testDuplicateMultiExitDisc() throws Exception {
		parser.parseConfiguration(config.configurationAt("PathAttributes(3)"));
	}

	@Test(expected=ConfigurationException.class)
	public void testUnknownAttribute() throws Exception {
		parser.parseConfiguration(config.configurationAt("PathAttributes(4)"));
	}

	@Test
	public void testAll() throws Exception {
		PathAttributeConfiguration pac = parser.parseConfiguration(config.configurationAt("PathAttributes(5)"));
		
		Assert.assertEquals(4, pac.getAttributes().size());
		
		Iterator<PathAttribute> it = pac.getAttributes().iterator();

		PathAttribute pa = it.next();
		
		Assert.assertEquals(ASPathAttribute.class, pa.getClass());
		Assert.assertEquals(ASType.AS_NUMBER_2OCTETS, ((ASPathAttribute)pa).getAsType());
		Assert.assertEquals(0, ((ASPathAttribute)pa).getPathSegments().size());
		
		pa = it.next();
		Assert.assertEquals(LocalPrefPathAttribute.class, pa.getClass());
		Assert.assertEquals(100, ((LocalPrefPathAttribute)pa).getLocalPreference());

		pa = it.next();
		Assert.assertEquals(MultiExitDiscPathAttribute.class, pa.getClass());
		Assert.assertEquals(1, ((MultiExitDiscPathAttribute)pa).getDiscriminator());

		pa = it.next();
		Assert.assertEquals(OriginPathAttribute.class, pa.getClass());
		Assert.assertEquals(Origin.INCOMPLETE, ((OriginPathAttribute)pa).getOrigin());
	}

	@Test
	public void testGoodASPath() throws Exception {
		PathAttributeConfiguration pac = parser.parseConfiguration(config.configurationAt("PathAttributes(6)"));
		
		Assert.assertEquals(1, pac.getAttributes().size());
		
		Iterator<PathAttribute> it = pac.getAttributes().iterator();

		PathAttribute pa = it.next();
		Assert.assertEquals(ASPathAttribute.class, pa.getClass());
		
		ASPathAttribute asa = (ASPathAttribute)pa;
		Assert.assertEquals(ASType.AS_NUMBER_2OCTETS, asa.getAsType());
		Assert.assertEquals(2, asa.getPathSegments().size());
		
		PathSegment segment = asa.getPathSegments().get(0);
		
		Assert.assertEquals(ASType.AS_NUMBER_2OCTETS, segment.getAsType());
		Assert.assertEquals(PathSegmentType.AS_SEQUENCE, segment.getPathSegmentType());
		Assert.assertEquals(3, segment.getAses().size());
		Assert.assertEquals(1, segment.getAses().get(0).intValue());
		Assert.assertEquals(2, segment.getAses().get(1).intValue());
		Assert.assertEquals(3, segment.getAses().get(2).intValue());
		
		segment = asa.getPathSegments().get(1);
		
		Assert.assertEquals(ASType.AS_NUMBER_2OCTETS, segment.getAsType());
		Assert.assertEquals(PathSegmentType.AS_SET, segment.getPathSegmentType());
		Assert.assertEquals(3, segment.getAses().size());
		Assert.assertEquals(4, segment.getAses().get(0).intValue());
		Assert.assertEquals(5, segment.getAses().get(1).intValue());
		Assert.assertEquals(6, segment.getAses().get(2).intValue());
	}
}
