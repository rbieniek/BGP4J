package org.bgp4j.net;

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