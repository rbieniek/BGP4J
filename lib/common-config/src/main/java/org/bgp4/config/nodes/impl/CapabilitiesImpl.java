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

	private TreeSet<Capability> requiredCapabilities = new TreeSet<Capability>();
	private TreeSet<Capability> optionalCapabilities = new TreeSet<Capability>();
	
	public CapabilitiesImpl() {
	}
	
	public CapabilitiesImpl(Capability[] requiredCaps) {
		if(requiredCaps != null) {
			for(Capability cap : requiredCaps)
				requiredCapabilities.add(cap);
		}
	}

	public CapabilitiesImpl(Capability[] requiredCaps, Capability[] optionalCaps) {
		this(requiredCaps);

		if(optionalCaps != null) {
			for(Capability cap : optionalCaps)
				optionalCapabilities.add(cap);
		}
	}

	@Override
	public Set<Capability> getRequiredCapabilities() {
		return Collections.unmodifiableSet(requiredCapabilities);
	}

	void addRequiredCapability(Capability cap) {
		this.requiredCapabilities.add(cap);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		HashCodeBuilder hcb = new HashCodeBuilder();
		
		for(Capability cap : requiredCapabilities)
			hcb.append(cap).append(false);

		for(Capability cap : optionalCapabilities)
			hcb.append(cap).append(true);

		return hcb.toHashCode();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Capabilities))
			return false;

		Set<Capability> otherCaps = ((Capabilities)obj).getRequiredCapabilities();
		
		if(otherCaps.size() != requiredCapabilities.size())
			return false;
		
		for(Capability cap : requiredCapabilities)
			if(!otherCaps.contains(cap))
				return false;
		
		otherCaps = ((Capabilities)obj).getOptionalCapabilities();
		
		if(otherCaps.size() != optionalCapabilities.size())
			return false;
		
		for(Capability cap : optionalCapabilities)
			if(!otherCaps.contains(cap))
				return false;
		return true;
	}

	@Override
	public Set<Capability> getOptionalCapabilities() {
		return Collections.unmodifiableSet(optionalCapabilities);
	}
	
	void addOptionalCapability(Capability cap) {
		this.optionalCapabilities.add(cap);
	}

}
