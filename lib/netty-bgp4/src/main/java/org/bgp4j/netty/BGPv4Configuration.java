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
package org.bgp4j.netty;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * 
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public interface BGPv4Configuration {

	/**
	 * @return the bgpv4Server
	 */
	public abstract InetSocketAddress getBgpv4Server();

	/**
	 * get the list of peers
	 * 
	 * @return a read-only list of peers
	 */
	public abstract List<BGPv4PeerConfiguration> getPeers();

	/**
	 * add a peer to the configuration. Any listener is notified after the peer has been added.
	 * 
	 * @param peer the peer to add
	 */
	public abstract void addPeer(BGPv4PeerConfiguration peer);

	/**
	 * remove a peer from the configuration. If the peer is removed any listener is notified before the peer is removed.
	 * 
	 * @param peer the peer to remove.
	 */
	public abstract void removePeer(BGPv4PeerConfiguration peer);

	/**
	 * add a listener
	 * 
	 * @param listener the listener
	 */
	public abstract void addListener(PeerConfigurationChangedListener listener);

	/**
	 * remove a listener
	 * 
	 * @param listener the listener
	 */
	public abstract void removeListener(
			PeerConfigurationChangedListener listener);

}