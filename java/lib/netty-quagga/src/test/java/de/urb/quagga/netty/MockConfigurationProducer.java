/**
 * 
 */
package de.urb.quagga.netty;

import de.urb.quagga.weld.Configuration;
import de.urb.quagga.weld.ConfigurationProducer;

/**
 * @author rainer
 *
 */
public class MockConfigurationProducer implements ConfigurationProducer {

	/* (non-Javadoc)
	 * @see de.urb.quagga.weld.ConfigurationProducer#getConfiguration()
	 */
	@Override
	public Configuration getConfiguration() throws Exception {
		return new Configuration();
	}

}
