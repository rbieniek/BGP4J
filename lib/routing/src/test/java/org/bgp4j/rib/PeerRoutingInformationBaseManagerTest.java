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
 * File: org.bgp4j.rib.PeerRoutingInformationBaseManagerTest.java 
 */
package org.bgp4j.rib;



import junit.framework.Assert;

import org.bgp4j.weld.WeldTestCaseBase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class PeerRoutingInformationBaseManagerTest extends WeldTestCaseBase{

	@Before
	public void before() {
		catcher = obtainInstance(CreatedEventCatcher.class);
		catcher.reset();
		manager = obtainInstance(PeerRoutingInformationBaseManager.class);
		manager.reset();
	}

	@After
	public void after() {
		manager = null;
		catcher = null;
	}
	
	private PeerRoutingInformationBaseManager manager;
	private CreatedEventCatcher catcher;
	
	@Test(expected=IllegalArgumentException.class)
	public void testNullName() {
		manager.peerRoutingInformationBase(null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testEmptyName() {
		manager.peerRoutingInformationBase("");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testBlankName() {
		manager.peerRoutingInformationBase("  ");
	}

	
	@Test
	public void testDifferentGet() {
		PeerRoutingInformationBase base1 = manager.peerRoutingInformationBase("foo");
		PeerRoutingInformationBase base2 = manager.peerRoutingInformationBase("bar");
		
		Assert.assertFalse(base1.equals(base2));
		Assert.assertEquals(2, catcher.pribSize());
		Assert.assertEquals(1, catcher.getPRIBCreatedCount("foo"));
		Assert.assertEquals(1, catcher.getPRIBCreatedCount("bar"));
	}

	@Test
	public void testDuplicateGet() {
		PeerRoutingInformationBase base1 = manager.peerRoutingInformationBase("foo");
		PeerRoutingInformationBase base2 = manager.peerRoutingInformationBase("foo");
		
		Assert.assertEquals(base1, base2);
		Assert.assertEquals(1, catcher.pribSize());
		Assert.assertEquals(1, catcher.getPRIBCreatedCount("foo"));
	}
}
