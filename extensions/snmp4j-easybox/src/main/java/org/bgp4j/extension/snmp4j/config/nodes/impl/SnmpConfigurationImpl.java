/**
 * 
 */
package org.bgp4j.extension.snmp4j.config.nodes.impl;

import java.net.InetAddress;

import org.apache.commons.configuration.ConfigurationException;
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
	private int localPort;
	
	/* (non-Javadoc)
	 * @see org.bgp4j.extension.snmp4j.config.nodes.SnmpConfiguration#getAddress()
	 */
	@Override
	public InetAddress getTargetAddress() {
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
				.append(getTargetAddress())
				.append(getCommunity())
				.append(getLocalPort())
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
				.append(getTargetAddress(), o.getTargetAddress())
				.append(getCommunity(), o.getCommunity())
				.append(getLocalPort(), o.getLocalPort())
				.isEquals();
	}

	@Override
	public int getLocalPort() {
		return localPort;
	}
	
	void setLocalPort(int localPort) throws ConfigurationException {
		if(localPort >= 0 && localPort < 65536)
			this.localPort = localPort;
		else 
			throw new ConfigurationException("Invalid local port number: " + localPort);
	}
}
