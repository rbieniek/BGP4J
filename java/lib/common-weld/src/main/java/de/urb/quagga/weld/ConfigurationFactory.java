/**
 * 
 */
package de.urb.quagga.weld;

import javax.enterprise.inject.Produces;

/**
 * @author rainer
 *
 */
public class ConfigurationFactory {
	@Produces Configuration getConfiguration(ConfigurationProducer producer) {
		return producer.getConfiguration();
	}
}
