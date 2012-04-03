/**
 * 
 */
package org.bgp4j.extensions;

import java.util.Set;

/**
 * @author rainer
 *
 */
public abstract class ExtensionBase implements Extension {

	/* (non-Javadoc)
	 * @see org.bgp4j.extensions.Extension#getManagementObjects()
	 */
	@Override
	public Set<Object> getManagementObjects() {
		return null;
	}

}
