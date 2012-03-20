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
 * File: org.bgp4j.rib.RoutingTree.java 
 */
package org.bgp4j.rib;

import java.util.Collection;
import java.util.NavigableSet;
import java.util.TreeSet;

import org.bgp4j.net.NetworkLayerReachabilityInformation;
import org.bgp4j.net.PathAttribute;

/**
 * This class builds and manages a tree of (NLRI, Path attributes) tuples. The tree is build top-down
 * whereas the parent node always contains more coarse-grained routing information than the child nodes.
 * 
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
class RoutingTree {

	/**
	 * Internal node of the routing tree. The discriminating fact is the NLRI attached to the node. Therefore
	 * the ordering of the tree nodes is based solely on NLRI ordering.  
	 * 
	 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
	 *
	 */
	class RoutingTreeNode implements Comparable<RoutingTreeNode> {
		private NetworkLayerReachabilityInformation nlri;
		private Collection<PathAttribute> pathAttributes;
		private NavigableSet<RoutingTreeNode> childNodes = new TreeSet<RoutingTree.RoutingTreeNode>();

		public RoutingTreeNode(NetworkLayerReachabilityInformation nlri, Collection<PathAttribute> pathAttributes) {
			this.nlri = nlri;
			this.pathAttributes = pathAttributes;
		}
		
		@Override
		public int compareTo(RoutingTreeNode o) {
			return nlri.compareTo(o.nlri);
		}
		
		@Override
		public int hashCode() {
			return nlri.hashCode();
		}
		
		@Override
		public boolean equals(Object o) {
			if(!(o instanceof RoutingTreeNode))
				return false;
			
			return nlri.equals(((RoutingTreeNode)o).nlri);
		}

		/**
		 * @return the nlri
		 */
		NetworkLayerReachabilityInformation getNlri() {
			return nlri;
		}

		/**
		 * @return the pathAttributes
		 */
		Collection<PathAttribute> getPathAttributes() {
			return pathAttributes;
		}

		/**
		 * @return the childNodes
		 */
		NavigableSet<RoutingTreeNode> getChildNodes() {
			return childNodes;
		}
	}
	
	// the root of all nodes managed by this routing tree. This is the only node w/o a (NLRI prefix, Path attributes) tuple attached to it
	private RoutingTreeNode rootNode = new RoutingTreeNode(null, null);
	
	/**
	 * Destroy the routing tree and delete all information held within.
	 */
	void destroy() {
		rootNode.getChildNodes().clear();
	}
	
	/**
	 * Add a (NLRI, Path attributes) tuple to the tree
	 * 
	 * @param nlri the NLRI prefix to be added
	 * @param pathAttributes the path attributes belonging to this prefix
	 * @return <code>true<code> if the node was added, <code>false</code> if the node was not added
	 */
	synchronized boolean addRoute(NetworkLayerReachabilityInformation nlri, Collection<PathAttribute> pathAttributes) {
		return addRoute(this.rootNode, new RoutingTreeNode(nlri, pathAttributes));
	}

	/**
	 * Add a new node to a parent routing tree node. The rules for this process are as follows:
	 * <ol>
	 * <li>If a child node NLRI prefix is less specific than the NLRI prefix of the new node, recursively descend with the child node as
	 * the new parent node</li>
	 * <li>If the new node NLRI prefix is less specific than one or more child node NLRI prefixes, add the new node and reparent 
	 * the child nodes to the new node</li>
	 * <li>If a child node NLRI prefix equals the new node NLRI prefix, then the path attributes are replaced</li>
	 * <li>If neither of the conditions above holds true then simply add the new node to the parent.</li>
	 * </ol> 
	 * 
	 * @param parent
	 * @param newNode
	 * @return
	 */
	private boolean addRoute(RoutingTreeNode parent, RoutingTreeNode newNode) {
		boolean added = false;
		boolean handled = false;
		NavigableSet<RoutingTreeNode> reparentedNodes = new TreeSet<RoutingTree.RoutingTreeNode>();
		
		for(RoutingTreeNode child : parent.getChildNodes()) {
			if(child.getNlri().equals(newNode.getNlri())) {
				// we have an exact match on the NLRI preifxes --> just replace the path attributes but signal as addition
				child.getPathAttributes().clear();
				child.getPathAttributes().addAll(newNode.getPathAttributes());
				
				handled = true;
				added = true;
				break;
			} else if(child.getNlri().isPrefixOf(newNode.getNlri())) {
				// a child node has more coarse-grained routing info attached --> make this child node parent of the new node
				added = addRoute(child, newNode);
				handled = true;
				break;
			} else if(newNode.getNlri().isPrefixOf(child.getNlri())) {
				// the new node has more coarse-grained routing info attached --> the child must be reparented to the new node.
				reparentedNodes.add(child);
			}
		}
		
		if(!handled) {
			parent.getChildNodes().add(newNode);
			added = true;
			
			// we have nodes that need to be reparented to the new node
			if(reparentedNodes.size() > 0) {
				newNode.getChildNodes().addAll(reparentedNodes);
				parent.getChildNodes().removeAll(reparentedNodes);
			}
		}
		
		return added;
	}
	
	/**
	 * Withdraw the (NLRI, Path attributes) prefix from the tree. 
	 * 
	 * @param nlri the NLRI prefix to withdraw
	 * @return <code>true</code> if the node was removed, <code>false</code> otherwise
	 */
	synchronized boolean withdrawRoute(NetworkLayerReachabilityInformation nlri) {
		return false;
	}

	/**
	 * @return the rootNode
	 */
	RoutingTreeNode getRootNode() {
		return rootNode;
	}
	
}
