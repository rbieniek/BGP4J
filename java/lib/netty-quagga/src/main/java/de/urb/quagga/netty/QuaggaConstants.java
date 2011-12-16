/**
 * 
 */
package de.urb.quagga.netty;

/**
 * @author rainer
 *
 */
public class QuaggaConstants {

	public static final int ZEBRA_PROTOCOL_VERSION = 0x1;
	public static final int ZEBRA_HEADER_MARKER = 0xff;
	public static final int INTERFACE_NAMSIZ = 20;
	public static final int IFINDEX_INTERNAL = 0;
	public static final int SIZEOF_IPV4_ADDRESS = 4;
	public static final int SIZEOF_IPV6_ADDRESS = 16;
	
	public static final int ZEBRA_INTERFACE_ACTIVE = (1 << 0);
	public static final int ZEBRA_INTERFACE_SUB = (1 << 1);
	public static final int ZEBRA_INTERFACE_LINKDETECTION = (1 << 2);
	public static final int ZEBRA_IFA_SECONDARY = (1 << 0);
	public static final int ZEBRA_IFA_PEER = (1 << 1);

	// route types
	public static final int ZEBRA_ROUTE_SYSTEM = 0;
	public static final int ZEBRA_ROUTE_KERNEL = 1;
	public static final int ZEBRA_ROUTE_CONNECT = 2;
	public static final int ZEBRA_ROUTE_STATIC = 3;
	public static final int ZEBRA_ROUTE_RIP = 4;
	public static final int ZEBRA_ROUTE_RIPNG = 5;
	public static final int ZEBRA_ROUTE_OSPF = 6;
	public static final int ZEBRA_ROUTE_OSPF6 = 7;
	public static final int ZEBRA_ROUTE_ISIS = 8;
	public static final int ZEBRA_ROUTE_BGP = 9;
	public static final int ZEBRA_ROUTE_HSLS = 10;
	public static final int ZEBRA_ROUTE_MAX = 11;
	
	// zebra message flags 
	public static final int ZEBRA_FLAG_INTERNAL = 0x01;
	public static final int ZEBRA_FLAG_SELFROUTE = 0x02;
	public static final int ZEBRA_FLAG_BLACKHOLE = 0x04;
	public static final int ZEBRA_FLAG_IBGP = 0x08;
	public static final int ZEBRA_FLAG_SELECTED = 0x10;
	public static final int ZEBRA_FLAG_CHANGED = 0x20;
	public static final int ZEBRA_FLAG_STATIC = 0x40;
	public static final int ZEBRA_FLAG_REJECT = 0x80;

	/* Zebra API message flag. */
	public static final int ZAPI_MESSAGE_NEXTHOP = 0x01;
	public static final int ZAPI_MESSAGE_IFINDEX = 0x02;
	public static final int ZAPI_MESSAGE_DISTANCE = 0x04;
	public static final int ZAPI_MESSAGE_METRIC = 0x08;

	// zebra commands
	public static final int ZEBRA_INTERFACE_ADD = 1;
	public static final int ZEBRA_INTERFACE_DELETE = 2;
	public static final int ZEBRA_INTERFACE_ADDRESS_ADD = 3;
	public static final int ZEBRA_INTERFACE_ADDRESS_DELETE = 4;
	public static final int ZEBRA_INTERFACE_UP = 5;
	public static final int ZEBRA_INTERFACE_DOWN = 6;
	public static final int ZEBRA_IPV4_ROUTE_ADD = 7;
	public static final int ZEBRA_IPV4_ROUTE_DELETE = 8;
	public static final int ZEBRA_IPV6_ROUTE_ADD = 9;
	public static final int ZEBRA_IPV6_ROUTE_DELETE = 10;
	
	// known packet sizes
	public static final int ZEBRA_INTERFACE_ADD_PKT_MIN = 49;
	public static final int ZEBRA_INTERFACE_DEL_PKT_MIN = 49;
	public static final int ZEBRA_INTERFACE_ADDRESS_ADD_PKT_MIN = 15;
	public static final int ZEBRA_INTERFACE_ADDRESS_ADD_PKT_MAX = 39;
	public static final int ZEBRA_INTERFACE_ADDRESS_DELETE_PKT_MIN = 15;
	public static final int ZEBRA_INTERFACE_ADDRESS_DELETE_PKT_MAX = 39;
	public static final int ZEBRA_INTERFACE_UP_PKT_SIZE = 49;
	public static final int ZEBRA_INTERFACE_DOWN_PKT_SIZE = 49;
	
	// OS-dependent address familix values
	public static final int AF_INET = 2;
	public static final int AF_INET6_BSD = 24;
	public static final int AF_INET6_FREEBSD = 26;
	public static final int AF_INET6_DARWIN = 30;
	public static final int AF_INET6_LINUX = 10;
	public static final int AF_INET6_SOLARIS = 26;
	public static final int AF_INET6_WINSOCK = 23;
}
