/**
 * 
 */
package org.bgp4j.rib.web.dto;

import java.util.Set;
import java.util.TreeSet;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author rainer
 *
 */
@XmlRootElement
public class RouteCollection {
	
	private Set<RouteEntry> entries = new TreeSet<RouteEntry>();
	
	@XmlElement
	public Set<RouteEntry> getEntries() {
		return entries;
	}

	/**
	 * @param entries the entries to set
	 */
	public void setEntries(Set<RouteEntry> entries) {		
		if(entries != null)
			this.entries = new TreeSet<RouteEntry>(entries);
		else
			this.entries = new TreeSet<RouteEntry>();
	}

}
