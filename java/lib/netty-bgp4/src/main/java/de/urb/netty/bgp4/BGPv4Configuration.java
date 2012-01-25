/**
 * 
 */
package de.urb.netty.bgp4;

import java.net.InetSocketAddress;

/**
 * @author rainer
 *
 */
public class BGPv4Configuration {

	private InetSocketAddress bgpServer;
	private int bgpIdentifier;
	private int holdTime;

	/**
	 * @return the bgpv4Server
	 */
	public InetSocketAddress getBgpv4Server() {
		return bgpServer;
	}

	/**
	 * @param bgpv4Server the bgpv4Server to set
	 */
	public void setBgpv4Server(InetSocketAddress bgpv4Server) {
		this.bgpServer = bgpv4Server;
	}

	/**
	 * @return the bgpIdentifier
	 */
	public int getBgpIdentifier() {
		return bgpIdentifier;
	}

	/**
	 * @param bgpIdentifier the bgpIdentifier to set
	 */
	public void setBgpIdentifier(int bgpIdentifier) {
		this.bgpIdentifier = bgpIdentifier;
	}

	/**
	 * @return the holdTime
	 */
	public int getHoldTime() {
		return holdTime;
	}

	/**
	 * @param holdTime the holdTime to set
	 */
	public void setHoldTime(int holdTime) {
		this.holdTime = holdTime;
	}
	
	
}
