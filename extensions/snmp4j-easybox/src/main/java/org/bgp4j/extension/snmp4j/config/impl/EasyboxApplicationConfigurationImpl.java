/**
 * 
 */
package org.bgp4j.extension.snmp4j.config.impl;

import java.util.LinkedList;
import java.util.List;

import org.bgp4j.config.nodes.HttpServerConfiguration;
import org.bgp4j.extension.snmp4j.config.EasyboxApplicationConfiguration;
import org.bgp4j.extension.snmp4j.config.nodes.EasyboxConfiguration;

/**
 * @author rainer
 *
 */
public class EasyboxApplicationConfigurationImpl implements	EasyboxApplicationConfiguration {

	private List<EasyboxConfiguration> easyboxes = new LinkedList<EasyboxConfiguration>();
	private HttpServerConfiguration httpServer;
	
	@Override
	public List<EasyboxConfiguration> getEasyboxes() {
		return this.easyboxes;
	}

	@Override
	public HttpServerConfiguration getHttpServer() {
		return httpServer;
	}

	/**
	 * @param easyboxes the easyboxes to set
	 */
	void setEasyboxes(List<EasyboxConfiguration> easyboxes) {
		this.easyboxes = easyboxes;
	}

	/**
	 * @param httpServer the httpServer to set
	 */
	void setHttpServer(HttpServerConfiguration httpServer) {
		this.httpServer = httpServer;
	}
}
