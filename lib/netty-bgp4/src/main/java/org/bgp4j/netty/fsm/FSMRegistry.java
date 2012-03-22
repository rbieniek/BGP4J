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
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.Instance;
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
	
	private Map<InetSocketAddress, BGPv4FSM> fsmMap = new HashMap<InetSocketAddress, BGPv4FSM>();
	
	private @Inject Instance<BGPv4FSM> fsmProvider;
	private @Inject ApplicationConfiguration applicationConfiguration;
	private @Inject Logger log;
	private boolean haveRunningMachines = false;
	
	public FSMRegistry() {
		
	}
	
	public void createRegistry() {
		for(PeerConfiguration peerConfig : applicationConfiguration.listPeerConfigurations()) {
			try {
				BGPv4FSM fsm = fsmProvider.get();

				fsm.configure(peerConfig);
				fsmMap.put(fsm.getRemotePeerAddress(), fsm);
			} catch(Exception e) {
				log.error("Internal error: cannot create peer " + peerConfig.getPeerName(), e);
			}
		}
	}
	
	public void registerFSM(BGPv4FSM fsm) {
		synchronized (fsmMap) {
			fsmMap.put(fsm.getRemotePeerAddress(), fsm);
		}		
	}
	
	public BGPv4FSM lookupFSM(InetSocketAddress peerAddress) {
		synchronized (fsmMap) {
			return fsmMap.get(peerAddress);
		}
	}

	
	public BGPv4FSM lookupFSM(InetAddress peerAddress) {
		List<BGPv4FSM> candidates = new LinkedList<BGPv4FSM>();
		BGPv4FSM fsm = null;
		
		synchronized (fsmMap) {
			for(Entry<InetSocketAddress, BGPv4FSM> fsmEntry : fsmMap.entrySet()) {
				if(fsmEntry.getKey().getAddress().equals(peerAddress)) {
					candidates.add(fsmEntry.getValue());
				}
			}
		}
		
		if(candidates.size() > 1)
			throw new IllegalStateException("Having more than one FSM instance for address " + peerAddress);
		else if(candidates.size() == 1)
			fsm = candidates.get(0);
		
		return fsm;
	}

	public void destroyRegistry() {
		for(InetSocketAddress addr : fsmMap.keySet())
			fsmMap.get(addr).destroyFSM();
		
		fsmMap.clear();
	}
	
	public void peerChanged(@Observes PeerConfigurationEvent event) {
		BGPv4FSM fsm = null;
		InetSocketAddress remotePeerAddress = null;
		
		switch(event.getType()) {
		case CONFIGURATION_ADDED:
			try {
				fsm = fsmProvider.get();
				
				fsm.configure(event.getCurrent());
				
				synchronized (fsmMap) {
					fsmMap.put(fsm.getRemotePeerAddress(), fsm);				
				}
				
				if(haveRunningMachines)
					fsm.startFSMAutomatic();
			} catch(Exception e) {
				log.error("Internal error: cannot create peer " + event.getCurrent().getPeerName());
			}

			break;
		case CONFIGURATION_REMOVED:
			remotePeerAddress = event.getFormer().getClientConfig().getRemoteAddress();

			synchronized (fsmMap) {
				fsm = fsmMap.remove(remotePeerAddress);
			}

			if (fsm != null) {
				fsm.stopFSM();
				fsm.destroyFSM();
			}
			break;
		}
	}

	public void startFiniteStateMachines() {
		for(Entry<InetSocketAddress, BGPv4FSM> entry : fsmMap.entrySet()) {
			log.info("starting FSM automatic for connection to " + entry.getKey());
			
			entry.getValue().startFSMAutomatic();
		}
		haveRunningMachines = true;
	}

	public void stopFiniteStateMachines() {
		haveRunningMachines = false;
		
		for(Entry<InetSocketAddress, BGPv4FSM> entry : fsmMap.entrySet()) {
			log.info("stopping FSM automatic for connection to " + entry.getKey());
			
			entry.getValue().stopFSM();
		}
	}
}
