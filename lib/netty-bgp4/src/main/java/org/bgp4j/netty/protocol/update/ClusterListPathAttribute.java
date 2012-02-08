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
package org.bgp4j.netty.protocol.update;

import java.util.LinkedList;
import java.util.List;

import org.bgp4j.netty.BGPv4Constants;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class ClusterListPathAttribute extends Attribute {

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

	/* (non-Javadoc)
	 * @see org.bgp4j.netty.protocol.update.Attribute#getTypeCode()
	 */
	@Override
	protected int getTypeCode() {
		return BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_CLUSTER_LIST;
	}

	/* (non-Javadoc)
	 * @see org.bgp4j.netty.protocol.update.Attribute#getValueLength()
	 */
	@Override
	protected int getValueLength() {
		int size = 0;
		
		if(this.clusterIds != null)
			size += this.clusterIds.size() * 4;
		
		return size;
	}

	/* (non-Javadoc)
	 * @see org.bgp4j.netty.protocol.update.Attribute#encodeValue()
	 */
	@Override
	protected ChannelBuffer encodeValue() {
		ChannelBuffer buffer = ChannelBuffers.buffer(getValueLength());
		
		for(int clusterId : clusterIds)
			buffer.writeInt(clusterId);
		
		return buffer;
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
		this.clusterIds = clusterIds;
	}

}
