/**
 * 
 */
package org.bgp4j.rib.web.dto;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.bgp4j.net.NetworkLayerReachabilityInformation;
import org.bgp4j.net.NextHop;
import org.bgp4j.net.attributes.PathAttribute;

/**
 * @author rainer
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
public class RouteEntry implements Comparable<RouteEntry> {

	private NetworkLayerReachabilityInformation nlri;
	private NextHop nextHop;
	private Set<PathAttribute> pathAttributes = new TreeSet<PathAttribute>();

	public RouteEntry() {}
	
	public RouteEntry(NetworkLayerReachabilityInformation nlri, NextHop nextHop, Collection<PathAttribute> pathAttributes) {
		setNlri(nlri);
		setNextHop(nextHop);
		getPathAttributes().addAll(pathAttributes);
	}
	
	/**
	 * @return the nlri
	 */
	public NetworkLayerReachabilityInformation getNlri() {
		return nlri;
	}

	/**
	 * @param nlri the nlri to set
	 */
	public void setNlri(NetworkLayerReachabilityInformation nlri) {
		this.nlri = nlri;
	}

	@Override
	public int compareTo(RouteEntry o) {
		CompareToBuilder builder = (new CompareToBuilder())
				.append(getNlri(), o.getNlri())
				.append(getNextHop(), o.getNextHop())
				.append(getPathAttributes().size(), o.getPathAttributes().size());
		
		if(builder.toComparison() == 0) {
			Iterator<PathAttribute> lit = getPathAttributes().iterator();
			Iterator<PathAttribute> rit = o.getPathAttributes().iterator();
			
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
				.append(getNlri())
				.append(getNextHop())
				.append(getPathAttributes().size());
		
		for(PathAttribute attr : getPathAttributes())
			builder.append(attr);
		
		return builder.hashCode();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof RouteEntry))
			return false;
		
		RouteEntry o = (RouteEntry)obj;
		
		EqualsBuilder builder = (new EqualsBuilder())
				.append(getNlri(), o.getNlri())
				.append(getNextHop(), o.getNextHop())
				.append(getPathAttributes().size(), o.getPathAttributes().size());
		
		if(builder.isEquals()) {
			Iterator<PathAttribute> lit = getPathAttributes().iterator();
			Iterator<PathAttribute> rit = o.getPathAttributes().iterator();
			
			while(lit.hasNext())
				builder.append(lit.next(), rit.next());
		}

		return builder.build();
	}

	/**
	 * @return the nextHop
	 */
	public NextHop getNextHop() {
		return nextHop;
	}

	/**
	 * @param nextHop the nextHop to set
	 */
	public void setNextHop(NextHop nextHop) {
		this.nextHop = nextHop;
	}

	/**
	 * @return the pathAttributes
	 */
	public Set<PathAttribute> getPathAttributes() {
		return pathAttributes;
	}

	/**
	 * @param pathAttributes the pathAttributes to set
	 */
	public void setPathAttributes(Set<PathAttribute> pathAttributes) {
		if(pathAttributes != null)
			this.pathAttributes = new TreeSet<PathAttribute>(pathAttributes);
		else
			this.pathAttributes = new TreeSet<PathAttribute>();
	}

}
