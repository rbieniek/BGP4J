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
 * File: org.bgp4.config.nodes.impl.PeerConfigurationParser.java 
 */
package org.bgp4.config.nodes.impl;

import java.util.List;
import java.util.NoSuchElementException;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.bgp4.config.nodes.PeerConfiguration;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
@Singleton
public class PeerConfigurationParser {

	private @Inject ClientConfigurationParser clientConfigurationParser;
	
	public PeerConfiguration parseConfiguration(HierarchicalConfiguration config) throws ConfigurationException {
		PeerConfigurationImpl peerConfig = new PeerConfigurationImpl();
		List<HierarchicalConfiguration> clientConfigs = config.configurationsAt("Client");
		
		if(clientConfigs.size() > 1) {
			throw new ConfigurationException("duplicate <Client/> element");
		} else if(clientConfigs.size() == 0) {
			throw new ConfigurationException("missing <Client/> element");			
		} else
			peerConfig.setClientConfig(clientConfigurationParser.parseConfig(clientConfigs.get(0)));
		
		try {
			peerConfig.setLocalAS(config.getInt("AutonomousSystem[@local]"));
		} catch(NoSuchElementException e) {
			throw new ConfigurationException("local AS number not given", e);
		}
		try {
			peerConfig.setRemoteAS(config.getInt("AutonomousSystem[@remote]"));
		} catch(NoSuchElementException e) {
			throw new ConfigurationException("remote AS number not given", e);
		}
		
		return peerConfig;
	}
}
