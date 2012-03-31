/**
 * 
 */
package org.bgp4.config.nodes.impl;

import junit.framework.Assert;

import org.apache.commons.configuration.XMLConfiguration;
import org.bgp4.config.ConfigTestBase;
import org.bgp4j.extensions.ExtensionsFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author rainer
 *
 */
public class ExtensionsConfigurationParserTest extends ConfigTestBase {

	@Before
	public void before() throws Exception {
		this.config = loadConfiguration("config/nodes/ExtensionsConfig.xml");
		this.parser = obtainInstance(ExtensionsConfigurationParser.class);
		this.factory = obtainInstance(ExtensionsFactory.class);
	}
	
	@After
	public void after() {
		this.config = null;
		this.parser = null;
		this.factory = null;
	}
	
	private XMLConfiguration config;
	private ExtensionsConfigurationParser parser;
	private ExtensionsFactory factory;

	@Test
	public void testParseConfiguration() throws Exception {
		parser.parseConfiguration(config.configurationsAt("Extensions.Extension"));

		Assert.assertTrue(((TestExtension)factory.getExtensionByName("test")).isConfigured());
	}
}
