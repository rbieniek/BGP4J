/**
 *  Copyright 2012 Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
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
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
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
		weldContainer.event().select(ApplicationBootstrapEvent.class).fire(new ApplicationBootstrapEvent());
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
		
	protected <T> T obtainInstance(final Class<T> clazz) {
		return weldContainer.instance().select(clazz).get();
	}
}
