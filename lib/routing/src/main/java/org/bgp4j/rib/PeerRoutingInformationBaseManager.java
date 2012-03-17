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
 * File: org.bgp4j.rib.RoutingInformationBaseManager.java 
 */
package org.bgp4j.rib;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.event.Event;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * 
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
@Singleton
public class PeerRoutingInformationBaseManager {

	private Map<String, PeerRoutingInformationBase> peerRibs = Collections.synchronizedMap(new HashMap<String, PeerRoutingInformationBase>());
	private @Inject Event<PeerRoutingInformationBaseCreated> peerRibCreated;
	private @Inject Instance<PeerRoutingInformationBase> pribProvider;
	
	public PeerRoutingInformationBase peerRoutingInformationBase(String peerName) {
		if(StringUtils.isBlank(peerName))
			throw new IllegalArgumentException("empty peer name");
		
		PeerRoutingInformationBase result = null;
		boolean created = false;
		
		synchronized (peerRibs) {
			if(!peerRibs.containsKey(peerName)) {
				PeerRoutingInformationBase prib = pribProvider.get();
				
				prib.setPeerName(peerName);
				peerRibs.put(peerName, prib);
				created = true;
			}
			result = peerRibs.get(peerName);
		}
		
		if(created)
			peerRibCreated.fire(new PeerRoutingInformationBaseCreated(peerName));
		
		return result;
	}


	void reset() {
		peerRibs.clear();
	}
}
