/**
 * 
 */
package org.bgp4j.weld;

import javax.enterprise.inject.spi.BeanManager;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 * @author rainer
 *
 */
public class WeldTestCaseBase {
	
	private static Weld weld;
	private static WeldContainer weldContainer;
	
	private BeanManager beanManager;
	
	@BeforeClass
	public static void setupWeldContainer() {
		weld = new Weld();
		
		weldContainer = weld.initialize();
	}
	
	@AfterClass
	public static void teardownWeldContainer() {
		weld.shutdown();
	}
	
	@Before
	public void setupBeanManager() {
		beanManager = weldContainer.getBeanManager();
	}

	/**
	 * @return the beanManager
	 */
	protected BeanManager getBeanManager() {
		return beanManager;
	}
	
	protected <T> T obtainInstance(Class<T> clazz) {
		return weldContainer.instance().select(clazz).get();
	}
}
