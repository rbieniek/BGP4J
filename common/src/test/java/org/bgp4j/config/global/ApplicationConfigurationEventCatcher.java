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
 * File: org.bgp4.config.global.ApplicationConfigurationEventCatcher.java 
 */
package org.bgp4j.config.global;

import javax.enterprise.event.Observes;
import javax.inject.Singleton;

import org.bgp4j.config.global.BgpServerConfigurationEvent;
import org.bgp4j.config.global.EventType;
import org.bgp4j.config.global.PeerConfigurationEvent;
import org.bgp4j.config.nodes.BgpServerConfiguration;
import org.bgp4j.config.nodes.PeerConfiguration;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
@Singleton
public class ApplicationConfigurationEventCatcher {

	private boolean bgpServerConfigurationEventFired = false;
	private EventType bgpServerConfigurationEventType = null;
	private BgpServerConfiguration bgpServerConfiguration = null;
	
	private boolean peerConfigurationEventFired = false;
	private EventType peerConfigurationEventType = null;
	private PeerConfiguration formerPeerConfiguration = null;
	private PeerConfiguration currentPeerConfiguration = null;
	
	public void catchBgpServerConfigurationEvent(@Observes BgpServerConfigurationEvent event) {
		bgpServerConfigurationEventFired = true;
		bgpServerConfigurationEventType = event.getType();
		bgpServerConfiguration = event.getConfiguration();
	}

	public void catchPeerConfigurationEvent(@Observes PeerConfigurationEvent event) {
		peerConfigurationEventFired = true;
		peerConfigurationEventType = event.getType();
		formerPeerConfiguration = event.getFormer();
		currentPeerConfiguration = event.getCurrent();
	}
	
	void reset() {
		bgpServerConfiguration = null;
		bgpServerConfigurationEventFired = false;
		bgpServerConfigurationEventType = null;

		peerConfigurationEventFired = false;
		peerConfigurationEventType = null;
		formerPeerConfiguration = null;
		currentPeerConfiguration = null;
	}
	
	/**
	 * @return the bgpServerConfigurationEventFired
	 */
	public boolean isBgpServerConfigurationEventFired() {
		return bgpServerConfigurationEventFired;
	}

	/**
	 * @return the bgpServerConfigurationEventType
	 */
	public EventType getBgpServerConfigurationEventType() {
		return bgpServerConfigurationEventType;
	}

	/**
	 * @return the bgpServerConfiguration
	 */
	public BgpServerConfiguration getBgpServerConfiguration() {
		return bgpServerConfiguration;
	}

	/**
	 * @return the peerConfigurationEventFired
	 */
	public boolean isPeerConfigurationEventFired() {
		return peerConfigurationEventFired;
	}

	/**
	 * @return the peerConfigurationEventType
	 */
	public EventType getPeerConfigurationEventType() {
		return peerConfigurationEventType;
	}

	/**
	 * @return the formerPeerConfiguration
	 */
	public PeerConfiguration getFormerPeerConfiguration() {
		return formerPeerConfiguration;
	}

	/**
	 * @return the currentPeerConfiguration
	 */
	public PeerConfiguration getCurrentPeerConfiguration() {
		return currentPeerConfiguration;
	}
}
