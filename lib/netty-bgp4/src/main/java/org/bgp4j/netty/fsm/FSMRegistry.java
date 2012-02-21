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
 * File: org.bgp4j.netty.fsm.FSMRegistry.java 
 */
package org.bgp4j.netty.fsm;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.New;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.bgp4.config.global.ApplicationConfiguration;
import org.bgp4.config.global.PeerConfigurationEvent;
import org.bgp4.config.nodes.PeerConfiguration;
import org.slf4j.Logger;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
@Singleton
public class FSMRegistry {
	
	private Map<InetAddress, BGPv4FSM> fsmMap = new HashMap<InetAddress, BGPv4FSM>();
	
	private @Inject @New Instance<BGPv4FSM> fsmProvider;
	private @Inject ApplicationConfiguration applicationConfiguration;
	private @Inject Logger log;
	
	public FSMRegistry() {
		
	}
	
	public void createRegistry() {
		for(PeerConfiguration peerConfig : applicationConfiguration.listPeerConfigurations()) {
			try {
				BGPv4FSM fsm = fsmProvider.get();

				fsm.configure(peerConfig);
				fsmMap.put(fsm.getRemotePeerAddress(), fsm);

				fsm.startFSMAutomatic();
			} catch(Exception e) {
				log.error("Internal error: cannot create peer " + peerConfig.getPeerName());
			}
		}
	}
	
	public BGPv4FSM lookupFSM(InetAddress peerAddress) {
		synchronized (fsmMap) {
			return fsmMap.get(peerAddress);
		}
	}
	
	public void destroyRegistry() {
		for(InetAddress addr : fsmMap.keySet())
			fsmMap.get(addr).stopFSM();
		
		fsmMap.clear();
	}
	
	public void peerChanged(@Observes PeerConfigurationEvent event) {
		BGPv4FSM fsm = null;
		InetAddress remotePeerAddress = null;
		
		switch(event.getType()) {
		case CONFIGURATION_ADDED:
			try {
				fsm = fsmProvider.get();
				
				fsm.configure(event.getCurrent());
				
				synchronized (fsmMap) {
					fsmMap.put(fsm.getRemotePeerAddress(), fsm);				
				}
				
				fsm.startFSMAutomatic();
			} catch(Exception e) {
				log.error("Internal error: cannot create peer " + event.getCurrent().getPeerName());
			}

			break;
		case CONFIGURATION_REMOVED:
			remotePeerAddress = event.getFormer().getClientConfig().getRemoteAddress().getAddress();

			synchronized (fsmMap) {
				fsm = fsmMap.remove(remotePeerAddress);
			}

			if (fsm != null)
				fsm.destroyFSM();
			break;
		}
	}

	public void startFiniteStateMachines() {
		// TODO Auto-generated method stub
		
	}

	public void stopFiniteStateMachines() {
		// TODO Auto-generated method stub
		
	}
}
