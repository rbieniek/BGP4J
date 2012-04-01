/**
 * 
 */
package org.bgp4j.extensions;

import java.util.List;

import junit.framework.Assert;

import org.bgp4j.weld.WeldTestCaseBase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author rainer
 *
 */
public class ExtensionsFactoryTest extends WeldTestCaseBase {

	@Before
	public void before() {
		factory = obtainInstance(ExtensionsFactory.class);
	}
	
	@After
	public void after() {
		factory = null;
	}
	
	private ExtensionsFactory factory;
	
	@Test
	public void testListExtensions() {
		List<Extension> extensions = factory.listExtensions();
		
		Assert.assertEquals(1, extensions.size());
		Assert.assertEquals(DummyExtension.class, extensions.get(0).getClass());
	}

	@Test
	public void testGetExtensionByName() {
		Extension ext;
		
		ext = factory.getExtensionByName("dummy");

		Assert.assertNotNull(ext);
		Assert.assertEquals(DummyExtension.class, ext.getClass());
		Assert.assertTrue(((DummyExtension)ext).isReadyForService());
		
		Assert.assertNull(factory.getExtensionByName("foo"));
	}
}
