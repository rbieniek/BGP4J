/**
 * 
 */
package org.bgp4.config.nodes.impl;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.bgp4.config.nodes.AddressFamilyRoutingConfiguration;
import org.bgp4.config.nodes.RoutingConfiguration;

/**
 * @author rainer
 *
 */
public class RoutingConfigurationImpl implements RoutingConfiguration {

	private Set<AddressFamilyRoutingConfiguration> routingConfigurations = new TreeSet<AddressFamilyRoutingConfiguration>();
	
	RoutingConfigurationImpl() {}
	
	RoutingConfigurationImpl(Collection<AddressFamilyRoutingConfiguration> routingConfigurations) {
		this.routingConfigurations.addAll(routingConfigurations);
	}
	
	/* (non-Javadoc)
	 * @see org.bgp4.config.nodes.RoutingConfiguration#getRoutingConfigurations()
	 */
	@Override
	public Set<AddressFamilyRoutingConfiguration> getRoutingConfigurations() {
		return routingConfigurations;
	}

	/**
	 * @param routingConfigurations the routingConfigurations to set
	 */
	void setRoutingConfigurations(Set<AddressFamilyRoutingConfiguration> routingConfigurations) {
		this.routingConfigurations.clear();
		
		if(routingConfigurations != null)
			this.routingConfigurations.addAll(routingConfigurations);
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(RoutingConfiguration o) {
		CompareToBuilder builder = (new CompareToBuilder())
				.append(getRoutingConfigurations().size(), o.getRoutingConfigurations().size());
		
		if(builder.toComparison() == 0) {
			Iterator<AddressFamilyRoutingConfiguration> lit = getRoutingConfigurations().iterator();
			Iterator<AddressFamilyRoutingConfiguration> rit = o.getRoutingConfigurations().iterator();
			
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
		HashCodeBuilder builder = new HashCodeBuilder();
		
		for(AddressFamilyRoutingConfiguration route : getRoutingConfigurations())
			builder.append(route);
		
		return builder.toHashCode();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof RoutingConfiguration))
			return false;
		
		return (compareTo((RoutingConfiguration)obj) == 0);
	}

}
