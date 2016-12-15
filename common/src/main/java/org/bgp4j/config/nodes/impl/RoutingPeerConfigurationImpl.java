/**
 * 
 */
package org.bgp4j.config.nodes.impl;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.bgp4j.config.nodes.AddressFamilyRoutingPeerConfiguration;
import org.bgp4j.config.nodes.RoutingPeerConfiguration;

/**
 * @author rainer
 *
 */
public class RoutingPeerConfigurationImpl implements RoutingPeerConfiguration {

	private String peerName;
	private Set<AddressFamilyRoutingPeerConfiguration> addressFamilyConfigrations = new TreeSet<AddressFamilyRoutingPeerConfiguration>();
	
	RoutingPeerConfigurationImpl() {}
	
	RoutingPeerConfigurationImpl(String peerName, Collection<AddressFamilyRoutingPeerConfiguration> addressFamilyConfigrations) {
		setPeerName(peerName);
		
		if(addressFamilyConfigrations != null)
			this.addressFamilyConfigrations.addAll(addressFamilyConfigrations);
	}
	
	/* (non-Javadoc)
	 * @see org.bgp4j.config.nodes.RoutingPeerConfiguration#getPeerName()
	 */
	@Override
	public String getPeerName() {
		return peerName;
	}

	/* (non-Javadoc)
	 * @see org.bgp4j.config.nodes.RoutingPeerConfiguration#getAddressFamilyConfigrations()
	 */
	@Override
	public Set<AddressFamilyRoutingPeerConfiguration> getAddressFamilyConfigrations() {
		return addressFamilyConfigrations;
	}

	/**
	 * @param peerName the peerName to set
	 */
	void setPeerName(String peerName) {
		this.peerName = peerName;
	}

	/**
	 * @param addressFamilyConfigrations the addressFamilyConfigrations to set
	 */
	void setAddressFamilyConfigrations(Set<AddressFamilyRoutingPeerConfiguration> addressFamilyConfigrations) {
		this.addressFamilyConfigrations.clear();

		if(addressFamilyConfigrations != null)
			this.addressFamilyConfigrations.addAll(addressFamilyConfigrations);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(RoutingPeerConfiguration o) {
		CompareToBuilder builder = (new CompareToBuilder())
				.append(getPeerName(), o.getPeerName())
				.append(getAddressFamilyConfigrations().size(), o.getAddressFamilyConfigrations().size());
		
		if(builder.toComparison() == 0) {
			Iterator<AddressFamilyRoutingPeerConfiguration> lit = getAddressFamilyConfigrations().iterator();
			Iterator<AddressFamilyRoutingPeerConfiguration> rit = o.getAddressFamilyConfigrations().iterator();
			
			while(lit.hasNext())
				builder.append(lit.next(), rit.next());
		}
		
		return builder.toComparison();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		HashCodeBuilder builder = (new HashCodeBuilder())
				.append(getPeerName());
		
		for(AddressFamilyRoutingPeerConfiguration afrc : getAddressFamilyConfigrations())
			builder.append(afrc);
		
		return builder.toHashCode();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof RoutingPeerConfiguration))
			return false;
		
		RoutingPeerConfiguration o = (RoutingPeerConfiguration)obj;
		
		EqualsBuilder builder = (new EqualsBuilder())
				.append(getPeerName(), o.getPeerName())
				.append(getAddressFamilyConfigrations().size(), o.getAddressFamilyConfigrations().size());
		
		if(builder.isEquals()) {
			Iterator<AddressFamilyRoutingPeerConfiguration> lit = getAddressFamilyConfigrations().iterator();
			Iterator<AddressFamilyRoutingPeerConfiguration> rit = o.getAddressFamilyConfigrations().iterator();
			
			while(lit.hasNext())
				builder.append(lit.next(), rit.next());
		}
		
		return builder.isEquals();
	}

}
