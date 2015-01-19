/**
 * 
 */
package org.bgp4j.net.attributes.bind;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.bgp4j.net.Origin;
import org.bgp4j.net.attributes.OriginPathAttribute;


/**
 * @author rainer
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class OriginDTO  {

	private Origin value;
	
	public OriginDTO() {}
	
	public OriginDTO(OriginPathAttribute pa) {
		setValue(pa.getOrigin());
	}

	/**
	 * @return the value
	 */
	public Origin getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(Origin value) {
		this.value = value;
	}

}
