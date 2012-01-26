/**
 * 
 */
package org.bgp4j.weld;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

/**
 * @author rainer
 *
 */
@ApplicationScoped
public class ConfigurationFactory {
	@Config @Produces Configuration getConfiguration(ConfigurationProducer producer) throws Exception {
		return producer.getConfiguration();
	}
}
