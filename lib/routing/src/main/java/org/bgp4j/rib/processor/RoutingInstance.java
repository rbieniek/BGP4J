/**
 * 
 */
package org.bgp4j.rib.processor;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.bgp4j.config.nodes.AddressFamilyRoutingPeerConfiguration;
import org.bgp4j.config.nodes.RoutingInstanceConfiguration;
import org.bgp4j.net.AddressFamilyKey;
import org.bgp4j.rib.PeerRoutingInformationBaseManager;
import org.slf4j.Logger;

/**
 * @author rainer
 *
 */
public class RoutingInstance {

	private @Inject Logger log;
	private @Inject Instance<AddressFamilyRoutingInstance> familyInstanceProvider;
	private String firstPeerName;
	private String secondPeerName;
	private @Inject PeerRoutingInformationBaseManager pribManager;
	private RoutingInstanceState state = RoutingInstanceState.STOPPED;
	
	private List<AddressFamilyRoutingInstance> familyInstances = new LinkedList<AddressFamilyRoutingInstance>();

	void configure(RoutingInstanceConfiguration instConfig) {
		setFirstPeerName(instConfig.getFirstPeer().getPeerName());
		setSecondPeerName(instConfig.getSecondPeer().getPeerName());
		
		Map<AddressFamilyKey, AddressFamilyRoutingPeerConfiguration> firstFamilyRouting = new HashMap<AddressFamilyKey, AddressFamilyRoutingPeerConfiguration>();
		Map<AddressFamilyKey, AddressFamilyRoutingPeerConfiguration> secondFamilyRouting = new HashMap<AddressFamilyKey, AddressFamilyRoutingPeerConfiguration>();
		Set<AddressFamilyKey> wantedFamilies = new HashSet<AddressFamilyKey>();
		
		for(AddressFamilyRoutingPeerConfiguration afrfc : instConfig.getFirstPeer().getAddressFamilyConfigrations()) {
			firstFamilyRouting.put(afrfc.getAddressFamilyKey(), afrfc);
			wantedFamilies.add(afrfc.getAddressFamilyKey());
		}
		for(AddressFamilyRoutingPeerConfiguration afrfc : instConfig.getSecondPeer().getAddressFamilyConfigrations()) {
			secondFamilyRouting.put(afrfc.getAddressFamilyKey(), afrfc);
			wantedFamilies.add(afrfc.getAddressFamilyKey());
		}
		
		for(AddressFamilyKey afk : wantedFamilies) {
			AddressFamilyRoutingInstance instance = familyInstanceProvider.get();
			
			instance.configure(afk, firstFamilyRouting.get(afk), secondFamilyRouting.get(afk));
			familyInstances.add(instance);
		}
		
		familyInstances = Collections.unmodifiableList(familyInstances);
	}

	void startInstance() {
		if(pribManager.isPeerRoutingInformationBaseAvailable(getFirstPeerName())) {
			if(pribManager.isPeerRoutingInformationBaseAvailable(getSecondPeerName())) {
				state = RoutingInstanceState.STARTING;
				
				for(AddressFamilyRoutingInstance instance : getFamilyInstances()) {
					log.info("Starting routing instance for " + instance.getAddressFamilyKey());
					
					try {
						instance.startInstance(pribManager.peerRoutingInformationBase(getFirstPeerName()), 
								pribManager.peerRoutingInformationBase(getSecondPeerName()));
						
						if(instance.getState() != RoutingInstanceState.RUNNING) {
							log.error("failed to routing instance for " + instance.getAddressFamilyKey() + " with state " + instance.getState());
							state = RoutingInstanceState.PARTLY_RUNNING;
						}
					} catch(Throwable t) {
						log.error("failed to routing instance for " + instance.getAddressFamilyKey(), t);
						
						state = RoutingInstanceState.PARTLY_RUNNING;
					}
				}
				
				if(state == RoutingInstanceState.STARTING)
					state = RoutingInstanceState.RUNNING;
			} else {
				state = RoutingInstanceState.PEER_ROUTING_BASE_UNAVAILABLE;
			}			
		} else {
			state = RoutingInstanceState.PEER_ROUTING_BASE_UNAVAILABLE;	
		}		
	}
	
	void stopInstance() {
		for(AddressFamilyRoutingInstance instance : getFamilyInstances()) {
			log.info("Stopping routing instance for " + instance.getAddressFamilyKey());
			
			try {
				instance.stopInstance();
			} catch(Throwable t) {
				log.error("failed to routing instance for " + instance.getAddressFamilyKey(), t);
			}
		}		
	}

	/**
	 * @return the firstPeerName
	 */
	public String getFirstPeerName() {
		return firstPeerName;
	}

	/**
	 * @param firstPeerName the firstPeerName to set
	 */
	private void setFirstPeerName(String firstPeerName) {
		this.firstPeerName = firstPeerName;
	}

	/**
	 * @return the secondPeerName
	 */
	public String getSecondPeerName() {
		return secondPeerName;
	}

	/**
	 * @param secondPeerName the secondPeerName to set
	 */
	private void setSecondPeerName(String secondPeerName) {
		this.secondPeerName = secondPeerName;
	}

	/**
	 * @return the familyInstances
	 */
	public List<AddressFamilyRoutingInstance> getFamilyInstances() {
		return familyInstances;
	}

	/**
	 * @return the state
	 */
	public RoutingInstanceState getState() {
		return state;
	}
}
