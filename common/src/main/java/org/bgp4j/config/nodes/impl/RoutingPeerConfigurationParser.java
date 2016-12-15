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
import org.apache.commons.lang3.StringUtils;
import org.bgp4j.config.nodes.AddressFamilyRoutingPeerConfiguration;
import org.bgp4j.config.nodes.RoutingPeerConfiguration;
import org.bgp4j.net.AddressFamilyKey;

/**
 * @author rainer
 *
 */
@Singleton
class RoutingPeerConfigurationParser {

	private @Inject AddressFamilyRoutingPeerConfigurationParser peerParser;
	
	RoutingPeerConfiguration parseConfiguration(HierarchicalConfiguration config) throws ConfigurationException {
		RoutingPeerConfigurationImpl result = new RoutingPeerConfigurationImpl();
		Set<AddressFamilyKey> afks = new HashSet<AddressFamilyKey>();
		String peerName = config.getString("[@peerName]");
		String extension = config.getString("[@extension]");
		String key = config.getString("[@key]");
		
		if(StringUtils.isBlank(peerName)) {			
			if(StringUtils.isBlank(extension) || StringUtils.isBlank(key))
				throw new ConfigurationException("Either peerName or extension and key attributes are required");
			
			peerName = extension + "_" + key;
		} else if(StringUtils.isNotBlank(extension) || StringUtils.isNotBlank(key))
			throw new ConfigurationException("Either peerName or extension and key attributes are required");

		result.setPeerName(peerName);
		
		for(HierarchicalConfiguration subConfig : config.configurationsAt("Routing")) {
			AddressFamilyRoutingPeerConfiguration afprc = peerParser.parseConfiguration(subConfig);
			
			if(afks.contains(afprc.getAddressFamilyKey()))
				throw new ConfigurationException("Duplicated configuration for address family: " + afprc.getAddressFamilyKey());
			
			result.getAddressFamilyConfigrations().add(afprc);
			afks.add(afprc.getAddressFamilyKey());
		}
		
		return result;
	}
}
