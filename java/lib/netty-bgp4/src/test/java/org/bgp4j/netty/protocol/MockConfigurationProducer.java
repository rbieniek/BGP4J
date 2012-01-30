/**
 * 
 */
package org.bgp4j.netty.protocol;

import org.bgp4j.weld.Configuration;
import org.bgp4j.weld.ConfigurationProducer;

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
