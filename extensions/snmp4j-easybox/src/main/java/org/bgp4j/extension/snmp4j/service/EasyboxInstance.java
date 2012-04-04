/**
 * 
 */
package org.bgp4j.extension.snmp4j.service;

import java.util.List;

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
	
	/**
	 * 
	 * @return
	 */
	public List<EasyboxInterface> getInterfaces();
}
