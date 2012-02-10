/**
 *  Copyright 2012 Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  
 */
package org.bgp4j.netty;

/**
 * Constant value defined in various protocol RFC documents
 * 
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class BGPv4Constants {

	/**
	 * Address families as defined in RFC 1700
	 * @author rainer
	 *
	 */
	public enum AddressFamily {
		RESERVED,
		IPv4,
		IPv6,
		NSAP,
		HDLC,
		BBN1882,
		IEEE802,
		E163,
		E164,
		F69,
		X121,
		IPX,
		APPLETALK,
		DECNET4,
		BANYAN,
		RESERVED2;
		
		public int toCode() {
			switch(this) {
			case RESERVED:
				return 0;
			case IPv4:
				return 1;
			case IPv6:
				return 2;
			case NSAP:
				return 3;
			case HDLC:
				return 4;
			case BBN1882:
				return 5;
			case IEEE802:
				return 6;
			case E163:
				return 7;
			case E164:
				return 8;
			case F69:
				return 9;
			case X121:
				return 10;
			case IPX:
				return 11;
			case APPLETALK:
				return 12;
			case DECNET4:
				return 13;
			case BANYAN:
				return 14;
			case RESERVED2:
				return 65535;
			default:
				throw new IllegalArgumentException("unknown address family: " + this);
			}
		}
		
		public static AddressFamily fromCode(int code) {
			switch(code) {
			case 0:
				return RESERVED;
			case 1:
				return IPv4;
			case 2:
				return IPv6;
			case 3:
				return NSAP;
			case 4:
				return HDLC;
			case 5:
				return BBN1882;
			case 6:
				return IEEE802;
			case 7:
				return E163;
			case 8:
				return E164;
			case 9:
				return F69;
			case 10:
				return X121;
			case 11:
				return IPX;
			case 12:
				return APPLETALK;
			case 13:
				return DECNET4;
			case 14:
				return BANYAN;
			case 65535:
				return RESERVED2;
			default:
				throw new IllegalArgumentException("unknown address family code: " + code);
			}
		}
	}
	
	/**
	 * Subsequent address family as defined in RFC 2858
	 * 
	 * @author rainer
	 *
	 */
	public enum SubsequentAddressFamily {
		NLRI_UNICAST_FORWARDING,
		NLRI_MULTICAST_FORWARDING,
		NLRI_UNICAST_MULTICAST_FORWARDING;
		
		public int toCode() {
			switch(this) {
			case NLRI_UNICAST_FORWARDING:
				return 1;
			case NLRI_MULTICAST_FORWARDING:
				return 2;
			case NLRI_UNICAST_MULTICAST_FORWARDING:
				return 3;
			default:
				throw new IllegalArgumentException("Unknown subsequent address family: " + this);
			}
		}
		
		public static SubsequentAddressFamily fromCode(int code) {
			switch(code) {
			case 1:
				return NLRI_UNICAST_FORWARDING;
			case 2:
				return NLRI_MULTICAST_FORWARDING;
			case 3:
				return NLRI_UNICAST_MULTICAST_FORWARDING;
			default:
				throw new IllegalArgumentException("Unknown subsequent address family code: " + code);
			}
		}
	}
	
	/** Port number assigned by IANA */
	public static final int BGP_PORT = 179;
	
	/** supported protocol version */
	public static final int BGP_VERSION = 4;

	/** Transitive Autonomous System number for 4-byte AS number support (RFC 4893) */
	public static final int BGP_AS_TRANS = 23456;
	
	/** packet marker with all bits set to 1 (RFC 4271) */
	public static final int BGP_PACKET_MARKER_LENGTH = 16;
	
	/** BGPv4 packet header length (RFC 4271) */
	public static final int BGP_PACKET_HEADER_LENGTH = 19;
	
	/** BGPv4 capability header length (RFC 4271) */
	public static final int BGP_CAPABILITY_HEADER_LENGTH = 2;
	
	/** BGPv4 capability value length (RFC 4271) */
	public static final int BGP_CAPABILITY_MAX_VALUE_LENGTH = 255;
	
	/** Minimum packet length according to RFC 4271 */
	public static final int BGP_PACKET_MIN_LENGTH = BGP_PACKET_HEADER_LENGTH;

	/** Maximum packet length according to RFC 4271 */
	public static final int BGP_PACKET_MAX_LENGTH = 4096;

	/** OPEN packet type code (RFC 4271) */
	public static final int BGP_PACKET_TYPE_OPEN = 1;
	
	/** UPDATE packet type code (RFC 4271) */
	public static final int BGP_PACKET_TYPE_UPDATE = 2;
	
	/** NOTIFICATION packet type code (RFC 4271) */
	public static final int BGP_PACKET_TYPE_NOTIFICATION = 3;
	
	/** KEEPALIVE packet type code (RFC 4271) */
	public static final int BGP_PACKET_TYPE_KEEPALIVE = 4;
	
	/** REFRESH packet type code (RFC 2918) */
	public static final int BGP_PACKET_TYPE_ROUTE_REFRESH = 5;
	
	/** MULTIPROTOCOL capability (RFC 2858) */
	public static final int BGP_CAPABILITY_TYPE_MULTIPROTOCOL = 1;
	
	/** ROUTE-REFRESH capability (RFC 2918) */
	public static final int BGP_CAPABILITY_TYPE_ROUTE_REFRESH = 2;

	/** Outbound route filtering capability (RFC 5291) */
	public static final int BGP_CAPABILITY_TYPE_OUTBOUND_ROUTE_FILTERING = 3;

	/** Multiplte routes to destination capability (RFC 3107) */
	public static final int BGP_CAPABILITY_TYPE_MULTIPLE_ROUTES_TO_DESTINATION = 4;

	/** Extended next hop encoding capability (RFC 5549) */
	public static final int BGP_CAPABILITY_TYPE_EXTENDED_NEXT_HOP_ENCODING = 5;

	/** 4-byte Autonomous System numbers capability (RFC 4893) */
	public static final int BGP_CAPABILITY_TYPE_AS4_NUMBERS = 65;
	
	/** MULTIPROTOCOL capability length (RFC 2858) */
	public static final int BGP_CAPABILITY_LENGTH_MULTIPROTOCOL = 4;
	
	/** MULTIPROTOCOL capability length (RFC 4893) */
	public static final int BGP_CAPABILITY_LENGTH_AS4_NUMBERS = 4;
	
	/** Minimum OPEN packet size including header (RFC 4271) */
	public static final int BGP_PACKET_MIN_SIZE_OPEN = 29;
	
	/** Minimum NOTIFICATION packet size including header (RFC 4271) */
	public static final int BGP_PACKET_MIN_SIZE_NOTIFICATION = 21;
	
	/** Minimum NOTIFICATION packet size including header (RFC 4271) */
	public static final int BGP_PACKET_MIN_SIZE_UPDATE = 23;
	
	/** KEEPALIVE packet size including header (RFC 4271) */
	public static final int BGP_PACKET_SIZE_KEEPALIVE = 19;
	
	/** NOTIFICATION error code (RFC 4271) */
	public static final int BGP_ERROR_CODE_MESSAGE_HEADER = 1;
	
	/** NOTIFICATION error code (RFC 4271) */
	public static final int BGP_ERROR_CODE_OPEN = 2;
	
	/** NOTIFICATION error code (RFC 4271) */
	public static final int BGP_ERROR_CODE_UPDATE = 3;
	
	/** NOTIFICATION error code (RFC 4271) */
	public static final int BGP_ERROR_CODE_HOLD_TIMER_EXPIRED = 4;
	
	/** NOTIFICATION error code (RFC 4271) */
	public static final int BGP_ERROR_CODE_FINITE_STATE_MACHINE_ERROR = 5;
	
	/** NOTIFICATION error code (RFC 4271) */
	public static final int BGP_ERROR_CODE_CEASE = 6;
	
	/** PATH ATTRIBUTE ORIGIN type code (RFC 4271) */
	public static final int BGP_PATH_ATTRIBUTE_TYPE_ORIGIN = 1;
	
	/** PATH ATTRIBUTE AS PATH type code (RFC 4271) */
	public static final int BGP_PATH_ATTRIBUTE_TYPE_AS_PATH = 2;
	
	/** PATH ATTRIBUTE NEXT_HOP type code (RFC 4271) */
	public static final int BGP_PATH_ATTRIBUTE_TYPE_NEXT_HOP = 3;

	/** PATH ATTRIBUTE MULTI_EXIT_DISC type code (RFC 4271) */
	public static final int BGP_PATH_ATTRIBUTE_TYPE_MULTI_EXIT_DISC = 4;

	/** PATH ATTRIBUTE LOCAL_PREF type code (RFC 4271) */
	public static final int BGP_PATH_ATTRIBUTE_TYPE_LOCAL_PREF = 5;

	/** PATH ATTRIBUTE ATOMIC_AGGREGATE type code (RFC 4271) */
	public static final int BGP_PATH_ATTRIBUTE_TYPE_ATOMIC_AGGREGATE = 6;

	/** PATH ATTRIBUTE AGGREGATOR type code (RFC 4271) */
	public static final int BGP_PATH_ATTRIBUTE_TYPE_AGGREGATOR = 7;
	
	/** PATH ATTRIBUTE COMMUNITIES type code (RFC 1997) */
	public static final int BGP_PATH_ATTRIBUTE_TYPE_COMMUNITIES = 8;
	
	/** PATH ATTRIBUTE ORIGINATOR_ID type code (RFC 4456) */
	public static final int BGP_PATH_ATTRIBUTE_TYPE_ORIGINATOR_ID = 9;
	
	/** PATH ATTRIBUTE CLUSTER_LIST type code (RFC 4456) */
	public static final int BGP_PATH_ATTRIBUTE_TYPE_CLUSTER_LIST = 10;
	
	/** PATH ATTRIBUTE AGGREGATOR type code (RFC 4760) */
	public static final int BGP_PATH_ATTRIBUTE_TYPE_MP_REACH_NLRI = 14;
	
	/** PATH ATTRIBUTE AGGREGATOR type code (RFC 4760) */
	public static final int BGP_PATH_ATTRIBUTE_TYPE_MP_UNREACH_NLRI = 15;
	
	/** PATH ATTRIBUTE AS PATH type code (RFC 4893) */
	public static final int BGP_PATH_ATTRIBUTE_TYPE_AS4_PATH = 17;

	/** PATH ATTRIBUTE AGGREGATOR type code (RFC 4893) */
	public static final int BGP_PATH_ATTRIBUTE_TYPE_AS4_AGGREGATOR = 18;
	
	/** PATH ATTRIBUTE FLAG for OPTIONAL bit (based on 16 bit flags / type code value) */
	public static  final int BGP_PATH_ATTRIBUTE_OPTIONAL_BIT = 1<<15;
	
	/** PATH ATTRIBUTE FLAG for TRANSITIVE bit (based on 16 bit flags / type code value) */
	public static  final int BGP_PATH_ATTRIBUTE_TRANSITIVE_BIT = 1<<14;

	/** PATH ATTRIBUTE FLAG for PARTIAL bit (based on 16 bit flags / type code value) */
	public static  final int BGP_PATH_ATTRIBUTE_PARTIAL_BIT = 1<<13;

	/** PATH ATTRIBUTE FLAG for EXTENDED_LENGTH bit (based on 16 bit flags / type code value) */
	public static  final int BGP_PATH_ATTRIBUTE_EXTENDED_LENGTH_BIT = 1<<12;
	
	/** PATH ATTRIBUTE type code mask (based on 16 bit flags / type code value) */
	public static  final int BGP_PATH_ATTRIBUTE_TYPE_MASK = 0x00ff;

	/** OPEN packet AUTH parameter type (RFC 5492) */
	public static final int BGP_OPEN_PARAMETER_TYPE_AUTH = 1;
	
	/** OPEN packet CAPABILITY parameter type (RFC 5492) */
	public static final int BGP_OPEN_PARAMETER_TYPE_CAPABILITY = 2;
	
	/** Well-known BGP community value NO_EXPORT (RFC 1997) */
	public static final int BGP_COMMUNITY_NO_EXPORT = 0xFFFFFF01;
	
	/** Well-known BGP community value NO_ADVERTISE (RFC 1997) */
	public static final int BGP_COMMUNITY_NO_ADVERTISE = 0xFFFFFF02;
	
	/** Well-known BGP community value NO_EXPORT_SUBCONFED (RFC 1997) */
	public static final int BGP_COMMUNITY_NO_EXPORT_SUBCONFED = 0xFFFFFF03;
	
	/** Address prefix based outbound route filter type (RFC 5292) */
	public static final int BGP_OUTBOUND_ROUTE_FILTER_TYPE_ADDRESS_PREFIX_BASED = 64;
}