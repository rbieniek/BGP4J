/**
 * 
 */
package org.bgp4j.extensions;

import java.lang.annotation.Annotation;

/**
 * @author rainer
 *
 */
public interface ExtensionBeanFactory {

	/**
	 * Create a bean of a certain type further defined by the given annotations
	 * 
	 * @param beanClass
	 * @param annotations
	 * @return
	 */
	public <T> T getBeanInstance(Class<T> beanClass, Annotation...annotations);

	/**
	 * Create a bean of a certain type
	 * @param beanClass
	 * @return
	 */
	public <T> T getBeanInstance(Class<T> beanClass);
}
