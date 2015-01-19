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
 * File: org.bgp4j.net.capabilities.MultiProtocolCapabilityTest.java 
 */
package org.bgp4j.net.capabilities;

import java.util.Arrays;
import java.util.List;

import org.bgp4j.net.AddressFamily;
import org.bgp4j.net.AddressFamilyKey;
import org.bgp4j.net.SubsequentAddressFamily;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class MultiProtocolCapabilityTest {

	@Test
	public void testToAddressFamilyKey() {
		List<AddressFamily> afList = Arrays.asList(AddressFamily.APPLETALK, 
				AddressFamily.BANYAN, 
				AddressFamily.BBN1882,
				AddressFamily.DECNET4,
				AddressFamily.E163,
				AddressFamily.E164,
				AddressFamily.F69,
				AddressFamily.HDLC,
				AddressFamily.IEEE802,
				AddressFamily.IPv4,
				AddressFamily.IPv6,
				AddressFamily.IPX,
				AddressFamily.NSAP,
				AddressFamily.X121);
		List<SubsequentAddressFamily> safList = Arrays.asList(SubsequentAddressFamily.NLRI_MULTICAST_FORWARDING,
				SubsequentAddressFamily.NLRI_UNICAST_FORWARDING,
				SubsequentAddressFamily.NLRI_UNICAST_MULTICAST_FORWARDING);
		
		for(AddressFamily afi : afList) {
			for(SubsequentAddressFamily safi : safList) {
				MultiProtocolCapability mpcap = new MultiProtocolCapability(afi, safi);
				
				Assert.assertEquals(new AddressFamilyKey(afi, safi), mpcap.toAddressFamilyKey());
			}
		}
	}
}
