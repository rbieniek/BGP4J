/**
 * 
 */
package org.bgp4j.net.attributes.bind;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.bgp4j.net.attributes.ClusterListPathAttribute;

/**
 * @author rainer
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ClusterListDTO {

	private List<Integer> clusterIds = new LinkedList<Integer>();

	public ClusterListDTO() {}
	
	public ClusterListDTO(ClusterListPathAttribute pa) {
		setClusterIds(pa.getClusterIds());
	}

	/**
	 * @return the clusterIds
	 */
	public List<Integer> getClusterIds() {
		return clusterIds;
	}

	/**
	 * @param clusterIds the clusterIds to set
	 */
	public void setClusterIds(List<Integer> clusterIds) {
		this.clusterIds = clusterIds;
	}
}
