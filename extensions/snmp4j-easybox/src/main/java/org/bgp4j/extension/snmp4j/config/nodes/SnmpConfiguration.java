/**
 * 
 */
package org.bgp4j.extension.snmp4j.config.nodes;

import java.net.InetAddress;

/**
 * @author rainer
 *
 */
public interface SnmpConfiguration {

	/**
	 * get the IP address
	 * 
	 * @return
	 */
	public InetAddress getAddress();
	
	/**
	 * get the community
	 * 
	 * @return
	 */
	public String getCommunity();
}
