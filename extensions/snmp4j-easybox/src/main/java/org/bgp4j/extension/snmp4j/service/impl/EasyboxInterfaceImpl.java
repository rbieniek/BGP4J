/**
 * 
 */
package org.bgp4j.extension.snmp4j.service.impl;

import java.net.Inet4Address;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.bgp4j.extension.snmp4j.service.EasyboxInterface;

/**
 * @author rainer
 *
 */
public class EasyboxInterfaceImpl implements EasyboxInterface {

	private String description;
	private int mtu;
	private int speed;
	private boolean adminUp;
	private boolean operUp;
	private long octetsIn;
	private long octetsOut;
	private Inet4Address address;
	
	public EasyboxInterfaceImpl() {}
	
	public EasyboxInterfaceImpl(EasyboxInterface src) {
		setDescription(src.getDescription());
		setMtu(src.getMtu());
		setSpeed(src.getSpeed());
		setAdminUp(src.isAdminUp());
		setOperUp(src.isOperUp());
		setOctetsIn(src.getOctetsIn());
		setOctetsOut(src.getOctetsOut());
		setAddress(src.getAddress());
	}
	
	/**
	 * @return the description
	 */
	@Override
	public String getDescription() {
		return description;
	}
	/**
	 * @return the mtu
	 */
	@Override
	public int getMtu() {
		return mtu;
	}
	/**
	 * @return the speed
	 */
	@Override
	public int getSpeed() {
		return speed;
	}
	/**
	 * @return the adminUp
	 */
	@Override
	public boolean isAdminUp() {
		return adminUp;
	}
	/**
	 * @return the operUp
	 */
	@Override
	public boolean isOperUp() {
		return operUp;
	}
	/**
	 * @return the octetsIn
	 */
	@Override
	public long getOctetsIn() {
		return octetsIn;
	}
	/**
	 * @return the octetsOut
	 */
	@Override
	public long getOctetsOut() {
		return octetsOut;
	}
	/**
	 * @return the address
	 */
	@Override
	public Inet4Address getAddress() {
		return address;
	}
	/**
	 * @param description the description to set
	 */
	void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @param mtu the mtu to set
	 */
	void setMtu(int mtu) {
		this.mtu = mtu;
	}
	/**
	 * @param speed the speed to set
	 */
	void setSpeed(int speed) {
		this.speed = speed;
	}
	/**
	 * @param adminUp the adminUp to set
	 */
	void setAdminUp(boolean adminUp) {
		this.adminUp = adminUp;
	}
	/**
	 * @param operUp the operUp to set
	 */
	void setOperUp(boolean operUp) {
		this.operUp = operUp;
	}
	/**
	 * @param octetsIn the octetsIn to set
	 */
	void setOctetsIn(long octetsIn) {
		this.octetsIn = octetsIn;
	}
	/**
	 * @param octetsOut the octetsOut to set
	 */
	void setOctetsOut(long octetsOut) {
		this.octetsOut = octetsOut;
	}
	/**
	 * @param address the address to set
	 */
	void setAddress(Inet4Address address) {
		this.address = address;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return (new HashCodeBuilder())
				.append(getMtu())
				.append(getOctetsIn())
				.append(getOctetsOut())
				.append(getSpeed())
				.append(getAddress())
				.append(isAdminUp())
				.append(isOperUp())
				.toHashCode();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof EasyboxInterface))
			return false;
		
		EasyboxInterface o = (EasyboxInterface)obj;
		
		return (new EqualsBuilder())
				.append(getMtu(), o.getMtu())
				.append(getOctetsIn(), o.getOctetsIn())
				.append(getOctetsOut(), o.getOctetsOut())
				.append(getSpeed(), o.getSpeed())
				.append(getAddress(), o.getAddress())
				.append(isAdminUp(), o.isAdminUp())
				.append(isOperUp(), o.isOperUp())
				.isEquals();
	} 
		
	@Override
	public boolean isChanged(EasyboxInterface o) {
		if(o == null)
			return true;
		
		return !((new EqualsBuilder())
				.append(getAddress(), o.getAddress())
				.append(isAdminUp(), o.isAdminUp())
				.append(isOperUp(), o.isOperUp())
				.isEquals());
	} 
}
