/**
 * 
 */
package org.bgp4j.netty;

/**
 * @author rainer
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
	
	/** ROUTE-REFRESH capability (RFC 2918) */
	public static final int BGP_CAPABILITY_TYPE_ROUTE_REFRESH = 2;
	
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
	
}
