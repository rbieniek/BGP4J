/**
 * 
 */
package de.urb.quagga.netty.protocol;

/**
 * @author rainer
 *
 */
public class ZServRouteAddPacket extends ZServRoutePacket {

	private int distance;
	private int metric;

	/**
	 * @param protocolVersion
	 * @param routingPacketType
	 */
	protected ZServRouteAddPacket(int protocolVersion,
			RoutingPacketType routingPacketType) {
		super(protocolVersion, routingPacketType);
	}

	/**
	 * @return the distance
	 */
	public int getDistance() {
		return distance;
	}

	/**
	 * @param distance the distance to set
	 */
	public void setDistance(int distance) {
		this.distance = distance;
	}

	/**
	 * @return the metric
	 */
	public int getMetric() {
		return metric;
	}

	/**
	 * @param metric the metric to set
	 */
	public void setMetric(int metric) {
		this.metric = metric;
	}

}
