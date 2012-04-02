/**
 * 
 */
package org.bgp4j.config.nodes;

import java.util.Set;

import org.bgp4j.net.attributes.PathAttribute;

/**
 * @author rainer
 *
 */
public interface PathAttributeConfiguration extends Comparable<PathAttributeConfiguration>{

	/**
	 * 
	 * @return
	 */
	public Set<PathAttribute> getAttributes();
}
