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
 * File: org.bgp4.config.nodes.impl.ServerConfigurationParser.java 
 */
package org.bgp4j.config.nodes.impl;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.bgp4j.config.nodes.ClientConfiguration;
import org.slf4j.Logger;

/**
 * Parse a generic server configuration
 * 
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
@Singleton
public class ClientConfigurationParser {
	private @Inject Logger log;
	
	public ClientConfiguration parseConfig(HierarchicalConfiguration config) throws ConfigurationException {
		InetAddress address = null;
		int port =  config.getInt("[@port]", 0);
		String listenRep = config.getString("[@remote]");

		if(StringUtils.isBlank(listenRep))
			throw new ConfigurationException("remote attribute required");
		
		try {
			address = InetAddress.getByName(listenRep);
		} catch (UnknownHostException e) {
			log.error("failed to parse remote address: " + listenRep, e);
			
			throw new ConfigurationException(e);
		}
		
		return new ClientConfigurationImpl(new InetSocketAddress(address, port));
	}
}
