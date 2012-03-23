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
 * File: org.bgp4j.netty.protocol.update.CommunitiesPathAttribute.java 
 */
package org.bgp4j.net.attributes;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class CommunityPathAttribute extends PathAttribute {

	public CommunityPathAttribute() {
		super(Category.OPTIONAL_TRANSITIVE);
	}

	public static class CommunityMember implements Comparable<CommunityMember> {
		private int asNumber;
		private int memberFlags;
		/**
		 * @return the asNumber
		 */
		public int getAsNumber() {
			return asNumber;
		}
		/**
		 * @param asNumber the asNumber to set
		 */
		public void setAsNumber(int asNumber) {
			this.asNumber = asNumber;
		}
		/**
		 * @return the memberFlags
		 */
		public int getMemberFlags() {
			return memberFlags;
		}
		/**
		 * @param memberFlags the memberFlags to set
		 */
		public void setMemberFlags(int memberFlags) {
			this.memberFlags = memberFlags;
		}
		
		@Override
		public int compareTo(CommunityMember o) {
			return (new CompareToBuilder())
				.append(getAsNumber(), o.getAsNumber())
				.append(getMemberFlags(), o.getMemberFlags())
				.toComparison();
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			return (new HashCodeBuilder())
				.append(getAsNumber())
				.append(getMemberFlags())
				.toHashCode();
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if(!(obj instanceof CommunityMember))
				return false;
			
			CommunityMember o = (CommunityMember)obj;
			
			return (new EqualsBuilder())
				.append(getAsNumber(), o.getAsNumber())
				.append(getMemberFlags(), o.getMemberFlags())
				.isEquals();
		}
	}
	
	private int community;
	private List<CommunityMember> members = new LinkedList<CommunityPathAttribute.CommunityMember>();

	/**
	 * @return the community
	 */
	public int getCommunity() {
		return community;
	}

	/**
	 * @param community the community to set
	 */
	public void setCommunity(int community) {
		this.community = community;
	}

	/**
	 * @return the members
	 */
	public List<CommunityMember> getMembers() {
		return members;
	}

	/**
	 * @param members the members to set
	 */
	public void setMembers(List<CommunityMember> members) {
		this.members = members;
	}

	@Override
	protected PathAttributeType internalType() {
		return PathAttributeType.COMMUNITY;
	}

	@Override
	protected boolean subclassEquals(PathAttribute obj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected int sublcassHashCode() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int subclassCompareTo(PathAttribute o) {
		// TODO Auto-generated method stub
		return 0;
	}

}
