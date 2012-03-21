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

import java.util.Collection;
import java.util.HashSet;

import junit.framework.Assert;

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
		
		Assert.assertTrue(tree.addRoute(nlri, attrs1));
		
		RoutingTree.RoutingTreeNode node = tree.getRootNode().getChildNodes().first();
		
		Assert.assertNotNull(node);
		Assert.assertEquals(nlri, node.getNlri());
		Assert.assertTrue(equalCollections(attrs1, node.getPathAttributes()));
	}
	
	@Test
	public void testAddTwoSiblingNodesFirstLowerSecondHigher() {
		NetworkLayerReachabilityInformation lowerNlri = new NetworkLayerReachabilityInformation(24, new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x04 }); // prefix 192.168.4/24
		NetworkLayerReachabilityInformation higherNlri = new NetworkLayerReachabilityInformation(24, new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x08 }); // prefix 192.168.8/24
		RoutingTree.RoutingTreeNode node;
		
		Assert.assertTrue(tree.addRoute(lowerNlri, attrs1));
		Assert.assertTrue(tree.addRoute(higherNlri, attrs2));
		
		node = tree.getRootNode().getChildNodes().first();
		
		Assert.assertNotNull(node);
		Assert.assertEquals(lowerNlri, node.getNlri());
		Assert.assertTrue(equalCollections(attrs1, node.getPathAttributes()));		
		
		node = tree.getRootNode().getChildNodes().last();
		
		Assert.assertNotNull(node);
		Assert.assertEquals(higherNlri, node.getNlri());
		Assert.assertTrue(equalCollections(attrs2, node.getPathAttributes()));		

	}
	
	
	@Test
	public void testAddTwoSiblingNodesFirstHigherSecondLower() {
		NetworkLayerReachabilityInformation lowerNlri = new NetworkLayerReachabilityInformation(24, new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x04 }); // prefix 192.168.4/24
		NetworkLayerReachabilityInformation higherNlri = new NetworkLayerReachabilityInformation(24, new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x08 }); // prefix 192.168.8/24
		RoutingTree.RoutingTreeNode node;
		
		Assert.assertTrue(tree.addRoute(higherNlri, attrs2));
		Assert.assertTrue(tree.addRoute(lowerNlri, attrs1));
		
		node = tree.getRootNode().getChildNodes().first();
		
		Assert.assertNotNull(node);
		Assert.assertEquals(lowerNlri, node.getNlri());
		Assert.assertTrue(equalCollections(attrs1, node.getPathAttributes()));		
		
		node = tree.getRootNode().getChildNodes().last();
		
		Assert.assertNotNull(node);
		Assert.assertEquals(higherNlri, node.getNlri());
		Assert.assertTrue(equalCollections(attrs2, node.getPathAttributes()));		

	}

	@Test
	public void testAddSingleNodeReplacePathAttributes() {
		NetworkLayerReachabilityInformation nlri = new NetworkLayerReachabilityInformation(0, null); // default route prefix
		RoutingTree.RoutingTreeNode node;
		
		Assert.assertTrue(tree.addRoute(nlri, attrs1));
		
		node = tree.getRootNode().getChildNodes().first();
		
		Assert.assertNotNull(node);
		Assert.assertEquals(nlri, node.getNlri());
		Assert.assertTrue(equalCollections(attrs1, node.getPathAttributes()));
		
		Assert.assertTrue(tree.addRoute(nlri, attrs2));
		
		node = tree.getRootNode().getChildNodes().first();
		
		Assert.assertNotNull(node);
		Assert.assertEquals(nlri, node.getNlri());
		Assert.assertTrue(equalCollections(attrs2, node.getPathAttributes()));
	}

	@Test
	public void testAddTwoNodesFirstLessSpecificSecondMoreSpecific() {
		NetworkLayerReachabilityInformation lessNlri = new NetworkLayerReachabilityInformation(24, new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x04 }); // prefix 192.168.4/24
		NetworkLayerReachabilityInformation moreNlri = new NetworkLayerReachabilityInformation(28, 
				new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x04, (byte)0x40 }); // prefix 192.168.4.16/28
		RoutingTree.RoutingTreeNode node;

		Assert.assertTrue(tree.addRoute(lessNlri, attrs1));
		Assert.assertTrue(tree.addRoute(moreNlri, attrs2));

		node = tree.getRootNode().getChildNodes().first();
		
		Assert.assertNotNull(node);
		Assert.assertEquals(lessNlri, node.getNlri());
		Assert.assertTrue(equalCollections(attrs1, node.getPathAttributes()));
		
		node = node.getChildNodes().first();

		Assert.assertNotNull(node);
		Assert.assertEquals(moreNlri, node.getNlri());
		Assert.assertTrue(equalCollections(attrs2, node.getPathAttributes()));		
	}
	
	@Test
	public void testAddTwoNodesFirstMoreSpecificSecondLessSpecific() {
		NetworkLayerReachabilityInformation lessNlri = new NetworkLayerReachabilityInformation(24, new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x04 }); // prefix 192.168.4/24
		NetworkLayerReachabilityInformation moreNlri = new NetworkLayerReachabilityInformation(28, 
				new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x04, (byte)0x40 }); // prefix 192.168.4.16/28
		RoutingTree.RoutingTreeNode node;

		Assert.assertTrue(tree.addRoute(moreNlri, attrs2));
		Assert.assertTrue(tree.addRoute(lessNlri, attrs1));

		node = tree.getRootNode().getChildNodes().first();
		
		Assert.assertNotNull(node);
		Assert.assertEquals(lessNlri, node.getNlri());
		Assert.assertTrue(equalCollections(attrs1, node.getPathAttributes()));
		
		node = node.getChildNodes().first();

		Assert.assertNotNull(node);
		Assert.assertEquals(moreNlri, node.getNlri());
		Assert.assertTrue(equalCollections(attrs2, node.getPathAttributes()));		
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

		Assert.assertTrue(tree.addRoute(moreNlri1, attrs2));
		Assert.assertTrue(tree.addRoute(moreNlri2, attrs2));
		Assert.assertTrue(tree.addRoute(lessNlri, attrs1));

		parent = tree.getRootNode().getChildNodes().first();
		
		Assert.assertNotNull(parent);
		Assert.assertEquals(lessNlri, parent.getNlri());
		Assert.assertTrue(equalCollections(attrs1, parent.getPathAttributes()));
		
		node = parent.getChildNodes().first();

		Assert.assertNotNull(node);
		Assert.assertEquals(moreNlri1, node.getNlri());
		Assert.assertTrue(equalCollections(attrs2, node.getPathAttributes()));		

		node = parent.getChildNodes().last();

		Assert.assertNotNull(node);
		Assert.assertEquals(moreNlri2, node.getNlri());
		Assert.assertTrue(equalCollections(attrs2, node.getPathAttributes()));		
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

		Assert.assertTrue(tree.addRoute(moreNlri1, attrs2));
		Assert.assertTrue(tree.addRoute(lessNlri, attrs1));
		Assert.assertTrue(tree.addRoute(moreNlri2, attrs2));

		parent = tree.getRootNode().getChildNodes().first();
		
		Assert.assertNotNull(parent);
		Assert.assertEquals(lessNlri, parent.getNlri());
		Assert.assertTrue(equalCollections(attrs1, parent.getPathAttributes()));
		
		node = parent.getChildNodes().first();

		Assert.assertNotNull(node);
		Assert.assertEquals(moreNlri1, node.getNlri());
		Assert.assertTrue(equalCollections(attrs2, node.getPathAttributes()));		

		node = parent.getChildNodes().last();

		Assert.assertNotNull(node);
		Assert.assertEquals(moreNlri2, node.getNlri());
		Assert.assertTrue(equalCollections(attrs2, node.getPathAttributes()));		
	}
	
	@Test
	public void testAddAndRemoveSingleNode() {
		NetworkLayerReachabilityInformation nlri = new NetworkLayerReachabilityInformation(0, null); // default route prefix
		
		Assert.assertTrue(tree.addRoute(nlri, attrs1));
		
		RoutingTree.RoutingTreeNode node = tree.getRootNode().getChildNodes().first();
		
		Assert.assertNotNull(node);
		Assert.assertEquals(nlri, node.getNlri());
		Assert.assertTrue(equalCollections(attrs1, node.getPathAttributes()));
		
		Assert.assertTrue(tree.withdrawRoute(nlri));
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
		Assert.assertTrue(tree.addRoute(lessNlri, attrs1));
		Assert.assertTrue(tree.addRoute(moreNlri, attrs2));

		node = tree.getRootNode().getChildNodes().first();
		
		Assert.assertNotNull(node);
		Assert.assertEquals(lessNlri, node.getNlri());
		Assert.assertTrue(equalCollections(attrs1, node.getPathAttributes()));
		
		node = node.getChildNodes().first();

		Assert.assertNotNull(node);
		Assert.assertEquals(moreNlri, node.getNlri());
		Assert.assertTrue(equalCollections(attrs2, node.getPathAttributes()));		
		
		// remove node
		Assert.assertFalse(tree.withdrawRoute(notExistingNlri));

		node = tree.getRootNode().getChildNodes().first();
		
		Assert.assertNotNull(node);
		Assert.assertEquals(lessNlri, node.getNlri());
		Assert.assertTrue(equalCollections(attrs1, node.getPathAttributes()));
		
		node = node.getChildNodes().first();

		Assert.assertNotNull(node);
		Assert.assertEquals(moreNlri, node.getNlri());
		Assert.assertTrue(equalCollections(attrs2, node.getPathAttributes()));		
	}
		
	@Test
	public void testAddTwoNodesFirstLessSpecificSecondMoreSpecificRemoveMoreSpecific() {
		NetworkLayerReachabilityInformation lessNlri = new NetworkLayerReachabilityInformation(24, new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x04 }); // prefix 192.168.4/24
		NetworkLayerReachabilityInformation moreNlri = new NetworkLayerReachabilityInformation(28, 
				new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x04, (byte)0x40 }); // prefix 192.168.4.64/28
		RoutingTree.RoutingTreeNode node;

		// add nodes
		Assert.assertTrue(tree.addRoute(lessNlri, attrs1));
		Assert.assertTrue(tree.addRoute(moreNlri, attrs2));

		node = tree.getRootNode().getChildNodes().first();
		
		Assert.assertNotNull(node);
		Assert.assertEquals(lessNlri, node.getNlri());
		Assert.assertTrue(equalCollections(attrs1, node.getPathAttributes()));
		
		node = node.getChildNodes().first();

		Assert.assertNotNull(node);
		Assert.assertEquals(moreNlri, node.getNlri());
		Assert.assertTrue(equalCollections(attrs2, node.getPathAttributes()));		
		
		// remove node
		Assert.assertTrue(tree.withdrawRoute(moreNlri));

		node = tree.getRootNode().getChildNodes().first();
		
		Assert.assertNotNull(node);
		Assert.assertEquals(lessNlri, node.getNlri());
		Assert.assertTrue(equalCollections(attrs1, node.getPathAttributes()));
		
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

		Assert.assertTrue(tree.addRoute(moreNlri1, attrs2));
		Assert.assertTrue(tree.addRoute(lessNlri, attrs1));
		Assert.assertTrue(tree.addRoute(moreNlri2, attrs2));

		parent = tree.getRootNode().getChildNodes().first();
		
		Assert.assertNotNull(parent);
		Assert.assertEquals(lessNlri, parent.getNlri());
		Assert.assertTrue(equalCollections(attrs1, parent.getPathAttributes()));
		
		node = parent.getChildNodes().first();

		Assert.assertNotNull(node);
		Assert.assertEquals(moreNlri1, node.getNlri());
		Assert.assertTrue(equalCollections(attrs2, node.getPathAttributes()));		

		node = parent.getChildNodes().last();

		Assert.assertNotNull(node);
		Assert.assertEquals(moreNlri2, node.getNlri());
		Assert.assertTrue(equalCollections(attrs2, node.getPathAttributes()));
		
		// remove
		Assert.assertTrue(tree.withdrawRoute(lessNlri));
		
		node = tree.getRootNode().getChildNodes().first();
		Assert.assertNotNull(node);
		Assert.assertEquals(moreNlri1, node.getNlri());
		Assert.assertTrue(equalCollections(attrs2, node.getPathAttributes()));		
		
		node = tree.getRootNode().getChildNodes().last();
		Assert.assertNotNull(node);
		Assert.assertEquals(moreNlri2, node.getNlri());
		Assert.assertTrue(equalCollections(attrs2, node.getPathAttributes()));
	}

	@Test
	public void testAddTwoNodesFirstLessSpecificSecondMoreSpecificRemoveLessSpecific() {
		NetworkLayerReachabilityInformation lessNlri = new NetworkLayerReachabilityInformation(24, new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x04 }); // prefix 192.168.4/24
		NetworkLayerReachabilityInformation moreNlri = new NetworkLayerReachabilityInformation(28, 
				new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x04, (byte)0x40 }); // prefix 192.168.4.64/28
		RoutingTree.RoutingTreeNode node;

		// add nodes
		Assert.assertTrue(tree.addRoute(lessNlri, attrs1));
		Assert.assertTrue(tree.addRoute(moreNlri, attrs2));

		node = tree.getRootNode().getChildNodes().first();
		
		Assert.assertNotNull(node);
		Assert.assertEquals(lessNlri, node.getNlri());
		Assert.assertTrue(equalCollections(attrs1, node.getPathAttributes()));
		
		node = node.getChildNodes().first();

		Assert.assertNotNull(node);
		Assert.assertEquals(moreNlri, node.getNlri());
		Assert.assertTrue(equalCollections(attrs2, node.getPathAttributes()));		
		
		// remove node
		Assert.assertTrue(tree.withdrawRoute(lessNlri));

		node = tree.getRootNode().getChildNodes().first();
		
		Assert.assertNotNull(node);
		Assert.assertEquals(moreNlri, node.getNlri());
		Assert.assertTrue(equalCollections(attrs2, node.getPathAttributes()));		
	}

	@Test
	public void testLookupExactMatch() {
		NetworkLayerReachabilityInformation lessNlri = new NetworkLayerReachabilityInformation(24, new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x04 }); // prefix 192.168.4/24
		NetworkLayerReachabilityInformation moreNlri = new NetworkLayerReachabilityInformation(28, 
				new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x04, (byte)0x40 }); // prefix 192.168.4.64/28

		// add nodes
		Assert.assertTrue(tree.addRoute(lessNlri, attrs1));
		Assert.assertTrue(tree.addRoute(moreNlri, attrs2));

		LookupResult result = tree.lookupRoute(moreNlri);
		
		Assert.assertNotNull(result);
		Assert.assertEquals(moreNlri, result.getNlri());
		Assert.assertEquals(attrs2, result.getPathAttributes());
	}

	@Test
	public void testLookupInexactMatch() {
		NetworkLayerReachabilityInformation lessNlri = new NetworkLayerReachabilityInformation(24, new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x04 }); // prefix 192.168.4/24
		NetworkLayerReachabilityInformation moreNlri = new NetworkLayerReachabilityInformation(28, 
				new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x04, (byte)0x10 }); // prefix 192.168.4.16/28
		NetworkLayerReachabilityInformation lookupNlri = new NetworkLayerReachabilityInformation(26, 
				new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x04, (byte)0x00 }); // prefix 192.168.4.0/26

		// add nodes
		Assert.assertTrue(tree.addRoute(lessNlri, attrs1));
		Assert.assertTrue(tree.addRoute(moreNlri, attrs2));

		LookupResult result = tree.lookupRoute(lookupNlri);
		
		Assert.assertNotNull(result);
		Assert.assertEquals(lessNlri, result.getNlri());
		Assert.assertEquals(attrs1, result.getPathAttributes());
	}


	@Test
	public void testLookupNoMatch() {
		NetworkLayerReachabilityInformation lessNlri = new NetworkLayerReachabilityInformation(24, new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x04 }); // prefix 192.168.4/24
		NetworkLayerReachabilityInformation moreNlri = new NetworkLayerReachabilityInformation(28, 
				new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x04, (byte)0x10 }); // prefix 192.168.4.16/28
		NetworkLayerReachabilityInformation lookupNlri = new NetworkLayerReachabilityInformation(24, 
				new byte[] { (byte)0xc0, (byte)0xa8, (byte)0x05 }); // prefix 192.168.5.0/24

		// add nodes
		Assert.assertTrue(tree.addRoute(lessNlri, attrs1));
		Assert.assertTrue(tree.addRoute(moreNlri, attrs2));

		Assert.assertNull(tree.lookupRoute(lookupNlri));
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
