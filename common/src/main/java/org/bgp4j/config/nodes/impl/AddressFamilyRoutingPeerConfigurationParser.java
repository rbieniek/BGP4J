/**
 * 
 */
package org.bgp4j.config.nodes.impl;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.bgp4j.config.nodes.AddressFamilyRoutingPeerConfiguration;
import org.bgp4j.config.nodes.RoutingFilterConfiguration;
import org.bgp4j.net.AddressFamily;
import org.bgp4j.net.AddressFamilyKey;
import org.bgp4j.net.SubsequentAddressFamily;


/**
 * @author rainer
 *
 */
@Singleton
public class AddressFamilyRoutingPeerConfigurationParser {

	private @Inject RoutingFilterConfigurationParser filterParser;
	private @Inject PathAttributeConfigurationParser pathAttributeParser;
	
	AddressFamilyRoutingPeerConfiguration parseConfiguration(HierarchicalConfiguration config) throws ConfigurationException {
		AddressFamilyRoutingPeerConfigurationImpl result = new AddressFamilyRoutingPeerConfigurationImpl();
		String addressFamily = config.getString("[@addressFamily]");
		String subsequentAddressFamily = config.getString("[@subsequentAddressFamily]");
		HierarchicalConfiguration localFilterConfiguration = first(config, "Local.Filters");
		HierarchicalConfiguration remoteFilterConfiguration = first(config, "Remote.Filters");
		HierarchicalConfiguration localPathAttributes = first(config, "Local.DefaultPathAttributes");
		HierarchicalConfiguration remotePathAttributes = first(config, "Remote.DefaultPathAttributes");

		try {
			result.setAddressFamilyKey(new AddressFamilyKey(AddressFamily.fromString(addressFamily), 
					SubsequentAddressFamily.fromString(subsequentAddressFamily)));
		} catch(IllegalArgumentException e) {
			throw new ConfigurationException("Invalid AddressFamilyKey given", e);
		}
		
		if(localFilterConfiguration != null)
			result.setLocalRoutingFilters(parsRoutingeFilters(localFilterConfiguration));
		
		if(remoteFilterConfiguration != null)
			result.setRemoteRoutingFilters(parsRoutingeFilters(remoteFilterConfiguration));
		
		if(localPathAttributes != null)
			result.setLocalDefaultPathAttributes(pathAttributeParser.parseConfiguration(localPathAttributes));
		
		if(remotePathAttributes != null)
			result.setRemoteDefaultPathAttributes(pathAttributeParser.parseConfiguration(remotePathAttributes));
		
		return result;
	}
	
	private HierarchicalConfiguration first(HierarchicalConfiguration config, String key) throws ConfigurationException {
		HierarchicalConfiguration result = null;
		List<HierarchicalConfiguration> childs = config.configurationsAt(key);
		
		if(childs.size() > 1)
			throw new ConfigurationException("Duplicate element " + key);
		else if(childs.size() == 1)
			result = childs.get(0);
		
		return result;
	}
	
	private Set<RoutingFilterConfiguration> parsRoutingeFilters(HierarchicalConfiguration config) throws ConfigurationException {
		Set<RoutingFilterConfiguration> result = new TreeSet<RoutingFilterConfiguration>();
		
		for(HierarchicalConfiguration subConfig : config.configurationsAt("Filter"))
			result.add(filterParser.parseConfiguration(subConfig));
		
		return result;
	}
}
