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
	private int localBgpIdentifier;
	private int remoteBgpIdentifier; 
	private int holdTime;
	private int connectRetryInterval;
	
	public PeerConfigurationImpl() {
		
	}

	public PeerConfigurationImpl(String peerName, ClientConfiguration clientConfig, int localAS, int remoteAS) throws ConfigurationException {
		setPeerName(peerName);
		setClientConfig(clientConfig);
		setLocalAS(localAS);
		setRemoteAS(remoteAS);
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
	public int getLocalBgpIdentifier() {
		return localBgpIdentifier;
	}

	/**
	 * @param localBgpIdentifier the localBgpIdentifier to set
	 */
	void setLocalBgpIdentifier(int localBgpIdentifier) throws ConfigurationException  {
		if(localBgpIdentifier <= 0)
			throw new ConfigurationException("Illegal local BGP identifier: " + localBgpIdentifier);
		this.localBgpIdentifier = localBgpIdentifier;
	}

	/**
	 * @return the remoteBgpIdentifier
	 */
	public int getRemoteBgpIdentifier() {
		return remoteBgpIdentifier;
	}

	/**
	 * @param remoteBgpIdentifier the remoteBgpIdentifier to set
	 */
	void setRemoteBgpIdentifier(int remoteBgpIdentifier) throws ConfigurationException  {
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
	public int getConnectRetryInterval() {
		return connectRetryInterval;
	}

	/**
	 * @param connectRetryInterval the connectRetryInterval to set
	 */
	void setConnectRetryInterval(int connectRetryInterval)  throws ConfigurationException {
		if(connectRetryInterval < 0)
			throw new ConfigurationException("Illegal connect retry interval given: " + connectRetryInterval);
		
		this.connectRetryInterval = connectRetryInterval;
	}

}
