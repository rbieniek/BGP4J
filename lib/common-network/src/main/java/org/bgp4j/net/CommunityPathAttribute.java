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
package org.bgp4j.net;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class CommunityPathAttribute extends PathAttribute {

	public CommunityPathAttribute() {
		super(Category.OPTIONAL_TRANSITIVE);
	}

	public static class CommunityMember {
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

}
