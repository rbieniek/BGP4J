/**
 * 
 */
package org.bgp4j.extension.snmp4j.config.nodes;

import java.net.InetAddress;

/**
 * @author rainer
 *
 */
public abstract class SnmpConfigurationLocalPortDecorator implements SnmpConfiguration {

	private SnmpConfiguration decorated;

	protected SnmpConfigurationLocalPortDecorator(SnmpConfiguration decorated) {
		this.decorated = decorated;
	}
	
	/**
	 * @return
	 * @see org.bgp4j.extension.snmp4j.config.nodes.SnmpConfiguration#getTargetAddress()
	 */
	public InetAddress getTargetAddress() {
		return decorated.getTargetAddress();
	}

	/**
	 * @return
	 * @see org.bgp4j.extension.snmp4j.config.nodes.SnmpConfiguration#getLocalPort()
	 */
	public int getLocalPort() {
		int port = decorated.getLocalPort();
		
		if(port == 0)
			port = getDefaultLocalPort();
		
		return port;
	}

	protected abstract int getDefaultLocalPort();
	
	/**
	 * @return
	 * @see org.bgp4j.extension.snmp4j.config.nodes.SnmpConfiguration#getCommunity()
	 */
	public String getCommunity() {
		return decorated.getCommunity();
	}
	
	
}
