/**
 * 
 */
package de.urb.netty.bgp4.client;

import de.urb.netty.bgp4.BGPv4Configuration;

/**
 * @author rainer
 *
 */
public class BGPv4ClientConfiguration extends BGPv4Configuration {
	private int remoteBgpIdentitifer;
	private int remoteAutonomousSystem;

	/**
	 * @return the remoteBgpIdentitifer
	 */
	public int getRemoteBgpIdentitifer() {
		return remoteBgpIdentitifer;
	}

	/**
	 * @param remoteBgpIdentitifer the remoteBgpIdentitifer to set
	 */
	public void setRemoteBgpIdentitifer(int remoteBgpIdentitifer) {
		this.remoteBgpIdentitifer = remoteBgpIdentitifer;
	}

	/**
	 * @return the remoteAutonomousSystem
	 */
	public int getRemoteAutonomousSystem() {
		return remoteAutonomousSystem;
	}

	/**
	 * @param remoteAutonomousSystem the remoteAutonomousSystem to set
	 */
	public void setRemoteAutonomousSystem(int remoteAutonomousSystem) {
		this.remoteAutonomousSystem = remoteAutonomousSystem;
	}
	
}
