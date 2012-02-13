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

import javax.enterprise.inject.Instance;
import javax.enterprise.inject.New;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.bgp4j.netty.BGPv4Configuration;
import org.bgp4j.netty.BGPv4PeerConfiguration;
import org.bgp4j.netty.PeerConfigurationChangedListener;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
@Singleton
public class FSMRegistry {

	private class FSMInstanceManagerListener implements PeerConfigurationChangedListener {

		@Override
		public void peerAdded(BGPv4PeerConfiguration peerConfig) {
			BGPv4FSM fsm = fsmProvider.get();
			
			fsm.configure(peerConfig);
			
			synchronized (fsmMap) {
				fsmMap.put(fsm.getRemotePeerAddress(), fsm);				
			}
		}

		@Override
		public void peerRemoved(BGPv4PeerConfiguration peerConfig) {
			InetAddress remotePeerAddress = peerConfig.getRemotePeerAddress().getAddress();

			BGPv4FSM fsm = null;
			
			synchronized (fsmMap) {
				fsm = fsmMap.remove(remotePeerAddress);
			}
			
			if(fsm != null)
				fsm.destroyFSM();
		}
	}
	
	private Map<InetAddress, BGPv4FSM> fsmMap = new HashMap<InetAddress, BGPv4FSM>();
	
	private @Inject @New Instance<BGPv4FSM> fsmProvider;
	
	public FSMRegistry() {
		
	}
	
	public void createRegistry(BGPv4Configuration config) {
		config.addListener(new FSMInstanceManagerListener());
		
		for(BGPv4PeerConfiguration peerConfig : config.getPeers()) {
			BGPv4FSM fsm = fsmProvider.get();
			
			fsm.configure(peerConfig);
			fsmMap.put(fsm.getRemotePeerAddress(), fsm);
		}
	}
	
	public BGPv4FSM lookupFSM(InetAddress peerAddress) {
		synchronized (fsmMap) {
			return fsmMap.get(peerAddress);
		}
	}
	
	public void destroyRegistry() {
		for(InetAddress addr : fsmMap.keySet())
			fsmMap.get(addr).destroyFSM();
		
		fsmMap.clear();
	}
}
