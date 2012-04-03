/**
 * 
 */
package org.bgp4j.extension.snmp4j.config.nodes.impl;

import java.util.List;

import javax.inject.Inject;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.bgp4j.config.nodes.impl.RoutingConfigurationParser;
import org.bgp4j.extension.snmp4j.config.nodes.EasyboxConfiguration;

/**
 * @author rainer
 *
 */
public class EasyBoxConfigurationParser {

	private @Inject SnmpConfigurationParser snmpConfigParser;
	private @Inject RoutingConfigurationParser routingConfigParser;
	
	public EasyboxConfiguration parseConfguration(HierarchicalConfiguration config) throws ConfigurationException {
		EasyBoxConfigrationImpl ebc = new EasyBoxConfigrationImpl();
		
		ebc.setName(config.getString("[@name]"));
		if(StringUtils.isBlank(ebc.getName()))
			throw new ConfigurationException("Name missing");

		ebc.setInterfaceMacAddress(config.getString("[@interfaceMac]"));
		if(StringUtils.isBlank(ebc.getInterfaceMacAddress()))
			throw new ConfigurationException("Interface MAC address missing");

		ebc.setSnmpConfiguration(
				new SnmpConfigurationDefaultLocalPortDecorator(
						snmpConfigParser.parseConfiguration(
								first(config.configurationsAt("Snmp"), "Snmp"))));
		ebc.setRoutingConfiguration(routingConfigParser.parseConfiguration(first(config.configurationsAt("Routing"), "Routing")));
		
		return ebc;
	}
	
	private HierarchicalConfiguration first(List<HierarchicalConfiguration> configs, String key) throws ConfigurationException {
		if(configs.size() == 0)
			throw new ConfigurationException("Missing entry: " + key);
		else if(configs.size() > 1)
			throw new ConfigurationException("Duplicate entry: " + key);
		else
			return configs.get(0);
	}
}
