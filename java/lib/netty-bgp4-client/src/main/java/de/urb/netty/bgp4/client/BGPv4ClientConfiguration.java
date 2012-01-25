/**
 * 
 */
package de.urb.netty.bgp4.client;

import java.net.InetSocketAddress;

/**
 * @author rainer
 *
 */
public class BGPv4ClientConfiguration {

	private InetSocketAddress bgpv4Server;

	/**
	 * @return the bgpv4Server
	 */
	public InetSocketAddress getBgpv4Server() {
		return bgpv4Server;
	}

	/**
	 * @param bgpv4Server the bgpv4Server to set
	 */
	public void setBgpv4Server(InetSocketAddress bgpv4Server) {
		this.bgpv4Server = bgpv4Server;
	}
	
	
}
