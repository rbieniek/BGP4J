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
 * File: org.bgp4j.netty.protocol.MaximumNumberOfPrefixesReachedNotificationPacket.java 
 */
package org.bgp4j.net.packets;

import org.bgp4j.net.AddressFamily;
import org.bgp4j.net.EChannelDirection;
import org.bgp4j.net.SubsequentAddressFamily;
import org.bgp4j.net.events.MaximumNumberOfPrefixesReachedNotificationEvent;
import org.bgp4j.net.events.NotificationEvent;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class MaximumNumberOfPrefixesReachedNotificationPacket extends CeaseNotificationPacket {

	private AddressFamily addressFamily;
	private SubsequentAddressFamily subsequentAddressFamily;
	private int prefixUpperBound;
	
	/**
	 * @param subcode
	 */
	public MaximumNumberOfPrefixesReachedNotificationPacket() {
		super(CeaseNotificationPacket.SUBCODE_MAXIMUM_NUMBER_OF_PREFIXES_REACHED);
	}

	/**
	 * @param subcode
	 */
	public MaximumNumberOfPrefixesReachedNotificationPacket(AddressFamily addressFamily, SubsequentAddressFamily subsequentAddressFamily, int prefixUpperBound) {
		super(CeaseNotificationPacket.SUBCODE_MAXIMUM_NUMBER_OF_PREFIXES_REACHED);
		
		this.addressFamily = addressFamily;
		this.subsequentAddressFamily = subsequentAddressFamily;
		this.prefixUpperBound = prefixUpperBound;
	}

	/**
	 * @return the addressFamily
	 */
	public AddressFamily getAddressFamily() {
		return addressFamily;
	}

	/**
	 * @param addressFamily the addressFamily to set
	 */
	public void setAddressFamily(AddressFamily addressFamily) {
		this.addressFamily = addressFamily;
	}

	/**
	 * @return the subsequentAddressFamily
	 */
	public SubsequentAddressFamily getSubsequentAddressFamily() {
		return subsequentAddressFamily;
	}

	/**
	 * @param subsequentAddressFamily the subsequentAddressFamily to set
	 */
	public void setSubsequentAddressFamily(
			SubsequentAddressFamily subsequentAddressFamily) {
		this.subsequentAddressFamily = subsequentAddressFamily;
	}

	/**
	 * @return the prefixUpperBound
	 */
	public int getPrefixUpperBound() {
		return prefixUpperBound;
	}

	/**
	 * @param prefixUpperBound the prefixUpperBound to set
	 */
	public void setPrefixUpperBound(int prefixUpperBound) {
		this.prefixUpperBound = prefixUpperBound;
	}

	@Override
	public NotificationEvent event(EChannelDirection direction) {
		return new MaximumNumberOfPrefixesReachedNotificationEvent(direction);
	}

}
