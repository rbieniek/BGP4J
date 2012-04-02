/**
 * 
 */
package org.bgp4j.extension.snmp4j.config.nodes;

import org.bgp4.config.nodes.RoutingConfiguration;

/**
 * @author rainer
 *
 */
public interface EasyboxConfiguration {

	/**
	 * get the symbolic name of this easybox
	 * 
	 * @return
	 */
	public String getName();
	
	/**
	 * get the SNMP configuration
	 * 
	 * @return
	 */
	public SnmpConfiguration getSnmpConfiguration();
	
	/**
	 * get the interface MAC address. It is needed to identify the external transport interface of the easybox
	 * 
	 * @return
	 */
	public String getInterfaceMacAddress();
	
	/**
	 * get the static routing configuration advertised for the Easybox
	 */
	public RoutingConfiguration getRoutingConfiguration();
}
