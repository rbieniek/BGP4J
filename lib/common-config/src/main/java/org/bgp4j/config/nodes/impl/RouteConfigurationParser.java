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
import org.bgp4j.config.nodes.PathAttributeConfiguration;
import org.bgp4j.config.nodes.RouteConfiguration;

/**
 * @author rainer
 *
 */
@Singleton
public class RouteConfigurationParser {
	private @Inject PathAttributeConfigurationParser pathAttrParser;
	private @Inject NetworkLayerReachabilityParser nlriParser;
	
	public RouteConfiguration parseConfiguration(HierarchicalConfiguration config) throws ConfigurationException {
		String prefix = config.getString("Prefix[@value]");
		List<HierarchicalConfiguration> pa = config.configurationsAt("PathAttributes");
		
		if(StringUtils.isBlank(prefix))
			throw new ConfigurationException("NLRI missing");
		if(pa.size() > 1)
			throw new ConfigurationException("PathAttributes missing of given multiple times");
		else {
			PathAttributeConfiguration pac = new PathAttributeConfigurationImpl();
			
			if(pa.size() == 1)
				pac = pathAttrParser.parseConfiguration(pa.get(0));
				
			return new RouteConfigurationImpl(nlriParser.parseNlri(prefix), pac);			
		}
	}
	
}
