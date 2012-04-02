/**
 * 
 */
package org.bgp4j.extension.snmp4j.config.nodes.impl;

import org.bgp4j.config.nodes.RoutingConfiguration;
import org.bgp4j.extension.snmp4j.config.nodes.EasyboxConfiguration;
import org.bgp4j.extension.snmp4j.config.nodes.SnmpConfiguration;

/**
 * @author rainer
 *
 */
public class EasyBoxConfigrationImpl implements EasyboxConfiguration {

	private String name;
	private SnmpConfiguration snmpConfiguration;
	private String interfaceMacAddress;
	private RoutingConfiguration routingConfiguration;
	
	/* (non-Javadoc)
	 * @see org.bgp4j.extension.snmp4j.config.nodes.EasyboxConfiguration#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see org.bgp4j.extension.snmp4j.config.nodes.EasyboxConfiguration#getSnmpConfiguration()
	 */
	@Override
	public SnmpConfiguration getSnmpConfiguration() {
		return snmpConfiguration;
	}

	/* (non-Javadoc)
	 * @see org.bgp4j.extension.snmp4j.config.nodes.EasyboxConfiguration#getInterfaceMacAddress()
	 */
	@Override
	public String getInterfaceMacAddress() {
		return interfaceMacAddress;
	}

	/* (non-Javadoc)
	 * @see org.bgp4j.extension.snmp4j.config.nodes.EasyboxConfiguration#getRoutingConfiguration()
	 */
	@Override
	public RoutingConfiguration getRoutingConfiguration() {
		return routingConfiguration;
	}

	/**
	 * @param name the name to set
	 */
	void setName(String name) {
		this.name = name;
	}

	/**
	 * @param snmpConfiguration the snmpConfiguration to set
	 */
	void setSnmpConfiguration(SnmpConfiguration snmpConfiguration) {
		this.snmpConfiguration = snmpConfiguration;
	}

	/**
	 * @param interfaceMacAddress the interfaceMacAddress to set
	 */
	void setInterfaceMacAddress(String interfaceMacAddress) {
		this.interfaceMacAddress = interfaceMacAddress;
	}

	/**
	 * @param routeingConfiguration the routeingConfiguration to set
	 */
	void setRoutingConfiguration(RoutingConfiguration routingConfiguration) {
		this.routingConfiguration = routingConfiguration;
	}

}
