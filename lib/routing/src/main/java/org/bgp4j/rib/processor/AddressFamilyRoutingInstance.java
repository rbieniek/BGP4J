/**
 * 
 */
package org.bgp4j.rib.processor;

import javax.inject.Inject;

import org.bgp4j.config.nodes.AddressFamilyRoutingPeerConfiguration;
import org.bgp4j.net.AddressFamilyKey;
import org.slf4j.Logger;

/**
 * @author rainer
 *
 */
public class AddressFamilyRoutingInstance {

	private @Inject Logger log;

	private AddressFamilyKey addressFamilyKey;
	
	void configure(AddressFamilyKey addressFamilyKey, AddressFamilyRoutingPeerConfiguration firstConfig, AddressFamilyRoutingPeerConfiguration secondConfig) {
		
	}
	
	void startInstance() {
		
	}
	
	void stopInstance() {
		
	}

	/**
	 * @return the addressFamilyKey
	 */
	public AddressFamilyKey getAddressFamilyKey() {
		return addressFamilyKey;
	}
}
