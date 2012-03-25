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
 * File: org.bgp4j.rib.web.server.RIBManagementServer.java 
 */
package org.bgp4j.rib.web.server;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.bgp4j.rib.PeerRoutingInformationBaseManager;
import org.bgp4j.rib.web.dto.RIBCollection;
import org.bgp4j.rib.web.interfaces.RIBManagement;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
@Singleton
public class RIBManagementServer implements RIBManagement {

	private @Inject PeerRoutingInformationBaseManager pribManager; 
	
	@Override
	public RIBCollection listRIBs() {
		RIBCollection result = new RIBCollection();
		
		return result;
	}

}
