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
 * File: org.bgp4j.rib.PeerRoutingInformationBaseTest.java 
 */
package org.bgp4j.rib;

import junit.framework.Assert;

import org.bgp4j.net.AddressFamily;
import org.bgp4j.net.AddressFamilyKey;
import org.bgp4j.net.SubsequentAddressFamily;
import org.bgp4j.weld.WeldTestCaseBase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class PeerRoutingInformationBaseTest extends WeldTestCaseBase {

	private PeerRoutingInformationBase prib;
	
	@Before
	public void before() {
		prib = obtainInstance(PeerRoutingInformationBase.class);
	}
	
	@After
	public void after() {
		prib = null;
	}
	
	@Test
	public void testCreateRIB() {
		Assert.assertNull(prib.routingBase(RIBSide.Local, new AddressFamilyKey(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING)));
		
		prib.allocateRoutingInformationBase(RIBSide.Local, new AddressFamilyKey(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING));
		Assert.assertNotNull(prib.routingBase(RIBSide.Local, new AddressFamilyKey(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING)));

	}
}
