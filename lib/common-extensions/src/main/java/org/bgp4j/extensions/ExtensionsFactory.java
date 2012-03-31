/**
 * 
 */
package org.bgp4j.extensions;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;
import javax.inject.Singleton;


/**
 * @author rainer
 *
 */
@Singleton
public class ExtensionsFactory {

	@Inject private ExtensionsLoader loader;
	
	private Map<String, Extension> extensions = null;
	
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
			loaded.put(extension.getName(), extension);
		}
		
		this.extensions = loaded;
	}
}
