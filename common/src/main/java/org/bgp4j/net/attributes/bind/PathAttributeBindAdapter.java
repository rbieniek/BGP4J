/**
 * 
 */
package org.bgp4j.net.attributes.bind;

import java.net.Inet4Address;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.bgp4j.net.attributes.ASPathAttribute;
import org.bgp4j.net.attributes.AggregatorPathAttribute;
import org.bgp4j.net.attributes.AtomicAggregatePathAttribute;
import org.bgp4j.net.attributes.ClusterListPathAttribute;
import org.bgp4j.net.attributes.CommunityPathAttribute;
import org.bgp4j.net.attributes.LocalPrefPathAttribute;
import org.bgp4j.net.attributes.MultiExitDiscPathAttribute;
import org.bgp4j.net.attributes.MultiProtocolReachableNLRI;
import org.bgp4j.net.attributes.MultiProtocolUnreachableNLRI;
import org.bgp4j.net.attributes.NextHopPathAttribute;
import org.bgp4j.net.attributes.OriginPathAttribute;
import org.bgp4j.net.attributes.OriginatorIDPathAttribute;
import org.bgp4j.net.attributes.PathAttribute;
import org.bgp4j.net.attributes.UnknownPathAttribute;


/**
 * @author rainer
 *
 */
public class PathAttributeBindAdapter extends XmlAdapter<PathAttributeDTO, PathAttribute> {

	@Override
	public PathAttribute unmarshal(PathAttributeDTO dto) throws Exception {
		PathAttribute pa = null;
		
		switch(dto.getType()) {
		case AGGREGATOR:
			pa = new AggregatorPathAttribute(dto.getAggregator().getAsType(), 
					dto.getAggregator().getAsNumber(), 
					(Inet4Address)Inet4Address.getByAddress(dto.getAggregator().getAggregatorAddress()));
			break;
		case AS_PATH:
			pa = new ASPathAttribute(dto.getAsPath().getAsType(), 
					dto.getAsPath().getPathSegments());
			break;
		case ATOMIC_AGGREGATE:
			pa = new AtomicAggregatePathAttribute();
			break;
		case CLUSTER_LIST:
			pa = new ClusterListPathAttribute(dto.getClusterList().getClusterIds());
			break;
		case COMMUNITY:
			pa = new CommunityPathAttribute(dto.getCommunity().getCommunity(), 
					dto.getCommunity().getMembers());
			break;
		case MULTI_EXIT_DISC:
			pa = new MultiExitDiscPathAttribute(dto.getMultiExitDisc().getDiscriminator());
			break;
		case LOCAL_PREF:
			pa = new LocalPrefPathAttribute(dto.getLocalPreference().getValue());
			break;
		case MULTI_PROTOCOL_REACHABLE:
			pa = new MultiProtocolReachableNLRI(dto.getMultiProtocolReachable().getAddressFamily(),
					dto.getMultiProtocolReachable().getSubsequentAddressFamily(),
					dto.getMultiProtocolReachable().getNextHop(),
					dto.getMultiProtocolReachable().getNlris());
			break;
		case MULTI_PROTOCOL_UNREACHABLE:
			pa = new MultiProtocolUnreachableNLRI(dto.getMultiProtocolUnreachable().getAddressFamily(),
					dto.getMultiProtocolUnreachable().getSubsequentAddressFamily(),
					dto.getMultiProtocolUnreachable().getNlris());
			break;
		case NEXT_HOP:
			pa = new NextHopPathAttribute(dto.getNextHop().getValue());
			break;
		case ORIGIN:
			pa = new OriginPathAttribute(dto.getOrigin().getValue());
			break;
		case ORIGINATOR_ID:
			pa = new OriginatorIDPathAttribute(dto.getOriginatorID().getValue());
			break;
		case UNKNOWN:
			pa = new UnknownPathAttribute(dto.getUnknown().getTypeCode(), 
					dto.getUnknown().getValue());
			break;
		}
		
		if(pa != null) {
			pa.setOptional(dto.isOptional());
			pa.setPartial(dto.isPartial());
			pa.setTransitive(dto.isTransitive());
		}
		
		return pa;
	}

	@Override
	public PathAttributeDTO marshal(PathAttribute v) throws Exception {
		PathAttributeDTO dto = new PathAttributeDTO(v);
		
		switch(v.getType()) {
		case AGGREGATOR:
			dto.setAggregator(new AggregatorDTO((AggregatorPathAttribute)v));
			break;		
		case AS_PATH:
			dto.setAsPath(new ASPathDTO((ASPathAttribute)v));
			break;
		case ATOMIC_AGGREGATE:
			break;
		case CLUSTER_LIST:
			dto.setClusterList(new ClusterListDTO((ClusterListPathAttribute)v));
			break;
		case COMMUNITY:
			dto.setCommunity(new CommunityDTO((CommunityPathAttribute)v));
			break;
		case LOCAL_PREF:
			dto.setLocalPreference(new LocalPreferenceDTO((LocalPrefPathAttribute)v));
			break;
		case MULTI_EXIT_DISC:
			dto.setMultiExitDisc(new MultiExitDiscDTO((MultiExitDiscPathAttribute)v));
			break;
		case MULTI_PROTOCOL_REACHABLE:
			dto.setMultiProtocolReachable(new MultiProtocolReachableDTO((MultiProtocolReachableNLRI) v));
			break;
		case MULTI_PROTOCOL_UNREACHABLE:
			dto.setMultiProtocolUnreachable(new MultiProtocolUnreachableDTO((MultiProtocolUnreachableNLRI) v));
			break;
		case NEXT_HOP:
			dto.setNextHop(new IPv4NextHopDTO((NextHopPathAttribute) v));
			break;
		case ORIGIN:
			dto.setOrigin(new OriginDTO((OriginPathAttribute) v));
			break;
		case ORIGINATOR_ID:
			dto.setOriginatorID(new OriginatorIDDTO((OriginatorIDPathAttribute) v));
			break;
		case UNKNOWN:
			dto.setUnknown(new UnknownDTO((UnknownPathAttribute) v));
			break;
		}
		
		return dto;
	}

}
