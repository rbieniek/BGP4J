/**
 * 
 */
package org.bgp4j.config.nodes.impl;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.bgp4j.config.nodes.AddressFamilyRoutingConfiguration;
import org.bgp4j.config.nodes.RoutingConfiguration;
import org.bgp4j.net.AddressFamilyKey;

/**
 * @author rainer
 *
 */
@Singleton
public class RoutingConfigurationParser {

	private @Inject AddressFamilyRoutingConfigurationParser afParser;
	
	public RoutingConfiguration parseConfiguration(HierarchicalConfiguration config) throws ConfigurationException {
		RoutingConfigurationImpl result = new RoutingConfigurationImpl();
		Set<AddressFamilyKey> keys = new HashSet<AddressFamilyKey>();

		for(HierarchicalConfiguration afConfig: config.configurationsAt("AddressFamily")) {
			AddressFamilyRoutingConfiguration afRouting = afParser.parseConfiguration(afConfig);
			
			if(keys.contains(afRouting.getKey()))
				throw new ConfigurationException("Duplicate address family: " + afRouting.getKey());
			
			result.getRoutingConfigurations().add(afRouting);
			keys.add(afRouting.getKey());
		}
		
		return result;
	}
}
