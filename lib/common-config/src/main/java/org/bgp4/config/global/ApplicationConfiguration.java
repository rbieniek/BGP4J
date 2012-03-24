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
 * File: org.bgp4.config.global.ApplicationConfiguration.java 
 */
package org.bgp4.config.global;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.enterprise.event.Event;
import javax.enterprise.inject.Any;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.bgp4.config.Configuration;
import org.bgp4.config.ModifiableConfiguration;
import org.bgp4.config.nodes.BgpServerConfiguration;
import org.bgp4.config.nodes.HttpServerConfiguration;
import org.bgp4.config.nodes.PeerConfiguration;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
@Singleton
public class ApplicationConfiguration implements ModifiableConfiguration {

	private BgpServerConfiguration bgpServerConfiguration;
	private HttpServerConfiguration httpServerConfiguration;
	private Map<String, PeerConfiguration> peers = new HashMap<String, PeerConfiguration>();
	
	private @Any @Inject Event<BgpServerConfigurationEvent> bgpServerConfigurationEvent;
	private @Any @Inject Event<HttpServerConfigurationEvent> httpServerConfigurationEvent;
	private @Any @Inject Event<PeerConfigurationEvent> peerConfigurationEvent;
	
	void resetConfiguration() {
		this.bgpServerConfiguration = null;
		this.httpServerConfiguration = null;
		this.peers = new HashMap<String, PeerConfiguration>();
	}
	
	public void importConfiguration(Configuration configuration) {
		setBgpServerConfiguration(configuration.getBgpServerConfiguration());
		
		for(PeerConfiguration peer : configuration.listPeerConfigurations())
			putPeer(peer);
	}
	
	/* (non-Javadoc)
	 * @see org.bgp4.config.Configuration#getBgpServerConfiguration()
	 */
	@Override
	public BgpServerConfiguration getBgpServerConfiguration() {
		return bgpServerConfiguration;
	}
	
	@Override
	public void setBgpServerConfiguration(BgpServerConfiguration serverConfiguration) {
		EventType type = EventType.determineEvent(this.bgpServerConfiguration, serverConfiguration);
		
		this.bgpServerConfiguration = serverConfiguration;

		if(type != null)
			bgpServerConfigurationEvent.fire(new BgpServerConfigurationEvent(type, this.bgpServerConfiguration));
	}

	public HttpServerConfiguration getHttpServerConfiguration() {
		return httpServerConfiguration;
	}

	public void setHttpServerConfiguration(HttpServerConfiguration httpServerConfiguration) {
		EventType type = EventType.determineEvent(this.httpServerConfiguration, httpServerConfiguration);

		this.httpServerConfiguration = httpServerConfiguration;
		
		if(type != null)
			httpServerConfigurationEvent.fire(new HttpServerConfigurationEvent(type, this.httpServerConfiguration));
	}

	/* (non-Javadoc)
	 * @see org.bgp4.config.Configuration#listPeerNames()
	 */
	@Override
	public Set<String> listPeerNames() {
		return Collections.unmodifiableSet(peers.keySet());
	}

	/* (non-Javadoc)
	 * @see org.bgp4.config.Configuration#listPeerConfigurations()
	 */
	@Override
	public List<PeerConfiguration> listPeerConfigurations() {
		List<PeerConfiguration> entries = new ArrayList<PeerConfiguration>(peers.size());
		
		for(Entry<String, PeerConfiguration> entry : peers.entrySet())
			entries.add(entry.getValue());
		
		return Collections.unmodifiableList(entries);
	}

	/* (non-Javadoc)
	 * @see org.bgp4.config.Configuration#getPeer(java.lang.String)
	 */
	@Override
	public PeerConfiguration getPeer(String peerName) {
		return peers.get(peerName);
	}

	public void putPeer(PeerConfiguration peer) {
		PeerConfiguration former = getPeer(peer.getPeerName());
		EventType type = EventType.determineEvent(former, peer);
		
		peers.put(peer.getPeerName(), peer);
		
		if(type != null)
			peerConfigurationEvent.fire(new PeerConfigurationEvent(type, former, peer));
	}
	
	public void removePeer(String peerName) {
		PeerConfiguration peer = peers.remove(peerName);
		
		if(peer != null)
			peerConfigurationEvent.fire(new PeerConfigurationEvent(EventType.CONFIGURATION_REMOVED, peer, null));
	}
}
