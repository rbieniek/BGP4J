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
 * File: org.bgp4j.netty.protocol.update.ClusterListPathAttribute.java 
 */
package org.bgp4j.net.attributes;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class ClusterListPathAttribute extends PathAttribute {

	private List<Integer> clusterIds = new LinkedList<Integer>();
	
	/**
	 * @param category
	 */
	public ClusterListPathAttribute() {
		super(Category.OPTIONAL_NON_TRANSITIVE);
	}

	/**
	 * @param category
	 */
	public ClusterListPathAttribute(int[] clusterIds) {
		super(Category.OPTIONAL_NON_TRANSITIVE);
		
		for(int clusterId : clusterIds)
			this.clusterIds.add(clusterId);
	}

	/**
	 * @return the clusterIds
	 */
	public List<Integer> getClusterIds() {
		return clusterIds;
	}

	/**
	 * @param clusterIds the clusterIds to set
	 */
	public void setClusterIds(List<Integer> clusterIds) {
		if(clusterIds != null)
			this.clusterIds = clusterIds;
		else
			this.clusterIds = new LinkedList<Integer>();
	}

	@Override
	protected PathAttributeType internalType() {
		return PathAttributeType.CLUSTER_LIST;
	}

	@Override
	protected boolean subclassEquals(PathAttribute obj) {
		EqualsBuilder builder = new EqualsBuilder();
		ClusterListPathAttribute o = (ClusterListPathAttribute)obj;
		
		builder.append(getClusterIds().size(), o.getClusterIds().size());
		
		if(builder.isEquals()) {
			Iterator<Integer> lit = getClusterIds().iterator();
			Iterator<Integer> rit = o.getClusterIds().iterator();
			
			while(lit.hasNext())
				builder.append(lit.next(), rit.next());
		}
		
		return builder.isEquals();
	}

	@Override
	protected int subclassHashCode() {
		HashCodeBuilder builder = new HashCodeBuilder();
		Iterator<Integer> it = getClusterIds().iterator();
		
		while(it.hasNext())
			builder.append(it.next());
		
		return builder.toHashCode();
	}

	@Override
	protected int subclassCompareTo(PathAttribute obj) {
		CompareToBuilder builder = new CompareToBuilder();
		ClusterListPathAttribute o = (ClusterListPathAttribute)obj;
		
		builder.append(getClusterIds().size(), o.getClusterIds().size());
		
		if(builder.toComparison() == 0) {
			Iterator<Integer> lit = getClusterIds().iterator();
			Iterator<Integer> rit = o.getClusterIds().iterator();
			
			while(lit.hasNext())
				builder.append(lit.next(), rit.next());
		}
		
		return builder.toComparison();
	}

}
