/**
 * 
 */
package de.urb.quagga.netty.protocol;


/**
 * @author rainer
 *
 */
public class ZServIPv6RouteAddPacket extends ZServRouteAddPacket {
	
	public ZServIPv6RouteAddPacket(int protocolVersion) {
		super(protocolVersion, RoutingPacketType.IPv6);
	}
}
