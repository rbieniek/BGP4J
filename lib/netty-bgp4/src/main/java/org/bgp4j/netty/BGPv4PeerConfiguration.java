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

/**
 * Configuration for a BGPv4 peer.
 *
 * The peer configuration <b>MUST</b> implement <code>equals</code> properly.
 * 
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public interface BGPv4PeerConfiguration {

	/**
	 * @return the remoteBgpIdentitifer
	 */
	public abstract int getRemoteBgpIdentitifer();

	/**
	 * @return the remoteAutonomousSystem
	 */
	public abstract int getRemoteAutonomousSystem();

	/**
	 * @return the remotePeerAddress
	 */
	public abstract InetSocketAddress getRemotePeerAddress();

	/**
	 * @return the localBgpIdentifier
	 */
	public abstract int getLocalBgpIdentifier();

	/**
	 * @return the localAutonomousSystem
	 */
	public abstract int getLocalAutonomousSystem();

	/**
	 * @return the localHoldTime
	 */
	public abstract int getLocalHoldTime();

	/**
	 * retry interval for establishing a connection to the remote peer
	 * @return
	 */
	public abstract int getConnectRetryInterval();
}