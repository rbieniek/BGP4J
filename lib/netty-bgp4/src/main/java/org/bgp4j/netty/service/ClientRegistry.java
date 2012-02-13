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

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

/**
 * This registry maps from an InetAddress to a client instance.
 * 
 * All methods may block the caller.
 * 
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
@Singleton
public class ClientRegistry {

	private Map<InetAddress, BGPv4Client> clientMap = new HashMap<InetAddress, BGPv4Client>();

	/**
	 * register a client 
	 * 
	 * @param remotePeerAddress
	 * @param client
	 */
	public void registerClient(BGPv4Client client) {
		synchronized (clientMap) {
			clientMap.put(client.getRemotePeerAddress(), client);
		}
	}
	
	/**
	 * creates a snapshot copy of all registered peer remote addresses 
	 * 
	 * @return
	 */
	public List<InetAddress> listRemotePeerAddresses() {
		synchronized (clientMap) {
			List<InetAddress> addrs = new ArrayList<InetAddress>(clientMap.size());

			addrs.addAll(clientMap.keySet());
			
			return Collections.unmodifiableList(addrs);
		}
	}
	
	/**
	 * Unregister a client and removes it from the registry
	 * 
	 * @param address the clients peer address
	 * @return the client instance or null if no client with this address is registered.
	 */
	public BGPv4Client unregisterClient(InetAddress address) {
		synchronized (clientMap) {
			return clientMap.remove(address);
		}
	}
	
	/**
	 * look up a client by its peer remote address
	 * 
	 * @param address the address to look up
	 * @return the client instance or null if no client with this address is registered.
	 */
	public BGPv4Client lookupClient(InetAddress address) {
		synchronized (clientMap) {
			return clientMap.get(address);
		}		
	}
}
