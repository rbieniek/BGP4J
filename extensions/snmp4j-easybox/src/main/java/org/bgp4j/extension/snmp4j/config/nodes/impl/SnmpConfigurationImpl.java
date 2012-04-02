/**
 * 
 */
package org.bgp4j.extension.snmp4j.config.nodes.impl;

import java.net.InetAddress;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.bgp4j.extension.snmp4j.config.nodes.SnmpConfiguration;

/**
 * @author rainer
 *
 */
class SnmpConfigurationImpl implements SnmpConfiguration {

	SnmpConfigurationImpl() {}
	
	SnmpConfigurationImpl(InetAddress address, String community) {
		this.address = address;
		this.community = community;
	}
	
	private InetAddress address;
	private String community;
	
	/* (non-Javadoc)
	 * @see org.bgp4j.extension.snmp4j.config.nodes.SnmpConfiguration#getAddress()
	 */
	@Override
	public InetAddress getAddress() {
		return address;
	}

	/* (non-Javadoc)
	 * @see org.bgp4j.extension.snmp4j.config.nodes.SnmpConfiguration#getCommunity()
	 */
	@Override
	public String getCommunity() {
		return community;
	}

	/**
	 * @param address the address to set
	 */
	void setAddress(InetAddress address) {
		this.address = address;
	}

	/**
	 * @param community the community to set
	 */
	void setCommunity(String community) {
		this.community = community;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return (new HashCodeBuilder())
				.append(getAddress())
				.append(getCommunity())
				.toHashCode();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof SnmpConfiguration))
			return false;
		
		SnmpConfiguration o = (SnmpConfiguration)obj;
		
		return (new EqualsBuilder())
				.append(getAddress(), o.getAddress())
				.append(getCommunity(), o.getCommunity())
				.isEquals();
	}
}
