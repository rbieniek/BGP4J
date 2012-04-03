/**
 * 
 */
package org.bgp4j.extension.snmp4j.config.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.bgp4j.config.ConfigurationParser;
import org.bgp4j.extension.snmp4j.config.EasyboxApplicationConfiguration;
import org.bgp4j.extension.snmp4j.config.nodes.EasyboxConfiguration;
import org.bgp4j.extension.snmp4j.config.nodes.impl.EasyBoxConfigurationParser;

/**
 * @author rainer
 *
 */
public class EasyboxApplicationConfigurationParser {

	private @Inject EasyBoxConfigurationParser easyboxParser;
	private @Inject ConfigurationParser configParser;
	
	public EasyboxApplicationConfiguration parseConfiguration(HierarchicalConfiguration config) throws ConfigurationException {
		EasyboxApplicationConfigurationImpl impl = new EasyboxApplicationConfigurationImpl();
		List<HierarchicalConfiguration> httpServerConfigs = config.configurationsAt("HttpServer");
		
		Set<String> keys = new HashSet<String>();
		
		for(HierarchicalConfiguration subConfig : config.configurationsAt("Easybox")) {
			EasyboxConfiguration ebc = easyboxParser.parseConfguration(subConfig);
			
			if(keys.contains(ebc.getName()))
				throw new ConfigurationException("duplicate Easybox " + ebc.getName());
			
			impl.getEasyboxes().add(ebc);
			keys.add(ebc.getName());
		}

		if(httpServerConfigs.size() == 1)
			impl.setHttpServer(configParser.parseHttpServerConfiguration(httpServerConfigs.get(0)));
		else
			throw new ConfigurationException("HttpServer configuration missing or duplicated");
		
		return impl;
	}
}
