/**
 * 
 */
package org.bgp4j.extension.snmp4j.config;

import java.util.List;

import org.bgp4j.config.nodes.HttpServerConfiguration;
import org.bgp4j.extension.snmp4j.config.nodes.EasyboxConfiguration;

/**
 * @author rainer
 *
 */
public interface EasyboxApplicationConfiguration {
	/**
	 * 
	 * @return
	 */
	public List<EasyboxConfiguration> getEasyboxes();

	/**
	 * 
	 * @return
	 */
	public HttpServerConfiguration getHttpServer();
}
