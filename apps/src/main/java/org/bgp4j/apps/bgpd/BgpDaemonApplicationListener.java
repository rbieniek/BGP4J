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

import javax.enterprise.event.Observes;
import javax.inject.Inject;

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

	
	public void listen(@Observes @BgpDaemonApplicationSelector SeApplicationStartEvent event) throws Exception {
		try {
		} catch(Exception e) {
			log.error("failed to run client", e);
			
			throw e;
		}
	}
}
