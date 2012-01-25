package de.urb.netty.bgp4;

import java.net.InetSocketAddress;

public interface BGPv4PeerConfiguration {

	/**
	 * @return the remoteBgpIdentitifer
	 */
	public abstract int getRemoteBgpIdentitifer();

	/**
	 * @return the remoteAutonomousSystem
	 */
	public abstract int getRemoteAutonomousSystem();

	/**
	 * @return the remotePeerAddress
	 */
	public abstract InetSocketAddress getRemotePeerAddress();

	/**
	 * @return the localBgpIdentifier
	 */
	public abstract int getLocalBgpIdentifier();

	/**
	 * @return the localAutonomousSystem
	 */
	public abstract int getLocalAutonomousSystem();

	/**
	 * @return the localHoldTime
	 */
	public abstract int getLocalHoldTime();

}