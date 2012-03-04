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
 * File: org.bgp4.config.nodes.impl.CapabilitiesImpl.java 
 */
package org.bgp4.config.nodes.impl;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.bgp4.config.nodes.Capabilities;
import org.bgp4j.net.Capability;

/**
 * @author Rainer Bieniek (rainer@bgp4j.org)
 *
 */
public class CapabilitiesImpl implements Capabilities {

	private TreeSet<Capability> capabilities = new TreeSet<Capability>();
	
	CapabilitiesImpl() {
	}
	
	CapabilitiesImpl(Capability[] caps) {
		for(Capability cap : caps)
			capabilities.add(cap);
	}
	
	@Override
	public Set<Capability> getCapabilities() {
		return Collections.unmodifiableSet(capabilities);
	}

	void addCapability(Capability cap) {
		this.capabilities.add(cap);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		HashCodeBuilder hcb = new HashCodeBuilder();
		
		for(Capability cap : capabilities)
			hcb.append(cap);
		
		return hcb.toHashCode();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Capabilities))
			return false;

		Set<Capability> otherCaps = ((Capabilities)obj).getCapabilities();
		
		if(otherCaps.size() != capabilities.size())
			return false;
		
		for(Capability cap : capabilities)
			if(!otherCaps.contains(cap))
				return false;
		
		return true;
	}
}
