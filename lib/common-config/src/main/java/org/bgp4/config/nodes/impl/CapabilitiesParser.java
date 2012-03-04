/**
 *  Copyright 2012 Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * 
 * File: org.bgp4.config.nodes.impl.CapabilitiesParser.java 
 */
package org.bgp4.config.nodes.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.bgp4.config.nodes.Capabilities;
import org.bgp4j.net.AddressFamily;
import org.bgp4j.net.AutonomousSystem4Capability;
import org.bgp4j.net.MultiProtocolCapability;
import org.bgp4j.net.ORFSendReceive;
import org.bgp4j.net.ORFType;
import org.bgp4j.net.OutboundRouteFilteringCapability;
import org.bgp4j.net.RouteRefreshCapability;
import org.bgp4j.net.SubsequentAddressFamily;

/**
 * @author Rainer Bieniek (rainer@bgp4j.org)
 *
 */
@Singleton
public class CapabilitiesParser {

	public Capabilities parseConfig(HierarchicalConfiguration hierarchicalConfiguration) throws ConfigurationException {
		CapabilitiesImpl  caps = new CapabilitiesImpl();

		int as4Number = hierarchicalConfiguration.getInt("LargeAutonomousSystem[@local]", -1);
		
		if(as4Number > 0)
			caps.addCapability(new AutonomousSystem4Capability(as4Number));

		if(hierarchicalConfiguration.containsKey("RouteRefresh"))
			caps.addCapability(new RouteRefreshCapability());
		
		parseMultiprotocolCapabilities(hierarchicalConfiguration.configurationsAt("MultiProtocol"), caps);
		parseOutboundRouteFilteringCapabilities(hierarchicalConfiguration.configurationsAt("OutboundRouteFiltering"), caps);
		
		return caps;
	}

	private void parseMultiprotocolCapabilities(List<HierarchicalConfiguration> capabilityConfigs, CapabilitiesImpl  caps) throws ConfigurationException {
		for(HierarchicalConfiguration config : capabilityConfigs) {
			try {
				caps.addCapability(new MultiProtocolCapability(AddressFamily.fromString(config.getString("[@addressFamily]")), 
						SubsequentAddressFamily.fromString(config.getString("[@subsequentAddressFamily]"))));
			} catch(IllegalArgumentException e) {
				throw new ConfigurationException(e);
			}
		}
	}

	private void parseOutboundRouteFilteringCapabilities(List<HierarchicalConfiguration> capabilityConfigs, CapabilitiesImpl  caps) throws ConfigurationException {
		for(HierarchicalConfiguration config : capabilityConfigs) {
			try {
				Map<ORFType, ORFSendReceive> filters = new HashMap<ORFType, ORFSendReceive>();
				
				for(HierarchicalConfiguration entryConfig : config.configurationsAt("Entry")) {
					filters.put(ORFType.fromString(entryConfig.getString("[@type]")), 
							ORFSendReceive.fromString(entryConfig.getString("[@direction]")));
				}
				
				if(filters.size() == 0)
					throw new ConfigurationException("filter type/direction pair required");
				
				caps.addCapability(new OutboundRouteFilteringCapability(AddressFamily.fromString(config.getString("[@addressFamily]")), 
						SubsequentAddressFamily.fromString(config.getString("[@subsequentAddressFamily]")), 
						filters));
			} catch(IllegalArgumentException e) {
				throw new ConfigurationException(e);
			}
		}
	}
}
