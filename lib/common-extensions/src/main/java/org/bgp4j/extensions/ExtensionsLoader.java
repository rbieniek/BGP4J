/**
 * 
 */
package org.bgp4j.extensions;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * Wrapper around java.util.ServiceLoader to load all available extensions from the class path
 * 
 * @author rainer
 *
 */
public class ExtensionsLoader {

	/**
	 * 
	 */
	List<Extension> loadExtensions() {
		List<Extension> result = new LinkedList<Extension>();
		
		ServiceLoader<Extension> loader = ServiceLoader.load(Extension.class);
		Iterator<Extension> loaded = loader.iterator();
		
		while(loaded.hasNext())
			result.add(loaded.next());
		
		return result;
	}
}
