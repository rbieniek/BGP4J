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
		createCatcher = obtainInstance(CreatedEventCatcher.class);
		createCatcher.reset();
		destroyCatcher = obtainInstance(DestroyedEventCatcher.class);
		destroyCatcher.reset();
		manager = obtainInstance(PeerRoutingInformationBaseManager.class);
		manager.reset();
	}

	@After
	public void after() {
		manager = null;
		createCatcher = null;
		destroyCatcher = null;
	}
	
	private PeerRoutingInformationBaseManager manager;
	private CreatedEventCatcher createCatcher;
	private DestroyedEventCatcher destroyCatcher;
	
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
		Assert.assertEquals(2, createCatcher.pribSize());
		Assert.assertEquals(1, createCatcher.getPRIBCreatedCount("foo"));
		Assert.assertEquals(1, createCatcher.getPRIBCreatedCount("bar"));
	}

	@Test
	public void testDuplicateGet() {
		PeerRoutingInformationBase base1 = manager.peerRoutingInformationBase("foo");
		PeerRoutingInformationBase base2 = manager.peerRoutingInformationBase("foo");
		
		Assert.assertEquals(base1, base2);
		Assert.assertEquals(1, createCatcher.pribSize());
		Assert.assertEquals(1, createCatcher.getPRIBCreatedCount("foo"));
	}

	@Test 
	public void testDestroyExisting() {
		manager.peerRoutingInformationBase("foo");
	
		Assert.assertEquals(1, createCatcher.pribSize());
		Assert.assertEquals(1, createCatcher.getPRIBCreatedCount("foo"));
		Assert.assertEquals(0, destroyCatcher.pribSize());
		Assert.assertEquals(0, destroyCatcher.getPRIBCreatedCount("foo"));

		manager.destroyPeerRoutingInformationBase("foo");

		Assert.assertEquals(1, createCatcher.pribSize());
		Assert.assertEquals(1, createCatcher.getPRIBCreatedCount("foo"));
		Assert.assertEquals(1, destroyCatcher.pribSize());
		Assert.assertEquals(1, destroyCatcher.getPRIBCreatedCount("foo"));
	}

	@Test 
	public void testDestroyNonExisting() {
		Assert.assertEquals(0, createCatcher.pribSize());
		Assert.assertEquals(0, createCatcher.getPRIBCreatedCount("foo"));
		Assert.assertEquals(0, destroyCatcher.pribSize());
		Assert.assertEquals(0, destroyCatcher.getPRIBCreatedCount("foo"));

		manager.destroyPeerRoutingInformationBase("foo");

		Assert.assertEquals(0, createCatcher.pribSize());
		Assert.assertEquals(0, createCatcher.getPRIBCreatedCount("foo"));
		Assert.assertEquals(0, destroyCatcher.pribSize());
		Assert.assertEquals(0, destroyCatcher.getPRIBCreatedCount("foo"));
	}
}
