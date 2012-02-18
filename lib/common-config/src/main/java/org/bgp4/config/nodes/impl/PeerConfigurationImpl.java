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
 * File: org.bgp4.config.nodes.impl.PeerConfigurationImpl.java 
 */
package org.bgp4.config.nodes.impl;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.bgp4.config.nodes.ClientConfiguration;
import org.bgp4.config.nodes.PeerConfiguration;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class PeerConfigurationImpl implements PeerConfiguration {

	private ClientConfiguration clientConfig;
	private int localAS;
	private int remoteAS;
	private String peerName;
	private long localBgpIdentifier;
	private long remoteBgpIdentifier; 
	private int holdTime;
	private int idleHoldTime;
	private boolean allowAutomaticStart;
	private boolean allowAutomaticStop;
	private boolean collisionDetectEstablishedState;
	private boolean dampPeerOscillation;
	private boolean delayOpen;
	private boolean passiveTcpEstablishment;
	private int delayOpenTime;
	
	public PeerConfigurationImpl() {
		
	}

	public PeerConfigurationImpl(String peerName, ClientConfiguration clientConfig) throws ConfigurationException {
		setPeerName(peerName);
		setClientConfig(clientConfig);
	}

	public PeerConfigurationImpl(String peerName, ClientConfiguration clientConfig, int localAS, int remoteAS) throws ConfigurationException {
		this(peerName, clientConfig);
		setLocalAS(localAS);
		setRemoteAS(remoteAS);
	}

	public PeerConfigurationImpl(String peerName, ClientConfiguration clientConfig, int localAS, int remoteAS, 
			long localBgpIdentifier, long remoteBgpIdentifier) throws ConfigurationException {
		this(peerName, clientConfig, localAS, remoteAS);
		
		setLocalBgpIdentifier(localBgpIdentifier);
		setRemoteBgpIdentifier(remoteBgpIdentifier);
	}

	public PeerConfigurationImpl(String peerName, ClientConfiguration clientConfig, int localAS, int remoteAS, 
			long localBgpIdentifier, long remoteBgpIdentifier, int holdTime, int connectRetryInterval) throws ConfigurationException {
		this(peerName, clientConfig, localAS, remoteAS, localBgpIdentifier, remoteBgpIdentifier);

		setHoldTime(holdTime);
		setIdleHoldTime(connectRetryInterval);
	}
	
	public PeerConfigurationImpl(String peerName, ClientConfiguration clientConfig, int localAS, int remoteAS, 
			long localBgpIdentifier, long remoteBgpIdentifier, int holdTime, int connectRetryInterval,
			boolean allowAutomaticStart, boolean allowAutomaticStop, boolean dampPeerOscillation) throws ConfigurationException {
		this(peerName, clientConfig, localAS, remoteAS, localBgpIdentifier, remoteBgpIdentifier, holdTime, connectRetryInterval);
		
		setAllowAutomaticStart(allowAutomaticStart);
		setAllowAutomaticStop(allowAutomaticStop);
		setDampPeerOscillation(dampPeerOscillation);
	}

	public PeerConfigurationImpl(String peerName, ClientConfiguration clientConfig, int localAS, int remoteAS, 
			long localBgpIdentifier, long remoteBgpIdentifier, int holdTime, int connectRetryInterval,
			boolean allowAutomaticStart, boolean allowAutomaticStop, boolean dampPeerOscillation,
			boolean passiveTcpEstablishment, boolean delayOpen, int delayOpenTime) throws ConfigurationException {
		this(peerName, clientConfig, localAS, remoteAS, localBgpIdentifier, remoteBgpIdentifier, holdTime, connectRetryInterval,
				allowAutomaticStart, allowAutomaticStop, dampPeerOscillation);
		
		setPassiveTcpEstablishment(passiveTcpEstablishment);
		setDelayOpen(delayOpen);
		setDelayOpenTime(delayOpenTime);
	}

	public PeerConfigurationImpl(String peerName, ClientConfiguration clientConfig, int localAS, int remoteAS, 
			long localBgpIdentifier, long remoteBgpIdentifier, int holdTime, int connectRetryInterval,
			boolean allowAutomaticStart, boolean allowAutomaticStop, boolean dampPeerOscillation,
			boolean passiveTcpEstablishment, boolean delayOpen, int delayOpenTime,
			boolean collisionDetectEstablishedState) throws ConfigurationException {
		this(peerName, clientConfig, localAS, remoteAS, localBgpIdentifier, remoteBgpIdentifier, holdTime, connectRetryInterval,
				allowAutomaticStart, allowAutomaticStop, dampPeerOscillation, passiveTcpEstablishment, delayOpen, delayOpenTime);
		
		setCollisionDetectEstablishedState(collisionDetectEstablishedState);
	}

	@Override
	public ClientConfiguration getClientConfig() {
		return clientConfig;
	}

	@Override
	public int getLocalAS() {
		return localAS;
	}

	@Override
	public int getRemoteAS() {
		return remoteAS;
	}

	/**
	 * @param clientConfig the clientConfig to set
	 * @throws ConfigurationException 
	 */
	void setClientConfig(ClientConfiguration clientConfig) throws ConfigurationException {
		if(clientConfig == null)
			throw new ConfigurationException("null client configuration not allowed");
		
		if(!(clientConfig instanceof BgpClientPortConfigurationDecorator))
			clientConfig = new BgpClientPortConfigurationDecorator(clientConfig);
		
		this.clientConfig = clientConfig;
	}

	/**
	 * @param localAS the localAS to set
	 */
	void setLocalAS(int localAS) throws ConfigurationException {
		if(localAS <= 0)
			throw new ConfigurationException("negative AS number not allowed");
		
		this.localAS = localAS;
	}

	/**
	 * @param remoteAS the remoteAS to set
	 * @throws ConfigurationException 
	 */
	void setRemoteAS(int remoteAS) throws ConfigurationException {
		if(remoteAS <= 0)
			throw new ConfigurationException("negative AS number not allowed");

		this.remoteAS = remoteAS;
	}

	/**
	 * @return the name
	 */
	@Override
	public String getPeerName() {
		return peerName;
	}

	/**
	 * @param name the name to set
	 * @throws ConfigurationException 
	 */
	void setPeerName(String name) throws ConfigurationException {
		if(StringUtils.isBlank(name))
			throw new ConfigurationException("blank name not allowed");
		
		this.peerName = name;
	}

	/**
	 * @return the localBgpIdentifier
	 */
	public long getLocalBgpIdentifier() {
		return localBgpIdentifier;
	}

	/**
	 * @param localBgpIdentifier the localBgpIdentifier to set
	 */
	void setLocalBgpIdentifier(long localBgpIdentifier) throws ConfigurationException  {
		if(localBgpIdentifier <= 0)
			throw new ConfigurationException("Illegal local BGP identifier: " + localBgpIdentifier);
		this.localBgpIdentifier = localBgpIdentifier;
	}

	/**
	 * @return the remoteBgpIdentifier
	 */
	public long getRemoteBgpIdentifier() {
		return remoteBgpIdentifier;
	}

	/**
	 * @param remoteBgpIdentifier the remoteBgpIdentifier to set
	 */
	void setRemoteBgpIdentifier(long remoteBgpIdentifier) throws ConfigurationException  {
		if(remoteBgpIdentifier <= 0)
			throw new ConfigurationException("Illegal remote BGP identifier: " + remoteBgpIdentifier);
		this.remoteBgpIdentifier = remoteBgpIdentifier;
	}

	/**
	 * @return the holdTime
	 */
	public int getHoldTime() {
		return holdTime;
	}

	/**
	 * @param holdTime the holdTime to set
	 */
	void setHoldTime(int holdTime)  throws ConfigurationException {
		if(holdTime < 0)
			throw new ConfigurationException("Illegal hold time given: " + holdTime);
		
		this.holdTime = holdTime;
	}

	/**
	 * @return the connectRetryInterval
	 */
	public int getIdleHoldTime() {
		return idleHoldTime;
	}

	/**
	 * @param connectRetryInterval the connectRetryInterval to set
	 */
	void setIdleHoldTime(int connectRetryInterval)  throws ConfigurationException {
		if(connectRetryInterval < 0)
			throw new ConfigurationException("Illegal connect retry interval given: " + connectRetryInterval);
		
		this.idleHoldTime = connectRetryInterval;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return (new HashCodeBuilder())
				.append(clientConfig)
				.append(idleHoldTime)
				.append(holdTime)
				.append(localAS)
				.append(localBgpIdentifier)
				.append(peerName)
				.append(remoteAS)
				.append(remoteBgpIdentifier)
				.append(delayOpenTime)
				.append(allowAutomaticStart)
				.append(allowAutomaticStop)
				.append(collisionDetectEstablishedState)
				.append(dampPeerOscillation)
				.append(delayOpen)
				.append(passiveTcpEstablishment)
				.toHashCode();
				
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(getClass() != obj.getClass())
			return false;
		
		PeerConfigurationImpl o = (PeerConfigurationImpl)obj;
		
		return (new EqualsBuilder())
				.append(clientConfig, o.clientConfig)
				.append(idleHoldTime, o.idleHoldTime)	
				.append(holdTime, o.holdTime)
				.append(localAS, o.localAS)
				.append(localBgpIdentifier, o.localBgpIdentifier)
				.append(peerName, o.peerName)
				.append(remoteAS, o.remoteAS)
				.append(remoteBgpIdentifier, o.remoteBgpIdentifier)
				.append(delayOpenTime, o.delayOpenTime)
				.append(allowAutomaticStart, o.allowAutomaticStart)
				.append(allowAutomaticStop, o.allowAutomaticStop)
				.append(collisionDetectEstablishedState, o.collisionDetectEstablishedState)
				.append(dampPeerOscillation, o.dampPeerOscillation)
				.append(delayOpen, o.delayOpen)
				.append(passiveTcpEstablishment, o.passiveTcpEstablishment)
				.isEquals();
	}

	/**
	 * @return the allowAutomaticStart
	 */
	public boolean isAllowAutomaticStart() {
		return allowAutomaticStart;
	}

	/**
	 * @param allowAutomaticStart the allowAutomaticStart to set
	 */
	void setAllowAutomaticStart(boolean allowAutomaticStart) {
		this.allowAutomaticStart = allowAutomaticStart;
	}

	/**
	 * @return the allowAutomaticStop
	 */
	public boolean isAllowAutomaticStop() {
		return allowAutomaticStop;
	}

	/**
	 * @param allowAutomaticStop the allowAutomaticStop to set
	 */
	void setAllowAutomaticStop(boolean allowAutomaticStop) {
		this.allowAutomaticStop = allowAutomaticStop;
	}

	/**
	 * @return the collisionDetectEstablishedEnabledState
	 */
	public boolean isCollisionDetectEstablishedState() {
		return collisionDetectEstablishedState;
	}

	/**
	 * @param collisionDetectEstablishedEnabledState the collisionDetectEstablishedEnabledState to set
	 */
	void setCollisionDetectEstablishedState(
			boolean collisionDetectEstablishedEnabledState) {
		this.collisionDetectEstablishedState = collisionDetectEstablishedEnabledState;
	}

	/**
	 * @return the dampPeerOscillation
	 */
	public boolean isDampPeerOscillation() {
		return dampPeerOscillation;
	}

	/**
	 * @param dampPeerOscillation the dampPeerOscillation to set
	 */
	void setDampPeerOscillation(boolean dampPeerOscillation) {
		this.dampPeerOscillation = dampPeerOscillation;
	}

	/**
	 * @return the delayOpen
	 */
	public boolean isDelayOpen() {
		return delayOpen;
	}

	/**
	 * @param delayOpen the delayOpen to set
	 */
	void setDelayOpen(boolean delayOpen) {
		this.delayOpen = delayOpen;
	}

	/**
	 * @return the passiveTcpEstablishment
	 */
	public boolean isPassiveTcpEstablishment() {
		return passiveTcpEstablishment;
	}

	/**
	 * @param passiveTcpEstablishment the passiveTcpEstablishment to set
	 */
	void setPassiveTcpEstablishment(boolean passiveTcpEstablishment) {
		this.passiveTcpEstablishment = passiveTcpEstablishment;
	}

	/**
	 * @return the delayOpenTime
	 */
	public int getDelayOpenTime() {
		return delayOpenTime;
	}

	/**
	 * @param delayOpenTime the delayOpenTime to set
	 * @throws ConfigurationException 
	 */
	void setDelayOpenTime(int delayOpenTime) throws ConfigurationException {
		if(delayOpenTime < 0)
			throw new ConfigurationException("Illegal delay open time given: " + delayOpenTime);

		this.delayOpenTime = delayOpenTime;
	}

}
