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
 * File: org.bgp4j.rib.RoutingInformationBase.java 
 */
package org.bgp4j.rib;

import org.bgp4j.net.AddressFamilyKey;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class RoutingInformationBase {

	private String peerName;
	private RIBSide side;
	private AddressFamilyKey addressFamilyKey;
	
	RoutingInformationBase() {
	}

	/**
	 * @return the peerName
	 */
	public String getPeerName() {
		return peerName;
	}

	/**
	 * @return the side
	 */
	public RIBSide getSide() {
		return side;
	}

	/**
	 * @return the addressFamilyKey
	 */
	public AddressFamilyKey getAddressFamilyKey() {
		return addressFamilyKey;
	}

	/**
	 * @param peerName the peerName to set
	 */
	void setPeerName(String peerName) {
		this.peerName = peerName;
	}

	/**
	 * @param side the side to set
	 */
	void setSide(RIBSide side) {
		this.side = side;
	}

	/**
	 * @param addressFamilyKey the addressFamilyKey to set
	 */
	void setAddressFamilyKey(AddressFamilyKey addressFamilyKey) {
		this.addressFamilyKey = addressFamilyKey;
	}

	void destroyRIB() {
		// TODO Auto-generated method stub
		
	}

}
