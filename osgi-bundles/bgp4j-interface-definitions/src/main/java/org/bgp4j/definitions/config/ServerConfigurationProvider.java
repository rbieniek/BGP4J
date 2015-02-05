/**
 * 
 */
package org.bgp4j.definitions.config;

import java.net.InetSocketAddress;
import java.util.Collection;

/**
 * @author rainer
 *
 */
public interface ServerConfigurationProvider {
	/**
	 * 
	 * @return
	 */
	Collection<InetSocketAddress> bindAddresses();
}
