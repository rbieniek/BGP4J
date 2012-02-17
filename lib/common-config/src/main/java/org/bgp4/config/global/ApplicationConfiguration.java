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

import java.util.List;
import java.util.Set;

import javax.enterprise.event.Event;
import javax.enterprise.inject.Any;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.bgp4.config.ModifiableConfiguration;
import org.bgp4.config.nodes.BgpServerConfiguration;
import org.bgp4.config.nodes.PeerConfiguration;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
@Singleton
public class ApplicationConfiguration implements ModifiableConfiguration {

	private BgpServerConfiguration serverConfiguration;
	
	private @Any @Inject Event<BgpServerConfigurationEvent> serverConfigurationEvent;
	
	void resetConfiguration() {
		this.serverConfiguration = null;
	}
	
	/* (non-Javadoc)
	 * @see org.bgp4.config.Configuration#getBgpServerConfiguration()
	 */
	@Override
	public BgpServerConfiguration getBgpServerConfiguration() {
		return serverConfiguration;
	}
	
	@Override
	public void setBgpServerConfiguration(BgpServerConfiguration serverConfiguration) {
		EventType type = EventType.determineEvent(this.serverConfiguration, serverConfiguration);
		
		this.serverConfiguration = serverConfiguration;

		if(type != null)
			serverConfigurationEvent.fire(new BgpServerConfigurationEvent(type, this.serverConfiguration));
	}

	/* (non-Javadoc)
	 * @see org.bgp4.config.Configuration#listPeerNames()
	 */
	@Override
	public Set<String> listPeerNames() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.bgp4.config.Configuration#listPeerConfigurations()
	 */
	@Override
	public List<PeerConfiguration> listPeerConfigurations() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.bgp4.config.Configuration#getPeer(java.lang.String)
	 */
	@Override
	public PeerConfiguration getPeer(String peerName) {
		// TODO Auto-generated method stub
		return null;
	}


}
