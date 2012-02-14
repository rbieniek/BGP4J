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
 * File: org.bgp4.config.nodes.impl.ServerConfigurationImpl.java 
 */
package org.bgp4.config.nodes.impl;

import java.net.InetSocketAddress;

import org.bgp4.config.nodes.ServerConfiguration;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class ServerConfigurationImpl implements ServerConfiguration {

	private InetSocketAddress listenAddress;
	

	public ServerConfigurationImpl() {
		
	}
	
	public ServerConfigurationImpl(InetSocketAddress listenAddress) {
		super();
		this.listenAddress = listenAddress;
	}
	@Override
	public InetSocketAddress getListenAddress() {
		return listenAddress;
	}

	/**
	 * @param listenAddress the listenAddress to set
	 */
	void setListenAddress(InetSocketAddress listenAddress) {
		this.listenAddress = listenAddress;
	}

}
