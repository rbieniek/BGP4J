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
 * File: org.bgp4j.rib.web.service.HttpServer.java 
 */
package org.bgp4j.management.web.service;

import org.bgp4.config.nodes.HttpServerConfiguration;
import org.bgp4j.management.web.application.ManagementApplication;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher;

/**
 * @author Rainer Bieniek (rainer@bgp4j.org)
 *
 */
public class WebManagementServer {

	private HttpServerConfiguration serviceConfig;
	private Server httpServer;
	
	/**
	 * @param serviceConfig
	 */
	public void setConfiguration(HttpServerConfiguration serviceConfig) {
		this.serviceConfig = serviceConfig;
	}

	/**
	 * @throws Exception 
	 * 
	 */
	public void startServer() throws Exception {
		ServletContextHandler handler = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
		
		httpServer = new Server(serviceConfig.getServerConfiguration().getListenAddress());

		handler.setContextPath("/rest");
		handler.setInitParameter("javax.ws.rs.Application", ManagementApplication.class.getName());
		
		handler.addServlet(new ServletHolder(new PingServlet()), "/ping");
		handler.addServlet(new ServletHolder(new HttpServletDispatcher()), "/*");

		httpServer.setHandler(handler);
		httpServer.start();
	}

	/**
	 * @throws Exception 
	 * 
	 */
	public void stopServer() throws Exception {
		if(httpServer != null)
			httpServer.stop();
	}

}
