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

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * This registry maintains a mapping between peer InetSockAdress instances and channel IDs. This is used
 * to track connection and to shut down a BGP connection early if a similar but reverse connection already exists.
 * 
 * BGP is symmetric in that way that each peer may initiate a connection independently of its counterpart. 
 * 
 * All methods may block the caller.
 * 
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class PeerConnectionRegistry {

	private Map<InetSocketAddress, Integer> connections = new HashMap<InetSocketAddress, Integer>();
	
	/**
	 * Try to register a channel ID for a given peer address. 
	 * 
	 * @param peerAddress the BGP peer address
	 * @param channelId the channel ID
	 * @return <b>true</b> if the registration was successful and <b>false</b> if there is already a channel ID registered for the peer address.
	 */
	public boolean tryRegisterConnection(InetSocketAddress peerAddress, int channelId) {
		boolean result = true;
		
		synchronized (connections) {
			if(isPeerRegistered(peerAddress)) 
				result = false;
			else
				connections.put(peerAddress, channelId);
		}
		
		return result;
	}
	
	/**
	 * Check if a peer address is already registered. 
	 * 
	 * @param peerAddress the peer address
	 * @return <b>false</b> if the peer address is not registered and <b>true</b> if the peer address is registered.
	 */
	public boolean isPeerRegistered(InetSocketAddress peerAddress) {
		synchronized (connections) {
			return connections.containsKey(peerAddress);
		}
	}
	
	/**
	 * Unregister a channel ID.
	 *  
	 * @param channelId the channel ID to unregister.
	 */
	public void unregisterChannelId(int channelId) {
		synchronized (connections) {
			InetSocketAddress remove = null;
			
			for(Entry<InetSocketAddress, Integer> entry : connections.entrySet()) {
				if(entry.getValue() == channelId) {
					remove = entry.getKey();
					break;
				}
			}
				
			if(remove != null)
				connections.remove(remove);
		}
	}
}
