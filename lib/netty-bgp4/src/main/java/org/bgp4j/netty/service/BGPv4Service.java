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
 */
package org.bgp4j.netty.service;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.bgp4j.config.global.ApplicationConfiguration;
import org.bgp4j.netty.fsm.FSMRegistry;
import org.slf4j.Logger;


/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class BGPv4Service {
		
	private @Inject Logger log;

	private @Inject Instance<BGPv4Server> serverProvider;
	private @Inject FSMRegistry fsmRegistry;
	private @Inject ApplicationConfiguration applicationConfiguration;
	
	private BGPv4Server serverInstance;

	/**
	 * start the service
	 * 
	 * @param configuration the initial service configuration
	 */
	public void startService() {
		fsmRegistry.createRegistry();
				
		if(applicationConfiguration.getBgpServerConfiguration()!= null) {
			log.info("starting local BGPv4 server");
			
			this.serverInstance = serverProvider.get();
			
			serverInstance.startServer();
		}
		
		fsmRegistry.startFiniteStateMachines();
	}

	/**
	 * stop the running service
	 * 
	 */
	public void stopService() {
		fsmRegistry.stopFiniteStateMachines();

		if(serverInstance != null)
			serverInstance.stopServer();

		fsmRegistry.destroyRegistry();
	}
	
}
