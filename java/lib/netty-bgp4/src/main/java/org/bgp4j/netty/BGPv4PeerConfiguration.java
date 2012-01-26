package org.bgp4j.netty;

import java.net.InetSocketAddress;

/**
 * Configuration for a BGPv4 peer.
 *
 * The peer configuration <b>MUST</b> implement <code>equals</code> properly.
 * 
 * @author rainer
 *
 */
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

	/**
	 * retry interval for establishing a connection to the remote peer
	 * @return
	 */
	public abstract int getConnectRetryInterval();
}