/**
 * 
 */
package org.bgp4j.net.attributes.bind;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.bgp4j.net.ASType;
import org.bgp4j.net.PathSegment;
import org.bgp4j.net.attributes.ASPathAttribute;

/**
 * @author rainer
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ASPathDTO {

	private ASType asType;
	private List<PathSegment> pathSegments = new LinkedList<PathSegment>(); 

	public ASPathDTO() {}
	
	public ASPathDTO(ASPathAttribute pa) {
		setAsType(pa.getAsType());
		setPathSegments(pa.getPathSegments());
	}

	/**
	 * @return the asType
	 */
	public ASType getAsType() {
		return asType;
	}

	/**
	 * @param asType the asType to set
	 */
	public void setAsType(ASType asType) {
		this.asType = asType;
	}

	/**
	 * @return the pathSegments
	 */
	public List<PathSegment> getPathSegments() {
		return pathSegments;
	}

	/**
	 * @param pathSegments the pathSegments to set
	 */
	public void setPathSegments(List<PathSegment> pathSegments) {
		this.pathSegments = pathSegments;
	}
}
