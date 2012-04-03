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
package org.bgp4j.apps.easybox;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.LogManager;
import org.apache.log4j.xml.DOMConfigurator;
import org.bgp4j.config.global.ApplicationConfiguration;
import org.bgp4j.extension.snmp4j.service.EasyboxService;
import org.bgp4j.extension.snmp4j.web.EasyboxWebApplication;
import org.bgp4j.management.web.service.WebManagementService;
import org.bgp4j.weld.SeApplicationStartEvent;
import org.jboss.weld.environment.se.bindings.Parameters;
import org.slf4j.Logger;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class EasyboxDaemonApplicationListener {
	private @Inject Logger log;
	private @Inject @Parameters String[] commandLine;
	private @Inject ConfigurationFileProcessor configurationFileProcessor;
	private @Inject WebManagementService webManagementService;
	private @Inject ApplicationConfiguration appConfig;
	private @Inject EasyboxService easyboxService;
	private @Inject EasyboxWebApplication webApplication;
	
	public void listen(@Observes @EasyboxDaemonApplicationSelector SeApplicationStartEvent event) throws Exception {
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
			
			CommandLine cmd = (new PosixParser()).parse(options, commandLine);

			if(cmd.hasOption("l")) {
				LogManager.resetConfiguration();
				DOMConfigurator.configure(cmd.getOptionValue("l"));
			}
			
			configurationFileProcessor.processConfigFile(cmd.getOptionValue("c"));
			appConfig.setHttpServerConfiguration(configurationFileProcessor.getApplicationConfiguration().getHttpServer());
			easyboxService.configure(configurationFileProcessor.getApplicationConfiguration().getEasyboxes());
			
			webApplication.setService(easyboxService);
			webManagementService.registerSingleton(webApplication);
			
			webManagementService.startService();			
			easyboxService.startService();
		} catch(Exception e) {
			log.error("failed to run client", e);
			
			throw e;
		}
	}
}
