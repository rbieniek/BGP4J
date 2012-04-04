/**
 * 
 */
package org.bgp4j.extension.snmp4j.service;

/**
 * @author rainer
 *
 */
public interface EasyboxInterfaceListener {

	public void interfaceChanged(EasyboxInstance instance, EasyboxInterfaceEvent event);
}
