/**
 * 
 */
package org.bgp4j.extension.snmp4j.service;

/**
 * @author rainer
 *
 */
public interface EasyboxInstance {
	/**
	 * 
	 * @return
	 */
	public String getName();

	/**
	 * get easybox uptime
	 * 
	 * @return the uptime in milliseconds or -1 if the box is not up
	 */
	public long getUptime();
}
