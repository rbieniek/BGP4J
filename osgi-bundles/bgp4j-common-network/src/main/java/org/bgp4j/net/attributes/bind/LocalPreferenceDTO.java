/**
 * 
 */
package org.bgp4j.net.attributes.bind;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.bgp4j.net.attributes.LocalPrefPathAttribute;


/**
 * @author rainer
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class LocalPreferenceDTO  {

	private int value;
	
	public LocalPreferenceDTO() {}
	
	public LocalPreferenceDTO(LocalPrefPathAttribute pa) {
		setValue(pa.getLocalPreference());
	}

	/**
	 * @return the value
	 */
	public int getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(int value) {
		this.value = value;
	}

}
