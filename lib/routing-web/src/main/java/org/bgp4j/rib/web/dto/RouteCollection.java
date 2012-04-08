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
	
	private Set<RouteDTO> entries = new TreeSet<RouteDTO>();
	
	@XmlElement
	public Set<RouteDTO> getEntries() {
		return entries;
	}

	/**
	 * @param entries the entries to set
	 */
	public void setEntries(Set<RouteDTO> entries) {		
		if(entries != null)
			this.entries = new TreeSet<RouteDTO>(entries);
		else
			this.entries = new TreeSet<RouteDTO>();
	}

}
