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

	
	public static final int ZEBRA_INTERFACE_ADD = 1;
}
