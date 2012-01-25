/**
 * 
 */
package de.urb.netty.bgp4;

/**
 * @author rainer
 *
 */
public class BGPv4Constants {

	/** Port number assigned by IANA */
	public static final int BGP_PORT = 179;
	
	/** packet marker with all bits set to 1 (RFC 4271) */
	public static final int BGP_PACKET_MARKER_LENGTH = 16;
	
	/** BGPv4 packet header length (RFC 4271) */
	public static final int BGP_PACKET_HEADER_LENGTH = 19;
	
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
	
	/** KEEPALIVE packet size including header (RFC 4271) */
	public static final int BGP_PACKET_SIZE_KEEPALIVE = 19;
}
