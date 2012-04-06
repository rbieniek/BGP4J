/**
 * 
 */
package org.bgp4j.config.nodes.impl;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.bgp4j.config.nodes.RoutingInstanceConfiguration;

/**
 * @author rainer
 *
 */
@Singleton
public class RoutingInstanceConfurationParser {

	private @Inject RoutingPeerConfigurationParser peerParser;
	
	RoutingInstanceConfiguration parseConfiguration(HierarchicalConfiguration config) throws ConfigurationException {
		RoutingInstanceConfigurationImpl result = new RoutingInstanceConfigurationImpl();
		HierarchicalConfiguration firstConfig = config.configurationAt("First");		
		HierarchicalConfiguration secondConfig = config.configurationAt("Second");		
		
		if(firstConfig == null)
			throw new ConfigurationException("First peer configuration required");
		if(secondConfig == null)
			throw new ConfigurationException("Second peer configuration required");

		result.setFirstPeer(peerParser.parseConfiguration(firstConfig));
		result.setSecondPeer(peerParser.parseConfiguration(secondConfig));
		
		return result;
	}
}
