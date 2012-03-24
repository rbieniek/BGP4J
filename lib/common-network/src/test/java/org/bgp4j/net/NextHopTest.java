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
 * File: org.bgp4j.net.NextHopTest.java 
 */
package org.bgp4j.net;

import java.net.InetAddress;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class NextHopTest {

	@Test
	public void testSmaller() throws Exception {
		InetAddressNextHop<InetAddress> a = new InetAddressNextHop<InetAddress>(InetAddress.getByAddress(new byte[] { 1, 2, 3, 4}));
		BinaryNextHop b = new BinaryNextHop(new byte[] { 1, 2, 3, 4});
		
		Assert.assertTrue(a.compareTo(b) < 0);
	}

	@Test
	public void testLarger() throws Exception {
		BinaryNextHop a = new BinaryNextHop(new byte[] { 1, 2, 3, 4});
		InetAddressNextHop<InetAddress> b = new InetAddressNextHop<InetAddress>(InetAddress.getByAddress(new byte[] { 1, 2, 3, 4}));
		
		Assert.assertTrue(a.compareTo(b) > 0);
	}
}
