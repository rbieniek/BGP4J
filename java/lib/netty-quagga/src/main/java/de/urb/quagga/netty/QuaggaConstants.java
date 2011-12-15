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
	public static final int  IFINDEX_INTERNAL = 0;
	
	public static final int ZEBRA_INTERFACE_ACTIVE = (1 << 0);
	public static final int ZEBRA_INTERFACE_SUB = (1 << 1);
	public static final int ZEBRA_INTERFACE_LINKDETECTION = (1 << 2);
	public static final int ZEBRA_IFA_SECONDARY = (1 << 0);
	public static final int ZEBRA_IFA_PEER = (1 << 1);

	
	public static final int ZEBRA_INTERFACE_ADD = 1;
	public static final int ZEBRA_INTERFACE_DELETE = 2;
	public static final int ZEBRA_INTERFACE_ADDRESS_ADD = 3;
	public static final int ZEBRA_INTERFACE_ADDRESS_DELETE = 4;
	
	public static final int ZEBRA_INTERFACE_ADD_PKT_MIN = 49;
	public static final int ZEBRA_INTERFACE_DEL_PKT_MIN = 49;
	
	public static final int AF_INET = 2;
	public static final int AF_INET6_BSD = 24;
	public static final int AF_INET6_FREEBSD = 26;
	public static final int AF_INET6_DARWIN = 30;
	public static final int AF_INET6_LINUX = 10;
	public static final int AF_INET6_SOLARIS = 26;
	public static final int AF_INET6_WINSOCK = 23;
}
