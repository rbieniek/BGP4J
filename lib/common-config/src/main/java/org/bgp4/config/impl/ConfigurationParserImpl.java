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
 * File: org.bgp4.config.impl.ConfigurationParserImpl.java 
 */
package org.bgp4.config.impl;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.bgp4.config.Configuration;
import org.bgp4.config.nodes.impl.BgpServerConfigurationParser;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
@Singleton
public class ConfigurationParserImpl {

	private @Inject BgpServerConfigurationParser bgpServerConfigurationParser;
	
	public Configuration parseConfiguration(XMLConfiguration configuration) throws ConfigurationException {
		ConfigurationImpl configImpl = new ConfigurationImpl();
		List<HierarchicalConfiguration> bgpServerNodes = configuration.configurationsAt("BgpServer");
		
		if(bgpServerNodes.size() > 1)
			throw new ConfigurationException("Duplicate <BgpServer /> element");
		else if(bgpServerNodes.size() == 1)
			configImpl.setBgpServerConfiguration(bgpServerConfigurationParser.parseConfiguration(bgpServerNodes.get(0)));
		
		return configImpl;
	}

}
