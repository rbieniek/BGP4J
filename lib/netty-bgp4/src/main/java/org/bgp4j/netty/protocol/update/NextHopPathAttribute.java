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
 */
package org.bgp4j.netty.protocol.update;

import java.net.Inet4Address;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class NextHopPathAttribute extends PathAttribute {

	public NextHopPathAttribute() {
		super(Category.WELL_KNOWN_MANDATORY);
	}

	public NextHopPathAttribute(Inet4Address nextHop) {
		super(Category.WELL_KNOWN_MANDATORY);
		
		setNextHop(nextHop);
	}

	private Inet4Address nextHop;
	
	/**
	 * @return the nextHop
	 */
	public Inet4Address getNextHop() {
		return nextHop;
	}

	/**
	 * set the next hop. If the next hop is semantically invalid, an exception is raised.
	 * 
	 * @param nextHop the nextHop to set, MUST NOT be an IP multicast address
	 * @throws InvalidNextHopException next hop address is a multicast address.
	 */
	public void setNextHop(Inet4Address nextHop) {
		if(nextHop.isMulticastAddress())
			throw new InvalidNextHopException();
		
		this.nextHop = nextHop;
	}

}
