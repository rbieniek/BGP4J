/**
 * 
 */
package org.bgp4j.extension.snmp4j.config.nodes.impl;

import org.bgp4j.extension.snmp4j.config.nodes.SnmpConfiguration;
import org.bgp4j.extension.snmp4j.config.nodes.SnmpConfigurationLocalPortDecorator;

/**
 * @author rainer
 *
 */
public class SnmpConfigurationDefaultLocalPortDecorator extends	SnmpConfigurationLocalPortDecorator {

	public SnmpConfigurationDefaultLocalPortDecorator(SnmpConfiguration decorated) {
		super(decorated);
	}
	
	/* (non-Javadoc)
	 * @see org.bgp4j.extension.snmp4j.config.nodes.SnmpConfigurationLocalPortDecorator#getDefaultLocalPort()
	 */
	@Override
	protected int getDefaultLocalPort() {
		return 161;
	}

}
