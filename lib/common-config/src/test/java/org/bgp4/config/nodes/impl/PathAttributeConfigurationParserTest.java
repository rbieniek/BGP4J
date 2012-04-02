/**
 * 
 */
package org.bgp4.config.nodes.impl;

import java.util.Iterator;

import junit.framework.Assert;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.bgp4.config.ConfigTestBase;
import org.bgp4.config.nodes.PathAttributeConfiguration;
import org.bgp4j.net.attributes.LocalPrefPathAttribute;
import org.bgp4j.net.attributes.MultiExitDiscPathAttribute;
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
		
		Assert.assertEquals(2, pac.getAttributes().size());
		
		Iterator<PathAttribute> it = pac.getAttributes().iterator();

		PathAttribute pa = it.next();
		Assert.assertEquals(LocalPrefPathAttribute.class, pa.getClass());
		Assert.assertEquals(100, ((LocalPrefPathAttribute)pa).getLocalPreference());

		pa = it.next();
		Assert.assertEquals(MultiExitDiscPathAttribute.class, pa.getClass());
		Assert.assertEquals(1, ((MultiExitDiscPathAttribute)pa).getDiscriminator());
	}

}
