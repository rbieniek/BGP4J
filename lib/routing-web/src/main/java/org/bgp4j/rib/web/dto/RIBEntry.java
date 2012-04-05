/**
 * 
 */
package org.bgp4j.rib.web.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.bgp4j.net.AddressFamily;
import org.bgp4j.net.AddressFamilyKey;
import org.bgp4j.net.RIBSide;
import org.bgp4j.net.SubsequentAddressFamily;

/**
 * @author rainer
 *
 */
@XmlRootElement
public class RIBEntry implements Comparable<RIBEntry> {
	private String name;
	private AddressFamily afi;
	private SubsequentAddressFamily safi;
	private RIBSide side;
	
	public RIBEntry() {}

	public RIBEntry(String name, AddressFamily afi, SubsequentAddressFamily safi, RIBSide side) {
		this.name = name;
		this.afi = afi;
		this.safi = safi;
		this.side = side;
	}
	
	public RIBEntry(String name, AddressFamilyKey afk, RIBSide side) {
		this(name, afk.getAddressFamily(), afk.getSubsequentAddressFamily(), side);
	}
	
	/**
	 * @return the name
	 */
	@XmlElement
	public String getName() {
		return name;
	}
	
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return the afi
	 */
	@XmlElement
	public AddressFamily getAfi() {
		return afi;
	}
	
	/**
	 * @param afi the afi to set
	 */
	public void setAfi(AddressFamily afi) {
		this.afi = afi;
	}
	
	/**
	 * @return the safi
	 */
	@XmlElement
	public SubsequentAddressFamily getSafi() {
		return safi;
	}
	
	/**
	 * @param safi the safi to set
	 */
	public void setSafi(SubsequentAddressFamily safi) {
		this.safi = safi;
	}
	
	/**
	 * @return the side
	 */
	@XmlElement
	public RIBSide getSide() {
		return side;
	}
	
	/**
	 * @param side the side to set
	 */
	public void setSide(RIBSide side) {
		this.side = side;
	}

	@Override
	public int compareTo(RIBEntry o) {
		return (new CompareToBuilder())
				.append(getAfi(), o.getAfi())
				.append(getName(), o.getName())
				.append(getSafi(), o.getSafi())
				.append(getSide(), o.getSide())
				.toComparison();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return (new HashCodeBuilder())
				.append(getAfi())
				.append(getName())
				.append(getSafi())
				.append(getSide())
				.toHashCode();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof RIBEntry))
			return false;

		RIBEntry o = (RIBEntry)obj;
		
		return (new EqualsBuilder())
			.append(getAfi(), o.getAfi())
			.append(getName(), o.getName())
			.append(getSafi(), o.getSafi())
			.append(getSide(), o.getSide())
			.isEquals();
	}
	
	public void setAddressFamilyKey(AddressFamilyKey afk) {
		setAfi(afk.getAddressFamily());
		setSafi(afk.getSubsequentAddressFamily());
	}
	
	@XmlTransient
	public AddressFamilyKey getAddressFamilyKey() {
		return new AddressFamilyKey(getAfi(), getSafi());
	}
}
