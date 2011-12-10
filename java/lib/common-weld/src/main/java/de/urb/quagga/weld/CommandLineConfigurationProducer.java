/**
 * 
 */
package de.urb.quagga.weld;

import javax.inject.Inject;

import org.jboss.weld.environment.se.bindings.Parameters;

/**
 * @author rainer
 *
 */
public class CommandLineConfigurationProducer implements ConfigurationProducer {

	@Inject @Parameters private String[] commandLine;
	
	/* (non-Javadoc)
	 * @see de.urb.quagga.weld.ConfigurationProducer#getConfiguration()
	 */
	@Override
	public Configuration getConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

}
