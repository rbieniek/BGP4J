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
 * File: org.bgp4.config.impl.ConfigurationImpl.java 
 */
package org.bgp4.config.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.configuration.ConfigurationException;
import org.bgp4.config.Configuration;
import org.bgp4.config.nodes.BgpServerConfiguration;
import org.bgp4.config.nodes.PeerConfiguration;
import org.bgp4.config.nodes.PeerConfigurationTimerDecorator;
import org.bgp4.config.nodes.impl.FixedDefaultsPeerConfigurationTimerDecorator;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class ConfigurationImpl implements Configuration {

	private BgpServerConfiguration bgpServerConfigImpl;
	private Map<String, PeerConfiguration> peerMap = new HashMap<String, PeerConfiguration>();
	
	@Override
	public BgpServerConfiguration getBgpServerConfiguration() {
		return bgpServerConfigImpl;
	}
	
	void setBgpServerConfiguration(BgpServerConfiguration bgpServerConfigImpl) {
		this.bgpServerConfigImpl = bgpServerConfigImpl;
	}

	@Override
	public Set<String> listPeerNames() {
		return Collections.unmodifiableSet(peerMap.keySet());
	}

	@Override
	public PeerConfiguration getPeer(String peerName) {
		return peerMap.get(peerName);
	}
	
	void addPeer(PeerConfiguration peerConfig) throws ConfigurationException {
		if(peerMap.containsKey(peerConfig.getPeerName()))
				throw new ConfigurationException("duplicate pper name " + peerConfig.getPeerName());
		
		if(!(peerConfig instanceof PeerConfigurationTimerDecorator))
			peerConfig = new FixedDefaultsPeerConfigurationTimerDecorator(peerConfig);
		
		peerMap.put(peerConfig.getPeerName(), peerConfig);
	}

	@Override
	public List<PeerConfiguration> listPeerConfigurations() {
		List<PeerConfiguration> peers = new ArrayList<PeerConfiguration>(peerMap.size());
		
		for(Entry<String, PeerConfiguration> peerEntry : peerMap.entrySet())
			peers.add(peerEntry.getValue());
		
		return Collections.unmodifiableList(peers);
	}
}
