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
 * File: org.bgp4j.rib.RoutingTreeTest.java 
 */
package org.bgp4j.rib;

import java.net.InetAddress;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import junit.framework.Assert;

import org.bgp4j.net.AddressFamilyKey;
import org.bgp4j.net.InetAddressNextHop;
import org.bgp4j.net.NetworkLayerReachabilityInformation;
import org.bgp4j.net.attributes.LocalPrefPathAttribute;
import org.bgp4j.net.attributes.PathAttribute;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class RoutingTreeTest {

	@Before
	public void before() {
		tree = new RoutingTree();
		attrs1 = new HashSet<PathAttribute>();
		attrs1.add(new LocalPrefPathAttribute(100));
		
		attrs2 = new HashSet<PathAttribute>();
		attrs2.add(new LocalPrefPathAttribute(200));
	}
	
	@After
	public void after() {
		tree = null;
		attrs1 = null;
		attrs2 = null;
	}
	
	private RoutingTree tree;
	private Collection<PathAttribute> attrs1;
	private Collection<PathAttribute> attrs2;
	
	@Test
	public void testAddSingleNode() {
		NetworkLayerReachabilityInformation nlri = new NetworkLayerReachabilityInformation(0, null); // default route prefix
		
		Assert.assertTrue(tree.addRoute(new Route(null, nlri, attrs1, null)));
		
		RoutingTree.RoutingTreeNode node = tree.getRootNode().getChildNodes().first();
		
		Assert.assertNotNull(node);
		Assert.assertEquals(nlri, node.getRoute().getNlri());
		Assert.assertTrue(equalCollections(attrs1, node.getRoute().getPathAttributes()));
	}
	
	@Test
	public void testAddTwoSiblingNodesFirstLowerSecondHigher() {
		NetworkLayerReachabilityInformation lowerNlri = new NetworkLayerReachabilityInformation(24, new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x04 }); // prefix 192.168.4/24
		NetworkLayerReachabilityInformation higherNlri = new NetworkLayerReachabilityInformation(24, new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x08 }); // prefix 192.168.8/24
		RoutingTree.RoutingTreeNode node;
		
		Assert.assertTrue(tree.addRoute(new Route(null, lowerNlri, attrs1, null)));
		Assert.assertTrue(tree.addRoute(new Route(null, higherNlri, attrs2, null)));
		
		node = tree.getRootNode().getChildNodes().first();
		
		Assert.assertNotNull(node);
		Assert.assertEquals(lowerNlri, node.getRoute().getNlri());
		Assert.assertTrue(equalCollections(attrs1, node.getRoute().getPathAttributes()));		
		
		node = tree.getRootNode().getChildNodes().last();
		
		Assert.assertNotNull(node);
		Assert.assertEquals(higherNlri, node.getRoute().getNlri());
		Assert.assertTrue(equalCollections(attrs2, node.getRoute().getPathAttributes()));		

	}
	
	
	@Test
	public void testAddTwoSiblingNodesFirstHigherSecondLower() {
		NetworkLayerReachabilityInformation lowerNlri = new NetworkLayerReachabilityInformation(24, new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x04 }); // prefix 192.168.4/24
		NetworkLayerReachabilityInformation higherNlri = new NetworkLayerReachabilityInformation(24, new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x08 }); // prefix 192.168.8/24
		RoutingTree.RoutingTreeNode node;
		
		Assert.assertTrue(tree.addRoute(new Route(null, higherNlri, attrs2, null)));
		Assert.assertTrue(tree.addRoute(new Route(null, lowerNlri, attrs1, null)));
		
		node = tree.getRootNode().getChildNodes().first();
		
		Assert.assertNotNull(node);
		Assert.assertEquals(lowerNlri, node.getRoute().getNlri());
		Assert.assertTrue(equalCollections(attrs1, node.getRoute().getPathAttributes()));		
		
		node = tree.getRootNode().getChildNodes().last();
		
		Assert.assertNotNull(node);
		Assert.assertEquals(higherNlri, node.getRoute().getNlri());
		Assert.assertTrue(equalCollections(attrs2, node.getRoute().getPathAttributes()));		

	}

	@Test
	public void testAddSingleNodeReplacePathAttributes() {
		NetworkLayerReachabilityInformation nlri = new NetworkLayerReachabilityInformation(0, null); // default route prefix
		RoutingTree.RoutingTreeNode node;
		
		Assert.assertTrue(tree.addRoute(new Route(null, nlri, attrs1, null)));
		
		node = tree.getRootNode().getChildNodes().first();
		
		Assert.assertNotNull(node);
		Assert.assertEquals(nlri, node.getRoute().getNlri());
		Assert.assertTrue(equalCollections(attrs1, node.getRoute().getPathAttributes()));
		
		Assert.assertTrue(tree.addRoute(new Route(null, nlri, attrs2, null)));
		
		node = tree.getRootNode().getChildNodes().first();
		
		Assert.assertNotNull(node);
		Assert.assertEquals(nlri, node.getRoute().getNlri());
		Assert.assertTrue(equalCollections(attrs2, node.getRoute().getPathAttributes()));
	}

	@Test
	public void testAddTwoNodesFirstLessSpecificSecondMoreSpecific() {
		NetworkLayerReachabilityInformation lessNlri = new NetworkLayerReachabilityInformation(24, new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x04 }); // prefix 192.168.4/24
		NetworkLayerReachabilityInformation moreNlri = new NetworkLayerReachabilityInformation(28, 
				new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x04, (byte)0x40 }); // prefix 192.168.4.16/28
		RoutingTree.RoutingTreeNode node;

		Assert.assertTrue(tree.addRoute(new Route(null, lessNlri, attrs1, null)));
		Assert.assertTrue(tree.addRoute(new Route(null, moreNlri, attrs2, null)));

		node = tree.getRootNode().getChildNodes().first();
		
		Assert.assertNotNull(node);
		Assert.assertEquals(lessNlri, node.getRoute().getNlri());
		Assert.assertTrue(equalCollections(attrs1, node.getRoute().getPathAttributes()));
		
		node = node.getChildNodes().first();

		Assert.assertNotNull(node);
		Assert.assertEquals(moreNlri, node.getRoute().getNlri());
		Assert.assertTrue(equalCollections(attrs2, node.getRoute().getPathAttributes()));		
	}
	
	@Test
	public void testAddTwoNodesFirstMoreSpecificSecondLessSpecific() {
		NetworkLayerReachabilityInformation lessNlri = new NetworkLayerReachabilityInformation(24, new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x04 }); // prefix 192.168.4/24
		NetworkLayerReachabilityInformation moreNlri = new NetworkLayerReachabilityInformation(28, 
				new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x04, (byte)0x40 }); // prefix 192.168.4.16/28
		RoutingTree.RoutingTreeNode node;

		Assert.assertTrue(tree.addRoute(new Route(null, moreNlri, attrs2, null)));
		Assert.assertTrue(tree.addRoute(new Route(null, lessNlri, attrs1, null)));

		node = tree.getRootNode().getChildNodes().first();
		
		Assert.assertNotNull(node);
		Assert.assertEquals(lessNlri, node.getRoute().getNlri());
		Assert.assertTrue(equalCollections(attrs1, node.getRoute().getPathAttributes()));
		
		node = node.getChildNodes().first();

		Assert.assertNotNull(node);
		Assert.assertEquals(moreNlri, node.getRoute().getNlri());
		Assert.assertTrue(equalCollections(attrs2, node.getRoute().getPathAttributes()));		
	}
	
	@Test
	public void testAddTwoSiblingsAddLessSpecificParent() {
		NetworkLayerReachabilityInformation lessNlri = new NetworkLayerReachabilityInformation(24, new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x04 }); // prefix 192.168.4/24
		NetworkLayerReachabilityInformation moreNlri1 = new NetworkLayerReachabilityInformation(28, 
				new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x04, (byte)0x10 }); // prefix 192.168.4.16/28
		NetworkLayerReachabilityInformation moreNlri2 = new NetworkLayerReachabilityInformation(28, 
				new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x04, (byte)0x20 }); // prefix 192.168.4.32/28
		RoutingTree.RoutingTreeNode node;
		RoutingTree.RoutingTreeNode parent;

		Assert.assertTrue(tree.addRoute(new Route(null, moreNlri1, attrs2, null)));
		Assert.assertTrue(tree.addRoute(new Route(null, moreNlri2, attrs2, null)));
		Assert.assertTrue(tree.addRoute(new Route(null, lessNlri, attrs1, null)));

		parent = tree.getRootNode().getChildNodes().first();
		
		Assert.assertNotNull(parent);
		Assert.assertEquals(lessNlri, parent.getRoute().getNlri());
		Assert.assertTrue(equalCollections(attrs1, parent.getRoute().getPathAttributes()));
		
		node = parent.getChildNodes().first();

		Assert.assertNotNull(node);
		Assert.assertEquals(moreNlri1, node.getRoute().getNlri());
		Assert.assertTrue(equalCollections(attrs2, node.getRoute().getPathAttributes()));		

		node = parent.getChildNodes().last();

		Assert.assertNotNull(node);
		Assert.assertEquals(moreNlri2, node.getRoute().getNlri());
		Assert.assertTrue(equalCollections(attrs2, node.getRoute().getPathAttributes()));		
	}
	
	@Test
	public void testAddTwoNodesFirstLessSpecificSecondMoreSpecificAddMoreSpecificChild() {
		NetworkLayerReachabilityInformation lessNlri = new NetworkLayerReachabilityInformation(24, new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x04 }); // prefix 192.168.4/24
		NetworkLayerReachabilityInformation moreNlri1 = new NetworkLayerReachabilityInformation(28, 
				new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x04, (byte)0x10 }); // prefix 192.168.4.16/28
		NetworkLayerReachabilityInformation moreNlri2 = new NetworkLayerReachabilityInformation(28, 
				new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x04, (byte)0x20 }); // prefix 192.168.4.32/28
		RoutingTree.RoutingTreeNode node;
		RoutingTree.RoutingTreeNode parent;

		Assert.assertTrue(tree.addRoute(new Route(null, moreNlri1, attrs2, null)));
		Assert.assertTrue(tree.addRoute(new Route(null, lessNlri, attrs1, null)));
		Assert.assertTrue(tree.addRoute(new Route(null, moreNlri2, attrs2, null)));

		parent = tree.getRootNode().getChildNodes().first();
		
		Assert.assertNotNull(parent);
		Assert.assertEquals(lessNlri, parent.getRoute().getNlri());
		Assert.assertTrue(equalCollections(attrs1, parent.getRoute().getPathAttributes()));
		
		node = parent.getChildNodes().first();

		Assert.assertNotNull(node);
		Assert.assertEquals(moreNlri1, node.getRoute().getNlri());
		Assert.assertTrue(equalCollections(attrs2, node.getRoute().getPathAttributes()));		

		node = parent.getChildNodes().last();

		Assert.assertNotNull(node);
		Assert.assertEquals(moreNlri2, node.getRoute().getNlri());
		Assert.assertTrue(equalCollections(attrs2, node.getRoute().getPathAttributes()));		
	}
	
	@Test
	public void testAddAndRemoveSingleNode() {
		NetworkLayerReachabilityInformation nlri = new NetworkLayerReachabilityInformation(0, null); // default route prefix
		
		Assert.assertTrue(tree.addRoute(new Route(null, nlri, attrs1, null)));
		
		RoutingTree.RoutingTreeNode node = tree.getRootNode().getChildNodes().first();
		
		Assert.assertNotNull(node);
		Assert.assertEquals(nlri, node.getRoute().getNlri());
		Assert.assertTrue(equalCollections(attrs1, node.getRoute().getPathAttributes()));
		
		Assert.assertTrue(tree.withdrawRoute(new Route(AddressFamilyKey.IPV4_UNICAST_FORWARDING, nlri, null, null)));
		Assert.assertEquals(0, tree.getRootNode().getChildNodes().size());
	}
	
	@Test
	public void testAddTwoNodesFirstLessSpecificSecondMoreSpecificRemoveNotExisting() {
		NetworkLayerReachabilityInformation lessNlri = new NetworkLayerReachabilityInformation(24, new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x04 }); // prefix 192.168.4/24
		NetworkLayerReachabilityInformation moreNlri = new NetworkLayerReachabilityInformation(28, 
				new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x04, (byte)0x40 }); // prefix 192.168.4.64/28
		NetworkLayerReachabilityInformation notExistingNlri = new NetworkLayerReachabilityInformation(28, 
				new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x04, (byte)0x80 }); // prefix 192.168.4.128/28
		RoutingTree.RoutingTreeNode node;

		// add nodes
		Assert.assertTrue(tree.addRoute(new Route(null, lessNlri, attrs1, null)));
		Assert.assertTrue(tree.addRoute(new Route(null, moreNlri, attrs2, null)));

		node = tree.getRootNode().getChildNodes().first();
		
		Assert.assertNotNull(node);
		Assert.assertEquals(lessNlri, node.getRoute().getNlri());
		Assert.assertTrue(equalCollections(attrs1, node.getRoute().getPathAttributes()));
		
		node = node.getChildNodes().first();

		Assert.assertNotNull(node);
		Assert.assertEquals(moreNlri, node.getRoute().getNlri());
		Assert.assertTrue(equalCollections(attrs2, node.getRoute().getPathAttributes()));		
		
		// remove node
		Assert.assertFalse(tree.withdrawRoute(new Route(AddressFamilyKey.IPV4_UNICAST_FORWARDING, notExistingNlri, null, null)));

		node = tree.getRootNode().getChildNodes().first();
		
		Assert.assertNotNull(node);
		Assert.assertEquals(lessNlri, node.getRoute().getNlri());
		Assert.assertTrue(equalCollections(attrs1, node.getRoute().getPathAttributes()));
		
		node = node.getChildNodes().first();

		Assert.assertNotNull(node);
		Assert.assertEquals(moreNlri, node.getRoute().getNlri());
		Assert.assertTrue(equalCollections(attrs2, node.getRoute().getPathAttributes()));		
	}
		
	@Test
	public void testAddTwoNodesFirstLessSpecificSecondMoreSpecificRemoveMoreSpecific() {
		NetworkLayerReachabilityInformation lessNlri = new NetworkLayerReachabilityInformation(24, new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x04 }); // prefix 192.168.4/24
		NetworkLayerReachabilityInformation moreNlri = new NetworkLayerReachabilityInformation(28, 
				new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x04, (byte)0x40 }); // prefix 192.168.4.64/28
		RoutingTree.RoutingTreeNode node;

		// add nodes
		Assert.assertTrue(tree.addRoute(new Route(null, lessNlri, attrs1, null)));
		Assert.assertTrue(tree.addRoute(new Route(null, moreNlri, attrs2, null)));

		node = tree.getRootNode().getChildNodes().first();
		
		Assert.assertNotNull(node);
		Assert.assertEquals(lessNlri, node.getRoute().getNlri());
		Assert.assertTrue(equalCollections(attrs1, node.getRoute().getPathAttributes()));
		
		node = node.getChildNodes().first();

		Assert.assertNotNull(node);
		Assert.assertEquals(moreNlri, node.getRoute().getNlri());
		Assert.assertTrue(equalCollections(attrs2, node.getRoute().getPathAttributes()));		
		
		// remove node
		Assert.assertTrue(tree.withdrawRoute(new Route(AddressFamilyKey.IPV4_UNICAST_FORWARDING, moreNlri, null, null)));

		node = tree.getRootNode().getChildNodes().first();
		
		Assert.assertNotNull(node);
		Assert.assertEquals(lessNlri, node.getRoute().getNlri());
		Assert.assertTrue(equalCollections(attrs1, node.getRoute().getPathAttributes()));
		
		Assert.assertEquals(0, node.getChildNodes().size());
	}

	@Test
	public void testAddTwoNodesFirstLessSpecificSecondMoreSpecificAddMoreSpecificChildRemoveLessSpecific() {
		NetworkLayerReachabilityInformation lessNlri = new NetworkLayerReachabilityInformation(24, new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x04 }); // prefix 192.168.4/24
		NetworkLayerReachabilityInformation moreNlri1 = new NetworkLayerReachabilityInformation(28, 
				new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x04, (byte)0x10 }); // prefix 192.168.4.16/28
		NetworkLayerReachabilityInformation moreNlri2 = new NetworkLayerReachabilityInformation(28, 
				new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x04, (byte)0x20 }); // prefix 192.168.4.32/28
		RoutingTree.RoutingTreeNode node;
		RoutingTree.RoutingTreeNode parent;

		Assert.assertTrue(tree.addRoute(new Route(null, moreNlri1, attrs2, null)));
		Assert.assertTrue(tree.addRoute(new Route(null, lessNlri, attrs1, null)));
		Assert.assertTrue(tree.addRoute(new Route(null, moreNlri2, attrs2, null)));

		parent = tree.getRootNode().getChildNodes().first();
		
		Assert.assertNotNull(parent);
		Assert.assertEquals(lessNlri, parent.getRoute().getNlri());
		Assert.assertTrue(equalCollections(attrs1, parent.getRoute().getPathAttributes()));
		
		node = parent.getChildNodes().first();

		Assert.assertNotNull(node);
		Assert.assertEquals(moreNlri1, node.getRoute().getNlri());
		Assert.assertTrue(equalCollections(attrs2, node.getRoute().getPathAttributes()));		

		node = parent.getChildNodes().last();

		Assert.assertNotNull(node);
		Assert.assertEquals(moreNlri2, node.getRoute().getNlri());
		Assert.assertTrue(equalCollections(attrs2, node.getRoute().getPathAttributes()));
		
		// remove
		Assert.assertTrue(tree.withdrawRoute(new Route(AddressFamilyKey.IPV4_UNICAST_FORWARDING, lessNlri, null, null)));
		
		node = tree.getRootNode().getChildNodes().first();
		Assert.assertNotNull(node);
		Assert.assertEquals(moreNlri1, node.getRoute().getNlri());
		Assert.assertTrue(equalCollections(attrs2, node.getRoute().getPathAttributes()));		
		
		node = tree.getRootNode().getChildNodes().last();
		Assert.assertNotNull(node);
		Assert.assertEquals(moreNlri2, node.getRoute().getNlri());
		Assert.assertTrue(equalCollections(attrs2, node.getRoute().getPathAttributes()));
	}

	@Test
	public void testAddTwoNodesFirstLessSpecificSecondMoreSpecificRemoveLessSpecific() {
		NetworkLayerReachabilityInformation lessNlri = new NetworkLayerReachabilityInformation(24, new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x04 }); // prefix 192.168.4/24
		NetworkLayerReachabilityInformation moreNlri = new NetworkLayerReachabilityInformation(28, 
				new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x04, (byte)0x40 }); // prefix 192.168.4.64/28
		RoutingTree.RoutingTreeNode node;

		// add nodes
		Assert.assertTrue(tree.addRoute(new Route(null, lessNlri, attrs1, null)));
		Assert.assertTrue(tree.addRoute(new Route(null, moreNlri, attrs2, null)));

		node = tree.getRootNode().getChildNodes().first();
		
		Assert.assertNotNull(node);
		Assert.assertEquals(lessNlri, node.getRoute().getNlri());
		Assert.assertTrue(equalCollections(attrs1, node.getRoute().getPathAttributes()));
		
		node = node.getChildNodes().first();

		Assert.assertNotNull(node);
		Assert.assertEquals(moreNlri, node.getRoute().getNlri());
		Assert.assertTrue(equalCollections(attrs2, node.getRoute().getPathAttributes()));		
		
		// remove node
		Assert.assertTrue(tree.withdrawRoute(new Route(AddressFamilyKey.IPV4_UNICAST_FORWARDING, lessNlri, null, null)));

		node = tree.getRootNode().getChildNodes().first();
		
		Assert.assertNotNull(node);
		Assert.assertEquals(moreNlri, node.getRoute().getNlri());
		Assert.assertTrue(equalCollections(attrs2, node.getRoute().getPathAttributes()));		
	}

	@Test
	public void testLookupExactMatch() {
		NetworkLayerReachabilityInformation lessNlri = new NetworkLayerReachabilityInformation(24, new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x04 }); // prefix 192.168.4/24
		NetworkLayerReachabilityInformation moreNlri = new NetworkLayerReachabilityInformation(28, 
				new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x04, (byte)0x40 }); // prefix 192.168.4.64/28

		// add nodes
		Assert.assertTrue(tree.addRoute(new Route(null, lessNlri, attrs1, null)));
		Assert.assertTrue(tree.addRoute(new Route(null, moreNlri, attrs2, null)));

		LookupResult result = tree.lookupRoute(moreNlri);
		
		Assert.assertNotNull(result);
		Assert.assertEquals(moreNlri, result.getRoute().getNlri());
		Assert.assertEquals(attrs2, result.getRoute().getPathAttributes());
	}

	@Test
	public void testLookupExactMatchNextHop() throws Exception {
		NetworkLayerReachabilityInformation lessNlri = new NetworkLayerReachabilityInformation(24, new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x04 }); // prefix 192.168.4/24
		NetworkLayerReachabilityInformation moreNlri = new NetworkLayerReachabilityInformation(28, 
				new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x04, (byte)0x40 }); // prefix 192.168.4.64/28
		InetAddress lessNextHop = InetAddress.getByAddress(new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x01, (byte)0x01 });
		InetAddress moreNextHop = InetAddress.getByAddress(new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x01, (byte)0x02 });

		// add nodes
		Assert.assertTrue(tree.addRoute(new Route(null, lessNlri, attrs1,  new InetAddressNextHop<InetAddress>(lessNextHop))));
		Assert.assertTrue(tree.addRoute(new Route(null, moreNlri, attrs2,  new InetAddressNextHop<InetAddress>(moreNextHop))));

		LookupResult result = tree.lookupRoute(moreNlri);
		
		Assert.assertNotNull(result);
		Assert.assertEquals(moreNlri, result.getRoute().getNlri());
		Assert.assertEquals(attrs2, result.getRoute().getPathAttributes());
		Assert.assertEquals(new InetAddressNextHop<InetAddress>(moreNextHop), result.getRoute().getNextHop());
	}

	@Test
	public void testLookupInexactMatch() {
		NetworkLayerReachabilityInformation lessNlri = new NetworkLayerReachabilityInformation(24, new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x04 }); // prefix 192.168.4/24
		NetworkLayerReachabilityInformation moreNlri = new NetworkLayerReachabilityInformation(28, 
				new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x04, (byte)0x10 }); // prefix 192.168.4.16/28
		NetworkLayerReachabilityInformation lookupNlri = new NetworkLayerReachabilityInformation(26, 
				new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x04, (byte)0x00 }); // prefix 192.168.4.0/26

		// add nodes
		Assert.assertTrue(tree.addRoute(new Route(null, lessNlri, attrs1, null)));
		Assert.assertTrue(tree.addRoute(new Route(null, moreNlri, attrs2, null)));

		LookupResult result = tree.lookupRoute(lookupNlri);
		
		Assert.assertNotNull(result);
		Assert.assertEquals(lessNlri, result.getRoute().getNlri());
		Assert.assertEquals(attrs1, result.getRoute().getPathAttributes());
	}

	@Test
	public void testLookupInexactMatchNextHop() throws Exception {
		NetworkLayerReachabilityInformation lessNlri = new NetworkLayerReachabilityInformation(24, new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x04 }); // prefix 192.168.4/24
		NetworkLayerReachabilityInformation moreNlri = new NetworkLayerReachabilityInformation(28, 
				new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x04, (byte)0x10 }); // prefix 192.168.4.16/28
		NetworkLayerReachabilityInformation lookupNlri = new NetworkLayerReachabilityInformation(26, 
				new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x04, (byte)0x00 }); // prefix 192.168.4.0/26
		InetAddress lessNextHop = InetAddress.getByAddress(new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x01, (byte)0x01 });
		InetAddress moreNextHop = InetAddress.getByAddress(new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x01, (byte)0x02 });
		
		// add nodes
		Assert.assertTrue(tree.addRoute(new Route(null, lessNlri, attrs1, new InetAddressNextHop<InetAddress>(lessNextHop))));
		Assert.assertTrue(tree.addRoute(new Route(null, moreNlri, attrs2, new InetAddressNextHop<InetAddress>(moreNextHop))));

		LookupResult result = tree.lookupRoute(lookupNlri);
		
		Assert.assertNotNull(result);
		Assert.assertEquals(lessNlri, result.getRoute().getNlri());
		Assert.assertEquals(attrs1, result.getRoute().getPathAttributes());
		Assert.assertEquals(new InetAddressNextHop<InetAddress>(lessNextHop), result.getRoute().getNextHop());
	}

	@Test
	public void testLookupNoMatch() {
		NetworkLayerReachabilityInformation lessNlri = new NetworkLayerReachabilityInformation(24, new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x04 }); // prefix 192.168.4/24
		NetworkLayerReachabilityInformation moreNlri = new NetworkLayerReachabilityInformation(28, 
				new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x04, (byte)0x10 }); // prefix 192.168.4.16/28
		NetworkLayerReachabilityInformation lookupNlri = new NetworkLayerReachabilityInformation(24, 
				new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x05 }); // prefix 192.168.5.0/24

		// add nodes
		Assert.assertTrue(tree.addRoute(new Route(null, lessNlri, attrs1, null)));
		Assert.assertTrue(tree.addRoute(new Route(null, moreNlri, attrs2, null)));

		Assert.assertNull(tree.lookupRoute(lookupNlri));
	}

	public static class RecordingNodeVisitor implements RoutingTreeVisitor {

		private List<Route> records = new LinkedList<Route>();

		@Override
		public void visitRouteTreeNode(Route route) {
			records.add(route);
		}

		/**
		 * @return the records
		 */
		public List<Route> getRecords() {
			return records;
		}
	}
	
	@Test
	public void testVisitNodes() {
		NetworkLayerReachabilityInformation lessNlri = new NetworkLayerReachabilityInformation(24, new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x04 }); // prefix 192.168.4/24
		NetworkLayerReachabilityInformation moreNlri1 = new NetworkLayerReachabilityInformation(28, 
				new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x04, (byte)0x10 }); // prefix 192.168.4.16/28
		NetworkLayerReachabilityInformation moreNlri2 = new NetworkLayerReachabilityInformation(28, 
				new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x04, (byte)0x20 }); // prefix 192.168.4.32/28
		RecordingNodeVisitor visitor = new RecordingNodeVisitor();
		
		Assert.assertTrue(tree.addRoute(new Route(null, moreNlri1, attrs2, null)));
		Assert.assertTrue(tree.addRoute(new Route(null, lessNlri, attrs1, null)));
		Assert.assertTrue(tree.addRoute(new Route(null, moreNlri2, attrs2, null)));

		tree.visitTree(visitor);
		
		Iterator<Route> it = visitor.getRecords().iterator();
		
		Assert.assertTrue(it.hasNext());
		Assert.assertEquals(new Route(null, lessNlri, attrs1, null), it.next());
		Assert.assertTrue(it.hasNext());
		Assert.assertEquals(new Route(null, moreNlri1, attrs2, null), it.next());
		Assert.assertTrue(it.hasNext());
		Assert.assertEquals(new Route(null, moreNlri2, attrs2, null), it.next());
		Assert.assertFalse(it.hasNext());		
	}

	private <T> boolean equalCollections(Collection<T> col1, Collection<T> col2) {
		if(col1.size() != col2.size())
			return false;
		
		for(T member : col1)
			if(!col2.contains(member))
				return false;
		
		return true;
	}
}
