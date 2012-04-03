/**
 * 
 */
package org.bgp4j.config.nodes.impl;

import java.util.Collection;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.bgp4j.extensions.Extension;
import org.bgp4j.extensions.ExtensionBase;
import org.bgp4j.extensions.ExtensionBeanFactory;
import org.bgp4j.extensions.ProvidedRIBs;

/**
 * @author rainer
 *
 */
public class TestExtension extends ExtensionBase implements Extension {

	private boolean configured;
	
	/* (non-Javadoc)
	 * @see org.bgp4j.extensions.Extension#getName()
	 */
	@Override
	public String getName() {
		return "test";
	}

	/* (non-Javadoc)
	 * @see org.bgp4j.extensions.Extension#configure(org.apache.commons.configuration.HierarchicalConfiguration)
	 */
	@Override
	public void configure(HierarchicalConfiguration config)	throws ConfigurationException {
		if(config.getInt("Foo[@bar]") != 1)
			throw new ConfigurationException("bar@Foo not found");
		
		configured = true;
	}

	/* (non-Javadoc)
	 * @see org.bgp4j.extensions.Extension#start()
	 */
	@Override
	public void startExtension() throws Exception {
	}

	/* (non-Javadoc)
	 * @see org.bgp4j.extensions.Extension#stop()
	 */
	@Override
	public void stopExtension() throws Exception {
	}

	/* (non-Javadoc)
	 * @see org.bgp4j.extensions.Extension#getProvidedRIBs()
	 */
	@Override
	public Collection<ProvidedRIBs> getProvidedRIBs() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isConfigured() {
		return configured;
	}

	@Override
	public boolean isReadyForService() {
		return true;
	}

	@Override
	public void initialize(ExtensionBeanFactory beanFactory) {
		// TODO Auto-generated method stub
		
	}


}
