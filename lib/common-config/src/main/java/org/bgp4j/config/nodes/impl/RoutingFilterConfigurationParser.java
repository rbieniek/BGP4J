/**
 * 
 */
package org.bgp4j.config.nodes.impl;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.bgp4j.config.nodes.RoutingFilterConfiguration;

/**
 * @author rainer
 *
 */
@Singleton
public class RoutingFilterConfigurationParser {

	private @Inject NetworkLayerReachabilityParser nlriParser;

	RoutingFilterConfiguration parseConfiguration(HierarchicalConfiguration config) throws ConfigurationException {
		List<HierarchicalConfiguration> prefixList = config.configurationsAt("Prefixes");
		RoutingFilterConfigurationImpl rfc = null;
	
		if(prefixList.size() > 1)
			throw new ConfigurationException("more then one subnode specified");
		
		if(prefixList.size() == 1)
			rfc = parsePrefixFilter(prefixList.get(0));
		
		if(rfc == null)
			throw new ConfigurationException("no filter type specified");

		rfc.setName(config.getString("[@name]", ""));
		
		return rfc;
	}
	
	private RoutingFilterConfigurationImpl parsePrefixFilter(HierarchicalConfiguration config) throws ConfigurationException {
		PrefixRoutingFilterConfigurationImpl prfc = new PrefixRoutingFilterConfigurationImpl();
		
		for(HierarchicalConfiguration subConfig : config.configurationsAt("Prefix")) {
			String rep = subConfig.getString("[@value]");
			
			if(StringUtils.isBlank(rep))
				throw new ConfigurationException("empty prefix specified");
			
			prfc.getFilterPrefixes().add(nlriParser.parseNlri(rep));
		}
		
		return prfc;
	}
}
