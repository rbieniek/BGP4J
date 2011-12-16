/**
 * 
 */
package de.urb.quagga.netty.protocol;


/**
 * @author rainer
 *
 */
public class ZServIPv6RouteDeletePacket extends ZServRoutePacket {
	
	public ZServIPv6RouteDeletePacket(int protocolVersion) {
		super(protocolVersion, RoutingPacketType.IPv6);
	}
}
