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
 * File: org.bgp4.config.nodes.PeerConfigurationDecorator.java 
 */
package org.bgp4.config.nodes;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class PeerConfigurationDecorator implements PeerConfiguration {

	protected PeerConfiguration decorated;

	protected PeerConfigurationDecorator(PeerConfiguration decorated) {
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
		return decorated.getHoldTime();
	}

	/**
	 * @return
	 * @see org.bgp4.config.nodes.PeerConfiguration#getIdleHoldTime()
	 */
	public int getIdleHoldTime() {
		return decorated.getIdleHoldTime();
	}

	/**
	 * @return
	 * @see org.bgp4.config.nodes.PeerConfiguration#isAllowAutomaticStart()
	 */
	public boolean isAllowAutomaticStart() {
		return decorated.isAllowAutomaticStart();
	}

	/**
	 * @return
	 * @see org.bgp4.config.nodes.PeerConfiguration#isAllowAutomaticStop()
	 */
	public boolean isAllowAutomaticStop() {
		return decorated.isAllowAutomaticStop();
	}

	/**
	 * @return
	 * @see org.bgp4.config.nodes.PeerConfiguration#isDampPeerOscillation()
	 */
	public boolean isDampPeerOscillation() {
		return decorated.isDampPeerOscillation();
	}

	/**
	 * @return
	 * @see org.bgp4.config.nodes.PeerConfiguration#isPassiveTcpEstablishment()
	 */
	public boolean isPassiveTcpEstablishment() {
		return decorated.isPassiveTcpEstablishment();
	}

	/**
	 * @return
	 * @see org.bgp4.config.nodes.PeerConfiguration#isDelayOpen()
	 */
	public boolean isDelayOpen() {
		return decorated.isDelayOpen();
	}

	/**
	 * @return
	 * @see org.bgp4.config.nodes.PeerConfiguration#getDelayOpenTime()
	 */
	public int getDelayOpenTime() {
		return decorated.getDelayOpenTime();
	}

	/**
	 * @return
	 * @see org.bgp4.config.nodes.PeerConfiguration#isCollisionDetectEstablishedState()
	 */
	public boolean isCollisionDetectEstablishedState() {
		return decorated.isCollisionDetectEstablishedState();
	}

	/**
	 * @param other
	 * @return
	 * @see org.bgp4.config.nodes.PeerConfiguration#equals(java.lang.Object)
	 */
	public boolean equals(Object other) {
		if(!(other instanceof PeerConfiguration))
			return false;
		
		PeerConfiguration o = (PeerConfiguration)other;
		
		return (new EqualsBuilder())
				.append(getAutomaticStartInterval(), o.getAutomaticStartInterval())
				.append(getClientConfig(), o.getClientConfig())
				.append(getIdleHoldTime(), o.getIdleHoldTime())
				.append(getHoldTime(), o.getHoldTime())
				.append(getLocalAS(), o.getLocalAS())
				.append(getLocalBgpIdentifier(), o.getLocalBgpIdentifier())
				.append(getPeerName(), o.getPeerName())
				.append(getRemoteAS(), o.getRemoteAS())
				.append(getRemoteBgpIdentifier(), o.getRemoteBgpIdentifier())
				.append(getDelayOpenTime(), o.getDelayOpenTime())
				.append(isAllowAutomaticStart(), o.isAllowAutomaticStart())
				.append(isAllowAutomaticStop(), o.isAllowAutomaticStop())
				.append(isCollisionDetectEstablishedState(), o.isCollisionDetectEstablishedState())
				.append(isDampPeerOscillation(), o.isDampPeerOscillation())
				.append(isDelayOpen(), o.isDelayOpen())
				.append(isPassiveTcpEstablishment(), o.isPassiveTcpEstablishment())
				.append(getConnectRetryTime(), o.getConnectRetryTime())
				.isEquals();
	}

	/**
	 * @return
	 * @see org.bgp4.config.nodes.PeerConfiguration#hashCode()
	 */
	public int hashCode() {
		/*
		return (new HashCodeBuilder())
				.append(allowAutomaticStart)
				.append(allowAutomaticStop)
				.append(clientConfig)
				.append(collisionDetectEstablishedState)
				.append(dampPeerOscillation)
				.append(connectRetryTime)
				.append(delayOpen)
				.append(delayOpenTime)
				.append(holdTime)
				.append(idleHoldTime)
				.append(localAS)
				.append(localBgpIdentifier)
				.append(passiveTcpEstablishment)
				.append(peerName)
				.append(remoteAS)
				.append(remoteBgpIdentifier)
				.toHashCode();
		 */
		return (new HashCodeBuilder())
				.append(isAllowAutomaticStart())
				.append(isAllowAutomaticStop())
				.append(getAutomaticStartInterval())
				.append(getClientConfig())
				.append(isCollisionDetectEstablishedState())
				.append(getConnectRetryTime())
				.append(isDampPeerOscillation())
				.append(isDelayOpen())
				.append(getDelayOpenTime())
				.append(getHoldTime())				
				.append(getIdleHoldTime())				
				.append(getLocalAS())
				.append(getLocalBgpIdentifier())
				.append(isPassiveTcpEstablishment())				
				.append(getPeerName())
				.append(getRemoteAS())
				.append(getRemoteBgpIdentifier())
				.toHashCode();
	}

	/**
	 * @return
	 * @see org.bgp4.config.nodes.PeerConfiguration#getConnectRetryTime()
	 */
	public int getConnectRetryTime() {
		return decorated.getConnectRetryTime();
	}

	@Override
	public int getAutomaticStartInterval() {
		return decorated.getAutomaticStartInterval();
	}
}
