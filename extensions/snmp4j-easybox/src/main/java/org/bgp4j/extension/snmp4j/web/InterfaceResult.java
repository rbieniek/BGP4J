/**
 * 
 */
package org.bgp4j.extension.snmp4j.web;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author rainer
 *
 */
@XmlRootElement(name="interface")
public class InterfaceResult {

	private String description;
	private int mtu;
	private int speed;
	private boolean adminUp;
	private boolean operUp;
	private long octetsIn;
	private long octetsOut;
	private String address;
	
	/**
	 * @return the description
	 */
	@XmlElement
	public String getDescription() {
		return description;
	}

	/**
	 * @return the mtu
	 */
	@XmlElement
	public int getMtu() {
		return mtu;
	}

	/**
	 * @return the speed
	 */
	@XmlElement
	public int getSpeed() {
		return speed;
	}
	
	/**
	 * @return the adminUp
	 */
	@XmlElement
	public boolean isAdminUp() {
		return adminUp;
	}

	/**
	 * @return the operUp
	 */
	@XmlElement
	public boolean isOperUp() {
		return operUp;
	}

	/**
	 * @return the octetsIn
	 */
	@XmlElement
	public long getOctetsIn() {
		return octetsIn;
	}

	/**
	 * @return the octetsOut
	 */
	@XmlElement
	public long getOctetsOut() {
		return octetsOut;
	}

	/**
	 * @return the address
	 */
	@XmlElement
	public String getAddress() {
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
	void setAddress(String address) {
		this.address = address;
	} 
	
	
}
