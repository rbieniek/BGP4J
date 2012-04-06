/**
 * 
 */
package org.bgp4j.config.nodes.impl;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.bgp4j.config.nodes.RoutingFilterConfiguration;

/**
 * @author rainer
 *
 */
public abstract class RoutingFilterConfigurationImpl implements RoutingFilterConfiguration {

	private String name;

	protected RoutingFilterConfigurationImpl() {}
	
	protected RoutingFilterConfigurationImpl(String name) {
		setName(name);
	}

	@Override
	public String getName() {
		return name;
	}

	void setName(String name)  {
		this.name = name;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(RoutingFilterConfiguration o) {
		CompareToBuilder builder = (new CompareToBuilder())
				.append(getName(), o.getName())
				.append(getType(), ((RoutingFilterConfigurationImpl)o).getType());
		
		if(builder.toComparison() == 0)
			subclassCompareTo(builder, o);
		
		return builder.toComparison();
	}

	/* (non-Javadoc)
	 * @see org.bgp4j.config.nodes.RoutingFilterConfiguration#getName()
	 */


	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		HashCodeBuilder builder = (new HashCodeBuilder())
				.append(getName())
				.append(getType());
		
		subclassHashCode(builder);
		
		return builder.toHashCode();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof RoutingFilterConfiguration))
			return false;
		
		RoutingFilterConfiguration o = (RoutingFilterConfiguration)obj;
		
		EqualsBuilder builder = (new EqualsBuilder())
				.append(getName(), o.getName())
				.append(getType(), ((RoutingFilterConfigurationImpl)o).getType());
		
		if(builder.isEquals())
			subclassEquals(builder, o);
		
		return builder.isEquals();
	}
	
	protected abstract RoutingFilterType getType();

	protected abstract void subclassCompareTo(CompareToBuilder builder, RoutingFilterConfiguration o); 

	protected abstract void subclassEquals(EqualsBuilder builder, RoutingFilterConfiguration o); 
	
	protected abstract void subclassHashCode(HashCodeBuilder builder);
}
