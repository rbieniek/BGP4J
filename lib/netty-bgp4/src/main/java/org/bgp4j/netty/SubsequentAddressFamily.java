package org.bgp4j.netty;

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