/**
 * 
 */
package org.bgp4j.config.nodes.impl;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.bgp4j.config.nodes.RoutingInstanceConfiguration;
import org.bgp4j.config.nodes.RoutingPeerConfiguration;

/**
 * @author rainer
 *
 */
class RoutingInstanceConfigurationImpl implements RoutingInstanceConfiguration {
	private RoutingPeerConfiguration firstPeer;
	private RoutingPeerConfiguration secondPeer;

	RoutingInstanceConfigurationImpl() {}

	RoutingInstanceConfigurationImpl(RoutingPeerConfiguration firstPeer, RoutingPeerConfiguration secondPeer) {
		setFirstPeer(firstPeer);
		setSecondPeer(secondPeer);
	}
	
	/* (non-Javadoc)
	 * @see org.bgp4j.config.nodes.RoutingInstanceConfuration#getFirstPeer()
	 */
	@Override
	public RoutingPeerConfiguration getFirstPeer() {
		return firstPeer;
	}

	/* (non-Javadoc)
	 * @see org.bgp4j.config.nodes.RoutingInstanceConfuration#getSecondPeer()
	 */
	@Override
	public RoutingPeerConfiguration getSecondPeer() {
		return secondPeer;
	}

	/**
	 * @param firstPeer the firstPeer to set
	 */
	public void setFirstPeer(RoutingPeerConfiguration firstPeer) {
		this.firstPeer = firstPeer;
	}

	/**
	 * @param secondPeer the secondPeer to set
	 */
	public void setSecondPeer(RoutingPeerConfiguration secondPeer) {
		this.secondPeer = secondPeer;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(RoutingInstanceConfiguration o) {
		return (new CompareToBuilder())
				.append(getFirstPeer(), o.getFirstPeer())
				.append(getSecondPeer(), o.getSecondPeer())
				.toComparison();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return (new HashCodeBuilder())
				.append(getFirstPeer())
				.append(getSecondPeer())
				.toHashCode();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof RoutingInstanceConfiguration))
			return false;
		
		RoutingInstanceConfiguration o = (RoutingInstanceConfiguration)obj;

		return (new EqualsBuilder())
				.append(getFirstPeer(), o.getFirstPeer())
				.append(getSecondPeer(), o.getSecondPeer())
				.isEquals();
	}

}
