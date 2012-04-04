/**
 * 
 */
package org.bgp4j.config.nodes.impl;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.inject.Singleton;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.bgp4j.config.nodes.PathAttributeConfiguration;
import org.bgp4j.net.Origin;
import org.bgp4j.net.attributes.LocalPrefPathAttribute;
import org.bgp4j.net.attributes.MultiExitDiscPathAttribute;
import org.bgp4j.net.attributes.OriginPathAttribute;

/**
 * @author rainer
 *
 */
@Singleton
public class PathAttributeConfigurationParser {

	PathAttributeConfiguration parseConfiguration(HierarchicalConfiguration config) throws ConfigurationException {
		PathAttributeConfigurationImpl result = new PathAttributeConfigurationImpl();
		
		for(String key : stripKeys(config.getKeys())) {
			parseConfiguration(config.configurationsAt(key), key, result);
		}
		
		return result;
	}

	
	private void parseConfiguration(List<HierarchicalConfiguration> configurations, String key, PathAttributeConfigurationImpl result) throws ConfigurationException {
		if(StringUtils.equals(key, "LocalPreference")) {
			parseLocalPreference(first(configurations, key), result, key);
		} else if(StringUtils.equals(key, "MultiExitDisc")) {
			parseMultiExitDiscPreference(first(configurations, key), result, key);			
		} else if(StringUtils.equals(key, "Origin")) {
			parseOrigin(first(configurations, key), result, key);			
		} else
			throw new ConfigurationException("Unknown path attribute: " + key);
	}


	private void parseMultiExitDiscPreference(HierarchicalConfiguration config, PathAttributeConfigurationImpl result, String key) throws ConfigurationException {
		result.getAttributes().add(new MultiExitDiscPathAttribute(parseValue(config, key)));
	}


	private void parseLocalPreference(HierarchicalConfiguration config, PathAttributeConfigurationImpl result, String key) throws ConfigurationException {
		result.getAttributes().add(new LocalPrefPathAttribute(parseValue(config, key)));
	}

	private void parseOrigin(HierarchicalConfiguration config, PathAttributeConfigurationImpl result, String key) throws ConfigurationException {
		try {
			result.getAttributes().add(new OriginPathAttribute(Origin.fromString(config.getString("[@value]"))));			
		} catch(IllegalArgumentException e) {
			throw new ConfigurationException(e);
		}
	}


	private HierarchicalConfiguration first(List<HierarchicalConfiguration> configurations, String key) throws ConfigurationException {
		if(configurations.size() == 0)
			return null;
		else if(configurations.size() > 1)
			throw new ConfigurationException("duplicate element: " + key);
		else
			return configurations.get(0);
	}
	
	private int parseValue(HierarchicalConfiguration config, String key) throws ConfigurationException {
		int value = config.getInt("[@value]", -1);
		
		if(value < 0)
			throw new ConfigurationException("Invalid or missing value given for key " + key);
		
		return value;
	}
	
	private Set<String> stripKeys(Iterator<String> keys) {
		Set<String> cleaned = new HashSet<String>();
		
		while(keys.hasNext()) {
			String key = keys.next();
			
			if(StringUtils.contains(key, "[@")) {
				int index = StringUtils.indexOf(key, "[@");
				
				key = StringUtils.substring(key, 0, index);
			}
			
			cleaned.add(key);
		}
		
		return cleaned;
	}
}
