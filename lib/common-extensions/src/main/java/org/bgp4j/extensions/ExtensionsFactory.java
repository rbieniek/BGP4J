/**
 * 
 */
package org.bgp4j.extensions;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.bgp4j.weld.ApplicationBootstrapEvent;
import org.jboss.weld.environment.se.WeldContainer;


/**
 * @author rainer
 *
 */
@Singleton
public class ExtensionsFactory {

	private class ExtensionBeanFactoryImpl implements ExtensionBeanFactory {

		private WeldContainer weldContainer;
		
		private ExtensionBeanFactoryImpl(WeldContainer weldContainer) {
			this.weldContainer = weldContainer;
		}
		
		@Override
		public <T> T getBeanInstance(Class<T> beanClass) {
			return weldContainer.instance().select(beanClass).get();
		}
		
		@Override
		public <T> T getBeanInstance(Class<T> beanClass, Annotation... annotations) {
			return weldContainer.instance().select(beanClass, annotations).get();
		}

	}
	
	@Inject private ExtensionsLoader loader;
	private ExtensionBeanFactory beanFactory;
	
	private Map<String, Extension> extensions = null;
	
	public void applicationStarted(@Observes ApplicationBootstrapEvent event) {
		this.beanFactory = new ExtensionBeanFactoryImpl(event.getWeldContainer());
	}
	
	public Extension getExtensionByName(String name) {
		loadExtensions();
		
		return extensions.get(name);
	}
	
	public List<Extension> listExtensions() {
		loadExtensions();

		List<Extension> list = new LinkedList<Extension>();
		
		for(Entry<String, Extension> entry : extensions.entrySet()) 
			list.add(entry.getValue());
		
		return list;
	}
	
	private void loadExtensions() {
		if(extensions != null)
			return;
		
		Map<String, Extension> loaded = new HashMap<String, Extension>();
		
		for(Extension extension : loader.loadExtensions()) {
			extension.initialize(beanFactory);
			
			loaded.put(extension.getName(), extension);
		}
		
		this.extensions = loaded;
	}
}
