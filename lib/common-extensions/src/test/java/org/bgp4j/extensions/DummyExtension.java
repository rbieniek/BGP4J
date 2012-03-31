/**
 * 
 */
package org.bgp4j.extensions;

import java.util.Collection;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;

/**
 * @author rainer
 *
 */
public class DummyExtension implements Extension {

	/* (non-Javadoc)
	 * @see org.bgp4j.extensions.Extension#getName()
	 */
	@Override
	public String getName() {
		return "dummy";
	}

	/* (non-Javadoc)
	 * @see org.bgp4j.extensions.Extension#configure(org.apache.commons.configuration.HierarchicalConfiguration)
	 */
	@Override
	public void configure(HierarchicalConfiguration config)
			throws ConfigurationException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.bgp4j.extensions.Extension#start()
	 */
	@Override
	public void start() throws Exception {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.bgp4j.extensions.Extension#stop()
	 */
	@Override
	public void stop() throws Exception {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.bgp4j.extensions.Extension#getProvidedRIBs()
	 */
	@Override
	public Collection<ProvidedRIBs> getProvidedRIBs() {
		// TODO Auto-generated method stub
		return null;
	}

}
