package org.bgp4j.net.bind;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class NextHopDTO {

	private NextHopType type;
	private byte[] binary;
	/**
	 * @return the type
	 */
	public NextHopType getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(NextHopType type) {
		this.type = type;
	}
	/**
	 * @return the binary
	 */
	public byte[] getBinary() {
		return binary;
	}
	/**
	 * @param binary the binary to set
	 */
	public void setBinary(byte[] binary) {
		this.binary = binary;
	}
	
}
