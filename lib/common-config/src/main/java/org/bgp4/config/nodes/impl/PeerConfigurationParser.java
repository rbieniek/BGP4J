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
		
		try {
			peerConfig.setPeerName(config.getString("[@name]"));
		} catch(NoSuchElementException e) {
			throw new ConfigurationException("peer name not set", e);
		}
		
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

		
		try {
			long identifier = config.getLong("BgpIdentifier[@local]");
			
			if(!isValidBgpIdentifier(identifier))
				throw new ConfigurationException("Invalid local BGP identifier: " + identifier);
			
			peerConfig.setLocalBgpIdentifier(identifier);
		} catch(NoSuchElementException e) {
			throw new ConfigurationException("local BGP identifier not given", e);
		}
		try {
			long identifier = config.getLong("BgpIdentifier[@remote]");
			
			if(!isValidBgpIdentifier(identifier))
				throw new ConfigurationException("Invalid remote BGP identifier: " + identifier);
			
			peerConfig.setRemoteBgpIdentifier(identifier);
		} catch(NoSuchElementException e) {
			throw new ConfigurationException("remote BGP identifier not given", e);
		}

		peerConfig.setHoldTime(config.getInt("Timers[@holdTime]", 0));
		peerConfig.setIdleHoldTime(config.getInt("Timers[@idleHoldTime]", 0));
		peerConfig.setDelayOpenTime(config.getInt("Timers[@delayOpenTime]", 0));
		
		peerConfig.setAllowAutomaticStart(config.getBoolean("Options[@allowAutomaticStart]", true));
		peerConfig.setAllowAutomaticStop(config.getBoolean("Options[@allowAutomaticStop]", false));
		peerConfig.setDampPeerOscillation(config.getBoolean("Options[@dampPeerOscillation]", false));
		peerConfig.setCollisionDetectEstablishedState(config.getBoolean("Options[@collisionDetectEstablishedState]", false));
		peerConfig.setDelayOpen(config.getBoolean("Options[@delayOpen]", false));
		peerConfig.setPassiveTcpEstablishment(config.getBoolean("Options[@passiveTcpEstablishment]", false));

		return peerConfig;
	}
	
	private boolean isValidBgpIdentifier(long id) {
		return ((id > 0) && (id < 0x00000000ffffffffL));
	}
}
