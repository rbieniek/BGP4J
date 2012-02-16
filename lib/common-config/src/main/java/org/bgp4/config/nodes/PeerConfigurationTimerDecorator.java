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
 * File: org.bgp4.config.nodes.PeerConfigurationTimerDecorator.java 
 */
package org.bgp4.config.nodes;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public abstract class PeerConfigurationTimerDecorator implements PeerConfiguration {

	private PeerConfiguration decorated;
	
	protected PeerConfigurationTimerDecorator(PeerConfiguration decorated) {
		this.decorated = decorated;
	}

	/**
	 * @return
	 * @see org.bgp4.config.nodes.PeerConfiguration#getClientConfig()
	 */
	public ClientConfiguration getClientConfig() {
		return decorated.getClientConfig();
	}

	/**
	 * @return
	 * @see org.bgp4.config.nodes.PeerConfiguration#getLocalAS()
	 */
	public int getLocalAS() {
		return decorated.getLocalAS();
	}

	/**
	 * @return
	 * @see org.bgp4.config.nodes.PeerConfiguration#getRemoteAS()
	 */
	public int getRemoteAS() {
		return decorated.getRemoteAS();
	}

	/**
	 * @return
	 * @see org.bgp4.config.nodes.PeerConfiguration#getPeerName()
	 */
	public String getPeerName() {
		return decorated.getPeerName();
	}

	/**
	 * @return
	 * @see org.bgp4.config.nodes.PeerConfiguration#getRemoteBgpIdentifier()
	 */
	public long getRemoteBgpIdentifier() {
		return decorated.getRemoteBgpIdentifier();
	}

	/**
	 * @return
	 * @see org.bgp4.config.nodes.PeerConfiguration#getLocalBgpIdentifier()
	 */
	public long getLocalBgpIdentifier() {
		return decorated.getLocalBgpIdentifier();
	}

	/**
	 * @return
	 * @see org.bgp4.config.nodes.PeerConfiguration#getHoldTime()
	 */
	public int getHoldTime() {
		int holdTime = decorated.getHoldTime();
		
		if(holdTime == 0)
			holdTime = getDefaultHoldTime();
		
		return holdTime;
	}

	/**
	 * 
	 * @return
	 */
	protected abstract int getDefaultHoldTime();
	
	/**
	 * @return
	 * @see org.bgp4.config.nodes.PeerConfiguration#getConnectRetryInterval()
	 */
	public int getConnectRetryInterval() {
		int retryInterval = decorated.getConnectRetryInterval();
		
		if(retryInterval == 0)
			retryInterval = getDefaultConnectRetryInterval();
		
		return retryInterval;
	}
	
	/**
	 * 
	 * @return
	 */
	protected abstract int  getDefaultConnectRetryInterval();
}
