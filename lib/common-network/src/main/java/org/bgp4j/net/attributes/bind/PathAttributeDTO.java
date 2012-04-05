/**
 * 
 */
package org.bgp4j.net.attributes.bind;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.bgp4j.net.attributes.PathAttribute;
import org.bgp4j.net.attributes.PathAttributeType;

/**
 * @author rainer
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class PathAttributeDTO {

	private PathAttributeType type;
	private boolean optional;
	private boolean transitive;
	private boolean partial;
	
	private LocalPreferenceDTO localPreference;
	
	public PathAttributeDTO() {}
	
	public PathAttributeDTO(PathAttribute pa) {
		setType(pa.getType());
		setOptional(pa.isOptional());
		setTransitive(pa.isTransitive());
		setPartial(pa.isPartial());
	}
	
	/**
	 * @return the type
	 */
	public PathAttributeType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(PathAttributeType type) {
		this.type = type;
	}

	/**
	 * @return the optional
	 */
	public boolean isOptional() {
		return optional;
	}

	/**
	 * @param optional the optional to set
	 */
	public void setOptional(boolean optional) {
		this.optional = optional;
	}

	/**
	 * @return the transitive
	 */
	public boolean isTransitive() {
		return transitive;
	}

	/**
	 * @param transitive the transitive to set
	 */
	public void setTransitive(boolean transitive) {
		this.transitive = transitive;
	}

	/**
	 * @return the partial
	 */
	public boolean isPartial() {
		return partial;
	}

	/**
	 * @param partial the partial to set
	 */
	public void setPartial(boolean partial) {
		this.partial = partial;
	}

	/**
	 * @return the localPreference
	 */
	public LocalPreferenceDTO getLocalPreference() {
		return localPreference;
	}

	/**
	 * @param localPreference the localPreference to set
	 */
	public void setLocalPreference(LocalPreferenceDTO localPreference) {
		this.localPreference = localPreference;
	}
}
