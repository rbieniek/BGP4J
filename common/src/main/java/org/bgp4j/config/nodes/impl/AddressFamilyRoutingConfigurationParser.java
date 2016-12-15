/**
 * 
 */
package org.bgp4j.config.nodes.impl;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.bgp4j.config.nodes.AddressFamilyRoutingConfiguration;
import org.bgp4j.config.nodes.RouteConfiguration;
import org.bgp4j.net.AddressFamily;
import org.bgp4j.net.AddressFamilyKey;
import org.bgp4j.net.SubsequentAddressFamily;

/**
 * @author rainer
 *
 */
@Singleton
public class AddressFamilyRoutingConfigurationParser {

	private @Inject RouteConfigurationParser routeParser;
	
	AddressFamilyRoutingConfiguration parseConfiguration(HierarchicalConfiguration config) throws ConfigurationException {
		try {
			AddressFamilyKey key = new AddressFamilyKey(AddressFamily.fromString(config.getString("[@addressFamily]")), 
					SubsequentAddressFamily.fromString(config.getString("[@subsequentAddressFamily]")));
			
			List<RouteConfiguration> routes = new LinkedList<RouteConfiguration>();
			
			for(HierarchicalConfiguration routeConfig : config.configurationsAt("Route"))
				routes.add(routeParser.parseConfiguration(routeConfig));
			
			return new AddressFamilyRoutingConfigurationImpl(key, routes);
		} catch(IllegalArgumentException  e) {
			throw new ConfigurationException("Invalid value: ", e);
		}
	}
}
