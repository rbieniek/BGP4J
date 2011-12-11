/**
 * 
 */
package de.urb.quagga.weld;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

/**
 * @author rainer
 *
 */
@ApplicationScoped
@Singleton
public class ConfigurationFactory {
	@Produces Configuration getConfiguration(ConfigurationProducer producer) throws Exception {
		return producer.getConfiguration();
	}
}
