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
 * File: org.bgp4.config.impl.BgpServerConfigurationImpl.java 
 */
package org.bgp4j.config.nodes.impl;

import org.bgp4j.config.nodes.HttpServerConfiguration;
import org.bgp4j.config.nodes.ServerConfiguration;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class HttpServerConfigurationImpl implements HttpServerConfiguration {

	private ServerConfiguration serverConfiguration;
	
	public HttpServerConfigurationImpl() {}
	
	public HttpServerConfigurationImpl(ServerConfiguration serverConfiguration) {
		this.serverConfiguration = serverConfiguration;
	}
	
	@Override
	public ServerConfiguration getServerConfiguration() {
		return this.serverConfiguration;
	}

	void setServerConfiguration(ServerConfiguration config) {
		if(!(config instanceof HttpServerPortConfigurationDecorator))
			config = new HttpServerPortConfigurationDecorator(config);
		
		this.serverConfiguration = config;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((serverConfiguration == null) ? 0 : serverConfiguration
						.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HttpServerConfigurationImpl other = (HttpServerConfigurationImpl) obj;
		if (serverConfiguration == null) {
			if (other.serverConfiguration != null)
				return false;
		} else if (!serverConfiguration.equals(other.serverConfiguration))
			return false;
		return true;
	}

}
