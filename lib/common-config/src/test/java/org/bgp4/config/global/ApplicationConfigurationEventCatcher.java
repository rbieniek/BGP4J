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
package org.bgp4.config.global;

import javax.enterprise.event.Observes;
import javax.inject.Singleton;

import org.bgp4.config.nodes.BgpServerConfiguration;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
@Singleton
public class ApplicationConfigurationEventCatcher {

	private boolean bgpServerConfigurationEventFired = false;
	private EventType bgpServerConfigurationEventType = null;
	private BgpServerConfiguration bgpServerConfiguration = null;
	
	public void catchBgpServerConfigurationEvent(@Observes BgpServerConfigurationEvent event) {
		bgpServerConfigurationEventFired = true;
		bgpServerConfigurationEventType = event.getType();
		bgpServerConfiguration = event.getConfiguration();
	}

	void reset() {
		bgpServerConfiguration = null;
		bgpServerConfigurationEventFired = false;
		bgpServerConfigurationEventType = null;
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
}
