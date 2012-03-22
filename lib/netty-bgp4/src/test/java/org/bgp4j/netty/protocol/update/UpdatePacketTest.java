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
 * File: org.bgp4j.netty.protocol.update.UpdatePacketTest.java 
 */
package org.bgp4j.netty.protocol.update;

import java.util.Set;

import junit.framework.Assert;

import org.bgp4j.net.AddressFamily;
import org.bgp4j.net.SubsequentAddressFamily;
import org.bgp4j.net.attributes.MultiProtocolReachableNLRI;
import org.bgp4j.net.attributes.MultiProtocolUnreachableNLRI;
import org.junit.Test;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class UpdatePacketTest {

	@Test
	public void testLookupPathAttributes() {
		UpdatePacket packet = new UpdatePacket();
		MultiProtocolReachableNLRI reachableIPv4Unicast = new MultiProtocolReachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING);
		MultiProtocolReachableNLRI reachableIPv6Unicast = new MultiProtocolReachableNLRI(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING);
		MultiProtocolUnreachableNLRI unreachableIPv4Unicast = new MultiProtocolUnreachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING);
		MultiProtocolUnreachableNLRI unreachableIPv6Unicast = new MultiProtocolUnreachableNLRI(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING);
		
		packet.getPathAttributes().add(reachableIPv6Unicast);
		packet.getPathAttributes().add(reachableIPv4Unicast);
		packet.getPathAttributes().add(unreachableIPv6Unicast);
		packet.getPathAttributes().add(unreachableIPv4Unicast);
		
		Set<MultiProtocolReachableNLRI> reachables = packet.lookupPathAttributes(MultiProtocolReachableNLRI.class);
		Set<MultiProtocolUnreachableNLRI> unreachables = packet.lookupPathAttributes(MultiProtocolUnreachableNLRI.class);
		
		Assert.assertNotNull(reachables);
		Assert.assertNotNull(unreachables);
		
		Assert.assertEquals(2, reachables.size());
		Assert.assertEquals(2, unreachables.size());
		
		Assert.assertTrue(reachables.contains(reachableIPv4Unicast));
		Assert.assertTrue(reachables.contains(reachableIPv6Unicast));
		Assert.assertTrue(unreachables.contains(unreachableIPv4Unicast));
		Assert.assertTrue(unreachables.contains(unreachableIPv6Unicast));
	}
	
	@Test
	public void testLookupPathAttributesEmptyResult() {
		UpdatePacket packet = new UpdatePacket();
		MultiProtocolReachableNLRI reachableIPv4Unicast = new MultiProtocolReachableNLRI(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING);
		MultiProtocolReachableNLRI reachableIPv6Unicast = new MultiProtocolReachableNLRI(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING);
		
		packet.getPathAttributes().add(reachableIPv6Unicast);
		packet.getPathAttributes().add(reachableIPv4Unicast);
		
		Set<MultiProtocolUnreachableNLRI> unreachables = packet.lookupPathAttributes(MultiProtocolUnreachableNLRI.class);
		
		Assert.assertNotNull(unreachables);
		
		Assert.assertEquals(0, unreachables.size());
	}
	
}
