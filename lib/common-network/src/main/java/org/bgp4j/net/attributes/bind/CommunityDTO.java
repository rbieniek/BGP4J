/**
 * 
 */
package org.bgp4j.net.attributes.bind;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.bgp4j.net.attributes.CommunityMember;
import org.bgp4j.net.attributes.CommunityPathAttribute;

/**
 * @author rainer
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class CommunityDTO {

	private int community;
	private List<CommunityMember> members = new LinkedList<CommunityMember>();
	
	public CommunityDTO() {}
	
	public CommunityDTO(CommunityPathAttribute pa) {
		setCommunity(pa.getCommunity());
		setMembers(pa.getMembers());
	}
	
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
