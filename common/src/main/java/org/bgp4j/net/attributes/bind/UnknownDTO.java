/**
 * 
 */
package org.bgp4j.net.attributes.bind;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.bgp4j.net.attributes.UnknownPathAttribute;

/**
 * @author rainer
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class UnknownDTO {
	private int typeCode;
	private byte[] value;
	
	public UnknownDTO() {}
	
	public UnknownDTO(UnknownPathAttribute pa) {
		setTypeCode(pa.getTypeCode());
		setValue(pa.getValue());
	}
	
	/**
	 * @return the typeCode
	 */
	public int getTypeCode() {
		return typeCode;
	}
	/**
	 * @param typeCode the typeCode to set
	 */
	public void setTypeCode(int typeCode) {
		this.typeCode = typeCode;
	}
	/**
	 * @return the value
	 */
	public byte[] getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(byte[] value) {
		this.value = value;
	}

}
