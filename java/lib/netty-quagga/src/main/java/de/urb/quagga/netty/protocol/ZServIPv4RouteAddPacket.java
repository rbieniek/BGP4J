/**
 * 
 */
package de.urb.quagga.netty.protocol;

/**
 * @author rainer
 *
 */
public class ZServIPv4RouteAddPacket extends ZServRouteAddPacket {
	
	public ZServIPv4RouteAddPacket(int protocolVersion) {
		super(protocolVersion, RoutingPacketType.IPv4);
	}
}
