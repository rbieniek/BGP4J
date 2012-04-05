/**
 *  Copyright 2012 Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * 
 * File: org.bgp4j.rib.web.dto.RIBCollection.java 
 */
package org.bgp4j.rib.web.dto;

import java.util.Set;
import java.util.TreeSet;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
@XmlRootElement
public class RIBCollection {
	
	private Set<RIBEntry> entries = new TreeSet<RIBEntry>();
	
	@XmlElement
	public Set<RIBEntry> getEntries() {
		return entries;
	}

	/**
	 * @param entries the entries to set
	 */
	public void setEntries(Set<RIBEntry> entries) {		
		if(entries != null)
			this.entries = new TreeSet<RIBEntry>(entries);
		else
			this.entries = new TreeSet<RIBEntry>();
	}	
}
