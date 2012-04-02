/**
 * 
 */
package org.bgp4j.config.nodes.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.bgp4j.extensions.Extension;
import org.bgp4j.extensions.ExtensionsFactory;

/**
 * @author rainer
 *
 */
public class ExtensionsConfigurationParser {

	private @Inject ExtensionsFactory factory;
	
	public void parseConfiguration(List<HierarchicalConfiguration> extensionNodes) throws ConfigurationException {
		Set<String> processedExtensions = new HashSet<String>();
		
		for(HierarchicalConfiguration config : extensionNodes)
			parseConfiguration(config, processedExtensions);
	}

	private void parseConfiguration(HierarchicalConfiguration config, Set<String> processedExtensions) throws ConfigurationException {
		String name = config.getString("[@name]");
		
		if(StringUtils.isBlank(name))
			throw new ConfigurationException("empty name provided");
		
		if(processedExtensions.contains(name))
			throw new ConfigurationException("configuration for extension \"" + name + "\" given twice");
		
		Extension extension = factory.getExtensionByName(name);
		
		if(extension == null)
			throw new ConfigurationException("extension \"" + name + "\" not found");
		
		extension.configure(config);
		
		processedExtensions.add(name);
	}

}
