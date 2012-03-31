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
public class ExtensionsLoaderTest extends WeldTestCaseBase {

	@Before
	public void before() {
		loader = obtainInstance(ExtensionsLoader.class);
	}
	
	@After
	public void after() {
		loader = null;
	}
	
	private ExtensionsLoader loader;
	
	@Test
	public void testLoadExtension() {
		List<Extension> extensions = loader.loadExtensions();
		
		Assert.assertEquals(1, extensions.size());
		Assert.assertEquals(DummyExtension.class, extensions.get(0).getClass());
	}
}
