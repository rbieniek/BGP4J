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
import org.bgp4j.config.nodes.RoutingInstanceConfiguration;
import org.bgp4j.config.nodes.RoutingProcessorConfiguration;

/**
 * @author rainer
 *
 */
@Singleton
public class RoutingProcessorConfigurationParser {

	private @Inject RoutingInstanceConfurationParser instanceParser;
	
	public RoutingProcessorConfiguration parseConfigration(HierarchicalConfiguration config) throws ConfigurationException {
		RoutingProcessorConfigurationImpl result = new RoutingProcessorConfigurationImpl();
		Set<String> instanceKeys = new HashSet<String>();
		
		for(HierarchicalConfiguration subConfig : config.configurationsAt("RoutingInstance")) {
			RoutingInstanceConfiguration instance = instanceParser.parseConfiguration(subConfig);
			String key = instance.getFirstPeer().getPeerName() + "/" + instance.getSecondPeer().getPeerName();
			
			if(instanceKeys.contains(key))
				throw new ConfigurationException("Duplicate confugration, first: " 
						+ instance.getFirstPeer().getPeerName() + ", second: "
						+  instance.getSecondPeer().getPeerName());
			
			result.getRoutingInstances().add(instance);
			instanceKeys.add(key);
		}
		
		return result;
	}
}
