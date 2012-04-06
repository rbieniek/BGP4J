/**
 * 
 */
package org.bgp4j.config.nodes.impl;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.bgp4j.config.nodes.RoutingInstanceConfiguration;
import org.bgp4j.config.nodes.RoutingProcessorConfiguration;

/**
 * @author rainer
 *
 */
class RoutingProcessorConfigurationImpl implements RoutingProcessorConfiguration {

	private Set<RoutingInstanceConfiguration> routingInstances = new TreeSet<RoutingInstanceConfiguration>();
	
	/* (non-Javadoc)
	 * @see org.bgp4j.config.nodes.RoutingProcessorConfiguration#getRoutingInstances()
	 */
	@Override
	public Set<RoutingInstanceConfiguration> getRoutingInstances() {
		return routingInstances;
	}


	/**
	 * @param routingInstances the routingInstances to set
	 */
	void setRoutingInstances(Set<RoutingInstanceConfiguration> routingInstances) {
		this.routingInstances.clear();
		
		if(routingInstances != null)
			this.routingInstances.addAll(routingInstances);
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(RoutingProcessorConfiguration o) {
		CompareToBuilder builder = (new CompareToBuilder())
				.append(getRoutingInstances().size(), o.getRoutingInstances().size());
		
		if(builder.toComparison() == 0) {
			Iterator<RoutingInstanceConfiguration> lit = getRoutingInstances().iterator();
			Iterator<RoutingInstanceConfiguration> rit = o.getRoutingInstances().iterator();
			
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
		
		for(RoutingInstanceConfiguration instance : getRoutingInstances())
			builder.append(instance);
		
		return builder.toHashCode();
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof RoutingProcessorConfiguration))
			return false;
		
		RoutingProcessorConfiguration o = (RoutingProcessorConfiguration)obj;

		EqualsBuilder builder = (new EqualsBuilder())
				.append(getRoutingInstances().size(), o.getRoutingInstances().size());
		
		if(builder.isEquals()) {
			Iterator<RoutingInstanceConfiguration> lit = getRoutingInstances().iterator();
			Iterator<RoutingInstanceConfiguration> rit = o.getRoutingInstances().iterator();
			
			while(lit.hasNext())
				builder.append(lit.next(), rit.next());
		}

		return builder.isEquals();
	}
}
