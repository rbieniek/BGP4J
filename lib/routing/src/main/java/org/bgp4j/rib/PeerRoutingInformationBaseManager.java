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
import java.util.Map.Entry;

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
public class PeerRoutingInformationBaseManager implements ExtensionRoutingBaseManager {

	private Map<String, PeerRoutingInformationBase> peerRibs = Collections.synchronizedMap(new HashMap<String, PeerRoutingInformationBase>());
	private @Inject Event<PeerRoutingInformationBaseCreated> peerRibCreated;
	private @Inject Event<PeerRoutingInformationBaseDestroyed> peerRibDestroyed;
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

	/* (non-Javadoc)
	 * @see org.bgp4j.rib.ExtensionRoutingBaseManager#extensionRoutingInformationBase(java.lang.String, java.lang.String)
	 */
	@Override
	public PeerRoutingInformationBase extensionRoutingInformationBase(String extensionName, String key) {
		if(StringUtils.isBlank(extensionName))
			throw new IllegalArgumentException("empty extension name");

		if(StringUtils.isBlank(key))
			throw new IllegalArgumentException("empty key");
		
		PeerRoutingInformationBase prib = peerRoutingInformationBase(extensionName + "_" + key);
		
		prib.setExtensionRoutingBase(true);
		
		return prib;
	}
	
	public void destroyPeerRoutingInformationBase(String peerName) {
		PeerRoutingInformationBase prib = null;
		
		synchronized (peerRibs) {
			prib = peerRibs.remove(peerName);
		}
		
		if(prib != null)
			peerRibDestroyed.fire(new PeerRoutingInformationBaseDestroyed(prib.getPeerName()));
	}
	
	public void vistPeerRoutingBases(PeerRoutingInformationBaseVisitor visitor) {
		synchronized (peerRibs) {
			for(Entry<String, PeerRoutingInformationBase> entry : peerRibs.entrySet()) {
				entry.getValue().vistPeerRoutingBases(visitor);
			}
		}
	}
	
	/**
	 * completely reset the manager and release all RIB instances inside
	 */
	public void resetManager() {
		peerRibs.clear();
	}

}
