/**
 * 
 */
package de.urb.quagga.netty.protocol;


/**
 * @author rainer
 *
 */
public class ZServIPv4RouteDeletePacket extends ZServRoutePacket {
	
	public ZServIPv4RouteDeletePacket(int protocolVersion) {
		super(protocolVersion, RoutingPacketType.IPv4);
	}
}
