/**
 * 
 */
package org.bgp4j.extension.snmp4j.service;

/**
 * @author rainer
 *
 */
public interface EasyboxInterfaceEvent {
	/**
	 * get the interface that dropped from active state
	 * 
	 * @return
	 */
	public EasyboxInterface getDroppedInterface();
	
	/**
	 * get the interface that went into service
	 * 
	 * @return
	 */
	public EasyboxInterface getCurrentInterface();
}
