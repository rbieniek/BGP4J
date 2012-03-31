/**
 * 
 */
package org.bgp4j.extensions;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import org.bgp4j.net.AddressFamilyKey;
import org.bgp4j.net.RIBSide;


/**
 * @author rainer
 *
 */
public class ProvidedRIBs {

	public static class SideKeyPair {
		private RIBSide side;
		private AddressFamilyKey key;
		
		public SideKeyPair(RIBSide side, AddressFamilyKey key) {
			this.side = side;
			this.key = key;
		}

		public RIBSide getSide() {
			return side;
		}

		public AddressFamilyKey getKey() {
			return key;
		}

	}
	
	private String peerName;
	private Collection<SideKeyPair> sideKeyPairs = new LinkedList<ProvidedRIBs.SideKeyPair>();
	
	public ProvidedRIBs(String peerName, Collection<SideKeyPair> sideKeyPairs) {
		this.peerName = peerName;
		
		if(sideKeyPairs != null)
			this.sideKeyPairs.addAll(sideKeyPairs);
		this.sideKeyPairs = Collections.unmodifiableCollection(this.sideKeyPairs);
	}

	public String getPeerName() {
		return peerName;
	}

	public Collection<SideKeyPair> getSideKeyPairs() {
		return sideKeyPairs;
	}
	
}
