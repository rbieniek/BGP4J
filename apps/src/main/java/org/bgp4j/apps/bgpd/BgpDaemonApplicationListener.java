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
 */
package org.bgp4j.apps.bgpd;

import java.util.Set;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.LogManager;
import org.apache.log4j.xml.DOMConfigurator;
import org.bgp4j.apps.bgpd.config.ConfigurationFileProcessor;
import org.bgp4j.extensions.Extension;
import org.bgp4j.extensions.ExtensionsFactory;
import org.bgp4j.management.web.service.WebManagementService;
import org.bgp4j.netty.service.BGPv4Service;
import org.bgp4j.rib.web.server.RIBManagementServer;
import org.bgp4j.weld.SeApplicationStartEvent;
import org.jboss.weld.environment.se.bindings.Parameters;
import org.slf4j.Logger;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class BgpDaemonApplicationListener {
	private @Inject Logger log;
	private @Inject @Parameters String[] commandLine;
	private @Inject ConfigurationFileProcessor configurationFileProcessor;
	private @Inject BGPv4Service  bgpService;
	private @Inject WebManagementService webManagementService;
	private @Inject ExtensionsFactory extensionsFactory;
	private @Inject RIBManagementServer ribServer;
	
	public void listen(@Observes @BgpDaemonApplicationSelector SeApplicationStartEvent event) throws Exception {
		BasicConfigurator.configure();

		try {
			Options options = new Options();
			Option option;
			
			option = new Option("c", "config-file", true, "XML configuration file (required)");
			option.setRequired(true);
			options.addOption(option);

			option = new Option("l", "log4-file", true, "Log4J XML configuration file (optional");
			option.setRequired(false);
			options.addOption(option);

			option = new Option("t", "test-config-file", false, "test XML configuration file and exit (optional)");
			option.setRequired(false);
			options.addOption(option);

			CommandLine cmd = (new PosixParser()).parse(options, commandLine);

			if(cmd.hasOption("l")) {
				LogManager.resetConfiguration();
				DOMConfigurator.configure(cmd.getOptionValue("l"));
			}

			boolean testOnly = cmd.hasOption("t");
			
			configurationFileProcessor.processConfigFile(cmd.getOptionValue("c"));

			if(!testOnly) {
				for(Extension extension : extensionsFactory.listExtensions()) {
					if(extension.isReadyForService()) {
						Set<Object> managementObjects = extension.getManagementObjects();
						
						if(managementObjects != null) {
							for(Object managementObject : managementObjects)
								webManagementService.registerSingleton(managementObject);
						}
	
						extension.startExtension();
					}
				}
				
				webManagementService.registerSingleton(ribServer);
				
				webManagementService.startService();			
				bgpService.startService();
			}			
		} catch(Exception e) {
			log.error("failed to run client", e);
			
			throw e;
		}
	}
}
