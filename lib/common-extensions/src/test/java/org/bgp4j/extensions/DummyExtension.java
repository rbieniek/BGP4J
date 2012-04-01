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

	private boolean readyForService;
	
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
	public void startExtension() throws Exception {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.bgp4j.extensions.Extension#stop()
	 */
	@Override
	public void stopExtension() throws Exception {
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

	@Override
	public boolean isReadyForService() {
		return readyForService;
	}

	@Override
	public void initialize(ExtensionBeanFactory beanFactory) {
		readyForService = (beanFactory.getBeanInstance(DummyExtensionBean.class) != null);
	}

}
