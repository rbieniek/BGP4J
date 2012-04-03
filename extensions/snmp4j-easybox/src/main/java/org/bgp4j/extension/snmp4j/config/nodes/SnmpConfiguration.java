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
	public InetAddress getTargetAddress();
	
	/**
	 * get the local binding port
	 * 
	 * @return
	 */
	public int getLocalPort();
	
	/**
	 * get the community
	 * 
	 * @return
	 */
	public String getCommunity();
}
