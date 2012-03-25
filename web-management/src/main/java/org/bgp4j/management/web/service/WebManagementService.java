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
 * File: org.bgp4j.rib.web.service.HttpService.java 
 */
package org.bgp4j.management.web.service;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.bgp4.config.global.ApplicationConfiguration;
import org.bgp4.config.nodes.HttpServerConfiguration;

/**
 * @author Rainer Bieniek (rainer@bgp4j.org)
 *
 */
public class WebManagementService {
	private @Inject ApplicationConfiguration appConfig;
	private @Inject Instance<WebManagementServer> httpServerProvider;
	private WebManagementServer httpServer;

	public void startService() throws Exception {
		HttpServerConfiguration serviceConfig = appConfig.getHttpServerConfiguration();
		
		if(serviceConfig != null) {
			httpServer = httpServerProvider.get();
			
			httpServer.setConfiguration(serviceConfig);
			httpServer.startServer();
		}
	}
	
	public void stopService() throws Exception {
		if(httpServer != null)
			httpServer.stopServer();
	}
}
