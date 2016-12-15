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
	private AggregatorDTO aggregator;
	private ASPathDTO asPath;
	private ClusterListDTO clusterList;
	private CommunityDTO community;
	private MultiExitDiscDTO multiExitDisc;
	private MultiProtocolReachableDTO multiProtocolReachable;
	private MultiProtocolUnreachableDTO multiProtocolUnreachable;
	private IPv4NextHopDTO nextHop;
	private OriginatorIDDTO originatorID;
	private OriginDTO origin;
	private UnknownDTO unknown;
	
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

	/**
	 * @return the aggregator
	 */
	public AggregatorDTO getAggregator() {
		return aggregator;
	}

	/**
	 * @param aggregator the aggregator to set
	 */
	public void setAggregator(AggregatorDTO aggregator) {
		this.aggregator = aggregator;
	}

	/**
	 * @return the asPath
	 */
	public ASPathDTO getAsPath() {
		return asPath;
	}

	/**
	 * @param asPath the asPath to set
	 */
	public void setAsPath(ASPathDTO asPath) {
		this.asPath = asPath;
	}

	/**
	 * @return the clusterList
	 */
	public ClusterListDTO getClusterList() {
		return clusterList;
	}

	/**
	 * @param clusterList the clusterList to set
	 */
	public void setClusterList(ClusterListDTO clusterList) {
		this.clusterList = clusterList;
	}

	/**
	 * @return the community
	 */
	public CommunityDTO getCommunity() {
		return community;
	}

	/**
	 * @param community the community to set
	 */
	public void setCommunity(CommunityDTO community) {
		this.community = community;
	}

	/**
	 * @return the multiExitDisc
	 */
	public MultiExitDiscDTO getMultiExitDisc() {
		return multiExitDisc;
	}

	/**
	 * @param multiExitDisc the multiExitDisc to set
	 */
	public void setMultiExitDisc(MultiExitDiscDTO multiExitDisc) {
		this.multiExitDisc = multiExitDisc;
	}

	/**
	 * @return the multiProtocolReachable
	 */
	public MultiProtocolReachableDTO getMultiProtocolReachable() {
		return multiProtocolReachable;
	}

	/**
	 * @param multiProtocolReachable the multiProtocolReachable to set
	 */
	public void setMultiProtocolReachable(
			MultiProtocolReachableDTO multiProtocolReachable) {
		this.multiProtocolReachable = multiProtocolReachable;
	}

	/**
	 * @return the multiProtocolUnReachable
	 */
	public MultiProtocolUnreachableDTO getMultiProtocolUnreachable() {
		return multiProtocolUnreachable;
	}

	/**
	 * @param multiProtocolUnReachable the multiProtocolUnReachable to set
	 */
	public void setMultiProtocolUnreachable(
			MultiProtocolUnreachableDTO multiProtocolUnReachable) {
		this.multiProtocolUnreachable = multiProtocolUnReachable;
	}

	/**
	 * @return the nextHop
	 */
	public IPv4NextHopDTO getNextHop() {
		return nextHop;
	}

	/**
	 * @param nextHop the nextHop to set
	 */
	public void setNextHop(IPv4NextHopDTO nextHop) {
		this.nextHop = nextHop;
	}

	/**
	 * @return the originatorID
	 */
	public OriginatorIDDTO getOriginatorID() {
		return originatorID;
	}

	/**
	 * @param originatorID the originatorID to set
	 */
	public void setOriginatorID(OriginatorIDDTO originatorID) {
		this.originatorID = originatorID;
	}

	/**
	 * @return the origin
	 */
	public OriginDTO getOrigin() {
		return origin;
	}

	/**
	 * @param origin the origin to set
	 */
	public void setOrigin(OriginDTO origin) {
		this.origin = origin;
	}

	/**
	 * @return the unknown
	 */
	public UnknownDTO getUnknown() {
		return unknown;
	}

	/**
	 * @param unknown the unknown to set
	 */
	public void setUnknown(UnknownDTO unknown) {
		this.unknown = unknown;
	}
}
