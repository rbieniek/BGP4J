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
	 * get all known interfaces
	 * 
	 * @return
	 */
	public List<EasyboxInterface> getInterfaces();

	/**
	 * return the active interface instance
	 * 
	 * @return
	 */
	public EasyboxInterface getActiveInterface();
	
	/**
	 * 
	 * @param listener
	 */
	public void addInterfaceListener(EasyboxInterfaceListener listener);

	
	/**
	 * 
	 * @param listener
	 */
	public void removeInterfaceListener(EasyboxInterfaceListener listener);
}
